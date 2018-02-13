package org.iSun.td.model;

import org.iSun.td.constant.SystemConstant;
import org.loon.framework.android.game.action.map.Field2D;
import org.loon.framework.android.game.core.graphics.window.actor.Actor;
import org.loon.framework.android.game.core.graphics.window.actor.ActorLayer;
public class BaseMissle extends Actor {
	protected int dir;
	protected int damage;
	protected double x, y;
	protected boolean removeFlag;
	protected Field2D field;
	protected int speed = SystemConstant.BulletSpeed.BASE_BULLET_SPEED;

	public BaseMissle(String fileName, int dir, int damage, Field2D field) {
		this.dir = dir;
		this.damage = damage;
		this.setImage(fileName);
		this.field = field;
		this.setDelay(50);
	}

	public BaseMissle(String fileName, int dir, Field2D field) {
		this.dir = dir;
		this.setImage(fileName);
		this.field = field;
	}
	@Override
	protected void addLayer(ActorLayer gameLayer) {
		// TODO Auto-generated method stub
		this.x = this.getX();
		this.y = this.getY();
	}

	public void move() {
		for (int i = 0; i < speed; i++) {
			double angle = Math.toRadians((double) this.dir);
			this.x += Math.cos(angle);
			this.y += Math.sin(angle);
		}

		this.setLocation((int) this.x
				+ (field.getTileWidth() - this.getWidth()) / 2, (int) this.y
				+ (field.getTileHeight() - this.getHeight()) / 2);

	}
	@Override
	public void action(long elapsedTime) {
		if (removeFlag) {
			return;
		}
		move();
		Enemy enemy = this.getCollisionEnemy();
		if (enemy != null) {
			this.removeFromLayer();
			enemy.beAttacted(this.damage);
			return;
		} else if (isOutOfScreen()) {
			this.removeFromLayer();
		}
	}

	public boolean isOutOfScreen() {
		return this.getX() <= 0 || this.getX() >= this.getLayer().getWidth()
				|| this.getY() <= 0
				|| this.getY() >= this.getLayer().getHeight();
	}

	public Enemy getCollisionEnemy() {
		Enemy enemy = null;
		enemy = (Enemy) this.getOnlyCollisionObject(Enemy.class);
		return enemy;
	}

	public void removeFromLayer() {
		this.removeFlag = true;
		getLayer().removeObject(this);
	}

}
