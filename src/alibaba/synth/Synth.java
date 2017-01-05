package alibaba.synth;

import alibaba.PlaySet;
import alibaba.note;

public interface Synth {
	
	
	
	public void play(note N, float tempo,PlaySet ps);
	public String getName() ;
	public void setName(String name);

}
