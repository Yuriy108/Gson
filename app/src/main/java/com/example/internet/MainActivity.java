package com.example.internet;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    String adreess="https://www.ukr.net/";
    String pictAdress="https://trkmedia.ollcdn.net/uploads/public/uploads/public/novosti/5c23845f28c8b_27017_nature_is_this_real_1.jpeg";
    ImageView imageView;

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String result= null;
        Download d= new Download();
        try {
            result=d.execute(adreess).get();
            Log.i("Url",result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Pattern pattern=Pattern.compile("<img src=(.*?)\"");
       // Matcher matcher=pattern.matcher(result);
       // Log.i("Link",matcher.group(1));

    }

    public void downloadPict(View view) {
        imageView=findViewById(R.id.imageView);
        DownloadPic downloadPic=new DownloadPic();
        try {
            Bitmap bitmap=downloadPic.execute(pictAdress).get();
            imageView.setImageBitmap(bitmap);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static class  DownloadPic extends AsyncTask<String,Void, Bitmap>{
        Bitmap pict=null;
        URL url=null;
        HttpURLConnection httpURLConnection=null;

         @Override
         protected Bitmap doInBackground(String... strings) {
             try {
                 url= new URL(strings[0]);
                 httpURLConnection= (HttpURLConnection) url.openConnection();
                 InputStream inputStream= httpURLConnection.getInputStream();
                 pict= BitmapFactory.decodeStream(inputStream);
                 return pict;
             } catch (MalformedURLException e) {
                 e.printStackTrace();
             } catch (IOException e) {
                 e.printStackTrace();
             }


             return null;
         }
     }
}
 class Download extends AsyncTask<String,Void,String>{
     @Override
     protected String doInBackground(String... strings) {
         HttpURLConnection urlConnection=null;
         StringBuilder result= new StringBuilder();
         try {
             URL url=new URL(strings[0]);
             urlConnection= (HttpURLConnection) url.openConnection();
             InputStream in= urlConnection.getInputStream();
             InputStreamReader reader=new InputStreamReader(in);
             BufferedReader bufferedReader=new BufferedReader(reader);

             String line =bufferedReader.readLine();
             while (line!=null){
                 result.append(line);
                 line=bufferedReader.readLine();

             }

         } catch (MalformedURLException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         } finally {
             if(urlConnection!=null){
                 urlConnection.disconnect();

             }

         }

         return result.toString();
     }

 }

