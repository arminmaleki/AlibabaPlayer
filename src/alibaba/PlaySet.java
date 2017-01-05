package alibaba;

import java.util.HashMap;
import java.util.Map;

import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;

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
	public Glide get(String string) {if (glMap.containsKey(string)) return glMap.get(string); else return null;}

}
