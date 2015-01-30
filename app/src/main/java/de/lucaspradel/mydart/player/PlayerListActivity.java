package de.lucaspradel.mydart.player;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import de.lucaspradel.mydart.R;
import de.lucaspradel.mydart.model.data.manager.PlayerManager;

public class PlayerListActivity extends ActionBarActivity implements PlayerManager.OnPlayersSelectedListener{

    private List<Player> players;
    private String[] playerNames;
    private ListView contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);
        contactList = (ListView) findViewById(R.id.lv_playerList);
        PlayerManager playerManager = new PlayerManager(getApplicationContext());
        playerManager.getPlayers(this); // Asynchrony
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player_list, menu);
        return true;
    }

    @Override
    public void onPlayersSelected(List<Player> players) {
        this.players = players;
        playerNames = new String[players.size()];
        for (int i = 0; i < players.size(); i++) {
            playerNames[i] = players.get(i).getUsername();
        }
        contactList
                .setAdapter(new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,
                        playerNames));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
