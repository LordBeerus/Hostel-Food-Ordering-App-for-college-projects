package com.cse.pec.foodapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class DataObject implements Serializable {
    public class customer{
        int id;
        double balance;
        String room_no,name,address,phone;
        ArrayList<Integer> orders=new ArrayList<>();
    }
    public class owner{
        int id,shopId;
        double balance;
        String name,address,phone;
        ArrayList<Integer> orders=new ArrayList<>();;

    }
    public class order implements Comparable<order>{
        int orderID,customerID,ownerID,dishID;
        String status,date,custName,custHostel,custRoom;
        double amount;



        @Override
        public int compareTo(order another) {
            if( this.date.compareTo(another.date)==1)
                 return -1;
             else  if( this.date.compareTo(another.date)==-1)
                return 1;
            else return 0;
        }
    }
    public class order_detail{
        int orderID,dishID,quantity;

    }
    public class Dish{
        String name;
        double price;
        int id,shopID,photo;
        boolean deleted;
    }
    public class Shop{
        int id,ownerID;
        String name,status;
        ArrayList<Integer> dishes=new ArrayList<>();
        int numDel;
    }
    public HashMap<Integer , ArrayList<order_detail>> orderDetails=new HashMap<Integer, ArrayList<order_detail>>();
    public customer c = new customer();
    public owner o=new owner();
    public Shop shopz=new Shop();
    public  HashMap<Integer,Shop> shopsByOwnerId=new HashMap<>();

    public  HashMap<Integer,Shop> shops=new HashMap<>();
    public  HashMap<Integer,order> Orders=new HashMap<>();;
    public HashMap<Integer,Dish> Dishes=new HashMap<>();;
    public int user=-1;//0->cus 1->owner
}
