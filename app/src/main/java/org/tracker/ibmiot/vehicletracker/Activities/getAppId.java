package org.tracker.ibmiot.vehicletracker.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.tracker.ibmiot.vehicletracker.cogs.dataBasehandler;

import org.tracker.ibmiot.vehicletracker.pojos.vehicleData;
import org.tracker.ibmiot.vehicletracker.startActivity;

import org.tracker.ibmiot.vehicletracker.R;

public class getAppId extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_app_id);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Intent intent = new Intent(this, org.tracker.ibmiot.vehicletracker.startActivity.class);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String appId = ((EditText) findViewById(R.id.getAppID)).getText().toString();
                if (appId.equals("")){
                    Snackbar.make(view, "Please Enter Your Name to Proceed", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                createDBUser(appId);
                startActivity(intent);
            }
        });
    }

    public void createDBUser(String appID){
        dataBasehandler db = new dataBasehandler(this);
        db.createUser(new vehicleData(appID,"50.000","50.000","",""));
        System.out.println("New User Created");
    }

}
