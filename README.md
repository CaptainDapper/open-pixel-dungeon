# Open Pixel Dungeon - Lobby/Framework

### Description
This essentially provides a lobby to choose which mod of the game to use. Not particularly useful right now, but as the number of published mods grows it could show it's strength! I am trying to build it into a proper framework, to make easy creation of games that can be ran in the lobby.

### Updates
0.1.0a - Update shattered to 0.2.1c  
0.1.0 - initial release

### Status

Work in Progress! There's a lot that needs to be done before I can feel good about calling it a framework

### How to Use
##### Your own game
To build your game into the framework, you will need the following sources linked to your game:

https://github.com/00-Evan/PD-classes  
https://github.com/sloanr333/open-pixel-dungeon

First, choose a one word string (authorPreface) of characters that best represents the author, or metaproject. examples are "watabou", "shattered", "opd". The biggest changes to your code are (roughly, I'll tune this list up later) the following:  
* You'll need to preface your .dat files with "authorPreface-", in order to place them in separate folders. If you plan on making multiple games, avoid .dat collision by "authorPreface-gamePreface-example.dat".  
* Similarly, you'll need to put all the assets for your game in a subfolder of assets. (Eg. Folder: "assets/authorPreface(or gamePreface, revisionPreface, whatever)/items.png" Code: "public static final String SND\_CLICK	= "authorPreface/snd\_click.mp3";)"
* All preferences are shared across games. This means if you need to add one, it needs to be added to the lobby code, too. You could use your game's own preferences class, but avoid key collisions with the Lobby's Preferences, please.
* don't extend Game or Scene; instead extend OPDGame and OPDScene. I might refactor them to LobbyGame and LobbyScene, or SuperGame SuperScene, idk I suck at naming stuff.
* see this: https://github.com/sloanr333/open-pixel-dungeon/blob/openpd/vagueinstructions.txt

##### Your own lobby
If you want to build it with Pixel Dungeon and Shattered Pixel Dungeon(for example) you need the following sources linked to the openpd branch:

https://github.com/00-Evan/PD-classes  
https://github.com/sloanr333/opd-shattered  
https://github.com/sloanr333/opd-watabou

Alternatively, you can download my latest build (may be buggy) here: http://www.openpixeldungeon.com/download/

### Disclaimer
Thank you! to:
https://github.com/watabou/pixel-dungeon  
https://github.com/00-Evan/shattered-pixel-dungeon  
http://www.reddit.com/r/PixelDungeon/ mods and community!

