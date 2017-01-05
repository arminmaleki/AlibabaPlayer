package alibaba.compose;

import alibaba.CodeEvent;
import alibaba.Filter;
import alibaba.PlaySet;
import alibaba.Player;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.TapIn;
import net.beadsproject.beads.ugens.TapOut;

public class Command5 {

	public static void play(Player p,PlaySet ps) {
		////Filters
		Filter echo=new Filter(){public void filter(AudioContext ac,PlaySet ps){
			  Gain delayGain=new Gain(ac,1,ps.get("echogain"));
			 
			   TapIn ti=new TapIn(ac,2000);
				TapOut to=new TapOut(ac,ti,ps.get("echodelay"));
		
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
			public void event(Player p,String Mother){
				
			//	if (Mother.equals("metro1")) p.lastDefaultSongPs().newGlide("tone", ps.get("tone"));
				 p.SongPs("tempobaba").gOut.setGain((float)0.05);	
					}});
		
		p.addCodeEvent("event2", new CodeEvent(){
			public void event(Player p,String Mother){
				
				if (Mother.equals("metro1")) p.lastDefaultSongPs().newGlide("tone", ps.get("tone"));
				
					}}
	                );
		p.addCodeEvent("event1", new CodeEvent(){
			public void event(Player p,String Mother){
				
				if (Mother.equals("metro1")) p.lastDefaultSongPs().gOut.setGain((float) 0.05);
				
					}}
	                );
		p.addCodeEvent("event4", new CodeEvent(){
			public void event(Player p,String Mother){
				
				 
				 float shift=(float) (Math.floor(Math.random()*12)/12);
				 Glide gl=new Glide(p.ac,shift,100);
				 p.lastDefaultSongPs().newGlide("tone", gl);
				 p.lastDefaultSongPs().gOut.setGain((float) (0.8-shift*0.5));
					}}
	                );
		p.addCodeEvent("event5", new CodeEvent(){
			public void event(Player p,String Mother){
				
				 
				 System.out.println(" I AM EVENT 5");
				 Envelope e=new Envelope(p.ac,(float) 0.1);
				 e.addSegment((float) 0.7, 1000);
				 e.addSegment((float)0.7, 1500);
				 e.addSegment((float)0.05, 1000);
				// p.lastDefaultSongPs().g.setGain(e);
				 p.SongPs("tempobaba").gOut.setGain(e);
					}}
	                );
		
		echo.filter(p.ac, ps);
		
		
		/*
		ti.addInput(MasterGain);
	  delayGain.addInput(to);
	  delayGain2.addInput(delayGain);
		 MasterGain.addInput(delayGain2);*/
		
	}

}
