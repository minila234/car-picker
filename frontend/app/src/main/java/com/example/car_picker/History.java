package com.example.car_picker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.car_picker.historyRecyclerView.HistoryAdapter;
import com.example.car_picker.historyRecyclerView.HistoryObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    RecyclerView rv;
    public Session session;
    public String username;
    private RecyclerView.Adapter mHistoryAdapter;
    private RecyclerView.LayoutManager mHistoryLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        session =new Session(getApplicationContext());
        username=session.getusename();
        rv=(RecyclerView)findViewById(R.id.recyclerView);
        new GetjouneyData().execute("http://192.168.43.183:3001/journey/"+username);





    }

    private static ArrayList resultsHistory = new ArrayList<HistoryObject>();
    private ArrayList<HistoryObject> getDataSetHistory() {

        return resultsHistory;
    }

    //get data
     class GetjouneyData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            progressDialog = new ProgressDialog(History.this);
            progressDialog.setMessage("Loaging....");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                return getData(params[0]);


            } catch (IOException ex) {
                return "Network error !";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //set data response to textView
            /////// the open new activity
            try{

                rv.setNestedScrollingEnabled(false);
                rv.setHasFixedSize(true);
                mHistoryLayoutManager = new LinearLayoutManager(History.this);
                rv.setLayoutManager(mHistoryLayoutManager);
                mHistoryAdapter = new HistoryAdapter(getDataSetHistory(), History.this);
                rv.getAdapter();
                rv.setAdapter(mHistoryAdapter);

            }catch (Exception e){
                e.printStackTrace();
            }


            //cancel progress dialog
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        private String getData(String urlPath) throws IOException {
            StringBuilder result = new StringBuilder();
            String status = null;
            JSONObject result2 =new JSONObject();
            BufferedReader bufferedReader =null;



            try {
                //Initialize and config request, then connect to server
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(10000 /* milliseconds */);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");// set header
                urlConnection.connect();

                //Read data response from server

                String pass;

                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;


                while ((line = bufferedReader.readLine()) != null) {

                    result.append(line).append("\n");
                }
                String respond =result.toString();
                JSONObject jsonObj = new JSONObject(respond);
                JSONArray arr = jsonObj.getJSONArray("data");
                //JSONObject c  = jsonObj.getJSONObject("data");
                int length = 1;
                int i=0;
                JSONObject c;
                while ((arr.length()>=length)){
                     c = arr.getJSONObject(i);

                        String rideid = c.getString("_id");
                        HistoryObject obj = new HistoryObject(rideid);
                        resultsHistory.add(obj);
                        //mHistoryAdapter.notifyDataSetChanged();
                    ++length;
                  ++i;
                }
                mHistoryAdapter =new HistoryAdapter(resultsHistory,History.this);
                mHistoryAdapter.notifyDataSetChanged();
                status ="sussess";
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }

            }

            return status;

        }
    }
}
