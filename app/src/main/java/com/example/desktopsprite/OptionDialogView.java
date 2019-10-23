package com.example.desktopsprite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;



public class OptionDialogView extends LinearLayout{

    private Context context;

    private DesktopSpriteManager desktopSpriteManager;
    private final WindowManager windowManager;
    private WindowManager.LayoutParams dialogParams;

    public int dialogWidth, dialogHeight;

    public OptionDialogView(Context context, DesktopSpriteManager desktopSpriteManager) {
        super(context);
        this.context = context;
        this.desktopSpriteManager = desktopSpriteManager;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.option_dialog_layout, this);

        LinearLayout view = findViewById(R.id.option_dialog_layout);
        dialogWidth = view.getLayoutParams().width;
        dialogHeight = view.getLayoutParams().height;
    }

    public void setOptionDialogParams(WindowManager.LayoutParams dialogParams) {
        this.dialogParams = dialogParams;
    }

    public void setTxt(String txt) {
        TextView textView = findViewById(R.id.alert_dialog_txt);
        textView.setText(txt);
    }

    public void setFirstButton(String txt){
        Button button = (Button)findViewById(R.id.alert_dialog_button1);
        button.setText(txt);
        button.setOnClickListener(new OnClickListener(){
            public void onClick(View v){

                desktopSpriteManager.hideOptionDialogView();
                int q = desktopSpriteManager.getQuestionNumber();
                if (q==4){
                    desktopSpriteManager.showActionOnWeather();
                }else if (q==1){
                    desktopSpriteManager.feed_complementary();
                }else{ }
            }
        });
    }

    public void setSecondButton(String txt){
        Button button = (Button)findViewById(R.id.alert_dialog_button2);
        button.setText(txt);
        button.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                desktopSpriteManager.hideOptionDialogView();
                int q = desktopSpriteManager.getQuestionNumber();
                if (q==4){
                    desktopSpriteManager.setBabyDeaultView();
                }else if (q==1){
                    desktopSpriteManager.feed_milk();
                }else{ }
            }
        });
    }

    public void setPosition(int x, int y, boolean left) {
        int leftSide = 0;
        if (left)   leftSide = - getWidth();
        if (!desktopSpriteManager.spriteShowing())  return;
        dialogParams.x = x + leftSide;
        dialogParams.y = y + 300;
        windowManager.updateViewLayout(this, dialogParams);
    }

}
