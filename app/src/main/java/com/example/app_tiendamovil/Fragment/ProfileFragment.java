package com.example.app_tiendamovil.Fragment;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.Collection.profile.EditProfileActivity;
import com.example.app_tiendamovil.Fragment.TabProfile.ProfileProductFragment;

import com.example.app_tiendamovil.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class ProfileFragment extends Fragment {

    private TabLayout tabLayout;
    private TabItem tab_item1, tab_item2, tab_item3;
    private ViewPager viewPager;
    private FragmentManager fm;
    private ImageView img_avatar;
    private TextView txt_name, txt_phone;
    private AppBarLayout appBar;
    private View mFA;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            //menu (on create options menu)
        setHasOptionsMenu(true);

        mFA = inflater.inflate(R.layout.fragment_profile, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Mi Perfil");

        loadComponents(container);

        return mFA;
    }

    private void loadComponents(ViewGroup container) {
        txt_name = (TextView) mFA.findViewById(R.id.txt_fragment_profile_name);
        txt_phone = (TextView) mFA.findViewById(R.id.txt_fragment_profile_phone);

        img_avatar = (ImageView) mFA.findViewById(R.id.img_view_profile);
        getData();
        fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.stage_my_product, new ProfileProductFragment()).commit();

    }

    private void getData (){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.ID_PERSONA_SERVICE + Utils.ID, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("msn");
                    JSONObject obj = data.getJSONObject(0);

                    txt_name.setText(obj.getString("name"));
                    txt_phone.setText(obj.getString("phone"));

                    String avatar = obj.getString("avatar");
                    Glide.with (getActivity()).load (Utils.HOST + avatar).placeholder(R.drawable.defaultimage).error(R.drawable.defaultimage).transform(new CenterCrop(), new RoundedCorners(16)).into(img_avatar);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.update_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_edit_profile){
            //Toast.makeText(getActivity(), "HOLA", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
