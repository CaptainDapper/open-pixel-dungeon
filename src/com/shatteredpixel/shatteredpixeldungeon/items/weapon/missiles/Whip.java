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
package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Piercing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Swing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;

//TODO!
public class Whip extends MissileWeapon {

	{
		name = "whip";
		image = ItemSpriteSheet.WHIP;
		
		STR = 10;
		
		MIN = 1;
		MAX = 4;
		
		stackable = false;
	}
	
	@Override
	public boolean isUpgradable() {
		return true;
	}
	
	@Override
	public Item upgrade() {
		return upgrade( false );
	}
	
	@Override
	public Item upgrade( boolean enchant ) {
		MIN += 1;
		MAX += 2;
		super.upgrade( enchant );
		
		updateQuickslot();
		
		return this;
	}
	
	@Override
	public Item degrade() {
		MIN -= 1;
		MAX -= 2;
		return super.degrade();
	}
	
	@Override
	public Weapon enchant( Enchantment ench ) {
		while (ench instanceof Piercing || ench instanceof Swing) {
			ench = Enchantment.random();
		}
		
		return super.enchant( ench );
	}
	
	/*@Override
	public void proc( Char attacker, Char defender, int damage ) {
		super.proc( attacker, defender, damage );
		if (attacker instanceof Hero && ((Hero)attacker).usingRanged) {
			//circleBack( defender.pos, (Hero)attacker );
		}
	}*/
	
	/*@Override
	protected void miss( int cell ) {
		//circleBack( cell, curUser );
	}*/
	
	/*private void circleBack( int from, Hero owner ) {
		if (!collect( curUser.belongings.backpack )) {
			Dungeon.level.drop( this, owner.pos ).sprite.drop();
		}
		((MissileSprite)curUser.sprite.parent.recycle( MissileSprite.class )).
			reset( from, curUser.pos, curItem, null );
	}*/
	
	@Override
	public String desc() {
		return 
			"A length of woven leather capable of medium-ranged attacks.";
	}
}
