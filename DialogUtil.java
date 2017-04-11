package com.syezon.cipher.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;


import com.mydrem.www.wificonnect.AccessPoint;
import com.mydrem.www.wificonnect.constant.WiFiConnectMethod;
import com.mydrem.www.wificonnect.wificonnect.WiFiConnectManager;
import com.syezon.cipher.R;
import com.syezon.cipher.ScreenConfig;
import com.syezon.cipher.bean.WifiInfo;
import com.syezon.cipher.config.SConfig;

/**
 * Created by June on 2017/3/28.
 * 自定义对话框形式：
 * 改变对话框尺寸：dialog..getWindow().setAttributes(lp);
 * 注意：在AlertDialog中，使用builder.setView()方法，视图将小于window
 *      window.setContentView(dialogView)默认隐藏键盘
 *
 */
public class DialogUtil {

    public static void showConnectDialog(final Context context, final WifiInfo wifiInfo, final int type, final ConfirmListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_wifi_connect_1, null);
        TextView dialogName = (TextView) dialogView.findViewById(R.id.dialog_wifi_name);
        final EditText dialogPassword = (EditText) dialogView.findViewById(R.id.dialog_wifi_password);
        TextView tvCancel = (TextView) dialogView.findViewById(R.id.dialog_wifi_cancel);
        final TextView tvOk = (TextView) dialogView.findViewById(R.id.dialog_wifi_ok);
        dialogName.setText("连接" + wifiInfo.getAccessPoint().getSSID());
//        builder.setView(dialogView);
        final AlertDialog connectDialog = builder.create();

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectDialog.dismiss();
            }
        });
        tvOk.setEnabled(false);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessPoint accessPoint = wifiInfo.getAccessPoint();
                accessPoint.setmPassword(dialogPassword.getText().toString());
                accessPoint.setmConnectMethod(WiFiConnectMethod.WIFI_CONNECT_FROM_INPUT_LOCAL_PASSWORD);
                WiFiConnectManager.getInstance().connectWiFi(context, accessPoint);
                listener.confirm();
                connectDialog.dismiss();
            }
        });
        dialogPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                if(str.length() >= 8){
                    tvOk.setEnabled(true);
                }else {
                    tvOk.setEnabled(false);
                }
            }
        });

        connectDialog.show();
        Window window = connectDialog.getWindow();
        // window.setContentView 默认隐藏键盘
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_wifi_connect));

//        设置对话框尺寸
        WindowManager.LayoutParams  lp= connectDialog.getWindow().getAttributes();

        lp.horizontalMargin = 0;
        lp.verticalMargin = 0;
        lp.width= (int) (330 * ScreenConfig.SCREEN_SCALE);//定义宽度
        lp.height= (int) (186 * ScreenConfig.SCREEN_SCALE);//定义高度
        connectDialog.getWindow().setAttributes(lp);
        window.setContentView(dialogView);

    }


    public interface ConfirmListener{
        void confirm();
    }
}
