//package cn.nineton.onetake.adapter;
//
//import android.app.Activity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import cn.nineton.onetake.DisplayAlbumView;
//import cn.nineton.onetake.R;
//import cn.nineton.onetake.bean.VideoAlbumEntity;
//import cn.nineton.onetake.util.AnimationUtil;
//import cn.nineton.onetake.util.AppPhotoConfig;
//import cn.nineton.onetake.util.DensityUtil;
//import cn.nineton.onetake.util.FrescoUriUtil;
//import cn.nineton.onetake.util.ScrimUtil;
//import cn.nineton.onetake.util.TestUtil;
//import cn.nineton.onetake.util.TextUtil;
//import cn.nineton.onetake.util.TintColorUtil;
//import cn.nineton.onetake.widget.AvenirNextRegularTextView;
////import butterknife.InjectView;
////import com.blink.academy.onetake.fresco.FrescoUriUtil;
////import com.blink.academy.onetake.fresco.configs.imagepipeline.AppPhotoConfig;
////import com.blink.academy.onetake.support.utils.AnimationUtil;
////import com.blink.academy.onetake.support.utils.DensityUtil;
////import com.blink.academy.onetake.support.utils.ScrimUtil;
////import com.blink.academy.onetake.support.utils.TestUtil;
////import com.blink.academy.onetake.support.utils.TextUtil;
////import com.blink.academy.onetake.support.utils.TintColorUtil;
////import com.blink.academy.onetake.ui.activity.video.VideoActivity2;
////import com.blink.academy.onetake.ui.adapter.VideoAlbumAdapter$VideoAlbumViewHolder$.Lambda.1;
////import com.blink.academy.onetake.ui.adapter.base.BaseRecyclerAdapter;
////import com.blink.academy.onetake.ui.adapter.entities.VideoAlbumEntity;
////import com.blink.academy.onetake.ui.adapter.holder.ABRecyclerViewHolder;
////import com.blink.academy.onetake.widgets.DisplayAlbumView;
////import com.blink.academy.onetake.widgets.TextView.AvenirNextRegularTextView;
//import com.facebook.drawee.controller.BaseControllerListener;
//import com.facebook.drawee.view.SimpleDraweeView;
//import java.util.ArrayList;
//import java.util.List;
//
//public class VideoAlbumAdapter extends BaseRecyclerAdapter<VideoAlbumEntity> {
//    private static final String TAG = VideoAlbumAdapter.class.getSimpleName();
//    private boolean canAdd = true;
//    private boolean canAlphaShow = true;
//    private int comeFrom;
//    private DisplayAlbumView displayAlbumView;
//    private boolean fromFilterActivity = false;
//    private String mCurrentTimeStamp;
//    private String mFilterName;
//    private int mItemLength = (((DensityUtil.getMetricsWidth(getActivity()) - DensityUtil.dip2px(2.0f)) / 3) - DensityUtil.dip2px(1.0f));
//    private OnSelectStateChange onSelectStateChange;
//    private ArrayList<String> pathList;
//    private List<VideoAlbumEntity> selList;
//
//    public interface OnSelectStateChange {
//        void onCancel(List<VideoAlbumEntity> list);
//
//        void onSelect(List<VideoAlbumEntity> list);
//    }
//
//    public class VideoAlbumViewHolder extends ABRecyclerViewHolder {
//        @BindView(R.id.alpha_view)
//        View alpha_view;
//        @BindView(R.id.alpha_view_mul)
//        View alpha_view_mul;
//        @BindView(R.id.gradient_view)
//        View gradient_view;
//        VideoAlbumEntity mVideoAlbumEntity;
//        @BindView(R.id.multiple_select_view)
//        View multiple_select_view;
//        @BindView(R.id.ring_gold)
//        View ring_gold;
//        @BindView(R.id.rl_already_import)
//        View rl_already_import;
//        @BindView(R.id.rl_multiple_select)
//        View rl_multiple_select;
//        @BindView(R.id.tag_image_circle_otpb)
//        View tag_image_circle_otpb;
//        @BindView(R.id.tv_multiple_num)
//        TextView tv_multiple_num;
//        @BindView(R.id.video_duration_anrtv)
//        AvenirNextRegularTextView video_duration_anrtv;
//        @BindView(R.id.video_icon_iv)
//        ImageView video_icon_iv;
//        @BindView(R.id.video_info_layout_rl)
//        View video_info_layout_rl;
//        @BindView(R.id.video_thumbnail_sdv)
//        SimpleDraweeView video_thumbnail_sdv;
//
//        public VideoAlbumViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//            TintColorUtil.tintDrawable(getActivity(), this.video_icon_iv, 2131755096);
//            this.video_info_layout_rl.getLayoutParams().width = mItemLength;
//            this.video_info_layout_rl.getLayoutParams().height = mItemLength;
//            this.gradient_view.setBackground(ScrimUtil.makeCubicGradientScrimDrawable(getActivity().getResources().getColor(R.color.color30Black), 2, 80));
//        }
//
//        public void onBindViewHolder(int position) {
//            if (getCards() != null) {
//                int size = getCards().size();
//                if (position >= 0 && position < size) {
//                    boolean hasSelected;
//                    this.mVideoAlbumEntity = (VideoAlbumEntity) getCards().get(position);
//                    boolean contained = false;
//                    if (TextUtil.isValidate(pathList) && pathList.contains(this.mVideoAlbumEntity.getVideoPath())) {
//                        contained = true;
//                    }
//                    if (TextUtil.isValidate(selList) && this.mVideoAlbumEntity.getSelect() == 0) {
//                        int changeIndex = -1;
//                        for (int i = 0; i < selList.size(); i++) {
//                            if (((VideoAlbumEntity) selList.get(i)).getVideoPath().equals(this.mVideoAlbumEntity.getVideoPath())) {
//                                this.mVideoAlbumEntity.setSelect(1);
//                                changeIndex = i;
//                            }
//                        }
//                        if (changeIndex != -1) {
//                            selList.remove(changeIndex);
//                            selList.add(changeIndex, this.mVideoAlbumEntity);
//                        }
//                    }
//                    if (contained) {
//                        this.rl_already_import.setVisibility(View.VISIBLE);
//                    } else {
//                        this.rl_already_import.setVisibility(View.GONE);
//                    }
//                    this.rl_multiple_select.setVisibility(View.VISIBLE);
//                    if (this.mVideoAlbumEntity.getSelect() == 0) {
//                        hasSelected = true;
//                    } else {
//                        hasSelected = false;
//                    }
//                    if (hasSelected) {
//                        this.ring_gold.setVisibility(View.INVISIBLE);
//                        this.alpha_view_mul.setVisibility(View.GONE);
//                        this.tv_multiple_num.setVisibility(View.GONE);
//                    } else {
//                        this.ring_gold.setVisibility(View.VISIBLE);
//                        this.ring_gold.setAlpha(1.0f);
//                        int index = selList.indexOf(this.mVideoAlbumEntity) + 1;
//                        this.tv_multiple_num.getPaint().setFakeBoldText(true);
//                        this.tv_multiple_num.setText(String.valueOf(index));
//                        this.tv_multiple_num.setVisibility(View.VISIBLE);
//                        if (contained) {
//                            this.alpha_view_mul.setVisibility(View.GONE);
//                        } else {
//                            this.alpha_view_mul.setVisibility(View.VISIBLE);
//                        }
//                    }
//                    this.alpha_view.setVisibility(View.GONE);
//                    this.tag_image_circle_otpb.setVisibility(View.GONE);
//                    this.video_thumbnail_sdv.setController(AppPhotoConfig.getAlbumPhotoConfig(FrescoUriUtil.SchemeFile + (TextUtil.isValidate(this.mVideoAlbumEntity.getVideoThumbnailPath()) ? this.mVideoAlbumEntity.getVideoThumbnailPath() : this.mVideoAlbumEntity.getVideoPath()), this.video_thumbnail_sdv, new BaseControllerListener()));
//                    this.video_duration_anrtv.setText(VideoAlbumAdapter.durationFormat((int) Math.floor((double) (((float) this.mVideoAlbumEntity.getVideoDuration()) / 1000.0f))));
//                    final boolean finalContained = contained;
////                    this.multiple_select_view.setOnClickListener(1.lambdaFactory$(this, finalContained));
//                    multiple_select_view.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            lambda$onBindViewHolder$0(finalContained,v);
//                        }
//                    });
//                    this.video_info_layout_rl.setOnClickListener(new OnClickListener() {
//                        public void onClick(View v) {
//                            if (!DisplayAlbumView.canAdapterClick) {
//                                return;
//                            }
//                            if (canAlphaShow) {
//                                if (TestUtil.isOS105()) {
//                                    //always true
//                                    //if (getActivity() instanceof VideoActivity2) {
//                                       // ((VideoActivity2) getActivity()).closeCamera();
////                                   // }
//                                    alpha_view.setVisibility(View.VISIBLE);
//                                    tag_image_circle_otpb.setVisibility(View.VISIBLE);
//                                    if (displayAlbumView != null) {
//                                        displayAlbumView.singleVideoClick(mVideoAlbumEntity);
//                                    }
//                                }
//                            } else if (mVideoAlbumEntity.getSelect() != 0) {
//                                selList.remove(mVideoAlbumEntity);
//                                if (onSelectStateChange != null) {
//                                    onSelectStateChange.onCancel(selList);
//                                }
//                                mVideoAlbumEntity.setSelect(0);
//                                notifyDataSetChanged();
//                            } else if (canAdd) {
//                                selList.add(mVideoAlbumEntity);
//                                if (onSelectStateChange != null) {
//                                    onSelectStateChange.onSelect(selList);
//                                }
//                                mVideoAlbumEntity.setSelect(1);
//                                int index = selList.size();
//                                tv_multiple_num.getPaint().setFakeBoldText(true);
//                                tv_multiple_num.setText(String.valueOf(index));
//                                if (!finalContained) {
//                                    alpha_view_mul.setVisibility(View.VISIBLE);
//                                }
//                                AnimationUtil.alphaAnimate(ring_gold, 1.0f, 30, null);
//                                AnimationUtil.alphaAnimate(tv_multiple_num, 1.0f, 30, null);
//                            }
//                        }
//                    });
//                }
//            }
//        }
//
//        private /* synthetic */ void lambda$onBindViewHolder$0(boolean finalContained, View v) {
//            if (!DisplayAlbumView.canAdapterClick) {
//                return;
//            }
//            if (this.mVideoAlbumEntity.getSelect() != 0) {
//                selList.remove(this.mVideoAlbumEntity);
//                if (onSelectStateChange != null) {
//                    onSelectStateChange.onCancel(selList);
//                }
//                this.mVideoAlbumEntity.setSelect(0);
//                notifyDataSetChanged();
//            } else if (canAdd) {
//                selList.add(this.mVideoAlbumEntity);
//                if (onSelectStateChange != null) {
//                    onSelectStateChange.onSelect(selList);
//                }
//                this.mVideoAlbumEntity.setSelect(1);
//                int index = selList.size();
//                this.tv_multiple_num.getPaint().setFakeBoldText(true);
//                this.tv_multiple_num.setText(String.valueOf(index));
//                if (!finalContained) {
//                    this.alpha_view_mul.setVisibility(View.VISIBLE);
//                }
//                AnimationUtil.alphaAnimate(this.ring_gold, 1.0f, 30, null);
//                AnimationUtil.alphaAnimate(this.tv_multiple_num, 1.0f, 30, null);
//            }
//        }
//    }
//
//    public void setDisplayAlbumView(DisplayAlbumView displayAlbumView) {
//        this.displayAlbumView = displayAlbumView;
//    }
//
//    public void setCanAdd(boolean canAdd) {
//        this.canAdd = canAdd;
//    }
//
//    public void setCanAlphaShow(boolean canAlphaShow) {
//        this.canAlphaShow = canAlphaShow;
//    }
//
//    public void setComeFrom(int comeFrom) {
//        this.comeFrom = comeFrom;
//    }
//
//    public void clearSelList() {
//        this.selList.clear();
//    }
//
//    public List<VideoAlbumEntity> getSelectVideoBeans() {
//        return this.selList;
//    }
//
//    public List<String> getSelectVideoPaths() {
//        if (!TextUtil.isValidate(this.selList)) {
//            return null;
//        }
//        List<String> arrayList = new ArrayList();
//        for (VideoAlbumEntity entity : this.selList) {
//            arrayList.add(entity.getVideoPath());
//        }
//        return arrayList;
//    }
//
//    public void setPathList(ArrayList<String> pathList) {
//        this.pathList = pathList;
//    }
//
//    public VideoAlbumAdapter(Activity activity, List<VideoAlbumEntity> list, int comeFrom, String currentTimeStamp) {
//        super(activity, list);
//        this.comeFrom = comeFrom;
//        this.mCurrentTimeStamp = currentTimeStamp;
//        this.selList = new ArrayList();
//    }
//
//    public ABRecyclerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
//        return new VideoAlbumViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.layout_video_album, parent, false));
//    }
//
//    protected void onBindItemView(ABRecyclerViewHolder holder, int position) {
//        holder.onBindViewHolder(position);
//    }
//
//    public void setFilterName(String filterName) {
//        this.mFilterName = filterName;
//    }
//
//    public void setFromFilterActivity(boolean fromFilterActivity) {
//        this.fromFilterActivity = fromFilterActivity;
//    }
//
//    public static String durationFormat(int duration) {
//        if (duration <= 0) {
//            return "";
//        }
//        StringBuilder format = new StringBuilder("");
//        if (duration < 10) {
//            format.append("00:0").append(duration);
//        } else if (duration < 60) {
//            format.append("00:").append(duration);
//        } else {
//            int s = duration % 60;
//            int min = duration / 60;
//            if (min < 10) {
//                format.append("0").append(min).append(":");
//            } else {
//                format.append(min).append(":");
//            }
//            if (s < 10) {
//                format.append("0").append(s);
//            } else {
//                format.append(s);
//            }
//        }
//        return format.toString();
//    }
//
//    public void setOnSelectStateChange(OnSelectStateChange onSelectStateChange) {
//        this.onSelectStateChange = onSelectStateChange;
//    }
//}