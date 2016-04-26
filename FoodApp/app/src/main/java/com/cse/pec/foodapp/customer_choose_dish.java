package com.cse.pec.foodapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class customer_choose_dish extends AppCompatActivity {
    List<DataObject.Dish> dishList = new ArrayList<>();
    int shopNo;
    boolean[] check = new boolean[100];
    FloatingActionButton fab;
    customer_choose_dish thisActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopNo = ((MyApp)getApplication()).customer_shop_choice_to_dishes;
        setContentView(R.layout.activity_customer_choose_dish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Choose Dish");
        thisActivity = this;
        check =  ((MyApp) getApplication()).check;
        //----------------------------------\\\
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        //----------------------------------------------------\
        prepare();
        DishAdapter dishAdapter = new DishAdapter(dishList);

        recyclerView.setAdapter(dishAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

           CheckBox c =  (CheckBox)   view.findViewById(R.id.customer_choose_dish_checkbox);
                c.toggle();
                check[position]=!check[position];
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));



        //--FAB-----------------
        fab= (FloatingActionButton)findViewById(R.id.customer_choose_dish_confirm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<DataObject.order_detail> list=new ArrayList<DataObject.order_detail>();
               if(dishList.size()==0){
                   return;
               }


                for(int i=0;i<dishList.size();i++){
                    if(check[i]){
                        DataObject.order_detail x=new DataObject().new order_detail();
                        x.dishID = dishList.get(i).id;
                        x.quantity=1;
                        list.add(x);

                    }
                }
                ((MyApp) getApplication()).setOrders(list);
                startActivity(new Intent(thisActivity ,customer_order_list_pay.class));
            }
        });
    }

    //--------------------------------------------------------------
    class DishAdapter extends RecyclerView.Adapter<DishAdapter.MyViewHolder> {
        List<DataObject.Dish> dishList;

        public DishAdapter(List<DataObject.Dish> a) {
            dishList = a;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.customer_choose_dish_list_row, parent, false);
           return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
           holder.checked.setText((String.valueOf(dishList.get(position).name)));
            holder.price.setText("\u20B9".concat(String.valueOf(dishList.get(position).price)));
            holder.setIsRecyclable(false);
            if(check[position]){
                holder.checked.setChecked(true);
            }

        }

        @Override
        public int getItemCount() {
            return dishList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name, price;
            CheckBox checked;

            public MyViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.customer_choose_dish_name);
                price = (TextView) itemView.findViewById(R.id.customer_choose_dish_price);
                checked = (CheckBox) itemView.findViewById(R.id.customer_choose_dish_checkbox);
            }
        }
    }


    //-----------------------------------------------------------
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private customer_choose_dish.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final customer_choose_dish.ClickListener clickListener) {
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
//-----------------------------------------------------------------------------------
    void prepare(){
        DataObject  curr = ((MyApp)getApplication()).data ;
        DataObject.Shop x = curr.shops.get(shopNo);
        for(int i =0;i<x.dishes.size();i++){
            int dish= x.dishes.get(i);
            if(!curr.Dishes.get(dish).deleted)
            dishList.add(curr.Dishes.get(dish));

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
