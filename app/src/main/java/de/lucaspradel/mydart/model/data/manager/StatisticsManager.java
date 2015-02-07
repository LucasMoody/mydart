package de.lucaspradel.mydart.model.data.manager;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import de.lucaspradel.mydart.model.data.DatabaseHelper;
import de.lucaspradel.mydart.model.data.contract.DatabaseContract;

/**
 * Created by lpradel on 2/4/15.
 */
public class StatisticsManager {
    public interface OnPlayerAvgFinishedListener{
        public void onPlayerAvgFinished(List<PlayerAvgStatistics> list);
    }
    public interface OnPlayerFinishedPctFinishedListener{
        public void onPlayerFinishedPctFinished(List<Pair<String, Double>> list);
    }
    private final DatabaseHelper dbHelper;
    private OnPlayerAvgFinishedListener listener;
    private OnPlayerFinishedPctFinishedListener finishedPctListener;

    public StatisticsManager(Context ctxt) {
        this.dbHelper = new DatabaseHelper(ctxt);
    }

    public void getPlayersAvgs() {
        new SelectPlayersAvgTask().execute();
    }

    public void getPlayersFinishedPct() {new SelectPlayersFinishPctTask().execute();}

    public void setListener(OnPlayerAvgFinishedListener listener) {
        this.listener = listener;
    }

    public void setFinishedPctListener(OnPlayerFinishedPctFinishedListener finishedPctListener) {
        this.finishedPctListener = finishedPctListener;
    }

    private class SelectPlayersAvgTask extends AsyncTask<Void, Void, List<PlayerAvgStatistics>> {
        @Override
        protected List<PlayerAvgStatistics> doInBackground(Void... params) {
            List<PlayerAvgStatistics> result = new ArrayList<>();
            Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT "
                    + DatabaseContract.Player.COLUMN_NAME_USER_NAME + ", AVG("
                    + DatabaseContract.X01Scores.COLUMN_NAME_HIT_SECTION + " * "
                    + DatabaseContract.X01Scores.COLUMN_NAME_MULTIPLIER + ") FROM "
                    + DatabaseContract.Player.TABLE_NAME + ", "
                    + DatabaseContract.X01Scores.TABLE_NAME + " WHERE "
                    + DatabaseContract.Player.TABLE_NAME + "." + DatabaseContract.Player._ID + " = "
                    + DatabaseContract.X01Scores.TABLE_NAME + "."
                    + DatabaseContract.X01Scores.COLUMN_NAME_PLAYER + " GROUP BY "
                    + DatabaseContract.X01Scores.COLUMN_NAME_PLAYER + ";", null);
            while (cursor.moveToNext()) {
                result.add(new PlayerAvgStatistics(cursor.getString(0), cursor.getDouble(1)));
            }
            cursor.close();
            return result;
        }

        @Override
        protected void onPostExecute(List<PlayerAvgStatistics> list) {
            listener.onPlayerAvgFinished(list);
        }
    }

    private class SelectPlayersFinishPctTask extends AsyncTask<Void, Void, List<Pair<String, Double>>> {
        @Override
        protected List<Pair<String, Double>> doInBackground(Void... params) {
            List<Pair<String, Double>> result = new ArrayList<>();
            /*Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT "
                    + DatabaseContract.Player.COLUMN_NAME_USER_NAME + ", case when chanceNo / finishedNo is null then 0 else chanceNo / finishedNo end from"
                    + DatabaseContract.Player.TABLE_NAME + ", (SELECT " + DatabaseContract.X01Scores._ID
                    + ", case when tempCol is null then 0 else tempCol end as \"finishedNo\" from player left join (SELECT "
                    + DatabaseContract.Player.COLUMN_NAME_USER_NAME + ", Count(*) as \"tempCol\" FROM "
                    + DatabaseContract.X01Scores.TABLE_NAME + " WHERE (" + DatabaseContract.X01Scores.COLUMN_NAME_POINTS_LEFT
                    + " <= 20 or " +  DatabaseContract.X01Scores.COLUMN_NAME_POINTS_LEFT + " = 25) and "
                    + DatabaseContract.X01Scores.COLUMN_NAME_HIT_SECTION " * " + DatabaseContract.X01Scores.COLUMN_NAME_MULTIPLIER
                    + " = " + DatabaseContract.X01Scores.COLUMN_NAME_POINTS_LEFT + "GROUP BY " + DatabaseContract.X01Scores.COLUMN_NAME_PLAYER
                    + ") as temp on " + DatabaseContract.Player._ID " = temp." + DatabaseContract.X01Scores.COLUMN_NAME_PLAYER
                    + ")" */
            Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select username, finishedNo, chanceNo from player, (Select _id, case when tempCol is null then 0 else tempCol end as \"finishedNo\" from player left join (SELECT player, Count(*)  as \"tempCol\" FROM x01scores WHERE (pointsleft < 20 or pointsleft = 25) and hitsection * multiplier = pointsleft GROUP BY player) as temp on player._id = temp.player) as finishedTable, (Select _id, case when tempCol is null then 0 else tempCol end as \"chanceNo\" from player left join (SELECT player, Count(*)  as \"tempCol\" FROM x01scores WHERE pointsleft < 20 or pointsleft = 25 GROUP BY player) as temp on player._id = temp.player) as chanceTable where player._id = finishedTable._id and chanceTable._id = player._id;",null);
            double finishNo;
            double chanceNo;
            while (cursor.moveToNext()) {
                finishNo = (double) cursor.getInt(1);
                chanceNo = (double) cursor.getInt(2);

                result.add(new Pair<String, Double>(cursor.getString(0), finishNo / chanceNo));
            }
            cursor.close();
            return result;
        }

        /*select username, case when chanceNo / finishedNo is null then 0 else chanceNo / finishedNo end as finishedPct from player,
        (Select _id, case when tempCol is null then 0 else tempCol end as "finishedNo" from player left join (SELECT player, Count(*)  as "tempCol" FROM x01scores
        WHERE
                (pointsleft < 20 or pointsleft = 25) and hitsection * multiplier = pointsleft
        GROUP BY player) as temp on player._id = temp.player) as finishedTable,
        (Select _id, case when tempCol is null then 0 else tempCol end as "ChanceNo" from player left join (SELECT player, Count(*)  as "tempCol" FROM x01scores
        WHERE
        pointsleft < 20 or pointsleft = 25
        GROUP BY player) as temp on player._id = temp.player) as chanceTable
        where player._id = finishedTable._id and chanceTable._id = player._id;*/

        @Override
        protected void onPostExecute(List<Pair<String, Double>> list) {
            finishedPctListener.onPlayerFinishedPctFinished(list);
        }
    }

    public class PlayerAvgStatistics{
        private final String username;
        private final double avg;

        public PlayerAvgStatistics(String username, double avg) {
            this.username = username;
            this.avg = avg;
        }

        public String getUsername() {
            return username;
        }

        public double getAvg() {
            return avg;
        }
    }

}
