package com.cse.pec.foodapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class owner_see_prev_order_details extends AppCompatActivity {
    DataObject data;
    ArrayList<DataObject.order_detail> order_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_see_prev_order_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Details");

        data = (DataObject) ((MyApp) getApplication()).data;
        int order = getIntent().getIntExtra("OrderID", 0);
        order_details = data.orderDetails.get(order);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.order_detail_fragment_recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        OrderAdapter orderAdapter = new OrderAdapter();
        recyclerView.setAdapter(orderAdapter);
    }

    class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {


        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView dish, units, cost;

            public MyViewHolder(View itemView) {
                super(itemView);
                dish = (TextView) itemView.findViewById(R.id.order_detail_dish);
                units = (TextView) itemView.findViewById(R.id.order_detail_units);
                cost = (TextView) itemView.findViewById(R.id.order_detail_cost);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.order_detail_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(OrderAdapter.MyViewHolder holder, final int position) {
            holder.dish.setText(data.Dishes.get(order_details.get(position).dishID).name);
            holder.units.setText(order_details.get(position).quantity + " Units");
            holder.cost.setText("₹" + data.Dishes.get(order_details.get(position).dishID).price * order_details.get(position).quantity);
        }


        @Override
        public int getItemCount() {
            return order_details.size();
        }


    }
}