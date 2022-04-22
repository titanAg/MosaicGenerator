import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageResizer {
	private static File inputDir = new File("NewImages2");
	private static String outputDir = "output";
	private static int newWidth = 80;
	private static int newHeight = 60;
	public static void main(String[] args) throws IOException{
		File[] files = inputDir.listFiles();
		int count = 0;
		for (File file : files) {
			BufferedImage img = ImageIO.read(file);
			Image tmp = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
			 BufferedImage dimg = new BufferedImage(newWidth, newHeight, BufferedImage.SCALE_DEFAULT);
			    Graphics2D g2d = dimg.createGraphics();
			    g2d.drawImage(tmp, 0, 0, null);
			    g2d.dispose();
			    count++;
			ImageIO.write(dimg,"jpg",new File(outputDir + "/output" + count + ".jpg"));
		}
	}
}
