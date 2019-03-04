package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RegularMove implements Move {

    int[] bestMoveScore = new int[2];


    @Override
    public void doMove(PylosGameIF game, PylosBoard board, PylosPlayer player) {

        //System.out.println(board.getNumberOfSpheresOnBoard() );
        if (board.getNumberOfSpheresOnBoard() == 0) {
            openingMove(game, board, player);
        } else {
            regularMove(game, board, player);
        }
        System.out.println(scoreOpponent(game, board, player));
    }

    public void openingMove(PylosGameIF game, PylosBoard board, PylosPlayer player) {
        PylosSphere myReserveSphere = board.getReserve(player);
        PylosLocation standardLocation = board.getBoardLocation(0, 0, 0);
        game.moveSphere(myReserveSphere, standardLocation);

    }

    public void regularMove(PylosGameIF game, PylosBoard board, PylosPlayer player) {
        PylosLocation[] allLocations = board.getLocations();
        shuffleArray(allLocations);
        PylosSphere myReserveSphere = board.getReserve(player);
        int i = 0;


        while (i < allLocations.length) {
            if (allLocations[i].isUsable()) {
                game.moveSphere(myReserveSphere, allLocations[i]);
                i = allLocations.length;
            }
            i++;
        }
    }

    public int scoreOpponent(PylosGameIF game, PylosBoard board, PylosPlayer player) {
        int score = 0;
        PylosSquare[] squares = board.getAllSquares();
        for (PylosSquare square : squares) {
            if(square.getInSquare(player)==0 && square.getInSquare(player.OTHER)!=0){
                score+=1;
            }
        }
        return score;
    }

    @Override
    public boolean isPossible(PylosGameIF game, PylosBoard board, PylosPlayer player) {
        return false;
    }

    @Override
    public int getScore(PylosGameIF game, PylosBoard board, PylosPlayer players) {
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
