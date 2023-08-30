package com.example.deepfake4;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

/**
 * Created by TedPark on 15. 12. 10..
 */
public class IncomingCallBroadcastReceiver extends BroadcastReceiver {
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private int count = 0;
    static String mLastState;
    static final String TAG = "CallStateListner";

    @Override
    public void onReceive(Context context, Intent intent) {

        // 전화 수신 체크
        CallReceivedChk(context, intent);
    }

    private void CallReceivedChk(Context context, Intent intent) {
        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                String mState = String.valueOf(state);
                if (mState.equals(mLastState)) { // 두번 호출되는 문제 해결 목적
                    return;
                } else {
                    mLastState = mState;
                }

                switch(state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d(TAG,"전화 수신 상태가 아닙니다 : CALL_IDLE");
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.d(TAG, "전화를 받았습니다 : CALL_OFFHOOK");
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d(TAG, "CALL_RINGING, 수신 전화번호 : " + PhoneNumberUtils.formatNumber(incomingNumber));
                        // 처리하고자 하는 코드 추가하면 된다.

                        if(PhoneNumberUtils.formatNumber(incomingNumber).equals("010-5388-5418")||PhoneNumberUtils.formatNumber(incomingNumber).equals("010-4913-2716")) {

                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                            Intent notificationIntent = new Intent(context, MainActivity.class);
                            notificationIntent.putExtra("notificationId", count); //전달할 값
                            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                                    .setContentTitle("CheckFake")
                                    .setContentText("[경고] " + PhoneNumberUtils.formatNumber(incomingNumber) + "으로부터 전화를 수신하였습니다.")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setContentIntent(pendingIntent)
                                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                                    .setAutoCancel(true);
               /*Uri soundUri= Uri.parse("android.resource://"+getPackageName()+"/"+R.음악파일폴더.음악파일이름);
                builder.setSound(soundUri); 음악파일넣는코드인데 에뮬이라 그런가 소리가 안들림;;*/


                            //OREO API 26 이상에서는 채널 필요
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
                                CharSequence channelName = "노티페케이션 채널";
                                String description = "오레오 이상을 위한 것임";
                                int importance = NotificationManager.IMPORTANCE_HIGH;

                                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                channel.setDescription(description);

                                // 노티피케이션 채널을 시스템에 등록
                                assert notificationManager != null;
                                notificationManager.createNotificationChannel(channel);

                            } else
                                builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

                            assert notificationManager != null;
                            notificationManager.notify(1234, builder.build()); // 고유숫자로 노티피케이션 동작시킴
                        }




                        break;
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

}
