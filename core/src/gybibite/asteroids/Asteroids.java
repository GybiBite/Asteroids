package gybibite.asteroids;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Asteroids extends ApplicationAdapter {
	static SpriteBatch batch;
	static ShapeRenderer s;
	static Array<Entity> entities = new Array<Entity>(new Entity[0]);

	static boolean verbose = false;
	static Random rand = new Random();

	public static final int S_WIDTH = 640, S_HEIGHT = 480;

	// This is just here to parse arguments
	public Asteroids(String[] arg) {
		for (int i = 0; i < arg.length; i++) {
			if (arg[i].contentEquals("-v")) { // Handle verbose argument (shows some debug info)
				verbose = true;
			}
		}
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		s = new ShapeRenderer();
		Gdx.graphics.setWindowedMode(800, 600); // Doesn't set window res, just scales :(
		new EntityPlayer(1.5f); // Creates a new player (ship), while passing the sprite batch so it can render

		new EntityAsteroid(2, 100, 100);
		new EntityAsteroid(1, 100, 100);
		new EntityAsteroid(0, 100, 100);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0.05f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		for(int i = 0; i < entities.size; i++) {
			entities.toArray()[i].checkHit();
		}

		batch.begin();
		for (Entity e : entities) {
			e.tick();
			e.render(batch);
		}
		batch.end();

		if (verbose) {
			Gdx.graphics.setTitle("Asteroids - FPS: " + Math.floor(1 / Gdx.graphics.getDeltaTime() * 10) / 10);
			for (Entity e : entities) {
				System.out.println(e);
				s.begin(ShapeType.Line);
				s.setColor(1, 1, 0, 1);
				e.drawHB();
				s.end();
			}
			System.out.println("===== END OF FRAME =====");
		}

	}

	@Override
	public void dispose() {
		batch.dispose();

		for (Entity e : entities) {
			e.dispose();
		}
	}

	public static void addEntity(Entity e) {
		entities.add(e);
	}

	public static void destroyEntity(Entity e) {
		if (entities.contains(e, true)) {
			entities.removeValue(e, true);
		} else {
			System.err.println("ERROR: " + e + " not found in entity list! Skipping removal...");
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

	public static boolean isVerbose() {
		return verbose;
	}

	// Static variables for temp use with overlaps()
	private static final Vector2 center = new Vector2();
	private static final Vector2 vec1 = new Vector2();
	private static final Vector2 vec2 = new Vector2();

	public static boolean overlaps(Polygon polygon, Circle circle) {
		float[] vertices = polygon.getTransformedVertices();
		center.set(circle.x, circle.y);
		float squareRadius = circle.radius * circle.radius;
		for (int i = 0; i < vertices.length; i += 2) {
			if (i == 0) {
				if (Intersector.intersectSegmentCircle(
						vec1.set(vertices[vertices.length - 2], vertices[vertices.length - 1]),
						vec2.set(vertices[i], vertices[i + 1]), center, squareRadius))
					return true;
			} else {
				if (Intersector.intersectSegmentCircle(vec1.set(vertices[i - 2], vertices[i - 1]),
						vec2.set(vertices[i], vertices[i + 1]), center, squareRadius))
					return true;
			}
		}
		return polygon.contains(circle.x, circle.y);
	}
}