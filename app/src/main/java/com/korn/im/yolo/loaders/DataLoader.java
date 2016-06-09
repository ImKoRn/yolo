package com.korn.im.yolo.loaders;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.korn.im.yolo.managers.DataManager;
import com.korn.im.yolo.objects.Post;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataLoader extends HandlerThread {
    private static final String name = "DataLoader";

    public static final int REQUEST_LOAD_NEW_DATA = 1;
    public static final int REQUEST_RESTORE_DATA = 2;


    private static final int RESPONSE_NEW_DATA_LOADED = -1;
    private static final int RESPONSE_RESTORE_DATA = -2;
    private static final int RESPONSE_CLIENT_ERROR = -3;
    private static final int RESPONSE_SERVER_ERROR = -4;
    private static final int RESPONSE_UNKNOWN_REQUEST = -5;

    private static final String SITE_URL = "http://yolo.kiev.ua/wp-json/wp/v2/posts";

    private static Handler loadingHandler;
    private static Handler dataHandler;

    private final Map<Integer, ResultListener> requestsMap;
    private final DataManager dataManager;
    private Context context;
    private boolean isInitialized = false;

    private static DataLoader instance;

    private DataLoader() {
        super(name);
        requestsMap = Collections.synchronizedMap(new HashMap<Integer, ResultListener>());
        dataManager = new DataManager();
        start();
    }

    public static DataLoader getInstance() {
        if(instance == null)
            instance = new DataLoader();

        return instance;
    }

    public void init(Context context) {
        this.context = context;
        isInitialized = true;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        if(loadingHandler == null)
            loadingHandler = new LoadingHandler(getLooper()) ;

        if(dataHandler == null)
            dataHandler = new DataHandler(Looper.getMainLooper());
    }

    public void sendRequest(int request, int categoryId, ResultListener<? extends Post> resultListener) {
        sendRequest(request, categoryId, 0, resultListener);
    }

    public void sendRequest(int request, int categoryId, int offset, ResultListener<? extends Post> resultListener) {
        if (!isInitialized) throw new IllegalStateException("DataLoader not initialized, call init()");

        if (loadingHandler != null) {
            requestsMap.put(categoryId, resultListener);

            loadingHandler.removeMessages(request, null);
            loadingHandler.obtainMessage(request, categoryId, offset).sendToTarget();
        }
    }

    private class DataHandler extends Handler {
        public DataHandler(Looper mainLooper) {
            super(mainLooper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataLoader.RESPONSE_NEW_DATA_LOADED: {
                    ResultListener resultListener;
                    if((resultListener = requestsMap.get(msg.arg1)) != null) {
                        List<Post> list = dataManager.getDataByCategory(msg.arg1);
                        if(msg.arg2 != 0) list = list.subList(msg.arg2, list.size());

                        if(list != null) resultListener.onResult(list);
                        else resultListener.onResult(Collections.emptyList());
                    }
                    requestsMap.remove(msg.arg1);
                    break;
                }
                case DataLoader.RESPONSE_RESTORE_DATA: {
                    ResultListener resultListener;
                    if((resultListener = requestsMap.get(msg.arg1)) != null) {
                        List<Post> list = dataManager.getDataByCategory(msg.arg1);
                        if(msg.arg2 != 0) list = list.subList(msg.arg2, list.size());

                        if(list != null) resultListener.onResult(list);
                        else resultListener.onResult(Collections.emptyList());
                    }

                    requestsMap.remove(msg.arg1);
                    break;
                }
                case DataLoader.RESPONSE_SERVER_ERROR: {
                    ResultListener resultListener;
                    if((resultListener = requestsMap.get(msg.arg1)) != null) {
                        List<Post> list = dataManager.getDataByCategory(msg.arg1);
                        if(msg.arg2 != 0) list = list.subList(msg.arg2, list.size());

                        if(list != null) resultListener.onResult(list);
                        else resultListener.onResult(Collections.emptyList());
                    }

                    requestsMap.remove(msg.arg1);
                    break;
                }
                case DataLoader.RESPONSE_CLIENT_ERROR: {
                    ResultListener resultListener;
                    if((resultListener = requestsMap.get(msg.arg1)) != null) {
                        List<Post> list = dataManager.getDataByCategory(msg.arg1);
                        if(msg.arg2 != 0) list = list.subList(msg.arg2, list.size());

                        if(list != null) resultListener.onResult(list);
                        else resultListener.onResult(Collections.emptyList());
                    }
                    requestsMap.remove(msg.arg1);
                    break;
                }
                case DataLoader.RESPONSE_UNKNOWN_REQUEST: {
                    break;
                }
            }
        }
    }

    private class LoadingHandler extends Handler {
        public LoadingHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            StringBuilder result = new StringBuilder();

            Message message = dataHandler.obtainMessage(RESPONSE_UNKNOWN_REQUEST, msg.arg1, msg.arg2);

            switch (msg.what) {
                case REQUEST_LOAD_NEW_DATA: {
                    HttpURLConnection httpURLConnection = null;

                    String request = resolveRequest(msg);

                    if (request != null) {
                        try {
                            httpURLConnection =
                                    (HttpURLConnection) new URL(request).openConnection();

                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                            String str;
                            while ((str = bufferedReader.readLine()) != null)
                                result.append(str);
                            if(result.length() > 0) {
                                parseResult(msg, result.toString());
                            }
                            else throw new IOException("Can't read data");

                            message.what = RESPONSE_NEW_DATA_LOADED;
                        } catch (IOException e) {
                            e.printStackTrace();
                            message.what = RESPONSE_SERVER_ERROR;
                        } finally {
                            if (httpURLConnection != null)
                                httpURLConnection.disconnect();
                        }

                    }
                    break;
                }
                case REQUEST_RESTORE_DATA : {
                    if(!dataManager.restore(msg.arg1, context))
                        message.what = RESPONSE_CLIENT_ERROR;
                    else message.what = RESPONSE_RESTORE_DATA;

                    break;
                }
                default: {
                    message.what = RESPONSE_UNKNOWN_REQUEST;
                }
            }

            message.sendToTarget();
            super.handleMessage(msg);
        }

        private void parseResult(Message msg, String string) {
            switch (msg.what) {
                case REQUEST_LOAD_NEW_DATA: {
                    dataManager.parseStringToObjects(msg.arg1, string, msg.arg2);
                    dataManager.save(msg.arg1, context);
                    break;
                }
            }
        }

        private String resolveRequest(Message msg) {
            switch (msg.what) {
                case REQUEST_LOAD_NEW_DATA: {
                    return SITE_URL + "?categories=" + msg.arg1 + "&offset=" + msg.arg2;
                }
            }
            return null;
        }
    }

    public interface ResultListener<T extends Post> {
        void onResult(List<T> list);
    }
}