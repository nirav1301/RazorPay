package com.example.razorpay;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.razorpay.databinding.ActivityMainBinding;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {
    private ActivityMainBinding binding;
    String messege;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        messege = getIntent().getStringExtra("message");
        String sAmount = "100";
        int amount  = Math.round(Float.parseFloat(sAmount));

        binding.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checkout checkout = new Checkout();
                checkout.setKeyID("rzp_test_m8pwWyYSOaL0El");
                checkout.setImage(R.drawable.sc03_ic_message);
                JSONObject object = new JSONObject();
                try {
                    object.put("name","Nirav Vasava");
                    object.put("description","Test Payment");
                    object.put("theme.color","#0093DD");
                    object.put("currency","INR");
                    object.put("amount",amount);
                    object.put("prefill.contact","9898714318");
                    object.put("prefill.email","vasavanirav1301@gmail.com");
                    checkout.open(MainActivity.this,object);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    public void onPaymentSuccess(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("PAYMENT ID");
        builder.setMessage(s);
        builder.show();
        addNotification();


    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

    }
    private void addNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "1";
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_message)
                .setTicker("Hearty365")
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Order details")
                .setContentText("Your payment is successful")
                .setContentInfo("Congratulations! You have sucessfully completed the payment for this item");




        notificationManager.notify(/*notification id*/1, notificationBuilder.build());

    }
}