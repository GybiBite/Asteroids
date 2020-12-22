package gybibite.asteroids;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;

public class EntityAsteroid extends Entity {

	int size;
	Polygon[] cl;

	public EntityAsteroid(float scale, int size, float x, float y) {
		super(scale, 2, setTexture(size));

		this.size = size;
		this.x = x;
		this.y = y;

		setHitbox();
		
		double speed = Math.random();
		
		rot = (float) ((speed * 720) - 360);
		
		speed *= 100;
		
		vy += (float) (Math.cos(Math.toRadians(rot))) * speed;
		vx -= (float) (Math.sin(Math.toRadians(rot))) * speed;
	}

	@Override
	void tick() {
		
	}

	@Override
	void setHitbox() {
		
	}

	@Override
	void drawHB() {
		Asteroids.s.begin(ShapeType.Line);
		Asteroids.s.setColor(1, 1, 0, 1);
		Asteroids.s.polygon(hitbox.getTransformedVertices());
		Asteroids.s.end();
	}

	static Texture setTexture(int size) {
		switch (size) {
		case 0:
			return new Texture("asteroid_small.png");
		case 1:
			return new Texture("asteroid.png");
		case 2:
			return new Texture("asteroid_large.png");
		default:
			return new Texture("asteroid.png");
		}
	}
}