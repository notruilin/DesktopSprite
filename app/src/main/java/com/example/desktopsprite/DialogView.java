package com.example.desktopsprite;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogView extends LinearLayout {
    private Context context;

    private DesktopSpriteManager desktopSpriteManager;
    private final WindowManager windowManager;
    private WindowManager.LayoutParams dialogParams;

    public int dialogWidth, dialogHeight;

    public DialogView(Context context, DesktopSpriteManager desktopSpriteManager) {
        super(context);
        this.context = context;
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

    public void setPosition(int x, int y, boolean left) {
        int leftSide = 0;
        if (left)   leftSide = - getWidth();
        if (!desktopSpriteManager.spriteShowing())  return;
        Log.w("myApp", "x: " + x);
        Log.w("myApp", "dialog width: " + getWidth());
        dialogParams.x = x + leftSide;
        dialogParams.y = y + 300;
        windowManager.updateViewLayout(this, dialogParams);
    }
}
