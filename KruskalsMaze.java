import java.util.*;

public class KruskalsMaze {
    private static final int N = 1;
    private static final int S = 2;
    private static final int E = 4;
    private static final int W = 8;
    private static final Map<Integer, Integer> DX = new HashMap<>();
    private static final Map<Integer, Integer> DY = new HashMap<>();
    private static final Map<Integer, Integer> OPPOSITE = new HashMap<>();
    private static int[][] mazeGrid;
    private static int rows,cols;

    public static int getRows() {
        return rows;
    }

    public static int getCols() {
        return cols;
    }

    static {
        DX.put(E, 1);
        DX.put(W, -1);
        DX.put(N, 0);
        DX.put(S, 0);
        DY.put(E, 0);
        DY.put(W, 0);
        DY.put(N, -1);
        DY.put(S, 1);
        OPPOSITE.put(E, W);
        OPPOSITE.put(W, E);
        OPPOSITE.put(N, S);
        OPPOSITE.put(S, N);
    }

    private static class Tree {
        private Tree parent;

        public Tree() {
            this.parent = null;
        }

        public Tree root() {
            return parent != null ? parent.root() : this;
        }

        public boolean connected(Tree other) {
            return root() == other.root();
        }

        public void connect(Tree other) {
            other.root().parent = this;
        }
    }

    public static int[][] generateMaze(int width, int height, long seed) {
        Random rand = new Random(seed);
        int[][] grid = new int[height][width];
        Tree[][] sets = new Tree[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sets[y][x] = new Tree();
            }
        }

        List<int[]> edges = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y > 0) edges.add(new int[]{x, y, N});
                if (x > 0) edges.add(new int[]{x, y, W});
            }
        }

        Collections.shuffle(edges, rand);

        while (!edges.isEmpty()) {
            int[] edge = edges.remove(edges.size() - 1);
            int x = edge[0];
            int y = edge[1];
            int direction = edge[2];
            int nx = x + DX.get(direction);
            int ny = y + DY.get(direction);

            Tree set1 = sets[y][x];
            Tree set2 = sets[ny][nx];

            if (!set1.connected(set2)) {
                set1.connect(set2);
                grid[y][x] |= direction;
                grid[ny][nx] |= OPPOSITE.get(direction);
            }
        }

        putEntryAndExit(grid);

        return grid;
    }

    private static void putEntryAndExit(int[][] grid) {
        boolean entry = true;
        boolean exit = true;
        int i = 1;
        int j = grid.length - 2;

        while (entry || exit) {
            if (entry && i < grid.length && grid[i][1] == 0) {
                grid[i][0] = 0;
                entry = false;
            } else if (entry) {
                i++; }


            if (exit && j >= 0 && grid[j][grid[j].length - 2] == 0) {
                exit = false;
                grid[grid.length-2][grid.length-1] = 0;

            } else if (exit) {
                j--;
            }

            if (i >= grid.length && j < 0) {
                break;
            }

        }

    }



    public static int[][] getMazeGrid(int[][] grid) {
        rows = grid.length * 2 + 1;
        cols = grid[0].length * 2 + 1;
        mazeGrid = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mazeGrid[i][j] = 1;
            }
        }

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                int cell = grid[y][x];

                mazeGrid[y * 2 + 1][x * 2 + 1] = 0;

                if ((cell & S) != 0) mazeGrid[y * 2 + 2][x * 2 + 1] = 0; // Południe
                if ((cell & E) != 0) mazeGrid[y * 2 + 1][x * 2 + 2] = 0; // Wschód
            }
        }
        putEntryAndExit(mazeGrid);


        return mazeGrid;
    }

    public static void main(String[] args) {
        int width = 10;
        int height = 10;
        long seed = System.currentTimeMillis();

        int[][] maze = generateMaze(width, height, seed);



    }
}