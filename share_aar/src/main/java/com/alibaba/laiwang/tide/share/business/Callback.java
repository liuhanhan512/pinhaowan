package com.alibaba.laiwang.tide.share.business;

//**

import java.util.Map;

/** SDK的回调处理接口
*
* @author cnoss
* @param <T>
*/
public interface Callback<T> {

    /**
     * 业务调用之前的处理（如：显示提醒、参数验证）
     * @param
     */
    public boolean onPreExecute(Map<String, Object> parameters, Map<String, String> headers);

    /**
     * 业务调用之后的处理
     */
    public void onPostExecute();

    /**
     * 服务调用中回调处理（如：显示进度提醒等）
     */
    public void onProgressUpdate(Integer... values);

    /**
     * 处理业务的正常返回
     *
     * @param result
     */
    public void onSuccess(T result);

    /**
     * 处理业务调用异常
     *
     */
    public void onServiceException();

    /**
     * 处理网络异常
     *
     */
    public void onNetworkException();


    /**
     * 描述一个Void的返回类型
     *
     * @author cnoss
     */
    public final static class Void {

        volatile static Void instance = new Void();

        private Void() {
        }

        public static Void instance() {
            return instance;
        }
    }

}
