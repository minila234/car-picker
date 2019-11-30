package com.example.car_picker;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button registation,login,map;
    MaterialEditText nic,password;
    static String fname1,lname1,nic1,dob1,pass1,email1,number1;
    static String nicLoging,passwordLoging;
    public DrawerLayout drwaerlayout;
    public ActionBarDrawerToggle mtoggle;
    public Session session;
    CompositeDisposable compositeDisposable =new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new Session(getApplicationContext());

        Retrofit client =Client.getInstance();
        iMyService = client.create(IMyService.class);

        nic =(MaterialEditText)findViewById(R.id.nic);
        password =(MaterialEditText)findViewById(R.id.pass);
        login =(Button)findViewById(R.id.login);

        //nevigation bar
        drwaerlayout=(DrawerLayout)findViewById(R.id.draw);
        mtoggle =new ActionBarDrawerToggle(this,drwaerlayout,R.string.open,R.string.close);
        drwaerlayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //navigation view
        NavigationView navigationView =(NavigationView)findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        map=(Button)findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MainActivity.this,Map.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // loginuser(nic.getText().toString(),password.getText().toString());

                nicLoging= nic.getText().toString();
                passwordLoging= password.getText().toString();
                if(!(nicLoging.trim().matches("^[0-9]{9}[vVxX]$")))
                {
                    Toast.makeText(MainActivity.this, "Invalid NIC", Toast.LENGTH_SHORT).show();

                }

               new GetDataTask().execute("http://192.168.43.183:3001/customer/login/"+nicLoging );

            }
        });

        registation = (Button)findViewById(R.id.reg);
        registation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View registation = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.registation,null);
                new MaterialStyledDialog.Builder(MainActivity.this)
                        .setTitle("Registation")
                        .setDescription("fill the details")
                        .setCustomView(registation)
                        .setNegativeText("CANCEL")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveText("Register")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                MaterialEditText fname =(MaterialEditText)registation.findViewById(R.id.fname);
                                MaterialEditText lname =(MaterialEditText)registation.findViewById(R.id.lname);
                                MaterialEditText nic =(MaterialEditText)registation.findViewById(R.id.nic);
                                //MaterialEditText dob =(MaterialEditText)registation.findViewById(R.id.Dob);
                                MaterialEditText pass =(MaterialEditText)registation.findViewById(R.id.pass);
                                MaterialEditText email =(MaterialEditText)registation.findViewById(R.id.email);
                                MaterialEditText number =(MaterialEditText)registation.findViewById(R.id.phone);

                                    fname1=fname.getText().toString();
                                    lname1=lname.getText().toString();
                                nic1=nic.getText().toString();
                                //dob1=dob.getText().toString();
                               pass1= pass.getText().toString();
                               email1= email.getText().toString();
                                number1=number.getText().toString();


                               /*if(TextUtils.isEmpty(fname.getText().toString())){
                                    Toast.makeText(MainActivity.this, "fname can't be empty", Toast.LENGTH_SHORT).show();

                                }
                                if(TextUtils.isEmpty(lname.getText().toString())){
                                    Toast.makeText(MainActivity.this, "last name can't be empty", Toast.LENGTH_SHORT).show();

                                }
                                if(TextUtils.isEmpty(nic.getText().toString())){
                                    Toast.makeText(MainActivity.this, "nic can't be empty", Toast.LENGTH_SHORT).show();

                                }
                                if(TextUtils.isEmpty(pass.getText().toString())){
                                    Toast.makeText(MainActivity.this, "password can't be empty", Toast.LENGTH_SHORT).show();

                                }
                                if(TextUtils.isEmpty(email.getText().toString())){
                                    Toast.makeText(MainActivity.this, "email can't be empty", Toast.LENGTH_SHORT).show();

                                }
                                if(TextUtils.isEmpty(number.getText().toString())){
                                    Toast.makeText(MainActivity.this, "phone number can't be empty", Toast.LENGTH_SHORT).show();

                                }*/


                                new PostDataTask().execute("http://192.168.43.183:3001/customer/");

                        }
                        }).show();
            }
        });


    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mtoggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void registeruser(String fname, String lname, String nic, String dob, String pass, String email, String phone) {

        compositeDisposable.add(iMyService.registerUser(fname,lname,nic,dob,100,pass,email,phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String respond) throws Exception {
                        Toast.makeText(MainActivity.this, "sussec"+respond, Toast.LENGTH_SHORT).show();

                    }
                }));


    }


    private void loginuser(String nic, String passw) {

        if(TextUtils.isEmpty(nic)){
            Toast.makeText(this, "nic can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(passw)){
            Toast.makeText(this, "password can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        compositeDisposable.add(iMyService.loginUser(nic,passw)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(String respond) throws Exception {
                Toast.makeText(MainActivity.this, "susses"+respond, Toast.LENGTH_SHORT).show();

            }
        }));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id= menuItem.getItemId();

        if(id == R.id.home){
            Intent i1= new Intent(MainActivity.this,Map.class);
            startActivity(i1);
            return true;
        }
        if(id ==R.id.logout)
        {
            String username = session.getusename();
            if(username.isEmpty()){
                Toast.makeText(this, "You must login first", Toast.LENGTH_SHORT).show();
                return true;
            }
            else {
                session.logout();
                Toast.makeText(this, "LogOut", Toast.LENGTH_SHORT).show();

                return true;
            }
        }
        if(id == R.id.histry){
            Intent i2= new Intent(MainActivity.this,History.class);
            startActivity(i2);
            return true;
        }
        if(id == R.id.profile){
            Intent i3= new Intent(MainActivity.this,Profile.class);
            startActivity(i3);
            return true;
        }
        return false;
    }

    //custermer

    class PostDataTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Inserting data...");
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

            //

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        private String postData(String urlPath) throws IOException, JSONException {

            final StringBuilder result = new StringBuilder();
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;
            // fll
            if(fname1.isEmpty() || lname1.isEmpty()||nic1.isEmpty()||pass1.isEmpty()||email1.isEmpty()||email1.isEmpty()){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "one or more field empty!!!",
                                Toast.LENGTH_LONG).show();

                    }
                });

            }
            else {

                //NIC validation
                if(!(nic1.trim().matches("^[0-9]{9}[vVxX]$")))
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Invalid NIC !!!",
                                    Toast.LENGTH_LONG).show();

                        }
                    });



                }
                else {


                    try {
                        //Create data to send to server


                        JSONObject dataToSend = new JSONObject();
                        dataToSend.put("First_Name", fname1);
                        dataToSend.put("Last_Name", lname1);
                        dataToSend.put("NIC_Passport_No", nic1);
                        //dataToSend.put("DateOfBirth", dob1);
                        //dataToSend.put("Deposit_Amount", 100);
                        dataToSend.put("Password", pass1);
                        dataToSend.put("Email_Address", email1);
                        dataToSend.put("Mobile", number1);

                        //Initialize and config request, then connect to server.
                        URL url = new URL(urlPath);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setReadTimeout(10000 /* milliseconds */);
                        urlConnection.setConnectTimeout(10000 /* milliseconds */);
                        urlConnection.setRequestMethod("POST");
                        urlConnection.setDoOutput(true);  //enable output (body data)
                        urlConnection.setRequestProperty("Content-Type", "application/json");// set header
                        urlConnection.connect();

                        //Write data into server
                        OutputStream outputStream = urlConnection.getOutputStream();
                        bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                        bufferedWriter.write(dataToSend.toString());
                        bufferedWriter.flush();

                        //Read data response from server
                        InputStream inputStream = urlConnection.getInputStream();
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            result.append(line).append("\n");
                        }
                    } finally {
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                        if (bufferedWriter != null) {
                            bufferedWriter.close();
                        }
                    }
                }
            }  // else close
            return result.toString();

        }
    }

    class GetDataTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
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

                if(arr.length()!=0) {


                    JSONObject c = arr.getJSONObject(0);
                    String password = c.getString("Password");
                    if (password.equals(passwordLoging)) {

                        status = "Login successfully";
                        session.setusename(nicLoging);

                    } else {

                        status = "Invalid details";
                       // Toast.makeText(MainActivity.this, "Invalid details", Toast.LENGTH_SHORT).show();
                        //return status;
                    }
                }
                else{
                    status ="invalid nic";
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }

            }

            final String finalStatus = status;
            if(status.equals("Login successfully")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                finalStatus,
                                Toast.LENGTH_LONG).show();
                        Intent i = new Intent(MainActivity.this, Map.class);
                        startActivity(i);
                    }
                });
            }
            else if(status.equals("Invalid details")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                finalStatus,
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
            else if(status.equals("invalid nic")) {
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


}
