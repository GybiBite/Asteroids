package gybibite.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MenuUI extends ScreenAdapter {

	SpriteBatch sb;
	ShapeRenderer sr;
	Button button;
	Asteroids g;

	public MenuUI(Asteroids g) {
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		this.g = g;
	}

	@Override
	public void show() {
		button = new Button(400, 100, 175, 40, "Start game", sb, sr).setClickEvent(() ->
			g.switchScreen(1)
		);
	}

	@Override
	public void render(float delta) {
		button.render();
		button.checkHover(Gdx.input.getX(), Gdx.input.getY(), Gdx.input.isButtonJustPressed(Buttons.LEFT));
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}