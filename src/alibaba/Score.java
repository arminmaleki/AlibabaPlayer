package alibaba;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Score {
String filename;
String name;
boolean NO_TIME=false;
boolean NO_INSTRUMENT=false;
boolean SET_INSTRUMENT=false;
String instrument="";
public List<note> noteList= new ArrayList<note>();
public Score(String name,String filename) {
	super();
	this.filename = filename;
	this.name=name;
	Path file = FileSystems.getDefault().getPath(filename);
	System.out.println(file.toString());
	try {
		BufferedReader reader = Files.newBufferedReader(file);
		String line=null;
		float dur=0;
		while ((line=reader.readLine())!=null) {//System.out.println(line);
		String[] l=line.split("\\s+");
	//	for (String s:l) System.out.print(s+"+");
	//	System.out.println("");
		//// COMMAND
		if (l[0].equals("#")){
			//System.out.println("COMMANDS:");
			switch (l[1]){
			case "NO_INSTRUMENT":
				NO_INSTRUMENT=true;
				System.out.println("COMMAND: NO INSTRUMENT");
				break;
			case "NO_TIME":
				NO_TIME=true;
				System.out.println("COMMAND: NO TIME");
				break;
			case "SET_INSTRUMENT":
				SET_INSTRUMENT=true;
				instrument=l[2];
				System.out.println("COMMAND: INSTRUMENT SET TO "+instrument);
				break;
			}
			
		}
		////
		else{
	
		int index=0;
		note newnote=new note();
		newnote.modification="";
		if (!NO_INSTRUMENT && !SET_INSTRUMENT) { newnote.inst=l[index];index++;}
		if (SET_INSTRUMENT) newnote.inst=instrument;
		if (!NO_TIME) 
			{
			if (!l[index].equals("NO_TIME")) 
			     {newnote.time=Float.parseFloat(l[index]);dur=newnote.time;index++;}
			else {newnote.time=dur; index++;}
			} else {newnote.time=dur;}
		if (!l[index].equals("SILENCE")){newnote.pitch=Float.parseFloat(l[index]); index++;} else {newnote.pitch=-1;newnote.modification="SILENCE";index++;}
		newnote.duration =Float.parseFloat(l[index]); dur+=newnote.duration; index++;
		if ((index<l.length)&& !newnote.modification.equals("SILENCE")) newnote.modification=l[index] ;
		System.out.println("inst:" + newnote.inst + " time:" + newnote.time + " pitch:" + newnote.pitch + " duration:"
				+ newnote.duration + " modification:" + newnote.modification);
		noteList.add(newnote);}
		}
	/*	for (note newnote:noteList) System.out.println("inst:" + newnote.inst + " time:" + newnote.time + " pitch:" + newnote.pitch + " duration:"
				+ newnote.duration + " modification:" + newnote.modification);*/
	} catch (IOException e) {
		System.out.println("file not found");
		e.printStackTrace();
	}
	
	
}
public String getName() {
	
	return name;
}

}
