package alibaba.compose;

import alibaba.CodeEvent;

import alibaba.PlaySet;
import alibaba.Player;
import alibaba.filters.Filter;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.LPRezFilter;
import net.beadsproject.beads.ugens.TapIn;
import net.beadsproject.beads.ugens.TapOut;
import net.beadsproject.beads.ugens.WaveShaper;

public class shsho9 {
   
	public static void play(Player p, PlaySet ps) {
		////Filters
		Filter lowpass=new Filter(){public void applyToPlaySet(AudioContext ac,PlaySet ps){
			//  Gain delayGain=new Gain(ac,1,ps.get("echogain"));
			  LPRezFilter lpr=new LPRezFilter(ac,ps.getGlide("lprfreq"),(float) 0.99);
			lpr.addInput(ps.gIn);
			ps.gOut.addInput(lpr);
			
		}};
		Filter echo=new Filter(){public void applyToPlaySet(AudioContext ac,PlaySet ps){
			  Gain delayGain=new Gain(ac,1,ps.getGlide("echogain"));
			 
			   TapIn ti=new TapIn(ac,2000);
				TapOut to=new TapOut(ac,ti,ps.getGlide("echodelay"));
		
			ti.addInput(ps.gIn);
			  delayGain.addInput(to);
			  ps.gIn.addInput(delayGain);
			 
			  ps.gOut.addInput(ps.gIn);
			
			
		}};
		Filter wshaper=new Filter(){public void applyToPlaySet(AudioContext ac,PlaySet ps){
			//  Gain delayGain=new Gain(ac,1,ps.get("echogain"));
			 
			//   TapIn ti=new TapIn(ac,2000);
			//	TapOut to=new TapOut(ac,ti,ps.get("echodelay"));
			PlaySet pps=ps;
			Gain gg=ps.gOut;
			Gain gg2=ps.gIn;
			if (ps.buttonMap.get("disttogle").toggle) {
		float[] WaveShape={(float) 0.0,(float) -0.5,(float) 1.0,(float) 0.5,(float) 0.0,(float) 0.5,(float) -1.0,(float) -0.5};
		WaveShaper ws=new WaveShaper(ac,WaveShape);
			ws.addInput(ps.gIn);
		//ps.gOut.addInput(ps.gIn);
			  LPRezFilter lpr=new LPRezFilter(ac,ps.getGlide("lprfreq"),(float) 0.98);
				lpr.addInput(ws);
				ps.gOut.addInput(lpr);
			ps.gOut.addInput(ws);
			} else ps.gOut.addInput(ps.gIn);
			
			
		}};
		p.addFilter("echo",echo);
		//////Songs
		p.fromFile("6o9.baba");
		/////Code Events
		 /*p.addCodeEvent("event3", new CodeEvent(){
			public void event(Player p,String Mother){
				
				if (Mother.equals("metro1")) {p.lastDefaultSongPs().newGlide("tone", ps.get("tone"));
				p.lastDefaultSongPs().gOut.clearInputConnections();
				p.lastDefaultSongPs().newGlide("echodelay", alibaba.AlibabaPlayer.echodelayGl);
				p.lastDefaultSongPs().newGlide("echogain", alibaba.AlibabaPlayer.echovolumeGl);
				echo.filter(p.ac,p.lastDefaultSongPs());
				p.lastDefaultSongPs().gOut.setGain((float) .0);
				} else {p.lastDefaultSongPs().gOut.setGain((float) 0.0);	
				p.lastDefaultSongPs().gOut.clearInputConnections();
				p.lastDefaultSongPs().addTogle("disttogle",alibaba.AlibabaPlayer.togl);
				p.lastDefaultSongPs().newGlide("vibration", alibaba.AlibabaPlayer.vibGl);
				
				wshaper.filter(p.ac,p.lastDefaultSongPs());
				}
				
					}}
	                );*/
		///Output Filters
		//echo.filter(p.ac, ps);
		//LPRezFilter lpr=new LPRezFilter(p.ac, 800, (float) 0.9);
		//lpr.addInput(ps.gIn);
	//	ps.gOut.addInput(lpr);
	
	
	
	
	
		ps.newGlide("echodelay", alibaba.AlibabaPlayer.echodelayGl);
		ps.newGlide("echogain", alibaba.AlibabaPlayer.echovolumeGl);
		ps.newGlide("lprfreq", alibaba.AlibabaPlayer.lowpass);
	//	ps.addTogle("disttogle",alibaba.AlibabaPlayer.togl);
	//	Filters.addFilters(p.ac, echo, wshaper, ps);
	//	lowpass.filter(p.ac, ps);
		ps.gOut.addInput(ps.gIn);
		
	}

}
