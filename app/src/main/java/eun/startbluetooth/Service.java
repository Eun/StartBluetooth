package eun.startbluetooth;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

public class Service extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Intent.ACTION_BOOT_COMPLETED:
                IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
                context.getApplicationContext().registerReceiver(this, filter);
                setBluetooth(context, R.string.pref_set_on_boot);
                break;
            case Intent.ACTION_SCREEN_ON:
                setBluetooth(context, R.string.pref_set_on_screen_on);
                break;
        }
    }

    public void setBluetooth(Context context, int resId) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            displayNotification(context, "Unable to get BluetoothAdapter");
            return;
        }


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String value = prefs.getString(context.getString(resId), "unset");
        if (value.equalsIgnoreCase("Enable")) {
            Log.i("StartBluetooth", "enabling bluetooth");
            bluetoothAdapter.enable();
        } else if (value.equalsIgnoreCase("Disable")) {
            Log.i("StartBluetooth", "disabling bluetooth");
            bluetoothAdapter.disable();
        }

    }
    private int notificationID = 0;
    private void displayNotification(Context context, String message) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            Toast.makeText(context, "StartBluetooth: Unable to get NotificationManager", Toast.LENGTH_SHORT).show();
            return;
        }

        notificationManager.notify(notificationID, new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("StartBluetooth").setContentText(message).build());
        notificationID++;
    }
}
