package com.example.basuratrack;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.util.ArrayList;

public class registerAddress extends AppCompatActivity {

    private MapView map = null;
    private Marker userMarker;
    private LocationManager locationManager;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_address);

        // Initialize LocationManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Loading OSMdroid config
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(getPackageName());
        Configuration.getInstance().setOsmdroidBasePath(getCacheDir());
        Configuration.getInstance().setOsmdroidTileCache(getCacheDir());

        //Initialize map view
        map = findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getController().setZoom(16.0);
        map.setMultiTouchControls(true);

        // Add compass overlay
        CompassOverlay compassOverlay = new CompassOverlay(this, map);
        compassOverlay.enableCompass();
        map.getOverlays().add(compassOverlay);

        //Request permissions
        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
        });

//       Handle map Clicks
        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                updateLocationMarker(p, "Pinned Location");
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {

                return false;
            }
        };

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(mapEventsReceiver);
        map.getOverlays().add(mapEventsOverlay);

        Button btnProceed = findViewById(R.id.btnProceed);
        btnProceed.setOnClickListener(view -> {
            Intent intent = new Intent(this, userMain.class);
            startActivity(intent);
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            boolean granted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }

            if (granted) {
                accessUserLocation();
            } else {
                System.out.println("Permissions not granted.");
            }
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE
            );
        } else {
            accessUserLocation();
        }
    }

    private void accessUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, location -> {
                    GeoPoint userLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                    updateLocationMarker(userLocation, "You're here");
                });
            } else {
                System.err.println("GPS provider not available.");
            }
        }
    }

    private void updateLocationMarker(GeoPoint location, String title) {
        if (userMarker == null) {
            userMarker = new Marker(map);
            userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(userMarker);
        }

        userMarker.setPosition(location);
        userMarker.setTitle(title);
        map.getController().animateTo(location);
    }


}