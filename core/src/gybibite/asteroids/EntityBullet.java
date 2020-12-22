package gybibite.asteroids;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.TimeUtils;

public class EntityBullet extends Entity {

	EntityPlayer pl;
	float originX, originY;
	/** Max bullet speed */
	static final float BULLET_SPEED = 500;
	/** How long does the bullet live before dying (ms) */
	static final float BULLET_TIME = 800;
	
	long age;

	public EntityBullet(float scale, EntityPlayer pl) {
		super(scale, 1, new Texture("bullet.png"));
		this.pl = pl;
		this.originX = pl.x;
		this.originY = pl.y;
		this.rot = pl.rot;

		x = originX;
		y = originY;

		vy = (float) (Math.cos(Math.toRadians(rot))) * BULLET_SPEED;
		vx = -(float) (Math.sin(Math.toRadians(rot))) * BULLET_SPEED;
		
		age = TimeUtils.millis();

		setHitbox();
	}

	@Override
	void tick() {
		// If the bullet has been alive as long as BULLET_TIME without hitting anything, kill the bullet
		if (TimeUtils.timeSinceMillis(age) >= BULLET_TIME) {
			this.kill();
		}

		x += vx * delta;
		y += vy * delta;
		
		if (x <= 0) {
			x = S_WIDTH;
		} else if (x >= S_WIDTH) {
			x = 0;
		}

		if (y <= 0) {
			y = S_HEIGHT;
		} else if (y >= S_HEIGHT) {
			y = 0;
		}

		hitbox.setRotation(rot);
		hitbox.setPosition(x, y);
	}

	@Override
	void setHitbox() {
		hb = new float[] { 0 - sprite.getWidth(), sprite.getHeight(),
				sprite.getWidth(), sprite.getHeight(),
				sprite.getWidth(), 0 - sprite.getHeight(),
				0 - sprite.getWidth(), 0 - sprite.getHeight() };

		hitbox = new Polygon(hb);
	}
}