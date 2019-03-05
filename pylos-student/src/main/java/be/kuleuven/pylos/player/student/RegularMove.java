package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RegularMove implements Move {

    int[] bestMoveScore = new int[4];


    @Override
    public void doMove(PylosGameIF game, PylosBoard board, PylosPlayer player) {
        bestMoveScore[0] = Integer.MAX_VALUE;
        bestMoveScore[1] = Integer.MIN_VALUE;
        bestMoveScore[2] = Integer.MIN_VALUE;
        bestMoveScore[3] = Integer.MIN_VALUE;

        //System.out.println(board.getNumberOfSpheresOnBoard() );
        if (board.getNumberOfSpheresOnBoard() == 0) {
            openingMove(game, board, player);
        } else {
            regularMove(game, board, player);
        }

    }

    public void openingMove(PylosGameIF game, PylosBoard board, PylosPlayer player) {
        PylosSphere myReserveSphere = board.getReserve(player);
        PylosLocation standardLocation = board.getBoardLocation(0, 0, 0);
        game.moveSphere(myReserveSphere, standardLocation);

    }

    public void regularMove(PylosGameIF game, PylosBoard board, PylosPlayer player) {
        PylosLocation bestLocation = null;
        PylosLocation[] allLocations = board.getLocations();
        shuffleArray(allLocations);
        PylosSphere myReserveSphere = board.getReserve(player);
        int i = 0;

        //System.out.println("##########################################################################");
        while (i < allLocations.length) {
            if (allLocations[i].isUsable()) {

                board.add(myReserveSphere, allLocations[i]);
                if (score(game, board, player,myReserveSphere)) {
                    bestLocation = allLocations[i];
                }
                board.remove(myReserveSphere);
            }
            i++;
        }

        game.moveSphere(myReserveSphere, bestLocation);
    }

    public boolean score(PylosGameIF game, PylosBoard board, PylosPlayer player, PylosSphere reserveSphere) {
        int score1 = 0;
        int score2 = 0;
        int score3 = Integer.MIN_VALUE;
        int score4 = Integer.MIN_VALUE;
        PylosSquare[] squares = board.getAllSquares();
        for (PylosSquare square : squares) {
            if (square.getInSquare(player) == 0 && square.getInSquare(player.OTHER) != 0) {
                score1 += 1;

            } else if (square.getInSquare(player) != 0 && square.getInSquare(player.OTHER) == 0) {
                score2 += 1;
            }
            if (containsLocation(square, reserveSphere)) {
                if (score3 < square.getInSquare(player.OTHER)) {
                    score3 = square.getInSquare(player.OTHER);
                }
                if (score4 < square.getInSquare(player)) {
                    score4 = square.getInSquare(player);
                }
            }

        }

        //System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        //System.out.println("1: "+score1+" 2: "+score2+" 3: "+score3+" 4: "+score4);
        if (score1 < bestMoveScore[0]) {
            bestMoveScore[0] = score1;
            return true;
        } else if (score1 == bestMoveScore[0] && score2 > bestMoveScore[1]) {
            bestMoveScore[1] = score2;
            return true;
        } else if (score1 == bestMoveScore[0] && score2 == bestMoveScore[1] && score3 > bestMoveScore[2]) {
            bestMoveScore[2] = score3;
            return true;
        } else if(score1 == bestMoveScore[0] && score2 == bestMoveScore[1] && score3 == bestMoveScore[2] && score4 > bestMoveScore [3]) {
            bestMoveScore[3] = score4;
            return true;
        } else if (score1 == bestMoveScore[0] && score2 == bestMoveScore[1] && score3 == bestMoveScore[2] && score4 == bestMoveScore[3]) {
            Random rand = new Random();
            return rand.nextInt(4) == 2;
        }
        return false;
    }

    public boolean containsLocation(PylosSquare square, PylosSphere sphere) {
        for (PylosLocation loc : square.getLocations()) {
            if (loc == sphere.getLocation()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isPossible(PylosGameIF game, PylosBoard board, PylosPlayer player) {
        return true;
    }

    @Override
    public int getScore(PylosGameIF game, PylosBoard board, PylosPlayer player, PylosLocation pl) {
        return 0;
    }

    static void shuffleArray(PylosLocation[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);

            // Simple swap
            PylosLocation a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
