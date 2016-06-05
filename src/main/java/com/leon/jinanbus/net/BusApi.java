package com.leon.jinanbus.net;

import com.leon.jinanbus.domain.BusInfo;
import com.leon.jinanbus.domain.BusLine;
import com.leon.jinanbus.domain.BusLineNotice;
import com.leon.jinanbus.domain.LineList;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 公交API和Bmob RestAPI封装
 */
public class BusApi {
    // 私有接口地址，隐藏
    private static final String BASE_URL = "http://0.0.0.0";
    private static final String APPLICATION_ID = "xxxx";
    private static final String REST_KEY = "xxxx";

    private static BusApi busApi;
    private final Retrofit mRetrofit;
    private final BusService mBusService;

    private BusApi() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mBusService = mRetrofit.create(BusService.class);

    }

    public static BusApi getInstance() {
        if (busApi == null) {
            synchronized (BusApi.class) {
                if (busApi == null) {
                    busApi = new BusApi();
                }
            }
        }

        return busApi;
    }

    private Func1<BusLine, BusLine> stateCheck = new Func1<BusLine, BusLine>() {
        @Override
        public BusLine call(BusLine busLine) {
            if (busLine.status.code != 0) {
                throw new IllegalStateException();
            }

            return busLine;
        }
    };

    public void getBusLineNotice(int skip, int limit, Subscriber<BusLineNotice> subscriber) {
        mBusService.getBusLineNotice(APPLICATION_ID, REST_KEY, "-updatedAt", skip, limit)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void queryBusNum(String busNum, Subscriber<LineList> subscriber) {
        mBusService.queryBusNum(busNum)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<LineList, LineList>() {
                    @Override
                    public LineList call(LineList lineList) {
                        if (lineList.status.code != 0) {
                            throw new IllegalStateException();
                        }

                        return lineList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getBusLine(String id, Subscriber<BusLine> subscriber) {
        mBusService.getBusLine(id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(stateCheck)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getBusInfo(String id, Action1<BusInfo> action) {
        mBusService.getBusInfo(id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action);
    }

    public void switchOrientation(String id, Subscriber<BusLine> subscriber) {
        mBusService.switchOrientation(id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(stateCheck)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}