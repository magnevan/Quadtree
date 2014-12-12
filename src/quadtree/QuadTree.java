package quadtree;

import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magne on 12-Dec-14.
 */
public class QuadTree<T> {
  public final int size;

  protected final int x;
  protected final int y;

  // If isTerminal is true, value is set, else quadrants are set
  protected boolean isTerminal;
  protected T value;
  protected List<QuadTree<T>> quadrants;

  /**
   *   q1  |  q2
   * ------+-----
   *   q3  |  q4
   */


  public QuadTree(int size, T value) {
    this(0, 0, size, value);
  }

  private QuadTree(int x, int y, int size, T value) {
    if (!isPowerOfTwo(size)) {
      throw new IllegalArgumentException("size must be a power of two");
    }

    this.x = x;
    this.y = y;
    this.size = size;
    this.isTerminal = true;
    this.value = value;
  }

  public void set(int x, int y, T value) {
    if (size == 1) {
      isTerminal = true;
      this.value = value;
      return;
    }

    if (isTerminal == true) {
      int s = size / 2;
      quadrants = new ArrayList<>();
      quadrants.add(new QuadTree<T>(this.x, this.y, s, this.value));
      quadrants.add(new QuadTree<T>(this.x + s, this.y, s, this.value));
      quadrants.add(new QuadTree<T>(this.x, this.y + s, s, this.value));
      quadrants.add(new QuadTree<T>(this.x + s, this.y + s, s, this.value));
      isTerminal = false;
    }

    getQuadrant(x, y).set(x, y, value);

    boolean merge = true;
    for (QuadTree<T> q : quadrants) {
      if (!q.isTerminal || q.value != value) {
        merge = false;
        break;
      }
    }

    if (merge) {
      isTerminal = true;
      this.value = value;
      quadrants = null;
    }
  }

  public T get(int x, int y) {
    if (isTerminal == true) {
      return value;
    }

    return getQuadrant(x, y).get(x, y);
  }

  private QuadTree<T> getQuadrant(int x, int y) {
    if (y - this.y < size / 2) {
      if (x - this.x < size / 2) {
        return quadrants.get(0);
      } else {
        return quadrants.get(1);
      }
    } else {
      if (x - this.x < size / 2) {
        return quadrants.get(2);
      } else {
        return quadrants.get(3);
      }
    }
  }

  private boolean isPowerOfTwo(int x) {
    return (x != 0) && ((x & (x - 1)) == 0);
  }
}
