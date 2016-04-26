package com.cse.pec.foodapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class owner_see_dishes extends AppCompatActivity {
    DataObject data;
    RecyclerView recyclerView;
    owner_see_dishes thisActivity;
    ArrayList<DataObject.Dish> dishes;
    DishAdapter dishAdapter;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_see_dishes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        thisActivity=this;
        data =(DataObject) ((MyApp)this.getApplication()).get();
        getSupportActionBar().setTitle("Welcome,".concat(data.o.name));
        //-------------------------------
         recyclerView = (RecyclerView) findViewById(R.id.owner_see_dish_recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        prepareData();
        dishAdapter = new DishAdapter(dishes);
        recyclerView.setAdapter(dishAdapter);
        //--------------------------
    }

    private void prepareData() {
        dishes = new ArrayList<>();
        int shopId = data.o.shopId;
        ArrayList<Integer> list = data.shopz.dishes;
        for(int i = 0 ;i<list.size();i++){
            dishes.add(data.Dishes.get(list.get(i)));
        }


    }

    class delAsync extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            InputStream is ;
            String shopId = params[0],dishId = params[1],deleted=params[2];
            String url= "http://oo7.comuv.com/changeStatusDish.php/?shopId=".concat(shopId).concat("&dishId=").concat(dishId)
                    .concat("&deleted=").concat(deleted);
            String ans;
            try {
                URL U = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) U.openConnection();
                conn.setReadTimeout(3000 /* milliseconds */);
                conn.setConnectTimeout(3000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                is = conn.getInputStream();
                String A = Request.readIt(is,400);
                String[] x=A.split(",") ;

                return x[0];



            }catch (Exception e){


            }
            return "NO";

        }
        @Override
        protected void onPostExecute(String  result) {
              result = result.replaceAll("\\s","");
            if(result.equals("YES")){
                Toast.makeText(thisActivity, "Update Complete",
                        Toast.LENGTH_LONG).show();
                 dishes.get(pos).deleted=!dishes.get(pos).deleted;

                data.Dishes.get(dishes.get(pos).id).deleted =  dishes.get(pos).deleted;
                if( data.Dishes.get(dishes.get(pos).id).deleted ){
                    data.shopz.numDel++;
                }else{
                    data.shopz.numDel--;
                }
               dishAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(thisActivity, "Could not complete operation.Try again later. " ,
                        Toast.LENGTH_LONG).show();
            }

        }
    }




    class DishAdapter extends RecyclerView.Adapter<DishAdapter.MyViewHolder> {
        List<DataObject.Dish> dishes;

        public DishAdapter(List<DataObject.Dish> a) {
            dishes = a;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.owner_see_dish_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.name.setText(dishes.get(position).name);
            holder.price.setText("\u20B9 " +String.valueOf(dishes.get(position).price));
           if(dishes.get(position).deleted==true){
               holder.statusDel.setImageResource(android.R.drawable.presence_busy);
               holder.delete.setImageResource(android.R.drawable.ic_input_add);
           }else{
               holder.statusDel.setImageResource(android.R.drawable.presence_online);
               holder.delete.setImageResource(android.R.drawable.ic_menu_delete);

           }

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                pos = position;
                    String shopId , dishId;

                    shopId =String.valueOf( data.shopz.id);
                    dishId =String.valueOf(dishes.get(position).id);
                    int deleted = (dishes.get(position).deleted==true)?0:1;
                    new delAsync().execute(shopId,dishId,String.valueOf(deleted));
                }
            });
        }

        @Override
        public int getItemCount() {
            return dishes.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name,price ;
            ImageView statusDel,delete;

            public MyViewHolder(View itemView) {
                super(itemView);
                name= (TextView) itemView.findViewById(R.id.owner_see_dish_name);
                price=(TextView) itemView.findViewById(R.id.owner_see_dish_price);
                statusDel = (ImageView)itemView.findViewById(R.id.owner_see_dish_deleted);
                delete=(ImageView)itemView.findViewById(R.id.owner_see_dish_delete);
            }
        }
    }











    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_option, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Intent intent = new Intent(thisActivity, login.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
