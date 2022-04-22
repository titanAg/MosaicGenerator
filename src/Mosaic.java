// Kyle Orcutt - Mosiac image generator
// Produces mosaic image for input image in res dir
// Mosiac tiles are analyzed on a pixel basis to find the closest match (tilesDir)
// The mosaics are super cool - zoom in a take a look at the output images!

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;

public class Mosaic {
	private static int tileWidth = 80;
	private static int tileHeight = 80;
	private static int scale = 20;
	private static int inputCount = 0;
	public static void main(String[] args) throws IOException {
		System.out.println("Reading tiles...");
		ArrayList<Tile> tileImages = getImagesFromTiles(new File("tilesDir"));
		System.out.println("Read the base image");
		File base = new File("res/input/simpsons.jpg");
		BufferedImage img = ImageIO.read(base);
		ArrayList<Tile> inputParts = getImagesFromInput(img);
		ArrayList<Tile> outputParts = new ArrayList<Tile>();
		int count = 0;
		
		for (Tile inputPart : inputParts){
			Tile outputPart = match(inputPart, tileImages);
			outputParts.add(new Tile(outputPart.image,inputPart.x,inputPart.y));
			count++;
			System.out.println("Files left: " + (inputCount - count));
		}
		System.out.println("Writing to file");
		BufferedImage image = new BufferedImage(img.getWidth()*scale, img.getHeight()*scale, BufferedImage.TYPE_3BYTE_BGR);
		for (Tile outputPart : outputParts) {
				BufferedImage imagePart = image.getSubimage(outputPart.x*scale,outputPart.y*scale,tileWidth,tileHeight);
				imagePart.setData(outputPart.image.getData());			
		}
		ImageIO.write(image,"jpg",new File("res/output/test.jpg"));
		System.out.println("Finished");
	}

	private static ArrayList<Tile> getImagesFromTiles(File tilesDir) throws IOException {
		ArrayList<Tile> tileImages = new ArrayList<Tile>();
		File[] files = tilesDir.listFiles();
		int tileCount = 0;
		for (int i = 0; i < files.length; i++) {
			BufferedImage img = ImageIO.read(files[i]);
			if (img != null) {
				tileCount++;
				System.out.println("Reading tile " + tileCount + " of " + files.length);
				tileImages.add(new Tile(img,0,0));
			} else {
				System.err.println("null image for file " + files[i].getName());
			}
		}
		return tileImages;
	}
	
	private static ArrayList<Tile> getImagesFromInput(BufferedImage input) throws IOException {
		ArrayList<Tile> inputParts = new ArrayList<Tile>();
		int height = input.getHeight();
		int width = input.getWidth();
		
		for (int x = 0; x <= width-tileWidth/scale; x+=tileWidth/scale) {
			for (int y = 0; y <= height-tileHeight/scale; y+=tileHeight/scale) {
				BufferedImage imageSeg = input.getSubimage(x, y, tileWidth/scale, tileHeight/scale);
				inputParts.add(new Tile(imageSeg,x,y));				
				inputCount++;
			}
		}
		return inputParts;
	}
	
	private static Tile match(Tile part, ArrayList<Tile> tiles) {
		Tile matchTile = null;
		int average = -1;
		int bestAverage = -1;		
		for (Tile tile : tiles) {
			average = 255 * 3 - (Math.abs(part.averageRed - tile.averageRed) + Math.abs(part.averageGreen - tile.averageGreen) + 
					Math.abs(part.averageBlue - tile.averageBlue));
			if (average > bestAverage) {
				bestAverage = average;
				matchTile = tile;
			}
		}
		return matchTile;
	}	
}

class Tile {
	public int averageRed;
	public int averageGreen;
	public int averageBlue;
	public BufferedImage image;
	public int x;
	public int y;

	Tile(BufferedImage i,int xt,int yt) {
		image = i;
		x = xt;
		y = yt;
		calculateAverage();
		//System.out.println("Average RGB " + averageRed + ", " + averageGreen + ", " + averageBlue);		
	}

	private void calculateAverage() {
		int width = image.getWidth();
		int height = image.getHeight();
		long red = 0, green = 0, blue = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color pixel = new Color(image.getRGB(x, y));
				red += pixel.getRed();
				green += pixel.getGreen();
				blue += pixel.getBlue();
			}
		}
		int numPixels = width * height;
		averageRed = (int) (red / numPixels);
		averageGreen = (int) (green / numPixels);
		averageBlue = (int) (blue / numPixels);
	}
}
