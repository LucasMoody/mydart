package de.lucaspradel.mydart.model.data.manager;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import de.lucaspradel.mydart.model.data.DatabaseHelper;
import de.lucaspradel.mydart.model.data.contract.DatabaseContract;

/**
 * Created by lpradel on 2/4/15.
 */
public class StatisticsManager {

    private final DatabaseHelper dbHelper;

    public StatisticsManager(Context ctxt) {
        this.dbHelper = new DatabaseHelper(ctxt);
    }

    private class SelectPlayersAvgTask extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Void... params) {
            return dbHelper.getReadableDatabase().rawQuery("SELECT "
                    + DatabaseContract.Player.COLUMN_NAME_USER_NAME + ", AVG("
                    + DatabaseContract.X01Scores.COLUMN_NAME_HIT_SECTION + " * "
                    + DatabaseContract.X01Scores.COLUMN_NAME_MULTIPLIER + ") FROM "
                    + DatabaseContract.Player.TABLE_NAME + ", "
                    + DatabaseContract.X01Scores.TABLE_NAME + " WHERE "
                    + DatabaseContract.Player._ID + " = "
                    + DatabaseContract.X01Scores.COLUMN_NAME_PLAYER + " GROUPBY " + DatabaseContract.X01Scores.COLUMN_NAME_PLAYER + ";", null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            Log.i("DB", cursor.getString(0) +" "+ cursor.getDouble(1));
        }
    }

}
