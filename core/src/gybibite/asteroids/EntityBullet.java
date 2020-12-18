package gybibite.asteroids;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EntityBullet extends Entity {
	
	EntityPlayer pl;
	float originX, originY;
	/** Max bullet speed */
	static final float BULLET_SPEED = 500;
	/** How far does the bullet move before dying */
	static final float BULLET_DIST = 400;

	public EntityBullet(SpriteBatch batch, float scale, EntityPlayer pl, float originX, float originY) {
		super(batch, scale, new Texture("bullet.png"));
		this.pl = pl;
		this.originX = originX;
		this.originY = originY;
		this.rot = -pl.rot; // For some reason this rotation is backwards
		
		x = originX;
		y = originY;
		
		vy = (float) (Math.cos(Math.toRadians(rot))) * BULLET_SPEED;
		vx = (float) (Math.sin(Math.toRadians(rot))) * BULLET_SPEED;
		
		setHitbox();
	}

	@Override
	void tick() {
		if (Math.abs(Math.hypot(x - originX, y - originY)) >= BULLET_DIST) {
			this.kill();
		}
		
		x += vx * delta;
		y += vy * delta;
	}

	@Override
	void setHitbox() {
	}
}