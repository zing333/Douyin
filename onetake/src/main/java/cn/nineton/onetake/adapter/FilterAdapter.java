package cn.nineton.onetake.adapter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.nineton.onetake.App;
import cn.nineton.onetake.FilterActivity;
import cn.nineton.onetake.R;
import cn.nineton.onetake.bean.FilterInfo;
import cn.nineton.onetake.event.CollectFilterEvent;
import cn.nineton.onetake.event.EnterCollectedModeEvent;
import cn.nineton.onetake.util.LocaleUtil;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.TextUtil;
import cn.nineton.onetake.util.TintColorUtil;
import cn.nineton.onetake.widget.AvenirNextCondensedRegularTextView;
import cn.nineton.onetake.widget.RoundAngleImageView;
import cn.nineton.onetake.widget.VerticalTextView;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.bean.FilterInfo;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.events.CollectFilterEvent;
//import com.blink.academy.onetake.support.events.EnterCollectedModeEvent;
//import com.blink.academy.onetake.support.utils.IntentUtil;
//import com.blink.academy.onetake.support.utils.LocaleUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.support.utils.TintColorUtil;
//import com.blink.academy.onetake.ui.activity.store.FilterStoreListActivity;
//import com.blink.academy.onetake.ui.activity.video.FilterActivity;
//import com.blink.academy.onetake.widgets.TextView.AvenirNextCondensedRegularTextView;
//import com.blink.academy.onetake.widgets.TextView.VerticalTextView;
//import com.blink.academy.onetake.widgets.imageview.RoundAngleImageView;
//import de.greenrobot.event.EventBus;
//import java.util.List;

public class FilterAdapter extends Adapter<ViewHolder> {
    public static int GROUP_223_POSITION = 0;
    public static int GROUP_BNW_POSITION = 0;
    public static int GROUP_BOOST_POSITION = 0;
    public static int GROUP_CINE_POSITION = 0;
    public static int GROUP_CLEAN_POSITION = 0;
    public static final String GROUP_COLLECT = "Favorite";
    public static int GROUP_COLLECT_POSITION = -1;
    public static int GROUP_FADED_POSITION = 0;
    public static int GROUP_FOOD_POSITION = 0;
    public static int GROUP_FRESH_POSITION = 0;
    public static int GROUP_GOLD_POSITION = 0;
    public static int GROUP_MOOD_POSITION = 0;
    public static int GROUP_PORTRAIT_POSITION = 0;
    public static int GROUP_SELFIE_POSITION = 0;
    public static int GROUP_SUBTLE_POSITION = 0;
    public static int GROUP_VIVID_POSITION = 0;
    private static final int MESSAGE_TO_COLLECT_MODE = 10001010;
    public static final int TYPE_DOWNING = 3;
    public static final int TYPE_FILTER = 2;
    public static final int TYPE_GROUP = 1;
    public static final int TYPE_SORT = 5;
    public final String GROUP_223 = "223";
    public final String GROUP_BNW = "BNW";
    public final String GROUP_BOOST = "Boost";
    public final String GROUP_CINE = "Cine";
    public final String GROUP_CLEAN = "Clean";
    public final String GROUP_FADED = "Faded";
    public final String GROUP_FOOD = "Food";
    public final String GROUP_FRESH = "Fresh";
    public final String GROUP_GOLD = "Gold";
    public final String GROUP_MOOD = "Mood";
    public final String GROUP_PORTRAIT = "Portrait";
    public final String GROUP_SELFIE = "Selfie";
    public final String GROUP_SUBTLE = "Subtle";
    public final String GROUP_VIVID = "Vivid";
    public boolean canEnterCollectedMode;
    public boolean canExeMessage = false;
    private Activity context;
    private int currentPosition = 1;
    private boolean filterIsChanging;
    private String filterName = FilterActivity.NONE_FILTER;
    private boolean hasClick;
    private List<FilterInfo> infos;
    public boolean isCollectedMode;
    private int itemPosition = -1;
    private FilterInfo mCurrentFilterInfo;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (FilterAdapter.this.canEnterCollectedMode) {
                FilterAdapter.this.isCollectedMode = true;
                FilterAdapter.this.notifyDataSetChanged();
                EventBus.getDefault().post(new EnterCollectedModeEvent());
            }
        }
    };
    private OnClickListener onClickListener;

    public interface OnClickListener {
        void onClick(View view, int i, String str, boolean z);

        void onClickForGroupNameClick(View view, int i, String str, boolean z);

        void onUndoReset(int i, String str);
    }

    private class DownloadingViewHolder extends ViewHolder {
        public DownloadingViewHolder(View itemView) {
            super(itemView);
        }

        public void onBindViewHolder(int position) {
        }
    }

    private class FilterViewHolder extends ViewHolder {
        RoundAngleImageView child_cancel_collect;
        RoundAngleImageView child_collect;
        RoundAngleImageView child_download;
        TextView child_label;
        RoundAngleImageView child_lock;
        ImageView child_none;
        ImageView child_preview;
        ImageView child_preview_stroke;

        public FilterViewHolder(View itemView) {
            super(itemView);
            this.child_preview = (ImageView) itemView.findViewById(R.id.child_preview);
            this.child_preview_stroke = (ImageView) itemView.findViewById(R.id.child_preview_stroke);
            this.child_label = (TextView) itemView.findViewById(R.id.child_label);
            this.child_lock = (RoundAngleImageView) itemView.findViewById(R.id.child_lock);
            this.child_collect = (RoundAngleImageView) itemView.findViewById(R.id.child_collect);
            this.child_cancel_collect = (RoundAngleImageView) itemView.findViewById(R.id.child_cancel_collect);
            this.child_download = (RoundAngleImageView) itemView.findViewById(R.id.child_download);
            this.child_none = (ImageView) itemView.findViewById(R.id.child_none);
        }

        public void onBindViewHolder(final int position) {
            boolean selected = true;
            final FilterInfo filterInfo = (FilterInfo) FilterAdapter.this.infos.get(position);
            this.itemView.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == 0) {
                        if (!(FilterAdapter.this.isCollectedMode || position == 0 || position == FilterAdapter.this.getNoneFilterIndex())) {
                            FilterAdapter.this.canEnterCollectedMode = true;
                            FilterAdapter.this.mHandler.sendEmptyMessageDelayed(FilterAdapter.MESSAGE_TO_COLLECT_MODE, 300);
                        }
                    } else if (event.getAction() == 1) {
                        FilterAdapter.this.firstClickFilter(position, filterInfo, FilterViewHolder.this.itemView, false);
                    }
                    return true;
                }
            });
            if (!FilterAdapter.this.isCollectedMode) {
                this.child_collect.setVisibility(View.GONE);
                this.child_cancel_collect.setVisibility(View.GONE);
            } else if (filterInfo.isCollected) {
                this.child_collect.setVisibility(View.VISIBLE);
                this.child_cancel_collect.setVisibility(View.GONE);
            } else {
                this.child_collect.setVisibility(View.GONE);
                this.child_cancel_collect.setVisibility(View.VISIBLE);
            }
            int languageCode = LocaleUtil.getLanguageCode();
            String currentFilterName = languageCode == 1 ? ((FilterInfo) FilterAdapter.this.infos.get(position)).name_chs : languageCode == 2 ? ((FilterInfo) FilterAdapter.this.infos.get(position)).name_cht : ((FilterInfo) FilterAdapter.this.infos.get(position)).name_en;
            if (position == 0) {
                this.child_label.setText(R.string.BUTTON_PRESET_STORE_ENTRY);
            } else if (position == FilterAdapter.this.getNoneFilterIndex()) {
                this.child_label.setText(R.string.LABEL_NO_FILTER);
            } else {
                this.child_label.setText(currentFilterName);
            }
            if (FilterAdapter.this.mCurrentFilterInfo == null || FilterAdapter.this.mCurrentFilterInfo.name_en == null) {
                selected = filterInfo.name_en.equals(FilterAdapter.this.filterName);
            } else if (!(FilterAdapter.this.mCurrentFilterInfo.name_en.equals(filterInfo.name_en) && FilterAdapter.this.mCurrentFilterInfo.groupNameEn.equals(filterInfo.groupNameEn))) {
                selected = false;
            }
            this.child_preview.setImageBitmap(filterInfo.bitmap);
            this.child_label.setTextColor(selected ? App.getContext().getResources().getColor(R.color.colorWhite) : App.getContext().getResources().getColor(R.color.colorDate));
            this.child_preview_stroke.setVisibility(selected ? View.VISIBLE : View.INVISIBLE);
            if (FilterAdapter.this.isCollectedMode || position == FilterAdapter.this.getNoneFilterIndex()) {
                this.child_preview_stroke.setImageBitmap(null);
            } else {
                this.child_preview_stroke.setImageResource(R.drawable.icon_20_filter_strength);
            }
            if (selected) {
                this.child_collect.setBackground(null);
                this.child_cancel_collect.setBackground(null);
            } else {
                this.child_collect.setBackgroundResource(R.drawable.shape_filter_collect);
                this.child_cancel_collect.setBackgroundResource(R.drawable.shape_filter_collect);
            }
            if (position == 0) {
                this.child_download.setVisibility(View.VISIBLE);
                this.child_preview.setImageBitmap(null);
                this.child_preview.setBackgroundColor(FilterAdapter.this.context.getResources().getColor(R.color.colorFilterDownload));
                this.child_collect.setVisibility(View.GONE);
                this.child_cancel_collect.setVisibility(View.GONE);
            } else {
                this.child_download.setVisibility(View.GONE);
                this.child_preview.setBackgroundResource(R.drawable.shape_filter_preview_corner_bg);
            }
            if (position == FilterAdapter.this.getNoneFilterIndex() && !selected && FilterAdapter.this.isCollectedMode) {
                this.child_none.setVisibility(View.VISIBLE);
            } else {
                this.child_none.setVisibility(View.GONE);
            }
            if (position == FilterAdapter.this.getNoneFilterIndex()) {
                this.child_collect.setVisibility(View.GONE);
                this.child_cancel_collect.setVisibility(View.GONE);
            }
            TintColorUtil.tintDrawable(FilterAdapter.this.context, this.child_download, R.color.colorWhite);
        }
    }

    private class GroupViewHolder extends ViewHolder {
        public VerticalTextView group_name;

        public GroupViewHolder(View itemView) {
            super(itemView);
            this.group_name = itemView.findViewById(R.id.group_name);
        }

        public void onBindViewHolder(int position) {
            int languageCode = LocaleUtil.getLanguageCode();
            String groupName = languageCode == 1 ? FilterAdapter.this.infos.get(position).name_chs : languageCode == 2 ? ((FilterInfo) FilterAdapter.this.infos.get(position)).name_cht : ((FilterInfo) FilterAdapter.this.infos.get(position)).name_en;
            if (TextUtil.isNull(groupName)) {
                groupName = "";
            }
            this.group_name.setText(groupName);
        }
    }

    private class SortFilterViewHolder extends ViewHolder {
        private final AvenirNextCondensedRegularTextView child_label;
        private final ImageView child_preview;

        public SortFilterViewHolder(View itemView) {
            super(itemView);
            this.child_preview = (ImageView) itemView.findViewById(R.id.child_preview);
            this.child_label = (AvenirNextCondensedRegularTextView) itemView.findViewById(R.id.child_label);
        }

        public void onBindViewHolder(int position) {
            this.child_label.setText(FilterAdapter.this.context.getString(R.string.BUTTON_PRESET_REORDER));
            this.child_preview.setImageResource(R.drawable.icon_25_preset_series_reorder);
            this.child_preview.setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(View v) {
                    EventBus.getDefault().post(new CollectFilterEvent(null, 0));
                }
            });
        }
    }

    public List<FilterInfo> getInfos() {
        return this.infos;
    }

    public FilterAdapter(Activity context, List<FilterInfo> infos) {
        this.context = context;
        this.infos = infos;
    }

    public int getItemViewType(int position) {
        FilterInfo filterInfo = (FilterInfo) this.infos.get(position);
        if (filterInfo.isDownloading) {
            return 3;
        }
        if (filterInfo.type == 5) {
            return 5;
        }
        if (filterInfo.isGroup) {
            return 1;
        }
        return 2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new GroupViewHolder(LayoutInflater.from(this.context).inflate(R.layout.child_preview_group, parent, false));
        }
        if (viewType == 2) {
            return new FilterViewHolder(LayoutInflater.from(this.context).inflate(R.layout.child_preview, parent, false));
        }
        if (viewType == 3) {
            return new DownloadingViewHolder(LayoutInflater.from(this.context).inflate(R.layout.child_preview_loading_filter, parent, false));
        }
        if (viewType == 5) {
            return new SortFilterViewHolder(LayoutInflater.from(this.context).inflate(R.layout.child_preview_sort, parent, false));
        }
        return null;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == 1) {
            ((GroupViewHolder) holder).onBindViewHolder(position);
        } else if (getItemViewType(position) == 2) {
            ((FilterViewHolder) holder).onBindViewHolder(position);
        } else if (getItemViewType(position) == 3) {
            ((DownloadingViewHolder) holder).onBindViewHolder(position);
        } else if (getItemViewType(position) == 5) {
            ((SortFilterViewHolder) holder).onBindViewHolder(position);
        }
    }

    public int getItemCount() {
        if (this.infos.size() == 0) {
            return 0;
        }
        return this.infos.size();
    }

    public String getFilterName() {
        return this.filterName;
    }

    public void setFilterName(String filterName, FilterInfo filterInfo) {
        this.filterName = filterName;
        this.mCurrentFilterInfo = filterInfo;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setFilterChangeState(boolean filterIsChanging) {
        this.filterIsChanging = filterIsChanging;
    }

    public void displayUndoFilter(int position, FilterInfo filterInfo) {
        this.canEnterCollectedMode = false;
        this.mHandler.removeMessages(MESSAGE_TO_COLLECT_MODE);
        if (position != 0) {
            if ((!this.isCollectedMode || position == getNoneFilterIndex()) && !this.isCollectedMode && this.onClickListener != null && filterInfo != null) {
                this.mCurrentFilterInfo = filterInfo;
                this.filterName = filterInfo.name_en;
                this.onClickListener.onUndoReset(position, filterInfo.name_en);
                this.currentPosition = position;
            }
        }
    }

    public void firstClickFilter(int position, FilterInfo filterInfo, View itemView, boolean isOut) {
        boolean z = true;
        this.canEnterCollectedMode = false;
        this.mHandler.removeMessages(MESSAGE_TO_COLLECT_MODE);
        if (position == 0) {
            if (!this.isCollectedMode) {
                //IntentUtil.toFilterStoreListActivity(this.context, FilterStoreListActivity.COMEFROM_VALUE_FILTER);
                Toast.makeText(context,"go to FilterStoreListActivity",Toast.LENGTH_LONG).show();
            }
        } else if (this.isCollectedMode && position != getNoneFilterIndex()) {
            EventBus.getDefault().post(new CollectFilterEvent(filterInfo, position));
        } else if (!this.isCollectedMode && this.onClickListener != null) {
            this.mCurrentFilterInfo = filterInfo;
            this.filterName = filterInfo.name_en;
            OnClickListener onClickListener;
            String str;
            if (isOut) {
                onClickListener = this.onClickListener;
                str = filterInfo.name_en;
                if (position <= this.currentPosition) {
                    z = false;
                }
                onClickListener.onClickForGroupNameClick(itemView, position, str, z);
            } else {
                onClickListener = this.onClickListener;
                str = filterInfo.name_en;
                if (position <= this.currentPosition) {
                    z = false;
                }
                onClickListener.onClick(itemView, position, str, z);
            }
            this.currentPosition = position;
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void addOrReducePosition(int i) {
        GROUP_CLEAN_POSITION += i;
        GROUP_BOOST_POSITION += i;
        GROUP_BNW_POSITION += i;
        GROUP_FADED_POSITION += i;
        GROUP_FRESH_POSITION += i;
        GROUP_SUBTLE_POSITION += i;
        GROUP_GOLD_POSITION += i;
        GROUP_223_POSITION += i;
        GROUP_CINE_POSITION += i;
        GROUP_PORTRAIT_POSITION += i;
        GROUP_VIVID_POSITION += i;
        GROUP_FOOD_POSITION += i;
        GROUP_SELFIE_POSITION += i;
        GROUP_MOOD_POSITION += i;
        LogUtil.d("addOrReducePosition", "GROUP_COLLECT_POSITION = " + GROUP_COLLECT_POSITION + ", \n GROUP_CLEAN_POSITION = " + GROUP_CLEAN_POSITION + ", \n GROUP_BOOST_POSITION = " + GROUP_BOOST_POSITION + ", \n GROUP_BNW_POSITION = " + GROUP_BNW_POSITION + ", \n GROUP_FADED_POSITION = " + GROUP_FADED_POSITION + ", \n GROUP_FRESH_POSITION = " + GROUP_FRESH_POSITION + ", \n GROUP_SUBTLE_POSITION = " + GROUP_SUBTLE_POSITION + ", \n GROUP_GOLD_POSITION = " + GROUP_GOLD_POSITION + ", \n GROUP_223_POSITION = " + GROUP_223_POSITION + ", \n GROUP_CINE_POSITION = " + GROUP_CINE_POSITION + ", \n GROUP_PORTRAIT_POSITION = " + GROUP_PORTRAIT_POSITION + ", \n GROUP_VIVID_POSITION = " + GROUP_VIVID_POSITION + ", \n GROUP_TINT_POSITION = " + GROUP_FOOD_POSITION + ", \n GROUP_SELFIE_POSITION = " + GROUP_SELFIE_POSITION + ", \n GROUP_MOOD_POSITION = " + GROUP_MOOD_POSITION);
    }

    public void forbidEnterCollectMode() {
        this.canEnterCollectedMode = false;
        this.mHandler.removeMessages(MESSAGE_TO_COLLECT_MODE);
    }

    public void onDestroyBitmap() {
        if (this.infos != null && this.infos.size() > 0) {
            for (int i = 0; i < this.infos.size(); i++) {
                ((FilterInfo) this.infos.remove(i)).bitmap = null;
            }
        }
    }

    public int getNoneFilterIndex() {
        if (!TextUtil.isValidate(this.infos)) {
            return 0;
        }
        if (((FilterInfo) this.infos.get(1)).type == 5) {
            return 2;
        }
        return 1;
    }
}