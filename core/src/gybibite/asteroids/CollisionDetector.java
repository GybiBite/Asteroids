package gybibite.asteroids;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public final class CollisionDetector {
  final Array<Entity> entities;
  Array<Entity> asts = new Array<>(new Entity[0]);
  Array<Entity> others = new Array<>(new Entity[0]);
  Entity e1;
  Entity e2;

  boolean e1Circle;
  boolean e2Circle;

  // variables for temporary use with overlaps()
  private static final Vector2 center = new Vector2();
  private static final Vector2 vec1 = new Vector2();
  private static final Vector2 vec2 = new Vector2();

  CollisionDetector(Array<Entity> entities) {
    this.entities = entities;
  }

  void checkForCollisions() {
    if (entities.size != 0) {
      for (int i = 0; i < entities.size; i++) {
        if (entities.get(i) instanceof EntityAsteroid) {
          asts.add(entities.get(i));
        } else {
          others.add(entities.get(i));
        }
      }

      for (Entity o : others) {
        e1 = o;
        for (Entity a : asts) {
          e2 = a;
          if (overlaps((Polygon) e1.getHitbox(), (Circle) e2.getHitbox())) {
            e2.notifyHit(e1);
            e1.notifyHit(e2);
          }
        }

        for (int i = 0; i < others.size; i++) {
          e2 = others.get(i);

          if (!e2.equals(e1) && Intersector.intersectPolygons((Polygon) e1.getHitbox(),
              (Polygon) e2.getHitbox(), null)) {
            e1.notifyHit(e2);
            e2.notifyHit(e1);
          }
        }
      }

      asts.clear();
      others.clear();
    }
  }

  
  // i stole this from stackoverflow lmao
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
