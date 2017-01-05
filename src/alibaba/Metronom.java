package alibaba;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.ugens.Clock;

//import net.beadsproject.beads.ugens.Gain;

public class Metronom{
	
	//Player.Event EVENTS;
//	Map<String,Player.Event> EVENTS=new HashMap<String,Player.Event>();
	
	AudioContext ac;
Clock c;
//Gain MasterGain;

String name;
float tempo;

int currentTime;
int currentBeat;
int offSetBeat;
float offSetTime;

Song s;
Player player=null;
public class MetroEvent {
	 boolean periodic=true; int period=8; int offset=0; 
String name; String playerEvent;
 float probability;
MetroEvent(){}
} 

Map<String,Song> SongMap=new HashMap<String,Song>();
Map<String,MetroEvent> metroEvents=new HashMap<String,MetroEvent>();
public void setPlayer(Player player){ this.player=player; }

public Metronom(AudioContext ac,String name, float tempo) {
	super();
	this.ac = ac;
	this.name = name;
	this.tempo = tempo;
//	this.MasterGain=MasterGain;
//	s=new Song(this.ac, this.MasterGain, "SongMetronom",7);
//	s.offset=64;
	float step=(float) (60000.0/tempo);
	c=new Clock(ac,step);

	//Envelope ie=new Envelope(ac, (float) 500.5);
	//c.setIntervalEnvelope(ie);
			c.setName(name);
//	c.set
			c.setTicksPerBeat(32);
	System.out.println("METRONOM "+c.getName()+ " COUNT"+c.getCount()+" tickperbeat: "+c.getTicksPerBeat());
	c.addMessageListener(new Bead(){
		  public void messageReceived(Bead message) {
		        Clock c = (Clock)message;
		        if(c.isBeat()&&player!=null)//  AlibabaPlayer.tik(inst1);
		        {	//System.out.println("Metronom "+name+" "+c.getBeatCount());
		      //  System.out.print("All Songs are:");
		      //  printAllSongs();
		        //	System.out.println(name);
		        for (MetroEvent me: metroEvents.values()){
		        	if (me.periodic) {if (c.getBeatCount()%me.period == me.offset) {System.out.println("METRONOM " + name + " SAIS " + me.playerEvent+ " "+c.getBeatCount());
		        	 player.setCommandSongs(player.playerEvents.get(me.playerEvent).command, name,player.playerEvents.get(me.playerEvent).name);
		       
		        	
		        	 player.playerEvents.get(me.playerEvent).codeEvent.run(player,name);
		        	}
		        	}
		        	else if (c.getBeatCount()==me.offset)	{//System.out.println("METRONOM " + name + " SAIS " + me.event+" ONCE AND FOR ALL"+ " "+c.getBeatCount());	
		        	player.setCommandSongs(player.playerEvents.get(me.playerEvent).command, name,player.playerEvents.get(me.playerEvent).name);
		        	player.playerEvents.get(me.playerEvent).codeEvent.run(player,name);
		        	}
		        }	
		        //	player.setCommandSongs(commandSongs, name);
		    }
		    // s.update((int)c.getCount());
		        
		        List<String> deadNames=new ArrayList<String>();
		        
		        for (Song ss:SongMap.values())
		        if (!ss.update((int)c.getCount(),c.getTempo()))
		        {//SongMap.remove(ss.name); 
		         deadNames.add(ss.name);
		        }
		        for (String S:deadNames){player.endOfSong(S);SongMap.remove(S);}
		        if (deadNames.size()!=0) {
		        	player.updateLabels(player.updateEvents());
		        	//for (String s:player.updateEvents()) System.out.print(s + " "); System.out.println("Metro Done");
		        	}
		        
		        }
	});
	c.pause(true);
	
	ac.out.addDependent(c);
}
void start(){c.start();}
void stop(){c.pause(true);}
void addEvent(String code2, String code3, String code4, String code5){
	MetroEvent em=new MetroEvent();
	em.name=code2;
	em.playerEvent=code3;
	em.probability= Float.parseFloat(code5)/(float)100.0;
	Pattern p;
	Matcher m;
	 p=Pattern.compile("(\\d+)%(\\d+)");
	 m=p.matcher(code4);
	 if (m.find()) {//System.out.println("MATCHER: 0:"+m.group(0)+" 1:"+m.group(1)+" 2:"+m.group(2));
	 em.periodic=true; em.period=Integer.parseInt(m.group(2)); em.offset=Integer.parseInt(m.group(1));} else {
		 em.periodic=false; em.offset=Integer.parseInt(code4);// System.out.println("METRO NON PERIODIC "+em.offset);
	 }
metroEvents.put(em.name, em);
	
}
void addSong(Song S){
	SongMap.put(S.name, S);
//	System.out.println("My name is " + name + " . My Songs Are:");
//	 printAllSongs();
}
void printAllSongs(){for (String key:SongMap.keySet()) System.out.print (key+" son of "+ SongMap.get(key).getMother()+","); System.out.println("");}
void setNewTempo(float tempo){
	offSetBeat+=currentBeat;
	offSetTime+=currentTime;
	
}
public String getName() {
	
	return name;
}

}
