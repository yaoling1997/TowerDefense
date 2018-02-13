package org.iSun.td.model;

import java.util.List;

import org.iSun.td.TurretDefense;
import org.iSun.td.constant.SystemConstant;
import org.iSun.td.constant.TurrentType;
import org.loon.framework.android.game.action.map.Field2D;

public class BombTurret extends Turret {

	public BombTurret(String fileName, TurretDefense td) {
		super(fileName, td);
		this.typeID = TurrentType.BOMB_TURRET;
		this.setInterval(150);
		this.ranges = new int[] { 60, 90, 120, 180 };
		this.values = new int[] { 30, 60, 100, 180 };
	}

	@Override
	public void action(long elapsedTime) {
		// TODO Auto-generated method stub
		// ±éÀúÖ¸¶¨°ë¾¶ÀàËùÓÐµÄEnemy
		List<?> es = this.getAttactAbleEnemys();
		Enemy target = null;
		// µ±´æÔÚµÐÈËÊ±
		if (!es.isEmpty()) {
			target = (Enemy) es.get(0);
			this.rotateToTarget(target);
		}

		// ÉèÖÃÅÚ»÷µÄÊ±¼ä¼ä¸ô
		if (this.delay > 0) {
			--this.delay;
		} else if (target != null) {
			fireTo(target);
		}
	}

	/**
	 * ÏòÄ¿±ê¿ª»ð
	 * 
	 * @param target
	 */
	public void fireTo(Enemy target) {
		// ¹¹ÔìÅÚµ¯
		Bomb bullet = new Bomb("assets/rocket.png", this.getRotation(), 4,
				field);
		bullet.target = target;
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
	 * µ¼µ¯
	 * 
	 * @author iSun
	 * 
	 */
	class Bomb extends BaseMissle {
		// ±»Ëø¶¨µÄÄ¿±ê
		private Enemy target;

		public Bomb(String fileName, int dir, int damage, Field2D field) {
			super(fileName, dir, damage, field);
			this.speed = SystemConstant.BulletSpeed.BOMB_BULLET_SPEED;
			this.setRotation(dir);
		}

		/**
		 * µ¼µ¯ÔË¶¯ÔË¶¯
		 */
		public void move() {
			// ¶à´Î¸Ä±ä×Óµ¯µÄ×ø±ê
			for (int i = 0; i < speed; i++) {
				// ¾ÀÕýµ¯µÀµÄÎ»ÖÃ
				double angle = Math.toRadians((double) this.dir);
				this.x += 1.5 * Math.cos(angle);
				this.y += Math.sin(angle);
			}
			if (!this.target.removeFlag) {
				traceTarget();
			}

			this.setLocation(
					(int) this.x + (field.getTileWidth() - this.getWidth()) / 2,
					(int) this.y + (field.getTileHeight() - this.getHeight())
							/ 2);

		}

		/**
		 * ×·×ÙÄ¿±ê
		 */
		public void traceTarget() {
			this.dir = (int) Math.toDegrees(Math.atan2(
					(target.getY() - this.getY()),
					(target.getX()) - this.getX()));
			if (!target.removeFlag) {
				setRotation(this.dir);
			}
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
			if (enemy != null && enemy.equals(target)) {
				this.removeFromLayer();
				enemy.beAttacted(this.damage);
				return;
			} else if (isOutOfScreen()) {
				this.removeFromLayer();
			}
		}
	}
}
