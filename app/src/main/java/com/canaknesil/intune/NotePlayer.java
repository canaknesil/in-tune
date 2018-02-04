package com.canaknesil.intune;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;

import java.util.HashMap;

/**
 * Created by canaknesil on 31.01.2018.
 */

public class NotePlayer {

    /* These parameters can be modified */
    private static final double DEFAULT_REF_FREQ = 440; // Hz
    private static final int SAMPLING_RATE = 44100; // Hz
    private static final int DURATION = 4; // in second

    /* Frequency rates of equal temperament according to note A */
    private static final double[] RATES  = {
            1.0,
            1.05946309435929530984,
            1.12246204830937301722,
            1.18920711500272102690,
            1.25992104989487319067,
            1.33483985417003436780,
            1.41421356237309514547,
            1.49830707687668152062,
            1.58740105196819936140,
            1.68179283050742900407,
            1.78179743628067854821,
            1.88774862536338683405
    };


    private short[][] noteArray = new short[12][];
    private SoundPlayer player;


    public NotePlayer(double freq) {
        initNoteArray(freq);
        player = new SoundPlayer(SAMPLING_RATE);
    }

    public NotePlayer() {
        this(DEFAULT_REF_FREQ);
    }


    private void initNoteArray(double baseFreq) {
        for (int i = 0; i < 12; i++) {
            noteArray[i] = generateNote(baseFreq * RATES[i]);
        }
    }

    private static short[] generateNote(double freq) {
        int noteSize = SAMPLING_RATE * DURATION;
        short[] note = new short[noteSize];

        int sineSize = (int) (SAMPLING_RATE / freq);
        short[] sine = new short[sineSize];
        for (int n = 0; n < sine.length; n++) {
            sine[n] = (short) (Math.sin(2 * Math.PI * freq * n / SAMPLING_RATE) * Short.MAX_VALUE);
        }

        for (int n = 0; n < noteSize; n++) {
            note[n] = sine[n % sineSize];
        }

        envelope(note);
        return note;
    }

    private static double envFunc(double x, double w /*width*/) {
        x = x/w;
        return (-x*x + 1) * Math.exp(-4*x);
    }

    private static void envelope(short[] note) {
        double len = note.length;
        for (int i = 0; i < len; i++) {
            note[i] =  (short) Math.round(note[i] * envFunc(i, len));
        }
    }


    public void play(String str) {
        int n;
        if (str.equals("A"))        n = 0;
        else if (str.equals("A#"))  n = 1;
        else if (str.equals("B"))   n = 2;
        else if (str.equals("C"))   n = 3;
        else if (str.equals("C#"))  n = 4;
        else if (str.equals("D"))   n = 5;
        else if (str.equals("D#"))  n = 6;
        else if (str.equals("E"))   n = 7;
        else if (str.equals("F"))   n = 8;
        else if (str.equals("F#"))  n = 9;
        else if (str.equals("G"))   n = 10;
        else if (str.equals("G#"))  n = 11;
        else n = -1;

        if (n >= 0) {
            player.play(noteArray[n]);
        }
    }

    public void stop() {
        player.stop();
    }



    public void setRefFreq(double freq) {
        initNoteArray(freq);
    }

    public double getDefaultRefFreq() {
        return DEFAULT_REF_FREQ;
    }




}
