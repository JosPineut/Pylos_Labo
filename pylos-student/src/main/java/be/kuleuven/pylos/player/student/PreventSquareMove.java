package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

public class PreventSquareMove implements Move {
    private PylosLocation bestPlace;
    private int[] bestScore = {100, 0, 0};
    private int totScore = 0;

    @Override
    public void doMove(PylosGameIF game, PylosBoard board, PylosPlayer player) {

    }

    @Override
    public boolean isPossible(PylosGameIF game, PylosBoard board, PylosPlayer player) {

        PylosSquare[] squares = board.getAllSquares();
        boolean possible = false;
        int score;

        for (PylosSquare ps : squares) {
            if (ps.getInSquare(player.OTHER) == 3) {
                PylosLocation[] pls = ps.getLocations();
                for (PylosLocation pl : pls) {
                    if (!pl.isUsed() && pl.isUsable()) {
                        possible = true;
                        score = getScore(game, board, player, pl);
                        if (score > totScore) {
                            totScore = score;
                            bestPlace = pl;
                        }
                    }
                }
            }
        }

        return possible;
    }

    @Override
    public int getScore(PylosGameIF game, PylosBoard board, PylosPlayer player, PylosLocation pl) {

        PylosSphere myReserveSphere = board.getReserve(player);
        board.move(myReserveSphere, pl);

        PylosSquare[] squares = board.getAllSquares();

        int otherSquares = 0;
        int ourSquares = 0;
        int maxSpheres = 0;

        for (PylosSquare ps : squares) {
            if (ps.getInSquare(player) > 0 && ps.getInSquare(player.OTHER) == 0) {
                otherSquares++;
            } else if (ps.getInSquare(player.OTHER) > 0 && ps.getInSquare(player) == 0) {
                ourSquares++;
            }
            for (PylosLocation ploc : ps.getLocations()) {
                if (ploc == pl) {
                    if (ps.getInSquare(player) > maxSpheres) {
                        maxSpheres = ps.getInSquare(player);
                    }
                }
            }
        }

        if (otherSquares < bestScore[0]) {
            bestScore[0] = otherSquares;
            bestScore[1] = ourSquares;
            bestScore[2] = maxSpheres;

            return totScore++;
        } else if (otherSquares == bestScore[0]) {
            if (ourSquares > bestScore[1]) {
                return totScore++;
            } else if (maxSpheres > bestScore[2]) {
                return totScore++;
            }
        }


        board.remove(myReserveSphere);
        return 0;
    }
}
