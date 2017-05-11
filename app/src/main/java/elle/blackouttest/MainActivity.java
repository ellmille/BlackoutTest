package elle.blackouttest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    TextView counterView;
    Button blackoutButton, timedBlackoutButton, dimScreenButton;
    int counter;
    CountThread countThread;
    boolean isThreadRunning, isDim;
    PowerManager powerManager;
    PowerManager.WakeLock wakelock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up View elements
        dimScreenButton = (Button) findViewById(R.id.button2);
        dimScreenButton.setOnClickListener(dimScreenButtonListener);
        isDim = false;
        timedBlackoutButton = (Button) findViewById(R.id.button1);
        timedBlackoutButton.setOnClickListener(timedBlackoutButtonListener);
        blackoutButton = (Button) findViewById(R.id.button);
        blackoutButton.setOnClickListener(BlackOutButtonListener);
        counterView = (TextView) findViewById(R.id.textView);
        counterView.setText(String.valueOf(0));
        //set up thread
        isThreadRunning = false;
        countThread = new CountThread();
        countThread.start();
        //set up wakelock
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "no sleep");
        //can we change settings?
        if(!Settings.System.canWrite(this.getApplicationContext())){
            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_SETTINGS}, Settings.ACTION_MANAGE_WRITE_SETTINGS);
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private View.OnClickListener dimScreenButtonListener = new View.OnClickListener() {
        public void onClick(View v){
            WindowManager.LayoutParams params = getWindow().getAttributes();
            if(isDim){
                params.screenBrightness = -1;
                getWindow().setAttributes(params);
            }else{
                //dim the screen
                params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                params.screenBrightness = 0;
                getWindow().setAttributes(params);
            }
            isDim = !isDim;
        }
    };

    private View.OnClickListener timedBlackoutButtonListener = new View.OnClickListener() {
        public void onClick(View v){
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 0);
            wakelock.acquire(3000);
        }
    };

    private View.OnClickListener BlackOutButtonListener = new View.OnClickListener() {
        public void onClick(View v){
            isThreadRunning = true;
            if(wakelock != null){
                wakelock.acquire();
            }else{
                counterView.setText("wake lock not set");
            }
        }
    };

    //thread that will update the vitals ever 3s
    private class CountThread extends Thread
    {
        CountThread() {
        }
        public void run()
        {
            StartCounting();
        }
    }

    private void StartCounting() {
        counter = 0;
        //noinspection InfiniteLoopStatement
        while (true){
            if(isThreadRunning){
                counter++;
                //sleep for a second
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_MENU)
        {
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(wakelock.isHeld()){
                //release wakelock
                wakelock.release();
            }
            //stop counting and set text
            isThreadRunning = false;
            counterView.setText(String.valueOf(counter));
            return true;
        }
        else
        {
            return super.onKeyUp(keyCode, event);
        }
    }
}
