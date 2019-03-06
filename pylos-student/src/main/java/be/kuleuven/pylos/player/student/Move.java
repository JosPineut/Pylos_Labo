package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGameIF;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

public interface Move {

    void doMove(PylosGameIF game, PylosBoard board, PylosPlayer player);
    boolean isPossible(PylosGameIF game, PylosBoard board, PylosPlayer player);
    int getScore(PylosGameIF game, PylosBoard board, PylosPlayer player, PylosLocation pl);


}
