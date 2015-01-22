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
package com.opd.lobby.windows;

import android.content.Intent;
import android.net.Uri;

import java.util.Enumeration;
import java.util.Hashtable;

import com.opd.lobby.ui.Icons;
import com.opd.lobby.ui.SimpleButton;
import com.opd.lobby.scenes.PixelScene;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Image;
import com.watabou.pixeldungeon.ui.RedButton;
import com.opd.lobby.ui.Window;
import com.opd.opdlib.OPDGame;
import com.opd.opdlib.OPDScene;
import com.opd.opdlib.SubGame;

public class WndGameInfo extends Window {

	private static final float GAP = 2;

	private static final int WIDTH = 160;

	private static final String TXT_NOTHING = "This isn't really something you should be happy about reading. It's kind of an error... :)";
	private static final String TXT_URL = "Website";
	private static final String TXT_PLAY_STORE_LINK = "Play Store";
	private static final String TXT_GITHUB_LINK = "GitHub";
	
	private SubGame subGame;

	private float yTrack;

	public WndGameInfo(final SubGame subGame) {
		super();
		this.subGame = subGame;
		
		closeButton();
		
		titleBar();
		
		desc();
		
		webLinks();
		
		resize(WIDTH, (int) (yTrack));
	}

	private void titleBar() {
		IconTitle titlebar = new IconTitle();
		Image image = new Image();
		image.texture(subGame.asset);
		titlebar.icon(image);
		titlebar.label(subGame.title);
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		yTrack = titlebar.bottom() + GAP;
	}

	private void desc() {
		BitmapText gameAuthor = OPDScene.createText( 6 );
		add( gameAuthor );
		
		gameAuthor.text( "Author: " + subGame.author );
		gameAuthor.measure();
		gameAuthor.hardlight(0xaaaaaa);

		gameAuthor.x = 0;
		gameAuthor.y = yTrack;
		
		yTrack = gameAuthor.y + gameAuthor.height() + GAP;
		
		BitmapText gameVersion = OPDScene.createText( 6 );
		add( gameVersion );
		
		gameVersion.text( "v" + subGame.version);
		gameVersion.measure();
		gameVersion.hardlight(0x888888);

		gameVersion.x = 0;
		gameVersion.y = yTrack;
		
		yTrack = gameVersion.y + gameVersion.height() + GAP;
		
		BitmapTextMultiline info = PixelScene.createMultiline(6);
		add(info);

		StringBuilder desc = new StringBuilder();

		final char newLine = '\n';
		desc.append(subGame.desc + newLine);

		desc.append(newLine + "Web Links:" + newLine);
		info.text(desc.length() > 0 ? desc.toString() : TXT_NOTHING);
		info.maxWidth = WIDTH;
		info.measure();
		info.x = 0;
		info.y = yTrack;

		yTrack = info.y + info.height() + GAP;
	}

	private void webLinks() {
		Hashtable<String, String> ht= new Hashtable<String, String>();
		ht.put(TXT_URL, subGame.url);
		ht.put(TXT_PLAY_STORE_LINK, subGame.playStoreLink);
		ht.put(TXT_GITHUB_LINK, subGame.githubLink);
		
		Enumeration<String> e = ht.keys();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			
			if (ht.get(key).length() > 0) {
				WebLinkButton urlBtn = new WebLinkButton(key, ht.get(key));
				urlBtn.setRect(0, yTrack, urlBtn.reqWidth() + 2,urlBtn.reqHeight() + 2);
				add(urlBtn);
				
				yTrack = urlBtn.bottom() + GAP;
			}
		}
	}

	private void closeButton() {
		SimpleButton close = new SimpleButton( Icons.get( Icons.CLOSE ) ) {
			protected void onClick() {
				hide();
			};
		};
		close.setPos(WIDTH - close.right() - GAP, GAP);
		add( close );
	}

	private static class WebLinkButton extends RedButton {
		private String url;

		public WebLinkButton(String text, String url) {
			super(text);
			this.url = url;
		}

		@Override
		protected void onClick() {
			OPDGame.instance.startActivity(new Intent(Intent.ACTION_VIEW)
					.setData(Uri.parse(url)));
		}
	}
}
