package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

public class HopOnMove implements Move {
    private PylosLocation bestPlace;
    private PylosSphere moveingSphere;
    private int bestScore = 100;

    @Override
    public void doMove(PylosGameIF game, PylosBoard board, PylosPlayer player) {

        PylosSphere myReserveSphere = board.getReserve(player);
        game.moveSphere(moveingSphere, bestPlace);

    }

    @Override
    public boolean isPossible(PylosGameIF game, PylosBoard board, PylosPlayer player) {
        PylosSphere[] mySpheres = board.getSpheres(player);
        PylosSquare[] squares = board.getAllSquares();
        boolean possible = false;
        int score = 100;
        PylosLocation pl;

        for (PylosSquare ps : squares) {

            if (ps.getInSquare() == 4 && !ps.getTopLocation().isUsed()) {

                for (PylosSphere psph : mySpheres) {
                    if (!psph.isReserve()) {
                        if (psph.canMoveTo(ps.getTopLocation())) {
                            score = getScore(game, board, ps.getTopLocation(), psph, player);
                            if (score < bestScore) {
                                bestScore = score;
                                bestPlace = ps.getTopLocation();
                                moveingSphere = psph;
                            }
                            possible = true;
                        }
                    }
                }




            }
        }

        return possible;
    }

    @Override
    public int getScore(PylosGameIF game, PylosBoard board, PylosPlayer player, PylosLocation pl) {
        return 0;
    }


    public int getScore(PylosGameIF game, PylosBoard board, PylosLocation pl, PylosSphere ps1, PylosPlayer player) {

        int score = 0;

        PylosLocation originalLoc = ps1.getLocation();

        board.move(ps1, pl);

        PylosSquare[] squares = board.getAllSquares();
        for (PylosSquare ps : squares) {
            if (ps.getInSquare(player) == 0 && ps.getInSquare(player.OTHER) > 0) {
                score++;
            }
        }

        board.moveDown(ps1, originalLoc);

        return score;

    }
}
