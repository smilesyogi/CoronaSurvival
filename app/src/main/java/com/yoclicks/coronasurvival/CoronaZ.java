package com.yoclicks.coronasurvival;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.yoclicks.coronasurvival.GameView.screenRatioX;
import static com.yoclicks.coronasurvival.GameView.screenRatioY;

public class CoronaZ {
    public int speed = 20;
    public boolean wasShot = true;
    int x = 0,y,width,height,coCounter = 1;
    Bitmap co1_1,co1_2,co1_3,co1_4,co2;      /*co3,co4,co5;*/

    CoronaZ(Resources res) {
        co1_1 = BitmapFactory.decodeResource(res,R.drawable.co1_1);
        co1_2 = BitmapFactory.decodeResource(res,R.drawable.co1_2);
        co1_3 = BitmapFactory.decodeResource(res,R.drawable.co1_3);
        co1_4 = BitmapFactory.decodeResource(res,R.drawable.co1_4);
        co2 = BitmapFactory.decodeResource(res,R.drawable.co2);



/*      co3 = BitmapFactory.decodeResource(res,R.drawable.co3);
        co4 = BitmapFactory.decodeResource(res,R.drawable.co4);
        co5 = BitmapFactory.decodeResource(res,R.drawable.co5);*/

        width = co1_1.getWidth();
        height = co1_1.getHeight();

        width /= 5;
        height /= 5;


        width *= (int) screenRatioX;
        height *= (int) screenRatioY;

        co1_1 = Bitmap.createScaledBitmap(co1_1, width, height, false);
        co1_2 = Bitmap.createScaledBitmap(co1_2, width, height, false);
        co1_3 = Bitmap.createScaledBitmap(co1_3, width, height, false);
        co1_4 = Bitmap.createScaledBitmap(co1_4, width, height, false);
        co2 = Bitmap.createScaledBitmap(co2, width *2, height*2, false);


/*      co3 = Bitmap.createScaledBitmap(co3, width, height, false);
        co4 = Bitmap.createScaledBitmap(co4, width, height, false);
        co5 = Bitmap.createScaledBitmap(co5, width, height, false);*/

        y = -height;

    }

    Bitmap getCo1() {
        if (coCounter == 1) {
            coCounter ++;
            return co1_1;
        }
        if (coCounter == 2) {
            coCounter ++;
            return co1_2;
        }
        if (coCounter == 3) {
            coCounter ++;
            return co1_3;
        }
        if (coCounter == 4) {
            coCounter ++;
            return co1_4;
        }
            coCounter = 1;
            return co1_1;
    }

    Bitmap getCo2() {

            return co2;

    }

    Rect getCollisionShape () {
        return  new Rect(x,y, x + width, y + height);
    }

}
