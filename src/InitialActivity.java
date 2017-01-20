package com.plectrum.heinrich.baconspace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

/*
 * This class manages the initial UI that lets the user
 * navigate around the app
 */
public class InitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
    }

	/*
	 * Goes to the main game screen
	 */
    public void play_game(View view){
        //Prevent stat stacking from previous games
        StatsActivity.clear_stats();
        Intent go = new Intent(this, MainActivity.class);
        startActivity(go);
    }

	/*
	 * Goes to the settings screen
	 */
    public void settings(View view){
        Intent go = new Intent(this, ReskinActivity.class);
        startActivity(go);
    }

	/*
	 * Goes to the difficulty screen
	 */
    public void set_difficulty(View view){
        Intent go = new Intent(this, DifficultyActivity.class);
        startActivity(go);
    }

}
