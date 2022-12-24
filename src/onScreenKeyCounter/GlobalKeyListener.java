package onScreenKeyCounter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

import javax.swing.*;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseMotionListener;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.dispatcher.SwingDispatchService;

public class GlobalKeyListener extends JFrame implements Runnable {
	
	Thread thListener = new Thread(this, "listener");
	NativeListener listener;
	int counter = 0;
	JLabel label = new JLabel("");
	
	public static void main(String args[]) {
		new GlobalKeyListener();
	}
	
	GlobalKeyListener() {
		
		thListener.start();
		GlobalScreen.setEventDispatcher(new SwingDispatchService());
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GlobalScreen.addNativeKeyListener(listener);
		GlobalScreen.addNativeMouseMotionListener(listener);
		
		this.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		this.setUndecorated(true);
		
		this.setBackground(new Color(0, 0, 0, 0));

		try {
		     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		     if(new File("Listener_lib/ExocetFont1.ttf").exists())
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Listener_lib/ExocetFont1.ttf")));
		     else ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/ExocetFont1.ttf")));
		} catch (IOException|FontFormatException e) {
		     //Handle exception
		}
		
		label.setFont(new Font("Exocet", 0, 144));
		label.setPreferredSize(new Dimension(200,400));
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		label.setHorizontalTextPosition(SwingConstants.CENTER);
		label.setVerticalTextPosition(SwingConstants.TOP);
		
		this.add(label);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setVisible(true);
		
	}
	
	public void run() {
		listener = new NativeListener();
	}
	
	class GPanel extends JPanel implements Runnable{
		
		
		GPanel() {
			
		}
		
		ArrayDeque<Point2D> points = new ArrayDeque<Point2D>(20);
		
		@Override
		public void paintComponent(Graphics G) {
			
		}
		
		public void addPointToList(Point2D point) {
			
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	}
	
	class Point2D {
		
	}
	
	class NativeListener implements NativeKeyListener, NativeMouseMotionListener {
		
		boolean shiftPressed, ctrlpressed, menuPressed;
		
		NativeListener() {
			
		}
		
		@Override
		public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
		    if(nativeKeyEvent.getRawCode() == 107) {
		    	counter++;
		    	label.setText(counter + "");	
		    } else {
		    	label.setText(nativeKeyEvent.getRawCode() + "");
		    }
		}
		@Override
		public void nativeMouseMoved(NativeMouseEvent event) {
			
		}
	}
}


