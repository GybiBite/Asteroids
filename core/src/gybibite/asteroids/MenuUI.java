package gybibite.asteroids;

import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public final class MenuUI extends ScreenAdapter {

  SpriteBatch sb;
  ShapeRenderer sr;
  Asteroids g;
  Array<Button> main = new Array<>(new Button[0]);
  static Array<Entity> entities = new Array<Entity>(new Entity[0]);
  Random rand = new Random();
  
  static final int AST_DECOR_COUNT = 8;

  private final BitmapFont font =
      new BitmapFont(Gdx.files.internal("fsex300.fnt"), Gdx.files.internal("fsex300.png"), false);

  public MenuUI(Asteroids g) {
    sb = new SpriteBatch();
    sr = new ShapeRenderer();
    this.g = g;
    font.setColor(1, 1, 1, 1);
    main.add(
        new Button(Asteroids.S_WIDTH/2, 200, 175, 40, "Start game", sb, sr).setClickEvent(() -> g.switchScreen(1)));
    main.add(
        new Button(Asteroids.S_WIDTH/2, 125, 175, 40, "Options", sb, sr).setClickEvent(() -> g.switchScreen(2)));
  }

  @Override
  public void render(float delta) {
    tick(delta);
    sb.begin();
    for(Entity e : entities) {
      e.render(sb);
    }
    
    font.getData().setScale(1.2f);
    font.draw(sb, "ASTEROIDS", Asteroids.S_WIDTH / 2, 400, 0, Align.center, false);
    sb.end();
    
    for (Button b : main) {
      b.render();
      b.checkHover(Gdx.input.getX(), Gdx.input.getY(), Gdx.input.isButtonJustPressed(Buttons.LEFT));
    }
  }
  
  public void tick(float delta) {
    for (Entity e : entities) {
      e.tick(delta);
    }
  }
  
  @Override
  public void show() {
    for(int i = 0; i < AST_DECOR_COUNT; i++) {
      entities.add(new EntityAsteroid(rand.nextInt(3), rand.nextInt(Asteroids.S_WIDTH), rand.nextInt(Asteroids.S_HEIGHT)));
    }
    
    GameUI.entities.clear();
  }
  
  @Override
  public void hide() {
    entities.clear();
  }
}
