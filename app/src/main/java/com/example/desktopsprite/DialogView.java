/*
 * Project - Desktop Sprite
 * COMP90018 Mobile Computing Systems Programming
 * Author - Yao Wang, Tong He, Dinghao Yong, Jianyu Yan, Ruilin Liu
 * Oct 2019, Semester 2
 */

package com.example.desktopsprite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * This class implements the view of the dialog
 * Including updating positions, updating text
 */

public class DialogView extends LinearLayout {

    private DesktopSpriteManager desktopSpriteManager;
    private final WindowManager windowManager;
    private WindowManager.LayoutParams dialogParams;

    public int dialogWidth, dialogHeight;

    public DialogView(Context context, DesktopSpriteManager desktopSpriteManager) {
        super(context);
        this.desktopSpriteManager = desktopSpriteManager;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.dialog_layout, this);

        LinearLayout view = findViewById(R.id.dialog_layout);
        dialogWidth = view.getLayoutParams().width;
        dialogHeight = view.getLayoutParams().height;
    }

    public void setDialogParams(WindowManager.LayoutParams dialogParams) {
        this.dialogParams = dialogParams;
    }

    public void setTxt(String txt) {
        TextView textView = findViewById(R.id.dialog_txt);
        textView.setText(txt);
    }

    /*
     * Set the option bar position
     * @param   x       the x coordinate of sprite (if left == true, left edge, if left == false, right edge)
     * @param   y       the y coordinate of sprite (top edge)
     * @param   left    if left == true, show the dialog on the left side of sprite
     */
    public void setPosition(int x, int y, boolean left) {
        int leftSide = 0;
        if (left)   leftSide = - getWidth();
        if (!desktopSpriteManager.spriteShowing())  return;
        dialogParams.x = x + leftSide;
        dialogParams.y = y + 300;
        windowManager.updateViewLayout(this, dialogParams);
    }
}
