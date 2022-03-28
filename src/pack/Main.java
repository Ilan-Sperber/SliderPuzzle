package pack;

import solver.Solver;

public class Main {
    public static void main(String[] args) {
        Board b = new Board(3);
        Solver s = new Solver(b);

        System.out.println(s.moves());
        System.out.println(s.isSolvable());
        s.solution().forEach(System.out::println);
    }
}
