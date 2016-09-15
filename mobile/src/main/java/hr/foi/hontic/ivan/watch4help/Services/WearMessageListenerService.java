package hr.foi.hontic.ivan.watch4help.Services;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import hr.foi.hontic.ivan.watch4help.MainActivity;

/**
 * Created by Ivan on 12.9.2016..
 */
public class WearMessageListenerService extends WearableListenerService {
    private static final String CONTACT_PERSON_OF_TRUST = "/contact_person_of_trust";


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        if( messageEvent.getPath().equalsIgnoreCase( CONTACT_PERSON_OF_TRUST ) ) {
            Log.d("MessageListener", "Text someone");


            Intent intentLoc = new Intent( this, LocationListenerService.class );
            intentLoc.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            startService( intentLoc );



        } else {

            //byte[] bytes = messageEvent.getData();
            //String str = new String(bytes);
            //Log.d("Hoc", "Poruka:" + str);
            super.onMessageReceived(messageEvent);
        }
    }


}