package org.iSun.td;
//Download by http://www.codefans.net
import java.util.ArrayList;
import java.util.HashMap;

import org.iSun.td.constant.SystemConstant;
import org.iSun.td.constant.TurrentType;
import org.iSun.td.model.BombTurret;
import org.iSun.td.model.BulletTurret;
import org.iSun.td.model.Enemy;
import org.iSun.td.model.PosionTurret;
import org.iSun.td.model.SoundWaveTurret;
import org.iSun.td.model.Turret;
import org.loon.framework.android.game.action.map.Field2D;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.graphics.window.actor.Actor;
import org.loon.framework.android.game.core.graphics.window.actor.Layer;

import android.graphics.Color;

public class MapLayer extends Layer {
	private boolean start;
	private int startX, startY, endX, endY;
	private int enemyIndex;
	public TurretDefense td;
	private Menu menu;
	public ArrayList<Enemy> enemys = new ArrayList<Enemy>();
	private int[] baseValue = new int[] { 15, 30, 25, 50 };

	public MapLayer(TurretDefense td, Menu menu) {

		super(576, 480, true);
		this.menu = menu;
		this.td = td;
		setLocked(false);
		setActorDrag(false);
		HashMap<Integer, LImage> pathMap = new HashMap<Integer, LImage>(10);
		pathMap.put(new Integer(0), new LImage("assets/sand.png"));
		pathMap.put(new Integer(1), new LImage("assets/sandTurn1.png"));
		pathMap.put(new Integer(2), new LImage("assets/sandTurn2.png"));
		pathMap.put(new Integer(3), new LImage("assets/sandTurn3.png"));
		pathMap.put(new Integer(4), new LImage("assets/sandTurn4.png"));
		pathMap.put(new Integer(5), new LImage("assets/base.png"));
		pathMap.put(new Integer(6), new LImage("assets/castle.png"));
		setField2DBackground(new Field2D(td.mapName, 32, 32), pathMap,
				"assets/rock.png");
		this.td.field = getField2D();
		this.startX = td.start.x;
		this.startY = td.start.y;

		this.endX = td.end.x;
		this.endY = td.end.y;

		setDelay(LSystem.SECOND);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action(long elapsedTime) {
		// TODO Auto-generated method stub
		if (start) {
			if (enemyIndex < td.waveArray[td.wave % td.waveArray.length].length) {
				addEnemy(td.waveArray[td.wave % td.waveArray.length][enemyIndex]);
				enemyIndex++;
			} else if (enemyIndex >= td.waveArray[td.wave % td.waveArray.length].length
					&& enemys.size() <= 0) {
				td.wave++;
				enemyIndex = 0;
			}
		}
	}

	private void addEnemy(int type) {
		Enemy enemy = null;
		switch (type) {
		case SystemConstant.EnemyType.BASE_ENEMY:
			enemy = new Enemy("assets/enemy.png", startX, startY, endX, endY,
					3, 40 + (td.wave - 1) * 3, this);

			break;
		case SystemConstant.EnemyType.FASE_ENEMY:
			enemy = new Enemy("assets/fastEnemy.png", startX, startY, endX,
					endY, 5, 30 + (td.wave - 1) * 3, this);
			break;
		case SystemConstant.EnemyType.SMALL_ENEMY:
			enemy = new Enemy("assets/smallEnemy.png", startX, startY, endX,
					endY, 4, 25 + (td.wave - 1) * 3, this);
			break;
		case SystemConstant.EnemyType.BIG_ENEMY:
			enemy = new Enemy("assets/bigEnemy.png", startX, startY, endX,
					endY, 2, 50 + (td.wave - 1) * 2, this);
			break;
		}
		this.enemys.add(enemy);
		addObject(enemy);
	}
	private Actor o = null;

	@Override
	public void downClick(int x, int y) {

		if (td.beSelectedTurret != null && td.beSelectedTurret.selected) {
			td.beSelectedTurret.selected = false;
			menu.sell.setVisible(false);
			menu.update.setVisible(false);
		}
		int newX = x / td.field.getTileWidth();
		int newY = y / td.field.getTileHeight();
		if ((o = getClickActor()) == null && td.selectTurret != -1
				&& td.field.getType(newY, newX) == -1) {
			if (td.getMoney() >= baseValue[td.selectTurret]) {
				addTurret(td.selectTurret, x, y);
			}
			td.selectTurret = -1;
		}
		updateUI(o);
		super.downClick(newX, newY);
	}

	public void updateUI(Object turret) {
		if (o != null && o instanceof Turret) {
			Turret temp = (Turret) o;
			temp.selected = true;
			td.beSelectedTurret = temp;
			menu.sell.setVisible(true);
			if (td.beSelectedTurret.updateAble
					&& td.getMoney() >= td.beSelectedTurret.values[td.beSelectedTurret.currentLevel] / 2) {
				menu.update.setVisible(true);
			}
		}
	}

	public void addTurret(int type, int x, int y) {
		int newX = x / td.field.getTileWidth();
		int newY = y / td.field.getTileHeight();
		Turret turret = null;
		switch (type) {
		case TurrentType.BULLET_TURRET:
			turret = new BulletTurret(td.turrets[type], td);
			break;
		case TurrentType.BOMB_TURRET:
			turret = new BombTurret(td.turrets[type], td);
			break;
		case TurrentType.POSION_TURRET:
			turret = new PosionTurret(td.turrets[type], td);
			break;
		case TurrentType.SOUNDWAVE_TURRET:
			turret = new SoundWaveTurret(td.turrets[type], td);
			break;
		}
		td.setMoney(td.getMoney() - turret.values[0]);
		addObject(turret, newX * td.field.getTileWidth(),
				newY * td.field.getTileHeight());
	}

	public void doStart() {
		this.start = true;
	}

	@Override
	public void paint(LGraphics g) {
		// TODO Auto-generated method stub
		LColor color = g.getColor();
		g.setColor(Color.WHITE);
		g.drawString("" + td.lives, endX + 10, endY + 20);
		g.setColor(color);
	}

}
