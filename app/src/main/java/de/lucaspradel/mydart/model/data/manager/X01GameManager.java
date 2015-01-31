package de.lucaspradel.mydart.model.data.manager;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import java.util.Date;

import de.lucaspradel.mydart.model.data.DatabaseHelper;
import de.lucaspradel.mydart.model.data.contract.DatabaseContract;

/**
 * Created by lpradel on 1/29/15.
 */
public class X01GameManager {

    private OnGameCreatedListener onGameCreatedList;

    public interface OnGameCreatedListener {
        public void onGameCreated(Long index);
    }

    private DatabaseHelper dbHelper;

    public X01GameManager(Context context) {this.dbHelper = new DatabaseHelper(context);}

    public void createX01Game(int points, boolean doubleIn, boolean doubleOut, boolean bestOf, int winningLegs, int winningSets, Date date, OnGameCreatedListener listener) {
        this.onGameCreatedList = listener;
        new CreateX01GameTask().execute(new X01Game(points, doubleIn, doubleOut, bestOf, winningLegs, winningSets, date));

    }

    private class CreateX01GameTask extends AsyncTask<X01Game, Void, Long> {
        @Override
        protected Long doInBackground(X01Game... games) {
            X01Game game = games[0];
            ContentValues cv = new ContentValues();
            cv.put(DatabaseContract.X01Games.COLUMN_NAME_POINTS, game.getPoints());
            cv.put(DatabaseContract.X01Games.COLUMN_NAME_DOUBLE_IN, game.isDoubleIn());
            cv.put(DatabaseContract.X01Games.COLUMN_NAME_DOUBLE_OUT, game.isDoubleOut());
            cv.put(DatabaseContract.X01Games.COLUMN_NAME_BEST_OF, game.isBestOf());
            cv.put(DatabaseContract.X01Games.COLUMN_NAME_WINNING_LEGS, game.getWinningLegs());
            cv.put(DatabaseContract.X01Games.COLUMN_NAME_WINNING_SETS, game.getWinningSets());
            cv.put(DatabaseContract.X01Games.COLUMN_NAME_DATE, game.getDate().getTime()/1000);
            return dbHelper.getWritableDatabase().insert(DatabaseContract.X01Games.TABLE_NAME,null, cv);
        }

        @Override
        protected void onPostExecute(Long index) {
            onGameCreatedList.onGameCreated(index);
        }
    }
    private class X01Game {
        private final int points;
        private final boolean doubleIn;
        private final boolean doubleOut;
        private final boolean bestOf;
        private final int winningLegs;
        private final Date date;
        private final int winningSets;

        public X01Game(int points, boolean doubleIn, boolean doubleOut, boolean bestOf, int winningLegs, int winningSets, Date date) {
            this.points = points;
            this.doubleIn = doubleIn;
            this.doubleOut = doubleOut;
            this.bestOf = bestOf;
            this.winningLegs = winningLegs;
            this.winningSets = winningSets;
            this.date = date;
        }
        public int getPoints() {
            return points;
        }
        public boolean isDoubleIn() {
            return doubleIn;
        }
        public boolean isDoubleOut() {
            return doubleOut;
        }
        public boolean isBestOf() {
            return bestOf;
        }
        public int getWinningLegs() {
            return winningLegs;
        }
        public int getWinningSets() {
            return winningSets;
        }
        public Date getDate() {
            return date;
        }
    }
}
