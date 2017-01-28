var Synth1=Java.type('alibaba.synth.Synth1');
var Score=Java.type('alibaba.Score');
var Song=Java.type('alibaba.Song');
var Metronom=Java.type('alibaba.Metronom');
var PlaySet=Java.type('alibaba.PlaySet');
var History=Java.type('alibaba.History');
var Note=Java.type('alibaba.note');
var events={};

function merge(o1,o2){
    o=Object.create(o1);
    for (var key in o2) o[key]=o2[key];
    return o;}

var inst0=new Synth1("inst",ac,0.01,100.0);

function newNote(o){
    if ("SILENCE" in o) return new Note(o.SILENCE);
    var o0={INSTRUMENT:"",PITCH:0.0,TIME:0.0,DURATION:0.0,MOD:""};
    var o=merge(o0,o);
    return new Note(o.INSTRUMENT,o.PITCH,o.TIME,o.DURATION,o.MOD);
}
function newSong(history,o) {
    //can set up three types of songs:normal,silent,one note.
    var o0={NAME:"noname",INST:inst0,PLAYSET:master,SILENCE:-1};
    var o=merge(o0,o);
    if (o.SILENCE==-1 && !("NOTE" in o)) S=new Song(o.NAME,o.SCORE,o.INST,o.PLAYSET);
    else if (!("NOTE" in o)) S=new Song(o.SILENCE*1.0);
    else S=new Song(o.NOTE,o.INST,o.PLAYSET);
    S.history=new History(history);
    
    manager.addSong(S);
    S.addManager(manager);
    
    if ('METRO' in o) o.METRO.metro.addSong(S);
    var SongObj={};
    //surrogate object for Song Class. prob=probability of doing
    //the end action. done=if the previous action was successful
    //otherwise for the Otherwise Method.it acts only if the last
    //action hasn't been successful.
    SongObj.prob=1.0;
    SongObj.done=false;
    SongObj.otherwise=false;
    SongObj.song=S;
    SongObj.addMetro=function(m){m.metro.addSong(this.song);
    
				 return this;};
    SongObj.OnEnd=function(e){
	if ((!this.otherwise && Math.random()<this.prob)
	    ||(this.otherwise && this.done==false && Math.random()<this.prob)){
        scripter.pushScriptEvent(e,this.song);
	    this.done=true;} else this.done=false;
	
	return this;}
    SongObj.Otherwise=function(){this.otherwise=true;this.prob=1.0; return this;}
    SongObj.Percent=function(p){this.prob=p/100.0; return this;}
    return SongObj;
}
function newScore(o){
    return new Score(o.NAME,o.FILE);
}
function newMetro(o){
    if (!('NAME' in o)) {o.NAME="noname";};
    o.TEMPO=o.TEMPO*1.0;
    m=new Metronom(ac,o.NAME,o.TEMPO);
    var MetroObj={};
    MetroObj.metro=m;
    MetroObj.start=function(){this.metro.start(); return this;};
    return MetroObj;}
function newEvent(s,f){
    // s is event name as string. f is the js function.
    //it keeps manager and history uptodate with the action.
    manager.addEvent(s);
    events[s]=function($){
	manager.eventCalled(s,$);
	newhist=new History($);
	newhist.lastEventName=s;
	f(newhist);};
}
//the variable through which song and event graph information is pass
//ed.can be used as $.lastEventName and $.lastSong. outside of an event,$.lastSong.history.lastEvent
//"lastEventname" is the previous event. inside,its the current event.
var $=null;

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



