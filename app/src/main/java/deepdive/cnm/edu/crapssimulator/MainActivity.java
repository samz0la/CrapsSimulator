package deepdive.cnm.edu.crapssimulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import deepdive.cnm.edu.crapssimulator.model.Game;
import deepdive.cnm.edu.crapssimulator.model.Game.Roll;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  private Game game;
  private MenuItem next;
  private MenuItem fast;
  private MenuItem pause;
  private MenuItem reset;
  private boolean running;
  private TextView wins;
  private TextView losses;
  private TextView percentage;
  private ListView rolls;
  private Thread runner;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Random rng = new SecureRandom();
    game = new Game(rng);
    wins = findViewById(R.id.wins);
    losses = findViewById(R.id.losses);
    percentage = findViewById(R.id.percentage);
    rolls =findViewById(R.id.rolls);
    updateTally(0,0);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.options, menu);
    next = menu.findItem(R.id.next);
    fast = menu.findItem(R.id.fast);
    pause = menu.findItem(R.id.pause);
    reset = menu.findItem(R.id.reset);

    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    next.setEnabled(!running);
    next.setVisible(!running);
    fast.setEnabled(!running);
    fast.setVisible(!running);
    pause.setEnabled(running);
    pause.setVisible(running);
    reset.setEnabled(!running);
    reset.setVisible(!running);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    boolean handled = true;
    switch (item.getItemId()) {
      case R.id.next:
        game.play();
        updateTally(game.getWins(), game.getLosses());
        updateRolls(game.getRolls());
        break;
      case R.id.fast:
        runFast(true);
        break;
      case R.id.pause:
        runFast(false);
        break;
      case R.id.reset:
        game = new Game(new SecureRandom());
        updateTally(game.getWins(), game.getLosses());
        updateRolls(game.getRolls());
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

  private void updateTally(int wins, int losses) {
    int total = wins + losses;
    double percentage = (total != 0) ? (100.0 * wins / total) : 0;
    this.wins.setText(getString(R.string.wins_format, wins));
    this.losses.setText(getString(R.string.losses_format, losses));
    this.percentage.setText(getString(R.string.percentage_format, percentage));


  }

private void updateRolls(List<Roll> rolls ) {
  ArrayAdapter<Roll> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rolls);
  this.rolls.setAdapter(adapter);
}

  private void runFast(boolean start) {
    running = start;
    if (start) {
      invalidateOptionsMenu();
      runner = new Runner();
      runner.start();
    }else {
      runner = null;
    }
  }

  private class Runner extends Thread {

    private static final int TALLY_UPDATE_INTERVAL = 1000;
    private static final int ROLLS_UPDATE_INTERVAL = 10000;

    @Override
    public void run() {
      int count = 0;
      while (running) {
        game.play();
        count++;
        if (count % TALLY_UPDATE_INTERVAL ==0) {
          int wins = game.getWins();
          int losses = game.getLosses();
          runOnUiThread(() -> updateTally(wins, losses));
        }
        if (count % ROLLS_UPDATE_INTERVAL == 0) {
          List<Roll> rolls = game.getRolls();
          runOnUiThread(() -> updateRolls(rolls));
        }
      }
      runOnUiThread(() -> {
        int wins = game.getWins();
        int losses = game.getLosses();
          updateTally(wins, losses);
        List<Roll> rolls = game.getRolls();
          updateRolls(rolls);
          invalidateOptionsMenu();

        }
      );
    }
  }
}
