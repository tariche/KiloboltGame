package kiloboltgame;

import java.awt.Image;
import java.awt.Rectangle;


public class Tile {

	private int tileX, tileY, speedX, tileType;
	private Image tileImage;
	private Robot robot = StartingClass.getRobot();
	private Background bg1 = StartingClass.getBg1();
	private Rectangle r;
	
	
	public Tile(int x, int y, int type) {
		tileX = x*40;
		tileY = y*40;
		r = new Rectangle();
		
		tileType = type;
	
		
		/*if (type == 1) {
			tileImage = StartingClass.tileOcean;
		} else if (type == 2) {
			tileImage = StartingClass.tileDirt;
		}
		*/
		if (type == 5) {
			tileImage = StartingClass.tileDirt;
		} else if (type == 2) {
			tileImage = StartingClass.tilegrassBot;
		} else if (type == 8) {
			tileImage = StartingClass.tilegrassTop;
		} else if (type == 4) {
			tileImage = StartingClass.tilegrassLeft;
		} else if (type == 6) {
			tileImage = StartingClass.tilegrassRight;
		} else {
			type = 0;
		}

	}
	
	public void update() {
		/*if (tileType == 1) {
			if (bg1.getSpeedX() == 0) {
				speedX = -1;
			} else {
				speedX = -2;
			}
		} else {
			speedX = bg1.getSpeedX() * 5;
		}*/
		
		speedX = bg1.getSpeedX() * 5;
		tileX += speedX;
		r.setBounds(tileX, tileY, 40, 40);
		
		if (r.intersects(Robot.yellowRed) && tileType != 0) {
			checkVerticalCollision(Robot.rect, Robot.rect2);
			checkSideCollision(Robot.rect3, Robot.rect4, Robot.footLeft, Robot.footRight);
		}
	}

	private void checkSideCollision(Rectangle rect3, Rectangle rect4,
			Rectangle footLeft, Rectangle footRight) {
		if (tileType == 8 || tileType == 4 || tileType == 6) {
			//tileType != 5 && tileType != 2 && tileType != 0
			if (rect3.intersects(r)) {
				robot.setCenterX(tileX+102);
				robot.setSpeedX(0);
			} else if (footLeft.intersects(r)) {
				robot.setCenterX(tileX+91);
				robot.setSpeedX(0);
			}
			
			if (rect4.intersects(r)) {
				robot.setCenterX(tileX-62);
				robot.setSpeedX(0);
			} else if (footRight.intersects(r)) {
				robot.setCenterX(tileX-50);
				robot.setSpeedX(0);
			}
		}
		
	}

	private void checkVerticalCollision(Rectangle rect, Rectangle rect2) {
		if (rect.intersects(r)) {
			
		}
		if (rect2.intersects(r) && tileType == 8) {
			robot.setJumped(false);
			robot.setSpeedY(0);
			robot.setCenterY(tileY-63);
		}
		
	}

	public int getTileX() {
		return tileX;
	}

	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	public int getTileY() {
		return tileY;
	}

	public void setTileY(int tileY) {
		this.tileY = tileY;
	}

	public Image getTileImage() {
		return tileImage;
	}

	public void setTileImage(Image tileImage) {
		this.tileImage = tileImage;
	}
	
	
}
