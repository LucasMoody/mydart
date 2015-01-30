package de.lucaspradel.mydart.game.x01;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by lpradel on 1/24/15.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    ScoreFragment scoref;
    StatisticsFragment statf;

    public SectionsPagerAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        scoref = ScoreFragment.newInstance(bundle);
        statf = StatisticsFragment.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return scoref;
            case 1:
                return statf;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
