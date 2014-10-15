/*
 * Pixel Dungeon
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
package com.shatteredpixel.shatteredpixeldungeon.scenes;

import java.util.HashMap;

import com.shatteredpixel.shatteredpixeldungeon.*;
import com.watabou.input.Touchscreen;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.NextButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.PrevButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.SimpleButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndList;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;

public class StartScene extends PixelScene {

	private static final float BUTTON_HEIGHT	= 24;
	private static final float GAP				= 2;
	
	private static final String TXT_TITLE	= "Select Your Hero";
	
	private static final String TXT_LOAD	= "Load Game";
	private static final String TXT_NEW		= "New Game";
	
	private static final String TXT_ERASE		= "Erase current game";
	private static final String TXT_DPTH_LVL	= "Depth: %d, level: %d";
	
	private static final String TXT_REALLY	= "Do you really want to start new game?";
	private static final String TXT_WARNING	= "Your current game progress will be erased.";
	private static final String TXT_YES		= "Yes, start new game";
	private static final String TXT_NO		= "No, return to main menu";
	
	private static final String TXT_UNLOCK	= "To unlock this character class, slay the 3rd boss with any other class";
	
	private float width;
	private float height;
	private float top;
	private float left;

	private static HashMap<HeroClass, GemButton> gems = new HashMap<HeroClass, StartScene.GemButton>();;
	
	private int heroPage = 0;
	private GemButton[][] gemBtns;
	private NextHeroPage next;
	private PrevHeroPage prev;
	
	private BitmapText title;
	
	private Avatar avatar;
	private NinePatch frame;
	private BitmapText className;
	
	private SimpleButton btnMastery;
	
	private GameButton btnLoad;
	private GameButton btnNewGame;
	
	private boolean huntressUnlocked;
	private boolean hideSecretHeroPage = true;
	private Group unlock;
	
	public static HeroClass curClass;
	
	@Override
	public void create() {
		
		super.create();
		
		Badges.loadGlobal();
		
		uiCamera.visible = false;
		
		int w = Camera.main.width;
		int h = Camera.main.height;
		
		width = 128;
		height = 220;
		left = (w - width) / 2;
		top = (h - height) / 2; 
		
		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );
		
		title = PixelScene.createText( TXT_TITLE, 9 );
		title.hardlight( Window.TITLE_COLOR );
		title.measure();
		title.x = align( (w - title.width()) / 2 );
		title.y = align( top );
		add( title );
		
		updateHeroGems(heroPage);
		
		frame = Chrome.get( Chrome.Type.TOAST_TR );
		add( frame );
		
		btnNewGame = new GameButton( TXT_NEW ) {
			@Override
			protected void onClick() {
				if (GamesInProgress.check( curClass ) != null) {
					StartScene.this.add( new WndOptions( TXT_REALLY, TXT_WARNING, TXT_YES, TXT_NO ) {
						@Override
						protected void onSelect( int index ) {
							if (index == 0) {
								startNewGame();
							}
						}
					} );
					
				} else {
					startNewGame();
				}
			}
		};
		add( btnNewGame );

		btnLoad = new GameButton( TXT_LOAD ) {	
			@Override
			protected void onClick() {
				InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
				Game.switchScene( InterlevelScene.class );
			}
		};
		add( btnLoad );
		
		frame.size( width, BUTTON_HEIGHT + frame.marginVer() );
		frame.x = align( left );
		frame.y = align( h - top - frame.height() );
		
		avatar = new Avatar();
		
		float q = (hideSecretHeroPage ? gemBtns[0][0].bottom() : next.bottom());
		
		NinePatch avFrame = Chrome.get( Chrome.Type.TOAST_TR );
		avFrame.size( avatar.width() * 1.6f, avatar.height() * 1.6f );
		avFrame.x = align( (w - avFrame.width()) / 2 );
		avFrame.y = align( (frame.y + q - avFrame.height()) / 2 );
		add( avFrame );
		
		className = PixelScene.createText( "Placeholder", 9 );
		className.measure();
		className.y = align( avFrame.y + avFrame.innerBottom() - className.height() );
		add( className );
		
		avatar.point( avFrame.center() );
		avatar.camera = Camera.main;
		align( avatar );
		add( avatar );
		
		Image iconInfo = Icons.INFO.get();
		iconInfo.x = avFrame.x + avFrame.innerRight() - iconInfo.width();
		iconInfo.y = avFrame.y + avFrame.marginTop();
		add( iconInfo );
		
		add( new TouchArea( avFrame ) {
			@Override
			protected void onClick( Touchscreen.Touch touch ) {
				add( new WndList( curClass.perks() ) );
			}
		} );
		
		btnMastery = new SimpleButton( Icons.MASTERY.get() ) {
			@Override
			protected void onClick() {
				String text = null;
				switch (curClass) {
				case WARRIOR:
					text = HeroSubClass.GLADIATOR.desc() + "\n\n" + HeroSubClass.BERSERKER.desc();
					break;
				case MAGE:
					text = HeroSubClass.BATTLEMAGE.desc() + "\n\n" + HeroSubClass.WARLOCK.desc();
					break;
				case ROGUE:
					text = HeroSubClass.FREERUNNER.desc() + "\n\n" + HeroSubClass.ASSASSIN.desc();
					break;
				case HUNTRESS:
					text = HeroSubClass.SNIPER.desc() + "\n\n" + HeroSubClass.WARDEN.desc();
					break;
				case RAIDER:
					//TODO!
					//text = HeroSubClass.SNIPER.desc() + "\n\n" + HeroSubClass.WARDEN.desc();
					break;
				}
				StartScene.this.add( new WndTitledMessage( Icons.MASTERY.get(), "Mastery", text ) );
			}
		};
		btnMastery.setPos( 
			avFrame.x + avFrame.innerRight() - btnMastery.width(),
			avFrame.y + avFrame.innerBottom() - btnMastery.height() );
		add( btnMastery );
		
		unlock = new Group();
		add( unlock );
		
		if (!(huntressUnlocked = Badges.isUnlocked( Badges.Badge.BOSS_SLAIN_3 ))) {
		
			BitmapTextMultiline text = PixelScene.createMultiline( TXT_UNLOCK, 5 );
			text.maxWidth = (int)frame.innerWidth();
			text.measure();
			
			float pos = frame.center().y - text.height() / 2;
			for (BitmapText line : text.new LineSplitter().split()) {
				line.measure();
				line.hardlight( 0xFFFF00 );
				line.x = PixelScene.align( frame.center().x - line.width() / 2 );
				line.y = PixelScene.align( pos );
				unlock.add( line );
				
				pos += line.height(); 
			}
		}
		hideSecretHeroPage = Badges.isUnlocked( Badges.Badge.VICTORY );
		
		curClass = null;
		updateClass( HeroClass.values()[ShatteredPixelDungeon.lastClass()] );
		
		fadeIn();
	}
	
	private void updateHeroGems(int oldHeroPage) {
		_updateHeroGems(oldHeroPage, oldHeroPage, true);
	}
	
	private void updateHeroGems(int oldHeroPage, int newHeroPage) {
		_updateHeroGems(oldHeroPage, newHeroPage, false);
	}
	
	private void _updateHeroGems(int oldHeroPage, int newHeroPage, boolean doFirstRun) {
		//Clear old gems
		if (!doFirstRun) {
			for (GemButton btn : gemBtns[oldHeroPage]) {
				remove( btn );
			}
		} else {
			gemBtns = new GemButton[2][4];
			gemBtns[0][0] = new GemButton( HeroClass.WARRIOR );
			gemBtns[0][1] = new GemButton( HeroClass.MAGE );
			gemBtns[0][2] = new GemButton( HeroClass.ROGUE );
			gemBtns[0][3] = new GemButton( HeroClass.HUNTRESS );
			gemBtns[1][0] = new GemButton( HeroClass.RAIDER );
			
			if (!hideSecretHeroPage) {
				next = new NextHeroPage();
				prev = new PrevHeroPage();
			}
		}
		
		//Make new gems
		float pos = title.y + title.height() + GAP;
				
		float space = width;
		for (GemButton btn : gemBtns[heroPage]) {
			if (btn != null) {
				space -= btn.width();
			}
		}

		float p = 0;
		for (GemButton btn : gemBtns[heroPage]) {
			if (btn != null) {
				add( btn );
				btn.setPos( align( left + p ), align( pos ) );
				p += btn.width() + space / 3;
			}
		}
		
		//Clear and add new page buttons
		if (!hideSecretHeroPage) {
			if (newHeroPage == 0) {
				remove( prev );
				add( next );
				next.setPos( align( left + width - next.width() ), align( gemBtns[heroPage][0].bottom() ) );
			} else {
				remove( next );
				add( prev );
				prev.setPos( align( left ), align( gemBtns[heroPage][0].bottom() ) );
			}
		}
	}
	
	private void updateClass( HeroClass cl ) {
		
		if (curClass == cl) {
			return;
		}
		
		if (curClass != null) {
			gems.get( curClass ).highlight( false );
		}
		
		gems.get( curClass = cl ).highlight( true );
		
		className.text( Utils.capitalize( cl.title() ) );
		className.measure();
		className.x = align( frame.center().x - className.width() / 2 );
		
		if (cl != HeroClass.HUNTRESS || huntressUnlocked) {
		
			unlock.visible = false;
			
			float buttonPos = frame.y + frame.innerBottom() - BUTTON_HEIGHT;
			
			GamesInProgress.Info info = GamesInProgress.check( curClass );
			if (info != null) {
				
				btnLoad.visible = true;
				btnLoad.secondary( Utils.format( TXT_DPTH_LVL, info.depth, info.level ) );
				btnNewGame.visible = true;
				btnNewGame.secondary( TXT_ERASE );
				
				float w = (frame.innerWidth() - GAP) / 2;
				
				btnLoad.setRect(
					frame.x + frame.marginLeft(), buttonPos, w, BUTTON_HEIGHT );
				btnNewGame.setRect(
					btnLoad.right() + GAP, buttonPos, w, BUTTON_HEIGHT );
				
			} else {
				btnLoad.visible = false;
				
				btnNewGame.visible = true;
				btnNewGame.secondary( null );
				btnNewGame.setRect(
					frame.x + frame.marginLeft(), buttonPos, frame.innerWidth(), BUTTON_HEIGHT );
			}
			
			Badges.Badge badgeToCheck = null;
			switch (curClass) {
			case WARRIOR:
				badgeToCheck = Badges.Badge.MASTERY_WARRIOR;
				break;
			case MAGE:
				badgeToCheck = Badges.Badge.MASTERY_MAGE;
				break;
			case ROGUE:
				badgeToCheck = Badges.Badge.MASTERY_ROGUE;
				break;
			case HUNTRESS:
				badgeToCheck = Badges.Badge.MASTERY_HUNTRESS;
				break;
			case RAIDER:
				badgeToCheck = Badges.Badge.MASTERY_RAIDER;
				break;
			}
			btnMastery.active = 
			btnMastery.visible = 
				Badges.isUnlocked( badgeToCheck );
			
		} else {
			
			unlock.visible = true;
			btnLoad.visible = false;
			btnNewGame.visible = false;
			btnMastery.active = btnMastery.visible = false;
			
		}
		
		avatar.selectClass( curClass );
	}
	
	private void startNewGame() {

		Dungeon.hero = null;
		InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
		
		if (ShatteredPixelDungeon.intro()) {
			ShatteredPixelDungeon.intro(false);
			Game.switchScene( IntroScene.class );
		} else {
			Game.switchScene( InterlevelScene.class );
		}	
	}
	
	@Override
	protected void onBackPressed() {
		Game.switchScene( TitleScene.class );
	}
	
	private static class Avatar extends Image {
		
		private static final int WIDTH	= 24;
		private static final int HEIGHT	= 32;
		private static final int SCALE	= 2;
		
		private TextureFilm frames;
		
		private float brightness = 0;
		
		public Avatar() {
			super( Assets.AVATARS );
			
			frames = new TextureFilm( texture, WIDTH, HEIGHT );
			selectClass( HeroClass.WARRIOR );
			scale.set( SCALE );
			
			origin.set( width() / 2, height() / 2 );
		}
		
		public void selectClass( HeroClass cl ) {
			frame( frames.get( cl.ordinal() ) );
		}
		
		public void flash() {
			brightness = 1f;
		}
		
		@Override
		public void update() {
			super.update();
		
			if (brightness > 0) {
				ra = ga = ba = brightness;
				brightness -= Game.elapsed * 4;
				if (brightness < 0) {
					resetColor();
				}
			}
		}
	}
	
	private class GemButton extends Button {
		
		private NinePatch bg;
		private Image icon;
		
		private HeroClass cl;
		
		public GemButton( HeroClass cl ) {
			super();
			
			this.cl = cl;
			gems.put( cl, this );
			
			icon.copy( Icons.get( cl ) );
			setSize( 32, 32 );
			
			highlight( false );
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			bg = Chrome.get( Chrome.Type.GEM );
			add( bg );
			
			icon = new Image();
			add( icon );
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			bg.x = x;
			bg.y = y;
			bg.size( width, height );
			
			icon.x = x + (width - icon.width) / 2;
			icon.y = y + (height - icon.height) / 2;
		}
		
		@Override
		protected void onTouchDown() {
			Emitter emitter = (Emitter)recycle( Emitter.class );
			emitter.revive();
			emitter.pos( bg );
			emitter.burst( Speck.factory( Speck.LIGHT ), 3 );
			
			updateClass( cl );
			avatar.flash();
			
			Sample.INSTANCE.play( Assets.SND_CLICK, 1, 1, 1.2f );
		}
		
		public void highlight( boolean value ) {
			if (value) {
				bg.rm = 1.2f;
				bg.gm = 1.2f;
				bg.bm = 1.1f;
				bg.am = 0.8f;
			} else {
				bg.rm = 1.0f;
				bg.gm = 1.0f;
				bg.bm = 1.0f;
				bg.am = 0.6f;
			}
		}
	}
	
	private static class GameButton extends RedButton {
		
		private static final int SECONDARY_COLOR	= 0xCACFC2;
		
		private BitmapText secondary;
		
		public GameButton( String primary ) {
			super( primary );
			
			this.secondary.text( null );
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			secondary = createText( 6 );
			secondary.hardlight( SECONDARY_COLOR );
			add( secondary );
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			if (secondary.text().length() > 0) {
				text.y = y + (height - text.height() - secondary.baseLine()) / 2;
				
				secondary.x = align( x + (width - secondary.width()) / 2 );
				secondary.y = align( text.y + text.height() ); 
			} else {
				text.y = y + (height - text.baseLine()) / 2;
			}
		}
		
		public void secondary( String text ) {
			secondary.text( text );
			secondary.measure();
		}
	}

	private class NextHeroPage extends NextButton {
		@Override
		protected void onClick() {
			int oldHeroPage = heroPage;
			heroPage += 1;
			updateHeroGems(oldHeroPage, heroPage);
		}
	}

	private class PrevHeroPage extends PrevButton {
		@Override
		protected void onClick() {
			int oldHeroPage = heroPage;
			heroPage -= 1;
			updateHeroGems(oldHeroPage, heroPage);
		}
	}
}
