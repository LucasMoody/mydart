package de.lucaspradel.mydart.model.data.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import de.lucaspradel.mydart.model.data.DatabaseHelper;
import de.lucaspradel.mydart.model.data.contract.DatabaseContract;
import de.lucaspradel.mydart.player.Player;

/**
 * Created by lpradel on 1/16/15.
 */
public class PlayerManager {
    public interface OnPlayersSelectedListener {
        public void onPlayersSelected(List<Player> players);
    }

    private DatabaseHelper dbHelper;

    public PlayerManager(Context ctxt) {
        this.dbHelper = new DatabaseHelper(ctxt);
    }

    public void createPlayer(String username) {
        CreatePlayerTask createPlayerTask = new CreatePlayerTask();
        createPlayerTask.execute(username);
    }

    public void getPlayers(OnPlayersSelectedListener listener) {
        new SelectPlayersTask(listener).execute();
    }

    private class CreatePlayerTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... names) {
            String username = names[0];
            ContentValues cv = new ContentValues();
            cv.put(DatabaseContract.Player.COLUMN_NAME_USER_NAME, username);
            dbHelper.getWritableDatabase().insert(
                    DatabaseContract.Player.TABLE_NAME, null, cv);
            return null;
        }

    }

    private class SelectPlayersTask extends AsyncTask<Void, Void, List<Player>> {

        private OnPlayersSelectedListener listener;

        public SelectPlayersTask (OnPlayersSelectedListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Player> doInBackground(Void... params) {
            List<Player> result = new ArrayList<Player>();
            Cursor cursor = dbHelper.getReadableDatabase().query(
                    DatabaseContract.Player.TABLE_NAME,
                    new String[] { DatabaseContract.Player._ID,
                            DatabaseContract.Player.COLUMN_NAME_USER_NAME}, null,
                    null, null, null, null);
            while (cursor.moveToNext()) {
                result.add(new Player(cursor.getInt(0), cursor.getString(1)));
            }
            cursor.close();
            return result;
        }

        @Override
        protected void onPostExecute(List<Player> players) {
            listener.onPlayersSelected(players);
        }
    }

}
