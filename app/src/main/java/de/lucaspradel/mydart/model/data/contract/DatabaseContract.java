package de.lucaspradel.mydart.model.data.contract;

import android.provider.BaseColumns;

/**
 * Created by lpradel on 1/16/15.
 */
public class DatabaseContract {

    public static abstract class Player implements BaseColumns {
        public static final String TABLE_NAME = "player";
        public static final String COLUMN_NAME_USER_NAME = "username";
        public static final String COLUMN_NAME_IMAGE = "image";
    }

    public static abstract class Training implements BaseColumns {
        public static final String TABLE_NAME = "training";
        public static final String COLUMN_NAME_PLAYER = "player";
        public static final String COLUMN_NAME_DATE = "date";
    }

    public static abstract class TrainingSession implements BaseColumns {
        public static final String TABLE_NAME = "trainingsession";
        public static final String COLUMN_NAME_TRAINING = "training";
        public static final String COLUMN_NAME_TRAINED_SECTION = "trainedsection";
        public static final String COLUMN_NAME_HIT_SECTION = "hitsection";
        public static final String COLUMN_NAME_MULTIPLIER = "multiplier";
        public static final String COLUMN_NAME_NO_OF_THROW = "numberofthrow";
    }

    public static abstract class X01Scores implements BaseColumns {
        public static final String TABLE_NAME = "x01scores";
        public static final String COLUMN_NAME_HIT_SECTION = "hitsection";
        public static final String COLUMN_NAME_MULTIPLIER = "multiplier";
        public static final String COLUMN_NAME_NO_OF_THROW = "numberofthrow";
        public static final String COLUMN_NAME_SCORING_ROUND = "scoringround";
        public static final String COLUMN_NAME_GAME = "game";
        public static final String COLUMN_NAME_PLAYER = "player";
        public static final String COLUMN_NAME_POINTS_LEFT = "pointsleft";
        public static final String COLUMN_NAME_OVERTHROWN = "overthrown";
        public static final String COLUMN_NAME_LEG = "legno";
        public static final String COLUMN_NAME_SET = "setno";
    }

    public static abstract class X01Games implements BaseColumns {
        public static final String TABLE_NAME = "x01games";
        public static final String COLUMN_NAME_POINTS = "points";
        public static final String COLUMN_NAME_DOUBLE_OUT = "doubleout";
        public static final String COLUMN_NAME_DOUBLE_IN = "doublein";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_BEST_OF = "bestof";
        public static final String COLUMN_NAME_WINNING_LEGS = "winninglegs";
        public static final String COLUMN_NAME_WINNING_SETS = "winningsets";
    }

    public static abstract class X01GamesPlayersSets implements BaseColumns {
        public static final String TABLE_NAME = "x01gameplayerssets";
        public static final String COLUMN_NAME_GAME = "game";
        public static final String COLUMN_NAME_PLAYER = "player";
        public static final String COLUMN_NAME_LEG = "legno";
        public static final String COLUMN_NAME_SET = "setno";
    }



}
