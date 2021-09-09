package com.example.flappybird;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class GameManager extends AppCompatActivity {
    BgImage bgImage;
    FlyingBird bird;
    ArrayList<TubeCollection> tubeCollections;
    BitmapElement bomb, money, shield, circleShield;
    Random rand;
    int scoreCount, scoreWhenBirdTouchTheShield, winningTube, scoreWhenTheBirdIsTouchingTheMoney, moneyCounter, isTheFirstTouch;
    Paint designPaint;
    boolean isObstacleOn, isShieldOn, isOverBecauseOfBomb;
    static int pauseVelocity, gameState, bestScore;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    public GameManager() {
        bgImage = new BgImage();
        bird = new FlyingBird();
        tubeCollections = new ArrayList<>();
        bomb = new BitmapElement();
        money = new BitmapElement();
        shield = new BitmapElement();
        circleShield = new BitmapElement();
        rand = new Random();
        generateTubeObject();
        initScoreVariables();
    }

    public void initScoreVariables() {
        scoreCount = 0;
        winningTube = 0;
        gameState = 0;
        isObstacleOn = true;
        isShieldOn = false;
        isTheFirstTouch = 0;
        isOverBecauseOfBomb = false;
        pauseVelocity = 1;
        designPaint = new Paint();
        designPaint.setColor(Color.YELLOW);
        designPaint.setTextSize(200);
        designPaint.setStyle(Paint.Style.FILL);
        designPaint.setFakeBoldText(true);
        designPaint.setShadowLayer(5.0f, 20.0f, 20.0f, Color.BLACK);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.keepSynced(true);
            userID = user.getUid();
            getDataFromDB();
        }
    }

    public void generateTubeObject() {
        for (int j = 0; j < AppHolder.tube_numbers; j++) {
            int tubeX = AppHolder.SCREEN_WIDTH_X + j * AppHolder.tubeDistance;
            int upTubeCollectionY = AppHolder.minimumTubeCollection_y;
            rand.nextInt(AppHolder.maximumTubeCollection_y - AppHolder.minimumTubeCollection_y + 1);
            TubeCollection tubeCollection = new TubeCollection(tubeX, upTubeCollectionY);
            tubeCollections.add(tubeCollection);
        }
    }

    public void scrollingTube(Canvas canvas) {
        if (gameState == 1) {
            tubeObstacleOn(isObstacleOn);
            if (tubeCollections.get(winningTube).getXtube() < bird.getX() - AppHolder.bitmapControl.getTubeWidth()) {
                scoreCount++;
                winningTube++;
                AppHolder.getSoundPlay().playScore();
                if (winningTube > AppHolder.tube_numbers - 1) {
                    winningTube = 0;
                }
            }
            for (int j = 0; j < AppHolder.tube_numbers; j++) {
                if (tubeCollections.get(j).getXtube() < -AppHolder.getBitmapControl().getTubeWidth()) {
                    tubeCollections.get(j).setXtube(tubeCollections.get(j).getXtube() + AppHolder.tube_numbers * AppHolder.tubeDistance);
                    int upTubeCollectionY = AppHolder.minimumTubeCollection_y + rand.nextInt(AppHolder.maximumTubeCollection_y - AppHolder.minimumTubeCollection_y + 1);
                    tubeCollections.get(j).setUpTubeCollection_Y(upTubeCollectionY);
                }
                tubeCollections.get(j).setXtube(tubeCollections.get(j).getXtube() - AppHolder.tubeVelocity * pauseVelocity);
                canvas.drawBitmap(AppHolder.getBitmapControl().getUpTube(), tubeCollections.get(j).getXtube(), tubeCollections.get(j).getUpTube_Y(), null);
                canvas.drawBitmap(AppHolder.getBitmapControl().getDownTube(), tubeCollections.get(j).getXtube(), tubeCollections.get(j).getDownTube_Y(), null);
            }
            canvas.drawText("" + scoreCount, (int) (AppHolder.SCREEN_WIDTH_X / 2.4), AppHolder.SCREEN_HEIGHT_Y / 8, designPaint);
        }
    }

    public void scrollingBomb(Canvas canvas) {

        int y = rand.nextInt(1 + AppHolder.SCREEN_HEIGHT_Y);
        int x = (AppHolder.SCREEN_WIDTH_X / 2 + tubeCollections.get(1).getXtube() + AppHolder.bombDistance*(1+rand.nextInt(3)));
        if (gameState == 1) {
            if (bomb.getX() < -AppHolder.getBitmapControl().getBombWidth()) {
                bomb.setY(y);
                bomb.setX(x);
            }
            if (isTheBirdTouchedTheCoin(bomb) && !isShieldOn) {
                isOverBecauseOfBomb = true;
                gameState = 2;
                gameOver();
            } else {
                bomb.setX(bomb.getX() - AppHolder.bombVelocity * pauseVelocity);
                canvas.drawBitmap(AppHolder.getBitmapControl().getBomb(), bomb.getX(), bomb.getY(), null);
            }
        }
    }

    public void scrollingMoney(Canvas canvas) {
        if (scoreCount >= 3) {
            if (gameState == 1) {
                int y = (tubeCollections.get(1).getDownTube_Y() - AppHolder.tubeGap / 2);
                int x = (tubeCollections.get(1).getXtube() + AppHolder.getBitmapControl().getTubeWidth() / 2 - AppHolder.getBitmapControl().getMoneyWidth() / 2);
                money.setY(y);
                money.setX(x);
                if (isTheBirdTouchedTheCoin(money)) {
                    scoreWhenTheBirdIsTouchingTheMoney = scoreCount;
                    isTheFirstTouch++;
                    if (isTheFirstTouch == 1)
                        moneyCounter++;
                }
                if (scoreWhenTheBirdIsTouchingTheMoney + 1 < scoreCount) {
                    canvas.drawBitmap(AppHolder.getBitmapControl().getMoney(), money.getX(), money.getY(), null);
                    isTheFirstTouch = 0;
                }
                money.setX(money.getX() - AppHolder.moneyVelocity * pauseVelocity);
            }
        }
    }

    public void scrollingShield(Canvas canvas) {
        int y = rand.nextInt(1 + AppHolder.SCREEN_HEIGHT_Y);
        int x = (AppHolder.SCREEN_WIDTH_X * 2 + AppHolder.SCREEN_WIDTH_X / 2 + tubeCollections.get(0).getXtube() + AppHolder.shieldDistance * (1 + rand.nextInt(3)));
        if (gameState == 1) {
            if (shield.getX() < -AppHolder.getBitmapControl().getShieldWidth()) {
                shield.setY(y);
                shield.setX(x);
            }
            if (isTheBirdTouchedTheCoin(shield)) {
                scoreWhenBirdTouchTheShield = scoreCount;
                isObstacleOn = false;
            }
            if (scoreWhenBirdTouchTheShield + 3 > scoreCount && scoreCount > 3) {
                circleShield.setX(bird.getX() + AppHolder.getBitmapControl().getBirdWidth() / 2 - AppHolder.getBitmapControl().getCircleShieldWidth() / 2);
                circleShield.setY(bird.getY() + AppHolder.getBitmapControl().getBirdHeight() / 2 - AppHolder.getBitmapControl().getCircleShieldHeight() / 2);
                canvas.drawBitmap(AppHolder.getBitmapControl().getCircleShield(), circleShield.getX(), circleShield.getY(), null);
                isShieldOn = true;
            } else if (scoreCount > 2) {
                isObstacleOn = true;
                isShieldOn = false;
                canvas.drawBitmap(AppHolder.getBitmapControl().getShield(), shield.getX(), shield.getY(), null);
            }
            shield.setX(shield.getX() - AppHolder.shieldVelocity * pauseVelocity);
        }
    }

    public void birdAnimation(Canvas canvas) {

        if (gameState == 1) {
            if (bird.getY() < (AppHolder.SCREEN_HEIGHT_Y - AppHolder.getBitmapControl().getBirdHeight()) || bird.getVelocity() < 0) {
                bird.setVelocity(bird.getVelocity() * pauseVelocity + AppHolder.gravityPull * pauseVelocity);
                bird.setY(bird.getY() + bird.getVelocity() * pauseVelocity);
            }
        }
        int currentFrame = bird.getCurrentFrame();
        canvas.drawBitmap(AppHolder.getBitmapControl().getBird(currentFrame), bird.getX(), bird.getY(), null);
        currentFrame++;
        if (currentFrame > FlyingBird.maximumFrame) {
            currentFrame = 0;
        }
        bird.setCurrentFrame(currentFrame);
    }

    public void backgroundAnimation(Canvas canvas) {
        bgImage.setX(bgImage.getX() - bgImage.getVelocity() * pauseVelocity);
        if (bgImage.getX() < -AppHolder.getBitmapControl().getBackgroundWidth())// if the image finish than set x to the beginning
        {
            bgImage.setX(0);
        }
        canvas.drawBitmap(AppHolder.getBitmapControl().getBackground(), bgImage.getX(), bgImage.getY(), null);
        if (bgImage.getX() < -(AppHolder.getBitmapControl().getBackgroundWidth() - AppHolder.SCREEN_WIDTH_X)) {
            canvas.drawBitmap(AppHolder.getBitmapControl().getBackground(), bgImage.getX() +
                    AppHolder.getBitmapControl().getBackgroundWidth(), bgImage.getY(), null);
        }
    }

    public void tubeObstacleOn(boolean bool) {
        if (bool) {
            if ((tubeCollections.get(winningTube).getXtube() < bird.getX() + AppHolder.getBitmapControl().getTubeWidth())
                    && (tubeCollections.get(winningTube).getUpTubeCollection_Y() > bird.getY()
                    || tubeCollections.get(winningTube).getDownTube_Y() < (bird.getY() + AppHolder.getBitmapControl().getBirdHeight()))
                    || (bird.getY() <= 0)) {
                gameState = 2;
                gameOver();
            }
        }
    }

    public boolean isTheBirdTouchedTheCoin(BitmapElement coin) {
        return ((bird.getY() + AppHolder.getBitmapControl().getBirdHeight() >= coin.getY() && bird.getY() <= coin.getY()) ||
                (coin.getY() + AppHolder.getBitmapControl().getShieldHeight() >= bird.getY() && bird.getY() >= coin.getY())) &&
                (((bird.getX() + AppHolder.getBitmapControl().getBirdWidth() >= coin.getX()) && bird.getX() < coin.getX()) ||
                        (coin.getX() + AppHolder.getBitmapControl().getShieldWidth() >= bird.getX() && bird.getX() > coin.getX()));
    }

    public void gameOver() {
        AppHolder.gameOver = true;
        AppHolder.getSoundPlay().playCrash();
        Context mContext = AppHolder.gameActivityContext;
        Intent mIntent = new Intent(mContext, GameOverActivity.class);
        mIntent.putExtra("score", scoreCount);
        addDataToDB();
        if (isOverBecauseOfBomb) {
            mIntent.putExtra("isOverBecauseOfBomb", isOverBecauseOfBomb);
        }
        mContext.startActivity(mIntent);
    }

    public void addDataToDB() {
        if (!AppHolder.guestMode) {
            reference.child(userID).child("moneyCount").setValue(moneyCounter);
            AppHolder.getUser().setMoneyCount(moneyCounter);
            if (scoreCount > bestScore) {
                bestScore = scoreCount;
                reference.child(userID).child("bestScore").setValue(bestScore);
                AppHolder.getUser().setBestScore(scoreCount);
            }
        }
    }

    public void getDataFromDB() {
        if (!AppHolder.guestMode) {
            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userProfile = snapshot.getValue(User.class);
                    bestScore = userProfile.getBestScore();
                    moneyCounter = userProfile.getMoneyCount();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

}


