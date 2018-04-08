package com.example.DFT_M;

class Harmonic {

    double amplitude;
    double frequency;

    Harmonic(double amplitude, double frequency) {
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    public Harmonic(short[] values, double period, int num) {
        double cos = 0, sin = 0;
        frequency = 2 * Math.PI * num / period;
        for (int dx = 0; dx < period; dx++) {
            cos += values[dx] * Math.cos(frequency * dx);
            sin += values[dx] * Math.sin(frequency * dx);
        }
        amplitude = Math.sqrt(cos * cos + sin * sin) * 2 / period;
    }
}
