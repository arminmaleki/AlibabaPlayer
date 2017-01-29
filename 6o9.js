var inst1=new Synth1("",ac,0.01,100.0)
var inst12=new Synth1("",ac,0.025,20)
var I1=new Synth1("",ac,0.0,0.0)
var P1=new Percussion()
var S1=new Sampler()
var inst22=Synth2("",ac,0.025,20)

var scAlibaba=newScore({FILE:"alibaba.scr"})
var scRythm1=newScore({FILE:"rythm1p1.scr"})
var scRythm2=newScore({FILE:"rythm1p2.scr"})
var scRythm3=newScore({FILE:"rythm1p3.scr"})
var scRythm4=newScore({FILE:"rythm1p4.scr"})
var scMelo1=newScore({FILE:"rythm1s1.scr"})
var scMelo2=newScore({FILE:"rythm1s2.scr"})
var scMelo3=newScore({FILE:"rythm1s3.scr"})
var scMelo4=newScore({FILE:"rythm1s4.scr"})
var scMelo5=newScore({FILE:"rythm1s5.scr"})

newMetro({TEMPO:300,DEFAULT:true})


    newSong($,{SILENCE:6}).OnEnd("loop1_1")
    newSong($,{SILENCE:9}).OnEnd("loop2_1")


newEvent("loop1_1",function($){
    newSong($,{SCORE:scRythm1,PITCH:2.0,INSTRUMENT:P1}).
	Percent(50.0).OnEnd("loop1_2").Otherwise().OnEnd("bif1")
    if (rnd(1)<0.3) events.ornament1($)
})

newEvent("loop1_2",function($){
    newSong($,{SCORE:scRythm2,INSTRUMENT:P1,PITCH:2.0}).
	Percent(80.0).OnEnd("loop1_1").Otherwise().OnEnd("interlude1")
})

newEvent("loop2_1",function($){
    newSong($,{SCORE:scRythm1,INSTRUMENT:P1,PITCH:5.0}).OnEnd("loop2_2")
    if (rnd(1)<0.6) events.ornament2($)
})
var pInter=2.0;var i=0;
newEvent("interlude1",function($){
    
    s=newSong($,{SCORE:scRythm1,INSTRUMENT:P1,PITCH:pInter})
    if (i<2) s.OnEnd("interlude1")
    else s.OnEnd("loop2_1)")
    i+=1
    pInter+=1.75
})
newEvent("ornament1",function($){newSong($,{SCORE:scMelo1,INSTRUMENT:S1})})
