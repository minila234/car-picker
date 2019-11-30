package com.example.car_picker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Profile extends AppCompatActivity {

   static EditText fnameET,lnameET,mobileET,emailET,nicET,passwordET;
    String fname,lname,mobile,email,nic,pass,fnameset,lnameset,mobileset,nicset,emailset,passwordset;
    private Session session;
    String username;
    Button update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        session =new Session(getApplicationContext());
        username=session.getusename();

        fnameET=(EditText)findViewById(R.id.fname);
        lnameET=(EditText)findViewById(R.id.lname);
        mobileET=(EditText)findViewById(R.id.mobile);
        emailET=(EditText)findViewById(R.id.email);
        nicET=(EditText)findViewById(R.id.nic);
        passwordET=(EditText)findViewById(R.id.pass);

        update=(Button)findViewById(R.id.put);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname= fnameET.getText().toString();
                lname =lnameET.getText().toString();
                mobile= mobileET.getText().toString();
                email= emailET.getText().toString();
                nic = nicET.getText().toString();
                pass= passwordET.getText().toString();
                new UpdatDataTask().execute("http://192.168.43.183:3001/customer/"+username);
            }
        });


        new GetDataTask().execute("http://192.168.43.183:3001/customer/login/"+username);
    }

    class GetDataTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            progressDialog = new ProgressDialog(Profile.this);
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
                fnameET.setText(fnameset);
                lnameET.setText(lnameset);
                nicET.setText(nicset);
                passwordET.setText(passwordset);
                emailET.setText(emailset);
                mobileET.setText(mobileset);
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

                if(arr.length()!=0){


                JSONObject c = arr.getJSONObject(0);

               fnameset=(c.getString("First_Name"));
                lnameset=(c.getString("Last_Name"));
                nicset=(c.getString("NIC_Passport_No"));
               passwordset=(c.getString("Password"));
              emailset=(c.getString("Email_Address"));
                mobileset=(c.getString("Mobile"));
                status="sussesfully loard";
                }
                else{
                    status="Invalid username";
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }

            }
            final String finalStatus = status;
            if(status.equals("Invalid username")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                finalStatus,
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
            return status;


        }
    }

    class UpdatDataTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            progressDialog = new ProgressDialog(Profile.this);
            progressDialog.setMessage("Updating....");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                return postData(params[0]);
            } catch (IOException ex) {
                return "Network error !";
            } catch (JSONException ex) {
                return "Data Invalid !";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //set data response to textView
            /////// the open new activity

            //cancel progress dialog
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        private String postData(String urlPath) throws IOException, JSONException {

            StringBuilder result = new StringBuilder();
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;

            try {
                //Create data to send to server


                JSONObject dataToSend = new JSONObject();
                dataToSend.put("First_Name", fname);
                dataToSend.put("Last_Name", lname);
                dataToSend.put("NIC_Passport_No", nic);
                dataToSend.put("Password", pass);
                dataToSend.put("Email_Address", email);
                dataToSend.put("Mobile", mobile);

                //Initialize and config request, then connect to server.
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(10000 /* milliseconds */);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setDoOutput(true);  //enable output (body data)
                urlConnection.setRequestProperty("Content-Type", "application/json");// set header
                urlConnection.connect();

                //Write data into server
                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();

                //Read data response from server
                /*InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }*/
                if(urlConnection.getResponseCode()==200){
                    return "Successfully updated";
                }
                else {
                    return "Updated failed !";
                }
            } finally {

                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            }


        }
    }
}
