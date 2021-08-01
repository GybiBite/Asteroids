package gybibite.asteroids;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import gybibite.asteroids.ParticleEmitter.Particle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameUI extends ScreenAdapter {
	
	static SpriteBatch batch;
	static ShapeRenderer s;
	Asteroids g;
	static Array<Entity> entities = new Array<>(new Entity[0]);
	static Array<Particle> particles = new Array<>(new Particle[0]);

	private final int NEXTLVL_DELAY = 3000;
	int nextLvlTimer = -1;
	private int level = 0;
	private boolean enemiesStillAlive, playerStillAlive;

	private long timeLast;
	Random rand = new Random();
	static CollisionDetector collide = new CollisionDetector(entities);
	EntityPlayer playerOne;

	GameUI(Asteroids g){
		this.g = g;
	}
	
	@Override
	public void show() {
		batch = new SpriteBatch();
		s = new ShapeRenderer();
		new EntityPlayer(1.875f, 1); // Creates a new player (ship), while passing the sprite batch so it can render
	}

	@Override
	public void render(float delta) {
		tick(delta);

		batch.begin(); // Begins the sprite batch. Required for libGDX
		for (Entity e : entities) {			
			e.render(batch);
		}
		batch.end(); // Ends the sprite batch. Required for libGDX
		
		if (Asteroids.isDebug()) {
			for (Entity e : entities) {
				Gdx.app.log("ENTITYLIST", e.toString()); // Print entity info
				s.begin(ShapeType.Line); // Begin the shape renderer for type Line
				Gdx.gl.glLineWidth(1);
				s.setColor(1, 1, 0, 1); // Set the line color to yellow
				e.drawHB(); // Draw the polygon representing the polygon
				s.end(); // End the shape renderer
			}
			Gdx.app.log("ENTITYLIST", "===== END OF FRAME =====");
		}
	}
	
	public void tick(float delta) {
		enemiesStillAlive = playerStillAlive = false;
		/*
		 * Because of an apparent bug with iterators, this has to be a classic
		 * "for loop" and not "Object o : Array"
		 * 
		 * PS: iterators are awful :)
		 */
		for (int i = 0; i < entities.size; i++) {
			// If current entity check is on a player, check user input
			if(entities.toArray()[i] instanceof EntityPlayer) {
				playerOne = (EntityPlayer) entities.toArray()[i];
				entities.toArray()[i].checkInput(new boolean[]{
				/*0*/	Gdx.input.isKeyPressed(Keys.UP),
				/*1*/	Gdx.input.isKeyPressed(Keys.DOWN),
				/*2*/	Gdx.input.isKeyPressed(Keys.LEFT),
				/*3*/	Gdx.input.isKeyPressed(Keys.RIGHT),
				/*4*/	Gdx.input.isButtonJustPressed(Buttons.LEFT),
				/*5*/	Gdx.input.isKeyJustPressed(Keys.ALT_RIGHT)});
				
				playerStillAlive = true;
			}
			// If current entity is considered an Enemy, tell the game enemies are still alive
			if(entities.toArray()[i] instanceof Enemy) {
				enemiesStillAlive = true;
			}
		}
		
		for(Particle p : particles) {
			p.tick(s);
		}
		for(Entity e : entities) {
			e.tick(delta);
			collide.checkForCollisions();
		}
		
		if(!enemiesStillAlive) {
			if(nextLvlTimer == -1) {
				nextLvlTimer = NEXTLVL_DELAY;
			} else if (TimeUtils.timeSinceMillis(nextLvlTimer) >= NEXTLVL_DELAY) {
				nextLvlTimer = -1;
				new EntityAsteroid(2, 200, 200);
				new EntityAsteroid(2, 200, 200);
			}
		}
		
		if(!playerStillAlive) {
			if(nextLvlTimer == -1) {
				nextLvlTimer = NEXTLVL_DELAY;
			} else if (TimeUtils.timeSinceMillis(nextLvlTimer) >= NEXTLVL_DELAY) {
				nextLvlTimer = -1;
				entities.clear();
				particles.clear();
				g.switchScreen(0);
			}
		}
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		batch.dispose();

		for (Entity e : entities) {
			e.dispose();
			destroyEntity(e);
		}
	}

	public static void addEntity(Entity e) {
		entities.add(e);
	}
	
	public static void addParticle(Particle p) {
		particles.add(p);
	}

	public static void destroyEntity(Entity e) {
		if (entities.contains(e, true)) {
			entities.removeValue(e, true);
		}
	}
	
	public static void destroyParticle(Particle p) {
		if (particles.contains(p, true)) {
			particles.removeValue(p, true);
		}
	}

	public static Array<Entity> getEntities() {
		return entities;
	}

	public static void drawPoly(float[] vert) {
		s.polygon(vert);
	}

	public static void drawCirc(Circle c) {
		s.circle(c.x, c.y, c.radius);
	}
}
