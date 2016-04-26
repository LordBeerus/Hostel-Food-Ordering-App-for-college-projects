package com.cse.pec.foodapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class add_dish_owner extends AppCompatActivity {
    add_dish_owner activity;
    String name,price;

    public final static boolean isValid (CharSequence target,String regex) {
        Pattern p = Pattern.compile( regex,Pattern.CASE_INSENSITIVE);

        Matcher m = p.matcher(target);
        return m.matches();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish_owner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add a dish");
        activity = this;
        final DataObject data =(DataObject) ((MyApp)this.getApplication()).get();
        final TextInputLayout iname,iprice;
        iname=(TextInputLayout)findViewById(R.id.add_dish_name_input);
        iprice=(TextInputLayout)findViewById(R.id.add_dish_price_input);
        Button add = (Button)findViewById(R.id.add_new_dish_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText u = (EditText) findViewById(R.id.add_new_dish_name);
                name = u.getText().toString();
                double price_;
                u = (EditText) findViewById(R.id.add_new_dish_price);
                price = u.getText().toString();

                iname.setErrorEnabled(true);
                iprice.setErrorEnabled(true);

                if(!isValid(name,"^[\\p{L} .'-]+$" )){
                    iname.setError("Only letters allowed" );

                    return;
                }else{iname.setErrorEnabled(false);}
                if(!isValid(price,"[0-9]{1,5}" )){

                    iprice.setError("Enter value in right format eg. 3 , 50 ,123" );

                    return;
                } else{iprice.setErrorEnabled(false);}
                name = name.replace(" ","%20");
                 new addDishAsync().execute(name,price,String.valueOf(data.o.shopId));
            }
        });
    }
    class addDishAsync extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {

            return new Request().addDish(params[0],params[1],params[2]);
        }
        @Override
        protected void onPostExecute(String result) {
            String[] two = result.split(",");
            if(two[0].equals("YES")){
                Toast.makeText(activity, "Dish Added",
                        Toast.LENGTH_LONG).show();

                DataObject.Dish a = new DataObject().new Dish();
                a.id = Integer.parseInt(two[1]);
                a.name = name;
                a.price=Integer.parseInt(price);
                a.deleted = false;
                a.shopID =  ((MyApp) getApplication()).data.shopz.id;
                ((MyApp) getApplication()).data.shopz.dishes.add(a.id);
                        ((MyApp) getApplication()).data.Dishes.put( a.id,a);
             }else{
                Toast.makeText(activity, "Dish could not be added.Retry",
                        Toast.LENGTH_LONG).show();



            }


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
            Intent intent = new Intent(activity,login.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
