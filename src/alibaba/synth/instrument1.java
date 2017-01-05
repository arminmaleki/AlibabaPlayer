package alibaba.synth;
import alibaba.PlaySet;
import alibaba.note;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Function;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;
//import net.beadsproject.beads.*;


public class instrument1 implements Synth{
	  float[][] fq=new float[100][3];
	  int i=0;
	  float vib;
	    Gain Masterr;
	    AudioContext ac;
	    String name;
	    
public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

public	   instrument1(String name,AudioContext ac,float vibb) {this.name=name;this.ac=ac;vib=vibb;}
WavePlayer modf[]=new WavePlayer[10];

public void play(note n,float tempo,PlaySet ps){
	// WavePlayer mod=new WavePlayer(ac, 10, Buffer.SINE);
	 if (n.modification.equals("SILENCE")) return;
	    modf[i]=new WavePlayer(ac, 5, Buffer.SINE);
	    
	    float frequency=n.pitch;
	    fq[i][0]=frequency;
	    fq[i][1]=fq[i][0]+(float)12.0;
	    fq[i][2]=fq[i][0]*2;
	  //  System.out.println("frequencyy:"+fq[i][0]+" "+i);
	    Function functionf=new Function(modf[i]) {
	      WavePlayer m=(WavePlayer) modf[i];
	      int a, ind;
	      public float calculate() { 
	        for (a=0; a<10; a++)
	        {
	          if (m==modf[a]) ind=a;
	          //  System.out.println(""+ind);
	        }
	        return (float)(400*Math.exp((fq[ind][0]+0.0)/12.0*Math.log(2.0))*(1-vib+vib*x[0])*1.0);
	      }
	    };
	    Function functionf2=new Function(modf[i]) {
	      WavePlayer m=(WavePlayer) modf[i];
	      int a, ind;
	      public float calculate() { 
	        for (a=0; a<10; a++)
	        {
	          if (m==modf[a]) ind=a;
	          //  System.out.println(""+ind);
	        }
	        return (float)(400*Math.exp((fq[ind][1]+0.0)/12.0*Math.log(2.0))*(0.98+0.02*(x[0]))*1.0);
	      }
	    };
	    //WavePlayer wp=new WavePlayer(ac,400*exp((random(12)+0)/12.0*log(2.0)),Buffer.SINE);
	    WavePlayer wp=new WavePlayer(ac, functionf, Buffer.SINE);
	    WavePlayer wp2=new WavePlayer(ac, functionf2, Buffer.SINE);

	    Envelope e=new Envelope(ac, 0);
	    Envelope e2=new Envelope(ac, 0);
	    Gain g, g2;
	    float rand=n.duration/tempo*60,ratio=(float)0.05;
	    float c=(float) 0.3;
	    g=new Gain(ac, 1, e);
	    g2=new Gain(ac, 1, e2);
	    e.addSegment((float)(1*0.8)*c, (float)50);
	    e.addSegment((float)(0.5*0.8)*c,(float) 100);
	    e.addSegment((float)(0.5*0.8)*c,(float) 1000*rand-150);
	    e.addSegment((float)0,(float)( 500*0.8), (float)0.5, new KillTrigger(g));
	    e2.addSegment(1*ratio, 50);
	    e2.addSegment((float)(0.5*ratio), 100);
	    e2.addSegment((float)(0.5*ratio), 1000*rand-150);
	    e2.addSegment((float)(0), (float)500, (float)0.5, new KillTrigger(g2));
        
	    g.addInput(wp);
	    g2.addInput(wp2);
	    ps.g.addInput(g);
	    ps.g.addInput(g2);
	  //  Masterr.printInputList();
	  //  System.out.println("");

	    // background(0);
	    //fill(255);text("BOOGH"+Master.ins,100,100);
	    i++;
	    if (i>9) i=0;
}
}
