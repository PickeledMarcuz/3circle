package io.markryan.a3circle;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    // the foursquare client_id and the client_secret
    private static final String CLIENT_ID = "CYEMKOM4OLTP5PHMOFVUJJAMWT5CH5G1JBCYREATW21XLLSZ";
    private static final String CLIENT_SECRET = "GYNP4URASNYRNRGXR5UEN2TGTKJHXY5FGSAXTIHXEUG1GYM2";
    private static String userQuery;

    private String TAG = MainActivity.class.getSimpleName();

    private ListView listView;
    private EditText userInput;
    private ListAdapter adapter;

    //Location Management / Listener
    LocationManager locationManager;
    LocationListener locationListener;
    private Location currentLocation;
    private Location lastKnownLocation;

    // URL to get venues/search JSON using lat lng from location manager. Using the near field if lat lng not returned the default will be local area.
    String url = "https://api.foursquare.com/v2/venues/search?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=20170617&ll=60,24&near=HELSINKI&m=foursquare&query=";
    String updatedURL;

    // HashMap for the venues array returned from JSON.
    ArrayList<HashMap<String, String>> venuesList;

    public void updateLocationInfo(Location location) {

        Log.i("LocationInfo", location.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Check do we have the permission to access location
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // 0, 0 is min time + min distance
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20, 10, locationListener);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        venuesList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);

        userInput = (EditText) findViewById(R.id.userInput);
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                venuesList.clear();

                userQuery = String.valueOf(userInput.getText());
                updatedURL = updatedURL + userQuery;

                //new GetContent().execute();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // allows access to our location from device
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // Ask for permission. Give int as proof it was this activity that asked for permission.
                    currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                }
                String lat = String.valueOf(currentLocation.getLatitude());
                String lng = String.valueOf(currentLocation.getLongitude());

                updatedURL = "https://api.foursquare.com/v2/venues/search?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=20170617&ll=" + lng + "," + lat + "&near=HELSINKI&m=foursquare&query=" + userQuery;

                Log.i("Location", lat);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        // If device is older than API 23 we dont need to ask permission
        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20, 10, locationListener);
        } else {
            // Check to see if there is no permission granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Ask for permission. Give int as proof it was this activity that asked for permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20, 10, locationListener);

                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (lastKnownLocation != null || lastKnownLocation == currentLocation) {
                    updateLocationInfo(lastKnownLocation);
                }
            }
        }

    }

}
