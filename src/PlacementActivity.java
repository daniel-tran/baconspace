package com.plectrum.heinrich.baconspace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/*
 * This class contains methods related to object placement and collision detection.
 *
 * move() and related methods are not found here, because they involve findViewById,
 * which is a system-provided non-static method, and therefore cannot be accessed
 * from an external context.
 */
public class PlacementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement);
    }

    /*
     * Randomises a position for obj, where the new position is
     * within the square defined by width and height, with margin offsets.
     */
    public static void random_position(View obj, int width, int height, float margin){
        obj.setX((float)(Math.random() * width) - margin);
        obj.setY((float)(Math.random() * height) - margin);

        /*
         * Due to the above calculation, positions can go beyond the minimum
         * margin. Force any coordinate to the minimum margin if this happens
         */


        if (obj.getX() < margin){
            obj.setX(margin);
        }

        if (obj.getY() < margin){
            obj.setY(margin);
        }
    }

    /*
     * Checks for proximity between player and obj.
     * Returns the closeness between said entities, in pixels presumably.
     */
    public static double check_collision(View player, View obj){
        //Gets your location on screen
        int[] you = new int[2];
        player.getLocationOnScreen(you);

        //Gets their location on screen
        int[] them = new int[2];
        obj.getLocationOnScreen(them);

        //Calculate distance between objects
        double distance = Math.sqrt( Math.pow(you[0] - them[0], 2) + Math.pow(you[1] - them[1], 2));

        return distance;
    }
}
