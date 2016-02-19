package com.atidvaya.admin.feelinghungrycms;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;


public class TodayOrdersFragment extends Fragment
{
    Context context;

    Firebase rootDataReference;



    public  TodayOrdersFragment()
    {

    }
    public TodayOrdersFragment(Context context)
    {
        this.context=context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Firebase.setAndroidContext(context);
        rootDataReference=new Firebase("https://feelinghungry.firebaseio.com/orders");




       View view=getActivity().getLayoutInflater().inflate(R.layout.fragment_today_orders,null);



        return view;
    }

}
