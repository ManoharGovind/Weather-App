package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import org.json.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView result;
    public class DownloadContent extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls) {
            String res="";
            URL url;
            HttpURLConnection connection=null;
            try{
                url=new URL(urls[0]);
                connection=(HttpURLConnection)url.openConnection();
                InputStream in=connection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1){
                    char c=(char)data;
                    res=res+c;
                    data=reader.read();
                }
                return res;
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                JSONObject jsonObject=new JSONObject(s);
                String weatherinfo=jsonObject.getString("weather");
                Log.i("weather",weatherinfo);
                JSONArray arr=new JSONArray(weatherinfo);
                String mes="";
                Log.i("answer",String.valueOf(arr.length()));
                for(int i=0;i<arr.length();i++){
                    JSONObject obj=arr.getJSONObject(i);
                    String main=obj.getString("main");
                    String description=obj.getString("description");
                    if(!(main.equals("") && description.equals(""))){
                        mes=mes+main+" : "+description+"\r\n";
                    }
                }
                if(!mes.equals("")){
                    result.setText(mes);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }
    public void goo(View view){
        DownloadContent downloadContent=new DownloadContent();
         editText=(EditText)findViewById(R.id.editText);
         result=(TextView)findViewById(R.id.answer);
        try{
            downloadContent.execute("http://api.openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&appid=c0cffe177ce14ab84aa434cc9c320568");
        }
        catch (Exception e){
            e.printStackTrace();;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
