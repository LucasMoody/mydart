package de.lucaspradel.mydart.model.data.manager;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import de.lucaspradel.mydart.model.data.DatabaseHelper;
import de.lucaspradel.mydart.model.data.contract.DatabaseContract;

/**
 * Created by lpradel on 1/16/15.
 */
public class TrainingManager {
    private int playerId;
    private long date;
    private Context ctxt;
    private DatabaseHelper dbHelper;
    private long sessionId;
    private long trainingId;

    public TrainingManager(Context ctxt, final int playerId, long date) {
        this.playerId = playerId;
        this.date = date;
        this.ctxt = ctxt;
        dbHelper = new DatabaseHelper(ctxt);
        CreateTrainingTask trainingTask = new CreateTrainingTask();
        trainingTask.execute();
    }

    public void saveThrow(int trainedSection, int section, int multiplier, int numberOfThrow) {
        SaveThrowTask saveThrowTask = new SaveThrowTask();
        saveThrowTask.execute(new Integer[] {trainedSection, section, multiplier, numberOfThrow});
    }

    public long getSessionId() {
        return sessionId;
    }

    public long getTrainingId() {
        return trainingId;
    }


    private class CreateTrainingTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            dbHelper = new DatabaseHelper(ctxt);
            ContentValues cv = new ContentValues();
            cv.put(DatabaseContract.Training.COLUMN_NAME_DATE, date);
            cv.put(DatabaseContract.Training.COLUMN_NAME_PLAYER, playerId);
            trainingId = dbHelper.getWritableDatabase().insert(DatabaseContract.Training.TABLE_NAME, null, cv);
            return null;
        }
    }

    private class SaveThrowTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... arg) {
            int trainedSection = arg[0];
            int section = arg[1];
            int multiplier = arg[2];
            int numberOfThrow = arg[3];
            ContentValues cv = new ContentValues();
            cv.put(DatabaseContract.TrainingSession.COLUMN_NAME_TRAINING, trainingId);
            cv.put(DatabaseContract.TrainingSession.COLUMN_NAME_NO_OF_THROW, numberOfThrow);
            cv.put(DatabaseContract.TrainingSession.COLUMN_NAME_TRAINED_SECTION, trainedSection);
            cv.put(DatabaseContract.TrainingSession.COLUMN_NAME_HIT_SECTION, section);
            cv.put(DatabaseContract.TrainingSession.COLUMN_NAME_MULTIPLIER, multiplier);
            sessionId = dbHelper.getWritableDatabase().insert(DatabaseContract.TrainingSession.TABLE_NAME, null, cv);
            return null;
        }

    }

}
