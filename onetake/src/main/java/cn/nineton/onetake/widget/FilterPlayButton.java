package cn.nineton.onetake.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import cn.nineton.onetake.R;
import cn.nineton.onetake.listener.FilterPlayButtonCallback;

public class FilterPlayButton extends FrameLayout implements OnClickListener {
    private boolean isPlaying;
    private ImageView left_play_iv;
    private FilterPlayButtonCallback playCallback;
    private ImageView play_iv;
    private ImageView right_play_iv;

    public FilterPlayButton(@NonNull Context context) {
        this(context, null);
    }

    public FilterPlayButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterPlayButton(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.isPlaying = false;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = View.inflate(context, R.layout.include_filter_view_playbutton, this);
        this.left_play_iv = view.findViewById(R.id.left_play_iv);
        this.play_iv = view.findViewById(R.id.play_iv);
        this.right_play_iv = view.findViewById(R.id.right_play_iv);
        setViewClickListener();
    }

    private void setViewClickListener() {
        this.left_play_iv.setOnClickListener(this);
        this.play_iv.setOnClickListener(this);
        this.right_play_iv.setOnClickListener(this);
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    private boolean playOrPauseVideo() {
        this.isPlaying = !this.isPlaying;
        changePlayIconByPlayState(this.isPlaying);
        return this.isPlaying;
    }

    private void changePlayIconByPlayState(boolean isPlaying) {
        if (isPlaying) {
            this.play_iv.setImageResource(R.drawable.icon_18_editor_player_play);
        } else {
            this.play_iv.setImageResource(R.drawable.icon_18_editor_player_pause);
        }
    }

    public void setPlayButtonCallback(FilterPlayButtonCallback callback) {
        this.playCallback = callback;
    }

    public void changePlayState(boolean shouldPlay) {
        this.isPlaying = shouldPlay;
        changePlayIconByPlayState(shouldPlay);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_iv:
                if (this.playCallback != null) {
                    this.playCallback.playClick(playOrPauseVideo());
                    return;
                }
                return;
            case R.id.left_play_iv:
                if (this.playCallback != null) {
                    this.playCallback.leftClick();
                    return;
                }
                return;
            case R.id.right_play_iv:
                if (this.playCallback != null) {
                    this.playCallback.rightClick();
                    return;
                }
                return;
            default:
                return;
        }
    }
}