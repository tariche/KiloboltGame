package kiloboltgame;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

public class Sounds implements Runnable {
	
	private Thread thread;
	private File fileLocation;
	private boolean loops;
	private Clip clip;
	private SourceDataLine line;
	private AudioInputStream din;
	
	public Sounds(File fileLocation, boolean loops) {
		super();
		thread = new Thread(this);
		this.fileLocation = fileLocation;
		this.loops = loops;
		thread.start();
	}

	@Override
	public void run() {
		try {
			//clip = AudioSystem.getClip();
			AudioInputStream in = AudioSystem.getAudioInputStream(fileLocation);
			AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(
	                AudioFormat.Encoding.PCM_SIGNED,
	                baseFormat.getSampleRate(),
	                16,
	                baseFormat.getChannels(),
	                baseFormat.getChannels() * 2,
	                baseFormat.getSampleRate(),
	                false);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			if (loops) {
				DataLine.Info info = new DataLine.Info(Clip.class, decodedFormat);
				clip = (Clip) AudioSystem.getLine(info);
				clip.open(din);
			} else {
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
				line = (SourceDataLine) AudioSystem.getLine(info);
				line.open(decodedFormat);
			}
			start();
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void start() {
		if (loops) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} else {
//			clip.start();
			line.start();
			byte[] data = new byte[4096];
			int byteRead = 0;
			try {
				while ((byteRead = din.read(data, 0, data.length)) != -1) {
					line.write(data, 0, data.length);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			line.drain();
			line.close();
			line.stop();
			line = null;
		}
	}
	
	public void stop() {
		clip.stop();
	}
	
}
