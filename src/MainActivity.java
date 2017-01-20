package com.plectrum.heinrich.baconspace;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.Button;
import android.os.Handler;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Standard step measurement
    final float inc = 25f;

    //How much to take off the time bar per step, as a percentile
    final float time_inc = 0.02f;

    //Tells if the player movement should pause (false: player movement is allowed)
    boolean hold = false;

    //How close objects need to be to be considered "collided", measured in pixels(?)
    final float collideDistance = 50f;

    //Millisecond time interval between press-and-hold movements
    final int interval = 150;

	//States if the game needs to restart
    boolean restart = true;

	//Holds the information for each object type
    ArrayList<ImageView> boots = new ArrayList<>();
    ArrayList<ImageView> goals = new ArrayList<>();
    ArrayList<ImageView> threats = new ArrayList<>();

	//Stores the images for each object type
    static int img_boot = R.mipmap.shoe;
    static int img_goal = R.mipmap.ham;
    static int img_threat = R.mipmap.bomb;
    static int[] img_player = {R.mipmap.chip_down, R.mipmap.chip_up,
            R.mipmap.chip_left, R.mipmap.chip_right};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialise();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);

        /*
         * Position randomisation cannot be called in initialise() because the screen has
         * not been drawn yet. As such, the game needs to wait until this system method
         * is internally called before getWidth() and getHeight() are accessible.
         *
         * Inspired by Sana's solution at
         * http://stackoverflow.com/questions/3591784/getwidth-and-getheight-of-view-returns-0
         */
        if (restart) {
            restart = false;

            ImageView player = get_player();

            RelativeLayout rl = get_layout();

            //Randomise player and goal positions within the layout bounds
            PlacementActivity.random_position(player, rl.getWidth(), rl.getHeight(), inc);
            PlacementActivity.random_position(boots, rl.getWidth(), rl.getHeight(), inc);
            PlacementActivity.random_position(goals, rl.getWidth(), rl.getHeight(), inc);
            PlacementActivity.random_position(threats, rl.getWidth(), rl.getHeight(), inc);
        }

        //Prevent movement build-up when holding buttons and unfocussing the app
        if (hasFocus){
            hold = false;
        }else{
            hold = true;
        }
    }

    /*
     * Initialises the game state as a new game
     */
    public void initialise(){

        //Indicate a restart for other functions to use
        restart = true;

        //Sets the tutorial text to be visible again
        TextView tutorial = get_tutorial();
        tutorial.setVisibility(View.VISIBLE);

        ImageView player = get_player();

        final ImageView timebar = get_timebar();

        //Set default player sprite when spawning
        player.setImageResource(player_down());

        //Set up all the buttons and enable press-and-hold functionality
        Button down_button = (Button) findViewById(R.id.down);
        press_and_hold(down_button, "down");

        Button up_button = (Button) findViewById(R.id.upp);
        press_and_hold(up_button, "up");

        Button left_button = (Button) findViewById(R.id.left);
        press_and_hold(left_button, "left");

        Button right_button = (Button) findViewById(R.id.right);
        press_and_hold(right_button, "right");

        //Set shoe and goal allocations for future use
        set_shoes();
        set_goals();
        set_threats();

        //Set up the timer bar
        timebar.setScaleY(1f);
        timebar.setVisibility(View.VISIBLE);
        StatsActivity.restore_scale();
        StatsActivity.timebar_recolour(timebar);

        //Default the score label for a new game
        set_score_label(0);
    }

    /*
     * Sets the shoe sprite to the given parameter.
     * Does nothing when already set to img.
     */
    public static void set_shoe_img(int img){
        if (img_boot != img) {
            img_boot = img;
        }
    }

    /*
     * Sets the goal sprite to the given parameter.
     * Does nothing when already set to img.
     */
    public static void set_goal_img(int img){
        if (img_goal != img){
            img_goal = img;
        }
    }

    /*
     * Sets the player sprite sheet to the given parameter.
     * Does nothing when already set to img.
     */
    public static void set_player_img(int[] img){
        if (img_player != img){
            img_player = img;
        }
    }

    public static void set_threat_img(int img){
        if (img_threat != img){
            img_threat = img;
        }
    }

    /*
     * Assigns the stored shoe sprite to the necessary targets.
     */
    public void update_shoe_img(){
        for (int i = 0; i < boots.size(); i++){
            boots.get(i).setImageResource(img_boot);
        }
    }

    /*
     * Assigns the stored goal sprite to the necessary targets.
     */
    public void update_goal_img(){
        for (int i = 0; i < goals.size(); i++){
            goals.get(i).setImageResource(img_goal);
        }
    }

    /*
     * Assigns the stored threat sprite to the necessary targets.
     */
    public void update_threat_img(){
        for (int i = 0; i < threats.size(); i++){
            threats.get(i).setImageResource(img_threat);
        }
    }

    /*
     * Sets the shoes in use. Only runs when source is empty.
     */
    public void set_shoes(){
        if (boots.size() <= 0){

            //Hard mode shoes
            boots.add((ImageView) findViewById(R.id.shoe));

            if (StatsActivity.get_difficulty() != StatsActivity.stressful()) {

                //Normal mode extra shoes
                boots.add((ImageView) findViewById(R.id.shoe2));
                boots.add((ImageView) findViewById(R.id.shoe3));

                if (StatsActivity.get_difficulty() != StatsActivity.normal()) {

                    //Easy mode extra shoes
                    boots.add((ImageView) findViewById(R.id.shoe4));
                    boots.add((ImageView) findViewById(R.id.shoe5));
                }
            }

            update_shoe_img();
        }
    }

    /*
     * Sets the goals in use. Only runs when source is empty.
     */
    public void set_goals(){
        if (goals.size() <= 0){

            //Hard mode goals
            goals.add((ImageView) findViewById(R.id.bugman));

            if (StatsActivity.get_difficulty() != StatsActivity.stressful()) {

                //Normal mode extra goals
                goals.add((ImageView) findViewById(R.id.goal2));
                if (StatsActivity.get_difficulty() != StatsActivity.normal()) {

                    //Easy mode extra goals
                    goals.add((ImageView) findViewById(R.id.goal3));
                }
            }

            update_goal_img();
        }
    }

    /*
     * Sets the threats in use. Only runs when source is empty.
     */
    public void set_threats(){
        if (threats.size() <= 0){
            threats.add((ImageView) findViewById(R.id.threat1));

            if (StatsActivity.get_difficulty() != StatsActivity.easy()){
                threats.add((ImageView) findViewById(R.id.threat2));
                threats.add((ImageView) findViewById(R.id.threat3));

                if (StatsActivity.get_difficulty() != StatsActivity.normal()){
                    threats.add((ImageView) findViewById(R.id.threat4));
                    threats.add((ImageView) findViewById(R.id.threat5));
                }
            }
        }

        update_threat_img();
    }

    /*
     * Updates the game state by checking for goal collisions and updating the timebar
     */
    public void update(){
        /*
         * http://stackoverflow.com/questions/26252710/detect-if-views-are-overlapping
         * - Inspiration for the following thought process
         *
         * http://www.mathwarehouse.com/algebra/distance_formula/index.php
         * - If you need to recall the distance formula again
         */
        TextView tutorial = get_tutorial();
        tutorial.setVisibility(View.INVISIBLE);

        //Gets your location on screen
        ImageView cake = get_player();

        RelativeLayout rl = get_layout();

        keep_inbound(cake, rl);

        //Updates other items and check if player has collected them
        collection_update(cake, boots, rl.getWidth(), rl.getHeight(), "boots");
        collection_update(cake, goals, rl.getWidth(), rl.getHeight(), "goals");
        collection_update(cake, threats, rl.getWidth(), rl.getHeight(), "threats");

        //Performs some maintenance operations in regards to scale status
        StatsActivity.check_scale();

        timebar_update(time_inc);
    }

    /*
     * This method handles collision checking between the user and a set of items, under the
     * assumption that they are all ImageViews.
     *
     * When an item is collected, its position is randomised within the specified width and height
     * parameters and activates an additional module based on the given effect.
     * (an effect that matches no valid entry will do nothing)
     */
    public void collection_update(ImageView user, ArrayList<ImageView> items, int width, int height, String effect){
        for (int i = 0; i < items.size(); i++){
            double dist = PlacementActivity.check_collision(user, items.get(i));

            //Check for a collision
            if (dist <= collideDistance){

                PlacementActivity.random_position(items.get(i), width, height, inc);

                //This is where specific effects are run from
                switch (effect) {
                    case "goals":
                        //Adds to score when the ham is obtained
                        StatsActivity.update_score();
                        set_score_label(StatsActivity.get_score());
                        break;

                    case "boots":
                        //Get more steps when the shoe is obtained
                        StatsActivity.increase_scale();
                        StatsActivity.update_shoe_count();
                        break;
                    case "threats":
                        StatsActivity.update_threat();
                        set_score_label(StatsActivity.get_score());
                        break;
                }
            }
        }
    }

    /*
     * Gets the player object
     */
    public ImageView get_player(){
        return (ImageView) findViewById(R.id.cake);
    }

    /*
     * Gets the layout object where everything is drawn
     */
    public RelativeLayout get_layout(){
        return (RelativeLayout) findViewById(R.id.relativelayout);
    }

    /*
     * Gets the goal object
     */
    public ArrayList<ImageView> get_goal(){
        return goals;
    }

    /*
     * Gets the shoe object
     */
    public ArrayList<ImageView> get_shoe(){
        return boots;
    }

    /*
     * Gets the timebar object
     */
    public ImageView get_timebar(){
        return (ImageView) findViewById(R.id.timebar);
    }

    /*
     * Gets the tutorial text
     */
    public TextView get_tutorial(){
        return (TextView) findViewById(R.id.tutorial);
    }

    /*
     * Moves an object based on the input string.
     * Does nothing if the string maps to none of the cases.
     */
    public void move(String str){
        switch(str.toLowerCase()){
            case "up":
                move_up();
                break;
            case "down":
                move_down();
                break;
            case "left":
                move_left();
                break;
            case "right":
                move_right();
                break;
        }
    }

    /*
     * Moves an object based on X and Y vectors.
     * (-1,-1) moves to bottom left,
     * (1,1) moves to top right,
     * (0,0) does nothing
     */
    public void move(int x, int y){

        ImageView obj = get_player();

        if (x > 0){
            //Move right
            obj.setX(obj.getX() + inc);
            obj.setImageResource(player_right());
        }else if (x < 0){
            //Move left
            obj.setX(obj.getX() - inc);
            obj.setImageResource(player_left());
        }

        if (y < 0){
            //Move down
            obj.setY(obj.getY() + inc);
            obj.setImageResource(player_down());
        }else if (y > 0){
            //move up
            obj.setY(obj.getY() - inc);
            obj.setImageResource(player_up());
        }
    }

    /*
     * These methods are synonyms for accessing the player
     * sprite array in a more intuitive way.
     */
    public int player_down(){ return img_player[0];}
    public int player_up(){ return img_player[1];}
    public int player_left(){ return img_player[2];}
    public int player_right(){ return img_player[3];}

    /*
     * Moves down and updates game state
     */
    public void move_down(){
        move(0,-1);

        update();
    }

    /*
     * Moves up and updates game state
     */
    public void move_up(){
        move(0,1);

        update();
    }

    /*
     * Moves left and updates game state
     */
    public void move_left(){
        move(-1,0);

        update();
    }

    /*
     * Moves right and updates game state
     */
    public void move_right(){
        move(1,0);

        update();
    }

    /*
     * Updates the timebar and handles end-of-game actions
     */
    public void timebar_update(float time){
        ImageView timebar = get_timebar();
        if (StatsActivity.get_scale() > 0){
            StatsActivity.adjust_scale(-time);
            timebar.setScaleY(StatsActivity.get_scale());
            StatsActivity.timebar_recolour(timebar);
        }
        if (StatsActivity.get_scale() <= 0){
            timebar.setVisibility(View.INVISIBLE);
            screen(AltActivity.class);
            initialise();
        }
    }

    /*
     * Keeps obj within the bounds of field, moving it when necessary
     */
    public void keep_inbound(View obj, RelativeLayout field){

        boolean no_move = false;

        if (obj.getY() > (field.getHeight() - inc)){
            move_up();
            no_move = true;
        }

        if (obj.getY() < inc){
            move_down();
            no_move = true;
        }

        if (obj.getX() < inc){
            move_right();
            no_move = true;
        }

        if (obj.getX() > (field.getWidth() - inc)){
            move_left();
            no_move = true;
        }

        //If movement was neutralised, get back the time lost
        if (no_move) {
            timebar_update(-time_inc*2);
        }
    }

    /*
     * Sets the score-related TextView with the specified integer
     */
    public void set_score_label(int num){
        TextView score_label = (TextView) findViewById(R.id.score_label);
        score_label.setText(String.valueOf(num));
    }

    /*
     * Opens a screen using the relevant class argument.
     * Causes a fatal error is said class has no associated XML file (e.g. screen(Integer.class) )
     */
    public void screen(Class ac){
        Intent intent = new Intent(this, ac);
        startActivity(intent);
    }

    /*
     * Enables a button to have press-and-hold functionality by adding a
     * Listener that detects if a touch press is still active.
     *
     * See source for inspiration:
     * http://stackoverflow.com/questions/10511423/android-repeat-action-on-pressing-and-holding-a-button
     */
    public void press_and_hold(Button button, final String method){
        button.setOnTouchListener(new View.OnTouchListener(){

            private Handler handle;

            @Override
            public boolean onTouch(View view, MotionEvent event){

                //Detect a touch on the button
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    if (handle == null) {

                        //Button is freshly held down; act, delay and repeat
                        move(method);

                        handle = new Handler();
                        handle.postDelayed(action, interval);
                    }else{

                        //Button is still held down
                        return true;
                    }
                }

                //Detect a release on the button
                if (event.getAction() == MotionEvent.ACTION_UP){
                    if (handle != null){

                        //Button is freshly released; erase method backlog
                        handle.removeCallbacks(action);
                        handle = null;
                    }else{

                        //Button is still released
                        return true;
                    }
                }

                return false;
            }

            //Perform this action while the button is held down
            Runnable action = new Runnable(){
                @Override
                public void run(){
                    move(method);

                    /*
                     * Ensures that a new game forgets the old actions.
                     * Repeated actions only occur when a movement has been
                     * made.
                     *
                     * !hold ensures that unfocussing the app will cancel existing
                     * actions, defaulting movement to nothing
                     */
                    if (!hold && StatsActivity.has_made_move(time_inc)) {
                        handle.postDelayed(this, interval);
                    }
                }
            };
        });
    }
}
