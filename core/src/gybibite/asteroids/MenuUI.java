package gybibite.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class MenuUI extends ScreenAdapter {

	SpriteBatch sb;
	ShapeRenderer sr;
	Asteroids g;
	Array<Button> main = new Array<>(new Button[0]);

	public MenuUI(Asteroids g) {
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		this.g = g;
		main.add(new Button(300, 100, 175, 40, "Start game", sb, sr).setClickEvent(() -> g.switchScreen(1)));
		main.add(new Button(490, 100, 175, 40, "Options", sb, sr).setClickEvent(() -> g.switchScreen(1)));
	}

	@Override
	public void show() {
		// not needed?
	}

	@Override
	public void render(float delta) {
		for (Button b : main) {
			b.render();
			b.checkHover(Gdx.input.getX(), Gdx.input.getY(), Gdx.input.isButtonJustPressed(Buttons.LEFT));
		}	
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