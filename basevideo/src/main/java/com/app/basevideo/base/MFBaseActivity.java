package com.app.basevideo.base;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.basevideo.R;
import com.app.basevideo.config.IntentConfig;
import com.app.basevideo.config.VedioCmd;
import com.app.basevideo.config.VideoConstant;
import com.app.basevideo.config.VideoErrorData;
import com.app.basevideo.framework.UniqueId;
import com.app.basevideo.framework.listener.AbsMessageListener;
import com.app.basevideo.framework.listener.MessageListener;
import com.app.basevideo.framework.manager.MessageManager;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.framework.message.TaskMessage;
import com.app.basevideo.net.BaseHttpResult;
import com.app.basevideo.net.INetFinish;
import com.app.basevideo.net.callback.MFResponseFilterImpl;
import com.app.basevideo.util.InsuranceVersionHelper;
import com.app.basevideo.util.MessageConfig;
import com.app.basevideo.util.StringHelper;
import com.app.basevideo.util.SystemBarTintManager;
import com.app.basevideo.util.ViewServer;
import com.app.basevideo.util.WindowUtil;
import com.app.basevideo.widget.AlertDialog;
import com.app.basevideo.widget.InsuranceTitleBar;
import com.app.basevideo.widget.ProgressDialog;


public abstract class MFBaseActivity extends FragmentActivity {

    /**
     * 页面唯一标识ID，在{@link MFBaseActivity#onCreate(Bundle)}中初始化
     * 注册监听时带上mUniqueId
     * 页面销毁时，在该页面注册的监听自动注销 {@link MFBaseActivity#onDestroy()}
     * <p/>
     * 所有的Model也将与其所在页面的mUniqueId绑定，构造Model时传入的activityContext带有页面的mUniqueId。详见{@link MFBaseModel#MFBaseModel(MFBaseActivity)}
     */
    private UniqueId mUniqueId;
    protected boolean mAutoResizeView = true;
    private INetFinish mNetFinishListener;
    private boolean mIsDisPatchTouchEvent = true;
    private ProgressDialog mProgressDialog;
    private boolean mCanUseAppConfig = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCanUseAppConfig = MFBaseApplication.getInstance().canUseAppConfig();
        registerListener(processNetErrorCodeListener);
        registerListener(appExitListener);
        mUniqueId = UniqueId.gen();
        if (this instanceof INetFinish) {
            mNetFinishListener = (INetFinish) this;
            registerNetResponsedListener();
        }
        registerListener(mNetErrorMessageListener);
    }


    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        this.setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        if (mAutoResizeView) {
            resizeView(view);
        }
        if (isUseImmersiveStatusBar()) {
            initImmersiveStatusBar(getTitleBarColor(view));
            adjustStatusBarHeight(view);
        }
        if (MFBaseApplication.getInstance().isDebugMode()) {
            ViewServer.get(this).addWindow(this);
        }
        super.setContentView(view);
    }

    /**
     * 初始化沉浸式状态栏
     */

    private void initImmersiveStatusBar(int color) {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);
        if (InsuranceVersionHelper.hasThanVersion19() && !InsuranceVersionHelper.hasThanVersion21()) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else if (InsuranceVersionHelper.hasThanVersion21()) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    /**
     * 获取TitleBar背景色,用于填充状态栏
     *
     * @param view
     * @return
     */
    private int getTitleBarColor(View view) {
        int DEFAULT_STATUS_BAR_BG_COLOR = getResources().getColor(R.color.finance_191d20);
        if (view == null || !(view instanceof ViewGroup)) {
            return DEFAULT_STATUS_BAR_BG_COLOR;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof InsuranceTitleBar) {
                InsuranceTitleBar InsuranceTitleBar = (InsuranceTitleBar) viewGroup.getChildAt(i);
                if (InsuranceTitleBar == null || InsuranceTitleBar.getBackground() == null) {
                    return DEFAULT_STATUS_BAR_BG_COLOR;
                }
                return ((ColorDrawable) InsuranceTitleBar.getBackground()).getColor();
            }
        }
        return DEFAULT_STATUS_BAR_BG_COLOR;
    }

    /**
     * 适配沉浸式状态栏高度
     *
     * @param view
     */
    private void adjustStatusBarHeight(View view) {
        if (view == null || !(view instanceof ViewGroup)) {
            return;
        }

        ((ViewGroup) view).setClipToPadding(true);
        view.setFitsSystemWindows(true);

    }

    /**
     * 重新计算View及子View的宽高、边距
     *
     * @param view
     */
    public void resizeView(View view) {
        WindowUtil.resizeRecursively(view);
    }


    /**
     * 是否开启沉浸式状态栏沉,默认为开启，若需关闭，请在相应的Activity中重写isUseImmersiveStatusBar()方法，将返回值改为return false即可
     */
    public boolean isUseImmersiveStatusBar() {
        return true;
    }


    /**
     * 统一处理网络请求错误码(与服务端事先约定),错误码在{@link MFResponseFilterImpl}中拦截分发
     * 对于不需要做处理的错误码需在{@link MFResponseFilterImpl#doFilter(BaseHttpResult)}中添加相应的拦截case
     */

    public void processNetErrorCode(BaseHttpResult result) {
//        Toast.makeText(MFBaseApplication.getInstance(), StringHelper.isEmpty(result.errorMsg) ? getString(R.string.finance_net_error_common_tip) : result.errorMsg, Toast.LENGTH_LONG).show();
    }


    /**
     * 网络请求错误码listener,用于接收
     * {@link MFResponseFilterImpl}
     * 分发出来的错误码
     */
    private MessageListener processNetErrorCodeListener = new MessageListener(MessageConfig.CMD_NET_ERROR_CODE_PROCESS) {
        @Override
        public void onMessage(CommonMessage<?> commonMessage) {
            if (commonMessage == null) {
                return;
            }
            processNetErrorCode((BaseHttpResult) commonMessage.getData());
            MessageManager.getInstance().abortResponsedMessage(commonMessage);
        }
    };

    /**
     * 注册网络请求回调监听,用于回调网络请求数据到请求对应的Activity
     * {@link  MFBaseModel#disPatchRequestSuccessMessage(Integer)}
     */
    private MessageListener mNetResponsedListener = null;

    private void registerNetResponsedListener() {
        mNetResponsedListener = new MessageListener(mUniqueId.getId()) {
            @Override
            public void onMessage(CommonMessage<?> responsedMessage) {
                if (mNetFinishListener == null) {
                    return;
                }
                mNetFinishListener.onHttpResponse(responsedMessage);
                dismissProgressDialog();
            }
        };
        registerListener(mNetResponsedListener);
    }


    /**
     * App全局退出listener,所有继承自BaseActivity的子Activity收到退出命令时依次回收资源并销毁，防止内存泄漏
     * 若有Activity没继承BaseActivity请在自己的Activity中注册该监听，注意使用命令号 CMD_APP_EXIT
     */
    private MessageListener appExitListener = new MessageListener(MessageConfig.CMD_APP_EXIT) {
        @Override
        public void onMessage(CommonMessage<?> responsedMessage) {
            MFBaseActivity.this.onDestroy();
            MFBaseActivity.this.finish();
        }
    };


    /**
     * 发送消息，不支持的消息类型无法发送，调试模式打印error日志
     *
     * @param taskMessage
     */
    public void sendMessage(TaskMessage<?> taskMessage) {
        if (taskMessage == null) {
            return;
        }
        if (taskMessage.getTag() == null) {
            taskMessage.setTag(mUniqueId);
        }
        MessageManager.getInstance().sendMessage(taskMessage);
    }


    /**
     * 注册responsedMessage的事件监听
     * <p/>
     * <p>
     * <b><font color=red>禁止在事件回调里面调用此方法，否则抛异常</font><br>
     * <b><font color=red>只允许主线程调用</font>
     * </p>
     * <p>
     * 注册的消息监听如果没有全局性需求，需要使用者调用{@link MessageManager#unRegisterListener}来注销监听<br>
     * 场景：比如一个activity注册了一个自己发起的http任务返回的消息监听，页面销毁时需要注销此消息监听，否则会<b><font
     * color=red>引起内存泄露</font>
     * </p>
     *
     * @param listener 监听器
     * @see #registerListener(MessageListener)
     * @see MessageManager#unRegisterListener(AbsMessageListener)
     */
    public void registerListener(MessageListener listener) {
        if (listener != null && listener.getTag() == null) {
            listener.setTag(mUniqueId);
        }
        MessageManager.getInstance().registerListener(listener);
    }

    /**
     * 注册事件监听，带有命令号和监听器注册监听重载
     * <p/>
     * <p>
     * <b><font color=red>禁止在事件回调里面调用此方法，否则抛异常</font><br>
     * <b><font color=red>只允许主线程调用</font>
     * </p>
     *
     * @param cmd      不能是零
     * @param listener listener中的域cmd必须是0
     * @see #registerListener(MessageListener)
     * @see MessageManager#unRegisterListener(AbsMessageListener)
     */
    public void registerListener(int cmd, MessageListener listener) {
        if (listener != null && listener.getTag() == null) {
            listener.setTag(mUniqueId);
        }
        MessageManager.getInstance().registerListener(cmd, listener);
    }

    public UniqueId getUniqueId() {
        return mUniqueId;
    }

    @Override
    protected void onResume() {
        if (MFBaseApplication.getInstance().isDebugMode()) {
            ViewServer.get(this).setFocusedWindow(this);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        MessageManager.getInstance().unRegisterListener(mUniqueId);
        MessageManager.getInstance().unRegisterListener(mNetErrorMessageListener);
        MessageManager.getInstance().unRegisterListener(processNetErrorCodeListener);
        MessageManager.getInstance().unRegisterListener(appExitListener);
        MessageManager.getInstance().unRegisterListener(mNetResponsedListener);
        MessageManager.getInstance().removeMessage(mUniqueId);
        if (MFBaseApplication.getInstance().isDebugMode()) {
            ViewServer.get(this).removeWindow(this);
        }
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mIsDisPatchTouchEvent) {
            return super.dispatchTouchEvent(ev);
        }
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mIsDisPatchTouchEvent) {
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    /**
     * 是否分发触摸事件
     *
     * @return
     */
    public boolean isDisPatchTouchEvent() {
        return mIsDisPatchTouchEvent;
    }

    /**
     * 设置当前Activity是否分发触摸事件
     * 使用场景举例：
     * 当popupwindow弹起时希望点击外部不消失，并别外部组件不响应触摸事件时可以设置不分发触摸事件，但在popupwindow消失时一定要还原，否则Activity将无法再分发触摸事件
     *
     * @param isDisPatchTouchEvent
     */
    public void setIsDisPatchTouchEvent(boolean isDisPatchTouchEvent) {
        this.mIsDisPatchTouchEvent = isDisPatchTouchEvent;
    }


    /**
     * 弹出进度对话框
     *
     * @param title
     */
    public void showProgressDialog(String title) {
        showProgressDialog(title, true, true);
    }

    /**
     * 弹出进度对话框
     *
     * @param title
     * @param outsideTouchable
     */
    public void showProgressDialog(String title, boolean outsideTouchable) {
        showProgressDialog(title, outsideTouchable, true);
    }

    /**
     * 弹出进度对话框
     *
     * @param title
     * @param outsideTouchable
     * @param cancelable
     */
    public void showProgressDialog(String title, boolean outsideTouchable, boolean cancelable) {
        mProgressDialog = new ProgressDialog(this, R.layout.finance_progress_dialog, R.style.finance_progress_dialog);
        mProgressDialog.setCanceledOnTouchOutside(outsideTouchable);
        mProgressDialog.setCancelable(cancelable);
        mProgressDialog.setTitle(StringHelper.isEmpty(title) ? getString(R.string.finance_dialog_default_title) : title);
        mProgressDialog.show();
    }

    /**
     * 销毁进度对话框
     *
     * @return
     */
    public boolean dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
            return true;
        }
        return false;
    }

    private AlertDialog mAlertDialog;

    /**
     * 弹出提示对话框
     *
     * @param context
     * @param title
     * @param content
     * @param onClickListener
     */
    public void showAlertDialog(Context context, String title, String content, View.OnClickListener onClickListener) {
        mAlertDialog = new AlertDialog(context, title, content, onClickListener);
        if (!mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
    }

    /**
     * 销毁提示对话框
     */
    public void disMissAlertDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    /**
     * 网络错误监听,所有的业务请求错误会在此进行提示并销毁进度对话框
     * Token失效时自动跳转到登录页
     */
    private MessageListener mNetErrorMessageListener = new MessageListener(MessageConfig.CMD_NET_ERROR) {
        @Override
        public void onMessage(CommonMessage<?> responsedMessage) {
            if (responsedMessage == null && !(responsedMessage.getData() instanceof VideoErrorData)) {
                return;
            }
            VideoErrorData errorData = (VideoErrorData) responsedMessage.getData();
            if (dismissProgressDialog()) {
                MessageManager.getInstance().abortResponsedMessage(responsedMessage);
                Toast.makeText(MFBaseActivity.this, StringHelper.isEmpty(errorData.getErrorMessage()) ? getString(R.string.finance_net_error_common_tip) : errorData.getErrorMessage(), Toast.LENGTH_SHORT).show();
                if (errorData.getErrorCode() == VideoConstant.NET_ERROR_TOKEN_INVAID || errorData.getErrorCode() == VideoConstant.NET_ERROR_TOKEN_INVAID_LOGIN) {
                    IntentConfig loginConfig = new IntentConfig();
                    loginConfig.mActivity = MFBaseActivity.this;
                    loginConfig.mTag = true;
                    sendMessage(new TaskMessage<Object>(VedioCmd.CMD_GOTO_LOGIN_ACTIVITY, loginConfig));
                }
            }
        }
    };

    private boolean mHasPointerDown = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mCanUseAppConfig) {
            return super.onTouchEvent(event);
        }

        final int POINTER_DOWN_LAST_TIME = 2500;//三个手指按下持续时间,当持续时间大于三秒时触发事件，跳转至App Config设置页面
        switch (event.getAction()) {
            case MotionEvent.ACTION_POINTER_3_DOWN:
                mHasPointerDown = true;
                break;
            case MotionEvent.ACTION_POINTER_3_UP:
            case MotionEvent.ACTION_POINTER_2_UP:
            case MotionEvent.ACTION_POINTER_1_UP:
                if (event.getEventTime() - event.getDownTime() > POINTER_DOWN_LAST_TIME && mHasPointerDown) {
                    mHasPointerDown = false;
                    IntentConfig appConfigIntentConfig = new IntentConfig();
                    appConfigIntentConfig.mActivity = MFBaseActivity.this;
                    MessageManager.getInstance().sendMessage(new TaskMessage<IntentConfig>(MessageConfig.CMD_GOTO_APP_CONFIG_ACTIVITY, appConfigIntentConfig));
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 是否使用缓存数据,需要
     *
     * @return
     */
    public boolean isUseCacheData() {
        return false;
    }
}
