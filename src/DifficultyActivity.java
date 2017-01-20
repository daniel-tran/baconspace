package com.plectrum.heinrich.baconspace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

/*
 * This class manages difficulty settings to the game
 */
public class DifficultyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        update_difficulty(StatsActivity.get_difficulty());
    }

    /*
     * This core method involves toggling the correct difficulty and setting
     * the blurb as needed
     */
    public void update_difficulty(int diff){

        //Check the radio button and set the internal difficulty
        check(diff);
        StatsActivity.set_difficulty(diff);

        //Difficulty blurbs
        if (diff == StatsActivity.easy()){
            set_blurb("The ideal difficulty for beginners\n"+
                      "and scrubs alike.\n\n"+
                      "Collectibles are plentiful!\n"+
                      "But no active multipliers.");

        }else if (diff == StatsActivity.normal()){
            set_blurb("Challenge your pals and impress\n"+
                      "ladies! Try it today!\n\n"+
                      "Collectibles are uncommon!\n"+
                      StatsActivity.get_point_multiplier()+ "x multiplier in points!\n"+
                      StatsActivity.get_step_multiplier()+ "x multiplier in steps from shoes!");

        }else if (diff == StatsActivity.stressful()){
            set_blurb("Don't get your shirt caught in the\n"+
                      "microwave...again.\n\n"+
                      "Collectibles become scarce!\n"+
                    StatsActivity.get_point_multiplier()+ "x multiplier in points!\n"+
                    StatsActivity.get_step_multiplier()+ "x multiplier in steps from shoes!");
        }
    }

	/*
	 * Sets the game to easy mode
	 */
    public void set_easy(View view){
        update_difficulty(StatsActivity.easy());
    }

	/*
	 * Sets the game to normal mode
	 */
    public void set_normal(View view){
        update_difficulty(StatsActivity.normal());
    }

	/*
	 * Sets the game to stressful mode
	 */
    public void set_stressful(View view){
        update_difficulty(StatsActivity.stressful());
    }

    /* ===== TAKEN DIRECTLY FROM ReskinActivity.java ======
     * This is because findViewById is not static and only applies to a specific activity.
     *
     * Checks the button of a given ID when the screen is opened.
     * This makes it look like the settings have been saved
     */
    public void check(int button){
        RadioButton rb = (RadioButton) findViewById(button);
        rb.setChecked(true);
    }

    /*
     * Sets the blurb object and changes its text to str.
     */
    public void set_blurb(String str){
        TextView blurb = (TextView) findViewById(R.id.blurb);
        blurb.setText(str);
    }
}
