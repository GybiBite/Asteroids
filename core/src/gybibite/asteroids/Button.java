package gybibite.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;

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

	private boolean enabled = true;
	private boolean highlighted;

	/** Text to be displayed on the button */
	private String label;
	private ShapeRenderer sr;
	private SpriteBatch sb;
	private BitmapFont font = new BitmapFont(Gdx.files.internal("assets/fsex300.fnt"),
			Gdx.files.internal("assets/fsex300.png"), false);
	private GlyphLayout gly = new GlyphLayout();

	private EventListener event;

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

		font.getData().setScale((float) (width * 0.8) / gly.width);
	}

	public Button setClickEvent(EventListener listener) {
		this.event = listener;
		return this;
	}

	public void runEvent() {
		event.trigger();
	}

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
		if(enabled)
			font.setColor(1, 1, 1, 1);
		else
			font.setColor(0.7f, 0.7f, 0.7f, 1);
		font.draw(sb, label, x - width / 2, y + font.getLineHeight() / 4, width, Align.center, false);
		sb.end();
	}

	public void checkHover(int mouseX, int mouseY, boolean mouseClicked) {
		highlighted = false;
		if (enabled && mouseX > x - width / 2 && mouseX < x + width / 2 && mouseY > y - height / 2 && mouseY < y + height / 2) {
			highlighted = true;
			if (mouseClicked)
				runEvent();
		}
	}

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
