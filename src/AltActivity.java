package com.plectrum.heinrich.baconspace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/*
 * This class contains methods to supply game information to the final screen
 */
public class AltActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alt);

        update_stats();
    }

    /*
     * Allocates values from MainActivity to TextViews that display said values
     */
    public void update_stats(){
		
		//Displays updated TextViews with end game information
        update_textview(R.id.score, StatsActivity.get_score());
        update_textview(R.id.steps, StatsActivity.get_steps());
        update_textview(R.id.shoes, StatsActivity.get_shoe_count());
        update_textview(R.id.point_mult, StatsActivity.get_point_multiplier());

        //Clears the stats for another game
        StatsActivity.clear_stats();

        //Display the difficulty in use and other non-game-related stats
        update_textview(R.id.difficulty, StatsActivity.get_difficulty_string());
    }

    /*
     * Updates the TextView of a given id with the specified integer value
     */
    public void update_textview(int id, int num){
        TextView item = (TextView) findViewById(id);
        item.setText(String.valueOf(num));
    }

    /*
     * Updates a TextView of a given id and directly updates its text as a String.
     */
    public void update_textview(int id, String str){
        TextView item = (TextView) findViewById(id);
        item.setText(str);
    }
}
