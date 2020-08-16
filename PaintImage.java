package com.pkg;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class PaintImage extends JFrame{
	
	private JLabel imageView;
	
	private Mat image;
	private Mat tempImage;
	private byte[] imageData;
	private Point origin;
	
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem saveMenu;
	public PaintImage() {
		setLayout(null);
		
		viewSetup();
		
		image = Imgcodecs.imread("images/opendv-test.jpg");
		loadImage(image);
		imageView.addMouseListener(new MouseAdapter() {
 
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				origin = new Point(e.getX(), e.getY());
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				image = tempImage.clone();
			}
			
		});
		
		imageView.addMouseMotionListener(new MouseAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				super.mouseDragged(e);
				tempImage = image.clone();
				final Point point = new Point(e.getX(),e.getY());
				
				Imgproc.rectangle(tempImage, origin, point, new Scalar(0, 0, 255), 2);
				
//				Imgproc.line(tempImage, origin, point, new Scalar(0,255,0), 1);
//				
//				Imgproc.circle(tempImage, origin, 10, new Scalar(255,0,0),1);
				
				loadImage(tempImage);
				
			}
			
		});
		setSize(640,480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void loadImage(Mat image) {
		
		MatOfByte mob = new MatOfByte();
		Imgcodecs.imencode(".jpg", image, mob);
		
		imageData = mob.toArray();
		
		ImageIcon icon = new ImageIcon(imageData);
		imageView.setIcon(icon);
	}

	
	private void viewSetup() {
		
		imageView = new JLabel();
		imageView.setBounds(0, 20, 640,480);
		add(imageView);
		
		menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 640, 20);
		menu = new JMenu("file");
		saveMenu = new JMenuItem("save");
		menu.add(saveMenu);
		menuBar.add(menu);
		add(menuBar);
		
		saveMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Imgcodecs.imwrite("images/out.jpg", image);
				System.out.println("write success");
				
			}
		});
		
		
	}

	public static void main(String[] args) {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				PaintImage image = new PaintImage();
			}
		});
	}
}