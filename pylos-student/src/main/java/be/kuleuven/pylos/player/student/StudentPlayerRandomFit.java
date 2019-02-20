package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGameIF;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.game.PylosSphere;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Ine on 5/05/2015.
 */
public class StudentPlayerRandomFit extends PylosPlayer{
    PylosSphere lastPlaced;

    @Override
    public void doMove(PylosGameIF game, PylosBoard board) {
		/* add a reserve sphere to a feasible random location */

        PylosLocation[] allLocations = board.getLocations();

        shuffleArray(allLocations);

        PylosSphere myReserveSphere = board.getReserve(this);


        int i=0;

        while(i<allLocations.length){

            if(allLocations[i].isUsable()){
                game.moveSphere(myReserveSphere,allLocations[i]);
                lastPlaced = myReserveSphere;
                i=allLocations.length;
            }
            i++;
        }

    }

    @Override
    public void doRemove(PylosGameIF game, PylosBoard board) {
		/* removeSphere a random sphere */
        game.removeSphere(lastPlaced);

    }

    @Override
    public void doRemoveOrPass(PylosGameIF game, PylosBoard board) {
		/* always pass */
        game.pass();

    }

    static void shuffleArray(PylosLocation[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);

            // Simple swap
            PylosLocation a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

}
