package com.blake.where.location;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

public class WhereActivity extends FragmentActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    // Global constants
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


   LocationClient mLocationClient;
    Location mCurrentLocation;

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        if(isGooglePlayAvailable()){
            mLocationClient = new LocationClient(this, this, this);
            mLocationClient.connect();

        }else{
            Toast.makeText(this, "No Google Play.", Toast.LENGTH_SHORT).show();
            Intent i = new Intent();
            i.putExtra("googleplay", "fail");
            setResult(RESULT_OK, i);
            finish();
        }

    }

    /*
    Called after stopping, calls onStart()
     */
    @Override
    public void onRestart(){
        super.onRestart();
    }
    /*
    Called by methods onCreate() and onRestart(),
                  if activity goes to front it calls onResume().
        if activity is hidden it calls onStop()
     */
    @Override
    public void onStart(){
        super.onStart();
    }
    /*
    activity will interact with user, will receive user input
    calls onPause()
     */
    @Override
    public void onResume(){
        super.onResume();
    }
    /*
    called by onResume(),
    do short tasks before resuming previous activity - save changes etc
        if activity goes to front it calls onResume().
    if activity is hidden it calls onStop()
 */
    @Override
    public void onPause(){
        super.onPause();
    }
    /*
    activity not visible, covered by other activity
    calls onRestart() if activity is coming back to user
    calls onDestroy() if activity is going away
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }

    /*
    called by finish(), or because system destroyed it.
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    //end lifecycle overrides




    private boolean isGooglePlayAvailable(){
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
        }else return false;
    }


    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    @Override
    protected void onActivityResult(  int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */
                        break;
                }

        }
    }


    /*
    * Called by Location Services when the request to connect the
    * client finishes successfully. At this point, you can
    * request the current location or start periodic updates
    */
    @Override
    public void onConnected(Bundle dataBundle) {
       if( mLocationClient.getLastLocation()==null){
           Toast.makeText(this, "Null Location.", Toast.LENGTH_SHORT).show();
           Intent i = new Intent();
           i.putExtra("gps", "fail");
           setResult(RESULT_CANCELED, i);
           finish();
       }else{
           mCurrentLocation = mLocationClient.getLastLocation();
         //  Toast.makeText(this, "Connected Yo"+ mCurrentLocation.toString(), Toast.LENGTH_LONG).show();
           Intent i = new Intent();
           i.putExtra("lat", Double.toString(mCurrentLocation.getLatitude()));
           i.putExtra("long", Double.toString(mCurrentLocation.getLongitude()));
           setResult(RESULT_OK, i);
           finish();
       }
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */

    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
          //  showErrorDialog(connectionResult.getErrorCode());
            Toast.makeText(this, "Error Code Conn Fail." + connectionResult.getErrorCode(),
                    Toast.LENGTH_SHORT).show();
        }
    }



}