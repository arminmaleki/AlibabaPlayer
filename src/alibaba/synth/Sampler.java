package alibaba.synth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import alibaba.PlaySet;
import alibaba.note;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Function;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.OnePoleFilter;

public class Sampler implements Synth {
String name="";
AudioContext ac;
Map <String,Sample> sMap=new HashMap<String,Sample>();
public Sampler(String name,AudioContext ac){ 
	this.ac=ac;
	this.name=name;
	try {
		Sample s=new Sample("sample/bachtrumpet.mp3");
		sMap.put("trumpet", s);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
	@Override
	public void play(note N, float tempo, PlaySet ps) {
		// TODO Auto-generated method stub
		 GranularSamplePlayer gsp = new GranularSamplePlayer(ac, sMap.get("trumpet"));


		    Glide randomnessValue = new Glide(ac, 0, 10);
		    Glide  intervalValue = new Glide(ac, 30, 100);
		    Glide  grainSizeValue = new Glide(ac, 30, 50);
		    Glide  positionValue = new Glide(ac, 100, 30);
		    Glide  pitchValue = new Glide(ac, (float) Math.exp((N.pitch-5-0.0+12)/12.0*Math.log(2)), 400);//
            pitchValue.setValue((float) Math.exp((N.pitch-5+2+12)/12.0*Math.log(2)));
		    gsp.setRandomness(randomnessValue);
		    gsp.setGrainInterval(intervalValue);
		    gsp.setGrainSize(grainSizeValue);
		    gsp.setPosition(positionValue);
		    gsp.setPitch(pitchValue);


		    Envelope e=new Envelope(ac, 0);

		    Gain g;
		    float rand=N.duration;
		    g=new Gain(ac, 1, e);
		    Glide timeGl=new Glide(ac,0,10000);
		    timeGl.setValue((float) 10.0);
		    Function function2=new Function(gsp,timeGl) {
//			      WavePlayer w=(WavePlayer) vwp;
//			      int a, ind;
			      public float calculate() { 
			    	 // System.out.println(x[4]);
			     /*   return (float) 
			        		
			        		(
			        				Math.tanh(       
			        						( x[0]*(1-x[3])+x[3]*randomchain((float) (x[4]*2*Math.PI*Float.parseFloat(vhash.get(""+vwp.hashCode())))) )
			        						
			        						*x[2]     )
			        				
			        				); */
		   return (float) 
			        		
			        		(
			        				(1.0-x[1]*0.9)*Math.tanh(       
			        						 x[0] 
			        						
			        						*(2+Math.tanh((x[1]-0.15)*10)*2) *4    )
			        				
			        				);
			      }
			    };
		  
		      e.addSegment((float) (0.05*0.5), 50);
		    e.addSegment((float) (1.0*0.5), 100);
		    e.addSegment((float) (2.0*0.5), 1000*rand-200,(float) 0.5);
		    e.addSegment(0, 50, (float) 0.5, new KillTrigger(g));
		    OnePoleFilter filter= new OnePoleFilter(ac,(float) 800.0);
		    filter.addInput(function2);
		    g.addInput(filter);

		    ps.gIn.addInput(g);
		    gsp.start(); 
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
