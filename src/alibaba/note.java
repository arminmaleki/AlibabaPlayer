package alibaba;

public class note {
	  String inst;
	  public String modification;
	  public note(){}
	  public void print() {
		  System.out.println("inst:" + this.inst + " time:" + this.time + " pitch:" + this.pitch + " duration:"
					+ this.duration + " modification:" + this.modification);
	  }
	  public note(note n){
		  this.inst=n.inst;
		  this.modification=n.modification;
		  this.pitch=n.pitch;
		  this.duration=n.duration;
		  this.time=n.time;
		  
	  }
	  public note(String inst, float pitch, float time, float duration, String modification) {
		super();
		this.inst = inst;
		this.pitch = pitch;
		this.time = time;
		this.duration = duration;
		this.modification = modification;
	}
	public float pitch,time,duration;
	 
}
