package de.lucaspradel.mydart.game.x01;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.lucaspradel.mydart.R;
import de.lucaspradel.mydart.model.data.manager.PlayerManager;
import de.lucaspradel.mydart.player.Player;

public class PrepareX01Activity extends ActionBarActivity implements PlayerManager.OnPlayersSelectedListener, View.OnClickListener{

    private List<Player> players;
    private String[] playerNames;
    private Button addPlayer;
    private ListView contactList;
    private EditText playtypeET;
    private int playType = 301;
    private EditText setConfiguration;
    private EditText legConfiguration;
    private Player player1;
    private Player player2;
    private TextView player2Name;
    private TextView player1Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_x01);
        findViewsById();
        PlayerManager playerManager = new PlayerManager(getApplicationContext());
        playerManager.getPlayers(this); // Asynchrony
    }

    private void findViewsById() {
        this.player1Name = (TextView) findViewById(R.id.tv_player1);
        player1Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player1Name.setText("");
                player1 = null;
            }
        });
        this.player2Name = (TextView) findViewById(R.id.tv_player2);
        player2Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player2Name.setText("");
                player2 = null;
            }
        });
        this.addPlayer = (Button) findViewById(R.id.btn_add_contacts);
        addPlayer.setOnClickListener(this);
        this.contactList = (ListView) findViewById(R.id.contact_list);
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (player1 == null) {
                    player1 = players.get(position);
                    player1Name.setText(player1.getUsername());
                } else if (player2 == null) {
                    player2 = players.get(position);
                    player2Name.setText(player2.getUsername());
                }
            }
        });
        this.playtypeET = (EditText) findViewById(R.id.et_choosePlayType);
        playtypeET.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(""))
                    playType = 301;
                else
                    playType = Integer.valueOf(s.toString());
                addPlayer.setText("Spiele " + playType);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

        });
        setConfiguration = (EditText) findViewById(R.id.et_chooseNumberOfSets);
        legConfiguration = (EditText) findViewById(R.id.et_chooseNumberOfLegs);
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
    public void onClick(View v) {
        //SparseBooleanArray checked = contactList.getCheckedItemPositions();
        if (player1 == null || player2 == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Es müssen zwei Spieler ausgewählt sein!", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        int[] selectedPlayerIds = new int[2];
        String[] selectedPlayerNames = new String[2];
        selectedPlayerIds[0] = player1.getId();
        selectedPlayerNames[0] = player1.getUsername();
        selectedPlayerIds[1] = player2.getId();
        selectedPlayerNames[1] = player2.getUsername();
        Intent intent = new Intent(getApplicationContext(), X01Activity.class);
        Bundle bundle = new Bundle();
        bundle.putIntArray("playerIds", selectedPlayerIds);
        bundle.putStringArray("playerNames", selectedPlayerNames);
        bundle.putInt("playType", playType);
        int set;
        String setText = setConfiguration.getText().toString();
        if (setText.equals("")) {
            set = 1;
        } else {
            set = Integer.valueOf(setText);
        }
        int leg;
        String legText = legConfiguration.getText().toString();
        if (legText.equals("")) {
            leg = 1;
        } else {
            leg = Integer.valueOf(legText);
        }
        bundle.putInt("legs", leg);
        bundle.putInt("sets", set);
        bundle.putBoolean("bestOf", true);
        bundle.putBoolean("doubleIn", false);
        bundle.putBoolean("doubleOut", false);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
