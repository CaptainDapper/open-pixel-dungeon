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
package com.opd.openpixeldungeon.scenes;

import java.util.ArrayList;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;
import com.opd.noosa.OPDGame;
import com.opd.openpixeldungeon.Assets;
import com.opd.openpixeldungeon.SubGames;
import com.opd.openpixeldungeon.SubGames.SubGame;
import com.opd.openpixeldungeon.effects.BannerSprites;
import com.opd.openpixeldungeon.effects.Fireball;
import com.opd.openpixeldungeon.scenes.PixelScene;
import com.opd.openpixeldungeon.ui.Archs;
import com.opd.openpixeldungeon.ui.ExitButton;
import com.opd.openpixeldungeon.ui.ScrollPane;

public class TitleScene extends PixelScene {
	
	private static final int WIDTH = 120;

	private ScrollPane list;
	private ArrayList<ListItem> subGamesList = new ArrayList<TitleScene.ListItem>();
	
	@Override
	public void create() {
		super.create();
		
		uiCamera.visible = false;
		
		int w = Camera.main.width;
		int h = Camera.main.height;
		
		float height = 180;
		
		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs ); 
		
		Image title = BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON );
		add( title );
		
		title.x = (w - title.width()) / 2;
		title.y = (h - height) / 2;
		
		placeTorch( title.x + 18, title.y + 20 );
		placeTorch( title.x + title.width - 18, title.y + 20 );
		
		int yPos = (int) (title.y + title.height());
		
		list = new ScrollPane( new Component() ) {
			@Override
			public void onClick( float x, float y ) {
				int size = subGamesList.size();
				for (int i=0; i < size; i++) {
					if (subGamesList.get( i ).onClick( x, y )) {
						break;
					}
				}
			}
		};
		add( list );
		list.setRect( (w - WIDTH)/2, yPos, WIDTH, h - yPos );
		
		updateList();
		
		ExitButton btnExit = new ExitButton();
		btnExit.setPos( w - btnExit.width(), 0 );
		add( btnExit );
		
		displayVersion(w, h);
		
		fadeIn();
	}
	
	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.setPos( x, y );
		add( fb );
	}

	private void updateList() {
		/*
		txtTitle.text( Utils.format( TXT_TITLE, showPotions ? TXT_POTIONS : TXT_SCROLLS ) );
		txtTitle.measure();
		txtTitle.x = PixelScene.align( PixelScene.uiCamera, (WIDTH - txtTitle.width()) / 2 );
		*/
		
		subGamesList.clear();
		
		Component content = list.content();
		content.clear();
		list.scrollTo( 0, 0 );
		
		float pos = 0;
		for (SubGame theSubGame : SubGames.all) {
			ListItem gameBtn = new ListItem( theSubGame );
			gameBtn.setRect( 0, pos, WIDTH, gameBtn.height() );
			content.add( gameBtn );
			subGamesList.add( gameBtn );
			
			pos += gameBtn.height();
		}
		
		content.setSize( WIDTH, pos );
	}

	private static class ListItem extends Button {
		private static final int GAP = 2;
		private BitmapText gameName;
		private BitmapText gameAuthor;
		private BitmapText gameVersion;
		private Image image;
		private SubGame subGame;
		
		public ListItem(SubGame subGame) {
			super();
			
			image.texture(subGame.asset);
			
			gameName.text( subGame.title );
			gameName.measure();
			
			gameAuthor.text( "Author: " + subGame.author );
			gameAuthor.measure();
			gameAuthor.hardlight(0xaaaaaa);
			
			gameVersion.text( "v" + subGame.version );
			gameVersion.measure();
			gameVersion.hardlight(0x888888);
			
			setSize( WIDTH, 36 );
			
			this.subGame = subGame;
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();

			image = new Image();
			add( image );

			gameName = createText( 9 );
			add( gameName );
			
			gameAuthor = createText( 6 );
			add( gameAuthor );
			
			gameVersion = createText( 6 );
			add( gameVersion );
		}
		
		@Override
		protected void layout() {
			image.x = align(x + GAP);
			image.y = align(y + GAP);
			
			float labelX = image.x + GAP + image.width;
			
			gameName.x = align( labelX );
			gameName.y = align( image.y + GAP );
			
			gameAuthor.x = align( labelX );
			gameAuthor.y = align( gameName.y + gameName.baseLine() + GAP );
			
			gameVersion.x = align( labelX );
			gameVersion.y = align( gameAuthor.y + gameAuthor.baseLine() + GAP );
		}
		
		public boolean onClick( float x, float y ) {
			if (inside( x, y )) {
				Sample.INSTANCE.play( Assets.SND_DESCEND, 1, 1, 1.2f );
				OPDGame.switchScene(subGame.titleSceneClass);
				return true;
			} else {
				return false;
			}
		}
		
		@Override
		protected void onTouchDown() {
			image.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.SND_CLICK, 1, 1, 0.8f );
		}
		
		@Override
		protected void onTouchUp() {
			image.resetColor();
		}
	}
}
