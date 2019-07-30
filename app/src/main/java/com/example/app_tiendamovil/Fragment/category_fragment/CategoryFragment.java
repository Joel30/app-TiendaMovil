package com.example.app_tiendamovil.Fragment.category_fragment;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.app_tiendamovil.Collection.Utils;
import com.example.app_tiendamovil.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements View.OnClickListener {


    private TextView txt_one, txt_two, txt_three, txt_four, txt_five, txt_six, txt_seven, txt_eight;
    private View mFA;
    private FragmentManager fm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFA = inflater.inflate(R.layout.fragment_category, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Categorias");
        loadComponents();
        return mFA;
    }

    private void loadComponents() {
        fm = getActivity().getSupportFragmentManager();

        txt_one = (TextView) mFA.findViewById(R.id.category_one);
        txt_two = (TextView) mFA.findViewById(R.id.category_two);
        txt_three = (TextView) mFA.findViewById(R.id.category_three);
        txt_four = (TextView) mFA.findViewById(R.id.category_four);
        txt_five = (TextView) mFA.findViewById(R.id.category_five);
        txt_six = (TextView) mFA.findViewById(R.id.category_six);
        txt_seven = (TextView) mFA.findViewById(R.id.category_seven);
        txt_eight =(TextView) mFA.findViewById(R.id.category_eight);

        txt_one.setOnClickListener(this);
        txt_two.setOnClickListener(this);
        txt_three.setOnClickListener(this);
        txt_four.setOnClickListener(this);
        txt_five.setOnClickListener(this);
        txt_six.setOnClickListener(this);
        txt_seven.setOnClickListener(this);
        txt_eight.setOnClickListener(this);

    }

    private void category(String s){
        Utils.CATEGORY = s;
        fm.beginTransaction().replace(R.id.stage, new ProductCategoryFragment()).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.category_one : {
                category(txt_one.getText().toString());
                break;
            }
            case R.id.category_two : {
                category(txt_two.getText().toString());
                break;
            }
            case R.id.category_three : {
                category(txt_three.getText().toString());
                break;
            }
            case R.id.category_four : {
                category(txt_four.getText().toString());
                break;
            }
            case R.id.category_five : {
                category(txt_five.getText().toString());
                break;
            }
            case R.id.category_six : {
                category(txt_six.getText().toString());
                break;
            }
            case R.id.category_seven : {
                category(txt_seven.getText().toString());
                break;
            }
            case R.id.category_eight : {
                category(txt_eight.getText().toString());
                break;
            }
            default: break;
        }
    }
}
