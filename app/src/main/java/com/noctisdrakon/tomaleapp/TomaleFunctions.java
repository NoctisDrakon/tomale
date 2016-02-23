package com.noctisdrakon.tomaleapp;

import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

/**
 * Created by NoctisDrakon on 14/02/2016.
 */
public class TomaleFunctions {

    /*public void addView()
    {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        imagencircular = new de.hdodenhof.circleimageview.CircleImageView(getApplication());
        imagencircular.setImageResource(R.drawable.tomalebg);

        layout = new LinearLayout(this);
        layout.addView(imagencircular); // And attach your objects

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        params.width=100;
        params.height=100;

        windowManager.addView(layout, params);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OnClickListener", "Clickeo D:");
                bounce = AnimationUtils.loadAnimation(getApplication(), R.anim.bounce);
                imagencircular.startAnimation(bounce);

            }
        });
    }*/

}
