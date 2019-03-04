package be.kuleuven.pylos.player.student;

public interface Move {

    public void doMove();
    public boolean isPossible();
    public int getScore();

}
