package com.shirlman.yiplayer.core;

import com.shirlman.yiplayer.models.ICibaResponse;
import com.shirlman.yiplayer.models.ShooterSubtitleDetailResponse;
import com.shirlman.yiplayer.models.ShooterSubtitleResponse;
import com.shirlman.yiplayer.models.YoudaoResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by KB-Server on 2016/7/24.
 */
public interface OnlineQueryService {
    @GET("http://fanyi.youdao.com/openapi.do")
    Call<YoudaoResponse> queryWordFromYoudao(@QueryMap Map<String, Object> filters);

    @GET("http://dict-co.iciba.com/api/dictionary.php")
    Call<ICibaResponse> queryWordFromICiba(@QueryMap Map<String, Object> filters);

    @GET("http://api.assrt.net/v1/sub/search")
    Call<ShooterSubtitleResponse> querySubtitleFromShooter(@QueryMap Map<String, Object> filters);

    @GET("http://api.assrt.net/v1/sub/detail")
    Call<ShooterSubtitleDetailResponse> getSubtitleDetailFromShooter(@QueryMap Map<String, Object> filters);
}
