package de.lucaspradel.mydart.statistics;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.lucaspradel.mydart.R;
import de.lucaspradel.mydart.model.data.manager.StatisticsManager;

public class PlayerStatisticsActivity extends ActionBarActivity {

    private ListView statisticsListView;
    private StatisticsManager statisticsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_statistics);
        this.statisticsListView = (ListView) findViewById(R.id.lv_playerStatistics);
        this.statisticsManager = new StatisticsManager(getApplicationContext());
        statisticsManager.setListener(new StatisticsManager.OnPlayerAvgFinishedListener() {
            @Override
            public void onPlayerAvgFinished(List<StatisticsManager.PlayerAvgStatistics> list) {
                statisticsListView.setAdapter(new CustomAdapter(getApplicationContext(), list));
            }
        });
        statisticsManager.getPlayersAvgs();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player_statistics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CustomAdapter extends ArrayAdapter<StatisticsManager.PlayerAvgStatistics> {

        private class ViewHolder {
            TextView name;
            TextView avg;
            protected ViewHolder(TextView name, TextView avg) {
                this.name = name;
                this.avg = avg;
            }
            public TextView getName() {
                return name;
            }
            public TextView getAvg() {
                return avg;
            }
        }
        public CustomAdapter(Context context, List<StatisticsManager.PlayerAvgStatistics> list) {
            super(context, R.layout.layout_two_column, list);
            playerAvgList = list;
        }

        private List<StatisticsManager.PlayerAvgStatistics> playerAvgList;
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            StatisticsManager.PlayerAvgStatistics playerStat = playerAvgList.get(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.layout_two_column, parent, false);
                TextView firstColumn = (TextView) convertView.findViewById(R.id.tv_firstColumn);
                TextView secondColumn = (TextView) convertView.findViewById(R.id.tv_secondColumn);
                viewHolder = new ViewHolder(firstColumn, secondColumn);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.getName().setText(playerStat.getUsername());
            viewHolder.getAvg().setText(new DecimalFormat("0.00").format(playerStat.getAvg()));
            return convertView;
        }
    }
}
