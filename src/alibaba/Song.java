package alibaba;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alibaba.synth.Synth;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Gain;

public class Song {
	AudioContext ac;
//	Gain MasterGain;
	public String name="";
	note n;
	Map<String, Synth> instMap;
	Synth CurrentInstrument;
	Score scr;
	String MotherEvent="";
	float maxTime=0;
	float songTime=0;
	Manager manager;
	boolean INSTRUMENT_VARIABLE=false;
	public List<CodeEvent> songEvents=new ArrayList<CodeEvent>();
	public void addManager(Manager manager){this.manager=manager;}
	public Song setPitch(float pitch){this.pitchShift=pitch; return this;}
	public void setScore(Score scr){
		this.scr=scr;
		for (note n:scr.noteList) {
			maxTime=Math.max(n.time, maxTime);
			songTime=Math.max(n.time+n.duration, songTime);
		}
	//	System.out.println("maxTime:" + maxTime + " songTime:" + songTime);
	}
	public void setInstMap(Map<String, Synth> instMap) {
		this.instMap = instMap;
	}
	int offset=0;
	String timerName="";
	boolean repeat=false;
	
	 float pitchShift=0;
	public String Mother;
	public PlaySet ps;
    public History history=new History();
	public Metronom Metronom;
	public Song(note n,Synth s,PlaySet ps){this("", new Score(n), s, ps);	}
	public Song(float d){this(new note(d), null, null);}
	public Song(String name,Score scr,Synth CurrentInstrument,PlaySet ps) {
		//super();
		this.name = name;
		this.CurrentInstrument=CurrentInstrument;
		this.ps=ps;
		//this.pitchShift=pitchShift;
		setScore(scr);
		
		
/*
		String[] CodeSplit=code.split("\\s+");
		CurrentInstrument=CodeSplit[0];
		
		n=new note("I1", Float.parseFloat(CodeSplit[1]), (float)0, (float)0.2, "");*/
	//	ac2.start();
	}

	public boolean update(int count,float tempo){
		if (count<offset) return true;
		if ((count%4)==0){
			if (((float)(count-offset)==songTime*32)&& !repeat) { return false;}
			if (   (float)(count-offset)==songTime*32 && repeat){offset= count; System.out.println(repeat);}
		//	System.out.println("I am playing");
			for (note n:scr.noteList) if ((Math.abs(n.time - (float)(count-offset)/32.0)< 0.01)&&(!n.modification.equals("SILENCE")))
			{ note n2=new note(n);
			 n2.pitch+=pitchShift;
		//	 n2.print();
			 
			//   System.out.println("is playing:   "+name+ " " +Mother);
				if ((!INSTRUMENT_VARIABLE)||(!instMap.containsKey(n2.inst))) 
					
				CurrentInstrument.play(n2,tempo,ps); 
				else {instMap.get(n2.inst).play(n2,tempo,ps); System.out.println("inst orig");}
			//	System.out.println("reporting " + name);
			//	for (String s:ps.glMap.keySet()) System.out.print("Glides: "+s+" ");
			//	System.out.println("");
//				System.out.println("song:"+name+" time:"+n.time+" count:"+count+" offset"+offset+" "+INSTRUMENT_VARIABLE);
			}
			
			
		}
		if ((count-offset)%16 ==0){
	//	tik();System.out.println("tik();"+instMap.get("inst1").getName()+" current:"+CurrentInstrument);
			}
		return true;
		
	}
	public  void tik(){
	//instMap.get(CurrentInstrument).play(n,tempo);
	
	
	}
	public String getName() {
		
		return name;
	}
	public void setMother(String mother) {
		this.Mother=mother;
		
	}
	public String getMother() {
		return this.Mother;
		
	}
	
}
