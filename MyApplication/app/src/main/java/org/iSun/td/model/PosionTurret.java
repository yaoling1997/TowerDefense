package org.iSun.td.model;

import java.util.List;

import org.iSun.td.TurretDefense;
import org.iSun.td.constant.SystemConstant;
import org.iSun.td.constant.TurrentType;
import org.loon.framework.android.game.action.map.Field2D;

/**
 * ¶¾Ò©ÅÚÌ¨£¬¼õËÙ¹¦ÄÜ
 * 
 * @author iSun
 * 
 */
public class PosionTurret extends Turret {
	// ÅÚ»÷¼ä¸ô
	public int delay = 5;

	public PosionTurret(String fileName, TurretDefense td) {
		super(fileName, td);
		this.typeID = TurrentType.SOUNDWAVE_TURRET;
		this.setInterval(70);
		this.ranges = new int[] { 60, 80, 120, 150 };
		this.values = new int[] { 25, 50, 100, 150 };
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

		// ÉèÖÃÅÚ»÷µÄÊ±¼ä¼ä¸ô
		if (this.delay > 0) {
			--this.delay;
		} else if (!es.isEmpty()) {
			fire();
		}
	}

	@Override
	public void fire() {
		// ¹¹ÔìÅÚµ¯
		PosionBullet bullet = new PosionBullet("assets/poison.png",
				this.getRotation(), field);
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

	class PosionBullet extends BaseMissle {
		private int[] damages;
		private float[] dragEffects;

		public PosionBullet(String fileName, int dir, Field2D field) {
			super(fileName, dir, field);
			this.damages = new int[] { 2, 4, 6, 8 };
			this.dragEffects = new float[] { 0.5f, 0.5f, 0.5f, 0.5f };
			speed = SystemConstant.BulletSpeed.POSION_BULLET_SPEED;
			// ÉèÖÃ¶¯×÷³ö·¢ÑÓÊ±Ê±¼ä
			this.setDelay(50);
		}

		/**
		 * ¶¯×÷´¦Àí
		 */
		@Override
		public void action(long elapsedTime) {
			// TODO Auto-generated method stub
			if (removeFlag) {
				return;
			}

			move();
			Enemy enemy = this.getCollisionEnemy();
			// µ±ÓëµÐÈËÅö×²Ê±
			if (enemy != null) {
				this.removeFromLayer();
				enemy.beAttacted(this.damages[PosionTurret.this.currentLevel]);
				// ¼õËÙ
				enemy.bePosion = true;
				enemy.posionTime = 10;
				enemy.move
						.setSpeed((int) (enemy.speed * this.dragEffects[PosionTurret.this.currentLevel]));
				return;
			} else if (isOutOfScreen()) {
				this.removeFromLayer();
			}
		}

	}
}
