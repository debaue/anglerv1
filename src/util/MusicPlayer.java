package util;

import javax.sound.sampled.*;
import java.io.File;

public class MusicPlayer {

    private static Clip clip;

    public static void play(String path) {
        try {
            if (clip != null && clip.isRunning()) clip.stop();
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(path));
            clip = AudioSystem.getClip();
            clip.open(audio);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
        }
    }

    public static void stop() {
        if (clip != null && clip.isRunning()) clip.stop();
    }

    public static void setMuted(boolean muted) {
        if (clip == null) return;
        if (muted && clip.isRunning()) clip.stop();
        else if (!muted && !clip.isRunning()) clip.start();
    }

    public static boolean isRunning() {
        return clip != null && clip.isRunning();
    }

}
