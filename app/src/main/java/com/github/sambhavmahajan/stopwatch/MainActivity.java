package com.github.sambhavmahajan.stopwatch;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private class TimerHandler extends Thread{
        private long timeElapsed;
        public boolean isRunning = false;
        public String getTime(){
            return String.format("%d:%02d:%02d", timeElapsed / 3600, (timeElapsed % 3600) / 60, timeElapsed % 60);
        }
        @Override
        public void run(){
            timeElapsed = 0;
            while(isRunning){
                try {
                    this.sleep(1000);
                    ++timeElapsed;
                    if(isRunning) runOnUiThread(() -> objTimeView.setText(getTime()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    private TimerHandler handler;
    private TextView objTimeView;
    public Button btn;
    private boolean isRunning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        objTimeView = findViewById(R.id.timeView);
        btn = btn = findViewById(R.id.bntStart);
        objTimeView.setText("0:00:00");
    }
    public void btnClick(View view){
        if (handler == null || !handler.isRunning) {
            handler = new TimerHandler();
            handler.isRunning = true;
            btn.setText("Stop");
            objTimeView.setText("0:00:00");
            handler.start();
        } else {
            handler.isRunning = false;
            btn.setText("Start");
        }
    }
}