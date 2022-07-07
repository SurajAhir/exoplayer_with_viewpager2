package com.example.exoplayerwithviewpager;

import static android.view.View.GONE;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.util.Map;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder> {
    MediaObject[] MEDIA_OBJECTS;
    Context context;

    public ViewPagerAdapter(MediaObject[] MEDIA_OBJECTS, Context context) {
        this.MEDIA_OBJECTS = MEDIA_OBJECTS;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_video_layout, parent, false);
        return new ViewPagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerViewHolder holder, int position) {
        holder.setVideoPath(MEDIA_OBJECTS[position].getMedia_url());
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewPagerViewHolder holder) {
        Log.d("TAGA", "attached" + holder.getAdapterPosition());
        if (!MyData.hashMap.isEmpty()) {
            ExoplayerItem item = MyData.hashMap.get(holder.getAdapterPosition());
            ExoPlayer exoPlayer = item.getExoPlayer();
            exoPlayer.setMediaSource(item.getMediaSource());
            exoPlayer.prepare();
            holder.videoView.setPlayer(item.getExoPlayer());
            exoPlayer.addListener(new Player.Listener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == Player.STATE_BUFFERING) {
                        holder.progressBar.setVisibility(View.VISIBLE);
                    } else if (playbackState == Player.STATE_READY) {
                        holder.progressBar.setVisibility(GONE);
                    } else if (playbackState == Player.STATE_ENDED) {
                        exoPlayer.seekTo(0);
                        exoPlayer.play();
                    }
                }

                @Override
                public void onPlayerErrorChanged(@Nullable PlaybackException error) {
                    Player.Listener.super.onPlayerErrorChanged(error);
                    Log.d("TAG", error.getLocalizedMessage());
                }

            });
            item.getExoPlayer().setPlayWhenReady(true);
            item.getExoPlayer().play();
        }
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewPagerViewHolder holder) {
        Log.d("TAGA", "detatched" + holder.getAdapterPosition());
        ExoplayerItem item = MyData.hashMap.get(holder.getAdapterPosition());
        item.getExoPlayer().setPlayWhenReady(false);
        for (Map.Entry<Integer,ExoplayerItem> entry:MyData.hashMap.entrySet()){
            if(entry.getValue().getExoPlayer().isPlaying()){
                entry.getValue().getExoPlayer().setPlayWhenReady(false);
                entry.getValue().getExoPlayer().pause();
            }
        }
        super.onViewDetachedFromWindow(holder);
    }

    /* for lowest version

      @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        Log.d("TAGA", "attached" + holder.getAdapterPosition());
        if (!MyData.hashMap.isEmpty()) {
            ExoplayerItem item = MyData.hashMap.put.get(holder.getAdapterPosition());
            assert item != null;
            ExoPlayer exoPlayer = item.getExoPlayer();
            exoPlayer.prepare(item.getMediaSource());
            holder.exoPlayerView.setPlayer(item.getExoPlayer());
            exoPlayer.addListener(new Player.EventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == Player.STATE_BUFFERING) {
                        holder.progressBar.setVisibility(View.VISIBLE);
                    } else if (playbackState == Player.STATE_READY) {
                        holder.progressBar.setVisibility(GONE);
                    }else if (playbackState==Player.STATE_ENDED ){
                        exoPlayer.seekTo(0);
                        exoPlayer.setPlayWhenReady(true);
                    }
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    Player.EventListener.super.onPlayerError(error);
                    Log.d("TAG","error"+ error.getLocalizedMessage());
                }
            });
            item.getExoPlayer().setPlayWhenReady(true);
        }
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolder holder) {
        Log.d("TAGA", "detatched" + holder.getAdapterPosition());
        ExoplayerItem item = MyData.hashMap.put.get(holder.getAdapterPosition());
        item.getExoPlayer().setPlayWhenReady(false);
        for (Map.Entry<Integer,ExoplayerItem> entry:MyData.hashMap.put.entrySet()){
            if(entry.getValue().getExoPlayer().isPlaying()){
                entry.getValue().getExoPlayer().setPlayWhenReady(false);
            }
        }
        super.onViewDetachedFromWindow(holder);
    }
     */

    @Override
    public int getItemCount() {
        return MEDIA_OBJECTS.length;
    }

    class ViewPagerViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        StyledPlayerView videoView;
        ExoPlayer exoPlayer= new ExoPlayer.Builder(context).build();
        MediaSource mediaSource;
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(context);

        /* this code for lowest version of exoplayer
         SimpleExoPlayerView myvideoView;
        MediaSource mediaSource;
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        ExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(con, trackSelector);
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
         */
        public ViewPagerViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar_viewpager);
            videoView = itemView.findViewById(R.id.video_view);
        }

        void setVideoPath(String url) {
            if (MyData.hashMap.isEmpty()) {
                mediaSource = new ProgressiveMediaSource.Factory(defaultDataSourceFactory).createMediaSource(MediaItem.fromUri(Uri.parse(url)));
                MyData.hashMap.put(getAdapterPosition(), new ExoplayerItem(exoPlayer, getAdapterPosition(), mediaSource));
            } else if (MyData.hashMap.get(getAdapterPosition()) == null) {
                mediaSource = new ProgressiveMediaSource.Factory(defaultDataSourceFactory).createMediaSource(MediaItem.fromUri(Uri.parse(url)));
                MyData.hashMap.put(getAdapterPosition(), new ExoplayerItem(exoPlayer, getAdapterPosition(), mediaSource));
            }


        }

        /* for lowest version
         void setVideoPath(String url) {
            if(MyData.INSTANCE.getHashMap().isEmpty()){
                try {
                    Uri videouri = Uri.parse(url);
                    mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);
                    Log.d("TAGA",getAdapterPosition()+""+url);
                    MyData.hashMap.put(getAdapterPosition(),new ExoplayerItem(exoPlayer,getAdapterPosition(),mediaSource));
                } catch (Exception e) {
                    Log.e("TAG", "Error : " + e.toString());
                }
            }else if (MyData.INSTANCE.getHashMap().get(getAdapterPosition())==null){
                try {
                    Uri videouri = Uri.parse(url);
                    mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);
                    Log.d("TAGA",getAdapterPosition()+""+url);
                    MyData.hashMap.put(getAdapterPosition(),new ExoplayerItem(exoPlayer,getAdapterPosition(),mediaSource));
                } catch (Exception e) {
                    Log.e("TAG", "Error : " + e.toString());
                }
            }

        }

         */
    }
}
