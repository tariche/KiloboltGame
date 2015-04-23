package kiloboltgame;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
//import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;



/*import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;*/

import kiloboltgame.framework.Animation;

public class StartingClass extends Applet implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	/**
	 * 
	 */
	
	enum GameState{
		Running,Dead
	}	
	GameState state = GameState.Running;
	
	private static final long serialVersionUID = -7273007167240426907L;
	public static Robot robot;
	private Image image, character, character2, character3, background, currentSprite, characterDown, characterJumped;
	private Image heliboy, heliboy2, heliboy3, heliboy4, heliboy5;
	public static Image tilegrassTop, tilegrassBot, tilegrassLeft, tilegrassRight, tileDirt;
	private URL base;
	private Graphics second;
	private static Background bg1,bg2;
	public static Heliboy hb, hb2;
//	private File soundLaser;
	
	public static int score = 0;
	private Font font = new Font(null, Font.BOLD, 30);
	private String dead = "Dead";
	private String restart = "Ponovi Igru";
	private Rectangle2D rrestart = new Rectangle2D.Double();
	private Rectangle2D rdead = new Rectangle2D.Double();
	
	
	private Animation anim, hanim;
	private ArrayList<Tile> tileArray = new ArrayList<>();
	
	@Override
	public void init() {
		setSize(800, 480);
		setBackground(Color.BLACK);
		setFocusable(true);
		Frame frame = (Frame) this.getParent().getParent();
		frame.setTitle("Q-Bot Alfa");
		addKeyListener(this);
		try {
			base = getDocumentBase();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		// Image setup
		character = getImage(base, "data/character.png");
		character2 = getImage(base, "data/character2.png");
		character3 = getImage(base, "data/character3.png");
		
		characterDown = getImage(base, "data/down.png");
		characterJumped = getImage(base, "data/jumped.png");
		
		heliboy = getImage(base, "data/heliboy.png");
		heliboy2 = getImage(base, "data/heliboy2.png");
		heliboy3 = getImage(base, "data/heliboy3.png");
		heliboy4 = getImage(base, "data/heliboy4.png");
		heliboy5 = getImage(base, "data/heliboy5.png");
		
		//tileOcean = getImage(base, "data/tileocean.png");
		tilegrassTop = getImage(base, "data/tilegrasstop.png");
		tilegrassBot = getImage(base, "data/tilegrassbot.png");
		tilegrassLeft = getImage(base, "data/tilegrassleft.png");
		tilegrassRight = getImage(base, "data/tilegrassright.png");
		tileDirt = getImage(base, "data/tiledirt.png");
		
		background = getImage(base, "data/background.png");
		
		anim = new Animation();
		anim.addFrame(character, 1250);
		anim.addFrame(character2, 50);
		anim.addFrame(character3, 50);
		anim.addFrame(character2, 50);
		
		hanim = new Animation();
		hanim.addFrame(heliboy, 100);
		hanim.addFrame(heliboy2, 100);
		hanim.addFrame(heliboy3, 100);
		hanim.addFrame(heliboy4, 100);
		hanim.addFrame(heliboy5, 100);
		hanim.addFrame(heliboy4, 100);
		hanim.addFrame(heliboy3, 100);
		hanim.addFrame(heliboy2, 100);
		
		currentSprite = anim.getImage();
		addMouseListener(this);
		addMouseMotionListener(this);
		
		// Sounds
//		soundLaser = new File("data/laserfire02.ogg");
	}

	@Override
	public void start() {
		bg1 = new Background(0, 0);
		bg2 = new Background(2160, 0);
		robot = new Robot();
		
		
		/*for (int i = 0; i < 200; i++) {
			for (int j = 0; j < 12; j++) {
				if (j == 10) {
					tileArray.add(new Tile(i, j, 1));
				}
				if (j == 11) {
					tileArray.add(new Tile(i, j, 2));
				}
			}
		}*/
		
		try {
			loadMap("data/map1.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		hb = new Heliboy(340, 360);
		hb2 = new Heliboy(700, 360);

		Thread thread = new Thread(this);
		thread.start();
	}

	private void loadMap(String filename) throws IOException {

		int width = 0;
		int hight = 0;
		ArrayList<String> lines = new ArrayList<>();
		BufferedReader read = new BufferedReader(new FileReader(filename));
		
		while (true) {
			String line = read.readLine();
			if (line == null) {
				read.close();
				break;
			}
			
			if (!line.startsWith("!")) {
				lines.add(line);
				width = Math.max(width, line.length());
			}
		}
		hight = lines.size();
		
		for (int j = 0; j < hight; j++) {
			String line = lines.get(j);
			for (int i = 0; i < width; i++) {
				if (i < line.length()) {
					char c = line.charAt(i);
					tileArray.add(new Tile(i, j, Character.getNumericValue(c)));
				}
			}
		}
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}
	
	@Override
	public void run() {
		if (state == GameState.Running) {
			while(true){
				robot.update();
				if (robot.isJumped()) {
					currentSprite = characterJumped;
				} else if (robot.isJumped() == false && robot.isDucked() == false) {
					currentSprite = anim.getImage();
				}
				
				ArrayList<Projectile> projectiles = robot.getProjectiles();
				for (int i = 0; i < projectiles.size(); i++) {
					Projectile p = projectiles.get(i);
					if (p.isVisible() == true) {
						p.update();
					} else {
						projectiles.remove(i);
					}
				}
				hb.update();
				hb2.update();
				bg1.update();
				bg2.update();
				for (int i = 0; i < tileArray.size(); i++) {
					tileArray.get(i).update();
				}
				
				animate();
				repaint();
				
				try {
					Thread.sleep(17);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (robot.getCenterY() > 500) {
					state = GameState.Dead;
					
				}
			}
		}
	}

	private void animate() {
		anim.update(10);
		hanim.update(50);
	}

	@Override
	public void update(Graphics g) {
		if (image == null) {
			image = createImage(this.getWidth(), this.getHeight());
			second = image.getGraphics();
		}
		
		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);
		
		g.drawImage(image, 0, 0, this);
	}
	
	@Override
	public void paint(Graphics g) {
		/*if (state == GameState.Running) {
			g.drawImage(background, bg1.getBgX(), bg1.getBgY(), this);
			g.drawImage(background, bg2.getBgX(), bg2.getBgY(), this);
			paintTiles(g);
			
			ArrayList<Projectile> projectile = robot.getProjectiles();
			for (int i = 0; i < projectile.size(); i++) {
				Projectile p = projectile.get(i);
				g.setColor(Color.YELLOW);
				g.fillRect(p.getX(), p.getY(), 10, 5);
			}
			g.drawImage(currentSprite, robot.getCenterX() - 61, robot.getCenterY() - 63, this);
			g.drawImage(hanim.getImage(), hb.getCenterX() - 48, hb.getCenterY() - 48, this);
			g.drawImage(hanim.getImage(), hb2.getCenterX() - 48, hb2.getCenterY() - 48, this);
			//g.drawRect((int)robot.rect.getX(), (int)robot.rect.getY(), (int)robot.rect.getWidth(), (int)robot.rect.getHeight());
			//g.drawRect((int)robot.rect2.getX(), (int)robot.rect2.getY(), (int)robot.rect2.getWidth(), (int)robot.rect2.getHeight());
			//g.drawRect((int)robot.rect3.getX(), (int)robot.rect3.getY(), (int)robot.rect3.getWidth(), (int)robot.rect3.getHeight());
			//g.drawRect((int)robot.rect4.getX(), (int)robot.rect4.getY(), (int)robot.rect4.getWidth(), (int)robot.rect4.getHeight());
			//g.drawRect((int)robot.footLeft.getX(), (int)robot.footLeft.getY(), (int)robot.footLeft.getWidth(), (int)robot.footLeft.getHeight());
			//g.drawRect((int)robot.footRight.getX(), (int)robot.footRight.getY(), (int)robot.footRight.getWidth(), (int)robot.footRight.getHeight());
			g.setFont(font);
			g.setColor(Color.WHITE);
			g.drawString(Integer.toString(score), 740, 30);
		} else if (state == GameState.Dead) {
			//g.setColor(Color.BLACK);
			//g.fillRect(0, 0, 800, 480);
			//g.setColor( Color.WHITE);
			//g.drawString("Dead", 360, 240);
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.BLACK);
			g2.fillRect(0, 0, 800, 480);
			FontRenderContext frc = g2.getFontRenderContext();
			Rectangle2D rdead = font.getStringBounds(dead, frc);
			g2.setColor(Color.RED);
			g2.drawString(dead, 400 - (int)(rdead.getWidth()/2), 240 - (int)(rdead.getHeight()) + 10);
			Rectangle2D rrestart = font.getStringBounds(restart, frc);
			g2.setColor(Color.GREEN);
			g2.drawString(restart, 400 - (int)(rrestart.getWidth()/2), 240 + 10);
		}*/
		
		g.drawImage(background, bg1.getBgX(), bg1.getBgY(), this);
		g.drawImage(background, bg2.getBgX(), bg2.getBgY(), this);
		paintTiles(g);
		
		ArrayList<Projectile> projectile = robot.getProjectiles();
		for (int i = 0; i < projectile.size(); i++) {
			Projectile p = projectile.get(i);
			g.setColor(Color.YELLOW);
			g.fillRect(p.getX(), p.getY(), 10, 5);
		}
		g.drawImage(currentSprite, robot.getCenterX() - 61, robot.getCenterY() - 63, this);
		g.drawImage(hanim.getImage(), hb.getCenterX() - 48, hb.getCenterY() - 48, this);
		g.drawImage(hanim.getImage(), hb2.getCenterX() - 48, hb2.getCenterY() - 48, this);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString(Integer.toString(score), 740, 30);
		
		if (state == GameState.Dead) {
			Graphics2D g2 = (Graphics2D) g;
			FontRenderContext frc = g2.getFontRenderContext();
			rdead = font.getStringBounds(dead, frc);
			g2.setColor(Color.RED);
			g2.drawString(dead, 400 - (int)(rdead.getWidth()/2), 240 - (int)(rdead.getHeight()) - 20 - (int)rdead.getY());
			//g2.drawRect(400 - (int)(rdead.getWidth()/2), 240 - (int)(rdead.getHeight()) - 20, (int)rdead.getWidth(), (int)(rdead.getHeight()));
			rrestart = font.getStringBounds(restart, frc);
			g2.setColor(Color.GREEN);
			g2.drawString(restart, 400 - (int)(rrestart.getWidth()/2), 240 + 20 - (int)rrestart.getY());
			rrestart.setFrame(400 - (rrestart.getWidth()/2), 260, rrestart.getWidth(), (rrestart.getHeight()));
			//g2.drawRect(400 - (int)(rrestart.getWidth()/2), 260, (int)rrestart.getWidth(), (int)(rrestart.getHeight()));
		}
	}
	

	private void paintTiles(Graphics g) {
		for (int i = 0; i < tileArray.size(); i++) {
			Tile t = tileArray.get(i);
			g.drawImage(t.getTileImage(), t.getTileX(), t.getTileY(), this);
		}
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_UP:
			robot.jump();;
			break;
		
		case KeyEvent.VK_DOWN:
			currentSprite = characterDown;
			if (robot.isJumped() == false) {
				robot.setDucked(true);
				robot.setSpeedX(0);
			}
			break;
			
		case KeyEvent.VK_LEFT:
			robot.moveLeft();
			robot.setMovingLeft(true);
			break;
			
		case KeyEvent.VK_RIGHT:
			robot.moveRight();
			robot.setMovingRight(true);
			break;
			
		case KeyEvent.VK_SPACE:
			if (robot.isJumped() == false && robot.isDucked() == false) {
				if (robot.isReadyToFire()) {
					robot.shoot();
//					playSound(soundLaser, false);
					robot.setReadyToFire(false);
				}
			}
			break;
			
		case KeyEvent.VK_CONTROL:
			
			break;
			
		default:
			break;
		}
	}

/*	private synchronized void playSound(File soundFile, boolean loops) {
		// TODO Auto-generated method stub
		
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream in = AudioSystem.getAudioInputStream(soundFile);
			AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(
	                AudioFormat.Encoding.PCM_SIGNED,
	                baseFormat.getSampleRate(),
	                16,
	                baseFormat.getChannels(),
	                baseFormat.getChannels() * 2,
	                baseFormat.getSampleRate(),
	                false);
			AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, in);
			clip.open(din);
			if (loops) {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} else {
				clip.start();
			}
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/

	@Override
	public void keyReleased(KeyEvent arg0) {
		
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_UP:
			break;
		
		case KeyEvent.VK_DOWN:
			currentSprite = character;
			robot.setDucked(false);
			break;
			
		case KeyEvent.VK_LEFT:
			robot.stopLeft();
			break;
			
		case KeyEvent.VK_RIGHT:
			robot.stopRight();
			break;
			
		case KeyEvent.VK_SPACE:
			robot.setReadyToFire(true);
			break;
			
		case KeyEvent.VK_CONTROL:
			break;
						
		default:
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static Background getBg1() {
        return bg1;
    }

    public static Background getBg2() {
        return bg2;
    }
    
    public static Robot getRobot() {
		return robot;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point2D p = new Point2D.Double(e.getX(), e.getY());
		if (rrestart.contains(p)) {
			//System.out.println("Pokreni igru ponovo");
			reloadGame();
			state = GameState.Running;
		}
	}

	
	private void reloadGame() {
		
		bg1.setBgX(0);
		bg2.setBgX(2160);
		
		robot.setSpeedX(0);
		robot.setSpeedY(0);
		robot.setJumped(false);
		robot.setCenterX(100);
		robot.setCenterY(377);

		hb.setCenterX(340);
		hb.setCenterY(360);
		hb.s.start();
		hb.health = 5;
		hb2.setCenterX(700);
		hb2.setCenterY(360);
		hb2.s.start();
		hb2.health = 5;
//		
		tileArray.clear();
		score = 0;
		
		try {
			loadMap("data/map1.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point2D p = new Point2D.Double(e.getX(), e.getY());
		if (rrestart.contains(p)) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		
	}
}
