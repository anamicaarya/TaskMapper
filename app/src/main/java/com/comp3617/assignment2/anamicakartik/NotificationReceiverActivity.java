package com.comp3617.assignment2.anamicakartik;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NotificationReceiverActivity extends AppCompatActivity{

    // UI elements
    private TextView showAlertMessage;
    private Button btnDismiss,btnSnooze;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_receiver);
        showAlertMessage = (TextView) findViewById(R.id.result_message);
        showAlertMessage.setText(getIntent().getStringExtra(CommonConstants.EXTRA_MESSAGE));

        //Button attaching to layout.
        btnDismiss = (Button) findViewById(R.id.btnDismiss);
        btnSnooze = (Button) findViewById(R.id.btnSnooze);
        // Notification action buttons
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        btnSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
