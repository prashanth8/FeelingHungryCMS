package com.atidvaya.admin.feelinghungrycms;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class UpdateOfferFragment extends Fragment
{



    Context context;
    Firebase rootDataReference;
    EditText offerEt;
    Button updateOffrBtn;



    public  UpdateOfferFragment()
    {

    }

    public UpdateOfferFragment(Context context)
    {
        this.context=context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Firebase.setAndroidContext(context);
        rootDataReference=new Firebase("https://feelinghungry.firebaseio.com/TodayOffer");
        View view=getActivity().getLayoutInflater().inflate(R.layout.fragment_update_offer,null);

        offerEt= (EditText) view.findViewById(R.id.offerEtId);
        updateOffrBtn= (Button) view.findViewById(R.id.updateOffrBtnId);

        updateOffrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(offerEt.getText().toString().equals(""))
                {

                }else {
                    String offer_data = offerEt.getText().toString();

                    final ProgressDialog progressDialog=new ProgressDialog(context);
                    progressDialog.setMessage("Updating Offer");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();


                    Map<String,String> offerObj= new HashMap<String, String>();
                    offerObj.put("offer",offer_data);

                    rootDataReference.setValue(offerObj);


                    rootDataReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder=new AlertDialog.Builder(context);
                            builder.setTitle("Success!");
                            builder.setMessage("Offer updated successfully");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alert=builder.create();
                            alert.show();


                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError)
                        {

                            progressDialog.dismiss();
                            AlertDialog.Builder builder=new AlertDialog.Builder(context);
                            builder.setTitle("Something is wrong!");
                            builder.setMessage("Not able to update offer at this moment");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alert=builder.create();
                            alert.show();
                        }
                    });


                }
            }
        });


        return view;
    }
}
