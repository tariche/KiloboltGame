package kiloboltgame;

import java.io.File;

public class Heliboy extends Enemy {

	public Heliboy(int centerX, int centerY) {
		setCenterX(centerX);
		setCenterY(centerY);
		s = new Sounds(new File("data/Military_Helicopter.ogg"), true);
	}
	

}
