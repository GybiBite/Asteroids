package gybibite.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;

interface EventListener {
	public void trigger();
}

public class Button {

	/** X position of the button's center */
	private int x;
	/** Y position of the button's center */
	private int y;

	private int width;
	private int height;
	private float[] corners;

	/** Text to be displayed on the button */
	private String label;
	private ShapeRenderer sr;
	private SpriteBatch sb;
	private BitmapFont font = new BitmapFont(Gdx.files.internal("assets/fsex300.fnt"),
			Gdx.files.internal("assets/fsex300.png"), false);

	private Array<EventListener> events;

	public Button(int width, int height, int x, int y, String label, SpriteBatch sb, ShapeRenderer sr) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.label = label;
		this.sb = sb;
		this.sr = sr;
		corners = new float[] { x - width / 2, y + height / 2, x + width / 2, y + height / 2, x - width / 2,
				y - height / 2, x + width / 2, y - height / 2 };
	}

	public void addClickEvent(EventListener listener) {
		events.add(listener);
	}

	public void runEvents() {
		for (EventListener l : events) {
			l.trigger();
		}
	}

	public void render() {

	}

	public void checkHover(int mouseX, int mouseY, boolean mouseClicked) {
		if (mouseX > x - width / 2 && mouseX < x + width / 2 && mouseY > y - height / 2 && mouseY < y + height / 2) {
			// TODO make a draw method of sorts, determine button style for drawing
			drawOutline();
			if (mouseClicked)
				runEvents();
		}
	}

	public void drawOutline() {
		sr.begin(ShapeType.Line);
		sr.setColor(1, 1, 1, 1);
		sr.polygon(corners);
		sr.end();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "Button: " + label + "Position: (" + x + ", " + y + ")" + "Size: (" + width + ", " + height + ")";
	}
}
