package alibaba.filters;

import alibaba.PlaySet;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.LPRezFilter;
import net.beadsproject.beads.ugens.WaveShaper;


public class Distortion implements Filter {

	@Override
	public PlaySet apply(PlaySet ps) {
	ps.disconnect();
	float[] WaveShape={(float) 0.0,(float) -0.5,(float) 1.0,(float) 0.5,(float) 0.0,(float) 0.5,(float) -1.0,(float) -0.5};
	WaveShaper ws=new WaveShaper(ps.ac,WaveShape);
	ws.addInput(ps.gIn);
	//ps.gOut.addInput(ps.gIn);
		  LPRezFilter lpr=new LPRezFilter(ps.ac,ps.addGlide("cutoff",800),(float) 0.98);
			lpr.addInput(ws);
			ps.gOut.addInput(lpr);
		//ps.gOut.addInput(ws);
		
		return ps;
	}

}
