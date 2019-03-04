package be.kuleuven.pylos.testbattle;

import be.kuleuven.pylos.battle.Battle;
import be.kuleuven.pylos.player.PylosPlayer;
import be.kuleuven.pylos.player.codes.PlayerFactoryCodes;
import be.kuleuven.pylos.player.student.PlayerFactoryStudent;

/**
 * Created by Jan on 20/03/2015.
 */
public class TestMain {

	public static void main(String[] args) {

		PylosPlayer codes = new PlayerFactoryCodes().getType("CODeS - Best Fit").create();
		PylosPlayer student = new PlayerFactoryStudent().getType("Student - Random").create();

		Battle.play(codes, student, 100);

		// Battle.play(new PylosPlayerMiniMax(), new StudentPlayerBestFit(), 100);
	}

}
