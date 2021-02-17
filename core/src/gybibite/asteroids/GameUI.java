package gybibite.asteroids;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameUI extends ScreenAdapter {
	
	static SpriteBatch batch;
	static ShapeRenderer s;
	Asteroids g;
	static Array<Entity> entities = new Array<>(new Entity[0]);

	private static final int SPAWN_TIME = 5000;

	private long timeLast;
	Random rand = new Random();

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
		/*
		 * Because of an apparent bug with iterators, this has to be a classic
		 * "for loop" and not "Object o : Array"
		 * 
		 * PS: iterators are awful :)
		 */
		for (int i = 0; i < entities.size; i++) {
			
			// If current entity check is on a player, check user input
			if(entities.toArray()[i].getId() == 0) {
				entities.toArray()[i].checkInput(new boolean[]{
						Gdx.input.isKeyPressed(Keys.UP),
						Gdx.input.isKeyPressed(Keys.DOWN),
						Gdx.input.isKeyPressed(Keys.LEFT),
						Gdx.input.isKeyPressed(Keys.RIGHT),
						Gdx.input.isButtonJustPressed(Buttons.LEFT)});
			}
			
			entities.toArray()[i].checkHit();
		}

		batch.begin(); // Begins the sprite batch. Required for libGDX
		for (Entity e : entities) {
			e.tick(delta);
			e.render(batch);
		}
		batch.end(); // Ends the sprite batch. Required for libGDX

		if (Asteroids.isVerbose()) {
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

		if (TimeUtils.timeSinceMillis(timeLast) > SPAWN_TIME) {
			timeLast = TimeUtils.millis();

			new EntityAsteroid(rand.nextInt(3), rand.nextInt(640), rand.nextInt(480));
			new EntityAsteroid(rand.nextInt(3), rand.nextInt(640), rand.nextInt(480));
			new EntityAsteroid(rand.nextInt(3), rand.nextInt(640), rand.nextInt(480));
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

	public static void destroyEntity(Entity e) {
		if (entities.contains(e, true)) {
			entities.removeValue(e, true);
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

	// variables for temp use with overlaps()
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
