package sk.tuke.gamestudio.game.pegsolitaire.consoleui;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class MusicalGame {
    public static void playSound(String soundFile) {
        try {
            URL soundURL = MusicalGame.class.getClassLoader().getResource("sounds/" + soundFile);
            if (soundURL == null) {
                System.err.println("Sound is not found!" + soundFile);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            AudioFormat baseFormat = audioStream.getFormat();
            AudioFormat convert = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(),
                    16,
                          1,  // mono
                                  2,  // 16-bit = 2
                    baseFormat.getSampleRate(),
                    false
            );
            AudioInputStream convertedStream = AudioSystem.getAudioInputStream(convert, audioStream);
            Clip clip = AudioSystem.getClip();
            clip.open(convertedStream);
            clip.start();

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Audio error!" + e.getMessage());
        }
    }
}
