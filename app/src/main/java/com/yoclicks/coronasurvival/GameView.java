package com.yoclicks.coronasurvival;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying, isGameOver = false/*, isNextLevel = false*/;
    private int score = 0;
    public static int screenX, screenY;
    public static int scoremax;
    public static float screenRatioX, screenRatioY;
    private Paint paint,gpaint,spaint,quapaint;
    private CoronaZ[] coronaZS/*,coronaZS2*/;
    private SharedPreferences prefs;
    private Random random;
    private SoundPool soundPool;
    private List<Bullet> bullets;
    private int sandrop, qua_sound, co_dead ,hero_up /*hero_dead*/;
    private MaskBoy maskBoy;
    public Bitmap[] life = new Bitmap[2];
    private GameActivity activity;
    private Background background1, background2;
    private int lifeCounter = 5;


    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);


        this.activity = activity;
        prefs = activity.getSharedPreferences("Game", Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_GAME).build();

            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).build();

        } else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        sandrop = soundPool.load(activity, R.raw.san_drop_co, 4);
        qua_sound = soundPool.load(activity, R.raw.quarantine_co, 2);
        co_dead = soundPool.load(activity, R.raw.blood_co, 3);
        hero_up = soundPool.load(activity, R.raw.heroup,5);
        /*        hero_dead = soundPool.load(activity, R.raw.hero_dead_co,1);*/

        this.screenX = screenX;
        this.screenY = screenY;

       screenRatioX = 2340f / screenX;
       screenRatioY = 1080f / screenY;

        background1 = new Background(screenX +50, screenY, getResources());
        background2 = new Background(screenX +50, screenY, getResources());
        maskBoy = new MaskBoy(this, screenY , getResources());

        bullets = new ArrayList<>();

        background2.x = screenX+50;                        //it shows bck2 is not in screen now it placed after bck1
        paint = new Paint();
        gpaint= new Paint();
        quapaint = new Paint();
        spaint= new Paint();


        paint.setTextSize(120);
        paint.setColor(Color.WHITE);


       spaint.setColor(Color.YELLOW);
       spaint.setTextSize(130);

        quapaint.setColor(Color.RED);
        quapaint.setTypeface(Typeface.DEFAULT_BOLD);
        quapaint.setStyle(Paint.Style.FILL_AND_STROKE);
        quapaint.setTextSize(160);


        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.coshield);                /* life of masskboy*/
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.noshield);


        coronaZS = new CoronaZ[4];

        for (int i = 0; i < 4; i++) {
            CoronaZ coronaZ = new CoronaZ(getResources());
            coronaZS[i] = coronaZ;
        }

        random = new Random();

    }


    @Override
    public void run() {

        while (isPlaying) {
            update();
            try {
                draw();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sleep();


        }

    }

    private void update() {

        background1.x -= 5;                                               //update
        background2.x -= 5;


        if (background1.x + background1.background.getWidth() == 0) {
            background1.x = screenX +50;                                               //condition to move background continously
        }

        if (background2.x + background2.background.getWidth() == 0) {
            background2.x = screenX+50;
        }



       if (maskBoy.isGoingUp)       //to pull maskboy UpSide

            maskBoy.y -= 50 * screenRatioY;                                                   //update maskboy
        else
            maskBoy.y += 30 * screenRatioY;
        if (maskBoy.y < 0)
            maskBoy.y = 0;
        if (maskBoy.y > screenY - maskBoy.height)
            maskBoy.y = screenY - maskBoy.height;


        List<Bullet> trash = new ArrayList<>();

        for (Bullet bullet : bullets) {
            if (bullet.x > screenX)
                trash.add(bullet);
            bullet.x += 50;


            for (CoronaZ coronaZ : coronaZS) {
                if (Rect.intersects(coronaZ.getCollisionShape(), bullet.getCollisionShape())) {
                    score++;

                    if (!prefs.getBoolean("isMute", false))
                        soundPool.play(co_dead, 1, 1, 0, 0, 1);
                    coronaZ.x = -500;
                    bullet.x = screenX + 500;

                    coronaZ.wasShot = true;
                }
            }

        }


        for (Bullet bullet : trash)
            bullets.remove(bullet);

        for (CoronaZ coronaZ : coronaZS) {
            coronaZ.x -= coronaZ.speed;


            if ( coronaZ.x > 0 && coronaZ.x <10 || coronaZ.x ==0)  {            /* this is for life */

                lifeCounter --;

                if (lifeCounter == 0) {

                    isGameOver = true;
                }

                return;
            }

            if (coronaZ.x + coronaZ.width < 0) {






/*                int bound = 30;*/
                int bound = (int) ( 30 * screenRatioX);
                coronaZ.speed = random.nextInt(bound);

/*                if (coronaZ.speed < 10)
                    coronaZ.speed = 10;*/
                if (coronaZ.speed < 10 *screenRatioX)
                    coronaZ.speed = (int) ( 10 *screenRatioX);

                coronaZ.x = screenX +50;
                coronaZ.y = random.nextInt(screenY - coronaZ.height);


                coronaZ.wasShot = false;
              /*  isNextLevel = false;*/
            }


            if (Rect.intersects(coronaZ.getCollisionShape(), maskBoy.getCollisionShape())) {


                   isGameOver = true;
                    return;
                }

        }
    }


    private Bitmap draw() throws InterruptedException {

        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);



            for (CoronaZ coronaZ : coronaZS)
                canvas.drawBitmap(coronaZ.getCo1(), coronaZ.x, coronaZ.y, paint);

/*            if (score < 50) {
                isNextLevel = true;
                    for (CoronaZ coronaZ : coronaZS2)
                        canvas.drawBitmap(coronaZ.getCo2(), coronaZ.x, coronaZ.y, paint);
                }*/



            if (isGameOver) {
                isPlaying = false;
                canvas.drawBitmap(maskBoy.getDead(), maskBoy.x, maskBoy.y, paint);
                Thread.sleep(1000);

                if (!prefs.getBoolean("isMute", false))
                    soundPool.play(qua_sound, 1, 1, 1, 0, 1);

               canvas.drawText(" QUARANTINE", screenX / 4f, screenY / 2f, quapaint);
                getHolder().unlockCanvasAndPost(canvas);
                checkcsore();
                getScore();
                saveIfHighScore();
                waitBeforeExiting();
                return null;
            }


            canvas.drawBitmap(maskBoy.getFlight(), maskBoy.x, maskBoy.y, paint);
            if (lifeCounter == 1 || lifeCounter ==2)
            {
                gpaint.setColor(Color.RED);
                gpaint.setAlpha((int) (Math.random() * 100));
            }
            else{
                gpaint.setColor(Color.GREEN);
                gpaint.setAlpha(70);
            }

            RectF rect = new RectF(80 * lifeCounter,100,10,150);
            canvas.drawRoundRect(rect,10,10,gpaint);




            for (Bullet bullet : bullets)
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);

            canvas.drawText(score + "", screenX / 2f, 164, spaint);


            getHolder().unlockCanvasAndPost(canvas);

        }
        return null;
    }

    private void waitBeforeExiting() {

        try {
            Thread.sleep(2000);

            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
        public void checkcsore() {
        if (score >scoremax)
            scoremax =score;
        }
    private void getScore() {
        if (prefs.getInt("Score ", score) == score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("Score", score);
            editor.apply();
        }
    }
    private void saveIfHighScore() {
        if (prefs.getInt("HighScore ", 0) < scoremax) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("HighScore", scoremax);
            editor.apply();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(17);                                   //    <------study
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {

        isPlaying = true;

        thread = new Thread(this);
        thread.start();

    }

    public void pause() {

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if  (event.getX() < screenX / 2) {
                    maskBoy.isGoingUp = true;
                    if (isPlaying == true)
                    {
                        if (!prefs.getBoolean("isMute", false))
                            soundPool.play(hero_up, (float) 0.2, (float) 0.2, 2, 0, 1);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                maskBoy.isGoingUp = false;
                if (event.getX() > screenX / 2)
                    maskBoy.toShoot++;
        }
        return true;
    }

    public void newBullet() {

        if (!prefs.getBoolean("isMute", false))
            soundPool.play(sandrop, 1, 1, 0, 0, 1);

        Bullet bullet = new Bullet(getResources());
        bullet.x = maskBoy.x + maskBoy.width;
        bullet.y = maskBoy.y + (maskBoy.height / 2);
        bullets.add(bullet);
    }



}



