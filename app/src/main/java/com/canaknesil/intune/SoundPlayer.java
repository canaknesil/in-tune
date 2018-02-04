package com.canaknesil.intune;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by canaknesil on 03.02.2018.
 */

public class SoundPlayer {

    private static final int CHANNEL = AudioFormat.CHANNEL_OUT_MONO;
    private static final int ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioTrack track;
    private Thread playThread;


    public SoundPlayer(int samplingRate) {
        int bufferSize = AudioTrack.getMinBufferSize(samplingRate, CHANNEL, ENCODING);
        track = new AudioTrack(AudioManager.STREAM_MUSIC, samplingRate, CHANNEL, ENCODING, bufferSize, AudioTrack.MODE_STREAM);
        track.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
    }


    public void play(final short[] note) {
        stop();

        playThread = new Thread() {
            public void run() {
                track.write(note, 0, note.length);
                track.stop();
            }
        };

        track.play();
        playThread.start();
    }

    public void stop() {
        track.stop();
        //TODO There is a click when stopping because of non zero endings.
        if (playThread != null) {
            if (playThread.isAlive()) {
                try {
                    playThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
