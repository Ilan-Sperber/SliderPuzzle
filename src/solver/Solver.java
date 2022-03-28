package solver;

import pack.Board;
import utils.MinPQ;

import java.util.LinkedList;
import java.util.List;

public class Solver {
    private final List<Board> solution;
    private final boolean solvable;
    private int moves;

    private record SearchNode(Board board, int moves, SearchNode previous) implements Comparable<SearchNode> {
        @Override
        public int compareTo(SearchNode other) {
            return Integer.compare(board.manhattan(), other.board.manhattan());
        }
    }

    public Solver(Board initial) {
        solution = new LinkedList<>();
        moves = 0;

        MinPQ<SearchNode> pq = new MinPQ<>();
        pq.insert(new SearchNode(initial, moves, null));

        MinPQ<SearchNode> twinPQ = new MinPQ<>();
        twinPQ.insert(new SearchNode(initial.twin(), moves, null));

        SearchNode current, currentTwin;
        do {
            moves++;
            current = pq.delMin();
            for (Board neighbor : current.board.neighbors()) {
                pq.insert(new SearchNode(neighbor, moves, current));
            }

            currentTwin = twinPQ.delMin();
            for (Board neighbor : currentTwin.board.neighbors()) {
                twinPQ.insert(new SearchNode(neighbor, moves, currentTwin));
            }
        } while (!current.board.isGoal() && !currentTwin.board.isGoal());

        assert !(current.board.isGoal() && currentTwin.board.isGoal());

        if (current.board.isGoal()) {
            solvable = true;
            while (current != null) {
                solution.add(current.board);
                current = current.previous;
            }
        } else {
            solvable = false;
        }
    }

    public Boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return moves;
    }

    public List<Board> solution() {
        return solution;
    }
}
