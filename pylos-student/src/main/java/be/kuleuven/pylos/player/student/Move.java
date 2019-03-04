package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGameIF;
import be.kuleuven.pylos.player.PylosPlayer;

public interface Move {

    public void doMove(PylosGameIF game, PylosBoard board, PylosPlayer player);
    public boolean isPossible(PylosGameIF game, PylosBoard board, PylosPlayer player);
    public int getScore(PylosGameIF game, PylosBoard board, PylosPlayer players);

}
