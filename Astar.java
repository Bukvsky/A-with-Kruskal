import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Astar {
    private Cell[][] cells;
    private int rows, cols;
    private Cell startCell, endCell;
    private List<Cell> path;
    private Set<Cell> visitedCells;
    private final Object lock = new Object();

    public Astar(Cell[][] cells, Cell startCell, Cell endCell) {
        this.cells = cells;
        this.rows = cells.length;
        this.cols = cells[0].length;
        this.startCell = startCell;
        this.endCell = endCell;
        this.path = new ArrayList<>();
        this.visitedCells = new HashSet<>();
    }

    private int heuristic(Cell a, Cell b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    public List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int nx = cell.x + dx[i];
            int ny = cell.y + dy[i];
            if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && !cells[nx][ny].isWall) {
                neighbors.add(cells[nx][ny]);
            }
        }
        return neighbors;
    }

    public void findPath() {
        new Thread(() -> {
            PriorityQueue<Cell> openSet = new PriorityQueue<>(Comparator.comparingInt(Cell::f));
            Map<Cell, Cell> cameFrom = new HashMap<>();
            Map<Cell, Integer> gScore = new HashMap<>();
            Map<Cell, Integer> fScore = new HashMap<>();

            openSet.add(startCell);
            gScore.put(startCell, 0);
            fScore.put(startCell, heuristic(startCell, endCell));

            while (!openSet.isEmpty()) {
                Cell current = openSet.poll();
                visitedCells.add(current);

                current.setColor(Color.RED);
                if (current.equals(endCell)) {
                    reconstructPath(cameFrom, current);
                    updateColors();
                    return;
                }

                for (Cell neighbor : getNeighbors(current)) {
                    int tentativeGScore = gScore.get(current) + 1;
                    if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                        cameFrom.put(neighbor, current);
                        gScore.put(neighbor, tentativeGScore);
                        fScore.put(neighbor, tentativeGScore + heuristic(neighbor, endCell));

                        if (!openSet.contains(neighbor)) {
                            openSet.add(neighbor);
                        }
                    }
                }

                try {
                    Thread.sleep(5); // Opóźnienie dla efektu animacji
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.updateColors();
        }).start();
        this.updateColors();
    }



    private void reconstructPath(Map<Cell, Cell> cameFrom, Cell current) {
        synchronized (lock) {
            path.clear();
            while (current != null) {
                path.add(current);
                current = cameFrom.get(current);
            }
            Collections.reverse(path);
        }
    }


    public List<Cell> getPath() {
        synchronized (lock) {
            return new ArrayList<>(path);
        }
    }

    public Set<Cell> getVisitedCells() {
        synchronized (lock) {
            return new HashSet<>(visitedCells);
        }
    }
    public void updateColors() {
        SwingUtilities.invokeLater(() -> {
            for (Cell cell : visitedCells) {
                if (!path.contains(cell)) {
                    cell.setColor(new Color(121,182,201)); // Odwiedzona, ale nie w ścieżce
                }
            }
            for (Cell cell : path) {
                cell.setColor(Color.RED); // Należy do końcowej ścieżki
            }
        });
    }

}
