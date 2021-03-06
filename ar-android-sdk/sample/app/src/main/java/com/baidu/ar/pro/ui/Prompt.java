/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ar.pro.ui;

import java.util.HashMap;

import com.baidu.ar.DuMixCallback;
import com.baidu.ar.base.MsgField;
import com.baidu.ar.bean.ARResource;
import com.baidu.ar.bean.BrowserBean;
import com.baidu.ar.bean.TrackRes;
import com.baidu.ar.pro.R;
import com.baidu.ar.pro.callback.PromptCallback;
import com.baidu.ar.util.Res;
import com.baidu.ar.util.UiThreadUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 提示层UI
 * Created by xiegaoxi on 2018/5/10.
 */

public class Prompt extends RelativeLayout implements View.OnClickListener, DuMixCallback {

    public static final String TAG = "PromptView";
    /**
     * 返回按钮
     */
    private ImageView mIconBack;
    /**
     * 返回按钮
     */
    private ImageView mIconCamera;

    /**
     * 闪光灯按钮
     */
    private ImageView mIconFlash;

    /**
     * 闪光灯是否处于关闭模式
     */
    private boolean mIsFlashOff = true;

    /**
     * Du Mix 状态回调
     */
    private DuMixCallback mDuMixCallback;

    /**
     *
     */
    private PromptCallback mPromptCallback;

    /**
     * Du Mix 状态回调提示文字
     */
    private TextView mDumixCallbackTips;

    /**
     * 本地识图云端识图返回的arKey
     */
    private String arKey;

    /**
     * 构造函数
     *
     * @param context context
     */
    public Prompt(Context context) {
        super(context);
        init(context);
    }

    /**
     * 构造函数
     *
     * @param context context
     * @param attrs   attrs
     */
    public Prompt(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 构造函数
     *
     * @param context  context
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public Prompt(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * init layout
     */
    private void init(Context context) {

        LayoutInflater.from(context).inflate(R.layout.bdar_layout_prompt, this);
        // button
        mIconBack = findViewById(R.id.bdar_titlebar_back);
        mIconBack.setOnClickListener(this);
        mIconCamera = findViewById(R.id.bdar_titlebar_camera);
        mIconCamera.setOnClickListener(this);
        mIconFlash = findViewById(R.id.bdar_titlebar_flash);
        mIconFlash.setOnClickListener(this);

        mDumixCallbackTips = findViewById(R.id.bdar_titlebar_tips);

        mDuMixCallback = this;
    }

    public DuMixCallback getDuMixCallback() {
        return mDuMixCallback;
    }

    public void setPromptCallback(PromptCallback callback) {
        mPromptCallback = callback;
    }

    @Override
    public void onClick(View view) {
        // back
        if (view.getId() == R.id.bdar_titlebar_back) {
            if (mPromptCallback != null) {
                mPromptCallback.onBackPressed();
            }
        } else if (view.getId() == R.id.bdar_titlebar_camera) {
            if (mPromptCallback != null) {
                mPromptCallback.onSwitchCamera();
            }
        } else if (view.getId() == R.id.bdar_titlebar_flash) {
            if (mPromptCallback != null) {
                mPromptCallback.onCameraFlashStatus(mIsFlashOff);
            }
            mIsFlashOff = !mIsFlashOff;
            if (mIsFlashOff) {
                mIconFlash.setImageDrawable(getResources().getDrawable(R.drawable
                        .bdar_drawable_btn_flash_disable_selector));
            } else {
                mIconFlash.setImageDrawable(getResources().getDrawable(R.drawable
                        .bdar_drawable_btn_flash_enable_selector));
            }
        }
    }

    public void release() {
        mDuMixCallback = null;
        mPromptCallback = null;
    }

    // callback
    @Override
    public void onStateChange(final int state, final Object msg) {
        Log.e(TAG, "onStateChange, state = " + state + " msg = " + msg);

        switch (state) {
            // so加载成功
            case MsgField.IMSG_SO_LOAD_SUCCESS:
                showToast("so load success");
                break;

            // so加载失败
            case MsgField.IMSG_SO_LOAD_FAILED:
                showToast("so load failed");
                break;

            // 解压zip失败
            case MsgField.MSG_ON_PARSE_RESOURCE_UNZIP_ERROR:
                showToast(Res.getString("bdar_error_unzip"));
                break;
            case MsgField.MSG_ON_PARSE_RESOURCE_JSON_ERROR:
                showToast(Res.getString("bdar_error_json_parser"));
                break;

            // 识图AR错误消息
            case MsgField.IMSG_RECGAR_TOAST_ERROR:
                showToast("识图AR错误消息");
                break;

            // 识图AR网络错误
            case MsgField.IMSG_RECGAR_NETWORT_ERROR:
                showToast("识图AR网络错误");
                break;

            // 云端识图AR错误消息
            case MsgField.IMSG_CLOUDAR_TOAST_ERROR:
                showToast("云端识图AR错误消息");
                break;

            // 截图成功
            case MsgField.IMSG_SAVE_PICTURE:
                showToast(" 截图成功");
                break;

            // 录制成功
            case MsgField.IMSG_SAVE_VIDEO:
                showToast(" 录制成功");
                break;

            // 网络未连接
            case MsgField.IMSG_NO_NETWORK:
                showToast(" 网络未连接");
                break;

            // 识图初始化
            case MsgField.IMSG_ON_DEVICE_IR_START:
                showToast(" 识图初始化");
                break;

            // 云端识图初始化
            case MsgField.IMSG_CLORD_ID_START:
                showToast(" 云端识图初始化");
                break;

            // track 模型显示
            case MsgField.IMSG_TRACK_MODEL_APPEAR:
                showToast(" track 模型显示");
                break;

            // slam 模型消失
            case MsgField.IMSG_SLAM_MODEL_DISAPPEAR:
                showToast(" slam 模型消失");
                break;

            // imu 模型消失
            case MsgField.IMSG_IMU_MODEL_DISAPPEAR:
                showToast(" imu 模型消失");
                break;

            // 2D算法跟踪丢失
            case MsgField.IMSG_TRACK_LOST:
                showToast(" 2D算法跟踪丢失 ");
                break;

            // 2D算法跟踪成功
            case MsgField.IMSG_TRACK_FOUND:
                showToast(" 2D算法跟踪成功 ");
                break;
            // 跟踪距离过远
            case MsgField.IMSG_TRACK_DISTANCE_TOO_FAR:
                showToast(" 跟踪距离过远 ");
                break;

            // 跟踪距离过近
            case MsgField.IMSG_TRACK_DISTANCE_TOO_NEAR:
                showToast(" 跟踪距离过近 ");
                break;

            // 跟踪距离正常
            case MsgField.IMSG_TRACK_DISTANCE_NORMAL:
                showToast(" 跟踪距离正常 ");
                break;

            // 引擎模型加载完毕, 所有ar业务都会发送此消息
            case MsgField.IMSG_MODEL_LOADED:
                showToast(" 引擎模型加载完毕 ");
                break;

            // Track消息 tips提示
            case MsgField.IMSG_TRACKED_TIPS_INFO:
                TrackRes trackRes = (TrackRes) msg;
                //               Log.e("onStateChange " , trackRes.getTipBean().get());
                //                initTipsInfo(trackRes);
                break;

            case MsgField.IMSG_MODE_SHOWING:
                break;

            // track 模型消失
            case MsgField.IMSG_TRACK_MODEL_NOT_SHOWING:
                break;
            // track 模型消失
            case MsgField.IMSG_TRACK_HIDE_LOST_INFO:
                // set lost info view GONE
                break;

            case MsgField.IMSG_TRACKED_TARGET_BITMAP_RES:
                break;

            case MsgField.MSG_ID_TRACK_MODEL_CAN_DISAPPEARING:
                break;

            case MsgField.MSG_ID_TRACK_MSG_ID_TRACK_LOST:
                break;
            case MsgField.MSG_OPEN_URL:
                BrowserBean browserBean = (BrowserBean) msg;
                String url = browserBean.getUrl();
                int type = browserBean.getType();
                // 打开url链接处理
                //                mUIController.getARCallback().openUrl(url);
                break;
            case MsgField.MSG_PADDLE_INIT:
                break;
            case MsgField.MSG_PADDLE_ENABLE:
                break;
            case MsgField.MSG_SHARE:
                break;
            // 本地识图 识别结果
            case MsgField.MSG_ON_DEVICE_IR_RESULT:
                arKey = (String) msg;
                // 根据本地识图结果 切换case
                mPromptCallback.onChangeCase(arKey);
                showToast(" 本地识图成功.切换CASE: " + arKey);
                break;
            case MsgField.IMSG_CLOUDAR_RECG_RESULT:
                arKey = (String) msg;
                // 根据云端识图结果 切换case
                mPromptCallback.onChangeCase(arKey);
                showToast(" 云端识图成功.切换CASE: " + arKey);
                break;
            default:
                break;
        }

    }

    @Override
    public void onLuaMessage(HashMap<String, Object> hashMap) {
        // TODO: 2018/5/10 接收lua信息到业务层
    }

    @Override
    public void onStateError(int i, String s) {
    }

    @Override
    public void onSetup(boolean b) {
    }

    @Override
    public void onCaseChange(boolean b) {
    }

    @Override
    public void onCaseCreated(ARResource arResource) {
    }

    @Override
    public void onPause(boolean b) {

    }

    @Override
    public void onResume(boolean b) {

    }

    @Override
    public void onReset(boolean b) {

    }

    @Override
    public void onRelease(boolean b) {

    }
    // callback end

    /**
     * ui 界面提示信息
     *
     * @param s
     */
    private void showToast(final String s) {
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDumixCallbackTips.setText(s);
            }
        });
    }

}
