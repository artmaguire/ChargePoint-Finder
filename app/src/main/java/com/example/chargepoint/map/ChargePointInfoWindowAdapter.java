package com.example.chargepoint.map;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.chargepoint.R;
import com.example.chargepoint.pojo.ChargeConnection;
import com.example.chargepoint.pojo.ChargePoint;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class ChargePointInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View iw_view;

    public ChargePointInfoWindowAdapter(Context context) {
        this.iw_view = View.inflate(context, R.layout.info_window_charge_point, null);
    }

    // Set the InfoWindow View Elements to the Clicked ChargePoint Marker
    @Override
    public View getInfoWindow(Marker marker) {
        ChargePoint cp = (ChargePoint) marker.getTag();

        if (cp == null) return null;

        TextView iw_operator = iw_view.findViewById(R.id.iw_operator);
        String operator = cp.getOperator();
        iw_operator.setText(operator);

        TextView iw_noChargePoints = iw_view.findViewById(R.id.iw_no_charge_points);
        String numberOfChargers = "# Charge Points:  ";
        numberOfChargers += cp.getNumberOfPoints();
        iw_noChargePoints.setText(numberOfChargers);

        TextView iw_powerKW = iw_view.findViewById(R.id.iw_powerKW);
        StringBuilder powerKW = new StringBuilder();
        for (ChargeConnection cc : cp.getConnections()) {
            if (cc.getPowerKW() > -1) {
                if (powerKW.length() == 0)
                    powerKW.append(cc.getPowerKW()).append("KW");
                else
                    powerKW.append(" - ").append(cc.getPowerKW()).append("KW");
            }
        }
        iw_powerKW.setText(powerKW.toString());

        return iw_view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
