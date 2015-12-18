package com.hwand.pinhaowanr.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BitmapLruCache;
import com.android.volley.toolbox.GsonRequest;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.MultipartRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hwand.pinhaowanr.DataCacheHelper;
import com.hwand.pinhaowanr.MainApplication;
import com.hwand.pinhaowanr.event.LogoutEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.greenrobot.event.EventBus;

/**
 * Created by dxz on 15-11-18.
 */
public class NetworkRequest {
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    public static final String SESSION_COOKIE = "JSESSIONID";

    /**
     * prevent make many instances
     */
    private NetworkRequest() {
    }

    /**
     * must call this to init at application
     *
     * @param context
     */
    public static void init(Context context) {
        int memoryClass = ((ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        if (mImageLoader == null) {
            if (memoryClass <= 16) {
                mImageLoader = new ImageLoader(mRequestQueue,
                        new BitmapLruCache(1024 * 1024 * memoryClass / 32));
            } else {
                mImageLoader = new ImageLoader(mRequestQueue,
                        new BitmapLruCache(1024 * 1024 * memoryClass / 18));
            }
        }
    }

    public static RequestQueue getRequestQueue() {
        if (null == mRequestQueue) {
            mRequestQueue = Volley.newRequestQueue(MainApplication.getInstance());
        }

        return mRequestQueue;
    }

    /**
     * Checks the response headers for session cookie and saves it
     * if it finds it.
     *
     * @param headers Response Headers.
     */
    public final static void checkSessionCookie(Map<String, String> headers) {
        for (String str : headers.keySet()) {
            LogUtil.d("dxz", str + ":" + headers.get(str));
        }
        if (headers.containsKey(SET_COOKIE_KEY)
                && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
            String cookie = headers.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                cookie = splitCookie[0];
                String sessionId = AndTools.getCurrentData(MainApplication.getInstance(), SESSION_COOKIE);
                if (TextUtils.isEmpty(sessionId)) {
                    AndTools.saveCurrentData2Cache(MainApplication.getInstance(), SESSION_COOKIE, cookie);
                } else {
                    if (!TextUtils.equals(sessionId, cookie)) {
                        AndTools.saveCurrentData2Cache(MainApplication.getInstance(), DataCacheHelper.KEY_USER_INFO, "");
                        DataCacheHelper.getInstance().setUserInfo(null);
                        EventBus.getDefault().post(new LogoutEvent());
                        AndTools.saveCurrentData2Cache(MainApplication.getInstance(), NetworkRequest.SESSION_COOKIE, cookie);
                    }
                }
            }
        }
    }

    /**
     * Adds session cookie to headers if exists.
     *
     * @param headers
     */
    public final static void addSessionCookie(Map<String, String> headers) {

        String sessionId = AndTools.getCurrentData(MainApplication.getInstance(), SESSION_COOKIE);
        if (sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(sessionId);
            if (headers.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(COOKIE_KEY));
            }
            headers.put(COOKIE_KEY, builder.toString());
        }
    }


    public static ImageLoader getImageLoader() {
        if (null == mImageLoader) {
            int memoryClass = ((ActivityManager) MainApplication.getInstance()
                    .getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();

            if (memoryClass <= 16) {
                mImageLoader = new ImageLoader(getRequestQueue(),
                        new BitmapLruCache(1024 * 1024 * memoryClass / 32));
            } else {
                mImageLoader = new ImageLoader(getRequestQueue(),
                        new BitmapLruCache(1024 * 1024 * memoryClass / 18));
            }
        }

        return mImageLoader;
    }

    public static Request<String> get(String url,
                                      Response.Listener<String> listener,
                                      Response.ErrorListener errorListener) {
        RequestQueue queue = getRequestQueue();
        return queue.add(new StringRequest(url, listener, errorListener));
    }

    public static <T> Request<T> get(String url, Class<T> clazz,
                                     Response.Listener<T> listener, Response.ErrorListener errorListener) {
        return get(url, clazz, listener, errorListener, false);
    }

    public static <T> Request<T> get(boolean shouldCache, String url,
                                     Class<T> clazz, Response.Listener<T> listener,
                                     Response.ErrorListener errorListener) {
        RequestQueue queue = getRequestQueue();
        GsonRequest<T> request = new GsonRequest<T>(url, clazz, listener,
                errorListener, true);
        request.setShouldCache(shouldCache);
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 5, 2.f));
        queue.add(request);
        return request;
    }

    public static <T> Request<T> get(String url, Class<T> clazz,
                                     Response.Listener<T> listener,
                                     Response.ErrorListener errorListener, boolean needUrlDecode) {

        RequestQueue queue = getRequestQueue();
        GsonRequest<T> request = new GsonRequest<T>(url, clazz, listener,
                errorListener, needUrlDecode);
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 5, 2.f));
        return queue.add(request);
    }

    public static String getSync(String url) {
        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest request = new StringRequest(url, future, future);
        RequestQueue queue = getRequestQueue();
        queue.add(request);

        String response;
        try {
            response = future.get();
        } catch (InterruptedException e) {
            response = "";
        } catch (ExecutionException e) {
            response = "";
        }

        return response;
    }

    public static void upload(String url, String filePath, final Map<String, String> params,
                              Response.Listener<String> listener,
                              Response.ErrorListener errorListener) {
        RequestQueue queue = getRequestQueue();
        File file = new File(filePath);
        String fileName = file.getName();
        MultipartRequest request = new MultipartRequest(url,
                errorListener, listener, fileName, file, params);
        queue.add(request);
    }

    public static void post(String url, final Map<String, String> params,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        RequestQueue queue = getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.POST, url,
                listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        queue.add(request);
    }

    public static Request<String> post(String url, final byte[] buffer,
                                       Response.Listener<String> listener,
                                       Response.ErrorListener errorListener) {
        RequestQueue queue = getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.POST, url,
                listener, errorListener) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return buffer;
            }
        };
        return queue.add(request);
    }

    public static <T> Request<T> post(String url,
                                      final Map<String, String> params, Class<T> clazz,
                                      Response.Listener<T> listener, Response.ErrorListener errorListener) {
        RequestQueue queue = getRequestQueue();
        GsonRequest<T> request = new GsonRequest<T>(url, clazz, listener,
                errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        return queue.add(request);
    }

    public static String getCache(String url) {
        RequestQueue queue = getRequestQueue();
        Cache.Entry cache = queue.getCache().get(url);

        if (null != cache) {
            return new String(cache.data);
        }

        return null;
    }

    public static <T> T getCache(String url, Class<T> clazz) {
        RequestQueue queue = getRequestQueue();
        Cache.Entry cache = queue.getCache().get(url);

        if (null != cache) {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(new String(cache.data), clazz);
        }

        return null;
    }

    public static Request<String> get(String url,
                                      Response.Listener<String> listener,
                                      Response.ErrorListener errorListener,
                                      final String shost) {
        RequestQueue queue = getRequestQueue();
        StringRequest request = new StringRequest(url, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (shost.length() != 0) {
                    HashMap<String, String> res = new HashMap<String, String>();
                    res.put("Host", shost);
                    return res;
                } else {
                    return super.getHeaders();
                }
            }
        };
        return queue.add(request);
    }

    public static void post(String url, final Map<String, String> params,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener,
                            final String posthost) {
        RequestQueue queue = getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.POST, url,
                listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (posthost.length() != 0) {
                    HashMap<String, String> res = new HashMap<String, String>();
                    res.put("Host", posthost);
                    return res;
                } else {
                    return super.getHeaders();
                }
            }
        };

        queue.add(request);
    }
}
