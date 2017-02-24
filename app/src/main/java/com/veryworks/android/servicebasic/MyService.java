package com.veryworks.android.servicebasic;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

public class MyService extends Service {
    private static final String TAG = "MyService";

    /*
      -------------- bindService() 에서 사용하는 부분 ---------------------
     */
    // Binder 객체는 IBinder 인터페이스 구현 객체입니다
    // public class Binder extends Object implements IBinder
    IBinder mBinder = new MyBinder();

    class MyBinder extends Binder {
        MyService getService() { // 서비스 객체를 리턴
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"=====onBind");
        // 액티비티에서 bindService() 를 실행하면 호출됨
        // 리턴한 IBinder 객체는 서비스와 클라이언트 사이의 인터페이스를 정의한다
        return mBinder; // 바인더 객체를 리턴
    }
    //---------------------------------------------------------------------

    public void print(String value){
        System.out.println("Service value================="+value);
    }

    public int getRandomNumber() { // 임의 랜덤값을 리턴하는 메서드
        return new Random().nextInt();
    }


    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "=============onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"=========================onStartCommand="+flags);

        for(int i=0; i < 1000; i++){
            System.out.println("서비스에서 동작중입니다="+i);
            Toast.makeText(getBaseContext(),
                    "서비스에서 동작중입니다="+i,
                    Toast.LENGTH_SHORT).show();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "=============onDestroy");
    }
}
