package pack;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static utils.Utils.pad;
import static utils.Utils.swap;

public class Board {
    private final int[][] tiles;

    public Board(int[][] tiles) {
        if (tiles.length != tiles[0].length) throw new IllegalArgumentException("Board must be square");
        this.tiles = new int[tiles.length][tiles.length];
        for (int row = 0; row < tiles.length; row++) {
            System.arraycopy(tiles[row], 0, this.tiles[row], 0, tiles.length);
        }
    }

    /** Creates a new random n * n board */
    public Board(int dimension) {
        tiles = new int[dimension][dimension];
        for (int i = 0; i < dimension * dimension; i++) {
            tiles[i / dimension][i % dimension] = i;
        }
        // 0 is the empty element

        // shuffle
        for (int row = dimension - 1; row > 0; row--) {
            for (int col = dimension - 1; col > 0; col--) {
                int swapRow = ThreadLocalRandom.current().nextInt(row + 1);
                int swapCol = ThreadLocalRandom.current().nextInt(col + 1);

                swap(tiles, swapRow, swapCol, row, col);
            }
        }
    }

    public int dimension() {
        return tiles.length;
    }

    /** @return number of tiles out of place */
    public int hamming() {
//        int outOfPlace = 0;
//        for (int row = 0; row < dimension(); row++) {
//            for (int col = 0; col < dimension(); col++) {
//                if (tiles[row][col] != row * dimension() + col) {
//                    outOfPlace++;
//                }
//            }
//        }
//        return outOfPlace;
        AtomicInteger idx = new AtomicInteger(1);
        return (int) Arrays.stream(tiles)
                .flatMapToInt(Arrays::stream) // flatten the array
                .filter(n -> idx.getAndIncrement() != n) // find the ones where the number does not equal its index + 1
                .count() - 1; // count them but the zero element never will so -1 for 0
    }

    public int manhattan() {
        int manhattan = 0;
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                int tile = tiles[row][col] - 1;
                int targetRow = tile / dimension();
                int targetCol = tile % dimension();

                manhattan += Math.abs(targetRow - row) + Math.abs(targetCol - col);
            }
        }
        return manhattan;
    }

    public boolean isGoal() {
        // return hamming == 0; // should work, however it doesn't need to keep counting after finding a false
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                if (tiles[row][col] != row * dimension() + col) {
                    return false;
                }
            }
        }
        return true;

    }

    public Iterable<Board> neighbors() {
        List<Board> boards = new LinkedList<>();

        int zeRow = -1; // haha get it get it??
        int zeCol = -1;

        int[][] copy = new int[dimension()][dimension()];
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                int tile = tiles[row][col];
                copy[row][col] = tile;
                if (tile == 0) {
                    zeRow = row;
                    zeCol = col;
                }
            }
        }

        assert zeRow >= 0 && zeRow <= dimension() && zeCol >= 0 && zeCol <= dimension();

        if (zeRow > 0) {
            swap(copy, zeRow, zeCol, zeRow - 1, zeCol);
            boards.add(new Board(copy));
            swap(copy, zeRow, zeCol, zeRow - 1, zeCol);
        }
        if (zeRow < dimension() - 1) {
            swap(copy, zeRow, zeCol, zeRow + 1, zeCol);
            boards.add(new Board(copy));
            swap(copy, zeRow, zeCol, zeRow + 1, zeCol);
        }
        if (zeCol > 0) {
            swap(copy, zeRow, zeCol, zeRow, zeCol - 1);
            boards.add(new Board(copy));
            swap(copy, zeRow, zeCol, zeRow + 1, zeCol - 1);
        }
        if (zeCol < dimension() - 1) {
            swap(copy, zeRow, zeCol, zeRow + 1, zeCol);
            boards.add(new Board(copy));
        }

        return boards;
    }
    
    public Board twin() {  
        int[][] newTiles = Arrays.copyOf(tiles, tiles.length);
        int row = dimension() - 1;
        int col = dimension();
        int swapRow = ThreadLocalRandom.current().nextInt(row + 1);
        int swapCol = ThreadLocalRandom.current().nextInt(col + 1);
        int temp = newTiles[swapRow][swapCol];
        newTiles[swapRow][swapCol] = newTiles[row][col];
        newTiles[row][col] = temp;
        Board newBoard = new Board(newTiles);
        return newBoard;
   }

    @Override
    public String toString() {
        int digits = String.valueOf(dimension() * dimension() - 1).length(); // number of digits in the largest number
        // 2 * digits = spaces around them, dimension + 1 = the number of bars between
        int length = dimension() * (2 + digits) + dimension() + 1;
        String sep = "-".repeat(length);
        StringBuilder sb = new StringBuilder(length * dimension() * 2 + 1);
        for (int row = 0; row < dimension(); row++) {
            sb.append(sep).append('\n').append('|'); // start the first bar
            for (int col = 0; col < dimension(); col++) {
                sb.append(' ').append(pad(tiles[row][col], digits)).append(' ').append('|'); // the padded number surrounded by spaces
            }
            sb.append('\n');
        }
        return sb.append(sep).toString();
    }

    @Override
    public boolean equals(Object o) {
        return o == this
                || o instanceof Board board
                && Arrays.deepEquals(tiles, board.tiles);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(tiles);
    }
}
