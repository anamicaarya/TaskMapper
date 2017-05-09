package com.comp3617.assignment2.anamicakartik;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Kartik on 16-07-20.
 */
public class LocationFinder {

    public static double[] getLocationFromAddress(Context context , String pAddress){
        double[] coordinates = new double[2];
        Geocoder g = new Geocoder(context);
        List<Address> addressList = null;
        Address localAddress = null;
        String searchRoad = pAddress ;
        try {
            addressList = g.getFromLocationName(searchRoad, 1);
            //localAddress = addressList.get(0);
            if (addressList.size() != 0){
                coordinates[0] = addressList.get(0).getLatitude();
                 coordinates[1] = addressList.get(0).getLongitude();
            }

        } catch (Exception e) {
            Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }

        return coordinates;
    }

}
