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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class owner_see_prev_dishes extends AppCompatActivity {
    owner_see_prev_dishes thisActivity;
    ArrayList<Integer> orders=new ArrayList<>();
    DataObject data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_see_previous_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Previous Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        data =(DataObject) ((MyApp)this.getApplication()).get();
        //----Recycler-------------
        prepareData();
        Collections.sort(orders);
        thisActivity = this;
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.customer_see_prev_order_recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        OrderAdapter dishAdapter = new OrderAdapter();

        recyclerView.setAdapter(dishAdapter);
        //----Recycler-------------
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(thisActivity,owner_see_prev_order_details.class);

                intent.putExtra("OrderID",orders.get(orders.size()-1-position) );
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private owner_see_prev_dishes.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final owner_see_prev_dishes.ClickListener clickListener) {
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


    private void prepareData()
    {
        int o = data.Orders.size();
        for(Map.Entry m : data.Orders.entrySet()){
            DataObject.order x =((DataObject.order)(m.getValue()));
            orders.add(x.orderID);



        }

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
            int orderId= orders.get(orders.size()-1-position);
            DataObject.order o = data.Orders.get(orderId);
            String cust = "Name:"+ o.custName+"\nRoom:"+o.custRoom+"\nHostel:"+o.custHostel;
            //get shop by owner id
            holder.orderDet.setText(cust);
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



}
