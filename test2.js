
var inst=new Synth1("inst",ac,0.1,100.0);
var sc=newScore({NAME:"alibaba",FILE:"alibaba.scr"});
var m=newMetro({TEMPO:180});

newEvent("e2",function($){
    newSong($,{NAME:"alibaba1",SCORE:sc}).addMetro(m)
	.Percent(50).OnEnd("e1").Otherwise().OnEnd("silence");
});


newEvent("e1",function($){
 //   print("event1 "+$.lastSong.name+" "+$.lastEventName);
    newSong($,{SCORE:sc,METRO:m,INST:inst}).OnEnd("e2");

});

newEvent("silence",function($){
    s=new Score(
	newNote({SILENCE:4.0})
    );
    s.addNote(newNote({PITCH:2,DURATION:4.0}));

    newSong($,{SILENCE:8}).addMetro(m).OnEnd("e1");
    newSong($,{
	       METRO:m,
	       INST:inst,
	NOTE: newNote({PITCH:12,DURATION:4.0})
    });
    newSong($,{METRO:m,SCORE:s});
   // newNote({PITCH:12,DURATION:8.0}).print();
});

events.e1($);
m.start();
