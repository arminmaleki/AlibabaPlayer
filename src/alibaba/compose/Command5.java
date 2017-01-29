package alibaba.compose;

import alibaba.CodeEvent;
import alibaba.PlaySet;
import alibaba.Player;
import alibaba.filters.Filter;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.TapIn;
import net.beadsproject.beads.ugens.TapOut;
/// this is a "class file" which accompanies a .baba file instruction. command5.baba

// it sets up the filters: define them, name and introduce them to a player, and start them
// with the audio context and the given playset

// it defines "Code Events" for player, that is events which are triggered through the GUI,etc...

// it starts the player with command5.baba
public class Command5 {

	public static void play(Player p,PlaySet ps) {
		////Filters
		Filter echo=new Filter(){public void applyToPlaySet(AudioContext ac,PlaySet ps){
			  Gain delayGain=new Gain(ac,1,ps.getGlide("echogain"));
			 
			   TapIn ti=new TapIn(ac,2000);
				TapOut to=new TapOut(ac,ti,ps.getGlide("echodelay"));
		
			ti.addInput(ps.gIn);
			  delayGain.addInput(to);
			  ps.gIn.addInput(delayGain);
			 
			  ps.gOut.addInput(ps.gIn);
			
			
		}};
		p.addFilter("echo",echo);
		//////Songs
		p.fromFile("command5.baba");
		/////Code Events
		p.addCodeEvent("eventbasic", new CodeEvent(){
			public void run(Player p,String Mother){
				
			//	if (Mother.equals("metro1")) p.lastDefaultSongPs().newGlide("tone", ps.get("tone"));
				 p.SongPs("tempobaba").gOut.setGain((float)0.05);	
					}});
		
		p.addCodeEvent("event2", new CodeEvent(){
			public void run(Player p,String Mother){
				
				if (Mother.equals("metro1")) p.lastDefaultSongPs().newGlide("tone", ps.getGlide("tone"));
				
					}}
	                );
		p.addCodeEvent("event1", new CodeEvent(){
			public void run(Player p,String Mother){
				
				if (Mother.equals("metro1")) p.lastDefaultSongPs().gOut.setGain((float) 0.05);
				
					}}
	                );
		p.addCodeEvent("event4", new CodeEvent(){
			public void run(Player p,String Mother){
				
				 
				 float shift=(float) (Math.floor(Math.random()*12)/12);
				 Glide gl=new Glide(p.ac,shift,100);
				 p.lastDefaultSongPs().newGlide("tone", gl);
				 p.lastDefaultSongPs().gOut.setGain((float) (0.8-shift*0.5));
					}}
	                );
		p.addCodeEvent("event5", new CodeEvent(){
			public void run(Player p,String Mother){
				
				 
				 System.out.println(" I AM EVENT 5");
				 Envelope e=new Envelope(p.ac,(float) 0.1);
				 e.addSegment((float) 0.7, 1000);
				 e.addSegment((float)0.7, 1500);
				 e.addSegment((float)0.05, 1000);
				// p.lastDefaultSongPs().g.setGain(e);
				 p.SongPs("tempobaba").gOut.setGain(e);
					}}
	                );
		
		echo.applyToPlaySet(p.ac, ps);
		
		
		/*
		ti.addInput(MasterGain);
	  delayGain.addInput(to);
	  delayGain2.addInput(delayGain);
		 MasterGain.addInput(delayGain2);*/
		
	}

}
