package quadtree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Magne on 12-Dec-14.
 */
public class QuadTreeDisplay extends JPanel implements KeyListener{
  private QuadTree<Color> quadtree;

  private final int size = 64;
  private final int csize = 10;

  private boolean altDown = false;

  public QuadTreeDisplay() {
    this.quadtree = new QuadTree<>(size, Color.WHITE);

    final QuadTreeDisplay display = this;

    this.addMouseMotionListener(new MouseAdapter() {
      private int lx, ly;

      @Override
      public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);

        int nx = e.getX() / csize;
        int ny = e.getY() / csize;
        if (nx != lx || ny != ly) {
          lx = nx;
          ly = ny;
          display.set(nx, ny);
        }
      }
    });
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(csize*size+1, csize*size+1);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    g.setColor(Color.cyan);
    g.fillRect(0, 0, csize*size+1, csize*size+1);

    recursivePaint(g, quadtree);

    if (altDown) {
      g.setColor(Color.lightGray);
      for (int i = 0; i < size; i++) {
        g.drawLine(csize * i, 0, csize * i, csize * size);
        g.drawLine(0, csize * i, csize * size, csize * i);
      }
      g.drawLine(csize * size, 0, csize * size, csize * size);
      g.drawLine(0, csize * size, csize * size, csize * size);
    }
  }

  private void recursivePaint(Graphics g, QuadTree<Color> q) {
    if (q.isTerminal) {
      g.setColor(Color.darkGray);
      g.drawRect(
        csize * q.x,
        csize * q.y,
        csize * q.size + 1,
        csize * q.size + 1
      );
      g.setColor(q.value);
      g.fillRect(
        csize * q.x + 1,
        csize * q.y + 1,
        csize * q.size - 1,
        csize * q.size - 1
      );
    } else {
     for (QuadTree<Color> child : q.quadrants) {
       recursivePaint(g, child);
     }
    }
  }

  private void set(int x, int y) {
    quadtree.set(x, y, Color.BLACK);
    this.repaint();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame f = new JFrame("Quadtree demo");
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      QuadTreeDisplay display = new QuadTreeDisplay();
      f.addKeyListener(display);
      f.setContentPane(display);
      f.pack();
      f.setVisible(true);
    });
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ALT) {
      altDown = true;
      repaint();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ALT) {
      altDown = false;
      repaint();
    }
  }
}
