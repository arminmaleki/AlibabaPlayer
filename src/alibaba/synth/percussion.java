package alibaba.synth;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import alibaba.PlaySet;
import alibaba.note;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;

public class percussion implements Synth {
String name;
AudioContext ac;

Map<String,Sample> sm=new HashMap<String,Sample>();
    public percussion(String name,AudioContext ac) {
    	this.ac=ac;
    	this.name=name;
    	int i;
    	for (i=1;i<=10;i++){ 
    	try {
			Sample s=new Sample("percussion/C1000-"+i+".wav.mp3");
			//s.pause(true);
			sm.put(""+i, s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
    	
    }

	@Override
	public void play(note N,float tempo,PlaySet ps) {
//	N.print();
	String[] Code=N.modification.split("#");
		SamplePlayer sp=new SamplePlayer(ac,sm.get(Code[0]));
		if (N.pitch!=0){
			Envelope e=new Envelope(ac, N.pitch);
			sp.setRate(e);
		}
		float vol=(float) 0.5;
		if (Code.length>1) vol=Float.parseFloat(Code[1])/10;
		Gain g=new Gain(ac,1,vol);
		g.addInput(sp);
		ps.gIn.addInput(g);
		sp.start();

	}

	@Override
	public String getName() {
		
		return name;
	}

	@Override
	public void setName(String name) {
		this.name=name;

	}

}
