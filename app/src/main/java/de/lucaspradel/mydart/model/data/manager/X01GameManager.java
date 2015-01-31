package de.lucaspradel.mydart.model.data.manager;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import java.util.Date;

import de.lucaspradel.mydart.model.data.DatabaseHelper;
import de.lucaspradel.mydart.model.data.contract.DatabaseContract;

/**
 * Created by lpradel on 1/29/15.
 */
public class X01GameManager {

    long gameId;

    private DatabaseHelper dbHelper;

    public X01GameManager(Context context) {this.dbHelper = new DatabaseHelper(context);}

    public void createX01Game(int points, boolean doubleIn, boolean doubleOut, boolean bestOf, int winningLegs, int winningSets, Date date) {
        new CreateX01GameTask().execute(new X01Game(points, doubleIn, doubleOut, bestOf, winningLegs, winningSets, date));
    }

    public void insertX01Score(int playerId, int hitSection, int multiplier, int numberOfThrow, int scoringRound, int pointsLeft, boolean overthrown, int leg, int set) {

    }

    public void insertSetWinner(int playerId, int leg, int set) {

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
            gameId = index;
        }
    }

    private class InsertX01ScoreTask extends AsyncTask<X01Score, Void, Void> {
        @Override
        protected Void doInBackground(X01Score... scores) {
            X01Score score = scores[0];
            ContentValues cv = new ContentValues();
            cv.put(DatabaseContract.X01Scores.COLUMN_NAME_GAME, gameId);
            cv.put(DatabaseContract.X01Scores.COLUMN_NAME_PLAYER, score.getPlayerId());
            cv.put(DatabaseContract.X01Scores.COLUMN_NAME_HIT_SECTION, score.getHitSection());
            cv.put(DatabaseContract.X01Scores.COLUMN_NAME_MULTIPLIER, score.getMultiplier());
            cv.put(DatabaseContract.X01Scores.COLUMN_NAME_NO_OF_THROW, score.getNumberOfThrow());
            cv.put(DatabaseContract.X01Scores.COLUMN_NAME_SCORING_ROUND, score.getScoringRound());
            cv.put(DatabaseContract.X01Scores.COLUMN_NAME_POINTS_LEFT, score.getPointsLeft());
            cv.put(DatabaseContract.X01Scores.COLUMN_NAME_OVERTHROWN, score.isOverthrown());
            cv.put(DatabaseContract.X01Scores.COLUMN_NAME_SET, score.getLeg());
            cv.put(DatabaseContract.X01Scores.COLUMN_NAME_SET, score.getSet());
            dbHelper.getWritableDatabase().insert(DatabaseContract.X01Scores.TABLE_NAME, null, cv);
            return null;
        }

    }

    private class InsertSetWinnerTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int playerId = params[0];
            int leg = params[1];
            int set = params[2];
            ContentValues cv = new ContentValues();
            cv.put(DatabaseContract.X01GamesPlayersSets.COLUMN_NAME_GAME, gameId);
            cv.put(DatabaseContract.X01GamesPlayersSets.COLUMN_NAME_PLAYER, playerId);
            cv.put(DatabaseContract.X01GamesPlayersSets.COLUMN_NAME_LEG, leg);
            cv.put(DatabaseContract.X01GamesPlayersSets.COLUMN_NAME_SET, set);
            dbHelper.getWritableDatabase().insert(DatabaseContract.X01GamesPlayersSets.TABLE_NAME, null, cv);
            return null;
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

    public class X01Score {
        private final int playerId;
        private final int hitSection;
        private final int multiplier;
        private final int numberOfThrow;
        private final int pointsLeft;
        private final int scoringRound;
        private final boolean overthrown;
        private final int leg;
        private final int set;

        public X01Score(int playerId, int hitSection, int multiplier, int numberOfThrow, int scoringRound, int pointsLeft, boolean overthrown, int leg, int set) {
            this.playerId = playerId;
            this.hitSection = hitSection;
            this.multiplier = multiplier;
            this.numberOfThrow = numberOfThrow;
            this.scoringRound = scoringRound;
            this.pointsLeft = pointsLeft;
            this.overthrown = overthrown;
            this.leg = leg;
            this.set = set;
        }
        public int getPlayerId() {
            return playerId;
        }
        public int getHitSection() {
            return hitSection;
        }
        public int getMultiplier() {
            return multiplier;
        }
        public int getNumberOfThrow() {
            return numberOfThrow;
        }
        public int getPointsLeft() {
            return pointsLeft;
        }
        public int getScoringRound() {
            return scoringRound;
        }
        public boolean isOverthrown() {
            return overthrown;
        }
        public int getLeg() {
            return leg;
        }
        public int getSet() {
            return set;
        }
    }
}
