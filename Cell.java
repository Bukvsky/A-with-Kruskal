import javax.swing.*;
import java.awt.*;

enum cellState{
    current,visited,unvisited
}
class Cell extends JLabel {
    public int x, y, size,g,h;
    public boolean leftWall, rightWall, upWall, downWall,isWall;
    private Color color;

    public Cell(int x, int y, int size, Color color,boolean isWall) {
        super();
        setLayout(new OverlayLayout(this));
        setOpaque(true);
        setBackground(color);
        this.x = x;
        this.y = y;
        this.size = size;
        this.isWall =isWall;
        this.leftWall = this.rightWall = this.upWall = this.downWall = false;
        this.g = Integer.MAX_VALUE;  // Domyślnie ustawiamy na nieskończoność
        this.h = 0;
        setVisible(true);
    }
    public int f() {
        return g + h;
    }

    public void setColor(Color color) {
        SwingUtilities.invokeLater(() -> {
            this.setBackground(color);
            repaint();
        });
    }


    public boolean isLeftWall() { return leftWall; }
    public boolean isRightWall() { return rightWall; }
    public boolean isUpWall() { return upWall; }
    public boolean isDownWall() { return downWall; }

    public void setLeftWall(boolean leftWall) { this.leftWall = leftWall; repaint(); }
    public void setRightWall(boolean rightWall) { this.rightWall = rightWall; repaint(); }
    public void setUpWall(boolean upWall) { this.upWall = upWall; repaint(); }
    public void setDownWall(boolean downWall) { this.downWall = downWall; repaint(); }
}