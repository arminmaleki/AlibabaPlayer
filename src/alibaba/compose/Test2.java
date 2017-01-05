package alibaba.compose;

import alibaba.CodeEvent;
import alibaba.Filter;
import alibaba.PlaySet;
import alibaba.Player;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.LPRezFilter;
import net.beadsproject.beads.ugens.TapIn;
import net.beadsproject.beads.ugens.TapOut;
import net.beadsproject.beads.ugens.WaveShaper;

public class Test2 {

	public static void play(Player p, PlaySet ps) {
		////Filters
		Filter lowpass=new Filter(){public void applyToPlaySet(AudioContext ac,PlaySet ps){
			  Gain delayGain=new Gain(ac,1,ps.getGlide("echogain"));
			  LPRezFilter lpr=new LPRezFilter(ac,ps.getGlide("lprfreq"),(float) 0.98);
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
			ps.gOut.addInput(ws);
			} else ps.gOut.addInput(ps.gIn);
			
			
		}};
		p.addFilter("echo",echo);
		//////Songs
		p.fromFile("test2.baba");
		/////Code Events
		p.addCodeEvent("event3", new CodeEvent(){
			public void event(Player p,String Mother){
				
				if (Mother.equals("metro1")) {p.lastDefaultSongPs().newGlide("tone", ps.getGlide("tone"));
				p.lastDefaultSongPs().gOut.clearInputConnections();
				p.lastDefaultSongPs().newGlide("echodelay", alibaba.AlibabaPlayer.echodelayGl);
				p.lastDefaultSongPs().newGlide("echogain", alibaba.AlibabaPlayer.echovolumeGl);
				echo.applyToPlaySet(p.ac,p.lastDefaultSongPs());
				p.lastDefaultSongPs().gOut.setGain((float) .0);
				} else {p.lastDefaultSongPs().gOut.setGain((float) 0.0);	
				p.lastDefaultSongPs().gOut.clearInputConnections();
				p.lastDefaultSongPs().addTogle("disttogle",alibaba.AlibabaPlayer.togl);
				p.lastDefaultSongPs().newGlide("vibration", alibaba.AlibabaPlayer.vibGl);
				
				wshaper.applyToPlaySet(p.ac,p.lastDefaultSongPs());
				}
				
					}}
	                );
		///Output Filters
		//echo.filter(p.ac, ps);
		//LPRezFilter lpr=new LPRezFilter(p.ac, 800, (float) 0.9);
		//lpr.addInput(ps.gIn);
	//	ps.gOut.addInput(lpr);
		ps.newGlide("lprfreq", alibaba.AlibabaPlayer.lowpass);
		lowpass.applyToPlaySet(p.ac, ps);
	//	ps.gOut.addInput(ps.gIn);
		
	}

}
