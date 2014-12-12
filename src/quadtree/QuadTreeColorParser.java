package quadtree;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * Created by Magne on 12-Dec-14.
 */
public class QuadTreeColorParser {
  public static void write(
    QuadTree<Color> quadtree,
    BufferedOutputStream stream
  ) throws IOException {
    if (quadtree.isTerminal) {
      stream.write(1);
      stream.write(quadtree.value.getRed());
      stream.write(quadtree.value.getGreen());
      stream.write(quadtree.value.getBlue());
    } else {
      stream.write(0);
      for (QuadTree<Color> child : quadtree.quadrants) {
        write(child, stream);
      }
    }
  }

  public static QuadTree<Color> read(BufferedInputStream stream)
    throws IOException
  {
    return null;
  }
}
