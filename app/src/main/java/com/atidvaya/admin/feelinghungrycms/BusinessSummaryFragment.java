package com.atidvaya.admin.feelinghungrycms;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class BusinessSummaryFragment extends Fragment {

    Context context;


    ListView todayOrdersListView,overAllOrdersListView;
    List<Map<String,String>> foodList;
    ArrayList<FoodItem> foodItemList;
    ArrayList<Orders> todayOrderList;
    BusinessSummaryAdapter businessSummaryAdapter;




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

System.out.println("************************************************************************************************************************************************************************************");

        foodItemList=new ArrayList<FoodItem>();
        todayOrderList=new ArrayList<Orders>();
        businessSummaryAdapter=new BusinessSummaryAdapter();
        foodList=new ArrayList<Map<String,String>>();
        View view=getActivity().getLayoutInflater().inflate(R.layout.fragment_business_summary,null);

        todayOrdersListView= (ListView) view.findViewById(R.id.todayOrdersListViewId);
        overAllOrdersListView= (ListView) view.findViewById(R.id.overAllOrderslistViewId);

        todayOrdersListView.setAdapter(businessSummaryAdapter);
        overAllOrdersListView.setAdapter(businessSummaryAdapter);


        new BackgroundMenuTask().execute("");





        return view;
    }

    private void updateTodayOrderSummay()
    {

        System.out.println("Menu size:" + OrderDataClass.foodList.size());


        if(OrderDataClass.foodList.size()>0)
        {

            if(todayOrderList.size()>0)
            {



                for(int i=0;i<foodList.size();i++)
                {


                    Map<String,String> foodItemMapObj=foodList.get(i);
                    int item_count=0;

                    String food_menu_item_name=foodItemMapObj.get("name");

                    System.out.println("-------"+food_menu_item_name+"---------");

                    for(int j=0;j<todayOrderList.size();j++)
                    {
                        String food_order_item_name=todayOrderList.get(j).getFood_item_name();

                        if(food_menu_item_name.equals(food_order_item_name))
                        {
                            item_count=item_count+Integer.parseInt(todayOrderList.get(j).getQuantity());
                        }

                    }

                    foodItemList.add(new FoodItem(food_menu_item_name,item_count));
                    businessSummaryAdapter.notifyDataSetChanged();


                }

                businessSummaryAdapter.notifyDataSetChanged();

            }



        }



    }


    class BusinessSummaryAdapter extends BaseAdapter
    {

        TextView itemNameTv,itemCountTv;

        ImageView callImageBtn;

        @Override
        public int getCount() {
            return foodItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return foodItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view=getActivity().getLayoutInflater().inflate(R.layout.custom_business_summary_row, null);
            itemNameTv= (TextView) view.findViewById(R.id.itemNameTvId);
            itemCountTv= (TextView) view.findViewById(R.id.itemCountTvId);


            System.out.println(foodItemList.get(position).getName()+"--"+foodItemList.get(position).getCount());

            itemNameTv.setText(foodItemList.get(position).getName());
            itemCountTv.setText(String.valueOf(foodItemList.get(position).getCount()));








            return view;
        }
    }















    class BackgroundOrdersTask extends AsyncTask<String,Void,String> {

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
                           businessSummaryAdapter.notifyDataSetChanged();

                        }









                    }

                    businessSummaryAdapter.notifyDataSetChanged();







                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    updateTodayOrderSummay();
                }


                super.onPostExecute(result);
            }
        }


    }


    class BackgroundMenuTask extends AsyncTask<String,Void,String>
    {

        ProgressDialog progressDialog;
        HttpURLConnection urlConnection;
        @Override
        protected void onPreExecute() {

            progressDialog=new ProgressDialog(context);
            progressDialog.setMessage("Please wait");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();


            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            StringBuffer result = new StringBuffer();

            try {
                URL url = new URL("https://feelinghungry.firebaseio.com/food_menu.json");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            }catch( Exception e) {
                e.printStackTrace();
                result.append("error");
            }
            finally {
                urlConnection.disconnect();
            }


            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if(result.equals("null"))
            {

            }else if(result.equals("error"))
            {


            }else
            {

                try {



                    JSONObject jsonObject=new JSONObject(result);

                    JSONArray jsonArray=jsonObject.getJSONArray("food_menu");

                    for(int i=0;i<jsonArray.length();i++)
                    {

                        JSONObject locJsonObj=jsonArray.getJSONObject(i);

                        String name= locJsonObj.getString("name").trim();
                        String price= locJsonObj.getString("price").trim();

                        Map<String,String> mapObj=new HashMap<String,String>();
                        mapObj.put("name",name);
                        mapObj.put("price", price);

                        foodList.add(mapObj);




                    }

                    OrderDataClass.foodList=foodList;


                    System.out.println(foodList.size());


                } catch (JSONException e) {
                    e.printStackTrace();


                }finally {
                    new BackgroundOrdersTask().execute("");

                }


            }





            super.onPostExecute(result);
        }
    }




}
