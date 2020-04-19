package com.example.chargepoint.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.example.chargepoint.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class ChargePointClusterRenderer extends DefaultClusterRenderer<ChargePointCluster> {

    private final Context context;

    public ChargePointClusterRenderer(Context context, GoogleMap map, ClusterManager<ChargePointCluster> clusterManager) {
        super(context, map, clusterManager);

        this.context = context;
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<ChargePointCluster> cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);

        markerOptions.icon(getClusterIcon(cluster));
    }

    private BitmapDescriptor getClusterIcon(Cluster cluster) {
        int count = cluster.getItems().size();
        String countString = String.valueOf(count);

        int res;
        if (count <= 10) res = R.drawable.m1;
        else if (count <= 50) res = R.drawable.m2;
        else if (count <= 100) res = R.drawable.m3;
        else if (count <= 500) res = R.drawable.m4;
        else res = R.drawable.m5;

        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), res).copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(28);
        Rect bounds = new Rect();
        paint.getTextBounds(countString, 0, countString.length(), bounds);

        Canvas canvas = new Canvas(bm);
        canvas.drawText(countString, bm.getWidth() / (float) 2 - bounds.centerX(), bm.getHeight() / (float) 2 - bounds.centerY(), paint);

        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    @Override
    protected void onClusterItemRendered(ChargePointCluster clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);

        marker.setTag(clusterItem.getChargePoint());
    }

    @Override
    protected void onBeforeClusterItemRendered(ChargePointCluster item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);

        int drawableId;

        if (item.getTitle().toLowerCase().contains("tesla"))
            drawableId = R.drawable.ic_tesla_32dp;
        else if (item.getTitle().toLowerCase().contains("esb"))
            drawableId = R.drawable.ic_esb_32dp;
        else if (item.getTitle().toLowerCase().contains("pod"))
            drawableId = R.drawable.ic_pod_32dp;
        else if (item.getTitle().toLowerCase().contains("ionic"))
            drawableId = R.drawable.ic_ionic_32dp;
        else
            drawableId = R.drawable.ic_generic_32dp;

        final BitmapDescriptor markerDescriptor = bitmapDescriptorFromVector(context, drawableId);
        markerOptions.icon(markerDescriptor);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
