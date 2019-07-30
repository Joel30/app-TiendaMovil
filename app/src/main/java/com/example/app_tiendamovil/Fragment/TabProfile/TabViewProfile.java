package com.example.app_tiendamovil.Fragment.TabProfile;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class TabViewProfile extends FragmentStatePagerAdapter {

    private final List<Fragment> listFragment = new ArrayList<>();
    private final List<String> listTitle = new ArrayList<>();
    public TabViewProfile(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title){
        listFragment.add(fragment);
        listTitle.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTitle.get(position);
    }

    @Override
    public Fragment getItem(int i) {
        return listFragment.get(i);
    }



    @Override
    public int getCount() {
        return listFragment.size();
    }


}
