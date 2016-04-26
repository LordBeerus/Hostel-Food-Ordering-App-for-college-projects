package com.cse.pec.foodapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class customer_order_list_pay extends AppCompatActivity {
    Button pay;
   double balance=543;int cost=0;
    RecyclerView recyclerView;
    customer_order_list_pay thisActivity;
    ArrayList<DataObject.order_detail> orderList = new ArrayList<>();
    DataObject data;
    int price;
    DataObject.order order = new DataObject().new order();
    OrderAdapter dishAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_list_pay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Checkout");
        data = (DataObject) ((MyApp) this.getApplication()).get();
        prepare();
        thisActivity = this;
        //----Recycler-------------
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.customer_order_list_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
       // recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));

        dishAdapter = new OrderAdapter();

        recyclerView.setAdapter(dishAdapter);
        //----Recycler-------------
        thisActivity = this;
        //----pay Order-------------------
        FloatingActionButton pay = (FloatingActionButton)findViewById(R.id.pay_order);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            int number=0;
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                Date date1 = new Date();
                String date=dateFormat.format(date1);
                String custID =String.valueOf(data.c.id);
                String ownerID =String.valueOf(data.shops.get ((((MyApp)getApplication()).customer_shop_choice_to_dishes)).ownerID);
                String custName = data.c.name,custHostel = data.c.address,custRoom=data.c.room_no;
                int amount = cost;

                String dishes="",quantity="";
                order.status="ORDERED";
                order.amount=cost;
                order.custRoom= custRoom;
                order.custHostel=custHostel;
                order.custName=custName;
                order.date = date;
                order.ownerID = Integer.parseInt(ownerID);
                order.customerID = Integer.parseInt(custID);
                for(int i=0;i<orderList.size();)
                    if(orderList.get(i).quantity>0){
                        number++;
                        dishes+=orderList.get(i).dishID+",";
                        quantity+=String.valueOf(orderList.get(i).quantity)+",";
                        i++;
                    }else{
                        orderList.remove(i);


                    }
                if(amount>data.c.balance){
                    Toast.makeText(thisActivity, "Not Enough Balance !",
                            Toast.LENGTH_LONG).show();
                }
         else {
                    new OrderAsync().execute(custID, ownerID, date, String.valueOf(number), String.valueOf(amount), custName, custHostel, custRoom, dishes, quantity);
                }
            }




        });



        //-------cancel order----------
        FloatingActionButton cancel = (FloatingActionButton)findViewById(R.id.cancel_order);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(thisActivity,customer_main.class));
            }
        });
    }






    public class OrderAsync extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            String response,url,custID,ownerID,date,number;
            String amount,custName,custHostel,custRoom,dishes,quantity;
            url = "http://oo7.comuv.com/mainOrderSystem.php/?";
            custID=params[0];
                    ownerID=params[1];
            date=params[2];
                    number=params[3];
            amount=params[4];
                    custName=params[5];
            custHostel=params[6];
                    custRoom=params[7];
            dishes=params[8];
                    quantity=params[9];
            url+="custID="+custID+"&ownerID="+ownerID+"&date="+date;
            url+="&number="+number+"&amount="+amount+"&custName="+custName;
            url+="&custHostel="+custHostel+"&custRoom="+custRoom+"&dishes="+dishes;
            url+="&quantity="+quantity;
            url= url.replace(" ","%20");
            try {
                URL U = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) U.openConnection();
                conn.setReadTimeout(13000 /* milliseconds */);
                conn.setConnectTimeout(13000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();
                String A = Request.readIt(is, 2000);
               String[] arr=A.split(",");
                    response = arr[0].replace(" ","");
                                if(response.equals("YES")) {
                                    response+=","+arr[1];

                                }
                return response;



            }catch (Exception e){

            return "NO";
            }



        }

        @Override
        protected void onPostExecute(String result) {
        String[] arr = result.split(",");
        arr[0]=arr[0].replace(" ","");

        if(arr[0].equals("YES") ){
           order.orderID = Integer.parseInt(arr[1]);
                   ((MyApp) getApplication()).data.Orders.put(order.orderID,order);
            ((MyApp) getApplication()).data.orderDetails.put(order.orderID,orderList);
            ((MyApp) getApplication()).data.c.balance-=order.amount;








            Intent intent = new Intent(thisActivity,customer_main.class);

            intent.putExtra("ORDER",true);
            startActivity(intent);
        }else if(result.equals("1")){
            Toast.makeText(thisActivity, "Shop is closed.Come back later.",
                    Toast.LENGTH_LONG).show();
            int shopId =   ((MyApp) getApplication()).customer_shop_choice_to_dishes;
            ((MyApp) getApplication()).data.shops.get(shopId).status = "CLOSED";
        }else{
            Toast.makeText(thisActivity, "Server Error.Try Again Later.",
                    Toast.LENGTH_LONG).show();

        }




        }
    }









    private void prepare() {

        orderList = (((MyApp) this.getApplication()).custOrders);
        balance = data.c.balance;
        for(int i =0;i<orderList.size();i++){
            cost+=(int) data.Dishes.get(orderList.get(i).dishID).price;
        }
    }

    class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

        public OrderAdapter() {

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView dishName, quant;
            SeekBar seekbar;

            public MyViewHolder(View itemView) {
                super(itemView);
                dishName = (TextView) itemView.findViewById(R.id.customer_pay_order_dish);
                quant = (TextView) itemView.findViewById(R.id.customer_pay_order_quant);
                seekbar = (SeekBar) itemView.findViewById(R.id.customer_pay_order_seekbar);


            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.customer_pay_order_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(OrderAdapter.MyViewHolder holder, final int position) {
            int size = orderList.size();
            if (position < size) {
                holder.seekbar.setVisibility(View.VISIBLE);
                holder.quant.setVisibility(View.VISIBLE);
                DataObject.Dish dish = data.Dishes.get(orderList.get(position).dishID);
                holder.dishName.setText(dish.name);
                holder.seekbar.setProgress(orderList.get(position).quantity);
                String x = String.valueOf(orderList.get(position).quantity);
                holder.quant.setText(x);
                holder.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser)
                        {}
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        int progress = seekBar.getProgress();
                        int d = orderList.get(position).dishID;
                        int currQ =  orderList.get(position).quantity;
                        double price = data.Dishes.get(d).price;
                        orderList.get(position).quantity = progress;
                        cost += (Integer)(progress - currQ)*price;
                        if(cost<0)
                            cost=0;
                        dishAdapter.notifyDataSetChanged();
                    }
                });
            }
            else{
                holder.seekbar.setVisibility(View.INVISIBLE);
                holder.quant.setVisibility(View.INVISIBLE);
                if(position==size){
                    holder.dishName.setText( "Total Amount :  \u20B9" + String.valueOf(cost));
                }else if (position==size+1){

                    holder.dishName.setText( "Balance :  \u20B9 " + String.valueOf(balance));
                }else{
                    holder.dishName.setText("  ");

                }



            }
        }


        @Override
        public int getItemCount() {

            return orderList.size() + 4;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Intent intent = new Intent(thisActivity,login.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
