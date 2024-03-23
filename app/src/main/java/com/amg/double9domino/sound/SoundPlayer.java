package com.amg.double9domino.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import com.amg.double9domino.R;

import java.util.Random;

public class SoundPlayer {
    private AudioManager audioManager;
    private boolean foreground;
    private SparseBooleanArray loaded;
    private final Sound[] playSounds = {Sound.PLAY1, Sound.PLAY2, Sound.PLAY3, Sound.PLAY4};
    private SoundPool player;
    private Random r;
    private SparseIntArray sounds;

    public enum Sound {
        PLAY1(0),
        PLAY2(1),
        PLAY3(2),
        PLAY4(3),
        PASS(4),
        WRONG(5),
        LOSE(100),
        WIN(101),
        SHUFFLE(200),
        BELL(300),
        FISH(400);
        
        int index;

        private Sound(int index2) {
            this.index = index2;
        }
    }

    public boolean isMute() {
        return false;
    }

    public boolean isForeground() {
        return this.foreground;
    }

    public void setForeground(boolean foreground2) {
        this.foreground = foreground2;
    }

    public void initSounds(Context context) {
        this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.r = new Random();
        this.sounds = new SparseIntArray();
        this.loaded = new SparseBooleanArray();
        this.player = new SoundPool(5, 3, 0);
        this.player.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            /* class com.lagunex.domino.sound.SoundPlayer.AnonymousClass1 */

            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                SoundPlayer.this.loaded.put(sampleId, status == 0);
            }
        });
        this.sounds.put(Sound.LOSE.index, this.player.load(context, R.raw.lose, 1));
        this.sounds.put(Sound.PASS.index, this.player.load(context, R.raw.pass, 1));
        this.sounds.put(Sound.PLAY1.index, this.player.load(context, R.raw.play1, 1));
        this.sounds.put(Sound.PLAY2.index, this.player.load(context, R.raw.play2, 1));
        this.sounds.put(Sound.PLAY3.index, this.player.load(context, R.raw.play3, 1));
        this.sounds.put(Sound.PLAY4.index, this.player.load(context, R.raw.play4, 1));
        this.sounds.put(Sound.SHUFFLE.index, this.player.load(context, R.raw.shuffle, 1));
        this.sounds.put(Sound.WIN.index, this.player.load(context, R.raw.win, 1));
        this.sounds.put(Sound.WRONG.index, this.player.load(context, R.raw.wrong, 1));
        this.sounds.put(Sound.BELL.index, this.player.load(context, R.raw.bell, 1));
        this.sounds.put(Sound.FISH.index, this.player.load(context, R.raw.fish, 1));
    }

    public void play(Sound sound) {
        if (this.foreground && !isMute() && this.loaded.get(this.sounds.get(sound.index))) {
            float volume = ((float) this.audioManager.getStreamVolume(3)) / ((float) this.audioManager.getStreamMaxVolume(3));
            this.player.play(this.sounds.get(sound.index), volume, volume, 1, 0, 1.0f);
        }
    }

    public void playMove() {
        play(this.playSounds[this.r.nextInt(4)]);
    }
}
