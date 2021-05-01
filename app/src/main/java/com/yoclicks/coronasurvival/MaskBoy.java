package com.yoclicks.coronasurvival;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.yoclicks.coronasurvival.GameView.screenRatioX;
import static com.yoclicks.coronasurvival.GameView.screenRatioY;

/*import static com.yoclicks.coronasurvival.GameView.screenRatioX;
import static com.yoclicks.coronasurvival.GameView.screenRatioY;*/

public class MaskBoy {
    public boolean isGoingUp = false;
    public int toShoot = 0;
    int x,y,width,height,wingcounter = 0, shootCounter = 1;
    Bitmap body1,body2, shoot1,shoot2, dead;

    private GameView gameView;

    MaskBoy (GameView gameView, int screenY, Resources res){

        this.gameView = gameView;

        body1= BitmapFactory.decodeResource(res,R.drawable.doc1);
        body2= BitmapFactory.decodeResource(res,R.drawable.doc2);

        width =body1.getWidth();
        height =body1.getHeight();

        width/= 3;
        height/= 3;

       width *= (int) screenRatioX;
        height *= (int) screenRatioY;

        body1 = Bitmap.createScaledBitmap(body1,width,height,false);
        body2 = Bitmap.createScaledBitmap(body2,width,height,false);

        shoot1 = BitmapFactory.decodeResource(res, R.drawable.docgun1);
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.docgun2);


        shoot1 = Bitmap.createScaledBitmap(shoot1,width,height,false);
        shoot2 = Bitmap.createScaledBitmap(shoot2,width,height,false);


        dead = BitmapFactory.decodeResource(res, R.drawable.docdead);
        dead = Bitmap.createScaledBitmap(dead, width,height,false);



        y =screenY/2;
        x = (int) (64 * screenRatioX);        /*differ by screen of mobile*/

    }

    Bitmap getFlight ()
    {
        if (toShoot != 0) {
            if (shootCounter == 1) {
                shootCounter ++;
                return shoot1;
            }
            if (shootCounter == 2) {
                shootCounter ++;
                return shoot1;
            }
            if (shootCounter == 3) {
                shootCounter ++;
                return shoot2;
            }
            if (shootCounter == 4) {
                shootCounter ++;
                return shoot2;
            }
            shootCounter =1;
            toShoot--;
            gameView.newBullet();

                return shoot1;
            }


        if (wingcounter == 0){

            wingcounter ++;
            return body1;
        }
        wingcounter--;
        return body2;
    }

    Rect getCollisionShape () {
        return  new Rect(x,y, x + width, y + height);
    }

    Bitmap getDead () {

        return dead;
    }


}
