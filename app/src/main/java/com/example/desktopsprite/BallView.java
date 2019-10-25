package com.example.desktopsprite;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * This class implements the view of the toy ball
 * Including updating positions
 */

public class BallView extends LinearLayout {
    private DesktopSpriteManager desktopSpriteManager;
    private final WindowManager windowManager;
    private WindowManager.LayoutParams ballParams;

    private int screenWidth, screenHeight;

    public int ballWidth, ballHeight;

    public BallView(Context context, DesktopSpriteManager desktopSpriteManager, int screenWidth, int screenHeight) {
        super(context);
        this.desktopSpriteManager = desktopSpriteManager;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.ball_layout, this);

        LinearLayout view = findViewById(R.id.ball_layout);
        ballWidth = view.getLayoutParams().width;
        ballHeight = view.getLayoutParams().height;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void setBallParams(WindowManager.LayoutParams ballParams) {
        this.ballParams = ballParams;
    }

    /*
     * Set the toy ball position
     * @param   x       the x coordinate of the ball
     * @param   y       the y coordinate of the ball
     */
    public void setPosition(int x, int y) {
        if (!desktopSpriteManager.spriteShowing())  return;
        ballParams.x = x;
        ballParams.y = y;
        windowManager.updateViewLayout(this, ballParams);
    }

    /*
     * Update the toy ball position
     * @param   dx       the delta x coordinate of the ball
     * @param   dy       the delta y coordinate of the ball
     */
    public void updatePosition(float dx, float dy) {
        if (!desktopSpriteManager.spriteShowing())  return;
        ballParams.x += dx;
        ballParams.y += dy;
        if (ballParams.x < 0) ballParams.x = 0;
        if (ballParams.y < 0) ballParams.y = 0;
        if (ballParams.x > screenWidth) ballParams.x = screenWidth;
        if (ballParams.y > screenHeight) ballParams.y = screenHeight;
        windowManager.updateViewLayout(this, ballParams);

        Log.w("myApp", "x: " + ballParams.x);
        if (ballParams.x < screenWidth / 2)
            desktopSpriteManager.changeToSeeLeft();
        else
            desktopSpriteManager.changeToSeeRight();
    }
}
