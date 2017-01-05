package alibaba;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Paint;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;

import org.apache.commons.collections15.Transformer;

import alibaba.synth.Sampler;
import alibaba.synth.Synth;
import alibaba.synth.Synth1;
import alibaba.synth.Synth2;
import alibaba.synth.instrument1;
import alibaba.synth.percussion;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
/* To do List:
 * Metronom Starts/stops
 * Visualization...
 * Start/Stop/ReStart Songs
 * Gain/Pitch Glide for Songs, Commands for Glides
 * Sample Player commands
 * Complex bars
 * Data structure for a growing tree
 * buttons for events, label for songs being played, sliders for metronoms
 * Kill functions for player
 * completing MetroEvent
 * replaying player
 * selfinst problem
 * */
public class Player {
	public AudioContext ac= new AudioContext();
	int eventCount=0;
	  public GraphVis G;
	  public GraphVis2 Gtree;
	  List<String> activeEvents=new ArrayList<String>();
    private Gain MasterGain=new Gain(ac,1,(float)1.0);
 //   public instrument1 inst1=new instrument1(ac, MasterGain, (float)0.1);
 //   public instrument1 inst12=new instrument1(ac, MasterGain, (float)0.5);
    
    public Map<String,Synth> instMap=new HashMap<String,Synth>();
    public Map<String,Metronom> metroMap=new HashMap<String,Metronom>();
  public Map<String,Score> scoreMap=new HashMap<String,Score>();
    public Map<String,Song> songMap=new HashMap<String,Song>();
    //Component c;
    private int commands=0;
    public Map<String,Component> labelMap=new HashMap<String,Component>();
  //  JLabel l;
	List<String> CommandInstruments;
	List<String> CommandMetronoms;
	List<String> CommandSongs;
	List<String> CommandScores;
	// holds song's name and its concised motherevents
	Map<String,String> songHistory=new HashMap<String,String>();
    public class PlayerEvent {String name; List<String> command; String[] ocasion; int number=0; 
    public CodeEvent codeEvent= new CodeEvent(){ public void run(Player p,String mother){//System.out.println(" I AM AN EVENT MY MOTHER IS " + mother);
    } };
    public void setCodeEvent(CodeEvent e) {this.codeEvent=e;};
    }
	Map<String,PlayerEvent>	playerEvents=new HashMap<String,PlayerEvent>();
	int sDefault=0;
	String lastDefault="";
	private Map<String,Filter> filterMap=new HashMap<String,Filter>();
	public void addFilter(String name,Filter f){filterMap.put(name, f);}
	public Player(AudioContext ac, Gain masterGain) {
		this.ac=ac;
		this.MasterGain=masterGain;
	}
	public void setCommandScores(List<String> commandScores) {
		CommandScores = commandScores;
		
		System.out.println("COMMAND Scores:");
		for (String s:commandScores){
			String[] Code=s.split("\\s+");
			Score scr1=new Score(Code[0],Code[1]);
			scoreMap.put(scr1.getName(), scr1);
			System.out.println("NAME:" + scr1.getName() + " File:" + scoreMap.get(scr1.getName()).filename);
		}
	/*	Score scr1=new Score("alibaba","alibaba.scr");
		scoreMap.put(scr1.getName(), scr1);*/
	}
	public void setCommandInstruments(List<String> commandInstruments) {
		CommandInstruments = commandInstruments;
		
		System.out.println("COMMAND Instruments:");
		for (String s:commandInstruments){
			String[] Code=s.split("\\s+");
	/*	if (Code[1].equals("SYNTH1"))	{
			Instrument I=new instrument1(ac, MasterGain, Float.parseFloat(Code[2]));
			I.setName(Code[0]);
			instMap.put(I.getName(), I);
			
		}
		if (Code[1].equals("PERCUSSION"))	{
			Instrument I=new percussion(ac, MasterGain);
			I.setName(Code[0]);
			instMap.put(I.getName(), I);
			
		} */
			if (Code[1].equals("INST1"))	{
				Synth I=new instrument1(Code[0],ac, Float.parseFloat(Code[2]));
			//	I.setName(Code[0]);
				instMap.put(I.getName(), I);
				System.out.println("##inst1 " + I.getName());
			}	
			if (Code[1].equals("SYNTH1"))	{
				Synth I=new Synth1(Code[0],ac, Float.parseFloat(Code[2]),Float.parseFloat(Code[3]));
			//	I.setName(Code[0]);
				instMap.put(I.getName(), I);
				System.out.println("##synth1 " + I.getName());
			}	
			if (Code[1].equals("SYNTH2"))	{
				Synth I=new Synth2(Code[0],ac, Float.parseFloat(Code[2]),Float.parseFloat(Code[3]));
			//	I.setName(Code[0]);
				instMap.put(I.getName(), I);
				System.out.println("##synth2 " + I.getName());
			}	
			if (Code[1].equals("PERCUSSION"))	{
				Synth I=new percussion(Code[0],ac);
			//	I.setName(Code[0]);
				instMap.put(I.getName(), I);
				System.out.println("##percussion " + I.getName());
				
			}	
			if (Code[1].equals("SAMPLER"))	{
				Synth I=new Sampler(Code[0],ac);
			//	I.setName(Code[0]);
				instMap.put(I.getName(), I);
				System.out.println("##Sampler " + I.getName());
				
			}	
		}
/*		inst1.setName("inst1");
		inst12.setName("inst12");
		instMap.put(inst1.getName(), inst1);
		instMap.put(inst12.getName(), inst12); */
	}
	public void setCommandMetronoms(List<String> commandMetronoms) {
		System.out.println("COMMAND Metronoms:");
		for (String s:commandMetronoms){
			String[] Code=s.split("\\s+");
			if (Code[0].equals("NEW")) {
				Metronom m=new Metronom(ac, Code[1], Float.parseFloat(Code[2]));
				m.setPlayer(this);
				metroMap.put(m.getName(), m);	
				System.out.println("NAME:" + m.getName() + " Tempo:" + metroMap.get(m.getName()).tempo);
			}
			
		}
	/*	CommandMetronoms = commandMetronoms;
		Metronom m=new Metronom(ac, "metro1", 60);
		metroMap.put(m.getName(), m);
		Metronom m2=new Metronom(ac, "metro2", 60);
		metroMap.put(m2.getName(), m2);*/

	}
	public void setCommandSongs(List<String> commandSongs, String Mother,String MotherEvent) {
	//	CommandSongs = commandSongs;
	//	System.out.println("COMMAND Songs:");
		commands++;
		int line=0;
		for (String st:commandSongs){
			line++;
			String[] Code=st.split("\\s+");
		switch (Code[0]){
		case "NEW":
			if (Code[1].equals("DEFAULT")) {sDefault++;lastDefault="default"+sDefault;Code[1]=lastDefault;}
			// this would eventually be the metronom name for the new song
			String metronamep;
			// if the song is not direct child of a metronom, Mother==""
			if (!Mother.equals("")){
			if (metroMap.containsKey(Mother)) metronamep=Mother;
			else metronamep=songMap.get(Mother).timerName;
			//System.out.println(metroMap.get(metronamep).c.getCount()+": "+MotherEvent.split("#")[0]+"#"+line+": "+st);
			}
			activeEvents.add(MotherEvent.split("#")[0]);
			Gain gOut=new Gain (ac,1,(float) 0.3);
			Gain gIn=new Gain (ac,1,(float) 1.0);
			//if (Code.length==)
			PlaySet ps=new PlaySet(gIn,gOut);
			
			ps.gOut.addInput(ps.gIn);
		///////////	
		/*	Gain gOut=new Gain(ac,1,0.3);
		 * filterMap.get("name")(g,gOut);
			MasterGain.addInput(f.gOut); */
	    ///////////
			MasterGain.addInput(gOut);
			
			Song s=new Song(Code[1],scoreMap.get(Code[2]),instMap.get(Code[3]), ps);
			s.setInstMap(instMap);
			s.setMother(Mother);
			s.MotherEvent=MotherEvent.split("#")[0]+"#"+line;
			for (int i=1;i<MotherEvent.split("#").length;i++) s.MotherEvent+="#"+MotherEvent.split("#")[i];
			s.MotherEvent=concise(s.MotherEvent);
			songHistory.put(Code[1], s.MotherEvent);
			System.out.println("s . Mother Event: "+s.MotherEvent);
			songMap.put(s.getName(), s);
			Gtree.g.addVertex(Code[1]);
			 System.out.println("Vertex Count:"+ Gtree.g.getVertexCount());
			 
			if (!Mother.equals("")){updateLabels(updateEvents()); 
			if (MotherEvent.split("#").length>1)
			Gtree.g.addEdge(MotherEvent.split("#")[1]+","+MotherEvent.split("#")[0]+"_"+commands,Mother, Code[1]);}
			if(Gtree.g.getVertexCount()>15){
				 List<String> d=new ArrayList<String>();
				 for (String sdummy:Gtree.g.getVertices()){
					 System.out.println("root: "+sdummy+ " "+ Gtree.g.getPredecessorCount(sdummy));
					 if (Gtree.g.getPredecessorCount(sdummy)==0) 
					
					 d.add(sdummy);
				 }
				// while (Gtree.g.getVertexCount()>15){
				 int rand=(int)Math.floor(Math.random()*d.size());
				 System.out.println(d.get(rand) + " REMOVED!");
				// for (String sdummy:Gtree.g.getInEdges(d.get(rand))) Gtree.g.removeEdge(sdummy);
				 Gtree.g.removeVertex(d.get(rand));
				// Gtree.g.removeEdge(arg0)
				d.remove(d.get(rand));
			//	 }
			
				 System.out.println(Gtree.g.toString());
				 
			 }	
			if (Gtree.g.getVertexCount()%1==0) Gtree.draw();
			break;
	/*	case "FILTER":
			if (Code[1].equals("DEFAULT")) Code[1]=lastDefault;
			if (Code[2].equals("echo")) {
				songMap.get(Code[1]).ps.gOut.clearInputConnections();
				Glide gl=new Glide(ac,Float.parseFloat(Code[2]),200);
				songMap.get(Code[1]).ps.newGlide("echodelay", gl);
				Glide gl=new Glide(ac,Float.parseFloat(Code[3]),200);
				songMap.get(Code[1]).ps.newGlide("echodelay", gl);
			}
			
				break;*/
		case "GAIN":
			if (Code[1].equals("DEFAULT")) Code[1]=lastDefault;
			if (Code[1].equals("QUERY")) {
				
				for(String sss:songMap.keySet())
					if (songMap.get(sss).MotherEvent.contains(Code[2])) 
						songMap.get(sss).ps.gOut.setGain(songMap.get(sss).ps.gOut.getGain()*Float.parseFloat(Code[3]));
				
			} else
			songMap.get(Code[1]).ps.gOut.setGain(Float.parseFloat(Code[2]));
				break;
		case "REPEAT":
			if (Code[1].equals("DEFAULT")) Code[1]=lastDefault;
			if (Code[2].equals("TRUE")) songMap.get(Code[1]).repeat=true;
			if (Code[2].equals("FALSE")) songMap.get(Code[1]).repeat=false;
				break;
		case "OFFSET":
			if (Code[1].equals("DEFAULT")) Code[1]=lastDefault;
			float o=0;
			o=Float.parseFloat(Code[2])*32;
			if (Code.length>3) if (Code[3].equals("BAR")) o*=4;
			songMap.get(Code[1]).offset+=(int) o;
				break;
		case "METRONOM":
			if (Code[1].equals("DEFAULT")) Code[1]=lastDefault;
			String metroname;
			metroname=Code[2];
			if (Code[2].equals("MOTHER"))  
			{if (metroMap.containsKey(Mother)) metroname=Mother;
			else metroname=songMap.get(Mother).timerName;
			}
			
			 
			metroMap.get(metroname).addSong(songMap.get(Code[1]));
			songMap.get(Code[1]).timerName=metroMap.get(metroname).getName();
			songMap.get(Code[1]).offset+=(int)metroMap.get(metroname).c.getCount();
			break;
		case "PITCH":
			if (Code[1].equals("DEFAULT")) Code[1]=lastDefault;
			songMap.get(Code[1]).pitchShift=Float.parseFloat(Code[2]);
			break;
		case "SELFINST":
			if (Code[1].equals("DEFAULT")) Code[1]=lastDefault;
			if (Code[2].equals("FALSE")) 
				songMap.get(Code[1]).INSTRUMENT_VARIABLE=true; 
			else songMap.get(Code[1]).INSTRUMENT_VARIABLE=false;
			break;
		case "SONGEVENT":
			if (Code[1].equals("DEFAULT")) Code[1]=lastDefault;
			PlayerEvent e=new PlayerEvent();
			e.name=Code[3];
			if (playerEvents.containsKey(e.name)) {
				playerEvents.get(e.name).ocasion=new String[Code.length];
			System.arraycopy( Code, 0, playerEvents.get(e.name).ocasion, 0, Code.length );
			//System.out.println(e.name + " Updated");
			}
			else { 
				e.ocasion=new String[Code.length];
				System.arraycopy( Code, 0, e.ocasion, 0, Code.length );
				playerEvents.put(e.name, e); System.out.println(e.name + " Added");}
			String ss=MotherEvent.split("#")[0]+"#"+line;
			for (int i=1;i<MotherEvent.split("#").length;i++) ss+="#"+MotherEvent.split("#")[i];
			ss=concise(ss);
		//	System.out.println(ss);
			if (Code.length>5) {
				if (Code[5].equals("ONCE")) 
					songMap.get(Code[1]).songEvents.put(Code[2]+"#"+Code[4]+"#"+"ONCE#"+ss, e.name);} 
			else
				{
			if (Code.length>4) songMap.get(Code[1]).songEvents.put(Code[2]+"#"+Code[4]+"#"+ss, e.name);
			
			else songMap.get(Code[1]).songEvents.put(Code[2]+"#"+ss, e.name);}
			break;
		case "METROEVENT":
			if (Code[2].equals("DEFAULT")) {sDefault++;lastDefault="default"+sDefault;Code[2]=lastDefault;}
			 e=new PlayerEvent();
			e.name=Code[3];
			
			
			metroMap.get(Code[1]).addEvent(Code[2],Code[3],Code[4],Code[5]);
			
			if (playerEvents.containsKey(e.name)) {
				playerEvents.get(e.name).ocasion=new String[Code.length];
			System.arraycopy( Code, 0, playerEvents.get(e.name).ocasion, 0, Code.length );
			//System.out.println(e.name + " Updated");
			}
			else { 
				e.ocasion=new String[Code.length];
				System.arraycopy( Code, 0, e.ocasion, 0, Code.length );
				playerEvents.put(e.name, e); System.out.println(e.name + " Added");}
			 break;
		case "ACTUALIZE":
			double r=Math.random();
			if (r<= Float.parseFloat(Code[2])/100.0){
				if (!Mother.equals("")){
				String metronamepp;
				if (metroMap.containsKey(Mother)) metronamepp=Mother;
				else metronamepp=songMap.get(Mother).timerName;
				System.out.println(metroMap.get(metronamepp).c.getCount()+": "+MotherEvent.split("#")[0]+": "+st);} 
				else System.out.println("Command"+": "+MotherEvent+": "+st);
				String ssp=MotherEvent.split("#")[0]+"#"+line;
				for (int i=1;i<MotherEvent.split("#").length;i++) ssp+="#"+MotherEvent.split("#")[i];
				//System.out.println(ss);
				G.g.addEdge(MotherEvent.split("#")[0]+","+playerEvents.get(Code[1]).name,MotherEvent.split("#")[0],playerEvents.get(Code[1]).name);
		   setCommandSongs(playerEvents.get(Code[1]).command,Mother,playerEvents.get(Code[1]).name+"#"+ssp);
		   playerEvents.get(Code[1]).codeEvent.run(this, Mother);
		   }
			break;
		case "KILL":
			
				List<String> deadlist=new ArrayList<String>();
				float rand=0;
				if (Code[1].equals("QUERY")) {
				for(String sss:songMap.keySet())
					if (songMap.get(sss).MotherEvent.contains(Code[2])) deadlist.add(sss);
				rand=Float.parseFloat(Code[3]);
				}
				else {deadlist.add(Code[1]); rand=Float.parseFloat(Code[2]);}
				if  (Math.random()<rand/100)
				for(String sss:deadlist) {
					songMap.get(sss).ps.gIn.kill();
					songMap.get(sss).ps.gOut.kill();
					metroMap.get(songMap.get(sss).timerName).SongMap.remove(sss);
					songMap.remove(sss);
					
					
				
			}
			
		}
		}
		
		/*
		//Song s=new Song( "song1","inst1 0");
		////// Song s=new Song("song1", scoreMap.get("alibaba"), "inst1", 0);
		s.setInstMap(instMap);
	//	s.setScore(scoreMap.get("alibaba"));
		 s.repeat=true;
	    metroMap.get("metro1").addSong(s);
	    songMap.put(s.getName(), s);
	    
	    
//	     s=new Song( "song2","inst12 7");
	   ////// s=new Song("song2", scoreMap.get("alibaba"), "inst12", 7);
	     s.offset=8*16;
	     s.setInstMap(instMap);
	
//	 	s.setScore(scoreMap.get("alibaba"));
		// s.repeat=true;
	    metroMap.get("metro2").addSong(s);
	    songMap.put(s.getName(), s); */
	    
	}
	public void start() {
		// TODO Auto-generated method stub
		ac.out.addInput(MasterGain);
		ac.start(); 
		for (String s: metroMap.keySet()) metroMap.get(s).start();
	//	metroMap.get("metro2").start();
	}
	public void fromFile(String filename) {
		Path file = FileSystems.getDefault().getPath(filename);
		System.out.println(file.toString());
		try {
			BufferedReader reader = Files.newBufferedReader(file);
			String line=null;
			
			line=reader.readLine();
			while (line!=null){
			
		//		System.out.println(line);
				List<String> command=new ArrayList<String>();
		//		line.replaceAll("\\s+", "");
				Pattern p=Pattern.compile("\\s+");
				Matcher m=p.matcher(line);
				line=m.replaceAll("");
				System.out.println(line + "END");
				switch(line){
				case "#METRONOMS":
					command.clear();
					line=reader.readLine();
					while (!endOfSection(line)) { command.add(line); line=reader.readLine();}
					System.out.println("THIS IS METRONOMS");
					for (String s:command) System.out.println(s);
					setCommandMetronoms(command);
					break;
				case "#SCORES":
					command.clear();
					line=reader.readLine();
					while (!endOfSection(line)) { command.add(line); line=reader.readLine();}
					System.out.println("THIS IS SCORES");
					for (String s:command) System.out.println(s);
					setCommandScores(command);
					break;
				
				case "#SONGS":
					command.clear();
					line=reader.readLine();
					while (!endOfSection(line)) { 
						command.add(line); 
						line=reader.readLine();}
					System.out.println("THIS IS SONGS");
					for (String s:command) System.out.println(s);
					setCommandSongs(command,"","");
					break;
				case "#INSTRUMENTS":
					command.clear();
					line=reader.readLine();
					while (!endOfSection(line)) { command.add(line); line=reader.readLine();}
					System.out.println("THIS IS INSTRUMENTSS");
					for (String s:command) System.out.println(s);
					setCommandInstruments(command);
					break;
				case "#EVENT":
					command.clear();
					line=reader.readLine();
					PlayerEvent e=new PlayerEvent();
					e.name=line.trim();
					e.number=eventCount;
					eventCount++;
					line=reader.readLine();
					while (!endOfSection(line)) { command.add(line); line=reader.readLine();}
					System.out.println("THIS IS "+e.name);
					for (String s:command) System.out.println(s);
					
					
					if (playerEvents.containsKey(e.name)) {
						playerEvents.get(e.name).command=new ArrayList<>(command); //System.out.println(e.name + " Updated");
						}
					else { e.command=new ArrayList<>(command);playerEvents.put(e.name, e); System.out.println(e.name + " Added");}
					break;
				default:
					if (line!=null) line=reader.readLine();
				
				}
				
			}
				
			G=new GraphVis(length);
			setcolor2 sc2=new setcolor2();
			Gtree=new GraphVis2(new setcolor(),sc2,songHistory);
			  Transformer<String,Paint> scc   =  new Transformer<String,Paint>() {
					public Paint transform(String input) {
						if (songHistory.containsKey(input)){System.out.println("OTHER "+songHistory.get(input).split("#")[0]);
						return new setcolor().transform(songHistory.get(input).split("#")[0]); }
						else { System.out.println("WHITE "+input);return Color.white;}
					}};
					 Gtree.vv.getRenderContext().setVertexFillPaintTransformer(scc);
		//	while ((line=reader.readLine())!=null) {System.out.println(line);}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setUplabels();
		
	}
	
	/*class setcolor implements Transformer<String,Paint>{

		@Override
		public Paint transform(String s) {
			// TODO Auto-generated method stub
			if (activeEvents.contains(s)) return Color.green; else
			 return Color.white;
		}}*/
	class setcolor2 implements Transformer<String,Paint>{

		@Override
		public Paint transform(String s) {
			// TODO Auto-generated method stub
	   if (songMap.containsKey(s)) {if (songMap.get(s).timerName.equals("metro1")) return Color.green;
	   if (songMap.get(s).timerName.equals("metro2")) return Color.red;}
	  
		return Color.white;
		}
		}
	class setcolor implements Transformer<String,Paint>{

		@Override
		public Paint transform(String s) {
			// TODO Auto-generated method stub
		String stat="";
		for (String s1:metroMap.keySet()){
			for (String s2:metroMap.get(s1).SongMap.keySet())
			{
				Song song=metroMap.get(s1).SongMap.get(s2);
				if (song.MotherEvent.split("#")[0].equals(s)) stat+=song.timerName;
			}
			
		}
		if (stat.equals("metro1")) return Color.green;
		if (stat.equals("metro2")) return Color.red;
		if ((stat.equals("metro1metro2"))||(stat.equals("metro2metro1"))) return Color.gray;
		return Color.white;
		}
		}
	class setlength implements Transformer<String,Integer>{

		@Override
		public Integer transform(String s) {
			// TODO Auto-generated method stub
			
			 return -50;
		}}
	public setlength length;
	private void setUplabels() {
		for (int i=0;i<eventCount;i++){
			for (String s:playerEvents.keySet()){ if (playerEvents.get(s).number==i){
				G.g.addVertex(s);
				Transformer<String,Paint> vertexPaint = new Transformer<String,Paint>() {
					 public Paint transform(String s) {
					 return Color.WHITE;
					 }};
					 G.vv.getRenderContext().setVertexFillPaintTransformer(new setcolor());
					Gtree.draw();
				JLabel lblThisIsA = new JLabel(playerEvents.get(s).name);
		//	    lblThisIsA.setForeground(Color.RED);
			    lblThisIsA.setFont(new Font("Tahoma", Font.BOLD, 11));
			   // alibaba.AlibabaPlayer.test(lblThisIsA);
			    labelMap.put(playerEvents.get(s).name, lblThisIsA);
			    alibaba.AlibabaPlayer.panel.add(lblThisIsA);
			  //  labelMap.get("loop1_1").setForeground(Color.RED);
			    alibaba.AlibabaPlayer.frame.validate();
			}
			}
			
		}
		G.layout.initialize();
	//	G.draw();
	    alibaba.AlibabaPlayer.frame.validate();	
	}
	
	private static String concise(String s){
		String[] c=s.split("#");
		String scon="";
		List<String> l=new ArrayList<String>();
		for (int i=0;i<c.length/2;i++)  
			if (!l.contains(c[2*i]+"#"+c[2*i+1]))
			{ l.add(c[2*i]+"#"+c[2*i+1]);if (i!=0)scon+="#"; scon+=c[2*i]+"#"+c[2*i+1]; }
		return scon;}
	static boolean endOfSection(String S){ if (S==null) return true; if (S.matches("^#(\\w|\\s)*")) return true; return false;}
	public void endOfSong(String name) {
		//System.out.println("End of Song " + name); 
		if (!songMap.containsKey(name)) return;
		Map<String,Song> m= new HashMap<String,Song>(songMap);
		songMap.get(name).ps.gIn.kill();
		songMap.get(name).ps.gOut.kill();
	/*	if (songMap.get(name).EVENTS.containsKey("ONEND")) 
		{ boolean dont=false;
//		 if (Events.get(songMap.get(name).EVENTS.get("ONEND")).ocasion.length==5)
		  float rand=Float.parseFloat(Events.get(songMap.get(name).EVENTS.get("ONEND")).ocasion[4])/(float)100.0;
		   System.out.println("Probability of action:" + rand);
		   if (Math.random()> rand) dont=true;
		 
			if (!dont) {setCommandSongs(Events.get(songMap.get(name).EVENTS.get("ONEND")).command,name);
			Events.get(songMap.get(name).EVENTS.get("ONEND")).e.event(this,name);
			}
			
		} */
		 Pattern p=Pattern.compile("ONEND([\\w\\W]*)");
		 for (String songEventKey:songMap.get(name).songEvents.keySet()){
		 Matcher mtch=p.matcher(songEventKey);
		 if (mtch.matches()){
			 boolean dont=false;
//			 if (Events.get(songMap.get(name).EVENTS.get("ONEND")).ocasion.length==5)
			 PlayerEvent playerEventOnTheEndOfSong;
			// Hash
			 playerEventOnTheEndOfSong=playerEvents.get(songMap.get(name).songEvents.get(songEventKey));
			 String[] cc=mtch.group(1).split("#");
			 
			 
			  float rand=Float.parseFloat(cc[1])/(float)100.0;
			  // System.out.println("Probability of action:" + rand);
			   if (Math.random()> rand) dont=true;
			   int once=0;
			   // the rather absurd structure I used here , songEventKey=ONEND#probability#ONCE[?]#generating event name# generating line
			   // therefore cc[2+once] is the generating event's name. 
			   if (cc[2].equals("ONCE")){
				   once=1;
			 for (String playingSong:songMap.keySet()){ 
				 System.out.println(playingSong+"   "+songMap.get(playingSong).MotherEvent+ " "+name);
				 
				if (!playingSong.equals(name)) if (songMap.get(playingSong).MotherEvent.contains(cc[3]+"#"+cc[4]))
			      //that is the event name and line of the generating command
					 {dont=true;}}}
				if (!dont) {
					System.out.println(metroMap.get(songMap.get(name).timerName).c.getCount()+":"+songMap.get(name).MotherEvent.split("#")[0]+": Going to run "+playerEventOnTheEndOfSong.name);
					//if (e.name.equals("event7"))
					//{System.out.println("event7!!!");}
					String s="";
					for (int i=2+once;i<cc.length;i++) s+="#"+cc[i];
				//	System.out.println(s);
					G.g.addEdge(cc[2+once]+","+playerEventOnTheEndOfSong.name,cc[2+once], playerEventOnTheEndOfSong.name);
					//G.vv.repaint();
					System.out.println("EDGE:" + cc[2 + once] + "," + playerEventOnTheEndOfSong.name);
					//G.layout.initialize();
					setCommandSongs(playerEventOnTheEndOfSong.command,name,playerEventOnTheEndOfSong.name +s
							);
				playerEventOnTheEndOfSong.codeEvent.run(this,name);
			 
		 }
				else{
					String S2="OTHERWISE"+cc[0];
			//		if (songMap.get(name).EVENTS.containsKey(S2))
					for (String ss:songMap.get(name).songEvents.keySet())	
						if (ss.split("#")[0].equals(S2))
					{playerEventOnTheEndOfSong=playerEvents.get(songMap.get(name).songEvents.get(ss));
					System.out.println(metroMap.get(songMap.get(name).timerName).c.getCount()+":"+songMap.get(name).MotherEvent.split("#")[0]+"Going to run "+playerEventOnTheEndOfSong.name);
					String s="";
					cc=ss.split("#");
					for (int i=1;i<cc.length;i++) s+="#"+cc[i];
					//System.out.println(s);	
					G.g.addEdge(cc[2+once]+","+playerEventOnTheEndOfSong.name,cc[1], playerEventOnTheEndOfSong.name);
					setCommandSongs(playerEventOnTheEndOfSong.command,name,playerEventOnTheEndOfSong.name+s);
					playerEventOnTheEndOfSong.codeEvent.run(this,name);}
				}
		 
		 }}
		
		
		Map<String,Song> m2= new HashMap<String,Song>(songMap);
		for (String s: m.keySet()) m2.remove(s);
		for (String s: m2.keySet()) { m2.get(s).update((int)metroMap.get((m2.get(s).timerName)).c.getCount(),metroMap.get((m2.get(s).timerName)).tempo);
		// System.out.println(m2.get(s).getName() + "Updated!");
		 }
		boolean dont=false;
		String s10,s11,s12;
		
		//metroMap
		String s=songMap.get(name).MotherEvent;
	//	if (!dont) 
	//	activeEvents.remove(songMap.get(name).MotherEvent.split("#")[0]);
		if (name.indexOf("default")!=-1) {songMap.remove(name);// System.out.println(name + " removed!");
		} 
		else songMap.get(name).offset=0;
		activeEvents.clear();
		for (String sss:songMap.keySet()) 
			// System.out.println(sss+"   "+songMap.get(sss).MotherEvent+ " "+name);
			
		
				
				activeEvents.add(songMap.get(sss).MotherEvent.split("#")[0]);
					
		
	}
	public void addCodeEvent(String string, CodeEvent codeEvent) {
		playerEvents.get(string).setCodeEvent(codeEvent);
		
	}
	public PlaySet lastDefaultSongPs() {
		// TODO Auto-generated method stub
		return songMap.get(lastDefault).ps;
	}
	public PlaySet SongPs(String name) {
		// TODO Auto-generated method stub
		return songMap.get(name).ps;
	}
	public void setCommandInstruments(String string) {
		List<String> command=Arrays.asList(string);
		setCommandInstruments(command);
		
	}
	public void setCommandSongs(String string,String Mother) {
		List<String> command=Arrays.asList(string);
		setCommandSongs(command,Mother,"");
		
	}
	public void setCommandMetronoms(String string) {
		List<String> command=Arrays.asList(string);
		setCommandMetronoms(command);
		
	}
	public void setCommandScores(String string) {
		List<String> command=Arrays.asList(string);
		setCommandScores(command);
		
	}
	public List<String> updateEvents() {
		List<String> eventlist=new ArrayList<String>();
		for (Song s:songMap.values()) if (!eventlist.contains(s.MotherEvent)) eventlist.add(s.MotherEvent.split("#")[0]);
		
		//updateLabels(eventlist);
		return eventlist;
	}
	public void updateLabels(List<String> eventlist){for (String s:playerEvents.keySet()) if (eventlist.contains(s)) labelMap.get(s).setForeground(Color.red); else
		labelMap.get(s).setForeground(Color.black);}

}
