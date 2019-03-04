package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGameIF;

public class MakeSquare implements Move{

    @Override
    public void doMove(PylosGameIF game, PylosBoard board) {

    }

    @Override
    public boolean isPossible() {



        return false;
    }

    @Override
    public int getScore() {
        return 0;
    }
}
