package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGameIF;

public interface Move {

    public void doMove(PylosGameIF game, PylosBoard board);
    public boolean isPossible();
    public int getScore();
    
}
