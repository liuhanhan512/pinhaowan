package com.hwand.pinhaowanr.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BitmapLruCache;
import com.android.volley.toolbox.GsonRequest;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hwand.pinhaowanr.MainApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by dxz on 15-11-18.
 */
public class NetworkRequest {
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

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
        return get(url, clazz, listener, errorListener, true);
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
