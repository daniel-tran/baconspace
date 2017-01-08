package com.plectrum.heinrich.baconspace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/*
 * This class contains methods related to stats management.
 */
public class StatsActivity extends AppCompatActivity {

    //Must be static for clear_stats to be statically accessible
    static int score = 0;
    final static int score_inc = 1;

    static int steps = 0;
    final static int step_inc = 1;

    static int shoe_count = 0;
    final static int shoe_label_inc = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
    }

    public static int get_score(){
        return score;
    }

    public static int get_shoe_count(){
        return shoe_count;
    }

    public static int get_steps(){
        return steps;
    }

    public static void update_steps(){
        steps += step_inc;
    }

    public static void update_shoe_count(){
        shoe_count += shoe_label_inc;
    }

    public static void update_score(){
        score += score_inc;
    }

    /*
     * Clears stats to 0
     */
    public static void clear_stats() {
        score = 0;
        steps = 0;
        shoe_count = 0;
    }
}
