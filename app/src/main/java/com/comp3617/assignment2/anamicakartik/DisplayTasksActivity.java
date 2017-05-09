package com.comp3617.assignment2.anamicakartik;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DisplayTasksActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // LogCat tag
    private static final String TAG = DisplayTasksActivity.class.getSimpleName();
    public String[] REQUESTED_PERMISSIONS = new String[] { Manifest.permission.ACCESS_FINE_LOCATION };

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    private ListView lvTasks;
    private TaskListAdapter adapter;

    //Mangage list of addresses
    private List<String> adddressOfInterests;

    // UI elements
    private TextView lblLocation,tvLocation,tvDistance;
    private View mainLayout;
    private Button btnStartLocationUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_tasks);
        adddressOfInterests = null;

        displayList();
        btnStartLocationUpdates = (Button) findViewById(R.id.btnStartLocationUpdates);
        lblLocation = (TextView) findViewById(R.id.lblLocation);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvDistance.setMovementMethod(new ScrollingMovementMethod());

        mainLayout = findViewById(R.id.relLayout);

        // First we need to check availability of play services
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }

        // Toggling the periodic location updates
        btnStartLocationUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePeriodicLocationUpdates();
            }
        });
    }

    //Displaying tasks in list adapter
    public void displayList(){
        lvTasks = (ListView)findViewById(R.id.lvTasks);
        List<Task> listOfTaskLists= new ArrayList<Task>();
        listOfTaskLists = TaskList.getInstance().getTaskList();

        //Storing list of address string of tasks
        adddressOfInterests = new ArrayList<String> ();
        for (Task task : listOfTaskLists){
            adddressOfInterests.add(task.getAddress());
        }

        adapter = new TaskListAdapter(this, TaskList.getInstance().getTaskList());
        lvTasks.setAdapter(adapter);
        lvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task taskSelected = (Task) lvTasks.getAdapter().getItem(position);
                taskSelected.setMyPositionInList(position);
                Intent editIntent = new Intent(DisplayTasksActivity.this, ActionActivity.class);
                editIntent.putExtra("selectedTaskItem", taskSelected);
                startActivity(editIntent);
            }
        });


        lvTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                alertMessage(index);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_create:
                Intent addIntent = new Intent(DisplayTasksActivity.this, ActionActivity.class);
                startActivity(addIntent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void alertMessage(int index) {
        final int deletePos = index;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        TaskList.getInstance().deleteTask(deletePos);
                        adapter.notifyDataSetChanged();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to delete this item?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


    @Override
    public void onConnected(Bundle bundle) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        StringBuffer showAllAddress = new StringBuffer();
        StringBuffer addressBuffer = new StringBuffer();
        // Assign the new location
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!",
                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();
        new GeoCoderTask().execute(mLastLocation);
        addressBuffer.append(getString(R.string.nearby));
        boolean pushNotification = false;
        for(String address : adddressOfInterests){
            showAllAddress.append(address + " :" + getDistance(address) + " metres \n");
            if(getDistance(address) < 1000) {
                pushNotification = true;
                addressBuffer.append(address + " :" + getDistance(address) + " metres \n");
            }

        }
        tvDistance.setText(showAllAddress);

        //Notify only when distance of interest loaction is less than 1000 m.
        if(pushNotification)
            createNotification(mainLayout, addressBuffer);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, getString(R.string.connectionFailed)
                + connectionResult.getErrorCode());
    }

    /**
     * Method to display the location on UI
     * */
    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            lblLocation.setText(latitude + ", " + longitude);

        } else {

            lblLocation
                    .setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }

    private float getDistance(String address){
        Location locationOfInterest = new Location("");//provider name is unecessary
        if(getLocationFromAddress(address) != null) {
            locationOfInterest.setLatitude(getLocationFromAddress(address).latitude);//your coords of course
            locationOfInterest.setLongitude(getLocationFromAddress(address).longitude);
        }
        return locationOfInterest.distanceTo(mLastLocation);
    }


    private class GeoCoderTask extends AsyncTask<Location, Void, String> {

        private Exception exception;
        @Override
        protected String doInBackground(Location... params) {
            Geocoder gc = new Geocoder(DisplayTasksActivity.this, Locale.getDefault());
            Location currentLocation = params[0];
            List<Address> addresses = null;
            try {
                Log.d(getClass().getSimpleName(), String.format("Address for %.2f, %.2f", currentLocation.getLongitude(),
                        currentLocation.getLatitude()));
                addresses = gc.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 5);
            }catch (Exception exp) {
                exp.printStackTrace();
                exception = exp;
            }

            Address address;
            if (addresses != null && addresses.size() > 1) {
                address = addresses.get(0);
                return String.format("You are at : %s, %s, %s", address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) :
                        "", address.getLocality(), address.getCountryName());
            }
            else
                return "Address not found";
        }

        @Override
        protected void onPostExecute(String s) {
            if (exception == null) {
                tvLocation.setText(s);

            }
            else
                tvLocation.setText("Exception " + exception.getMessage());

        }
    }

    public LatLng getLocationFromAddress(String addressString) {
        Geocoder g = new Geocoder(this);
        List<Address> addressList = null;
        LatLng coordinates = null;
        try {
            addressList = g.getFromLocationName(addressString, 1);
            if (addressList.size() != 0)
                coordinates = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude() );

        } catch (Exception e) {
            Toast.makeText(this, "Location not found",     Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();

        }
        return coordinates;
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        CommonConstants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(CommonConstants.UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(CommonConstants.FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(CommonConstants.DISPLACEMENT);
    }


    private void requestLocationPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            //show a message to the user to say why you are seeking this permission.....
            Snackbar.make(mainLayout, "Location access is required for this app",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(DisplayTasksActivity.this, REQUESTED_PERMISSIONS, CommonConstants.LOCATION_REQUEST_PERMISSION);
                }
            }).show();
        }
        else {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, CommonConstants.LOCATION_REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CommonConstants.LOCATION_REQUEST_PERMISSION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            }
        }
        else super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * Method to toggle periodic location updates
     * */
    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            // Changing the button text
            btnStartLocationUpdates
                    .setText(getString(R.string.btn_stop_location_updates));

            mRequestingLocationUpdates = true;

            // Starting the location updates
            startLocationUpdates();

            Log.d(TAG, "Periodic location updates started!");

        } else {
            // Changing the button text
            btnStartLocationUpdates
                    .setText(getString(R.string.btn_start_location_updates));

            mRequestingLocationUpdates = false;

            // Stopping the location updates
            stopLocationUpdates();

            Log.d(TAG, "Periodic location updates stopped!");
        }
    }

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Resuming the periodic location updates
            if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
                startLocationUpdates();
            }
        } else
            requestLocationPermissions();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    public void createNotification(View view, StringBuffer notificationList) {
        // Prepare intent which is triggered if the
        // notification_icon is selected
        Intent intent = new Intent(this, NotificationReceiverActivity.class);
        intent.putExtra(CommonConstants.EXTRA_MESSAGE , notificationList.toString());
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification_icon
        // Actions are just fake
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Task location nearby Alert")
                .setContentText("Location alert")
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification_icon after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, notification);

    }
}

