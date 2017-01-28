
var inst=new Synth1("inst",ac,0.1,100.0);
var sc=newScore({NAME:"alibaba",FILE:"alibaba.scr"});
var m=newMetro({TEMPO:180});
var m2=newMetro({TEMPO:170});
///////////////
newEvent("loop_single",function ($) {
  newSong($,{SILENCE:1,METRO:m}).OnEnd("loop_single").Percent(15).OnEnd("base_3").Percent(15).OnEnd("base_2");
  newSong($,
    {NOTE: newNote({PITCH:3,DURATION:0.5})
  ,METRO:m});
}
);
////////////////
newEvent("loop_single2",function ($) {
  newSong($,{SILENCE:1,METRO:m2}).OnEnd("loop_single2").Percent(15).OnEnd("pitchnoise");
  /*newSong($,
    {NOTE: newNote({PITCH:10,DURATION:2})
  ,METRO:m2});*/
  newSong($,{METRO:m2,
    SCORE: new Score(newNote({PITCH:10,DURATION:0.5}))
            .addNote(newNote({PITCH:5 ,DURATION:0.5}))

  });
}
);
/////////////
newEvent("base_2",function ($) {
  newSong($,
    {NOTE: newNote({PITCH:-14,DURATION:8})
  ,METRO:m});
}
);
/////////////
newEvent("base_3",function ($) {
  newSong($,
    {NOTE: newNote({PITCH:-10,DURATION:8})
  ,METRO:m});
}
);
////////
newEvent("pitchnoise",function ($) {
  newSong($,
    {NOTE: newNote({PITCH:10.0+Math.random()*6,DURATION:0.25})
  ,METRO:m2}).Percent(90).OnEnd("pitchnoise");
}
);

events.loop_single($);
events.loop_single2($);
m.start();
m2.start();
