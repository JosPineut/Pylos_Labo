package be.kuleuven.pylos.battle;

import be.kuleuven.pylos.player.codes.PylosPlayerBestFit;
import be.kuleuven.pylos.player.codes.PylosPlayerMiniMax;
import be.kuleuven.pylos.player.codes.PylosPlayerRandomFit;
import be.kuleuven.pylos.player.student.StudentPlayerBestFit;
import be.kuleuven.pylos.player.student.StudentPlayerRandomFit;

/**
 * Created by Jan on 23/02/2015.
 */
public class BattleMain {

	public static void main(String[] args){
        Battle.play(new StudentPlayerBestFit(), new PylosPlayerBestFit(), 100);
	}

}
