package com.imadrummoney;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;
import net.runelite.client.audio.*;

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

    @Inject
    private AudioPlayer audioPlayer;


    public void playClip(Sound sound) {
        try {
            float gain = 20f * (float)Math.log(config.soundVolume() / 100f);
            audioPlayer.play(SoundclipManager.class, sound.getFileName(), gain);
        }catch (IOException | UnsupportedAudioFileException | LineUnavailableException e){
            log.error("Cannot play clip - " + e.getMessage());
        }
    }

    public Sound getRandomSoundclip() {
        int randomClipNr = ThreadLocalRandom.current().nextInt(10);

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
        if (ThreadLocalRandom.current().nextInt(5) == 0) {
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
        if (ThreadLocalRandom.current().nextInt(50) == 0)
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
