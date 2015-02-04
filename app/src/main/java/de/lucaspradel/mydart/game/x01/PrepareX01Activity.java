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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_x01);
        findViewsById();
        PlayerManager playerManager = new PlayerManager(getApplicationContext());
        playerManager.getPlayers(this); // Asynchrony
    }

    private void findViewsById() {
        this.addPlayer = (Button) findViewById(R.id.btn_add_contacts);
        addPlayer.setOnClickListener(this);
        this.contactList = (ListView) findViewById(R.id.contact_list);
        contactList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
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
                                          int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {}

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
                        android.R.layout.simple_list_item_multiple_choice,
                        playerNames));
    }

    @Override
    public void onClick(View v) {
        SparseBooleanArray checked = contactList.getCheckedItemPositions();
        int[] selectedPlayerIds = new int[checked.size()];
        String[] selectedPlayerNames = new String[checked.size()];
        for (int i = 0; i < checked.size(); i++) {
            int position = checked.keyAt(i);
            selectedPlayerIds[i] = players.get(position).getId();
            selectedPlayerNames[i] = players.get(position).getUsername();
        }
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
