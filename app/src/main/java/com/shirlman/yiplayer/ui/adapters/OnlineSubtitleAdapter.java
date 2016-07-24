package com.shirlman.yiplayer.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shirlman.yiplayer.R;
import com.shirlman.yiplayer.core.OnlineQueryService;
import com.shirlman.yiplayer.models.ShooterSubtitleDetailResponse;
import com.shirlman.yiplayer.models.ShooterSubtitleResponse;
import com.shirlman.yiplayer.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by KB-Server on 2016/7/24.
 */
public class OnlineSubtitleAdapter extends RecyclerView.Adapter <OnlineSubtitleAdapter.ViewHolder>{
    private LayoutInflater mLayoutInflater;
    private List<ShooterSubtitleResponse.SubBean.SubsBean> mSubtitleList;

    private class OnDownloadClickListener implements View.OnClickListener {
        private String mSubtitleId;

        OnDownloadClickListener(String subtitleId) {
            mSubtitleId = subtitleId;
        }

        @Override
        public void onClick(View view) {
            String shooterBaseUrl = "http://api.assrt.net/v1/sub/detail/";
            String shooterToken = "5fjG5Znw0KgfqL1QmDffB3A7qzaGAXzF";

            Map<String, Object> searchFilters = new LinkedHashMap<>();
            searchFilters.put("token", shooterToken);
            searchFilters.put("id", mSubtitleId);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(shooterBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            OnlineQueryService service = retrofit.create(OnlineQueryService.class);
            service.getSubtitleDetailFromShooter(searchFilters).enqueue(new Callback<ShooterSubtitleDetailResponse>() {
                @Override
                public void onResponse(Call<ShooterSubtitleDetailResponse> call, Response<ShooterSubtitleDetailResponse> response) {
                    if(response.isSuccessful() && response.body() != null && response.body().getSub() != null
                            && response.body().getSub().getSubs() != null && response.body().getSub().getSubs().size() > 0) {
                        String downloadUrl = response.body().getSub().getSubs().get(0).getUrl();
                    }
                }

                @Override
                public void onFailure(Call<ShooterSubtitleDetailResponse> call, Throwable t) {
                    Log.e("OnDownloadClickListener", "querySubtitleFromShooter.onFailure: ", t);
                }
            });
        }
    }

    public OnlineSubtitleAdapter(Context context, List<ShooterSubtitleResponse.SubBean.SubsBean> subtitleList) {
        mLayoutInflater = LayoutInflater.from(context);
        mSubtitleList = subtitleList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = mLayoutInflater.inflate(R.layout.online_subtitle_item, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShooterSubtitleResponse.SubBean.SubsBean subtitle = mSubtitleList.get(position);

        String title = StringUtils.nullOrEmpty(subtitle.getNative_name())
                ? subtitle.getVideoname() : subtitle.getNative_name();
        String language = subtitle.getLang() == null ? "未知" : subtitle.getLang().getDesc();

        holder.title.setText(title);
        holder.voteScore.setText(subtitle.getVote_score());
        holder.language.setText(language);
        holder.download.setOnClickListener(new OnDownloadClickListener(subtitle.getId()));
    }

    @Override
    public int getItemCount() {
        return mSubtitleList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView language;
        public TextView voteScore;
        private Button download;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.online_subtitle_title);
            language = (TextView) itemView.findViewById(R.id.online_subtitle_language);
            voteScore = (TextView) itemView.findViewById(R.id.online_subtitle_vote_score);
            download = (Button) itemView.findViewById(R.id.online_subtitle_download);
        }
    }
}
