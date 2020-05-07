package com.example.myfirstpracticeapplication.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myfirstpracticeapplication.R;
import com.example.myfirstpracticeapplication.UserLoginActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CenterFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private TextView mTextView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public CenterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_center, container, false);
        mTextView = view.findViewById(R.id.user_login_tv);
        mTextView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_login_tv:
                Intent intent = new Intent(mContext, UserLoginActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
