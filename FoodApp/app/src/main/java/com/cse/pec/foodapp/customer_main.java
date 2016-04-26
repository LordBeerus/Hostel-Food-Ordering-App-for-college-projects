package com.cse.pec.foodapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class customer_main extends AppCompatActivity {
    customer_main activity;
    DataObject data=new DataObject();
    TextView bal;
    int ADD=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = this;

        if(getIntent().hasExtra("ORDER")){
            Toast.makeText(activity, "Order Placed",
                    Toast.LENGTH_LONG).show();

        }


        data =(DataObject)((MyApp)this.getApplication()).get();
        getSupportActionBar().setTitle("Welcome,".concat(data.c.name));

          bal = (TextView)findViewById(R.id.customer_main_balance);
        bal.setText("Balance : ".concat("\u20B9").concat(" " + String.valueOf(data.c.balance)));
        Button newOrder = (Button)findViewById(R.id.customer_new_order);
        Button prevOrder = (Button)findViewById(R.id.customer_previous_order_button);
        newOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           Intent i = new Intent(activity ,customer_show_shops.class);
                startActivity(i);
            }
        });
        prevOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity,customer_see_previous_orders.class));
            }
        });
        Button add =  (Button)findViewById(R.id.customer_add_cash);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            new AddAsync().execute(String.valueOf(data.c.id));
            }
        });

    }

    class AddAsync extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params)
        {
            InputStream is = null;
            String url= "http://oo7.comuv.com/add_balance.php/?";
            url +="customerID="+params[0]+"&add="+ADD;
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
                String[] arr=A.split(",");
                 return arr[0];

        }catch (Exception e){

            }
        return  "NO,";
        }

        @Override
        protected void onPostExecute(String  result) {
        if(result.equals("YES")){
            ((MyApp)getApplication()).data.c.balance +=ADD;
            data =((MyApp)getApplication()).data;
            bal.setText("Balance : ₹ "+String.valueOf(data.c.balance));

        }else{
            Toast.makeText(activity, "Network Error!",
                    Toast.LENGTH_LONG).show();
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
            Intent intent = new Intent(activity,login.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
