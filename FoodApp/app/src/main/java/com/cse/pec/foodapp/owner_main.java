package com.cse.pec.foodapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class owner_main extends AppCompatActivity {
    Button addDish, currDishes, prevOrders, shop,currOrders;
    owner_main activity;
    DataObject data = new DataObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = this;
        data = (DataObject) ((MyApp) this.getApplication()).get();
        getSupportActionBar().setTitle("Welcome,".concat(data.o.name));
//------------------------------------------
        currDishes = (Button) findViewById(R.id.owner_dish_button);
        currDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity,owner_see_dishes.class));
            }
        });

//------------------------------------------
        prevOrders = (Button)findViewById(R.id.owner_previous_order_button);
        prevOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity,owner_see_prev_dishes.class));
            }
        });
//------------------------------
        currOrders = (Button)findViewById(R.id.owner_current_order_button);
        currOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity,owner_current_orders.class));
            }
        });

        //------------------------------------
        TextView bal = (TextView)findViewById(R.id.owner_main_balance);
        bal.setText("Balance : ".concat("\u20B9 ").concat(String.valueOf(data.o.balance)));



        addDish = (Button) findViewById(R.id.owner_add_new_dish_button);
        shop = (Button) findViewById(R.id.owner_open_close_button);

        addDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, add_dish_owner.class);
                startActivity(intent);
            }
        });


        if(data.shopz.status.equals("CLOSED")){
            shop.setText("OPEN SHOP");
        }

        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg,status;

                if (data.shopz.status.equals("OPEN")) {
                    msg = "Confirm Closing Shop";
                    status = "CLOSED";

                } else {
                    status= "OPEN";
                    msg = "Confirm Opening Shop";
                }


                Snackbar snackbar = Snackbar
                        .make(shop, msg, Snackbar.LENGTH_INDEFINITE).setAction("Confirm", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                             new ShopAsync().execute(status,String.valueOf(data.shopz.id));

                            }
                        });
                snackbar.show();

            }
        });

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
            Intent intent = new Intent(activity, login.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


class ShopAsync extends AsyncTask<String,Void,String>{

    @Override
    protected String doInBackground(String... params) {
         String ans;
         ans=new Request().chageStatusShop(params[0],params[1]);
        return ans;
    }

    @Override
    protected void onPostExecute(String  result) {

        if(!result.equals("YES")){
            Toast.makeText(activity, "Server Or Network Failure.Retry.",
                    Toast.LENGTH_LONG).show();
        }
        else{
            String msg="CLOSED";
            shop.setText("OPEN SHOP");
            if(data.shopz.status.equals("CLOSED"))
            {msg="OPEN";
            shop.setText("CLOSE SHOP");}
            data.shopz.status=msg;
            ((MyApp)getApplication()).data.shopz.status=msg;
            Toast.makeText(activity, "Shop ".concat(msg),
                    Toast.LENGTH_LONG).show();
        }

    }

}

}
