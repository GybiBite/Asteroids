package gybibite.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

interface Enemy { }

public abstract class Entity {

  /** Placeholder variable for the amount of clone per entity. */
  public static final int CLONE_COUNT = 8;

  /** The main sprite object that represents this entity on screen.<p>
   *
   * FIXME: Every call to the size-getting methods use a scuffed fix for the
   * screen scaling that multiples every "size" by a fixed constant which
   * represents the difference between old and new screen size.
   */
  final Sprite sprite;
  /** Clones for screen wrapping. */
  final Sprite[] clones = new Sprite[CLONE_COUNT];
  /** Polygon object for controlling the hitbox. */
  Polygon hitbox;
  /* Positional variables */
  float x;
  float y;
  float vx;
  float vy;
  float rot;
  /** Hitbox definition. */
  float[] hb;

  /** The primary constructor for creating a new entity.
   *
   * @param scale How to scale the texture for the entity
   * @param texture The texture to use (assets directory)
   */
  Entity(final float scale, final Texture texture) {

    sprite = new Sprite(texture);

    // Set all "clones" to the same texture
    for (int i = 0; i < clones.length; i++) {
      clones[i] = new Sprite(texture);
    }

    // Apply scaling
    sprite.setScale(scale);
    for (Sprite s : clones) {
      s.setScale(scale);
    }

    // Keep track of this entity in the entity list
    GameUI.addEntity(this);
  }

  /** Returns properly centered X position of the entity, for LibGDX.
  *
  * @param xPos Uncorrected X position
  * @return Corrected X position
  */
  float centerX(final float xPos) {
    return x - sprite.getWidth() / 2;
  }

  /** Returns properly centered Y position of the entity, for LibGDX.
   *
   * @param yPos Uncorrected Y position
   * @return Corrected Y position
   */
  float centerY(final float yPos) {
    return yPos - sprite.getHeight() / 2;
  }

  /** Returns the velocity of the entity, in a float array */
  float[] getV() {
    return new float[] {vx, vy};
  }

  /** Returns the X position of the entity */
  float getX() {
    return x;
  }

  /** Returns the Y position of the entity */
  float getY() {
    return y;
  }

  /** Returns the rotation angle of the entity */
  float getRot() {
    return rot;
  }

  /** Positions "clones" to emulate screen texture wrapping. */
  void posClones() {
    clones[0].setPosition(
        centerX(x) - Asteroids.S_WIDTH,
        centerY(y) - Asteroids.S_HEIGHT); // Top right
    clones[1].setPosition(
        centerX(x),
        centerY(y) - Asteroids.S_HEIGHT); // Top middle
    clones[2].setPosition(
        centerX(x) + Asteroids.S_WIDTH,
        centerY(y) - Asteroids.S_HEIGHT); // Top left
    clones[3].setPosition(
        centerX(x) - Asteroids.S_WIDTH,
        centerY(y));                      // Middle right
    clones[4].setPosition(
        centerX(x) + Asteroids.S_WIDTH,
        centerY(y));                      // Middle left
    clones[5].setPosition(
        centerX(x) - Asteroids.S_WIDTH,
        centerY(y) + Asteroids.S_HEIGHT); // Bottom right
    clones[6].setPosition(
        centerX(x),
        centerY(y) + Asteroids.S_HEIGHT); // Bottom middle
    clones[7].setPosition(
        centerX(x) + Asteroids.S_WIDTH,
        centerY(y) + Asteroids.S_HEIGHT); // Bottom left
  }

  /** Tell the {@link SpriteBatch} to render the entity.
   *
   * @param batch The sprite batch to be passed to the draw command
   */
  public void render(final SpriteBatch batch) {
    // Set sprite location
    sprite.setPosition(centerX(x), centerY(y));
    posClones();
    // Tell the sprite batch to draw
    sprite.setRotation(rot);
    sprite.draw(batch);
    for (Sprite s : clones) {
      s.setRotation(rot);
      s.draw(batch);
    }
  }

  /** Tells the game UI to draw the hitbox to the screen. */
  void drawHB() {
    GameUI.drawPoly(hitbox.getTransformedVertices());
  }

  /** Disposes of any "Disposable" objects.
   *
   * @see com.badlogic.gdx.utils#Disposable
   */
  public void dispose() {
  }

  /** Removes the entity when necessary. */
  public void delete() {
    GameUI.destroyEntity(this);
    dispose();
  }

  /** Defines how the entity interacts with the world every game cycle.
   *
   * @param delta The time difference between the last tick and the current tick
   */
  abstract void tick(float delta);

  /** Sets the hitbox vertices. */
  abstract void setHitbox();

  /** Gets the hitbox object of the entity.
   *
   * @return Hitbox object (Polygon or Circle)
   */
  abstract Object getHitbox();

  /** Called if the entity was hit by another entity.
   *
   * @param e The entity type that the current entity was hit by
   */
  abstract void notifyHit(Entity e);

  /** Used for death effects before removing the entity. */
  abstract void die();

  /** Some debug stuff. */
  @Override
  public String toString() {
    return "-----------------" + "\n" +
        "Entity: " + this.getClass().getSimpleName() + " at "
        + GameUI.entities.indexOf(this, true) + "\n"
        + "Velocity: (" + vx + ", " + vy + ")" + "\n"
        + "Position: (" + x + ", " + y + ")";
  }

  /** Allows the entity to register key events.<br>
   * Intended to be overridden by {@link EntityPlayer}
   *
   * @param buttons Array of flags for which buttons are used
   */
  public void checkInput(final boolean[] buttons) {
    Gdx.app.log("INPUT", "Input can only be checked for a playable entity");
  }
}
