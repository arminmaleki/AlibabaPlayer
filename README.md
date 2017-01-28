Alibaba Player is a stochastic music sequencer  and visualizer coded in Java/JavaScript which accepts JavaScript “scripts” as instruction to make/play musics stochastically, in addition to accepting sheet music-like scores to play. Thus, Alibaba Player can create a sort of music which is simultaneously stochastic in nature and also incorporates familiar melodies (like a famous homonym nursery Rhyme Alibaba).

It works with a particular “Song” class which is based on the concept of “Bead” (by Ollie Bown and AL whose project are used as the basic audio library of Alibaba Player) and “Events” which decide what happens after a “Song” is played. “Events” are implemented as JavaScript functions. Each “Event” is represented as a vertex in the visualization graph and you can see how the musics control shifts between various Events. For the visualization, Alibaba Player uses Jung’s graph visualization library.

Alibaba Player includes a number of synthesizers and filters, written using “Beads” library and compiled in Java which can be accessed through the JavaScript interface. It interprets the JavaScript input using “Nasshorn”, JavaScript motor of Java 8.

As a future development, Alibaba Player would include a Web App/Daemon “chiBaba” which would provide additional instructions for Alibaba  Player and makes collective manipulation of a stream of sound possible (for example for a number of people in a gallery room) so that people can attend musical concerts by themselves.


Used libraries:
http://www.beadsproject.net/
https://github.com/jrtom/jung