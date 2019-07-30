package com.example.app_tiendamovil.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.Fragment.TabProfile.FavoriteFragment;
import com.example.app_tiendamovil.Fragment.TabProfile.FollowFragment;
import com.example.app_tiendamovil.Fragment.TabProfile.ProfileProductFragment;
import com.example.app_tiendamovil.Fragment.TabProfile.TabViewProfile;
import com.example.app_tiendamovil.R;

public class TabsFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AppBarLayout appBar;
    private View mFA;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFA = inflater.inflate(R.layout.fragment_tabs, container, false);
        loadComponents(container);
        return mFA;
    }

    private void loadComponents(ViewGroup container) {
        if (Utils.rotacion == 0){
            View parent = (View) container.getParent();
            if (appBar == null){
                appBar =  (AppBarLayout) parent.findViewById(R.id.appBar);
                tabLayout = new TabLayout(getActivity());
                tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
                appBar.addView(tabLayout);
                viewPager = (ViewPager) mFA.findViewById(R.id.view_pager_profile);
                if (Utils.tab == 0){
                    llenarViewPager(viewPager);
                } else {
                    llenarViewPagerOne(viewPager);
                }

                viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    }
                });
                tabLayout.setupWithViewPager(viewPager);
            }
        }else {
            Utils.rotacion = 1;
        }

    }

    private void llenarViewPager(ViewPager viewPager) {
        TabViewProfile adapter = new TabViewProfile(getFragmentManager());
        adapter.addFragment(new ProfileProductFragment(), "PRODUCTO");
        adapter.addFragment(new FavoriteFragment(), "FAVORITOS");
        adapter.addFragment(new FollowFragment(), "SEGUIDOS");

        viewPager.setAdapter(adapter);
    }
    private void llenarViewPagerOne(ViewPager viewPager) {
        TabViewProfile adapter = new TabViewProfile(getFragmentManager());
        adapter.addFragment(new FavoriteFragment(), "FAVORITOS");
        adapter.addFragment(new ProfileProductFragment(), "PRODUCTO");
        adapter.addFragment(new FollowFragment(), "SEGUIDOS");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (Utils.rotacion == 0){
            appBar.removeView(tabLayout);
        }
    }
}
