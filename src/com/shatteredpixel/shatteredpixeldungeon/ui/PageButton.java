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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;

public class PageButton extends Button {
	protected String TXT;
	protected NinePatch bg;
	protected int xOffset;
	protected BitmapText text;
	
	public PageButton() {
		super();
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		bg.x = x;
		bg.y = y;
		bg.size( width, height );
		
		text.x = (x + (width - text.width()) / 2) + xOffset;
		text.y = (y + (height - text.baseLine()) / 2);
	}
	
	@Override
	protected void onTouchDown() {
		Sample.INSTANCE.play( Assets.SND_CLICK, 1, 1, 1.2f );
	}
}