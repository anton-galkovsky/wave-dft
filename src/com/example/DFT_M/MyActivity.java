package com.example.DFT_M;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;


public class MyActivity extends Activity {

    private AudioRecord audioRecord;
    private MyDraw myDraw;
    private int myBufferSize;
    //    private int frequencyStep = 1;    //width = measAm * freqSt
//    private double measuringTime;
    public static short[] myBuffer;
    public static int measurementsAmount;   //width = measAm * freqSt
    public static Harmonic[] periods;
    public static Harmonic[] harmonics;
    public static boolean wasClick = false;
    public static boolean wasCount = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        myDraw = new MyDraw(this);
        ((LinearLayout) findViewById(R.id.my_layout)).addView(myDraw);

        createAudioRecorder();

        Point p = new Point();
        ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(p);
        measurementsAmount = Math.max(p.x, p.y);
//        measurementsAmount = Math.max(p.x, p.y) / frequencyStep;

        harmonics = new Harmonic[measurementsAmount];

        audioRecord.startRecording();
//        while (true)
//            while (!wasClick) {
//                long t0 = System.nanoTime();
//                audioRecord.read(myBuffer, 0, myBufferSize);
//                long t = System.nanoTime();
//                measuringTime = (t - t0) / 1000000000.0 / myBufferSize;
//            }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    while (!wasClick) {
//                    long t0 = System.nanoTime();
                        audioRecord.read(myBuffer, 0, myBufferSize);
//                    measuringTime = (System.nanoTime() - t0) / 1000000000.0;
                    }
                    count();
                    while (wasClick);
                }
            }
        }).start();
    }

    public void OnClick(View view) {
        wasClick = !wasClick;
        wasCount = false;
    }

    private void count() {
        short[] values = new short[measurementsAmount];
        System.arraycopy(myBuffer, 0, values, 0, measurementsAmount);

        periods = getPeriods(values);
        double perFuncMin = periods[1].amplitude;
        double period = periods[1].frequency;

        for (int i = 1; i < periods.length; i++) {
            periods[i].amplitude += 400 * i;
            if (perFuncMin > periods[i].amplitude) {
                perFuncMin = periods[i].amplitude;
                period = periods[i].frequency;
            }
        }
        MyDraw.period = (int) period;

        for (int num = 0; num < measurementsAmount; num++)
            harmonics[num] = new Harmonic(values, period, num);

        wasCount = true;
    }

    private Harmonic[] getPeriods(short[] values) {
        Harmonic[] periods = new Harmonic[values.length];
        periods[0] = new Harmonic(0, 0);
        for (int t = 1; t < values.length; t++) {
            periods[t] = new Harmonic(0, t);
            for (int i = 0; i < values.length; i++)
                periods[t].amplitude += Math.abs(values[i] - values[i % t]);
        }
        return periods;
    }

    private void createAudioRecorder() {
        if (audioRecord != null) {
            audioRecord.release();
        }
        int sampleRate = 88200;
        int audioSource = MediaRecorder.AudioSource.VOICE_RECOGNITION;
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        myBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
        audioRecord = new AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, myBufferSize);
        myBuffer = new short[myBufferSize];
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioRecord != null) {
            audioRecord.release();
        }
    }
}