package com.plectrum.heinrich.baconspace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.Button;
import android.os.Handler;

//import com.plectrum.heinrich.baconspace.AltActivity;

public class MainActivity extends AppCompatActivity {

    //Standard step measurement
    final float inc = 25f;

    //How much to take off the time bar per step, as a percentile
    final float time_inc = 0.01f;

    final float full_scale = 1f;
    float scale = full_scale;
    //Determines whether the time bar has ever exceeded full_scale
    boolean overscale = false;

    //How close objects need to be to be considered "collided", measured in pixels(?)
    final float collideDistance = 100f;

    //Millisecond time interval between press-and-hold movements
    final int interval = 150;

    //How much to add to the time bar when the shoe is collected, as a percentile
    final double shoe_inc = 0.05f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialise();
    }

    /*
     * Initialises the game state as a new game
     */
    public void initialise(){

        //Sets the tutorial text to be visible again
        TextView tutorial = get_tutorial();
        tutorial.setVisibility(View.VISIBLE);

        ImageView player = get_player();

        RelativeLayout rl = get_layout();

        ImageView get = get_goal();

        final ImageView timebar = get_timebar();

        ImageView shoe = get_shoe();

        player.setImageResource(R.mipmap.chip_down);


        //Set up all the buttons and enable press-and-hold functionality
        Button down_button = (Button) findViewById(R.id.down);
        press_and_hold(down_button, "down");

        Button up_button = (Button) findViewById(R.id.upp);
        press_and_hold(up_button, "up");

        Button left_button = (Button) findViewById(R.id.left);
        press_and_hold(left_button, "left");

        Button right_button = (Button) findViewById(R.id.right);
        press_and_hold(right_button, "right");


        //Randomise player and goal positions within the layout bounds
        PlacementActivity.random_position(player, rl.getWidth(), rl.getHeight(), inc);
        PlacementActivity.random_position(get, rl.getWidth(), rl.getHeight(), inc);
        PlacementActivity.random_position(shoe, rl.getWidth(), rl.getHeight(), inc);

        //Set up the timer bar
        timebar.setScaleY(1f);
        timebar.setVisibility(View.VISIBLE);
        scale = full_scale;
        overscale = false;

        //Default the score label for a new game
        set_score_label(0);
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

        ImageView bugman = get_goal();

        ImageView shoe = get_shoe();

        keep_inbound(cake, rl);

        double dist = PlacementActivity.check_collision(cake, bugman);

        //Print out if both are close enough (measured in pixels?)
        if (dist <= collideDistance){
            PlacementActivity.random_position(bugman, rl.getWidth(), rl.getHeight(), inc);

            StatsActivity.update_score();

            set_score_label(StatsActivity.get_score());
        }

        //Get more steps when the shoe is obtained
        double shoe_dist = PlacementActivity.check_collision(cake, shoe);
        if (shoe_dist <= collideDistance){
            PlacementActivity.random_position(shoe, rl.getWidth(), rl.getHeight(), inc);
            scale += shoe_inc;

            StatsActivity.update_shoe_count();
        }

        if (scale > full_scale){
            overscale = true;
        }

        StatsActivity.update_steps();

        timebar_update(time_inc);
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
    public ImageView get_goal(){
        return (ImageView) findViewById(R.id.bugman);
    }

    /*
     * Gets the timebar object
     */
    public ImageView get_timebar(){
        return (ImageView) findViewById(R.id.timebar);
    }

    /*
     * Gets the shoe object
     */
    public ImageView get_shoe(){
        return (ImageView) findViewById(R.id.shoe);
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
            obj.setX(obj.getX() + inc);
            obj.setImageResource(R.mipmap.chip_right);
        }else if (x < 0){
            obj.setX(obj.getX() - inc);
            obj.setImageResource(R.mipmap.chip_left);
        }

        if (y < 0){
            obj.setY(obj.getY() + inc);
            obj.setImageResource(R.mipmap.chip_down);
        }else if (y > 0){
            obj.setY(obj.getY() - inc);
            obj.setImageResource(R.mipmap.chip_up);
        }
    }

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
        if (scale > 0){
            scale -= time;
            timebar.setScaleY(scale);
        }
        if (scale <= 0){
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
                     * Overscale ensures that press-and-hold still
                     * operates as normal when the time bar is above 100%.
                     */
                    if (scale <= (full_scale - time_inc*2) || overscale) {
                        handle.postDelayed(this, interval);
                    }
                }
            };
        });
    }
}
