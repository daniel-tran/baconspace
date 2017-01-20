package com.plectrum.heinrich.baconspace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.ImageView;

import java.util.ArrayList;

/*
 * This class manages the customisation functionality of the game.
 */
public class ReskinActivity extends AppCompatActivity {

    ArrayList<RadioButton> boot_skins = new ArrayList<>();
    ArrayList<RadioButton> goal_skins = new ArrayList<>();
    ArrayList<RadioButton> player_skins = new ArrayList<>();
    ArrayList<RadioButton> threat_skins = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reskin);

        //Find radio buttons and categorises them
        set_boot_skins();
        set_player_skins();
        set_goal_skins();
        set_threat_skins();

        //Checks the necessary radio buttons
        check(StatsActivity.get_boot_check());
        check(StatsActivity.get_goal_check());
        check(StatsActivity.get_player_check());
        check(StatsActivity.get_threat_check());
    }

    /*
     * Instantiates the entries of the boot skins
     */
    public void set_boot_skins(){
        if (boot_skins.size() <= 0) {
            boot_skins.add((RadioButton) findViewById(R.id.opt_shoe));
            boot_skins.add((RadioButton) findViewById(R.id.opt_flipper));
            boot_skins.add((RadioButton) findViewById(R.id.opt_skates));
        }
    }

    /*
     * Instantiates the entries of the player skins
     */
    public void set_player_skins(){
        if (player_skins.size() <= 0) {
            player_skins.add((RadioButton) findViewById(R.id.opt_humanoid));
            player_skins.add((RadioButton) findViewById(R.id.opt_chomper));
            player_skins.add((RadioButton) findViewById(R.id.opt_tank));
        }
    }

    /*
     * Instantiates the entries of the goal skins
     */
    public void set_goal_skins(){
        if (goal_skins.size() <= 0) {
            goal_skins.add((RadioButton) findViewById(R.id.opt_ham));
            goal_skins.add((RadioButton) findViewById(R.id.opt_hotdog));
            goal_skins.add((RadioButton) findViewById(R.id.opt_chicken));
        }
    }

    /*
     * Instantiates the entries of the threat skins
     */
    public void set_threat_skins(){
        if (threat_skins.size() <= 0){
            goal_skins.add((RadioButton) findViewById(R.id.opt_bomb));
            goal_skins.add((RadioButton) findViewById(R.id.opt_fire));
            goal_skins.add((RadioButton) findViewById(R.id.opt_spider));
        }
    }

    /*
     * Checks the button of a given ID when the screen is opened.
     * This makes it look like the settings have been saved
     */
    public void check(int button){
        RadioButton rb = (RadioButton) findViewById(button);
        rb.setChecked(true);
    }

    /*
     * Shoe skin is active
     */
    public void opt_shoe(View view){
        set_boot(R.mipmap.shoe, R.id.shoe);
    }

    /*
     * Flipper skin is active
     */
    public void opt_flipper(View view){
        set_boot(R.mipmap.flipper, R.id.opt_flipper);
    }

    public void opt_skates(View view){ set_boot(R.mipmap.skates, R.id.opt_skates); }

    public void opt_ham(View view){ set_goal(R.mipmap.ham, R.id.opt_ham); }

    public void opt_hotdog(View view){ set_goal(R.mipmap.hotdog, R.id.opt_hotdog); }

    public void opt_chicken(View view){ set_goal(R.mipmap.drumstick, R.id.opt_chicken); }

    public void opt_humanoid(View view){
        int[] arr = {R.mipmap.chip_down, R.mipmap.chip_up,
                R.mipmap.chip_left, R.mipmap.chip_right};
        set_player(arr, R.id.opt_humanoid);
    }

    public void opt_chomper(View view){
        int[] arr = {R.mipmap.chomp_down, R.mipmap.chomp_up,
                R.mipmap.chomp_left, R.mipmap.chomp_right};
        set_player(arr, R.id.opt_chomper);
    }

    public void opt_tank(View view){
        int[] arr = {R.mipmap.tank_down, R.mipmap.tank_up,
                R.mipmap.tank_left, R.mipmap.tank_right};
        set_player(arr, R.id.opt_tank);
    }

    public void opt_bomb(View view){ set_threat(R.mipmap.bomb, R.id.opt_bomb);}

    public void opt_fire(View view){ set_threat(R.mipmap.fire, R.id.opt_fire);}

    public void opt_spider(View view){ set_threat(R.mipmap.spider, R.id.opt_spider);}

    /*
     * Sets the game shoe skin as needed, and checks the option selected
     */
    public void set_boot(int img, int id){
        MainActivity.set_shoe_img(img);
        StatsActivity.set_boot_check(id);
    }

    /*
     * Sets the game goal skin as needed, and checks the option selected
     */
    public void set_goal(int img, int id){
        MainActivity.set_goal_img(img);
        StatsActivity.set_goal_check(id);
    }

    /*
     * Sets the game player skin as needed, and checks the option selected
     */
    public void set_player(int[] sprites, int id){
        MainActivity.set_player_img(sprites);
        StatsActivity.set_player_check(id);
    }

    public void set_threat(int img, int id){
        MainActivity.set_threat_img(img);
        StatsActivity.set_threat_check(id);
    }
}
