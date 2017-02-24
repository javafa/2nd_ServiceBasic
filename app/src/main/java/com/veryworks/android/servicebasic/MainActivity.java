package com.veryworks.android.servicebasic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG="MainActivity";

    Button btnStart,btnStop,btnBind,btnUnbind,btnCallService;

    MyService bService; // 서비스 객체
    boolean isService = false; // 서비스 중 확인

    ServiceConnection conn = new ServiceConnection() {
        // 서비스와 연결되는 순간 호출되는 함수
        @Override                                         // 서비스의 onBind에서 리턴되는 값이 binder에 담겨온다
        public void onServiceConnected(ComponentName name, IBinder binder) {
            MyService.MyBinder mb = (MyService.MyBinder) binder;
            bService = mb.getService();
            isService = true;
        }
        // 이거는 서비스가 중단되거나 연결이 도중에 끊겼을 때 발생함
        // onDestroy 에서는 호출되지 않는다.
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isService = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnBind = (Button) findViewById(R.id.btnBind);
        btnUnbind = (Button) findViewById(R.id.btnUnbind);
        btnCallService = (Button) findViewById(R.id.btnCallService);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnBind.setOnClickListener(this);
        btnUnbind.setOnClickListener(this);
        btnCallService.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent( this, MyService.class);
        switch (v.getId()){
            case R.id.btnStart:
                startService(intent);
                break;
            case R.id.btnStop:
                stopService(intent);
                break;
            case R.id.btnBind:
                bindService(intent, conn, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btnUnbind:
                if(isService) {
                    unbindService(conn); // 서비스 종료
                    // unbind 시 onServiceDisconnected 호출안됨.. 서비스가 끊겼을 경우만 호출되므로
                    // 마지막 unbind 시 서비스가 실행되지 않고 있음을 알려야 함
                    isService = false;
                    // 아래처럼 강제적으로 onServiceDisconnected를 호출해 줄 수도 있긴 하다
                    //conn.onServiceDisconnected(new ComponentName("com.veryworks.android.servicetest","BindService.class"));
                }else
                    Toast.makeText(getApplicationContext(), "서비스중이 아닙니다, 종료할 수 없음", Toast.LENGTH_LONG).show();
                break;
            case R.id.btnCallService:
                if (!isService) {
                    Toast.makeText(getApplicationContext(), "서비스중이 아닙니다, 데이터받을수 없음", Toast.LENGTH_LONG).show();
                    return;
                }
                int num = bService.getRandomNumber();//서비스쪽 메소드에서 값 전달 받아 호출
                Toast.makeText(getApplicationContext(), "받아온 데이터 : " + num, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
