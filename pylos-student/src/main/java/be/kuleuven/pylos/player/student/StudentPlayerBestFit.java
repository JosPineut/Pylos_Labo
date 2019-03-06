package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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


    public class HopOnMove implements Move {
        private PylosLocation bestPlace;
        private PylosSphere moveingSphere;
        private int bestScore = 100;

        @Override
        public void doMove(PylosGameIF game, PylosBoard board, PylosPlayer player) {
            game.moveSphere(moveingSphere, bestPlace);
        }

        @Override
        public boolean isPossible(PylosGameIF game, PylosBoard board, PylosPlayer player) {
            PylosSphere[] mySpheres = board.getSpheres(player);
            PylosSquare[] squares = board.getAllSquares();
            boolean possible = false;
            int score;

            for (PylosSquare ps : squares) {
                if (ps.getInSquare() == 4 && !ps.getTopLocation().isUsed()) {
                    for (PylosSphere psph : mySpheres) {
                        if (!psph.isReserve()) {
                            if (psph.canMoveTo(ps.getTopLocation())) {
                                score = getScore( board, ps.getTopLocation(), psph, player);
                                if (score < bestScore) {
                                    bestScore = score;
                                    bestPlace = ps.getTopLocation();
                                    moveingSphere = psph;
                                }
                                possible = true;
                            }
                        }
                    }
                }
            }
            return possible;
        }

        public int getScore( PylosBoard board, PylosLocation pl, PylosSphere ps1, PylosPlayer player) {
            int score = 0;
            PylosLocation originalLoc = ps1.getLocation();
            board.move(ps1, pl);
            PylosSquare[] squares = board.getAllSquares();
            for (PylosSquare ps : squares) {
                if (ps.getInSquare(player) == 0 && ps.getInSquare(player.OTHER) > 0) {
                    score++;
                }
            }
            board.moveDown(ps1, originalLoc);
            return score;
        }
    }

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

            PylosSquare[] squares = board.getAllSquares();
            boolean possible=false;
            int score;

            for(PylosSquare ps:squares){
                if(ps.getInSquare(player) == 3){
                    PylosLocation[] pls =ps.getLocations();
                    for(PylosLocation pl:pls){
                        if(!pl.isUsed() && pl.isUsable()){
                            possible= true;
                            score=getScore(board,player,pl);
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

        public int getScore(PylosBoard board, PylosPlayer player,PylosLocation pl) {

            int score=0;

            PylosSphere myReserveSphere = board.getReserve(player);
            board.add(myReserveSphere, pl);

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

    public class PreventSquareMove implements Move {
        private PylosLocation bestPlace;
        private int[] bestScore = {100, 0, 0};
        private int totScore = 0;

        @Override
        public void doMove(PylosGameIF game, PylosBoard board, PylosPlayer player) {

            PylosSphere myReserveSphere = board.getReserve(player);
            game.moveSphere(myReserveSphere, bestPlace);
        }

        @Override
        public boolean isPossible(PylosGameIF game, PylosBoard board, PylosPlayer player) {

            PylosSquare[] squares = board.getAllSquares();
            boolean possible = false;
            int score;

            for (PylosSquare ps : squares) {
                if (ps.getInSquare(player.OTHER) == 3) {
                    PylosLocation[] pls = ps.getLocations();
                    for (PylosLocation pl : pls) {
                        if (!pl.isUsed() && pl.isUsable()) {
                            possible = true;
                            score = getScore( board, player, pl);
                            if (score > totScore) {
                                totScore = score;
                                bestPlace = pl;
                            }
                        }
                    }
                }
            }

            return possible;
        }

        public int getScore( PylosBoard board, PylosPlayer player, PylosLocation pl) {

            PylosSphere myReserveSphere = board.getReserve(player);
            board.add(myReserveSphere, pl);

            PylosSquare[] squares = board.getAllSquares();

            int otherSquares = 0;
            int ourSquares = 0;
            int maxSpheres = 0;

            for (PylosSquare ps : squares) {
                if (ps.getInSquare(player) > 0 && ps.getInSquare(player.OTHER) == 0) {
                    otherSquares++;
                } else if (ps.getInSquare(player.OTHER) > 0 && ps.getInSquare(player) == 0) {
                    ourSquares++;
                }
                for (PylosLocation ploc : ps.getLocations()) {
                    if (ploc == pl) {
                        if (ps.getInSquare(player) > maxSpheres) {
                            maxSpheres = ps.getInSquare(player);
                        }
                    }
                }
            }

            board.remove(myReserveSphere);

            if (otherSquares < bestScore[0]) {
                bestScore[0] = otherSquares;
                bestScore[1] = ourSquares;
                bestScore[2] = maxSpheres;

                return (totScore + 1);
            } else if (otherSquares == bestScore[0]) {
                if (ourSquares > bestScore[1]) {
                    return (totScore + 1);
                } else if (maxSpheres > bestScore[2]) {
                    return (totScore + 1);
                }
            }

            return 0;
        }
    }

    public class RegularMove implements Move {

        int[] bestMoveScore = new int[4];


        @Override
        public void doMove(PylosGameIF game, PylosBoard board, PylosPlayer player) {
            bestMoveScore[0] = Integer.MAX_VALUE;
            bestMoveScore[1] = Integer.MIN_VALUE;
            bestMoveScore[2] = Integer.MIN_VALUE;
            bestMoveScore[3] = Integer.MIN_VALUE;

            if (board.getNumberOfSpheresOnBoard() == 0) {
                openingMove(game, board, player);
            } else {
                regularMove(game, board, player);
            }

        }

        public void openingMove(PylosGameIF game, PylosBoard board, PylosPlayer player) {
            PylosSphere myReserveSphere = board.getReserve(player);
            PylosLocation standardLocation = board.getBoardLocation(0, 0, 0);
            game.moveSphere(myReserveSphere, standardLocation);

        }

        public void regularMove(PylosGameIF game, PylosBoard board, PylosPlayer player) {
            PylosLocation bestLocation = null;
            PylosLocation[] allLocations = board.getLocations();
            shuffleArray(allLocations);
            PylosSphere myReserveSphere = board.getReserve(player);
            int i = 0;

            while (i < allLocations.length) {
                if (allLocations[i].isUsable()) {

                    board.add(myReserveSphere, allLocations[i]);
                    if (score(game, board, player, myReserveSphere)) {
                        bestLocation = allLocations[i];
                    }
                    board.remove(myReserveSphere);
                }
                i++;
            }

            game.moveSphere(myReserveSphere, bestLocation);
        }

        public boolean score(PylosGameIF game, PylosBoard board, PylosPlayer player, PylosSphere reserveSphere) {
            int score1 = 0;
            int score2 = 0;
            int score3 = Integer.MIN_VALUE;
            int score4 = Integer.MIN_VALUE;
            PylosSquare[] squares = board.getAllSquares();
            for (PylosSquare square : squares) {
                if (square.getInSquare(player) == 0 && square.getInSquare(player.OTHER) != 0) {
                    score1 += 1;

                } else if (square.getInSquare(player) != 0 && square.getInSquare(player.OTHER) == 0) {
                    score2 += 1;
                }
                if (containsLocation(square, reserveSphere)) {
                    if (score3 < square.getInSquare(player.OTHER)) {
                        score3 = square.getInSquare(player.OTHER);
                    }
                    if (score4 < square.getInSquare(player)) {
                        score4 = square.getInSquare(player);
                    }
                }

            }

            if (score1 < bestMoveScore[0]) {
                bestMoveScore[0] = score1;
                return true;
            } else if (score1 == bestMoveScore[0] && score2 > bestMoveScore[1]) {
                bestMoveScore[1] = score2;
                return true;
            } else if (score1 == bestMoveScore[0] && score2 == bestMoveScore[1] && score3 > bestMoveScore[2]) {
                bestMoveScore[2] = score3;
                return true;
            } else if (score1 == bestMoveScore[0] && score2 == bestMoveScore[1] && score3 == bestMoveScore[2] && score4 > bestMoveScore[3]) {
                bestMoveScore[3] = score4;
                return true;
            } else if (score1 == bestMoveScore[0] && score2 == bestMoveScore[1] && score3 == bestMoveScore[2] && score4 == bestMoveScore[3]) {
                Random rand = new Random();
                return rand.nextInt(4) == 2;
            }
            return false;
        }

        public boolean containsLocation(PylosSquare square, PylosSphere sphere) {
            for (PylosLocation loc : square.getLocations()) {
                if (loc == sphere.getLocation()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean isPossible(PylosGameIF game, PylosBoard board, PylosPlayer player) {
            return true;
        }




    }

    public interface Move{
        void doMove(PylosGameIF game, PylosBoard board, PylosPlayer player);
        boolean isPossible(PylosGameIF game, PylosBoard board, PylosPlayer player);
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
