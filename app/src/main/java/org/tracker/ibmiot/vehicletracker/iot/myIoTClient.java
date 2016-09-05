package org.tracker.ibmiot.vehicletracker.iot;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;
import org.tracker.ibmiot.vehicletracker.cogs.dataBasehandler;
import org.tracker.ibmiot.vehicletracker.pojos.vehicleData;
import org.tracker.ibmiot.vehicletracker.startActivity;

import java.util.ArrayList;

/**
 * Created by Kelvin on 7/11/2016.
 */
public class myIoTClient implements Runnable{

    private static final String TAG = myIoTClient.class.getName();
    private static String deviceTopic = "iot-2/type/vehicle/id/+/evt/gps/fmt/json";
    private static String eventsTopic = "iot-2/type/vehicle/id/+/mon";
    private static MqttClient client;
    public static MqttClient getClient() {
        return client;
    }

    private GoogleMap googleMap;
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private ArrayList<String> result = new ArrayList<>();

    startActivity startActivity = new startActivity();

    String serverHost;
    String clientId;
    String authmethod;
    String authtoken;
    boolean isSSL;
    private Context context;
    public myIoTClient(String serverHost, String clientId, String authmethod,
                       String authtoken, boolean isSSL, Context context){
        this.serverHost = serverHost;
        this.clientId = clientId;
        this.authtoken = authtoken;
        this.authmethod = authmethod;
        this.isSSL = isSSL;
        this.context = context;
    }

    public void run(){

        MemoryPersistence persistance = new MemoryPersistence();
        if (client != null) {
            try {
                client.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
            client = null;
        }

        try {
            client = new MqttClient("tcp://" + serverHost + ":1883", clientId, persistance);

        } catch (MqttException e) {
            e.printStackTrace();
        }
        // register a callback for messages that we subscribe to ...
        client.setCallback(new MqttCallback() {

            @Override
            public void connectionLost(Throwable cause) {
                Log.d(TAG, "Connection Lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

                dataBasehandler db = new dataBasehandler(context);
                String data = new String(message.getPayload());
                Log.d(TAG, data);
                try {
                    JSONObject gpsData = new JSONObject(data);
                    JSONObject gps = gpsData.getJSONObject("d");
                    String lat = gps.getString("lat");
                    String lon = gps.getString("long");
                    System.out.println(lat + " -- " + lon);
                    db.updateVehicle(new vehicleData("test", lat, lon, "", ""));

                } catch (Exception e){
                    e.printStackTrace();
                }
                db.close();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {//Called when a outgoing publish is complete.
                Log.d(TAG, "Published Succesfully");
            }
        });

        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName(authmethod);
        options.setPassword(authtoken.toCharArray());

        if (isSSL) {
            java.util.Properties sslClientProps = new java.util.Properties();
            sslClientProps.setProperty("com.ibm.ssl.protocol", "TLSv1.2");
            options.setSSLProperties(sslClientProps);
        }

        try {
            client.connect(options);
            Log.d(TAG, "Connected To IOT");
            client.subscribe(deviceTopic);
            //client.subscribe(eventsTopic);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }
}
