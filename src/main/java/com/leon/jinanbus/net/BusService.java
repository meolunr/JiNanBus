package com.leon.jinanbus.net;

import com.leon.jinanbus.domain.BusInfo;
import com.leon.jinanbus.domain.BusLine;
import com.leon.jinanbus.domain.BusLineNotice;
import com.leon.jinanbus.domain.LineList;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

interface BusService {
    // Bmob私有接口地址，隐藏
    @GET("xx/BusLineNotice")
    Observable<BusLineNotice> getBusLineNotice(@Header("X-Bmob-Application-Id") String appId,
                                               @Header("X-Bmob-REST-API-Key") String restKey,
                                               @Query("order") String order,
                                               @Query("skip") int skip,
                                               @Query("limit") int limit);

    // 公交私有接口地址，隐藏
    @GET("xx/{busNum}")
    Observable<LineList> queryBusNum(@Path("busNum") String busNum);

    // 公交私有接口地址，隐藏
    @GET("xx/{id}")
    Observable<BusLine> getBusLine(@Path("id") String id);

    // 公交私有接口地址，隐藏
    @GET("xx/{id}")
    Observable<BusInfo> getBusInfo(@Path("id") String id);

    // 公交私有接口地址，隐藏
    @GET("xx/{id}")
    Observable<BusLine> switchOrientation(@Path("id") String id);
}