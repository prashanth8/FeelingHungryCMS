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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UpdateMenuFragment extends Fragment
{






    Context context;
    Firebase rootDataReference;

    List<Map<String,String>> foodList;

    Map<String,Object> foodMenuList;


    EditText foodNameEt,foodPriceEt;
    ListView foodMenuListView;

    Button addFoodBtn,updateFoodmenuBtn;

    MyAdapter ma;

    public  UpdateMenuFragment()
    {

    }
    public UpdateMenuFragment(Context context)
    {
        this.context=context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=getActivity().getLayoutInflater().inflate(R.layout.fragment_update_menu,null);

        Firebase.setAndroidContext(context);
        rootDataReference=new Firebase("https://feelinghungry.firebaseio.com/food_menu");

        foodNameEt= (EditText) view.findViewById(R.id.foodNameEtId);
        foodPriceEt= (EditText) view.findViewById(R.id.foodPriceEtId);

        foodMenuListView= (ListView) view.findViewById(R.id.foodMenuListViewId);
        addFoodBtn= (Button) view.findViewById(R.id.addFoodBtnId);
        updateFoodmenuBtn= (Button) view.findViewById(R.id.updateFoodMenuBtnId);



        foodList=new ArrayList<Map<String,String>>();
        foodMenuList=new HashMap<String,Object>();
         ma=new MyAdapter();
        foodMenuListView.setAdapter(ma);


    addFoodBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            if (foodNameEt.getText().toString().equals("")) {

            } else if (foodPriceEt.getText().toString().equals("")) {

            } else {

                String foodName = foodNameEt.getText().toString();
                String foodPrice = foodPriceEt.getText().toString();


                Map<String, String> foodObj = new HashMap<String, String>();
                foodObj.put("name", foodName);
                foodObj.put("price", foodPrice);

                foodList.add(foodObj);
                System.out.println(foodList);
                ma.notifyDataSetChanged();


                foodNameEt.setText("");
                foodPriceEt.setText("");
                foodNameEt.requestFocus();

            }


        }
    });


        updateFoodmenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
            if(foodList.size()>0)
            {


                final ProgressDialog progressDialog=new ProgressDialog(context);
                progressDialog.setMessage("Updating Offer");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();

                foodMenuList.put("food_menu",foodList);
                rootDataReference.setValue(foodMenuList);

                System.out.println(foodMenuList);


                rootDataReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setTitle("Success!");
                        builder.setMessage("Food menu updated successfully");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert=builder.create();
                       if(!alert.isShowing()) {
                           alert.show();

                       }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError)
                    {

                        progressDialog.dismiss();
                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setTitle("Something is wrong!");
                        builder.setMessage("Not able to update food menu at this moment");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert=builder.create();
                        if(!alert.isShowing()) {
                            alert.show();

                        }

                    }
                });



            }

            }
        });








        return view;
    }



    class MyAdapter extends BaseAdapter
    {

        TextView foodSrNoTv,foodNameTv,foodPriceTv;
        ImageView deleteImageButon;

        @Override
        public int getCount() {
            return foodList.size();
        }

        @Override
        public Object getItem(int position) {
            return foodList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            View view=getActivity().getLayoutInflater().inflate(R.layout.custom_food_menu_item_row,null);
            foodSrNoTv= (TextView) view.findViewById(R.id.foodserialNoTvId);
            foodNameTv= (TextView) view.findViewById(R.id.foodNameTvId);
            foodPriceTv= (TextView) view.findViewById(R.id.foodPriceTvId);
            deleteImageButon= (ImageView) view.findViewById(R.id.foodDeleteId);

            foodSrNoTv.setText(position+1+"");
            Map<String,String> mapObj=foodList.get(position);
            String name=mapObj.get("name");
            String price=mapObj.get("price");

            foodNameTv.setText(name);
            foodPriceTv.setText(price+"Rs");


            deleteImageButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    foodList.remove(position);

                    System.out.println(foodList);

                    ma.notifyDataSetChanged();
                }
            });



            return view;
        }
    }



}
