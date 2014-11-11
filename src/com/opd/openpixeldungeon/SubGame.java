package com.opd.openpixeldungeon;


public class SubGame {
	public String title = "Lobby";
	public String refName = "lobby";
	public String author = "SloanReynolds";
	public String version = OPDGame.version;
	public int versionCode = OPDGame.versionCode;
	public String titleSceneClassPath;
	public String asset;
	
	public Class<? extends OPDScene> titleClass;

	public static class easy extends SubGame {
		{
		  title= "Easy Dungeon";
		  refName= "easy";
		  author= "painless";
		  version= "3.0.15";
		  versionCode= 77;
		  titleSceneClassPath= "com.painless.easy.scenes.TitleScene";
		  asset= "easy_icon.png";
		}
	}
	public static class vanilla extends SubGame {
		{
		  title= "Pixel Dungeon (Vanilla)";
		  refName= "vanilla";
		  author= "watabou";
		  version= "1.7.2a";
		  versionCode= 62;
		  titleSceneClassPath= "com.watabou.pixeldungeon.scenes.TitleScene";
		  asset= "vanilla_icon.png";
		}
	}
	public static class shattered extends SubGame {
		{
		  title= "Shattered Pixel Dungeon";
		  refName= "shattered";
		  author= "00-Evan";
		  version= "0.2.1c";
		  versionCode= 12;
		  titleSceneClassPath= "com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene";
		  asset= "shattered_icon.png";
		}
	}
	public static class mofood extends SubGame {
		{
		  title= "Mo' Food Mod";
		  refName= "mofood";
		  author= "roastedlasagna";
		  version= "1.1.4a";
		  versionCode= 59;
		  titleSceneClassPath= "com.watabou.mofoodpd.scenes.TitleScene";
		  asset= "mofood_icon.png";
		}
	}
	public static class classy extends SubGame {
		{
		  title= "Classy PD";
		  refName= "classy";
		  author= "SloanReynolds";
		  version= "0.0.0";
		  versionCode= 0;
		  titleSceneClassPath= "com.opd.classy.scenes.TitleScene";
		  asset= "classy_icon.png";
		}
	}
}