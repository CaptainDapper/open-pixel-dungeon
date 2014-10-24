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

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;
import com.opd.noosa.OPDGame;
import com.opd.openpixeldungeon.Assets;
import com.opd.openpixeldungeon.SubGames;
import com.opd.openpixeldungeon.effects.BannerSprites;
import com.opd.openpixeldungeon.effects.Fireball;
import com.opd.openpixeldungeon.scenes.PixelScene;
import com.opd.openpixeldungeon.ui.Archs;
import com.opd.openpixeldungeon.ui.ExitButton;

public class TitleScene extends PixelScene {
	
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
		
		for (int i=0; i<SubGames.all.length; i++) {
			final SubGames.SubGame game = SubGames.all[i];
			
			DashboardItem btnGame = new DashboardItem( game.title, i ) {
				@Override
				protected void onClick() {
					OPDGame.switchScene( game.titleSceneClass );
				}
			};
			btnGame.setPos( (w - btnGame.width())/ 2 , yPos );
			add( btnGame );
			
			yPos += btnGame.height();
		}
		
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
	
	private static class DashboardItem extends Button {
		
		public static final float SIZE	= 48;
		
		//private static final int IMAGE_SIZE	= 32;
		
		private Image image;
		private BitmapText label;
		
		public DashboardItem( String text, int index ) {
			super();
			
			this.image.texture( SubGames.all[index].asset );
			
			//image.frame( image.texture.uvRect( index * IMAGE_SIZE, 0, (index + 1) * IMAGE_SIZE, IMAGE_SIZE ) );
			this.label.text( text );
			this.label.measure();
			
			setSize( SIZE, SIZE );
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();

			image = new Image();
			add( image );
			
			label = createText( 9 );
			add( label );
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			image.x = align( x + (width - image.width()) / 2 );
			image.y = align( y );
			
			label.x = align( x + (width - label.width()) / 2 );
			label.y = align( image.y + image.height() +2 );
		}
		
		@Override
		protected void onTouchDown() {
			image.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.SND_DESCEND, 1, 1, 1.2f );
		}
		
		@Override
		protected void onTouchUp() {
			image.resetColor();
		}
	}
}
