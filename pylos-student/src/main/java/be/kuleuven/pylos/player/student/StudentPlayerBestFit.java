package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGameIF;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.game.PylosSquare;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ine on 25/02/2015.
 */
public class StudentPlayerBestFit extends PylosPlayer{



    @Override
    public void doMove(PylosGameIF game, PylosBoard board) {
        PreventSquareMove p = new PreventSquareMove();
        MakeSquareMove m = new MakeSquareMove();
        HopOnMove h = new HopOnMove();
        RegularMove r = new RegularMove();

        if (p.isPossible(game, board, this)) {
            p.doMove(game, board, this);
        } else if (m.isPossible(game, board, this)) {
            m.doMove(game, board, this);
        } else if (h.isPossible(game, board, this)) {
            h.doMove(game, board, this);
        } else {
            r.doMove(game, board, this);
        }
    }

    @Override
    public void doRemove(PylosGameIF game, PylosBoard board) {
        for (PylosSquare square : board.getAllSquares()) {
            if (square.isSquare(this)) {
                PylosLocation[] locations = square.getLocations();
                Random rand = new Random();
                int i = 0;
                int index;
                while (i < 4) {
                    index = rand.nextInt(4-i);
                    if(locations[index].getSphere().canRemove()){
                        game.removeSphere(locations[index].getSphere());
                        i = 4;
                    } else {
                        PylosLocation temp = locations[index];
                        locations[index] = locations[3-i];
                        locations[3 - i] = temp;
                    }
                    i++;
                }

            }
        }
    }

    @Override
    public void doRemoveOrPass(PylosGameIF game, PylosBoard board) {
        game.pass();
    }
}
