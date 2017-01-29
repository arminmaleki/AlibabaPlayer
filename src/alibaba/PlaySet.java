package alibaba;

import java.util.HashMap;
import java.util.Map;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
/** A play set is a set of two gains, input and output, and a collection of glides and buttons which
/* Adjust the respective behavior of input and output. 
/* PlaySet is also sent to the relevant instrument to use the data on its glides and buttons, distortion, etc
/* it doesn't do anything by itself except assigning a name to each glide and button.*/
public class PlaySet {
	public AudioContext ac;
    public Gain gIn,gOut;
    public Map<String,Glide> glMap=new HashMap<String,Glide>();
    static public class toggle { public boolean toggle=false;}
    public Map<String,toggle> buttonMap=new HashMap <String,toggle>();
     public PlaySet set(String name, float value){
    	 glMap.get(name).setValue(value);
    	 return this;
     }
     public Glide addGlide(String name,float init){
    	 Glide gl=new Glide(ac,init,30);
    	 glMap.put(name, gl);
    	 return gl;
     }
     public PlaySet disconnect(){gOut.clearInputConnections();return this;}
     public PlaySet connect(){gOut.addInput(gIn);return this;}
     
     public PlaySet(AudioContext ac,float volume){
    	 this.ac=ac;
      
      
 		Gain gOut=new Gain (ac,1,  addGlide("volume", volume));
			Gain gIn=new Gain (ac,1,(float) 1.0);
			
			gOut.addInput(gIn);
			this.gIn=gIn;
			this.gOut=gOut;
     }
	public PlaySet(Gain gIn,Gain gOut) {
		this.gOut=gOut;
		this.gIn=gIn;
	}

	public void newGlide(String string, Glide pedal) {
		glMap.put(string, pedal);
		
	}
	public void addTogle(String string, toggle t) {
		buttonMap.put(string, t);
		
	}
	public PlaySet plugTo(PlaySet ps){ps.gIn.addInput(gOut);return this;}
	public Glide getGlide(String string) {if (glMap.containsKey(string)) return glMap.get(string); else return null;}

}
