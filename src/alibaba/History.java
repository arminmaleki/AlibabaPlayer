package alibaba;

public class History {
	public String lastEventName=null;
	public Song lastSong=null;
	public History(){}
	public History(History h) {
		// TODO Auto-generated constructor stub
		if (h!=null) {this.lastEventName=h.lastEventName;this.lastSong=h.lastSong;}
	}

}
