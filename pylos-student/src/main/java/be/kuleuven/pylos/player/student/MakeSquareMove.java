package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

public class MakeSquareMove implements Move {
    private PylosLocation bestPlace;
    private int bestScore=100;

    @Override
    public void doMove(PylosGameIF game, PylosBoard board, PylosPlayer player) {

        PylosSphere myReserveSphere = board.getReserve(player);
        game.moveSphere(myReserveSphere,bestPlace);
    }

    @Override
    public boolean isPossible(PylosGameIF game, PylosBoard board, PylosPlayer player) {

        PylosSphere[] mySpheres = board.getSpheres(player);
        PylosSquare[] squares = board.getAllSquares();
        boolean possible=false;
        int score=100;

        for(PylosSquare ps:squares){
            if(ps.getInSquare(player) == 3){
                PylosLocation[] pls =ps.getLocations();
                for(PylosLocation pl:pls){
                    if(!pl.isUsed() && pl.isUsable()){
                        possible= true;
                        score=getScore(game,board,player,pl);
                        if(score < bestScore){
                            bestScore= score;
                            bestPlace=pl;
                        }
                    }
                }
            }
        }

        return possible;
    }

    @Override
    public int getScore(PylosGameIF game, PylosBoard board, PylosPlayer player,PylosLocation pl) {

        int score=0;

        PylosSphere myReserveSphere = board.getReserve(player);
        board.move(myReserveSphere, pl);

        PylosSquare[] squares = board.getAllSquares();
        for(PylosSquare ps:squares){
            if(ps.getInSquare(player)==0 && ps.getInSquare(player.OTHER) > 0){
                score++;
            }
        }

        board.remove(myReserveSphere);
        return score;
    }
}
