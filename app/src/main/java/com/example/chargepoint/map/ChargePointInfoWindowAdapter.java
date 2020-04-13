package com.example.chargepoint.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.chargepoint.R;
import com.example.chargepoint.pojo.ChargeConnection;
import com.example.chargepoint.pojo.ChargePoint;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Eoin on 23/03/2018.
 * Custom InfoWindowAdapter uses layout info_window_stop.xml
 * Design needs to be the same as website info window
 */

public class ChargePointInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View iw_view;

    public ChargePointInfoWindowAdapter(Context context) {
        this.iw_view = LayoutInflater.from(context).inflate(R.layout.info_window_charge_point, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        ChargePoint cp = (ChargePoint) marker.getTag();

        TextView iw_operator = iw_view.findViewById(R.id.iw_operator);
        String operator = cp.getOperator();
        iw_operator.setText(operator);

        TextView iw_noChargePoints = iw_view.findViewById(R.id.iw_no_charge_points);
        String numberOfChargers = "# Charge Points:  ";
        numberOfChargers += cp.getNumberOfPoints();
        iw_noChargePoints.setText(numberOfChargers);

        TextView iw_powerKW = iw_view.findViewById(R.id.iw_powerKW);
        String powerKW = "";
        for (ChargeConnection cc : cp.getConnections()) {
            if (cc.getPowerKW() > -1) {
                if (powerKW.isEmpty())
                    powerKW += cc.getPowerKW() + "KW";
                else
                    powerKW += " - " + cc.getPowerKW() + "KW";
            }
        }
        iw_powerKW.setText(powerKW);

        return iw_view;
    }
}
