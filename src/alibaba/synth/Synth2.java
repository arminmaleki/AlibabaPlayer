package alibaba.synth;

import java.util.HashMap;
import java.util.Map;

import alibaba.PlaySet;
import alibaba.note;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Function;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.WavePlayer;

public class Synth2 implements Synth {
	String name="";
	AudioContext ac;
	float vib=0;
	Map<String,String> vhash=new HashMap<String,String>();
	
 //   Glide vibGl;
	
	private float rand[]=new float[10];
	private float tail; 
	@Override
	public void play(note N, float tempo, PlaySet ps) {
		// TODO Auto-generated method stub
  //     System.out.println("note played " + N.pitch);
//       for (String s:ps.glMap.keySet())
//    	   switch (s) {case "vibration": vibGl=ps.glMap.get(s); break;case "tone": toneGl=ps.glMap.get(s); break;}
		Glide toneGl;
		Glide vibGl;
if (ps.getGlide("vibration")!=null) {
	vibGl=ps.getGlide("vibration"); 
	//System.out.println("there is a vibration Glide set");
	}else 
		vibGl=new Glide(ac,(float) -1,1000);
float dummy=vibGl.getValue();
if (ps.getGlide("tone")!=null) toneGl=ps.getGlide("tone"); else toneGl=new Glide(ac,0,1000);
       WavePlayer vwp=new WavePlayer(ac,(float)8.0,Buffer.SAW);
      // WavePlayer vwp2=new WavePlayer(ac,(float)11.0,Buffer.SAW);
       vhash.put(""+vwp.hashCode(), ""+(float)(440*Math.exp(N.pitch*Math.log(2)/12)));
     
       Function function1=new Function(vwp,toneGl,vibGl) {
// 	      WavePlayer w=(WavePlayer) vwp;
 //	      int a, ind;
 	      public float calculate() { 
 	    	  float vib_=0;
 	        if (x[2]==-1)
 	        	vib_=vib ;
 	        else 
 	        	vib_=x[2];
 	        return (float) ( ( Float.parseFloat(vhash.get(""+vwp.hashCode())) * Math.exp(toneGl.getValue()*Math.log(2.0))  )*
 	        		(1-vib_+vib_*x[0])    );
 	      }
 	    };
 	   
      // WavePlayer wp=new WavePlayer(ac, (float)(220*Math.exp(N.pitch*Math.log(2)/12)), Buffer.SINE);
 	   WavePlayer wp=new WavePlayer(ac, function1, Buffer.SQUARE);
 	   Glide intGl=new Glide(ac,0,1000);
 	   Glide timeGl=new Glide(ac,0,10000);
 	  Function function2=new Function(wp,vwp,toneGl,intGl,timeGl) {
//	      WavePlayer w=(WavePlayer) vwp;
//	      int a, ind;
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
	        				(1.0-x[3]*0.9)*Math.tanh(       
	        						 x[0] 
	        						
	        						*(2+Math.tanh((x[3]-0.1)*5)*2) *4    )
	        				
	        				);
	      }
	    };
 	   Envelope e1=new Envelope(ac,0);
 	  
 	   float time=N.duration*60000/tempo;
 	  Gain g1=new Gain(ac, 1, e1);
 	  intGl.setValue((float) 1.0);
 	 timeGl.setValue((float) 10.0);
 	 if (N.modification.equals("UPWARD")) {
 		 e1.addSegment( (float)0.1,time/8*3);
 		e1.addSegment( (float)1.5,time/8);
 		 e1.addSegment( (float)1.2,time/2);
 		// e1.addSegment( (float)1.0,time);
 		 //  e1.addSegment( (float)(0.2),Math.max(0, time-60-100));
 	 	   e1.addSegment( 0,tail, new KillTrigger(g1));
 		 
 	 } else if (N.modification.equals("FLAT")){ e1.addSegment( (float)1.0,time, new KillTrigger(g1));} else{
 	   e1.addSegment( 1,10);
 	   e1.addSegment( (float)0.5,100);
 	   e1.addSegment( (float)(0.2),Math.max(0, time-60-100));
 	   e1.addSegment( 0,tail, new KillTrigger(g1));}
 	   
 	  e1.addSegment( 0,0, new KillTrigger(wp));
 	 e1.addSegment( 0,0, new KillTrigger(vwp));
 	  // g1.addInput(wp);
 	  
 	 g1.addInput(function2);
 	   
 	   vwp.setKillListener( new Bead(){
 		  public void messageReceived(Bead message){ //System.out.println("Ended"); 
 		  vhash.remove(""+((WavePlayer)message).hashCode());}
 		   
 	   }
 			   
 			   );
       ps.gIn.addInput(g1);
 //      System.out.println("");
 //      for (String s: vhash.values()) System.out.print(s + " ");
	}

	@Override
	public String getName() {
		
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name=name;

	}
	public Synth2(String name,AudioContext ac,float vib, float tail){
		this.name=name;
		this.ac=ac;
		this.vib=vib;
		this.tail=tail;
	//	vibGl=new Glide(ac,(float) 4.0);
	//	toneGl=new Glide(ac,(float) 0.0);
		rand[0]=0;
		float max=0;
		for (int i=1;i<rand.length;i++) {rand[i]=(float) (rand[i-1]+Math.random());}
		for (int i=0;i<rand.length;i++) {rand[i]-=rand[rand.length-1]*i*1.0/(rand.length); max=Math.max(Math.abs(rand[i]), max);}
		for (int i=0;i<rand.length;i++) rand[i]=rand[i]/max;
	//	for (int i=0;i<rand.length;i++) System.out.print(rand[i]+" ");
	//	System.out.println("");
	//	System.out.println(randomchain((float) (2*Math.PI-2 * Math.PI / 2/rand.length)));
		
		
	}
	float randomchain(float phase) {
		int ind=(int) Math.floor(phase/Math.PI/2*rand.length); float extra=(float) ((phase/2/Math.PI-ind*(1.0/rand.length)));
		ind=ind%rand.length;
		int indp=(ind+1)%rand.length;
	//	System.out.println(ind + " " + indp+ " "+extra+rand[ind]+" "+rand[indp]);
		return (float) (rand[ind]*(1.0-extra*rand.length)+rand[indp]*extra*rand.length);
	}

}
