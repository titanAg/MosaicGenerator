// Kyle Orcutt - Web scroller to download repo of image tiles
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class URLImageDownloader {
	private static String outputDir = "output2";
	private static int newWidth = 80;
	private static int newHeight = 60;
    private static int count = 0;
	public static void main(String[] args) throws IOException, BadLocationException{
		
		System.setProperty("http.agent", "Chrome");
		String webUrl = "https://stocksnap.io/search/nature";
		URL url = new URL(webUrl);
		URLConnection connection = url.openConnection();
		InputStream is = connection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		HTMLEditorKit htmlKit = new HTMLEditorKit();
		HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
		htmlKit.read(br, htmlDoc, 0);

		for (HTMLDocument.Iterator iterator = htmlDoc.getIterator(HTML.Tag.A); iterator.isValid(); iterator.next()) {
		    AttributeSet attributes = iterator.getAttributes();
		    String imgSrc = (String) attributes.getAttribute(HTML.Attribute.HREF);

		    System.out.println(imgSrc);
		    if (imgSrc != null && ((imgSrc.toLowerCase().contains(".jpg") || (imgSrc.contains(".png")) || (imgSrc.contains(".jpeg")) || (imgSrc.contains(".bmp")) || (imgSrc.contains(".ico"))))) {
		        try {
		    	    count++;
		        	print("downloading image: " + count);
		            downloadImage(webUrl, imgSrc);
		        } catch (IOException ex) {
		            //System.out.println(ex.getMessage());
		        }
		    }
		    
		}
	}
		
	
	private static void downloadImage(String url, String imgSrc) throws IOException {
	    BufferedImage image = null;
	    try {
	        if (!(imgSrc.startsWith("https"))) {
	            url = url + imgSrc;
	        } else {
	            url = imgSrc;
	        }
	        imgSrc = imgSrc.substring(imgSrc.lastIndexOf("/") + 1);
	        String imageFormat = null;
	        imageFormat = imgSrc.substring(imgSrc.lastIndexOf(".") + 1);
	        String imgPath = null;
	        imgPath = "output2/" + imgSrc + "";
	        URL imageUrl = new URL(url);
	        image = ImageIO.read(imageUrl);
	        if (image != null) {
	        	print("writing to " + outputDir + "/output" + count + ".jpg");
	            File file = new File(imgPath);
	        	Image tmp = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
				 BufferedImage dimg = new BufferedImage(newWidth, newHeight, BufferedImage.SCALE_DEFAULT);
				    Graphics2D g2d = dimg.createGraphics();
				    g2d.drawImage(tmp, 0, 0, null);
				    g2d.dispose();
	            ImageIO.write(dimg,"jpg",new File(outputDir + "/output" + count + ".jpg"));
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }

	}
	private static void print(String printStatement) {
		System.out.println(printStatement);
	}
}
