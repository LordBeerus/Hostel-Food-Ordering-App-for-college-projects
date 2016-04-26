package com.cse.pec.foodapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class owner_current_orders extends AppCompatActivity {
    owner_current_orders thisActivity;
    DataObject data;
    ArrayList<DataObject.order> orders=new ArrayList<>();
    OrderAdapter orderAdapter;
    int clickedPosRecycler=-1;
    String type,addSub,orderID,customerID,ownerID;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_current_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Dispatch/Cancel Order");
        thisActivity=this;
        data = (DataObject)((MyApp)getApplication()).get();
        //----Recycler-------------
        prepareData();
        Collections.sort(orders);
         recyclerView = (RecyclerView) findViewById(R.id.owner_current_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
          orderAdapter = new OrderAdapter();

        recyclerView.setAdapter(orderAdapter);
//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                Intent intent = new Intent(thisActivity, owner_current_order_details.class);
//
//                intent.putExtra("OrderID", orders.get(position).orderID);
//                startActivity(intent);
//
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));
        //----Recycler-------------




    }

    private void sendPostRequest(final String mobile, final String msg) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... paramss ) {
                InputStream is;
                String ans;
                try {
                    String message=msg;

                    URL url = new URL("http://www.csoft.co.uk/webservices/http/sendsms");
                    Map<String,Object> params = new LinkedHashMap<>();
                    params.put("UserName", "Yash.138757");
                    params.put("PIN", "03720340");
                    params.put("SendTo","91"+mobile);
                    params.put("Message", message);

                    StringBuilder postData = new StringBuilder();
                    for (Map.Entry<String,Object> param : params.entrySet()) {
                        if (postData.length() != 0) postData.append('&');
                        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                    }
                    byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postDataBytes);

                    Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                    StringBuilder sb = new StringBuilder();
                    for (int c; (c = in.read()) >= 0;)
                        sb.append((char)c);
                    String response = sb.toString().split(" ")[0];
                            if(response.equals("158"))
                                return "YES";
                              else
                                return "NO";
                }
                 catch (Exception e){
                    return "NO";
                }

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                  if(result.equals("YES"))
                    Toast.makeText(getApplicationContext(), "SENT", Toast.LENGTH_LONG).show();
                else
                      Toast.makeText(getApplicationContext(), "Error while Sending", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute( mobile, msg);
    }


    class OrderCD extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params)
        {
//            $type=  $_GET['type'];
//            $customerID = $_GET['customerID'];
//
//
//            $orderID = $_GET['orderID'];
//            $ownerID = $_GET['ownerID'];
//            $_GET['balanceCustomer']
//            $_GET['balanceOwner']


            InputStream is = null;
            String url= "http://oo7.comuv.com/cancel_deliver.php/?";
            url+="type="+ params[0]+"&";
            url+="customerID="+params[1] +"&";
            url+="ownerID="+params[2] +"&";
            url+="orderID="+params[3] +"&";
            url+="addSub="+ params[4]+"";

            String ans;
            try {
                URL U = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) U.openConnection();
                conn.setReadTimeout(13000 /* milliseconds */);
                conn.setConnectTimeout(13000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                is = conn.getInputStream();
                String A = Request.readIt(is,400);
                String[] arr=A.split(",");

                arr[0]=arr[0].replace(" ","");
                if(arr[0].equals("YES"))
                    arr[0] = arr[0]+","+arr[1];

                return arr[0];




            }catch (Exception e){


            }


            return "NO";

        }
        @Override
        protected void onPostExecute(String  result) {

            if(result.charAt(0)=='Y'){
                final String mobile = result.split(",")[1];

                ((MyApp)getApplication()).data.o.balance-=(int)Double.parseDouble(addSub);
                ((MyApp)getApplication()).data.Orders.get(Integer.parseInt(orderID)).status=type;
                Snackbar snackbar = Snackbar
                        .make(recyclerView, "This will send a SMS!!!", Snackbar.LENGTH_INDEFINITE).setAction("Confirm", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                        String message;
                                if(type.equals("DELIVERED")){
                                    message="Order from " + data.shopz.name + " will be delivered to room  "+orders.get(clickedPosRecycler).custRoom
                                            + " in hostel "+orders.get(clickedPosRecycler).custHostel+" in 10 minutes.";
                                }else{
                                message = "Order placed at "+  data.shopz.name+" has been cancelled .Sorry for the inconvenience. An amount of Rs."
                                   + orders.get(clickedPosRecycler).amount+" has been credited back to your account.";
                                }
                                orders.remove(clickedPosRecycler);
                                orderAdapter.notifyItemRemoved(clickedPosRecycler);
                                try{

                                    String SENT = "SMS_SENT";
                                    String DELIVERED = "SMS_DELIVERED";

                                    PendingIntent sentPI = PendingIntent.getBroadcast(thisActivity, 0,
                                            new Intent(SENT), 0);

                                    PendingIntent deliveredPI = PendingIntent.getBroadcast(thisActivity, 0,
                                            new Intent(DELIVERED), 0);

                                    //---when the SMS has been sent---
                                    registerReceiver(new BroadcastReceiver(){
                                        @Override
                                        public void onReceive(Context arg0, Intent arg1) {
                                            switch (getResultCode())
                                            {
                                                case Activity.RESULT_OK:
                                                    Toast.makeText(getBaseContext(), "SMS sent",
                                                            Toast.LENGTH_SHORT).show();
                                                    break;
                                                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                                    Toast.makeText(getBaseContext(), "Generic failure",
                                                            Toast.LENGTH_SHORT).show();
                                                    break;
                                                case SmsManager.RESULT_ERROR_NO_SERVICE:
                                                    Toast.makeText(getBaseContext(), "No service",
                                                            Toast.LENGTH_SHORT).show();
                                                    break;
                                                case SmsManager.RESULT_ERROR_NULL_PDU:
                                                    Toast.makeText(getBaseContext(), "Null PDU",
                                                            Toast.LENGTH_SHORT).show();
                                                    break;
                                                case SmsManager.RESULT_ERROR_RADIO_OFF:
                                                    Toast.makeText(getBaseContext(), "Radio off",
                                                            Toast.LENGTH_SHORT).show();
                                                    break;
                                            }
                                        }
                                    }, new IntentFilter(SENT));

                                    //---when the SMS has been delivered---
                                    registerReceiver(new BroadcastReceiver(){
                                                         @Override
                                                         public void onReceive(Context arg0, Intent arg1) {
                                                             switch (getResultCode())
                                                             {
                                                                 case Activity.RESULT_OK:
                                                                     Toast.makeText(getBaseContext(), "SMS delivered",
                                                                             Toast.LENGTH_SHORT).show();
                                                                     break;
                                                                 case Activity.RESULT_CANCELED:
                                                                     Toast.makeText(getBaseContext(), "SMS not delivered",
                                                                             Toast.LENGTH_SHORT).show();
                                                                     break;
                                                             }
                                                         }
                                                     }, new IntentFilter(DELIVERED));


                                    SmsManager smsManager = SmsManager.getDefault();

                            //    smsManager.sendTextMessage("9256496485", null, message, sentPI, deliveredPI);

                                 sendPostRequest(mobile,message);
                                }catch(Exception e){
                                   // Toast.makeText(thisActivity, "SMS failed",
                                     //       Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                snackbar.show();


            }else{

            }

        }


    }
    private void prepareData() {
        int o = data.Orders.size();
        for(Map.Entry m : data.Orders.entrySet()){
            DataObject.order x =((DataObject.order)(m.getValue()));
            if(x.status.equals("ORDERED"))
            orders.add(x );



        }


    }



    class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {



        public  class MyViewHolder extends RecyclerView.ViewHolder  {
            TextView orderDet,cost ;
            ImageButton dispatch,cancel;
            TextView clickIt;
            public MyViewHolder(View itemView) {
                super(itemView);
                orderDet =  (TextView)itemView.findViewById(R.id.owner_decision_name);
                cost=  (TextView)itemView.findViewById(R.id.owner_decision_cost);
                dispatch = (ImageButton)itemView.findViewById(R.id.owner_decision_dispatch);
                cancel=(ImageButton)itemView.findViewById(R.id.owner_decision_cancel);
                clickIt = (TextView)itemView.findViewById(R.id.owner_decision_click);

            }
        }
        @Override
        public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.owner_make_order_decision_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(OrderAdapter.MyViewHolder holder, final int position) {
            int orderId= orders.get(position).orderID;
            DataObject.order o = data.Orders.get(orderId);
            holder.orderDet.setText("Name : " + o.custName+ "\nRoom : "+o.custRoom +"\nHostel :"+o.custHostel);
            holder.cost.setText("₹ " + o.amount);
            holder.clickIt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(thisActivity, owner_current_order_details.class);

                    intent.putExtra("OrderID", orders.get(position).orderID);
                    startActivity(intent);

                }
            });


            holder.dispatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                clickedPosRecycler = position;
                    type = "DELIVERED";
                    DataObject.order order = orders.get(position);
                    addSub = String.valueOf(order.amount);
                    ownerID=String.valueOf(order.ownerID);
                    customerID=String.valueOf(order.customerID);
                    orderID =String.valueOf(order.orderID);

                    new OrderCD().execute(type,customerID,ownerID,orderID,addSub);




                }
            });
            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                clickedPosRecycler = position;
                    type = "CANCELLED";
                    DataObject.order order = orders.get(position);
                    addSub = String.valueOf(order.amount);
                    ownerID=String.valueOf(order.ownerID);
                    customerID=String.valueOf(order.customerID);
                    orderID =String.valueOf(order.orderID );

                    new OrderCD().execute(type,customerID,ownerID,orderID,addSub);



                }
            });
        }





        @Override
        public int getItemCount() {
            return orders.size();
        }


    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private owner_current_orders.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final owner_current_orders.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
