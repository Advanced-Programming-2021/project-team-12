package Utility;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sounds {
    public static Clip play(String music, int a) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        File musicAddress = new File(music);
        Clip clip = AudioSystem.getClip();
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicAddress);
        clip.open(audioInputStream);
        if (a == 0)
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        return clip;
    }
}
