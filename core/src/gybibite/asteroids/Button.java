package gybibite.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;

interface EventListener {
	void trigger();
}

public class Button {
	
	/** Position of the button's center. */
	private int x, y;

	/** Size of the button */
	private int width, height;

	/** Hold the position of the corners of the button. */
	private float[] corners;

	/** Represents if the button is clickable */
	private boolean enabled = true;

	/** If true, highlights the outline of the button */
	private boolean highlighted;

	/** Text to be displayed on the button */
	private String label;

	private ShapeRenderer sr;
	private SpriteBatch sb;
	private GlyphLayout gly = new GlyphLayout();

	/** What file to use for the font */
	private final BitmapFont font = new BitmapFont(Gdx.files.internal("fsex300.fnt"),
			Gdx.files.internal("fsex300.png"), false);

	/**
	 * When {@link #setClickEvent(EventListener)} is called, holds the button's
	 * action
	 */
	private EventListener event;

	/**
	 * Clickable button object that can be set to perform any generic set of
	 * commands. The text of the button will scale based on the width.
	 * 
	 * @param x      X position of the button
	 * @param y      Y position of the button
	 * @param width  Width of the button, in pixels
	 * @param height Height of the button, in pixels
	 * @param label  The string of text to be displayed
	 * @param sb     SpriteBatch to be passed for rendering
	 * @param sr     ShapeRenderer to be passed for rendering
	 */
	public Button(int x, int y, int width, int height, String label, SpriteBatch sb, ShapeRenderer sr) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.label = label;
		this.sb = sb;
		this.sr = sr;
		corners = new float[] { x - width / 2, y + height / 2, x + width / 2, y + height / 2, x + width / 2,
				y - height / 2, x - width / 2, y - height / 2 };
		gly.setText(font, label);

		font.getData().setScale(Math.min(((float) (width * 0.8) / gly.width), (float) (height * 0.75) / gly.height));
		
		if(this instanceof EventListener) {
			
		}
	}

	/**
	 * Set the action to be performed by the button
	 * 
	 * @param listener Instance of EventListener to be assigned an action
	 */
	public Button setClickEvent(EventListener listener) {
		event = listener;
		return this;
	}

	/**
	 * Run the action previously set for the button. Intended for internal use, but
	 * no harm will be done by being used outside of the class.
	 */
	public void runEvent() {
		event.trigger();
	}

	/**
	 * Renders the button to the screen using the previously passed ShapeRenderer
	 * and SpriteBatch.
	 */
	public void render() {
		sr.begin(ShapeType.Line);
		if (highlighted)
			sr.setColor(1, 1, 1, 1);
		else
			sr.setColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glLineWidth(3);
		for (int i = 0; i <= corners.length; i += 2) {
			sr.line(corners[i % 8], corners[(i + 1) % 8], corners[(i + 2) % 8], corners[(i + 3) % 8]);
		}
		sr.end();

		sb.begin();
		if (enabled)
			font.setColor(1, 1, 1, 1);
		else
			font.setColor(0.7f, 0.7f, 0.7f, 1);
		font.draw(sb, label, x - width / 2, y + font.getLineHeight() / 4, width, Align.center, false);
		sb.end();
	}

	/**
	 * Checks if the mouse is hovering over the button to highlight it, and
	 * subsequently checks for a mouse click.
	 */
	public void checkHover(int mouseX, int mouseY, boolean mouseClicked) {
		highlighted = false;
		mouseY = Math.abs(mouseY - Asteroids.S_HEIGHT);
		if (enabled && mouseX > x - width / 2 && mouseX < x + width / 2 && mouseY > y - height / 2
				&& mouseY < y + height / 2) {
			highlighted = true;
			if (mouseClicked)
				runEvent();
		}
	}

	/**
	 * Sets whether or not you can click on the button. If the button is disabled,
	 * the text will be grayed out.
	 * 
	 * @param enabled Whether or not button is enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
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

	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
		corners = new float[] { x - width / 2, y + height / 2, x + width / 2, y + height / 2, x - width / 2,
				y - height / 2, x + width / 2, y - height / 2 };
	}

	public int getY() {
		return y;
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
