/*
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.opd.openpixeldungeon;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.watabou.noosa.Game;
import com.watabou.noosa.Scene;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;

public class OPDGame extends Game {
	public OPDGame(Class<? extends Scene> c) {
		super( c );
		origScene = c;
	}

	private static OPDGame opdInstance;
	
	/* SUB GAMES */
	private ArrayList<SubGame> subGames;
	private SubGame currentSubGame;
	private Class<? extends Scene> origScene;
	 
	public static ArrayList<SubGame> subGames() {
		return opdInstance.subGames == null ? opdInstance.subGames = SubGames.load() : opdInstance.subGames;
	}
	
	public static SubGame currentSubGame() {
		return opdInstance.currentSubGame == null ? opdInstance.currentSubGame = new SubGame() : opdInstance.currentSubGame;
	}
	
	private static void setCurrentSubGame( SubGame game ) {
		opdInstance.currentSubGame = game;
	}

	public static void switchGame(SubGame subGame) {
		setCurrentSubGame(subGame);
		switchScene(subGame.titleClass);
	}

	public static InputStream openDatInput(String fileDatName) throws FileNotFoundException {
		String opdFileName = opdInstance.currentSubGame.refName + "-" + fileDatName;
		Log("Input .dat: " + opdFileName);
		return instance.openFileInput( opdFileName );
	}

	public static OutputStream openDatOutput(String fileDatName, int mode) throws FileNotFoundException {
		String opdFileName = opdInstance.currentSubGame.refName + "-" + fileDatName;
		Log("Output .dat: " + opdFileName);
		return instance.openFileOutput( opdFileName, mode );
	}

	public static boolean deleteDatFile(String fileDatName) {
		String opdFileName = opdInstance.currentSubGame.refName + "-" + fileDatName;
		Log("Delete .dat: " + opdFileName);
		return instance.deleteFile( opdFileName );
	}
	
	public static void Log(String string) {
		Log.i("OPD", string);
	}

	public static void Log(String string, boolean b) {
		if (b) {
			Log.w("OPD", string);
		} else {
			Log(string);
		}
	}
	
	/* Common PD Stuffs */
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState); 
		
		opdInstance = this;
		
		updateImmersiveMode();
		
		DisplayMetrics metrics = new DisplayMetrics();
		instance.getWindowManager().getDefaultDisplay().getMetrics( metrics );
		boolean landscape = metrics.widthPixels > metrics.heightPixels;
		
		if (Preferences.INSTANCE.getBoolean( Preferences.KEY_LANDSCAPE, false ) != landscape) {
			landscape( !landscape );
		}

		Music.INSTANCE.enable( music() );
		Sample.INSTANCE.enable( soundFx() );
	}
	
	@Override
	public void onWindowFocusChanged( boolean hasFocus ) {
		super.onWindowFocusChanged( hasFocus );
		
		if (hasFocus) {
			updateImmersiveMode();
		}
	}
	
	public static void switchNoFade( Class<? extends OPDScene> c ) {
		OPDScene.noFade = true;
		switchScene( c );
	}
	
	public static void goBack() {
		if (scene().getClass() == opdInstance.origScene) {
			instance.finish();
		} else {
			switchScene( opdInstance.origScene );
			setCurrentSubGame( new SubGame() );
		}
	}
	
	/*
	 * ---> Prefernces
	 */
	
	public static void landscape( boolean value ) {
		OPDGame.instance.setRequestedOrientation( value ?
			ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
			ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
		Preferences.INSTANCE.put( Preferences.KEY_LANDSCAPE, value );
	}
	
	public static boolean landscape() {
		return width > height;
	}
	
	// *** IMMERSIVE MODE ****
	
	private static boolean immersiveModeChanged = false;
	
	@SuppressLint("NewApi")
	public static void immerse( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_IMMERSIVE, value );
		
		instance.runOnUiThread( new Runnable() {
			@Override
			public void run() {
				updateImmersiveMode();
				immersiveModeChanged = true;
			}
		} );
	}
	
	@Override
	public void onSurfaceChanged( GL10 gl, int width, int height ) {
		super.onSurfaceChanged( gl, width, height );
		
		if (immersiveModeChanged) {
			requestedReset = true;
			immersiveModeChanged = false;
		}
	}
	
	@SuppressLint("NewApi")
	public static void updateImmersiveMode() {
		if (android.os.Build.VERSION.SDK_INT >= 19) {
			instance.getWindow().getDecorView().setSystemUiVisibility( 
				immersed() ?
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE | 
				View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | 
				View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | 
				View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | 
				View.SYSTEM_UI_FLAG_FULLSCREEN | 
				View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY 
				:
				0 );
		}
	}
	
	public static boolean immersed() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_IMMERSIVE, false );
	}
	
	// *****************************
	
	public static void scaleUp( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_SCALE_UP, value );
		switchScene( opdInstance.origScene );
	}
	
	public static boolean scaleUp() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_SCALE_UP, true );
	}

	public static void zoom( int value ) {
		Preferences.INSTANCE.put( Preferences.KEY_ZOOM, value );
	}
	
	public static int zoom() {
		return Preferences.INSTANCE.getInt( Preferences.KEY_ZOOM, 0 );
	}
	
	public static void music( boolean value ) {
		Music.INSTANCE.enable( value );
		Preferences.INSTANCE.put( Preferences.KEY_MUSIC, value );
	}
	
	public static boolean music() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_MUSIC, true );
	}
	
	public static void soundFx( boolean value ) {
		Sample.INSTANCE.enable( value );
		Preferences.INSTANCE.put( Preferences.KEY_SOUND_FX, value );
	}
	
	public static boolean soundFx() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_SOUND_FX, true );
	}
	
	public static void brightness( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_BRIGHTNESS, value );
		// TODO need to figure out ingame brightness adjustments? I think?
	}
	
	public static boolean brightness() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_BRIGHTNESS, false );
	}
	
	public static void donated( String value ) {
		Preferences.INSTANCE.put( Preferences.KEY_DONATED, value );
	}
	
	public static String donated() {
		return Preferences.INSTANCE.getString( Preferences.KEY_DONATED, "" );
	}
	
	public static void lastClass( int value ) {
		Preferences.INSTANCE.put( Preferences.KEY_LAST_CLASS, value );
	}
	
	public static int lastClass() {
		return Preferences.INSTANCE.getInt( Preferences.KEY_LAST_CLASS, 0 );
	}
	
	public static void challenges( int value ) {
		Preferences.INSTANCE.put( Preferences.KEY_CHALLENGES, value );
	}
	
	public static int challenges() {
		return Preferences.INSTANCE.getInt( Preferences.KEY_CHALLENGES, 0 );
	}
	
	public static void intro( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_INTRO, value );
	}
	
	public static boolean intro() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_INTRO, true );
	}

    public static void version( int value)  {
        Preferences.INSTANCE.put( Preferences.KEY_VERSION, value );
    }

    public static int version() {
        return Preferences.INSTANCE.getInt( Preferences.KEY_VERSION, 0 );
    }
	
	/*
	 * <--- Preferences
	 */
	
	public static void reportException( Exception e ) {
		Log.e( "PD", Log.getStackTraceString( e ) ); 
	}
}
