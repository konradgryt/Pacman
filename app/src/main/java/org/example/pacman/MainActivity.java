package org.example.pacman;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    GameView gameView;
    Game game;
    Timer mainLoop;
    Handler handler;

    public void setupGame() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        gameView =  findViewById(R.id.gameView);
        TextView textView = findViewById(R.id.points);

        game = new Game(this,textView);
        game.setGameView(gameView);
        gameView.setGame(game);
        game.newGame();

        gameView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                game.player.setDirection((Direction.UP));
            }
            public void onSwipeRight() {
                game.player.setDirection((Direction.RIGHT));
            }
            public void onSwipeLeft() {
                game.player.setDirection((Direction.LEFT));
            }
            public void onSwipeBottom() {
                game.player.setDirection((Direction.DOWN));
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainLoop = new Timer();
        handler = new Handler();
        mainLoop.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 8);
        setupGame();
        super.onCreate(savedInstanceState);
    }

    private void TimerMethod()
    {
        this.runOnUiThread(Timer_Tick);
        this.runOnUiThread(EnemyTimer);
    }


    private Runnable Timer_Tick = new Runnable() {
        public void run() {
        Log.d("Lil","runnin");
        game.updateMovingGameObjects();
        gameView.invalidate();
        }
    };

    private Runnable EnemyTimer = new Runnable() {
        public void run() {
            game.enemy.updateTarget(game.player);
            game.enemy.update();
            gameView.invalidate();
            handler.postDelayed(EnemyTimer,8000);
            Log.d("Lil","enemy");
        }
    };

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
