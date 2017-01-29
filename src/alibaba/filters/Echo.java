package alibaba.filters;

import alibaba.PlaySet;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.TapIn;
import net.beadsproject.beads.ugens.TapOut;

public class Echo implements Filter {

	@Override
	public PlaySet apply( PlaySet ps) {
		 ps.disconnect();
		 Gain delayGain=new Gain(ps.ac,1,ps.addGlide("echoGain", 0.3f));
		 
		   TapIn ti=new TapIn(ps.ac,2000);
			TapOut to=new TapOut(ps.ac,ti,ps.addGlide("echoDelay",100f));
	
		ti.addInput(ps.gIn);
		  delayGain.addInput(to);
		  ps.gIn.addInput(delayGain);
		 
		  ps.gOut.addInput(ps.gIn);
           return ps;
	}

}
