package gybibite.asteroids;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.TimeUtils;

public class ParticleEmitter {

	/** The size of the particle to be emitted */
	float size;
	/** The color of the particle to be emitted */
	Color color;
	/**  */
	Texture tex;
	/** The variation in the angle at which the particle is spawned */
	float angleVar;
	/** The radius around the spawn point that particles can spawn */
	float spawnRange;
	/** The time to live for particles */
	float lifetime;
	/** The variation for the time to live */
	float lifeVar;
	/** The Random object for generating random values */
	Random rand;

	/**
	 * Creates a new ParticleEmitter object to spawn particles from.<br>
	 * It stores the variation values for how much to randomize the particles that it spawns.
	 * 
	 * @param size How large the particle(s) will be, in pixels
	 * @param color What color value to draw the particle(s) with
	 * @param angleVar How much can the angle of the particle(s) vary
	 * @param spawnRange How far away the particle(s) can spawn from the spawn point
	 * @param lifetime How long should the particle(s) live for
	 * @param lifeVar How much variation the lifetime of the particle(s) can have
	 */
	ParticleEmitter(float size, Color color, float angleVar, float spawnRange, float lifetime, float lifeVar) {
		this.size = size;
		this.color = color;
		this.angleVar = angleVar;
		this.spawnRange = spawnRange;
		this.lifetime = lifetime;
		this.lifeVar = lifeVar;
		rand = new Random();
	}

	
	ParticleEmitter(float size, Texture tex, float angleVar, float spawnRange, float lifetime, float lifeVar) {
		this.size = size;
		this.tex = tex;
		this.angleVar = angleVar;
		this.spawnRange = spawnRange;
		this.lifetime = lifetime;
		this.lifeVar = lifeVar;
		rand = new Random();
	}
	 

	/**
	 * Creates a new particle object to be displayed on screen
	 * 
	 * @param x The X position of the spawn point
	 * @param y The Y position of the spawn point
	 * @param angle What angle the particle should move at
	 * @param speed How fast should the particle go
	 */
	void emit(float x, float y, float angle, float speed) {
		int angleOffsetSign = rand.nextBoolean() ? 1 : -1;
		int lifeOffsetSign = rand.nextBoolean() ? 1 : -1;
		int xOffsetSign = rand.nextBoolean() ? 1 : -1;
		int yOffsetSign = rand.nextBoolean() ? 1 : -1;

		float angleOffset = (float) Math.random() * angleVar * angleOffsetSign;
		float lifeOffset = (float) Math.random() * lifeVar * lifeOffsetSign;
		float xOffset = (float) Math.random() * spawnRange * xOffsetSign;
		float yOffset = (float) Math.random() * spawnRange * yOffsetSign;

		GameUI.addParticle(new Particle(x + xOffset, y + yOffset, angle + angleOffset, speed, lifetime + lifeOffset, color, size));
	}

	class Particle {

		float x, y;
		float angle;
		float speed;
		float lifetime;
		Color color;
		float size;
		long lifeTimer;

		Particle(float x, float y, float angle, float speed, float lifetime, Color color, float size) {
			this.x = x;
			this.y = y;
			this.angle = angle;
			this.speed = speed;
			this.lifetime = lifetime;
			this.color = color;
			this.size = size;
			this.lifeTimer = TimeUtils.millis();
		}

		void tick(ShapeRenderer s) {

			/* If the particle's life timer has exceeded its allowed lifetime, remove it */
			if (TimeUtils.timeSinceMillis(lifeTimer) >= lifetime) {
				GameUI.destroyParticle(this);
			}

			/* Draw the particle */
			s.begin(ShapeType.Filled);
			s.setColor(color);
			s.rect(x, y, size, size);
			s.end();

			/* Move the particle to its next position */
			x -= (float) (Math.sin(Math.toRadians(angle))) * speed;
			y -= (float) (Math.cos(Math.toRadians(angle))) * speed;
		}
	}
}
