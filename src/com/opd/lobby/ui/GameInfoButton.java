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
package com.opd.lobby.ui;

import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;
import com.opd.lobby.Assets;
import com.opd.lobby.windows.WndGameInfo;
import com.opd.opdlib.OPDGame;
import com.opd.opdlib.SubGame;

public class GameInfoButton extends Button {
	
	private Image image;
	private SubGame subGame;
	
	@Override
	protected void createChildren() {
		super.createChildren();
		
		image = Icons.PREFS.get();
		add( image );
	}
	
	public GameInfoButton() {
		super();
		
		width = image.width;
		height = image.height;
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		image.x = x;
		image.y = y;
	}
	
	@Override
	protected void onTouchDown() {
		image.brightness( 1.5f );
		Sample.INSTANCE.play( Assets.SND_CLICK );
	}
	
	@Override
	protected void onTouchUp() {
		image.resetColor();
	}
	
	@Override
	protected void onClick() {
		OPDGame.scene().add( new WndGameInfo( subGame ) );
	}

	public void setSubGame(SubGame subGame2) {
		subGame = subGame2;
	}
}
