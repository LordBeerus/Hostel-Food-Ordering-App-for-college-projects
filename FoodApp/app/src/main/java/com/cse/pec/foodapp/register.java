package com.cse.pec.foodapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {
    Button reg;
    Intent intent;
    String type="CUSTOMER";
    register thisActivity;
    public final static boolean isValid (CharSequence target,String regex) {
        Pattern p = Pattern.compile( regex,Pattern.CASE_INSENSITIVE);

        Matcher m = p.matcher(target);
        return m.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent = new Intent(this, login.class);
        reg = (Button) findViewById(R.id.register_button);
        thisActivity= this;
       final EditText shopName=  (EditText) findViewById(R.id.register_shop_name);
        final EditText hostoradd=  (EditText) findViewById(R.id.register_address);
        final EditText room=  (EditText) findViewById(R.id.room_no);

        final  TextInputLayout iname , ipass,iphone,iadd,ishop,iroom;
        iname = (TextInputLayout)findViewById(R.id.register_username_input);
        ipass = (TextInputLayout)findViewById(R.id.register_password_input);
        iphone = (TextInputLayout)findViewById(R.id.register_phone_input);
        ishop = (TextInputLayout)findViewById(R.id.register_shop_name_input);
        iadd = (TextInputLayout)findViewById(R.id.register_address_input);
        iroom =  (TextInputLayout)findViewById(R.id.register_room_input);
         RadioGroup grp = (RadioGroup) findViewById(R.id.register_choose_type);
        FloatingActionButton flBut = (FloatingActionButton)findViewById(R.id.fab_register);
        flBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg.callOnClick();
            }
        });
        grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.register_radio_customer) {
                    type = "CUSTOMER";
                    shopName.setVisibility(View.GONE);
                    ishop.setVisibility(View.GONE);
                    iroom.setVisibility(View.VISIBLE);
                     iadd.setHint("Hostel");
                   // hostoradd.setHint("Hostel");
                } else if (checkedId == R.id.register_radio_owner) {
                    type = "OWNER";
                    iadd.setHint("Shop Address/No.");
                  //  hostoradd.setHint("Shop Address/No.");

                    shopName.setVisibility(View.VISIBLE);
                    ishop.setVisibility(View.VISIBLE);
                    iroom.setVisibility(View.GONE);

                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, password, phone, address,shop="",room="-1";
                boolean correct=true;

                EditText u = (EditText) findViewById(R.id.register_username);
                username = u.getText().toString();
                username = username.trim();
                u = (EditText) findViewById(R.id.register_password);
                password = u.getText().toString();

                u = (EditText) findViewById(R.id.register_phone);
                phone = u.getText().toString();
                password=password.trim();

                u = (EditText) findViewById(R.id.register_address);
                address = u.getText().toString();

                u = (EditText) findViewById(R.id.register_shop_name);
                shop = u.getText().toString();
                shop = shop.trim();

                u =  (EditText) findViewById(R.id.room_no);
                room = u.getText().toString();


                iadd.setErrorEnabled(false);
                iname.setErrorEnabled(false);
                ishop.setErrorEnabled(false);
                iphone.setErrorEnabled(false);
                ipass.setErrorEnabled(false);
                if(!isValid(username,"^[\\p{L} .'-]+$" )){
                    iname.setError("Only letters allowed");
                    correct=false;
                    return;
                }else{iname.setErrorEnabled(false);}
                if(!isValid(password,"^[a-zA-Z0-9]{1,100}$" )){
                    ipass.setError("Atleast one letter and no spaces");
                    correct=false;
                    return;
                }else{ipass.setErrorEnabled(false);}
                if(!isValid(phone,"^[0-9]{10,10}$" )){

                    iphone.setError("Use 10 digits in phone and start with 9");
                    correct=false;
                    return;
                } else{iphone.setErrorEnabled(false);}
                if(type.equals("OWNER") && !isValid(shop,"^[a-zA-Z0-9 ]{1,100}$" )){
                    correct=false;
                    ishop.setError("Use only letters and digits in shop name");

                    return;
                }else{ishop.setErrorEnabled(false);}

                if(!isValid(address,"^[a-zA-Z0-9 ]{1,100}$" )){
                    correct=false;
                    iadd.setError("Use only letters and digits in address");

                    return;
                }else{iadd.setErrorEnabled(false);}
                if(username.isEmpty() || password.isEmpty() || phone.isEmpty() || address.isEmpty()  ){

                    return;
                }

                if(type.equals("CUSTOMER") && !isValid(room,"[0-9]{1,1000}") ){
                    iroom.setError("Enter Valid Room No");
                }
                username= username.replace(" ","%20");
                password= password.replace(" ","%20");
                phone= phone.replace(" ","%20");
                address= address.replace(" ","%20");
                room= room.replace(" ","%20");

                new RAsync().execute(type, username, password, phone, address,shop,room);
            }
        });
    }

    class RAsync extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            Request req = new Request();
            boolean ans = req.register(params[0], params[1], params[2], params[3], params[4],params[5],params[6]);
            return ans;
        }

        @Override
        protected void onPostExecute(Boolean result) {


            if (result == false) {
                Snackbar snackbar = Snackbar
                        .make(reg, "Registration Failed", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                snackbar.show();
            } else {
                Snackbar snackbar = Snackbar
                        .make(reg, "Registration Successful", Snackbar.LENGTH_INDEFINITE).setAction("LOGIN", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(intent);
                            }
                        });
                snackbar.show();
                ;
            }


        }

    }
}

