package alibaba;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.TapIn;
import net.beadsproject.beads.ugens.TapOut;

public class Scripter {
	
	AudioContext ac= new AudioContext();
	PlaySet master=new PlaySet(ac,0.3f);
	ScriptEngine engine=new ScriptEngineManager().getEngineByName("nashorn");
	Invocable Inv=(Invocable) engine;
	Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
	 Manager manager=new Manager();
	public void run(String filename) {
		String prelim="";
	//	prelim+="var SongClass= Java.type('scripting1.Song');\n";
		ac.start();
		ac.out.addInput(master.gOut);
		bindings.put("ac", ac);
		bindings.put("master", master);
		bindings.put("manager", manager);
		bindings.put("scripter", this);
		int a=2;
		bindings.put("a", a);
		
		try {
			engine.eval(new FileReader("frame.js"),bindings);
			engine.eval(new FileReader(filename),bindings);
		} catch (FileNotFoundException | ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void pushScriptEvent(final String event,final Song song){
		/*CodeEvent ce=new CodeEvent(){

			@Override
			public void run(History h) {
				event.call(null, h);
				
			}};*/
			CodeEvent ce=(History h)->{
				try {
					History newh=new History(h);
					newh.lastSong=song;
					System.out.println("event"+event+ " is being dispatched"
							+ newh.lastSong + " " + newh.lastEventName);
					Inv.invokeMethod(engine.eval("events"), event, newh);
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ScriptException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//event.call(null,h);
				};
			song.songEvents.add(ce);
		
	}

}
