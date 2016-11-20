package com.app.basevideo.framework.util;

import android.util.SparseArray;

import java.lang.reflect.Field;
import java.util.List;


/**
 * 根据cmd反查对应的可读的名字<br>
 * 查看cmd的类型请参考 {@link com.didi.basefinance.framework.common.FrameHelper#getMessageType(int)}
 * <p/>
 * <p>
 * 仅在调试模式下有效，内部保证非调试模式不执行这些逻辑
 * </p>
 * <p>
 * 开放者需要根据2种业务类型（http，自定义）定义不同的命令号配置文件，命令号配置文件定义举例： <br>
 * <p/>
 * <pre>
 * public class CmdConfigHttp {
 * 	public static final int CMD_ONLINE = 1001;
 * 	...
 * }
 * </pre>
 * <p/>
 * 开发者需要在应用初始化Application.onCreate()
 * 中调用CmdConvertManager.getInstance().init()，参数传入几个命令号配置文件的class路径。 如
 * <p/>
 * <pre>
 * List&lt;String&gt; paths = new ArrayList&lt;String&gt;();
 * paths.add(CmdConfigSocket.class.getName());
 * ...
 * CmdConvertHelper.getInstance().init(paths);
 * </pre>
 * <p/>
 * </p>
 * <p>
 * 消息框架内部根据命令号获取{@link com.app.basevideo.framework.task.MessageTask}，或者找消息监听者{@link com.app.basevideo.framework.listener.MessageListener}
 * ，没有找到的话，打印错误日志供开发者参考
 * </p>
 *
 * @see #init
 */

public class CmdConvertHelper {
    private volatile static CmdConvertHelper sInstance;
    private SparseArray<String> mCmdMap = null;

    public static CmdConvertHelper getInstance() {
        if (sInstance == null) {
            synchronized (CmdConvertHelper.class) {
                if (sInstance == null) {
                    sInstance = new CmdConvertHelper();
                }
            }
        }
        return sInstance;
    }

    private CmdConvertHelper() {
        mCmdMap = new SparseArray<String>();
    }

    /**
     * 初始化
     *
     * @param classPaths
     * @see #getNameByCmd(int)
     */
    public void init(List<String> classPaths) {
        //hasChange
//        if (!MFBaseApplication.getInstance().isDebugMode()) {
//            return;
//        }
        if (classPaths == null || classPaths.size() == 0) {
            return;
        }
        // 初始化完，大约需要20ms，小米2s测试
        for (String path : classPaths) {
            loadField(path);
        }
    }

    // 通过反射，得到key 和value
    private void loadField(String path) {
        // 根据给定的类路径，反射出cmd对应的名字
        ClassLoader classLoader = this.getClass().getClassLoader();
        try {
            Class<?> cmdConfig = classLoader.loadClass(path);
            Object cmdObject = cmdConfig.newInstance();

            Field[] fields = cmdConfig.getFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {

                    int cmdValue = field.getInt(cmdObject);// cmd的int值
                    String cmdName = field.getName();// cmd的name

                    if (mCmdMap.get(cmdValue) != null) {
                        throw new Error("cmd " + path + " " + cmdName + " 和 " + mCmdMap.get(cmdValue) + " 重复");
                    }
                    mCmdMap.put(cmdValue, cmdName);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据cmd返回对应的名字
     *
     * @param cmd
     * @return
     */
    public String getNameByCmd(int cmd) {
        String result = mCmdMap.get(cmd);
        if (result != null) {
            return result;
        }
        return null;
    }
}
