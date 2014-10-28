package com.opd.openpixeldungeon;

import com.opd.noosa.OPDGame;
import com.opd.noosa.OPDScene;

public class SubGames {
	public static SubGame[] all = {
		new watabouGame(),
		new easyGame(),
		new shatteredGame(),
		new mofoodGame()
	};
	
	private static class watabouGame extends SubGame {
		{
			title = "Pixel Dungeon (Vanilla)";
			refName = "vanilla";
			author = "watabou";
			version = "1.7.2a";
			versionCode = 62;
			titleSceneClass = com.watabou.pixeldungeon.scenes.TitleScene.class;
			canonicalName = "com.watabou.pixeldungeon.scenes";
			asset = "vanilla_icon.png"; 
		}
	}
	
	private static class easyGame extends SubGame {
		{
			title = "Easy Pixel Dungeon";
			refName = "easy";
			author = "painless";
			version = "3.0.15";
			versionCode = 77;
			titleSceneClass = com.painless.easy.scenes.TitleScene.class;
			canonicalName = "com.painless.easy.scenes";
			asset = "easy_icon.png";
		}
	}
	
	private static class shatteredGame extends SubGame {
		{
			title = "Shattered Pixel Dungeon";
			refName = "shattered";
			author = "00-Evan";
			version = "0.2.1c";
			versionCode = 12;
			titleSceneClass = com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene.class;
			canonicalName = "com.shatteredpixel.shatteredpixeldungeon.scenes";
			asset = "shattered_icon.png";
		}
	}
	
	private static class mofoodGame extends SubGame {
		{
			title = "Mo' Food Mod";
			refName = "mofood";
			author = "roastedlasagna";
			version = "1.1.3";
			versionCode = 59;
			titleSceneClass = com.watabou.mofoodpd.scenes.TitleScene.class;
			canonicalName = "com.watabou.mofoodpd.scenes";
			asset = "mofood_icon.png";
		}
	}
	
	public static class SubGame {
		public String title = "Lobby";
		public String refName = "lobby";
		public String author = "SloanReynolds";
		public String version = OPDGame.version;
		public int versionCode = OPDGame.versionCode;
		public Class<? extends OPDScene> titleSceneClass;
		public String canonicalName;
		public String asset;
	}
}
