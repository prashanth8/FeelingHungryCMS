package com.atidvaya.admin.feelinghungrycms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;


public class TodayOrdersFragment extends Fragment
{
    Context context;

    Firebase rootDataReference;


    ListView  todayOrdersListView;
    ArrayList<Orders> todayOrderList;

    TodayOrdersAdapter todayOrdersAdapter;



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
        rootDataReference=new Firebase("https://feelinghungry.firebaseio.com/food_orders");

        todayOrderList=new ArrayList<Orders>();
        todayOrdersAdapter=new TodayOrdersAdapter();


       View view=getActivity().getLayoutInflater().inflate(R.layout.fragment_today_orders,null);
        todayOrdersListView= (ListView) view.findViewById(R.id.todayOrdersListViewId);
        todayOrdersListView.setAdapter(todayOrdersAdapter);

        todayOrdersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                return false;
            }
        });



        new BackgroundTask().execute("");
        return view;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.order_options_menu, menu);


    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {


        return super.onContextItemSelected(item);
    }

    class TodayOrdersAdapter extends BaseAdapter
    {

        TextView customerNameTvId,food_nameTv,quantityTv,deliveryPointTv,totalPricetv;

        ImageView callImageBtn;

        @Override
        public int getCount() {
            return todayOrderList.size();
        }

        @Override
        public Object getItem(int position) {
            return todayOrderList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view=getActivity().getLayoutInflater().inflate(R.layout.custom_today_orders_layout, null);

            customerNameTvId= (TextView) view.findViewById(R.id.customerNameTvId);

            food_nameTv= (TextView) view.findViewById(R.id.foodTitleEt);
            quantityTv= (TextView) view.findViewById(R.id.foodQuantityTvId);
            deliveryPointTv= (TextView) view.findViewById(R.id.foodDeliveryPointTvId);
            totalPricetv= (TextView) view.findViewById(R.id.foodTotalPriceTvId);
            callImageBtn= (ImageView) view.findViewById(R.id.callCustomerBtnId);



            registerForContextMenu(callImageBtn);

            customerNameTvId.setText(todayOrderList.get(position).getName());
            food_nameTv.setText(todayOrderList.get(position).getFood_item_name());
            quantityTv.setText(todayOrderList.get(position).getQuantity());
            deliveryPointTv.setText(todayOrderList.get(position).getDelivery_point());
            totalPricetv.setText("Total = "+todayOrderList.get(position).getTotal_price()+" Rs");





            System.out.println("Position:" + position);


            callImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                getActivity().openContextMenu(callImageBtn);

                }
            });



            return view;
        }
    }


    class BackgroundTask extends AsyncTask<String,Void,String> {

        ProgressDialog progressDialog;
        HttpURLConnection urlConnection;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();


            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL("https://feelinghungry.firebaseio.com/food_orders.json");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
                result.append("error");
            } finally {
                urlConnection.disconnect();
            }


            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result.equals("null")) {

            } else if (result.equals("error")) {


            } else {

                try {


                    JSONObject mainObject = new JSONObject(result);

                    Iterator<String> keysOfObj = mainObject.keys();

                    while (keysOfObj.hasNext()) {
                        String key = keysOfObj.next();

                        JSONObject localJObj = mainObject.getJSONObject(key);

                        System.out.println(localJObj.toString());

                        String name=localJObj.getString("name").trim();
                        String mobile_number=localJObj.getString("mobile_number").trim();
                        String date_time=localJObj.getString("date_time").trim();

                        String delivery_point=localJObj.getString("delivery_point");
                        String total_price=localJObj.getString("total_price");
                        String quantity=localJObj.getString("quantity");
                        String food_item_name=localJObj.getString("food_item_name");



                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
                        String today = dateformat.format(c.getTime());


                        if(today.equals(date_time)) {
                            Orders myOrderObj = new Orders(name, mobile_number, food_item_name, quantity, date_time, delivery_point, total_price, "");


                            todayOrderList.add(myOrderObj);
                            todayOrdersAdapter.notifyDataSetChanged();

                        }









                    }

                    todayOrdersAdapter.notifyDataSetChanged();


                    System.out.println("Today orders:" + todayOrderList.size());




                } catch (JSONException e) {
                    e.printStackTrace();
                }


                super.onPostExecute(result);
            }
        }


    }







}
