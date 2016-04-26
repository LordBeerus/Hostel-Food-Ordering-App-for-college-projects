package com.cse.pec.foodapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class login extends AppCompatActivity {
    String type = "CUSTOMER";
    Button log,reg;
    Intent intent;
    login A;
    DataObject data = new DataObject();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        A = this;

        getSupportActionBar().setTitle("Login");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        log = (Button) findViewById(R.id.login_button);
        RadioGroup grp = (RadioGroup) findViewById(R.id.login_choose_type);
        reg = (Button)findViewById(R.id.register_button);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(A,register.class);
                startActivity(i);
            }
        });
        grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.login_radio_customer) {
                    type = "CUSTOMER";
                } else if (checkedId == R.id.login_radio_owner) {
                    type = "OWNER";
                }
                if (type.equals("CUSTOMER")) {
                    intent = new Intent(A, customer_main.class);
                } else if (type.equals("OWNER")) {
                    intent = new Intent(A, owner_main.class);
                }
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText user = (EditText) findViewById(R.id.login_username);
                String userName = user.getText().toString();
                user = (EditText) findViewById(R.id.login_password);
                String pass = user.getText().toString();
                Snackbar.make(log, "Logging in.....", Snackbar.LENGTH_INDEFINITE).setAction("OK", null).setDuration(Snackbar.LENGTH_SHORT).show();
                userName = userName.replace(" ","%20");
                pass = pass.replace(" ","%20");
                new RAsync().execute(type, userName, pass);
            }
        });


     }


    class RAsync extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            Request req = new Request();
            String[] ans = req.login(params[0], params[1], params[2]);

            return ans;
        }

        @Override
        protected void onPostExecute(String[] result) {


            if ( result[0].equals("YES")) {


                try {
                    if (type.equals("CUSTOMER")) {
                        storeCustomer(result);
                    } else {
                        storeOwner(result);
                    }
                }catch(Exception e)
                {

                    Snackbar snackbar = Snackbar
                            .make(log, "Login Failed", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                    snackbar.show();
                    return;
                }






                Intent i;
                if (type.equals("CUSTOMER")) {

                    i = new Intent(A, customer_main.class);

                } else {
                    i = new Intent(A, owner_main.class);

                }
                try{

                    ((MyApp)A.getApplication()).set(data);
                startActivity(i);}catch(Exception e){
                    Log.d("AAAAAAAA",e.toString()

                    );
                }

            }
            else if(result[0].equals("W")){
                Snackbar snackbar = Snackbar
                        .make(log, "Wrong Username / Password", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });

                snackbar.show();
            }
            else {
                Snackbar snackbar = Snackbar
                        .make(log, "Login Failed", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                snackbar.show();

            }


        }

    }

    void storeCustomer(String[] ans) {

        data.user = 1;
        //name customerid,add,phone
        int in = 1;
        data.c.name = ans[in++];
        data.c.id = Integer.parseInt(ans[in++]);
        data.c.address = ans[in++];
        data.c.room_no = ans[in++];
        data.c.balance = Double.parseDouble(ans[in++]);
        data.c.phone = ans[in++];
        int totShops = Integer.parseInt(ans[in++]);
        //id name status owner


        for (int i = 0; i < totShops; i++) {
            DataObject.Shop a = new DataObject().new Shop() ;

            a.id = Integer.parseInt(ans[in++]);
            int id = a.id;
            a.name = ans[in++];
            a.status = ans[in++];
            a.ownerID = Integer.parseInt(ans[in++]);



            //no of dishes
            int totDish = Integer.parseInt(ans[in++]);


            for (int j = 0; j < totDish; j++) {
                DataObject.Dish b =new DataObject().new Dish();
//id name price photo shop
                b.id = Integer.parseInt(ans[in++]);
                b.name = ans[in++];
                b.price = Double.parseDouble(ans[in++]);

                b.shopID = Integer.parseInt(ans[in++]);
                b.deleted = ans[in++].equals("0")?false:true;
                if(b.deleted)
                    a.numDel++;
                a.dishes.add(b.id);
                data.Dishes.put(b.id, b);
            }
            data.shops.put(a.id, a);
            data.shopsByOwnerId.put(a.ownerID,a);
        }





        int orders = Integer.parseInt(ans[in++]);
        for (int j = 0; j < orders; j++) {
            // order dish customer owner date status
            DataObject.order c = new DataObject().new order() ;
            c.orderID = Integer.parseInt(ans[in++]);

            in++;
            c.dishID =-1;

            c.customerID = Integer.parseInt(ans[in++]);
            c.ownerID = Integer.parseInt(ans[in++]);
            c.date = ans[in++];
            c.status = ans[in++];
            c.amount = Double.parseDouble(ans[in++]);

            data.Orders.put(c.orderID, c);
            data.c.orders.add(c.orderID);
            int detailNum = Integer.parseInt(ans[in++]);
            ArrayList<DataObject.order_detail> orderDetails=new ArrayList<>();
            for(int k =0 ; k<detailNum;k++){
                DataObject.order_detail det = new DataObject().new order_detail();
              det.orderID = c.orderID;
              det.dishID = Integer.parseInt(ans[in++]);
               det.quantity = Integer.parseInt(ans[in++]);
               orderDetails.add(det);
            }
            if(orderDetails.size()>0)
            { data.orderDetails.put(c.orderID,orderDetails);}

            int g;
        }


    }

    void storeOwner(String[] ans) {
        data.user = 0;
        //name owner addre phone shop
        int in = 1;
        data.o.name = ans[in++];
        data.o.id = Integer.parseInt(ans[in++]);
        data.o.address = ans[in++];
        data.o.phone = ans[in++];
        data.o.shopId = Integer.parseInt(ans[in++]);
        data.o.balance = Double.parseDouble(ans[in++]);
        //no of deishe
        data.shopz.id = data.o.shopId;
        data.shopz.ownerID = data.o.id;
        data.shopz.status = ans[in++];
        data.shopz.name = ans[in++];
        int dishes = Integer.parseInt(ans[in++]);
        for (int i = 0; i < dishes; i++)
        {
            //iid name price
            DataObject.Dish k =new DataObject().new Dish();
            k.id = Integer.parseInt(ans[in++]);
            k.name = ans[in++];
            k.price = Double.parseDouble(ans[in++]);
            k.deleted = ans[in++].equals("0")?false:true;


            data.shopz.dishes.add(k.id);
            data.Dishes.put(k.id, k);
        }
    int orders = Integer.parseInt(ans[in++]);
    for (int j = 0; j < orders; j++) {
        // order dish customer owner date status
        DataObject.order c =  new DataObject().new order();
        c.orderID = Integer.parseInt(ans[in++]);
        in++;
        c.dishID=-1;
        c.customerID = Integer.parseInt(ans[in++]);
        c.ownerID = Integer.parseInt(ans[in++]);
        c.date = ans[in++];
        c.status = ans[in++];
        c.amount = Double.parseDouble(ans[in++]);
        c.custName = ans[in++];
        c.custHostel = ans[in++];
        c.custRoom = ans[in++];
        data.Orders.put(c.orderID, c);
        data.o.orders.add(c.orderID);
        int detailNum = Integer.parseInt(ans[in++]);
        ArrayList<DataObject.order_detail> orderDetails=new ArrayList<>();
        for(int k =0 ; k<detailNum;k++){
            DataObject.order_detail det = new DataObject().new order_detail();
            det.orderID = c.orderID;
            det.dishID = Integer.parseInt(ans[in++]);
            det.quantity = Integer.parseInt(ans[in++]);
            orderDetails.add(det);
        }
        data.orderDetails.put(c.orderID, orderDetails);
    }


}

}

