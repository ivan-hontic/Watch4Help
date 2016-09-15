package hr.foi.hontic.ivan.watch4help;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks {

    Handler mHandler;
    ProgressBar progressBarMessage;
    boolean wearButtonPressed = false;

    ImageView iconMessage;

    private static final String CONTACT_PERSON_OF_TRUST = "/contact_person_of_trust";
    GoogleApiClient mApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this is stopping device from dimming or closing the app(because of saving energy)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mHandler = new Handler();
        progressBarMessage = (ProgressBar) findViewById(R.id.progressBarMessage);

        iconMessage = (ImageView) findViewById(R.id.imgView);

        iconMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                wearButtonPressed = true;
                                progressBarMessage.setVisibility(View.VISIBLE);
                                progressBarMessage.setMax(100);
                                progressBarMessage.setProgress(0);
                                contactParent();
                            }
                        }, 10);
                        break;

                    case MotionEvent.ACTION_UP:
                        wearButtonPressed = false;
                        progressBarMessage.setVisibility(View.INVISIBLE);
                        break;

                }
                return true;
            }
        });



        initGoogleApiClient();
    }

    private void contactParent() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBarMessage.setProgress(progressBarMessage.getProgress() + 1);
                if (progressBarMessage.getProgress() == 100 && wearButtonPressed) {
                    progressBarMessage.setVisibility(View.INVISIBLE);
                    progressBarMessage.setProgress(0);

                    textForHelp();

                    Log.d("Main", "contact parent");

                } else if (wearButtonPressed) {
                    contactParent();
                }
            }
        }, 20);
    }

    private void initGoogleApiClient() {
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .build();

        mApiClient.connect();
    }


    @Override
    public void onConnected( Bundle bundle ) {
        Log.d("Hoc", "Add listener mapiClient");

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Hoc", "Main - connSuspended:");
    }

    private void sendMessage( final String path, final String text ) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, text.getBytes() ).await();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();

        Log.d("Main", "onDestroy");
    }

    public void textForHelp(){
        sendMessage(CONTACT_PERSON_OF_TRUST, "N");
    }


}
