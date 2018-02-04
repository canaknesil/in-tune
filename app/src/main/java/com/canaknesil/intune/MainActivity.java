package com.canaknesil.intune;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    NotePlayer np;
    String currNote = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        np = new NotePlayer();

        final Spinner spinner = findViewById(R.id.note_spinner);
        currNote = spinner.getSelectedItem().toString();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currNote = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currNote = null;
            }
        });

        final EditText freqET = findViewById(R.id.ref_freq_editText);
        freqET.setText(String.valueOf(np.getDefaultRefFreq()));
        freqET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {

                    String str = freqET.getText().toString();
                    if (str.isEmpty()) {
                        setPlayerFreq(np.getDefaultRefFreq());
                    } else {
                        double freq = Double.parseDouble(freqET.getText().toString());
                        setPlayerFreq(freq);
                    }

                }
                return false;
            }
        });

        final ImageButton forkButton = findViewById(R.id.fork_imageButton);
        forkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNote();
            }
        });

    }

    private void setPlayerFreq(double freq) {
        np.setRefFreq(freq);
    }

    private void playNote()
    {
        if (currNote != null) {
            np.play(currNote);
        }
    }


}
