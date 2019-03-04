package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGameIF;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

public class HopOnMove implements Move {
    @Override
    public void doMove(PylosGameIF game, PylosBoard board, PylosPlayer player) {

    }

    @Override
    public boolean isPossible(PylosGameIF game, PylosBoard board, PylosPlayer player) {
        return false;
    }

    @Override
    public int getScore(PylosGameIF game, PylosBoard board, PylosPlayer player, PylosLocation pl) {
        return 0;
    }
}
