package com.hwand.pinhaowanr.mine;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.DataCacheHelper;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.main.MineFragment;
import com.hwand.pinhaowanr.model.UserInfo;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.CircleImageView;
import com.hwand.pinhaowanr.widget.DDAlertDialog;
import com.hwand.pinhaowanr.widget.FetchPhotoDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dxz on 15/12/01.
 */
public class UserInfoFragment extends BaseFragment {

    private FetchPhotoDialog mFetchPhotoDialog;

    /**
     * 请求相机.
     */
    public static final int REQUEST_IMAGE_CAPTURE = 10008;

    /**
     * 请求相册.
     */
    public static final int REQUEST_PHOTO_LIBRARY = 10009;

    private Uri mImageUri;

    private String mPath;

    public static UserInfoFragment newInstance() {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    private static final int MSG_INTENT_HEAD = 20;
    private static final int MSG_INTENT_CHILD_NAME = 21;
    private static final int MSG_INTENT_CHILD_SEX = 22;
    private static final int MSG_INTENT_CHILD_BIRTH = 23;
    private static final int MSG_INTENT_ADD = 24;
    private static final int MSG_INTENT_CONTENT = 25;


    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!isAdded()) {
                return;
            }
            FragmentManager fm = getFragmentManager();
            FragmentTransaction tx = fm.beginTransaction();
            switch (msg.what) {
                case MSG_INTENT_HEAD:
                    break;
                case MSG_INTENT_CHILD_NAME:
                    ChildNameFragment fragment = ChildNameFragment.newInstance();
                    tx.hide(UserInfoFragment.this);
                    tx.add(R.id.fragment_container, fragment, "ChildNameFragment");
                    tx.addToBackStack(null);
                    tx.commit();
                    break;
                case MSG_INTENT_CHILD_SEX:
                    SexModifyFragment fragmentSex = SexModifyFragment.newInstance();
                    tx.hide(UserInfoFragment.this);
                    tx.add(R.id.fragment_container, fragmentSex, "SexModifyFragment");
                    tx.addToBackStack(null);
                    tx.commit();
                    break;
                case MSG_INTENT_CHILD_BIRTH:
                    BirthdayModifyFragment fragmentBirth = BirthdayModifyFragment.newInstance();
                    tx.hide(UserInfoFragment.this);
                    tx.add(R.id.fragment_container, fragmentBirth, "BirthdayModifyFragment");
                    tx.addToBackStack(null);
                    tx.commit();
                    break;
                case MSG_INTENT_ADD:
                    AddressFragment fragmentAdd = AddressFragment.newInstance();
                    tx.hide(UserInfoFragment.this);
                    tx.add(R.id.fragment_container, fragmentAdd, "AddressFragment");
                    tx.addToBackStack(null);
                    tx.commit();
                    break;
                case MSG_INTENT_CONTENT:
                    ContentFragment fragmentCon = ContentFragment.newInstance();
                    tx.hide(UserInfoFragment.this);
                    tx.add(R.id.fragment_container, fragmentCon, "ContentFragment");
                    tx.addToBackStack(null);
                    tx.commit();
                    break;
                default:
                    break;

            }

        }
    };

    private ListView mListView;
    private View mHeader;
    private CircleImageView mHeadImageView;

    private MineAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_info_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();

        setTitleBarTtile("个人信息");
        mListView = (ListView) mFragmentView.findViewById(R.id.nv_list);
        mHeader = getActivity().getLayoutInflater().inflate(R.layout.info_header_layout, null);
        mHeadImageView = (CircleImageView) mHeader.findViewById(R.id.head);

        List<MineAdapter.NaviEntity> list = new ArrayList<MineAdapter.NaviEntity>();
        list.add(new MineAdapter.NaviEntity("宝宝名", MSG_INTENT_CHILD_NAME));
        list.add(new MineAdapter.NaviEntity("宝宝性别", MSG_INTENT_CHILD_SEX));
        list.add(new MineAdapter.NaviEntity("宝宝出生", MSG_INTENT_CHILD_BIRTH));
        list.add(new MineAdapter.NaviEntity("家庭地址", MSG_INTENT_ADD));
        list.add(new MineAdapter.NaviEntity("个人介绍", MSG_INTENT_CONTENT));
        mAdapter = new MineAdapter(getActivity(), mHandler, list);
        mListView.addHeaderView(mHeader);
        mListView.setAdapter(mAdapter);
        mHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyHeadPic();
            }
        });
        try {
            String url = DataCacheHelper.getInstance().getUserInfo().getUrl();
            LogUtil.d("dxz", url);
            ImageLoader.getInstance().displayImage(url, mHeadImageView);
        } catch (Exception e) {
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MineFragment.setNoExit(true);
    }

    private void modifyHeadPic() {
        if (mFetchPhotoDialog == null) {
            mFetchPhotoDialog = new FetchPhotoDialog(getActivity());
            mFetchPhotoDialog.setFetchPhoteClickListener(mFetchPhotoClickListener);
        }
        mFetchPhotoDialog.show();
    }

    private final FetchPhotoDialog.FetchPhotoClickListener mFetchPhotoClickListener = new FetchPhotoDialog.FetchPhotoClickListener() {
        @Override
        public void fetchFromAblumClick() {
            openPhotoLibrary(REQUEST_PHOTO_LIBRARY);

        }

        @Override
        public void fetchFromCameraClick() {
            mImageUri = openCapture(REQUEST_IMAGE_CAPTURE);
        }

        @Override
        public void cancelClick() {

        }
    };

    private Uri openCapture(int requestCode) {

        if (!isCanUseSDCard()) {
            Toast.makeText(getActivity(), R.string.sdcard_unavailable, Toast.LENGTH_SHORT).show();
            return null;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String filename = timeStampFormat.format(new Date());
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, filename);
        Uri uri = null;
        try {
            uri = getActivity().getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } catch (Throwable ex) {
            // 红米手机会出现无法创建文件的crash，做一个保护
            File file = new File(getImageCacheDir(getActivity()).getAbsolutePath(), filename + ".jpg");
            uri = Uri.fromFile(file);
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        getRootFragment().startActivityForResult(intent, requestCode);
        return uri;
    }

    private boolean isCanUseSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private File getImageCacheDir(Context context) {
        return getCacheDirByType("uil-images");
    }

    private File getCacheDirByType(String type) {
        File cacheDir = getActivity().getCacheDir();
        File typeCacheDir = new File(cacheDir, type);
        if (typeCacheDir != null && !typeCacheDir.exists()) {
            typeCacheDir.mkdirs();
        }
        return typeCacheDir;
    }

    private void openPhotoLibrary(int requestCode) {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            getRootFragment().startActivityForResult(intent, requestCode);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


//    private static final int ASPECT_X = 100;
//    private static final int ASPECT_Y = 100;
//
//    private static final String PIC_FORMAT = ".jpg";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
//            String tempFileName = System.currentTimeMillis() + PIC_FORMAT; // 图片剪裁的时候存储的临时文件的名称
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    // 设置头像使用图片剪裁
                    if (mImageUri != null) {
//                        startCropImageActivityForResult(mImageUri, false, REQUEST_CODE_IMAGE_CROP, PIC_FORMAT, ASPECT_X, ASPECT_Y);
                    }
                    break;
                case REQUEST_PHOTO_LIBRARY:
                    if (data == null)
                        return;
                    Uri uri = data.getData();
                    if (uri == null) {
                        return;
                    }
                    mImageUri = uri;
//                    startCropImageActivityForResult(uri, false, REQUEST_CODE_IMAGE_CROP, PIC_FORMAT, ASPECT_X, ASPECT_Y);
                    break;
//                case REQUEST_CODE_IMAGE_CROP:
//                    uploadAvatarFile(data);
//                    break;
//
//                    break;

            }
            if (mImageUri != null) {
                mPath = AndTools.getImageAbsolutePath(getActivity(), mImageUri);
                LogUtil.d("dxz", mImageUri.toString());
                LogUtil.d("dxz", mPath);
                upload();
            }
        }
    }

    /**
     * 得到根Fragment
     *
     * @return
     */
    private Fragment getRootFragment() {
        Fragment fragment = getParentFragment();
        while (fragment.getParentFragment() != null) {
            fragment = fragment.getParentFragment();
        }
        return fragment;

    }

    private void upload() {
        try {
            if (mImageUri != null) {
                Map<String, String> params = new HashMap<String, String>();
                NetworkRequest.upload(UrlConfig.URL_MODIFY_HEAD, mPath, params, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtil.d("dxz", response);
                        // 结果（result）0 失败 1 成功
                        if (!TextUtils.isEmpty(response) && response.contains("1")) {
                            AndTools.showToast("修改头像成功！");
                            try {
                                JSONObject json = new JSONObject(response);
                                String url = json.optString("url");
                                LogUtil.d("dxz", url);
                                ImageLoader.getInstance().displayImage(mImageUri.toString(), mHeadImageView);
                                UserInfo info = DataCacheHelper.getInstance().getUserInfo();
                                info.setUrl(url);
                                Gson gson = new Gson();
                                String str = gson.toJson(info, UserInfo.class);
                                DataCacheHelper.getInstance().saveUserInfo(str);
                                ImageLoader.getInstance().loadImageSync(url);
                            } catch (Exception e) {
                                ImageLoader.getInstance().displayImage(DataCacheHelper.getInstance().getUserInfo().getUrl(), mHeadImageView);
                            }

                        } else {
                            ImageLoader.getInstance().displayImage(DataCacheHelper.getInstance().getUserInfo().getUrl(), mHeadImageView);
                            new DDAlertDialog.Builder(getActivity())
                                    .setTitle("提示").setMessage("网络问题请重试！")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ImageLoader.getInstance().displayImage(DataCacheHelper.getInstance().getUserInfo().getUrl(), mHeadImageView);
                        new DDAlertDialog.Builder(getActivity())
                                .setTitle("提示").setMessage("网络问题请重试！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                });
            }
        } catch (Exception e) {

        }

    }

}
