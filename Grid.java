import javax.swing.*;
import java.awt.*;

public class Grid extends JPanel {
    private static int[][] mazeGrid;
    private JLayeredPane layeredPane;
    private Cell[][] cells;
    private Cell start,end;

    public Cell[][] getCells() {
        return cells;
    }

    public Grid(int size) {
    }

    public Cell getStart() {
        return start;
    }

    public Cell getEnd() {
        return end;
    }

    public void startMaze(int size) {
        JFrame mazeFrame = new JFrame("Maze Editor");
        mazeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mazeFrame.setLayout(new BorderLayout());
        long seed = System.currentTimeMillis();
        mazeGrid = KruskalsMaze.getMazeGrid(KruskalsMaze.generateMaze(size, size, seed));
        int mazeSize = mazeGrid.length;

        cells = new Cell[mazeSize][mazeSize];
        int cellSize = 800 / mazeSize; // Obliczenie rozmiaru komórek
        int panelSize = cellSize * (mazeSize); // Dopasowanie panelu dokładnie do labiryntu

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(panelSize, panelSize));
        layeredPane.setLayout(null);
        layeredPane.setBackground(Color.black);
        for (int i = 0; i < mazeGrid.length; i++) {
            for (int j = 0; j < mazeGrid[i].length; j++) {
                Color cellColor;

                cellColor = (mazeGrid[i][j] == 0) ? Color.WHITE : Color.BLACK;
                Cell cell = new Cell(i, j, cellSize, cellColor,(mazeGrid[i][j] == 0) ? false:true);
                cell.setBounds(j * cellSize, i * cellSize, cellSize, cellSize);
//                JLabel jLabel = new JLabel(""+j);
//                jLabel.setSize(new Dimension(cellSize,cellSize));
//                jLabel.setForeground(Color.YELLOW);
//                cell.add(jLabel);

                layeredPane.add(cell, JLayeredPane.DEFAULT_LAYER);
                cells[i][j] = cell;
                if(j==0&&mazeGrid[i][j] ==0){
                    start=cell;
                }
                if(j==mazeGrid[i].length-1 && mazeGrid[i][j] == 0){
                    end=cell;
                }

            }
        }

        mazeFrame.add(layeredPane, BorderLayout.CENTER);
        mazeFrame.pack(); // Dopasowanie rozmiaru okna do zawartości
        mazeFrame.setLocationRelativeTo(null); // Centrowanie okna na ekranie
        mazeFrame.setVisible(true);
    }
    }

