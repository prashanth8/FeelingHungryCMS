package com.atidvaya.admin.feelinghungrycms;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class BusinessSummaryFragment extends Fragment {
    Context context;

    public  BusinessSummaryFragment()
    {

    }
    public BusinessSummaryFragment(Context context)
    {
        this.context=context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=getActivity().getLayoutInflater().inflate(R.layout.fragment_business_summary,null);

        return view;
    }
}
