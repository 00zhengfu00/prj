package com.physicmaster.modules.mine.activity.user;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseActivityManager;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.CircleImageDrawable;
import com.physicmaster.common.GetPictrueProcess;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.modules.mine.activity.location.CargoLocationActivity;
import com.physicmaster.modules.mine.activity.school.SelectSchoolActivity;
import com.physicmaster.modules.study.fragment.StudyFragmentV2;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.GetOssInfoResponse;
import com.physicmaster.net.response.account.GetOssInfoResponse.DataBean.OssConfigBean;
import com.physicmaster.net.response.account.GetOssInfoResponse.DataBean.OssTokenBean;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.account.GetOssInfoService;
import com.physicmaster.net.service.user.ChageUserService;
import com.physicmaster.net.service.user.UpdataGradeSubjectService;
import com.physicmaster.utils.DateUtils;
import com.physicmaster.utils.OssUtils;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.utils.SubjectNameUtil;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.Utils;
import com.physicmaster.widget.ImageUploadDialog;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.RoundImageView;
import com.physicmaster.widget.TitleBuilder;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class UserActivity extends BaseActivity implements View.OnClickListener {

    private String beginTime;
    private TextView tvBirthday;

    private TextView tvName;
    private GetPictrueProcess avatorProcess;
    private RoundImageView ivChagedHeaders;
    private String currentTime;
    private ArrayList<String> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private String str1;
    private String str2;

    /**
     * Android6.0需要申请权限
     */
    private final static String DANGEROUS_PERMISSION[] = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final static int CAMERA_REQUEST_CODE = 1;

    private TextView tvSchool;
    private TextView tvGrade;
    private UserDataResponse.UserDataBean.LoginVoBean mDataBean;
    private TextView tvGender;
    private SelectChageReceiver mSelectChageReceiver;
    private SwipeRefreshLayout swipeContainer;
    private BaseActivityManager mActivityManager;
    private RelativeLayout rlBirthday;
    private RelativeLayout rlSchool;
    private RelativeLayout rlGradeSubject;
    private RelativeLayout rlName;
    private RelativeLayout rlChagedHeaders;
    private RelativeLayout rlGender;
    private TimePickerView pvTime;
    private RelativeLayout rlVersion;
    private RelativeLayout rlSubject;
    private TextView tvVersion;
    private OptionsPickerView pvOptions;
    private boolean success;
    private TextView tvLocation;
    private RelativeLayout rlLocation;
    private TextView tvBook;
    private RelativeLayout rlGrade;
    private TimePickerView pvCustomTime;

    private boolean backPressed = false;

    @Override
    protected void findViewById() {

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        ivChagedHeaders = (RoundImageView) findViewById(R.id.iv_chaged_headers);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvGender = (TextView) findViewById(R.id.tv_gender);
        tvBirthday = (TextView) findViewById(R.id.tv_birthday);
        tvSchool = (TextView) findViewById(R.id.tv_school);
        tvGrade = (TextView) findViewById(R.id.tv_grade);
        tvBook = (TextView) findViewById(R.id.tv_book);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        tvLocation = (TextView) findViewById(R.id.tv_location);


        rlChagedHeaders = (RelativeLayout) findViewById(R.id.rl_chaged_headers);
        rlName = (RelativeLayout) findViewById(R.id.rl_name);
        rlGender = (RelativeLayout) findViewById(R.id.rl_gender);
        rlBirthday = (RelativeLayout) findViewById(R.id.rl_birthday);
        rlSchool = (RelativeLayout) findViewById(R.id.rl_school);
        rlLocation = (RelativeLayout) findViewById(R.id.rl_location);
        rlGradeSubject = (RelativeLayout) findViewById(R.id.rl_grade_subject);
        rlGrade = (RelativeLayout) findViewById(R.id.rl_grade);
        //        rlSubject = (RelativeLayout) findViewById(R.id.rl_subject);
        //        rlVersion = (RelativeLayout) findViewById(R.id.rl_version);
        initTitle();

        rlChagedHeaders.setOnClickListener(this);
        rlGrade.setOnClickListener(this);
        rlLocation.setOnClickListener(this);
        rlName.setOnClickListener(this);
        rlGender.setOnClickListener(this);
        rlBirthday.setOnClickListener(this);
        rlSchool.setOnClickListener(this);
        rlGradeSubject.setOnClickListener(this);

        swipeContainer.setColorSchemeResources(android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            }
        });
        registerSchoolChangeBroadcast();
    }


    //性别
    public void setTvGender(String gender) {
        tvGender.setText(gender);
    }

    //年级
    public void setTvGrade(String grade) {
        tvGrade.setText(grade);
    }

    private void registerSchoolChangeBroadcast() {
        mSelectChageReceiver = new SelectChageReceiver();
        registerReceiver(mSelectChageReceiver, new IntentFilter("com.physicmaster.SELECT_SCHOOL"));
    }

    public class SelectChageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.physicmaster.SELECT_SCHOOL")) {
                String schoolName = intent.getStringExtra("schoolName");
                if (!TextUtils.isEmpty(schoolName)) {
                    tvSchool.setText(schoolName);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillData();
    }


    @Override
    protected void onPause() {
        backPressed = true;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (null != mSelectChageReceiver) {
            unregisterReceiver(mSelectChageReceiver);
            mSelectChageReceiver = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 填充控件数据
     */
    private void fillData() {
        mDataBean = BaseApplication.getUserData();
        if (mDataBean == null) {
            gotoLoginActivity();
            return;
        }

        Glide.with(this).load(mDataBean.portrait).placeholder(R.drawable.placeholder_gray)
                .into(ivChagedHeaders);
        if (!TextUtils.isEmpty(mDataBean.schoolName)) {
            tvSchool.setText(mDataBean.schoolName);
        } else {
            tvSchool.setText("未知");
        }

        if (0 != mDataBean.gender) {
            if (mDataBean.gender == 1) {
                tvGender.setText("男");
            } else {
                tvGender.setText("女");
            }
        } else {
            tvGender.setText("未知");
        }

        if (!TextUtils.isEmpty(mDataBean.birthday)) {
            tvBirthday.setText(mDataBean.birthday);
        } else {
            tvBirthday.setText("未知");
        }

        if (!TextUtils.isEmpty(mDataBean.nickname)) {
            tvName.setText(mDataBean.nickname);
        } else {
            tvName.setText("昵称");
        }

        if (!TextUtils.isEmpty(mDataBean.eduGrade + "") && mDataBean.eduGrade != 0) {
            String grade = SubjectNameUtil.gradeSelected(mDataBean.eduGrade);
            tvGrade.setText(grade);
        } else {
            tvGrade.setText("年级");
        }

        showSubjectGrade();
    }

    /**
     * 根据图片Uri地址转化为Bitmap
     */
    public Bitmap decodeUriAsBitmap() {
        Bitmap bitmap = null;
        try {
            if (!TextUtils.isEmpty(mDataBean.portrait)) {
                String userhead = mDataBean.portrait;
                bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(Uri
                        .parse(userhead)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示，以及事件
         * 2.设置中间TextView显示的文字
         */
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("个人信息");
    }


    @Override
    protected void initView() {
        avatorProcess = new GetPictrueProcess(UserActivity.this);
        mActivityManager = BaseActivityManager.getInstance();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_user;
    }


    @Override
    public void onClick(View view) {
        if (BaseApplication.getUserData().isTourist == 1) {
            Utils.gotoLogin(UserActivity.this);
            return;
        }
        switch (view.getId()) {
            case R.id.rl_chaged_headers:
                permissionRequest();
                break;
            case R.id.rl_name:
                startActivityForResult(new Intent(this, ChageNameActivity.class), 0);
                break;
            case R.id.rl_gender:
                new GenderDialogFragment()
                        .show(getSupportFragmentManager(), "dialog_gender");
                break;
            case R.id.rl_birthday:
                showBirthday();
                break;
            case R.id.rl_school:
                startActivity(new Intent(this, SelectSchoolActivity.class));
                break;
            case R.id.rl_grade_subject:
//                showSelect2();
                break;
            case R.id.rl_location:
                startActivity(new Intent(this, CargoLocationActivity.class));
                break;
            case R.id.rl_grade:
                new GradeDialogFragment()
                        .show(getSupportFragmentManager(), "dialog_grade");
                break;
        }
    }

    private void showSubjectGrade() {
        if (!TextUtils.isEmpty(mDataBean.bookName)) {
            tvBook.setText(mDataBean.bookName + "");
        }
    }

    private void showSelect2() {
        String bookData = "[\n" +
                "    {\n" +
                "        \"books\": [\n" +
                "            {\n" +
                "                \"bookId\": 6,\n" +
                "                \"name\": \"七年级上册\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"bookId\": 7,\n" +
                "                \"name\": \"七年级下册\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"bookId\": 8,\n" +
                "                \"name\": \"八年级上册\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"bookId\": 9,\n" +
                "                \"name\": \"八年级下册\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"bookId\": 10,\n" +
                "                \"name\": \"九年级上册\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"bookId\": 11,\n" +
                "                \"name\": \"九年级下册\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"id\": 4,\n" +
                "        \"name\": \"初中数学\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"books\": [\n" +
                "            {\n" +
                "                \"bookId\": 1,\n" +
                "                \"name\": \"八年级上册\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"bookId\": 2,\n" +
                "                \"name\": \"八年级下册\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"bookId\": 3,\n" +
                "                \"name\": \"九年级全册\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"初中物理\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"books\": [\n" +
                "            {\n" +
                "                \"bookId\": 4,\n" +
                "                \"name\": \"九年级上册\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"bookId\": 5,\n" +
                "                \"name\": \"九年级下册\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"id\": 2,\n" +
                "        \"name\": \"初中化学\"\n" +
                "    }\n" +
                "]";
        final List<Book> books = JSON.parseArray(bookData, Book.class);
        //选项选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                final int bookId;
                try {
                    str1 = options1Items.get(options1);
                    str2 = options2Items.get(options1).get(option2);
                    bookId = books.get(options1).books.get(option2).bookId;
                } catch (Exception e) {
                    e.printStackTrace();
                    UIUtils.showToast(UserActivity.this, "操作慢一点哦");
                    return;
                }
                SpUtils.putString(UserActivity.this, CacheKeys.SUBJECT_ACTION, options1 + "");
                SpUtils.putString(UserActivity.this, CacheKeys.GRAGE_ACTION, option2 + "");
                final UpdataGradeSubjectService service = new UpdataGradeSubjectService(UserActivity.this);
                service.setCallback(new IOpenApiDataServiceCallback<UserDataResponse>() {

                    @Override
                    public void onGetData(UserDataResponse data) {
                        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                                .USERINFO_LOGINVO, data.data.loginVo);
                        BaseApplication.setUserData(data.data.loginVo);
                        SpUtils.putString(UserActivity.this, CacheKeys.SUBJECT_STUDY_INFO, bookId + "");
                        if ((!TextUtils.isEmpty(str1)) && (!TextUtils.isEmpty(str2))) {
                            tvBook.setText(str1 + " " + str2);
                        }
                        LocalBroadcastManager.getInstance(UserActivity.this).sendBroadcast(new Intent(StudyFragmentV2.ON_SUBJECT_CHANGED));
                    }

                    @Override
                    public void onGetError(int errorCode, String errorMsg, Throwable error) {
                        UIUtils.showToast(UserActivity.this, errorMsg);
                    }
                });
                service.postLogined("bookId=" + bookId, false);
            }
        }).isDialog(false)
                .setTitleText("选择课本")
                .build();

        options1Items.clear();
        options2Items.clear();
        //选项1
        for (int i = 0; i < books.size(); i++) {
            options1Items.add(books.get(i).name);
            ArrayList<String> options2Item = new ArrayList<>();
            for (Book.BooksBean booksBean : books.get(i).books) {
                options2Item.add(booksBean.name);
            }
            options2Items.add(options2Item);
        }

        String action1 = SpUtils.getString(this, CacheKeys.SUBJECT_ACTION, "");
        String action2 = SpUtils.getString(this, CacheKeys.GRAGE_ACTION, "");
        if (TextUtils.isEmpty(action1) && TextUtils.isEmpty(action2)) {
            pvOptions.setSelectOptions(0, 0);
        } else {
            pvOptions.setSelectOptions(Integer.parseInt(action1), Integer.parseInt(action2));
        }
        pvOptions.setPicker(options1Items, options2Items);
        pvOptions.show();
    }

    private void showBirthday() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        startDate.set(1990, 0, 1);
        endDate.set(2020, 11, 31);

        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //选中事件回调
                chageBirthday(getTime(date));
                tvBirthday.setText(getTime(date));
            }
        }).isDialog(true)
                .setTitleText("选择生日")
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(getResources().getColor(R.color.color_background))
                .build();
        pvCustomTime.show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (pvTime != null && pvTime.isShowing()) {
                pvTime.dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void pickAvator() {
        final ImageUploadDialog dialog = new ImageUploadDialog(this, R.style.bottomOutStyle);
        avatorProcess = new GetPictrueProcess(this);
        dialog.setOnBack(new ImageUploadDialog.OnBack() {

            @Override
            public void click(int btn) {
                dialog.dismiss();
                Intent intent;
                if (R.id.btn_select_picture == btn) {
                    intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    if (null == intent.resolveActivity(getPackageManager())) {
                        UIUtils.showToast(UserActivity.this, "没有找到可供选择图片的软件");
                        return;
                    }
                    startActivityForResult(intent, GetPictrueProcess.REQUEST_CODE_PICK_IMAGE);
                } else {
                    intent = avatorProcess.takePicture();
                    startActivityForResult(intent, GetPictrueProcess.REQUEST_CODE_TAKE_PICTURE);
                }
            }
        });
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && data.hasExtra("name")) {
            String name = data.getStringExtra("name");
            tvName.setText(name);
        }

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == GetPictrueProcess.REQUEST_CODE_PICK_IMAGE) {
            if (data != null) {
                Intent intent = avatorProcess.cropAvatar(data.getData());
                startActivityForResult(intent, GetPictrueProcess.REQUEST_CODE_IMAGE_CROPPER);
            }
        } else if (requestCode == GetPictrueProcess.REQUEST_CODE_TAKE_PICTURE) {
            Intent intent = avatorProcess.cropAvatar(null);
            startActivityForResult(intent, GetPictrueProcess.REQUEST_CODE_IMAGE_CROPPER);
        } else if (requestCode == GetPictrueProcess.REQUEST_CODE_IMAGE_CROPPER) {
            getOssInfo();
            Bitmap bitmap = avatorProcess.decodeUriAsBitmap();
            if (null != bitmap) {
                CircleImageDrawable drawable = new CircleImageDrawable(bitmap);
                ivChagedHeaders.setImageDrawable(drawable);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void chageBirthday(final String date) {

        if (TextUtils.isEmpty(date)) {
            UIUtils.showToast(this, "请选择生日");
            return;
        }
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        final ChageUserService service = new ChageUserService(this);
        service.setCallback(new IOpenApiDataServiceCallback<UserDataResponse>() {

            @Override
            public void onGetData(UserDataResponse data) {
                UIUtils.showToast(UserActivity.this, "修改成功");
                CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO,
                        data.data.loginVo);
                BaseApplication.setUserData(data.data.loginVo);
                tvBirthday.setText(DateUtils.formateStringH(date, DateUtils.yyyyMMdd));
                loadingDialog.dismissDialog();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(UserActivity.this, errorMsg);
                loadingDialog.dismissDialog();
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("bday=" + date, false);
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    /***************************************
     * 阿里云oss开始
     ********************************/

    /**
     * Android6.0权限申请
     */
    private void permissionRequest() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                // 申请权限
                ActivityCompat.requestPermissions(this, DANGEROUS_PERMISSION,
                        CAMERA_REQUEST_CODE);
            } else {
                // 权限已经授予,直接初始化
                pickAvator();
            }
        } else {
            pickAvator();
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限授予成功,初始化
                Log.i("result", "成功获得授权");
                pickAvator();
            } else {
                Log.i("result", "未获得授权");
                // 三方处理自己逻辑,这里只做测试用
                UIUtils.showToast(UserActivity.this, "您拒绝了拍照权限，无法上传头像");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 获取阿里云访问token
     */
    private void getOssInfo() {
        GetOssInfoService service = new GetOssInfoService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetOssInfoResponse>() {
            @Override
            public void onGetData(GetOssInfoResponse data) {
                try {
                    startUpload(data.data.ossToken, data.data.ossConfig);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(UserActivity.this, "获取上传图片配置失败，请重试");
            }
        });
        service.postLogined("", true);
    }

    /**
     * 开始上传
     */
    private void startUpload(OssTokenBean ossTokenBean, OssConfigBean ossConfigBean) {
        String path = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            path = avatorProcess.getCropAvatarPath();
        } else {
            path = avatorProcess.getAfterCropImageUri().getPath().toString();
        }
        final String localPath = path;
        OssUtils.initOss(getApplicationContext(), ossTokenBean.tempAk, ossTokenBean.tempSk, ossConfigBean.hostId, ossTokenBean.securityToken);
        UserDataResponse.UserDataBean.LoginVoBean loginVoBean = BaseApplication.getUserData();
        if (loginVoBean == null) {
            gotoLoginActivity();
            return;
        }
        final String avatorName = loginVoBean.dtUserId + System.currentTimeMillis() + ".jpeg";
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        final AsyncTask<Void, Integer, Boolean> uploadTask = new AsyncTask<Void, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                if (isCancelled()) {
                    return false;
                }
                OssUtils.uploadFile(ossConfigBean.bucketName, ossConfigBean.imgPath + avatorName, localPath, new
                        OssUtils.OnUploadListener() {

                            @Override
                            public void onUploadSucc(final String path) {
                                success = true;
                            }

                            @Override
                            public void onUploadFail(String msg) {
                                success = false;
                            }

                            @Override
                            public void onUploadProgress(PutObjectRequest request, long currentSize, long totalSize) {
                            }
                        });
                return success;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismissDialog();
                if (aBoolean) {
                    UIUtils.showToast(UserActivity.this, "上传成功！");
                    notifyServer(ossConfigBean.imgPath + avatorName);
                } else {
                    UIUtils.showToast(UserActivity.this, "上传失败！");
                }
            }
        };
        loadingDialog.showDialog(() -> uploadTask.cancel(true));
        uploadTask.execute();
    }

    /**
     * 将上传图片信息通知服务器
     *
     * @param path
     */
    private void notifyServer(final String path) {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        final ChageUserService service = new ChageUserService(this);
        service.setCallback(new IOpenApiDataServiceCallback<UserDataResponse>() {

            @Override
            public void onGetData(UserDataResponse data) {
                UIUtils.showToast(UserActivity.this, "修改成功");
                CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO,
                        data.data.loginVo);
                BaseApplication.setUserData(data.data.loginVo);
                Glide.with(UserActivity.this).load(data.data.loginVo.portraitSmall).into
                        (ivChagedHeaders);
                loadingDialog.dismissDialog();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(UserActivity.this, errorMsg);
                loadingDialog.dismissDialog();
            }
        });
        loadingDialog.showDialog(() -> service.cancel());
        service.postLogined("portrait=" + path, false);
    }
    /************************************阿里云oss结束**********************************/
}

