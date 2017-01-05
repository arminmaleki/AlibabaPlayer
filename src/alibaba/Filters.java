package alibaba;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Gain;

public class Filters {
public static void addFilters(AudioContext ac,Filter f1,Filter f2,PlaySet ps){
	PlaySet ps1=new PlaySet(ps);
	PlaySet ps2=new PlaySet(ps);
	Gain g=new Gain(ac,1,1);
	ps1.gOut=g;
	ps2.gIn=g;
	f1.filter(ac, ps1);
	f2.filter(ac, ps2);
	ps1.gOut.kill();
	ps2.gIn.kill();
	g.kill();
	//g.kill();
	
}
}
