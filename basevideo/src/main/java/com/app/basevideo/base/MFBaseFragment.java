package com.app.basevideo.base;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

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
import com.app.basevideo.util.MessageConfig;
import com.app.basevideo.util.WindowUtil;
import com.app.basevideo.widget.ProgressDialog;


public abstract class MFBaseFragment extends Fragment {
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


    /**
     * 重新计算View及子View的宽高、边距
     *
     * @param view
     */
    public void resizeView(View view) {
        WindowUtil.resizeRecursively(view);
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
            MFBaseFragment.this.onDestroy();
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
     * 发送消息，不支持的消息类型无法发送，调试模式打印error日志
     *
     * @param message
     */
    public void dispatchMessage(CommonMessage message) {
        if (message == null) {
            return;
        }
        MessageManager.getInstance().dispatchResponsedMessage(message);
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        MessageManager.getInstance().unRegisterListener(mUniqueId);
        MessageManager.getInstance().unRegisterListener(mNetErrorMessageListener);
        MessageManager.getInstance().unRegisterListener(processNetErrorCodeListener);
        MessageManager.getInstance().unRegisterListener(appExitListener);
        MessageManager.getInstance().unRegisterListener(mNetResponsedListener);
        MessageManager.getInstance().removeMessage(mUniqueId);
        super.onDestroy();
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
        }
    };

}
