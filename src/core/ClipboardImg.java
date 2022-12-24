package core;

import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.*; 
import java.awt.datatransfer.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.Graphics;

public class ClipboardImg
{
	JFrame window = new JFrame("Screen");
	ScreenPanel ScreenMap;
	
	ClipboardImg(String[] args) throws Exception {
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setUndecorated(false);
		
		System.err.println("usage: java clipimg [filename]");
        String outputfile = "E:\\1.png";
        
        if(args.length > 0) outputfile = args[0];
        copyTo(outputfile);
	}
	
    public static void main(String[] args) {
        
    	try {
    		
			new ClipboardImg(args);
			
		} catch (Exception e) { e.printStackTrace(); }
    }
 
    int copyTo(String filename) throws Exception {
    	
        Transferable content = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        
      /*if(content == null) {
            System.err.println("error: nothing found in clipboard");
            return 1;
        }
        
        if(!content.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            System.err.println("error: no image found in clipboard");
            return 2;
        }*/
        
       	//411, 72; Toolkit.getDefaultToolkit().getScreenSize())
        
        BufferedImage img = (BufferedImage) new Robot().createScreenCapture(new Rectangle(0, 0, 411, 72)); //(BufferedImage) content.getTransferData(DataFlavor.imageFlavor);
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        
        String ext = ext(filename);
        
        if(ext == null) {
            ext = "png";
            filename += "." + ext;
        }
        
        File outfile = new File(filename);
        ImageIO.write(img, ext, outfile);
        ImageIO.write(img, ext, bos);
        
        int width = img.getWidth(), height = img.getHeight();
        
        System.err.println("image copied to: " + outfile.getAbsolutePath());
        System.err.println("Screen width: " + width + ", Screenshot height: " + height);
        
        ScreenMap = new ScreenPanel(img);
        window.setSize(width, height);
        window.add(ScreenMap);
        
        window.setVisible(true);
        return 0;
    }
    
    static String ext(String filename) {
    	
        int pos = filename.lastIndexOf('.') + 1;
        if(pos == 0 || pos >= filename.length()) return null;
        return filename.substring(pos);
    }
    
    class ScreenPanel extends JPanel {
    	
    	BufferedImage img;
    	Graphics G;
    	int height, width;
    	
    	ScreenPanel(BufferedImage img) {
    		
    		this.img = img;
    		height = img.getHeight();
    		width = img.getWidth();
    	}
    	
    	@Override
    	public void paintComponent(Graphics G) {
    		
    		G.drawImage(img, 0, 0, this);
    		
    		/*
    		for(int i = 0, j = 0; j < height; i++) {
    			int[] chanals = new int[4];
    			map.getPixel(i,j, chanals); 
    			
    			G.setColor(new Color(chanals[0], chanals[1], chanals[2], chanals[3]));
    			G.fillRect(i * 2, j * 2, 2 , 2);
    			if(i == width - 1) {i = -1; j++;}
    		}*/
    	}
    }
    
    class Recognizer {
    	
    	WritableRaster inputRaster;
    	ArrayList<WritableRaster> zoneList = new ArrayList<WritableRaster>();
    	
    	Recognizer(BufferedImage img) {
    		inputRaster = img.getRaster();
    	}
    	
    	byte[][] recordZone(byte[][] unrecognizedZone, int startX, int startY) {
    		
    		int zoneMarker = getZoneMarker(unrecognizedZone);
    		int cycles = 0;
    		boolean flag = true;
    		byte[][] recognizedZone = new byte[inputRaster.getWidth()][inputRaster.getHeight()];
    		byte[][] exportingZone = new byte[inputRaster.getWidth()][inputRaster.getHeight()];
    		
    		// 0 - не проверенный пиксель, 1 - пиксель добавленный в список, 2 - недоступный пиксель
    		// проверка идет по принципу клеточного автомата, зона распростараняется на пиксели которые подходят по тону к начальному.
    		
    		for(int i = 0, j = 0 ; j < unrecognizedZone.length ; i++) {
    			
    			int up, down, right, left;
    			
    			try {
	    				int[] probe = new int[4];
	    				inputRaster.getPixel(i, j - 1, probe);
	    				up = Math.round((probe[0] + probe[1] + probe[2] + probe[4]) / 4);
    			} catch(IndexOutOfBoundsException Ex) { up = -1; }
    			
    			try {
    				int[] probe = new int[4];
    				inputRaster.getPixel(i, j + 1, probe);
    				down = Math.round((probe[0] + probe[1] + probe[2] + probe[4]) / 4);
    			} catch(IndexOutOfBoundsException Ex) { down = -1; }
    			
    			try {
    				int[] probe = new int[4];
    				inputRaster.getPixel(i + 1, j, probe);
    				right = Math.round((probe[0] + probe[1] + probe[2] + probe[4]) / 4);
    			} catch(IndexOutOfBoundsException Ex) { right = -1; }
    			
    			try {
    				int[] probe = new int[4];
    				inputRaster.getPixel(i - 1, j, probe);
    				left = Math.round((probe[0] + probe[1] + probe[2] + probe[4]) / 4);
    			} catch(IndexOutOfBoundsException Ex) { left = -1; }
    			
    			if(up > zoneMarker - 10 && up < zoneMarker + 10 && up != -1) {
    				
    			}
    		}
    		
    		if (cycles == 0) return null;
    		else return exportingZone;
    	}
    	
    	int getZoneMarker(byte[][] zone) {
    		
    		int probe[] = new int[4];
    		
    		for(int i = 0, j = 0; j < zone.length; i++) {
    			
    			if(zone[i][j] == 0) {
    				inputRaster.getPixel(i, j, probe);
    				return Math.round((probe[0] + probe[1] + probe[2] + probe[4]) / 4);
    			}
    		}
    		
    		return -1;
    	}
    	
    	class SymbolZone {
    		
    		final ZonePixel startingPixel;
    		ArrayDeque<ZonePixel> testingPixels = new ArrayDeque<ZonePixel>();
    		
    		SymbolZone(ZonePixel startingPixel) {
    			this.startingPixel = startingPixel;
    		}
    		
    	}
    	
    	class ZonePixel {
    		
    		int posX, posY;
    		int Red, Green, Blue, Alpha;
    		
    		ZonePixel() {
    			
    		}
    	}
    }
}
