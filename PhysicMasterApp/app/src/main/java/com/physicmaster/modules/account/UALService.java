package com.physicmaster.modules.account;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.physicmaster.common.Constant;
import com.physicmaster.modules.mine.activity.friend.AddressListActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.CommonResponse;
import com.physicmaster.net.response.account.CheckAddrListUploadResponse;
import com.physicmaster.net.security.AESEncryption;
import com.physicmaster.net.security.Base64Encoder;
import com.physicmaster.net.security.RSAEncryption;
import com.physicmaster.net.service.account.AddrListUploadService;
import com.physicmaster.net.service.account.CheckAddrListUploadService;
import com.physicmaster.utils.MD5;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by huashigen on 2017/5/26.
 * 上传通讯录列表服务
 */

public class UALService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getUploadState();
        return super.onStartCommand(intent, flags, startId);
    }

    private void getUploadState() {
        CheckAddrListUploadService service = new CheckAddrListUploadService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CheckAddrListUploadResponse>() {
            @Override
            public void onGetData(CheckAddrListUploadResponse data) {
                //未上传
                if (data.data.isUpload == 0) {
                    queryContactPhoneNumber();
                } else {
                    UALService.this.stopSelf();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UALService.this.stopSelf();
            }
        });
        service.postLogined("", false);
    }

    /**
     * 上传通讯录
     */
    private void uploadAddrList(List<Person> persons) {
        UUID uuid = UUID.randomUUID();
        String randomStr = uuid.toString();
        String randomMD5 = MD5.hexdigest(randomStr);
        String data1 = "";
        try {
            byte[] rsaRandom = RSAEncryption.encryptByPublicKey(randomMD5.getBytes("UTF-8"),
                    RSAEncryption.getRSAKeyPair());
            data1 = Base64Encoder.encode(rsaRandom, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context
                .TELEPHONY_SERVICE);
        String number1 = telephonyManager.getLine1Number();
        JSONObject jsonData = new JSONObject();
        if (!TextUtils.isEmpty(number1)) {
            jsonData.put("c", number1);
        }
        JSONArray jsonPersons = new JSONArray((ArrayList) persons);
        jsonData.put("pl", jsonPersons);
        AddrListUploadService service = new AddrListUploadService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                UALService.this.stopSelf();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UALService.this.stopSelf();
            }
        });
        String encryptData = "";
        try {
            encryptData = AESEncryption.encrypt(jsonData.toString().getBytes(), randomMD5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            data1 = URLEncoder.encode(data1, "UTF-8");
            encryptData = URLEncoder.encode(encryptData, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        service.postLogined("data1=" + data1 + "&data2=" + encryptData, false);
    }

    /**
     * 查询手机联系人
     */
    private void queryContactPhoneNumber() {
        final AsyncTask<Boolean, Integer, List<Person>> contactTask = new
                AsyncTask<Boolean,
                        Integer,
                        List<Person>>() {
                    @Override
                    protected List<Person> doInBackground(Boolean... params) {
                        String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract
                                .CommonDataKinds.Phone.NUMBER};
                        Cursor cursor = getContentResolver().query(ContactsContract
                                        .CommonDataKinds.Phone
                                        .CONTENT_URI,
                                cols, null, null, null);
                        List<Person> users = new ArrayList<>();
                        if (cursor == null) {
                            return users;
                        }
                        for (int i = 0; i < cursor.getCount(); i++) {
                            cursor.moveToPosition(i);
                            // 取得联系人名字
                            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract
                                    .PhoneLookup
                                    .DISPLAY_NAME);
                            int numberFieldColumnIndex = cursor.getColumnIndex(ContactsContract
                                    .CommonDataKinds
                                    .Phone.NUMBER);
                            String name = cursor.getString(nameFieldColumnIndex);
                            String number = cursor.getString(numberFieldColumnIndex);
                            //去掉空格
                            String trimNumber = number.replace(" ", "");
                            if (trimNumber.length() > 11) {
                                trimNumber = trimNumber.substring(trimNumber.length() - 11);
                            }
                            Person person = new Person(name, trimNumber);
                            users.add(person);
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                        return users;
                    }

                    @Override
                    protected void onPostExecute(List<Person> persons) {
                        super.onPostExecute(persons);
                        uploadAddrList(persons);
                    }
                };
        contactTask.execute();
    }

    public static class Person {
        public Person(String name, String phone) {
            n = name;
            p = phone;
        }

        public String n;
        public String p;
    }
}
