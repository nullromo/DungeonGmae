import javax.sound.sampled.*;
import java.applet.*;

public class Sound {
	public static final Sound keyClick = new Sound("keytype");
	public static final Sound keyReturn = new Sound("keyreturn");
	public static final Sound keyBackSpace = new Sound("keybackspace");
	public static final Sound playerHurt = new Sound("playerhurt");
	//public static final Sound backgroundMusic = new Sound("backgroundmusic");
	private String name;
	public static boolean mute;
	
	private AudioClip clip;

	private Sound(String name) {
		this.name = name;
		try {
			clip = Applet.newAudioClip(Sound.class.getResource("sounds/" + name.toLowerCase() + ".wav"));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void play() {
		try {
			new Thread() {
				public void run() {
					if(name.equals("backgroundmusic"))
						((Clip)getClip()).loop(Clip.LOOP_CONTINUOUSLY);
					clip.play();
				}
			}.start();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	public AudioClip getClip(){return clip;}
}
/*
public class Sound
{
	public static final Sound keyClick = new Sound("keytype");
	public static final Sound keyReturn = new Sound("keyreturn");
	public static final Sound keyBackSpace = new Sound("keybackspace");
	public static final Sound playerHurt = new Sound("playerhurt");
	//public static final Sound backgroundMusic = new Sound("backgroundmusic");
	
	private AudioInputStream inputStream;
	private Clip clip;
	private String name;
	
	public Sound(String name)
	{
		this.name = name;
		try
		{
			clip = AudioSystem.getClip();
			inputStream = AudioSystem.getAudioInputStream(Sound.class.getResource("sounds/" + name.toLowerCase() + ".wav"));
			clip.open(inputStream);
		}catch (Exception e){e.printStackTrace();
		}
	}
	public static void playSound(Sound sound)
	{
		if(sound.name.equals("backgroundmusic"))
			sound.getClip().loop(Clip.LOOP_CONTINUOUSLY);
		if(sound.clip.isRunning())
		   sound.clip.stop();
		sound.clip.setFramePosition(0);
		sound.clip.start();
	}
	public Clip getClip(){return clip;}
}
*/
