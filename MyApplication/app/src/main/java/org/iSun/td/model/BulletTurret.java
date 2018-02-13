package org.iSun.td.model;

import java.util.List;

import org.iSun.td.TurretDefense;
import org.iSun.td.constant.TurrentType;
import org.loon.framework.android.game.action.map.Field2D;

/**
 * »úÇ¹ÅÚÌ¨
 * 
 * @author iSun
 * 
 */
public class BulletTurret extends Turret {
	// ÅÚ»÷¼ä¸ô
	public int delay = 5;

	public BulletTurret(String fileName, TurretDefense td) {
		super(fileName, td);
		this.typeID = TurrentType.BULLET_TURRET;
		this.setInterval(50);
		this.ranges = new int[] { 60, 90, 100, 160 };
		this.values = new int[] { 15, 30, 75, 100 };
	}

	@Override
	public void action(long elapsedTime) {
		// TODO Auto-generated method stub
		// ±éÀúÖ¸¶¨°ë¾¶ÀàËùÓÐµÄEnemy
		List<?> es = this.getAttactAbleEnemys();

		// µ±´æÔÚµÐÈËÊ±
		if (!es.isEmpty()) {
			Enemy target = (Enemy) es.get(0);
			this.rotateToTarget(target);
		}

		// ÅÚ»÷ÑÓÊ±
		if (this.delay > 0) {
			--this.delay;
		} else if (!es.isEmpty()) {
			fire();
		}
	}

	@Override
	public void fire() {
		// ¹¹ÔìÅÚµ¯
		JetBullet bullet = new JetBullet(turrets[4], this.getRotation(), 4,
				field);
		// ¼ÆËãÅÚ»÷µã
		int x = (int) Math.round(Math.cos(Math.toRadians(this.getRotation()))
				* (double) bullet.getWidth() * 2)
				+ this.getX();

		int y = (int) Math.round(Math.sin(Math.toRadians(this.getRotation()))
				* (double) bullet.getHeight() * 2)
				+ this.getY();
		// ½«ÅÚµ¯Ìí¼Óµ½Layer
		this.getLayer().addObject(bullet, x, y);
		// td.playtAssetsMusic("audio/minigun_shot.mp3", false);
		// ¸´Î»ÑÓÊ±¼ÆÊý±äÁ¿
		this.delay = 5;

	}

	/**
	 * »úÇ¹×Óµ¯
	 * 
	 * @author iSun
	 * 
	 */
	class JetBullet extends BaseMissle {
		public JetBullet(String fileName, int dir, int damage, Field2D field) {
			super(fileName, dir, damage, field);
		}
	}
}
