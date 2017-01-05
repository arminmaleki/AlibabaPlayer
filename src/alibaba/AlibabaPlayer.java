package alibaba;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


import alibaba.synth.Synth1;
import alibaba.synth.instrument1;
import alibaba.synth.percussion;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.ugens.Clock;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.TapIn;
import net.beadsproject.beads.ugens.TapOut;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.TextField;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import java.awt.Font;

public class AlibabaPlayer {
    private static AudioContext ac= new AudioContext();
    public static JPanel panel;
    private Gain MasterGain=new Gain(ac,1,(float)0.8);
   private Glide toneGl=new Glide(ac, 0, 1000);
   private Glide volumeGl=new Glide(ac, (float) 0.5, 30);
   public static Glide echodelayGl=new Glide(ac, (float) 400, 30);
   public static Glide echovolumeGl=new Glide(ac, (float) 0.3, 30);
   public static Glide lowpass=new Glide(ac, (float) 500, 30);
   public static PlaySet.toggle togl=new PlaySet.toggle();
public  static Glide vibGl=new Glide(ac, (float) 0.025, 30);;
   private Gain babaGain=new Gain(ac,1,(float) 0.9);

   TapIn ti=new TapIn(ac,2000);
	TapOut to=new TapOut(ac,ti,1000);
	public static JFrame frame;
	private Timer t;
	private  Clock c;

	public Player p= new Player(ac,babaGain);
	private Player p2= new Player(ac,babaGain);
	public static DrawCanvas canvas;
	public static JPanel panel2;
	public static JPanel paneltree;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AlibabaPlayer window = new AlibabaPlayer();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AlibabaPlayer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 780, 550);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		panel2=new JPanel();
		 panel2.setBounds(20, 200, 300, 300);
		    panel2.setBackground(Color.WHITE);
		    frame.getContentPane().add(panel2);
		    paneltree=new JPanel();
			 paneltree.setBounds(340, 300, 400, 200);
			    paneltree.setBackground(Color.WHITE);
			    frame.getContentPane().add(paneltree);
		JButton btnPlayRythm = new JButton("File");
		btnPlayRythm.setBounds(35, 33, 83, 23);
		btnPlayRythm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PlaySet ps=new PlaySet(babaGain,MasterGain);
				ps.newGlide("tone", toneGl);
				ps.newGlide("echodelay", echodelayGl);
				ps.newGlide("echogain", echovolumeGl);
				alibaba.compose.Command5.play(p,ps);
			//	alibaba.compose.shsho8.play(p,ps);
			//	alibaba.compose.shsho9.play(p,ps);
			//	alibaba.compose.Test2.play(p,ps);
				p.start();
			//	p2.start();
			}
		});
		frame.getContentPane().add(btnPlayRythm);
	//	instrument1 inst1=new instrument1(ac, MasterGain, 10);
		
		ac.out.addInput(MasterGain);
		ac.start();
	
		//p.setCommandInstruments(commandInstruments);
		
	///
	  //  canvas = new DrawCanvas();
	  //  canvas.setBackground(Color.WHITE);
	  //  canvas.setBounds(50, 200, 300, 300);
	 //   frame.getContentPane().add(canvas);
	 
	 //   JPanel panel2 = new JPanel();
	 //   panel2.setBounds(20, 200, 300, 300);
	    
	 //   frame.getContentPane().add(panel2);
	 //   panel2.add(G.vv);
	 //   panel2.repaint();
	    JButton btnCode = new JButton("Code");
	    btnCode.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	/*	Player p=new Player(ac, MasterGain);
	    		Synth S=new Synth1("inst1", ac, (float) 0.01, 20);
	    		p.instMap.put("inst1", S);*/
	    		// p=new Player(ac, MasterGain);
	    		
	    		p.setCommandInstruments("instc SYNTH1 0.01 100");
	    		p.setCommandScores("alibabac alibaba.scr");
	    		p.setCommandMetronoms("NEW metroc 180");
	    		p.setCommandSongs("NEW DEFAULT alibabac instc", "");
	    		p.setCommandSongs("METRONOM DEFAULT metroc", "");
	    		p.setCommandSongs("REPEAT DEFAULT TRUE", "");
	    		//p.lastDefaultSongPs().newGlide("tone", toneGl);
	    		p.lastDefaultSongPs().gOut.setGain(volumeGl);
	    		p.start();
	    	}
	    });
	    btnCode.setBounds(35, 68, 89, 23);
	    frame.getContentPane().add(btnCode);
	   // panel2.add(btnCode);
	    JButton btnSeparate = new JButton("separate");
	    btnSeparate.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		toneGl.setValue((float) (7.0/12));
	    	}
	    });
	    btnSeparate.setBounds(35, 102, 89, 23);
	    frame.getContentPane().add(btnSeparate);
	    
	    JButton btnJoin = new JButton("Join");
	    btnJoin.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		toneGl.setValue(0);
	    	}
	    });
	    btnJoin.setBounds(35, 132, 89, 23);
	    frame.getContentPane().add(btnJoin);
	    
	    JSlider slider = new JSlider();
	    slider.addChangeListener(new ChangeListener() {
	    	public void stateChanged(ChangeEvent arg0) {
	    		echovolumeGl.setValue((float) (slider.getValue()/100.0));
	    	}
	    });
	    slider.setBounds(166, 117, 200, 23);
	    frame.getContentPane().add(slider);
	    
	    JToggleButton tglbtnDistortion = new JToggleButton("Distortion");
	    tglbtnDistortion.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		togl.toggle=tglbtnDistortion.isSelected();
	    		System.out.println("I am Distortion");
	    		 JLabel lblThisIsA = new JLabel("DISTORTION");
	    		    lblThisIsA.setForeground(Color.RED);
	    		    lblThisIsA.setFont(new Font("Tahoma", Font.BOLD, 11));
	    		 //   frame.getContentPane().pan
	    		    panel.add(lblThisIsA);
	    		    test(lblThisIsA);
	    		    frame.validate();
	    	}
	    });
	    tglbtnDistortion.setBounds(227, 33, 121, 23);
	    frame.getContentPane().add(tglbtnDistortion);
	    
	    JToggleButton tglbtnVibration = new JToggleButton("Vibration");
	    tglbtnVibration.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		if (tglbtnVibration.isSelected()) 
	    			vibGl.setValue((float) 0.07); 
	    		else 
	    			vibGl.setValue((float) 0.025);
	    	}
	    });
	    tglbtnVibration.setBounds(227, 68, 121, 23);
	    frame.getContentPane().add(tglbtnVibration);
	    
	    JSlider slider_1 = new JSlider();
	    slider_1.addChangeListener(new ChangeListener() {
	    	public void stateChanged(ChangeEvent arg0) {
	    		lowpass.setValue(slider_1.getValue()*10);
	    	}
	    });
	    slider_1.setBounds(166, 151, 200, 23);
	    frame.getContentPane().add(slider_1);
	    
	    panel = new JPanel();
	    panel.setBounds(370, 11, 121, 240);
	    frame.getContentPane().add(panel);
	    panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	    
	    JLabel lblThisIsA = new JLabel("THIS IS A LABEL");
	    lblThisIsA.setForeground(Color.RED);
	    lblThisIsA.setFont(new Font("Tahoma", Font.BOLD, 11));
	    
	    panel.add(lblThisIsA);
	   lblThisIsA = new JLabel("dovvomi");
	    lblThisIsA.setForeground(Color.RED);
	    lblThisIsA.setFont(new Font("Tahoma", Font.BOLD, 11));
	 //  test(lblThisIsA);
	//   test(lblThisIsA);
	
	}
	public static void test(JLabel lblThisIsB) {
		  JLabel lblThisIsA = new JLabel("sevvomi");
		    lblThisIsA.setForeground(Color.RED);
		    lblThisIsA.setFont(new Font("Tahoma", Font.BOLD, 11));
		 panel.add(lblThisIsA);
		
	}

	public static void tik(instrument1 inst1){note n=new note("I1", (float)7, (float)0, (float)0.2, "");
	
	

	
	
	}
	class DrawCanvas extends JPanel
	{
	      @Override
	      public void paintComponent(Graphics g) 
	      {
	         super.paintComponent(g);
	   
	         return;
	      }}
}
