package com.plectrum.heinrich.baconspace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

/*
 * This class contains methods related to stats management.
 * These include components such as time bar information, score
 * and step counters, and difficulty management.
 */
public class StatsActivity extends AppCompatActivity {

    //Must be static for clear_stats to be statically accessible
    static int score = 0;
    final static int score_inc = 1;

    static int steps = 0;
    final static int step_inc = 1;

    static int shoe_count = 0;
    final static int shoe_label_inc = 1;
    //How much to add to the time bar when the shoe is collected, as a percentile
    final static double shoe_inc = 0.08f;

    /*
     * Indicates what buttons are checked on the Settings screen.
     * This is placed here because this information needs to be
     * persistent, thus kept in a class whose screen is never used.
     */
    static int boot_check = R.id.opt_shoe;
    static int goal_check = R.id.opt_ham;
    static int player_check = R.id.opt_humanoid;
    static int threat_check = R.id.opt_bomb;

	//Defines the values for each difficulty for referencing
    private final static int easy = R.id.diff_easy;
    private final static int normal = R.id.diff_normal;
    private final static int stressful = R.id.diff_stressful;
    static int difficulty = normal;

	//Defines the multipliers at each difficulty (easy, normal, stressful)
    public static int[] point_multiplier = {1, 4, 10};
    public static int[] step_multiplier = {1, 2, 3};
    public static int point_mult_ind = 1;
    public static int step_mult_ind = 1;
    //The indexes to point to each multiplier level in the arrays
    private static int[] ind = {0, 1, 2};

    final static float full_scale = 1f;
    final static float half_scale = 0.75f;
    final static float quarter_scale = 0.3f;
    static float scale = full_scale;
    //Determines whether the time bar has ever exceeded full_scale
    static boolean overscale = false;

	/*
	 * Gets the current steps remaining as a percentile
	 */
    public static float get_scale(){ return scale;}

	/*
	 * Sets the current steps remaining
	 */
    public static void set_scale(float num){ scale = num;}

	/*
	 * Increments steps by a given amount
	 */
    public static void adjust_scale(float inc){ scale += inc;}

    /*
     * When making a movement, the scale and time bar need to react accordingly.
     * This method helps associate these actions together whenever
     * MainActivity.update() is called.
     *
     * timebar_update() is missing since it involves findViewById
     */
    public static void check_scale(){

        //Indicate if the scale has ever exceeded 100%
        if (scale > full_scale){
            overscale = true;
        }

        //Increase step counter
        update_steps();
    }

    /*
     * Detects when the player has made a move that results in the scale
     * being between 100-N percent, where N is 100% with offset reduction.
     */
    public static boolean has_made_move(float offset){
        return (scale <= (full_scale - offset) || overscale );
    }

    /*
     * Recolours the time bar based on the amount of steps left
     */
    public static void timebar_recolour(ImageView bar){
        if (scale > full_scale){

            //Surplus of steps (i.e. >100%)
            bar.setImageResource(R.mipmap.blue);
        }else if (scale <= full_scale && scale > half_scale){

            //Most steps are still usable
            bar.setImageResource(R.mipmap.green);
        }else if (scale <= half_scale && scale >  quarter_scale){

            //Around the halfway mark of steps
            bar.setImageResource(R.mipmap.orange);
        }else{

            //Becoming close to running out of steps
            bar.setImageResource(R.mipmap.red);
        }
    }

    /*
     * Restores the scale to 100% and toggle overscale
     */
    public static void restore_scale(){
        scale = full_scale;
        overscale = false;
    }

    /*
     * Increases the scale by a chunk
     */
    public static void increase_scale(){
        modify_scale(1);
    }

    /*
     * Decreases the scale by a chunk with multiplier
     */
    public static void decrease_scale(int mult){
        modify_scale(-mult);
    }

    /*
     * Takes a chunk off the scale with a multiplier.
     * Intended for use when collecting shoes and threats.
     */
    private static void modify_scale(int mult){
        scale += get_step_inc() * get_step_multiplier() * mult;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
    }

	/*
	 * Gets the score
	 */
    public static int get_score(){
        return score;
    }

	/*
	 * Gets the number of steps made in the game
	 */
    public static int get_steps(){
        return steps;
    }

	/*
	 * Gets the number of shoes collected in the game
	 */
    public static int get_shoe_count(){ return shoe_count; }

	/*
	 * Gets the number of steps a shoe increases to the scale
	 */
    public static double get_step_inc(){ return shoe_inc;}

	/*
	 * Increases the step count (DOES NOT AFFECT STEPS REMAINING) 
	 */
    public static void update_steps(){
        steps += step_inc;
    }

	/*
	 * Increases shoe count
	 */
    public static void update_shoe_count(){
        shoe_count += shoe_label_inc;
    }

	/*
	 * Increases score count with multiplier 
	 */
    public static void update_score(){
        score += score_inc * point_multiplier[point_mult_ind];
    }

    //Multiplier methods
    public static int get_step_multiplier(){
        return step_multiplier[step_mult_ind];
    }
    public static int get_point_multiplier(){
        return point_multiplier[point_mult_ind];
    }

    //Setting and checking of object id's
    public static void set_boot_check(int id){
        boot_check = id;
    }
    public static int get_boot_check(){
        return boot_check;
    }

    public static void set_goal_check(int id){
        goal_check = id;
    }
    public static int get_goal_check(){ return goal_check;}

    public static void set_player_check(int id){
        player_check = id;
    }
    public static int get_player_check(){ return player_check;}

    public static int get_threat_check(){ return threat_check;}
    public static void set_threat_check(int id){ threat_check = id;};

    //Difficulty accessing methods
    public static int easy(){return easy;}
    public static int normal(){return normal;}
    public static int stressful(){return stressful;}

    public static int get_difficulty(){ return difficulty;}

    public static void update_threat(){
        switch (difficulty){
            case easy:
                //Just reduces steps
                decrease_scale(1);
                break;
            case normal:
                //Just reduces score
                reduce_score(2);
                break;
            case stressful:
                //Reduces steps and score
                reduce_score(3);
                decrease_scale(2);
                break;
        }
    }

	/*
	 * Reduces score with multiplier, with an additional
	 * mult variable to increase loss further
	 */
    public static void reduce_score(int mult){
        score -= score_inc * get_point_multiplier() * mult;
    }

    /*
     * Gets the difficulty as a String value rather than an ID integer.
     */
    public static String get_difficulty_string(){
        switch (difficulty){
            case easy: return "Easy";
            case normal: return "Normal";
            case stressful: return "Stressful";
        }

        //Difficulty is set to an unknown value
        return "Too difficult to measure";
    }

	/*
	 * Sets multipliers according to difficulty
	 */
    public static void set_multipliers(int diff){
        switch (difficulty){
            case easy:
                point_mult_ind = ind[0];
                step_mult_ind = ind[0];
                break;
            case normal:
                point_mult_ind = ind[1];
                step_mult_ind = ind[1];
                break;
            case stressful:
                point_mult_ind = ind[2];
                step_mult_ind = ind[2];
                break;
        }
    }

	/*
	 * Sets the difficulty to diff
	 */
    public static void set_difficulty(int diff){ difficulty = diff; set_multipliers(diff);}

    /*
     * Clears stats to 0
     */
    public static void clear_stats() {
        score = 0;
        steps = 0;
        shoe_count = 0;
    }
}
