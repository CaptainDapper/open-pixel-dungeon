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
package com.opd.lobby.scenes;

import com.watabou.noosa.audio.Sample;
import com.opd.lobby.Assets;
import com.opd.opdlib.OPDScene;

public class PixelScene extends OPDScene {
	
	@Override
	public void create() {
		super.create();
		Sample.INSTANCE.load( 
				Assets.SND_CLICK, 
				Assets.SND_DESCEND
			);
	}
}
