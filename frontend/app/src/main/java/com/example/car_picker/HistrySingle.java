package com.example.car_picker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.car_picker.historyRecyclerView.HistoryAdapter;
import com.example.car_picker.historyRecyclerView.HistoryObject;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class HistrySingle extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, RoutingListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private SupportMapFragment mMapFragment;
    TextView car,date,pickup,destination;
    String rid;
    String pickuploaction,destinationlocation,datetime,carnum;
    private LatLng pick,dest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histry_single);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.sinnglemap);
        mMapFragment.getMapAsync(this);

        car=(TextView)findViewById(R.id.carnuber);
        date=(TextView)findViewById(R.id.date);
        pickup=(TextView)findViewById(R.id.pick);
        destination=(TextView)findViewById(R.id.dest);


        Intent intent =getIntent();
        Bundle bundle=intent.getExtras();
        rid=bundle.get("rideId").toString();
        Log.i("tag",""+rid);
        new GetjouneyData().execute("http://192.168.43.183:3001/journey/history/"+rid);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        buildGoogleApiClient();

    }
    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {

    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    class GetjouneyData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            progressDialog = new ProgressDialog(HistrySingle.this);
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

            car.setText("Car No : "+carnum);
            date.setText("Data and Time : "+datetime);
            pickup.setText("pickup location : "+pickuploaction);
            destination.setText("destination location"+destinationlocation);
            List<Address> addressList2;
            List<Address> addressList3;
            Geocoder coder2;
            coder2 = new Geocoder(HistrySingle.this);


            try {
                StringTokenizer str =new StringTokenizer(pickuploaction," ");
                str.nextElement();
                String lon= str.nextElement().toString();
                StringTokenizer str1 =new StringTokenizer(lon,"(");

                StringTokenizer str2 =new StringTokenizer(str1.nextElement().toString(),")");
                String loglancord = str2.nextElement().toString();
                StringTokenizer str3 =new StringTokenizer(loglancord,",");
                String Latitude=str3.nextElement().toString();
                StringTokenizer str4 =new StringTokenizer(loglancord,",");
               str4.nextElement();
               String Longitude =str4.nextElement().toString();

               //destination
                StringTokenizer strd =new StringTokenizer(destinationlocation," ");
                strd.nextElement();
                String lond= strd.nextElement().toString();
                StringTokenizer str1d =new StringTokenizer(lond,"(");

                StringTokenizer str2d =new StringTokenizer(str1d.nextElement().toString(),")");
                String loglancordd = str2d.nextElement().toString();
                StringTokenizer str3d =new StringTokenizer(loglancordd,",");
                String Latituded=str3d.nextElement().toString();
                StringTokenizer str4d =new StringTokenizer(loglancordd,",");
                str4d.nextElement();
                String Longituded =str4d.nextElement().toString();




                    pick = new LatLng(Double.parseDouble(Latitude), Double.parseDouble(Longitude));
                    dest = new LatLng(Double.parseDouble(Latituded), Double.parseDouble(Longituded));




            } catch (Exception ex) {

                ex.printStackTrace();
            }

            mMap.addMarker(new MarkerOptions().position(pick).title("Pickup Here"));
            mMap.addMarker(new MarkerOptions().position(dest).title("Destination Here"));
            getRouteToMarker(pick,dest);


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

                if(arr.length()!=0) {


                    JSONObject c = arr.getJSONObject(0);
                     pickuploaction = c.getString("start");
                   destinationlocation = c.getString("end");
                    datetime = c.getString("date");
                    carnum = c.getString("carNo");
                        status="Sussfully loarded";
                }
                else{
                    status ="no details";
                }

                status ="sussess";
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }

            }

            final String finalStatus = status;
            if(status.equals("no details")) {
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

    private void getRouteToMarker(LatLng pickup,LatLng destination) {

        if (pickup != null && destination != null){
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(false)
                    .waypoints(pickup,destination)
                    .key("AIzaSyBiv-UINOEoJZwj4PagzobXNNDwlXP9UP8")
                    .build();
            routing.execute();
        }
    }
}
