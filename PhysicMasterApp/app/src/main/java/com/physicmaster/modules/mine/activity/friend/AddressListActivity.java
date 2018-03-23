package com.physicmaster.modules.mine.activity.friend;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.common.Constant;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.CommonResponse;
import com.physicmaster.net.response.im.FindFriendBatchResponse;
import com.physicmaster.net.service.im.FindFriendBatchService;
import com.physicmaster.net.service.im.InviteFriendService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.RoundImageView;
import com.physicmaster.widget.TitleBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class AddressListActivity extends BaseActivity {
    private ProgressLoadingDialog progressLoadingDialog;
    private ListView lvContactFriends;
    private List<FindFriendBatchResponse.DataBean.UserVoListBean> mFriendList;
    private AddFriendAdapter addFriendAdapter;

    @Override
    protected void findViewById() {
        lvContactFriends = (ListView) findViewById(R.id.lv_contact_friends);
    }

    @Override
    protected void initView() {
        initTitle();
        lvContactFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AddressListActivity.this, FriendInfoActivity.class);
                intent.putExtra("dtUserId", id + "");
                intent.putExtra("position", position);
                startActivityForResult(intent, 1);
            }
        });
        requestPermissions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                int position = data.getIntExtra("position", -1);
                boolean isAdded = data.getBooleanExtra("isAdded", false);
                if (isAdded && position != -1) {
                    mFriendList.get(position).added = true;
                    addFriendAdapter.notifyDataSetChanged();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_address_list;
    }

    private void initTitle() {
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("通讯录好友");
    }

    private void queryContactPhoneNumber() {
        progressLoadingDialog = new ProgressLoadingDialog(this);
        final AsyncTask<Boolean, Integer, List<Person>> contactTask = new AsyncTask<Boolean,
                Integer,
                List<Person>>() {
            @Override
            protected List<Person> doInBackground(Boolean... params) {
                String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, cols, null, null, null);
                List<Person> users = new ArrayList<>();
                if (cursor == null || cursor.getCount() == 0) {
                    return users;
                }
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    // 取得联系人名字
                    int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup
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
                return users;
            }

            @Override
            protected void onPostExecute(List<Person> persons) {
                super.onPostExecute(persons);
                if (0 == persons.size()) {
                    String name = "物理大师";
                    String packageName = getPackageName();
                    if (packageName.equals(Constant.CHYMISTMASTER)) {
                        name = "化学大师";
                    } else if (packageName.equals(Constant.MATHMASTER)) {
                        name = "数学大师";
                    }
                    UIUtils.showToast(AddressListActivity.this, "请在设置中打开" + name +
                            "通讯录权限，否则无法读取联系人");
                }
                findFriendBatch(persons);
                progressLoadingDialog.dismissDialog();
            }
        };
        progressLoadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                contactTask.cancel(true);
            }
        });
        contactTask.execute();
    }

    private static final int REQUEST_CONTACTS = 101;
    private static String[] PERMISSIONS_CONTACTS = {
            Manifest.permission.READ_CONTACTS};

    public void requestPermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACTS,
                    REQUEST_CONTACTS);
        } else {
            queryContactPhoneNumber();
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == REQUEST_CONTACTS) {
            if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限授予成功,初始化
                queryContactPhoneNumber();
            } else {
                UIUtils.showToast(AddressListActivity.this, "请在设置中打开物理大师通讯录权限，否则无法读取联系人");
            }
        }
    }

    /**
     * 批量搜索通讯录好友
     */
    private void findFriendBatch(List<Person> persons) {
        FindFriendBatchService service = new FindFriendBatchService(this);
        service.setCallback(new IOpenApiDataServiceCallback<FindFriendBatchResponse>() {
            @Override
            public void onGetData(FindFriendBatchResponse data) {
                mFriendList = data.data.userVoList;
                if (null == mFriendList || 0 == mFriendList.size()) {
                    findViewById(R.id.rl_empty).setVisibility(View.VISIBLE);
                    lvContactFriends.setVisibility(View.GONE);
                } else {
                    addFriendAdapter = new AddFriendAdapter();
                    lvContactFriends.setAdapter(addFriendAdapter);
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {

            }
        });
        String param = JSON.toJSONString(persons);
        service.postLogined("phones=" + param, false);
    }

    public static class Person {
        public Person(String name, String phone) {
            if (TextUtils.isEmpty(name)) {
                name = "未知";
            }
            try {
                n = URLEncoder.encode(name, Constant.CHARACTER_ENCODING);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            p = phone;
        }

        public String n;
        public String p;
    }

    class AddFriendAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mFriendList.size();
        }

        @Override
        public FindFriendBatchResponse.DataBean.UserVoListBean getItem(int position) {
            return mFriendList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mFriendList.get(position).dtUserId;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(AddressListActivity.this,
                        R.layout.list_item_addfriend, null);
                holder = new ViewHolder();
                holder.ivHeader = (RoundImageView) convertView
                        .findViewById(R.id.iv_header);
                holder.ivGender = (ImageView) convertView
                        .findViewById(R.id.iv_gender);
                holder.tvUserName = (TextView) convertView
                        .findViewById(R.id.tv_user_name);
                holder.tvIntroduction = (TextView) convertView
                        .findViewById(R.id.tv_introduction);
                holder.btnAdd = (Button) convertView
                        .findViewById(R.id.btn_add);
                holder.tvAdded = (TextView) convertView
                        .findViewById(R.id.tv_added);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final FindFriendBatchResponse.DataBean.UserVoListBean item = getItem(position);

            String name = item.nickname;
            String uname = "Lv" + item.userLevel;
            String str = name + uname;
            final SpannableStringBuilder sp = new SpannableStringBuilder(str);
            sp.setSpan(new ForegroundColorSpan(0xFFFCBE01), name.length(), str.length(), Spanned
                    .SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
            sp.setSpan(new AbsoluteSizeSpan(14, true), name.length(), str.length(), Spanned
                    .SPAN_EXCLUSIVE_EXCLUSIVE); //字体大小
            holder.tvUserName.setText(sp);

            holder.tvIntroduction.setText(item.intro + "");

            if (1 == item.gender) {
                holder.ivGender.setVisibility(View.VISIBLE);
                holder.ivGender.setImageResource(R.mipmap.nan);
            } else if (2 == item.gender) {
                holder.ivGender.setVisibility(View.VISIBLE);
                holder.ivGender.setImageResource(R.mipmap.nv);
            } else {
                holder.ivGender.setVisibility(View.GONE);
            }
            Glide.with(AddressListActivity.this).load(item.portrait).placeholder(R.drawable
                    .placeholder_gray)
                    .into(holder.ivHeader);
            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inviteFriend(item.dtUserId, position);
                }
            });
            if (item.friendState == 0) {
                holder.tvAdded.setVisibility(View.GONE);
                holder.btnAdd.setVisibility(View.VISIBLE);

                if (item.added) {
                    holder.tvAdded.setVisibility(View.VISIBLE);
                    holder.btnAdd.setVisibility(View.GONE);
                    holder.tvAdded.setText("等待验证");
                } else {
                    holder.tvAdded.setVisibility(View.GONE);
                    holder.btnAdd.setVisibility(View.VISIBLE);
                }

            } else if (item.friendState == 1) {
                holder.tvAdded.setVisibility(View.VISIBLE);
                holder.btnAdd.setVisibility(View.GONE);
                holder.tvAdded.setText("已是好友");
            } else if (item.friendState == 2) {
                holder.tvAdded.setVisibility(View.VISIBLE);
                holder.btnAdd.setVisibility(View.GONE);
                holder.tvAdded.setText("待同意");
            } else if (item.friendState == 3) {
                holder.tvAdded.setVisibility(View.VISIBLE);
                holder.btnAdd.setVisibility(View.GONE);
                holder.tvAdded.setText("待验证");
            }
            return convertView;
        }
    }

    /**
     * 请求添加某人为好友
     *
     * @param userId
     */
    private void inviteFriend(int userId, final int position) {
        final InviteFriendService service = new InviteFriendService(this);
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                loadingDialog.dismissDialog();
                mFriendList.get(position).added = true;
                UIUtils.showToast(AddressListActivity.this, "发送成功");
                addFriendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                loadingDialog.dismissDialog();
                UIUtils.showToast(AddressListActivity.this, "发送失败");
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("invitationDtUserId=" + userId, false);
    }

    static class ViewHolder {
        TextView tvIntroduction;
        TextView tvUserName;
        RoundImageView ivHeader;
        ImageView ivGender;
        TextView tvAdded;
        Button btnAdd;
    }
}
