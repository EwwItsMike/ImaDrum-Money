package com.imadrummoney;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sound.sampled.*;
import java.io.*;
import java.util.Random;

@Singleton
@Slf4j
public class SoundclipManager {

    public enum Sound {
        SOUND1("/imadrumMoney1.wav"),
        SOUND2("/imadrumMoney2.wav"),
        SOUND3("/imadrumMoney3.wav"),
        SOUND4("/imadrumMoney4.wav"),
        SOUND5("/imadrumMoney5.wav"),
        SOUND6("/imadrumMoney6.wav"),
        SOUND7("/imadrumMoney7.wav"),
        SOUND8("/imadrumMoney8.wav"),
        SOUND9("/imadrumMoney9.wav"),
        NEWPET("/newPetDrop.wav"),
        DUPEPET("/dupePetDrop.wav"),
        CLUEDROP("/clueScrollDrop.wav"),
        NOTHING("/nothing.wav"),
        SADNESS("/sadness.wav"),
        RUBYSPEC("/alotofdamage.wav"),
        RUBYSPEC2("/REEE.wav"),
        SUPERIOR("/bigboi.wav"),
        DEATH("/pleae.wav");
        private final String fileName;

        Sound(String filename) {
            fileName = filename;
        }

        String getFileName() {
            return fileName;
        }
    }

    @Inject
    private ImadrumMoneyConfig config;

    private Clip clip = null;
    private static final long CLIP_MTIME_UNLOADED = -2;
    private long lastClipMTime = CLIP_MTIME_UNLOADED;

    // Source: c-engineer-completed plugin
    // https://github.com/m0bilebtw/c-engineer-completed
    private boolean loadClip(Sound sound) {
        try (InputStream s = getClass().getResourceAsStream(sound.getFileName());
             InputStream bufferedIn = new BufferedInputStream(s);
             AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn)) {
            clip.open(audioStream);
            return true;
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | NullPointerException e) {
            log.warn("Failed to load sound " + sound, e);
        }
        return false;
    }

    // Source: c-engineer-completed plugin
    // https://github.com/m0bilebtw/c-engineer-completed
    public void playClip(Sound sound) {
        long currentMTime = System.currentTimeMillis();

        if (clip == null || currentMTime != lastClipMTime || !clip.isOpen()) {
            if (clip != null && clip.isOpen()) {
                clip.close();
            }

            try {
                clip = AudioSystem.getClip();
            } catch (LineUnavailableException e) {
                lastClipMTime = CLIP_MTIME_UNLOADED;
                log.warn("Failed to get clip for sound " + sound, e);
                return;
            }

            lastClipMTime = currentMTime;
            if (!loadClip(sound)) {
                return;
            }
        }

        // User configurable volume
        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float gain = 20f * (float) Math.log10(config.soundVolume() / 100f);
        gain = Math.min(gain, volume.getMaximum());
        gain = Math.max(gain, volume.getMinimum());
        volume.setValue(gain);

        clip.loop(0);
    }

    public Sound getRandomSoundclip() {
        Random r = new Random();
        int randomClipNr = r.nextInt(10);

        switch (randomClipNr) {
            case 1:
                return Sound.SOUND2;
            case 2:
                return Sound.SOUND3;
            case 3:
                return Sound.SOUND4;
            case 4:
                return Sound.SOUND5;
            case 5:
                return Sound.SOUND6;
            case 6:
                return Sound.SOUND7;
            case 7:
                return Sound.SOUND8;
            case 8:
                return Sound.SOUND9;
            default:
                return Sound.SOUND1;
        }
    }

    public Sound getRandomSadSoundClip() {
        if (new Random().nextInt(2) == 0) {
            return Sound.SADNESS;
        }
        return Sound.NOTHING;
    }

    public Sound getClueSound() {
        return Sound.CLUEDROP;
    }

    public Sound getNewPetSound() {
        return Sound.NEWPET;
    }

    public Sound getDupePetSound() {
        return Sound.DUPEPET;
    }

    public Sound getRubySpecSound() {

        if (new Random().nextInt(50) == 0)
            return Sound.RUBYSPEC2;
        else
            return Sound.RUBYSPEC;
    }

    public Sound getDeathSound() {
        return Sound.DEATH;
    }

    public Sound getSuperiorSound() {
        return Sound.SUPERIOR;
    }

}
