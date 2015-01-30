package de.lucaspradel.mydart.game.x01;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.lucaspradel.mydart.R;

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
    private OnFragmentInteractionListener mListener;
    private List<Button> scoreBtns;
    private List<GamePlayer> players;
    private GamePlayer curPlayer;
    private int curPlayerPosition = 0;
    private int[][] scoreRound = new int[3][2];
    private int curPosition = -1;
    private boolean overthrown = false;
    private TextView curPlayerScore;
    private TextView[] scoringRoundViews;
    private List<TextView> playerScoresList;

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
    }

    private void initGameParameters(Bundle bundle) {
        points = bundle.getInt("playType");
        legs = bundle.getInt("legs");
        sets = bundle.getInt("sets");
        String[] playerNames = bundle.getStringArray("playerNames");
        for (String playerName : playerNames) {
            players.add(new GamePlayer(points, legs,sets, playerName));
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
        return view;
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

    private void accept() {
        for (TextView tv : scoringRoundViews) {
            tv.setText("");
        }
        curPosition = -1;
        curPlayerPosition = getNextPlayerPosition();
        curPlayer = players.get(curPlayerPosition);
        curPlayerScore = playerScoresList.get(curPlayerPosition);
        overthrown = false;
        //- reset ScoreViews
        //- change curPlayer
        //- reset curPosition
        //- insert in db
    }

    private int getNextPlayerPosition() {
        return curPlayerPosition >= players.size() - 1 ? 0 : curPlayerPosition + 1;
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
        Log.e("Player finished", "Player finito");
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
        playerScoresList = new ArrayList<>();
        playerScoresList.add(playerScore1);
        playerScoresList.add(playerScore2);
        curPlayerScore = playerScoresList.get(0);
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
