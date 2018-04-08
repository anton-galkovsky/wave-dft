package com.example.DFT_M;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import static com.example.DFT_M.MyActivity.*;

class MyDraw extends View {

    private int width;
    private int height;
    private Paint myPaint1 = new Paint();
    private Paint myPaint2 = new Paint();
    private Paint myPaint3 = new Paint();
    private Paint myPaint4 = new Paint();
    static int period = 0;

    MyDraw(Context context) {
        super(context);

        Point p = new Point();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(p);
        width = Math.min(p.x, p.y);
        height = Math.max(p.x, p.y);

        myPaint1.setColor(Color.WHITE);
        myPaint2.setColor(Color.CYAN);
        myPaint3.setColor(Color.RED);
        myPaint4.setColor(Color.YELLOW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawLine(width / 2, 0, width / 2, height, myPaint1);
        if (wasCount) {
            for (Harmonic h : periods) {
                //Log.d("per ", h.frequency + "  " + h.amplitude);
                canvas.drawCircle((int) (width / 2 + h.amplitude / 5000), (int) (h.frequency), 3, myPaint3);
            }

            double k = height / harmonics[harmonics.length - 1].frequency;
            for (Harmonic h : harmonics) {
                //Log.d("har ", h.amplitude + "  " + h.frequency);
                canvas.drawCircle((int) (width / 2 + h.amplitude * 100), (int) (k * h.frequency), 3, myPaint2);
            }
            if (period != height)
                canvas.drawCircle((int) (width / 2 + periods[period].amplitude / 5000), (int) (periods[period].frequency), 5, myPaint4);


//            draw(3, periods, myPaint3, canvas);
//            draw(1, harmonics, myPaint2, canvas);
        }
        if (myBuffer != null)
            for (int x = 0; x < height; x++)
                canvas.drawPoint(width / 2 + myBuffer[x] / 10, x, myPaint1);
        invalidate();
    }

    private void draw(int k, Harmonic[] harmonic, Paint myPaint, Canvas canvas) {
        for (Harmonic h : harmonic) {
            //Log.d("aaa ", h.amplitude + "  " + h.frequency);
            canvas.drawCircle((int) (width / 2 + h.amplitude / 100 / k), (int) (h.frequency), 3, myPaint);
        }
//            canvas.drawCircle((int) (width / 2 + h.amplitude * 5 * k), (int) (h.frequency * 2), 2, myPaint);
    }

//    private int getRadius(double frequency) {
//        return 2 + (int) Math.cos(Math.PI / 2 * Math.signum(frequency - 440)) * 3;
//    }
//
//    private int x(Point p) {
//        return -p.y * kx;
//    }
//
//    private int y(Point p) {
//        return width / 2 + p.x * ky;
//    }
//
//    void setPoints(short[] myBuffer) {
//        this.myBuffer = myBuffer;
//    }
}