package com.cse.pec.foodapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class customer_see_previous_orders extends AppCompatActivity {
    customer_see_previous_orders thisActivity;
    ArrayList<Integer> orders=new ArrayList<>();
    DataObject data;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity=this;
        setContentView(R.layout.activity_customer_see_previous_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Previous Orders");
        data =(DataObject) ((MyApp)this.getApplication()).get();
        //----Recycler-------------
        prepareData();
        Collections.sort(orders);
    recyclerView = (RecyclerView) findViewById(R.id.customer_see_prev_order_recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
       OrderAdapter dishAdapter = new OrderAdapter();

        recyclerView.setAdapter(dishAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
            Intent intent = new Intent(thisActivity,cutomer_prev_order_details.class);

            intent.putExtra("OrderID",orders.get(orders.size()-1-position) );
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        //----Recycler-------------
    }

    private void prepareData()
    {

        int o = data.Orders.size();
        for(Map.Entry m : data.Orders.entrySet()){
            DataObject.order x =((DataObject.order)(m.getValue()));
            orders.add(x.orderID);



        }


    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {



        public  class MyViewHolder extends RecyclerView.ViewHolder  {
            TextView orderDet,cost,status;
            ImageView statusImg;
            public MyViewHolder(View itemView) {
                super(itemView);
                orderDet =  (TextView)itemView.findViewById(R.id.previous_order_date);
                cost=  (TextView)itemView.findViewById(R.id.previous_order_paid);
                status = (TextView)itemView.findViewById(R.id.previous_order_status);
                statusImg=(ImageView)itemView.findViewById(R.id.previous_order_status_img);


            }
        }
        @Override
        public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.previous_orders_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(OrderAdapter.MyViewHolder holder, final int position) {
            int pos = orders.size()-position-1;
            int orderId= orders.get(pos);
            int ownerId = data.Orders.get(orderId).ownerID;
            String shop = data.shopsByOwnerId.get(ownerId).name;
            //get shop by owner id
            holder.orderDet.setText(shop);
            holder.cost.setText("₹"+String.valueOf(data.Orders.get(orderId).amount));
            String status = data.Orders.get(orderId).status ;
            holder.status.setText(status+" ON "+ data.Orders.get(orderId).date);
            if(status.equals("DELIVERED")){

                holder.statusImg.setImageResource(android.R.drawable.presence_online);
            }else if(status.equals("CANCELLED")){

                holder.statusImg.setImageResource(android.R.drawable.presence_busy);
            }else if (status.equals("ORDERED")){

                holder.statusImg.setImageResource(android.R.drawable.presence_away);
            }

        }





        @Override
        public int getItemCount() {
            return orders.size();
        }


    }
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private customer_see_previous_orders.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final customer_see_previous_orders.ClickListener clickListener) {
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
