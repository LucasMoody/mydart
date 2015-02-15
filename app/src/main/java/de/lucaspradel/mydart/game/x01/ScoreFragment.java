package de.lucaspradel.mydart.game.x01;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.lucaspradel.mydart.Dashboard;
import de.lucaspradel.mydart.R;
import de.lucaspradel.mydart.model.data.manager.X01GameManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScoreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoreFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match

    private int points;
    private int legs;
    private int sets;
    private boolean doubleIn;
    private boolean doubleOut;
    private boolean bestOf;
    private int curSet = 1;
    private int curLeg = 1;
    private OnFragmentInteractionListener mListener;
    private List<Button> scoreBtns;
    private List<GamePlayer> players;
    private GamePlayer curPlayer;
    private int curPlayerPosition = 0;
    private int curSetBeginnerPosition = 0;
    private int curLegBeginnerPosition = 0;
    private int[][] scoreRound = new int[3][2];
    private int curPosition = -1;
    private boolean overthrown = false;
    private TextView curPlayerScore;
    private TextView[] scoringRoundViews;
    private List<TextView> playerScoreList;
    private List<TextView> playerLegList;
    private List<TextView> playerSetList;
    private X01GameManager x01GameManager;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScoreFragment newInstance(Bundle bundle) {
        ScoreFragment fragment = new ScoreFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public ScoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        players = new ArrayList<>();
        initGameParameters(getArguments());
        x01GameManager = new X01GameManager(getActivity().getApplicationContext());
        x01GameManager.createX01Game(points, doubleIn, doubleOut, bestOf, legs, sets, new Date());
    }

    private void initGameParameters(Bundle bundle) {
        points = bundle.getInt("playType");
        legs = bundle.getInt("legs");
        sets = bundle.getInt("sets");
        doubleIn = bundle.getBoolean("doubleIn");
        doubleOut = bundle.getBoolean("doubleOut");
        bestOf = bundle.getBoolean("bestOf");
        String[] playerNames = bundle.getStringArray("playerNames");
        int[] playerIds = bundle.getIntArray("playerIds");
        for (int i = 0; i<playerNames.length; i++) {
            players.add(new GamePlayer(points, 0,0, playerNames[i], playerIds[i]));
        }
        curPlayer = players.get(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_score, container, false);
        initScoreBtns(view);
        addScoreBtnsOnClickListener();
        initPlayerScoreViews(view);
        initScoringRoundViews(view);
        addProcessBtnListeners(view);
        initPlayerNameViews(view);
        initViews(view);
        return view;
    }

    private void finishGame() {
        Intent dashboard = new Intent(getActivity(), Dashboard.class);
        dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().startActivity(dashboard);
    }

    private void initViews(View view) {
        playerLegList = new ArrayList<>();
        TextView noLegsPlayer1 = (TextView) view.findViewById(R.id.tv_noLegsPlayer1);
        TextView noLegsPlayer2 = (TextView) view.findViewById(R.id.tv_noLegsPlayer2);
        playerLegList.add(noLegsPlayer1);
        playerLegList.add(noLegsPlayer2);
        playerSetList = new ArrayList<>();
        TextView noSetsPlayer1 = (TextView) view.findViewById(R.id.tv_noSetsPlayer1);
        TextView noSetsPlayer2 = (TextView) view.findViewById(R.id.tv_noSetsPlayer2);
        playerSetList.add(noSetsPlayer1);
        playerSetList.add(noSetsPlayer2);
    }

    private void initPlayerNameViews(View view) {
        TextView playerName1 = (TextView) view.findViewById(R.id.tv_playerName1);
        TextView playerName2 = (TextView) view.findViewById(R.id.tv_playerName2);
        playerName1.setText(players.get(0).getName());
        playerName2.setText(players.get(1).getName());
    }

    private void initPlayerScores(View view) {

    }

    private void addProcessBtnListeners(View view) {
        view.findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curPosition == 2 || overthrown) {
                accept();
            }}
        });
        view.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
    }

    private void delete() {
        if (curPosition >= 0) {
            if (overthrown) {
                int akk = 0;
                for (int i = 0; i<curPosition; i++) {
                    akk += scoreRound[i][0]*scoreRound[i][1];
                }
                curPlayer.setScore(curPlayer.getScore() - akk);
                scoreRound[curPosition][0] = -1;
                updateScoringRoundViews();
                updateCurPlayerScoreView();
                curPosition--;
                overthrown = false;
            } else {
                curPlayer.setScore(curPlayer.getScore() + scoreRound[curPosition][0]*scoreRound[curPosition][1]);
                updateCurPlayerScoreView();
                scoreRound[curPosition][0] = -1;
                updateScoringRoundViews();
                curPosition--;
            }

        }
    }

    private void saveThrows() {
        int scoreBeforeCurSave = curPlayer.getScore();
        if (!overthrown) {
            for (int i = 0; i<=curPosition; i++) {
                scoreBeforeCurSave += scoreRound[i][0] * scoreRound[i][1];
            }
        }
        for (int i=0; i<=curPosition; i++) {
            if (overthrown && i==curPosition) {
                x01GameManager.insertX01Score(curPlayer.getId(), scoreRound[i][0], scoreRound[i][1], i + 1, curPlayer.getScoringRound(), scoreBeforeCurSave, true, curLeg, curSet);
            } else {
                x01GameManager.insertX01Score(curPlayer.getId(), scoreRound[i][0], scoreRound[i][1], i + 1, curPlayer.getScoringRound(), scoreBeforeCurSave, false, curLeg, curSet);
                scoreBeforeCurSave -= scoreRound[i][0] * scoreRound[i][1];
            }
        }
    }

    private void accept() {
        saveThrows();
        curPlayer.setScoringRound(curPlayer.getScoringRound() + 1);
        for (TextView tv : scoringRoundViews) {
            tv.setText("");
        }
        curPosition = -1;
        curPlayerPosition = getNextPlayerPosition(curPlayerPosition);
        curPlayer = players.get(curPlayerPosition);
        curPlayerScore = playerScoreList.get(curPlayerPosition);
        overthrown = false;
        //- reset ScoreViews
        //- change curPlayer
        //- reset curPosition
        //- insert in db
    }

    private int getNextPlayerPosition(int position) {
        return position >= players.size() - 1 ? 0 : position + 1;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        //1. check whether score or multiplicator
        if (v instanceof Button) {
            String btnText = ((Button)v).getText().toString();
            try {
                int score = 0;
                if (btnText.equals("B")) {
                    score = 25;
                } else {
                    score = Integer.valueOf(btnText);
                }
                enterScore(score);

            } catch (NumberFormatException e) {
                enterMultiplicator(btnText);
            }
        }
    }

    private void enterScore(int score) {
        curPosition++;
        if (!checkValidScore() || overthrown) {
            curPosition--;
            return;
        }
        scoreRound[curPosition][0] = score;
        scoreRound[curPosition][1] = 1;
        updateScore(-score);
    }

    private void enterMultiplicator(String multiplicator) {
        if (!checkValidScore() || curPosition < 0 || overthrown ) {
            return;
        }
        int curMult = scoreRound[curPosition][1];
        int oldScore = scoreRound[curPosition][0] * curMult;
        if (multiplicator.equals("D")) {
            if (curMult == 2) {
                scoreRound[curPosition][1] = 1;
            } else {
                scoreRound[curPosition][1] = 2;
            }
        } else if (multiplicator.equals("T")) {
            if (scoreRound[curPosition][0] == 25) {
                return;
            } else if (curMult == 3) {
                scoreRound[curPosition][1] = 1;
            } else {
                scoreRound[curPosition][1] = 3;
            }
        }
        updateScore(oldScore - scoreRound[curPosition][0] * scoreRound[curPosition][1]);
    }

    private void setMultiplicatorViews(int multiplicator) {

    }

    private void updateScore(int delta) {
        int oldScore = curPlayer.getScore();
        if (oldScore + delta < 0) {
            overthrown(delta);
        } else {
            curPlayer.setScore(curPlayer.getScore() + delta);
        }
        updateScoringRoundViews();
        updateCurPlayerScoreView();
        if (curPlayer.getScore() == 0) {
            playerFinished();
        }
    }

    private void playerFinished() {
        boolean legIncr = false;
        //Satz in DB speichern
        x01GameManager.insertSetWinner(curPlayer.getId(), curLeg, curSet);
        if (bestOf) {
            if (curPlayer.getSets() + 1 < sets) {
                curPlayer.setSets(curPlayer.getSets() + 1);
                curSet++;
            } else {
                curSet = 1;
                curLeg++;
                curPlayer.setLegs(curPlayer.getLegs() + 1);
                legIncr = true;
            }
        } else {
            if (curSet + 1 < sets) {
                curSet++;
                curPlayer.setSets(curPlayer.getSets() + 1);
            } else {
                curSet = 1;
                curLeg++;
                GamePlayer curLeader = players.get(0);
                int curMax = 0;
                for (GamePlayer player : players) {
                    if (player.getSets() > curMax) {
                        curMax = player.getSets();
                        curLeader = player;
                    }
                }
                curLeader.setLegs(curLeader.getLegs() + 1);
                legIncr = true;
            }
        }
        //wahrscheinlich geht auch curPlayer.getLegs() > legs || curLeg>legs

        //accept um Daten in DB zu speichern und Views zu aktualisieren
        if ((bestOf && curPlayer.getLegs() >= legs) || curLeg - 1 >=legs) {
            gameOver();
        }
        accept();
        resetRound(legIncr);
    }

    private void gameOver() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
// 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(curPlayer.getName() + " hat gewonnen!")
                .setTitle("GEWONNEN").setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishGame();
                    }
                });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finishGame();
                }
                return false;
            }
        });
// 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void resetRound(boolean leg) {
        //scoringRoundView auf auf leer
        curPosition = -1;
        if (leg) {
            curPlayerPosition = curLegBeginnerPosition = curSetBeginnerPosition = getNextPlayerPosition(curLegBeginnerPosition);
        } else {
            curPlayerPosition = curSetBeginnerPosition = getNextPlayerPosition(curSetBeginnerPosition);
        }
        curPlayer = players.get(curPlayerPosition);
        for (GamePlayer player : players) {
            player.setScore(points);
            player.setScoringRound(1);
            if (leg) {
                player.setSets(0);
            }
        }
        //update set und leg views
        for(int i = 0; i<players.size(); i++) {
            //TODO in Römische Zeichen
            playerSetList.get(i).setText(String.valueOf(players.get(i).getSets()));
            playerLegList.get(i).setText(String.valueOf(players.get(i).getLegs()));
        }
        curPlayer = players.get(curPlayerPosition);
        curPlayerScore = playerScoreList.get(curPlayerPosition);
        updateAllPlayerScoreViews();
        updateScoringRoundViews();
    }

    private void overthrown(int delta) {
        int akkScore = delta;
        for (int i = 0; i<= curPosition; i++) {
            akkScore += scoreRound[i][0] * scoreRound[i][1];
        }
        curPlayer.setScore(curPlayer.getScore() + akkScore);
        overthrown = true;
        //lock afterwards
    }

    private void updateScoringRoundViews() {
        for (int i = 0; i<=curPosition; i++) {
            if (scoreRound[i][0] == -1)  {
                scoringRoundViews[i].setText("");
            } else {
                scoringRoundViews[i].setText(String.valueOf(scoreRound[i][0]*scoreRound[i][1]));
            }
        }
    }

    private void updateCurPlayerScoreView() {
        curPlayerScore.setText(String.valueOf(curPlayer.getScore()));
    }

    private void updateAllPlayerScoreViews() {
        for (int i = 0; i<playerScoreList.size(); i++) {
            playerScoreList.get(i).setText(String.valueOf(players.get(i).getScore()));
        }
    }

    private boolean checkValidScore() {
        return curPosition >= 3 ? false : true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void initScoreBtns(View view) {
        scoreBtns = new ArrayList<>();
        scoreBtns.add((Button) view.findViewById(R.id.btn_0));
        scoreBtns.add((Button) view.findViewById(R.id.btn_1));
        scoreBtns.add((Button) view.findViewById(R.id.btn_2));
        scoreBtns.add((Button) view.findViewById(R.id.btn_3));
        scoreBtns.add((Button) view.findViewById(R.id.btn_4));
        scoreBtns.add((Button) view.findViewById(R.id.btn_5));
        scoreBtns.add((Button) view.findViewById(R.id.btn_6));
        scoreBtns.add((Button) view.findViewById(R.id.btn_7));
        scoreBtns.add((Button) view.findViewById(R.id.btn_8));
        scoreBtns.add((Button) view.findViewById(R.id.btn_9));
        scoreBtns.add((Button) view.findViewById(R.id.btn_10));
        scoreBtns.add((Button) view.findViewById(R.id.btn_11));
        scoreBtns.add((Button) view.findViewById(R.id.btn_12));
        scoreBtns.add((Button) view.findViewById(R.id.btn_13));
        scoreBtns.add((Button) view.findViewById(R.id.btn_14));
        scoreBtns.add((Button) view.findViewById(R.id.btn_15));
        scoreBtns.add((Button) view.findViewById(R.id.btn_16));
        scoreBtns.add((Button) view.findViewById(R.id.btn_17));
        scoreBtns.add((Button) view.findViewById(R.id.btn_18));
        scoreBtns.add((Button) view.findViewById(R.id.btn_19));
        scoreBtns.add((Button) view.findViewById(R.id.btn_20));
        scoreBtns.add((Button) view.findViewById(R.id.btn_B));
        scoreBtns.add((Button) view.findViewById(R.id.btn_D));
        scoreBtns.add((Button) view.findViewById(R.id.btn_T));
    }

    private void initPlayerScoreViews(View view) {
        TextView playerScore1 = (TextView) view.findViewById(R.id.tv_scorePlayer1);
        TextView playerScore2 = (TextView) view.findViewById(R.id.tv_scorePlayer2);
        playerScore1.setText(String.valueOf(players.get(0).getScore()));
        playerScore2.setText(String.valueOf(players.get(1).getScore()));
        playerScoreList = new ArrayList<>();
        playerScoreList.add(playerScore1);
        playerScoreList.add(playerScore2);
        curPlayerScore = playerScoreList.get(0);
    }

    private void initScoringRoundViews(View view) {
        scoringRoundViews = new TextView[3];
        scoringRoundViews[0] = (TextView) view.findViewById(R.id.tv_score1);
        scoringRoundViews[1] = (TextView) view.findViewById(R.id.tv_score2);
        scoringRoundViews[2] = (TextView) view.findViewById(R.id.tv_score3);
    }

    private void addScoreBtnsOnClickListener() {
        for (Button btn : scoreBtns) {
            btn.setOnClickListener(this);
        }
    }

}
