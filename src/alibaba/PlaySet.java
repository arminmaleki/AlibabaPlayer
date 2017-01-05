package alibaba;

import java.util.HashMap;
import java.util.Map;

import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
// A play set is a set of two gains, input and output, and a collection of glides and buttons which
// Adjust the respective behavior of input and output. 
// it doesn't do anything by itself except assigning a name to each glide and button.
public class PlaySet {
    public Gain gIn,gOut;
    public Map<String,Glide> glMap=new HashMap<String,Glide>();
    static public class toggle { public boolean toggle=false;}
    public Map<String,toggle> buttonMap=new HashMap <String,toggle>();
    
     PlaySet (PlaySet ps){ this.gIn=ps.gIn;this.gOut=ps.gOut; this.glMap=new HashMap<String,Glide>(ps.glMap);
     this.buttonMap=new HashMap<String,toggle>(ps.buttonMap);
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
	public Glide getGlide(String string) {if (glMap.containsKey(string)) return glMap.get(string); else return null;}

}
