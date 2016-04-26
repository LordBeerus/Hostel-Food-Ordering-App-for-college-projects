package com.cse.pec.foodapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;






public class Request {
    public static String readIt(InputStream stream, int len) throws IOException{
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    public String addDish(String name,String price,String shopID){


        String url = "http://oo7.comuv.com/addDish.php/?name=".concat(name).concat("&price=").concat(price).concat("&shopid=").concat(shopID);

        InputStream is;


        String ans;
        int len = 2000;
        try {
            URL U = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) U.openConnection();
            conn.setReadTimeout(13000 /* milliseconds */);
            conn.setConnectTimeout(13000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            ans = readIt(is, len);
           String[] ans1 = ans.split(",") ;
            if(ans1[0].equals("YES"))
            {
                return "YES,"+ans1[1];

            }
            else
                return "NO";
        }catch (Exception e){
            return "NO";
        }


    }

    boolean register(String  type,String UserName,String password,String phone, String address,String shop,String room ){
        InputStream is = null;
        String url;
        try
        {password=(new AeSimpleSHA1()).SHA1(password);
        }catch(Exception e){}

            url = "http://oo7.comuv.com/register.php/?username=".concat(UserName).concat("&password=").concat(password)
                    .concat("&phone=").concat(phone).concat("&address=").concat(address).
                            concat("&shop=").concat(shop).concat("&type=").concat(type).concat("&roomNo=").concat(room);



        String ans;
        int len = 2000;
        try {
            URL U = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) U.openConnection();
            conn.setReadTimeout(13000 /* milliseconds */);
            conn.setConnectTimeout(13000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            ans = readIt(is, len);
            ans = ans.split(",")[0];
             if(ans.equals("YES"))
                 return true;
            else
                 return false;
        }catch (Exception e){
            return false;
        }


    }
    String[] login(String type,String username,String password){
        InputStream is = null;
        try
        {password=(new AeSimpleSHA1()).SHA1(password);
        }catch(Exception e){}
        String url="";
        if(type.equals("CUSTOMER"))
        { url = "http://oo7.comuv.com/login_customer.php/?username=".concat(username).concat("&password=").concat(password);}
        else if(type.equals("OWNER"))
        { url = "http://oo7.comuv.com/login_owner.php/?username=".concat(username).concat("&password=").concat(password);  }

        String[] ans=new String[500];
        int len = 5000;
        try {
            URL U = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) U.openConnection();
            conn.setReadTimeout(13000 /* milliseconds */);
            conn.setConnectTimeout(13000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            is = conn.getInputStream();
            String A = readIt(is,len);
            ans=A.split(",");

           return ans;



        }catch (Exception e){


        }
        ans[0]="NO";
        return ans;
    }


    String chageStatusShop(String S, String shopID){
        InputStream is = null;
        String url= "http://oo7.comuv.com/changeStatus.php/?status=".concat(S).concat("&shopId=").concat(shopID);
        String ans;
        try {
            URL U = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) U.openConnection();
            conn.setReadTimeout(13000 /* milliseconds */);
            conn.setConnectTimeout(13000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            is = conn.getInputStream();
            String A = readIt(is,2000);
            ans=A.split(",")[0];


            return ans;



        }catch (Exception e){


        }


    return "NO";
    }







      class AeSimpleSHA1 {
        private  String convertToHex(byte[] data) {
            StringBuilder buf = new StringBuilder();
            for (byte b : data) {
                int halfbyte = (b >>> 4) & 0x0F;
                int two_halfs = 0;
                do {
                    buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                    halfbyte = b & 0x0F;
                } while (two_halfs++ < 1);
            }
            return buf.toString();
        }

        public  String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            byte[] sha1hash = md.digest();
            return convertToHex(sha1hash);
        }
    }
}
