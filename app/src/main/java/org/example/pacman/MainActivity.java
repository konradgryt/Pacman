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


public class MainActivity extends AppCompatActivity {
    GameView gameView;
    Game game;
    Timer mainLoop;
    Handler handler;
    boolean paused = false;
    boolean scheduled = false;

    public void setupGame() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        gameView =  findViewById(R.id.gameView);

        TextView textView = findViewById(R.id.points);
        Button pauseButton = findViewById(R.id.pause);

        game = new Game(this,textView);
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
        pauseButton.setOnClickListener((v) -> paused = (paused == true) ? false : true);
        runTimers();
    }

    public void runTimers() {
        TextView timeView = findViewById(R.id.time);
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                    timeView.setText(String.format("Time left: " + "%d", millisUntilFinished / 1000));
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

                        }, 0, 5);
                        //bird
                        mainLoop.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (!paused) {
                                    game.enemy.updateTarget(game.player);
                                    game.enemy.update();
                                    gameView.invalidate();
                                }
                            }
                        }, 0, 30);
                        scheduled = true;
                    }
            }

            public void onFinish() {
                setupGame();
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupGame();
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
            setupGame();
            Toast.makeText(this,"New Game clicked",Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
