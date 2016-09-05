package org.tracker.ibmiot.vehicletracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.tracker.ibmiot.vehicletracker.Activities.getAppId;
import org.tracker.ibmiot.vehicletracker.cogs.dataBasehandler;
import org.tracker.ibmiot.vehicletracker.iot.myIoTClient;
import org.tracker.ibmiot.vehicletracker.pojos.vehicleData;

import java.util.ArrayList;
import java.util.List;

public class startActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager pager;
    private GoogleMap googleMap;
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private ArrayList<String> result = new ArrayList<>();
    //myIoTClient myIoTClient = new myIoTClient();

    dataBasehandler db = new dataBasehandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "BlueMix Login....", Snackbar.LENGTH_LONG)
                        .setAction("Logging in....", null).show();

                handleActivate();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = new Intent(this, getAppId.class);

        SharedPreferences firstRun = getSharedPreferences("firstRun21", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = firstRun.edit();
        if (firstRun.getBoolean("firstRun21", false)) {
            editor.putBoolean("firstRun21", true);
            editor.apply();
        }
        else {
            editor.putBoolean("firstRun21", true);
            editor.apply();
            intent.putExtra("key", "value");
            startActivity(intent);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } /*else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleActivate() {
        String org = "nafg3m";
        String id = "iotTrackerApp";
        String authMethod = "a-nafg3m-x3drvod0sb";
        String authTocken = "sqJUehiv@yeQf+NFrf";
        Boolean isSSL = false;
        String clientID = "a:" + org + ":" + id;
        String server = ".messaging.internetofthings.ibmcloud.com";
        String serverHost = org + server;
        dataBasehandler db = new dataBasehandler(this);

        try {
            //myIoTClient.run(serverHost, clientID, authMethod, authTocken, isSSL);
            //System.out.println(vehicleData.getLatt() +" - "+ vehicleData.getLont());
            CameraUpdate zoom= CameraUpdateFactory.zoomTo(18);
            googleMap.animateCamera(zoom);
            Runnable iotClient = new myIoTClient(serverHost, clientID, authMethod, authTocken, isSSL, this);
            moveMarker();
            new Thread(iotClient).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveMarker(){
        final int delay = 5000;
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        //Projection proj = mGoogleMapObject.getProjection();
        //Point startPoint = proj.toScreenLocation(marker.getPosition());
        //final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                List<vehicleData> vehicleDataList = db.getUsers();

                for (vehicleData vdl : vehicleDataList){
                    googleMap.clear();
                    System.out.println(vdl.getDeviceName() + " - " + vdl.getLatt() +" - "+ vdl.getLont());
                    CameraUpdate center= CameraUpdateFactory.newLatLng(new LatLng(Float.valueOf(vdl.getLatt()), Float.valueOf(vdl.getLont())));

                    options.position(new LatLng(Float.valueOf(vdl.getLatt()), Float.valueOf(vdl.getLont())));
                    options.title("Speed : " + vdl.getSpeed());
                    options.snippet("Name : " + vdl.getID());
                    googleMap.addMarker(options);
                    System.out.println(Float.valueOf(vdl.getLatt()) + "______" + Float.valueOf(vdl.getLont()));
                    googleMap.moveCamera(center);
                }
                db.close();
                vehicleDataList.clear();
                handler.postDelayed(this, delay);
            }
        });
    }

}
