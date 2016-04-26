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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class customer_show_shops extends AppCompatActivity {
    List<DataObject.Shop> shops = new ArrayList<>();
    customer_show_shops currAct;
    int totDishes =0;
    DataObject data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_show_shops);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        currAct = this;
        data = (DataObject) ((MyApp) this.getApplication()).get();
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getSupportActionBar().setTitle("Choose Shop");

        recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
prepareData();
        ShopAdapter shopAdapter = new ShopAdapter(shops);

        recyclerView.setAdapter(shopAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if( shops.get(position).status.equals("OPEN"))
                {
                    ((MyApp) getApplication()).check=new boolean[100];
                    Intent intent = new Intent(getBaseContext(), customer_choose_dish.class);
                    ((MyApp)getApplication()).setCSCTD(shops.get(position).id);
                startActivity(intent);}
                else{
                    Toast.makeText(   currAct , "Shop Closed,Come Back Later",
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder> {
        List<DataObject.Shop> stuff;

        public ShopAdapter(List<DataObject.Shop> a) {
            stuff = a;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.customer_choose_shop_list_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.name.setText(stuff.get(position).name);
            holder.status.setText(stuff.get(position).status);
            if(!stuff.get(position).status.equals("OPEN")){
                holder.status.setTextColor(getResources().getColor(R.color.statusNeg));
                holder.img.setImageResource(android.R.drawable.presence_busy);
            }

          holder.dishes.setText(String.valueOf (stuff.get(position).dishes.size()-data.shops.get(stuff.get(position).id).numDel).concat(" dishes"));
        }

        @Override
        public int getItemCount() {
            return stuff.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name,status,dishes;
            ImageView img;

            public MyViewHolder(View itemView) {
                super(itemView);
               name= (TextView) itemView.findViewById(R.id.customer_choose_shop_name);
                status= (TextView) itemView.findViewById(R.id.customer_choose_shop_status);
                dishes= (TextView) itemView.findViewById(R.id.customer_choose_shop_dishes);
                img = (ImageView)itemView.findViewById(R.id.customer_choose_shop_status_img);
            }
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private customer_show_shops.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final customer_show_shops.ClickListener clickListener) {
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


    void prepareData(){
        DataObject  data = ((DataObject) ((MyApp) getApplication()).get());
        int shops = data.shops.size();
         for(Map.Entry m : data.shops.entrySet()){
             DataObject.Shop x = (DataObject.Shop) m.getValue();

                 this.shops.add(x);

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
            Intent intent = new Intent(currAct,login.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
