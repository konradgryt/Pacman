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
    Button pauseButton;
    TextView textView;
    TextView textView2;

    private Timer pacmanTimer;
    private Timer enemy1Timer;
    private Timer enemy2Timer;
    private Timer enemy3Timer;

    public static String android_id;

    boolean initialized = false;
    boolean paused;
    long timeRemaining;

    public void setupGame() {
        if (initialized) {
            countDownTimer.cancel();
        }
        initialized = false;
        timeRemaining = (timeRemaining >= 0) ? 70000 - Game.level * 10000 : 5000;
        paused = false;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        gameView =  findViewById(R.id.gameView);
        textView = findViewById(R.id.points);
        textView2 = findViewById(R.id.highscore);
        pauseButton = findViewById(R.id.pause);

        SharedPreferences sharedpreferences = getSharedPreferences(android_id, Context.MODE_PRIVATE);
        textView2.setText(String.format(getResources().getString(R.string.highscore) + "%d", sharedpreferences.getInt("highscore", 0)));

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
        runCountdown();
    }

    public void pauseButtonHandler() {
        if (!paused) {
            paused = true;
            countDownTimer.cancel();
            pauseButton.setText(String.format("Continue"));
        } else {
            paused = false;
            initialized = false;
            runCountdown();
            pauseButton.setText(String.format("Pause"));
        }
    }

    public void runCountdown() {
        TextView timeView = findViewById(R.id.time);
        if (!initialized) {
            initialized = true;
            countDownTimer = new CountDownTimer(timeRemaining, 1000) {
                public void onTick(long millisUntilFinished) {
                    if (!paused) {
                        timeRemaining = millisUntilFinished;
                        timeView.setText(String.format("Time left: " + "%d", timeRemaining / 1000));
                    }
                }
                public void onFinish() {
                    setupGame();
                }
            }.start();
        }
    }

    private Runnable playerMovement = new Runnable() {
        public void run() {
            if (!paused) {
                game.updateMovingGameObjects();
                gameView.invalidate();
            }
        }
    };
    private Runnable enemyMovement = new Runnable() {
        public void run() {
            if (!paused) {
                game.enemy.escape = game.allCoinsCollected();
                game.enemy.updateTarget(game.player);
                game.enemy.update();
                gameView.invalidate();
            }
        }
    };
    private Runnable enemy2Movement = new Runnable() {
        public void run() {
            if (!paused) {
                game.enemy2.escape = game.allCoinsCollected();
                game.enemy2.updateTarget(game.player);
                game.enemy2.update();
                gameView.invalidate();
            }
        }
    };
    private Runnable enemy3Movement = new Runnable() {
        public void run() {
            if (!paused) {
                game.enemy3.escape = game.allCoinsCollected();
                game.enemy3.updateTarget(game.player);
                game.enemy3.update();
                gameView.invalidate();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        android_id = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        pacmanTimer = new Timer();
        pacmanTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(playerMovement);
            }

        }, 0, 3);
        enemy1Timer = new Timer();
        enemy1Timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(enemyMovement);
            }

        }, 0, 70);
        enemy2Timer = new Timer();
        enemy2Timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(enemy2Movement);
            }

        }, 0, 35);
        enemy3Timer = new Timer();
        enemy3Timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(enemy3Movement);
            }

        }, 0, 10);
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
