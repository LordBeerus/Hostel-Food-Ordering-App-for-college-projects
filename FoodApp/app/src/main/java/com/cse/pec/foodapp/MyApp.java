package com.cse.pec.foodapp;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by HP LAPTOP on 03-04-2016.
 */
public class MyApp extends Application {
    int customer_shop_choice_to_dishes = -1;
    public DataObject data=new DataObject();
    public ArrayList<DataObject.order_detail> custOrders;
    boolean[] check = new boolean[100];
    public void set(DataObject data){
       this.data=data;
   }
  public  DataObject get(){return data;}
    public void setOrders(ArrayList<DataObject.order_detail>x ){
        custOrders = x;
    }

    public void setCSCTD(int id){
        customer_shop_choice_to_dishes =id;
    }

}
