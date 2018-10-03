package org.example.pacman;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;
import android.widget.Button;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;

public class MainActivity extends AppCompatActivity {

    CountDownTimer countDownTimer;
    GameView gameView;
    Game game;
    Timer mainLoop;
    Handler handler;
    Button pauseButton;
    TextView textView;
    TextView textView2;

    public static String android_id;

    boolean initialized = false;
    boolean paused;
    boolean scheduled = false;
    long timeRemaining;

    public void setupGame() {
        if (initialized) {
            countDownTimer.cancel();
        }
        initialized = false;

        //timeRemaining = (timeRemaining >= 0) ? 70000 - Game.level * 10000 : 5000;
        timeRemaining = 60000;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        gameView =  findViewById(R.id.gameView);
        textView = findViewById(R.id.points);
        textView2 = findViewById(R.id.highscore);
        pauseButton = findViewById(R.id.pause);

        SharedPreferences sharedpreferences = getSharedPreferences(android_id, Context.MODE_PRIVATE);
        textView2.setText(String.format(getResources().getString(R.string.highscore) + "%d", sharedpreferences.getInt("highscore", 0)));

        paused = false;
        game = new Game(this,textView, textView2);
        game.setGameView(gameView);
        gameView.setGame(game);
        game.newGame();

        gameView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeTop() {
                game.player.setDirection((Direction.UP));
            }
            @Override
            public void onSwipeRight() {
                game.player.setDirection((Direction.RIGHT));
            }
            @Override
            public void onSwipeLeft() {
                game.player.setDirection((Direction.LEFT));
            }
            @Override
            public void onSwipeBottom() {
                game.player.setDirection((Direction.DOWN));
            }
        });
        pauseButton.setOnClickListener((v) -> pauseButtonHandler());
        runTimers();

    }

    public void pauseButtonHandler() {
        if (!paused) {
            paused = true;
            countDownTimer.cancel();
            pauseButton.setText(String.format("Continue"));
        } else {
            paused = false;
            initialized = false;
            runTimers();
            pauseButton.setText(String.format("Pause"));
        }
    }

    public void runTimers() {
        TextView timeView = findViewById(R.id.time);
        if (!initialized) {
            initialized = true;
            countDownTimer = new CountDownTimer(timeRemaining, 1000) {
                public void onTick(long millisUntilFinished) {
                    if (!paused) {
                        timeRemaining = millisUntilFinished;
                        timeView.setText(String.format("Time left: " + "%d", timeRemaining / 1000));
                        if (!scheduled) {
                            mainLoop = new Timer();
                            handler = new Handler();
                            //player
                            mainLoop.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (!paused) {
                                        game.updateMovingGameObjects();
                                        gameView.invalidate();
                                    }
                                }

                            }, 0, 3);
                            //bird
                            mainLoop.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (!paused) {
                                        game.enemy.escape = game.allCoinsCollected();
                                        game.enemy.updateTarget(game.player);
                                        game.enemy.update();
                                        gameView.invalidate();
                                    }
                                }
                            }, 0, 70);
                            mainLoop.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (!paused) {
                                        game.enemy2.escape = game.allCoinsCollected();
                                        game.enemy2.updateTarget(game.player);
                                        game.enemy2.update();
                                        gameView.invalidate();
                                    }
                                }
                            }, 0, 30);
                            scheduled = true;
                        }
                    }
                }

                public void onFinish() {
                    setupGame();
                }
            }.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        android_id = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        super.onCreate(savedInstanceState);
        setupGame();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedpreferences = getSharedPreferences(android_id, Context.MODE_PRIVATE);
        if (Game.points > sharedpreferences.getInt("highscore",  0)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt("highscore", Game.points);
            editor.apply();
            editor.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this,"settings clicked",Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_newGame) {
            Game.points = 0;
            Game.level = 1;
            setupGame();
            Toast.makeText(this,"New Game clicked",Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
