package de.lucaspradel.mydart.player;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.lucaspradel.mydart.R;
import de.lucaspradel.mydart.model.data.manager.PlayerManager;

public class CreatePlayerActivity extends ActionBarActivity {

    EditText userNameEt;
    String username;
    Button createPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_player);
        userNameEt = (EditText) findViewById(R.id.et_userName);
        username = userNameEt.getText().toString();
        createPlayer = (Button) findViewById(R.id.btn_createNewPlayer);
        createPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PlayerManager(getApplicationContext()).createPlayer(userNameEt.getText().toString());
                finish();
            }
        });
    }

}
