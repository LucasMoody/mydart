package de.lucaspradel.mydart.model.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import de.lucaspradel.mydart.model.data.contract.DatabaseContract;

/**
 * Created by lpradel on 1/16/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydartdb.db";
    private static final int SCHEMA = 1;
    private static final String COMMA_SEP = ", ";
    private static final String SQL_CREATE_PLAYERS = "CREATE TABLE "
            + DatabaseContract.Player.TABLE_NAME + " ("
            + DatabaseContract.Player._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
            + COMMA_SEP + DatabaseContract.Player.COLUMN_NAME_USER_NAME
            + " TEXT NOT NULL UNIQUE" + COMMA_SEP
            + DatabaseContract.Player.COLUMN_NAME_IMAGE + " TEXT" + ");";
    private static final String SQL_CREATE_TRAINING = "CREATE TABLE "
            + DatabaseContract.Training.TABLE_NAME + " ("
            + DatabaseContract.Training._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP
            + DatabaseContract.Training.COLUMN_NAME_PLAYER + " INTEGER NOT NULL"
            + COMMA_SEP + DatabaseContract.Training.COLUMN_NAME_DATE
            + " INTEGER NOT NULL" + COMMA_SEP + "FOREIGN KEY ("
            + DatabaseContract.Training.COLUMN_NAME_PLAYER + ") REFERENCES "
            + DatabaseContract.Player.TABLE_NAME + " ("
            + DatabaseContract.Player._ID + "));";
    private static final String SQL_CREATE_TRAINING_SESSION = "CREATE TABLE "
            + DatabaseContract.TrainingSession.TABLE_NAME + " ("
            + DatabaseContract.TrainingSession._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP
            + DatabaseContract.TrainingSession.COLUMN_NAME_TRAINING
            + " INTEGER NOT NULL" + COMMA_SEP
            + DatabaseContract.TrainingSession.COLUMN_NAME_TRAINED_SECTION
            + " INTEGER NOT NULL" + COMMA_SEP
            + DatabaseContract.TrainingSession.COLUMN_NAME_HIT_SECTION
            + " INTEGER NOT NULL" + COMMA_SEP
            + DatabaseContract.TrainingSession.COLUMN_NAME_MULTIPLIER
            + " INTEGER NOT NULL" + COMMA_SEP
            + DatabaseContract.TrainingSession.COLUMN_NAME_NO_OF_THROW
            + " INTEGER NOT NULL" + COMMA_SEP + "FOREIGN KEY ("
            + DatabaseContract.TrainingSession.COLUMN_NAME_TRAINING
            + ") REFERENCES " + DatabaseContract.Training.TABLE_NAME + " ("
            + DatabaseContract.Training._ID + "));";
    private static final String SQL_CREATE_X01_GAMES = "CREATE TABLE "
            + DatabaseContract.X01Games.TABLE_NAME + " ("
            + DatabaseContract.X01Games._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
            + COMMA_SEP + DatabaseContract.X01Games.COLUMN_NAME_POINTS + " INTEGER NOT NULL"
            + COMMA_SEP + DatabaseContract.X01Games.COLUMN_NAME_DOUBLE_IN + " INTEGER NOT NULL"
            + COMMA_SEP + DatabaseContract.X01Games.COLUMN_NAME_DOUBLE_OUT + " INTEGER NOT NULL"
            + COMMA_SEP + DatabaseContract.X01Games.COLUMN_NAME_BEST_OF + " INTEGER NOT NULL"
            + COMMA_SEP + DatabaseContract.X01Games.COLUMN_NAME_WINNING_LEGS + " INTEGER NOT NULL"
            + COMMA_SEP + DatabaseContract.X01Games.COLUMN_NAME_WINNING_SETS + " INTEGER NOT NULL"
            + COMMA_SEP + DatabaseContract.X01Games.COLUMN_NAME_DATE + " INTEGER NOT NULL DEFAULT (strftime('%s','now')));";
    private static final String SQL_CREATE_X01_SCORES = "CREATE TABLE "
            + DatabaseContract.X01Scores.TABLE_NAME + " ("
            + DatabaseContract.X01Scores._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
            + COMMA_SEP + DatabaseContract.X01Scores.COLUMN_NAME_GAME + " INTEGER NOT NULL"
            + COMMA_SEP + DatabaseContract.X01Scores.COLUMN_NAME_PLAYER + " INTEGER NOT NULL"
            + COMMA_SEP + DatabaseContract.X01Scores.COLUMN_NAME_SCORING_ROUND + " INTEGER NOT NULL"
            + COMMA_SEP + DatabaseContract.X01Scores.COLUMN_NAME_NO_OF_THROW + " INTEGER NOT NULL"
            + COMMA_SEP + DatabaseContract.X01Scores.COLUMN_NAME_HIT_SECTION + " INTEGER NOT NULL"
            + COMMA_SEP + DatabaseContract.X01Scores.COLUMN_NAME_MULTIPLIER + " INTEGER NOT NULL"
            + COMMA_SEP + DatabaseContract.X01Scores.COLUMN_NAME_POINTS_LEFT + " INTEGER NOT NULL"
            + COMMA_SEP + DatabaseContract.X01Scores.COLUMN_NAME_OVERTHROWN + " INTEGER NOT NULL"
            + COMMA_SEP + DatabaseContract.X01Scores.COLUMN_NAME_LEG + " INTEGER NOT NULL"
            + COMMA_SEP + DatabaseContract.X01Scores.COLUMN_NAME_SET + " INTEGER NOT NULL"
            + COMMA_SEP + "FOREIGN KEY (" + DatabaseContract.X01Scores.COLUMN_NAME_GAME
            + ") REFERENCES " + DatabaseContract.X01Games.TABLE_NAME
            + "(" + DatabaseContract.X01Games._ID + ")"
            + COMMA_SEP + "FOREIGN KEY (" + DatabaseContract.X01Scores.COLUMN_NAME_PLAYER
            + ") REFERENCES " + DatabaseContract.Player.TABLE_NAME
            + "(" + DatabaseContract.Player._ID +"));";
    private static final String SQL_CREATE_X01_GAMES_PLAYERS_SETS = "CREATE TABLE "
            + DatabaseContract.X01GamesPlayersSets.TABLE_NAME + "("
            + DatabaseContract.X01GamesPlayersSets._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
            + COMMA_SEP + DatabaseContract.X01GamesPlayersSets.COLUMN_NAME_GAME + " INTEGER NOT NULL"
            + COMMA_SEP + DatabaseContract.X01GamesPlayersSets.COLUMN_NAME_PLAYER + " INTEGER NOT NULL"
            + COMMA_SEP + DatabaseContract.X01GamesPlayersSets.COLUMN_NAME_LEG + " INTEGER NOT NULL"
            + COMMA_SEP + DatabaseContract.X01GamesPlayersSets.COLUMN_NAME_SET + " INTEGER NOT NULL"
            + COMMA_SEP + "FOREIGN KEY (" + DatabaseContract.X01GamesPlayersSets.COLUMN_NAME_GAME
            + ") REFERENCES " + DatabaseContract.X01Games.TABLE_NAME
            + "(" + DatabaseContract.X01Games._ID + ")"
            + COMMA_SEP + "FOREIGN KEY (" + DatabaseContract.X01GamesPlayersSets.COLUMN_NAME_PLAYER
            + ") REFERENCES " + DatabaseContract.Player.TABLE_NAME
            + "(" + DatabaseContract.Player._ID + "));";

    private Context ctxt = null;

    public DatabaseHelper(Context ctxt) {
        super(ctxt, DATABASE_NAME, null, SCHEMA);
        this.ctxt = ctxt;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            db.execSQL(SQL_CREATE_PLAYERS);
            db.execSQL(SQL_CREATE_TRAINING);
            db.execSQL(SQL_CREATE_TRAINING_SESSION);
            db.execSQL(SQL_CREATE_X01_GAMES);
            db.execSQL(SQL_CREATE_X01_SCORES);
            db.execSQL(SQL_CREATE_X01_GAMES_PLAYERS_SETS);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new RuntimeException("How did we land here?");
    }

}
