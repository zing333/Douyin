package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.util.List;

public class GiphyResultBean implements Parcelable {
    public static final Creator<GiphyResultBean> CREATOR = new Creator<GiphyResultBean>() {
        public GiphyResultBean createFromParcel(Parcel source) {
            return new GiphyResultBean(source);
        }

        public GiphyResultBean[] newArray(int size) {
            return new GiphyResultBean[size];
        }
    };
    public List<GiphyDataInfoBean> data;
    public GiphyMetaBean meta;
    public GiphyPaginationBean pagination;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.data);
        dest.writeParcelable(this.meta, flags);
        dest.writeParcelable(this.pagination, flags);
    }

    protected GiphyResultBean(Parcel in) {
        this.data = in.createTypedArrayList(GiphyDataInfoBean.CREATOR);
        this.meta = (GiphyMetaBean) in.readParcelable(GiphyMetaBean.class.getClassLoader());
        this.pagination = (GiphyPaginationBean) in.readParcelable(GiphyPaginationBean.class.getClassLoader());
    }

    public int removeInvalidData() {
        int count = 0;
        if (this.data != null) {
            for (int i = this.data.size() - 1; i > -1; i--) {
                GiphyDataInfoBean giphyDataInfoBean = (GiphyDataInfoBean) this.data.get(i);
                if (giphyDataInfoBean != null) {
                    GiphyImagesBean images = giphyDataInfoBean.images;
                    if (images != null) {
                        GiphyImageInfoBean fixed_width_downsampled = images.fixed_width_downsampled;
                        GiphyImageInfoBean original = images.original;
                        if (fixed_width_downsampled == null) {
                            this.data.remove(i);
                            count++;
                        } else if (TextUtils.isEmpty(fixed_width_downsampled.width) || TextUtils.isEmpty(fixed_width_downsampled.height)) {
                            this.data.remove(i);
                            count++;
                        } else if (original == null) {
                            this.data.remove(i);
                            count++;
                        } else if (TextUtils.isEmpty(original.mp4)) {
                            this.data.remove(i);
                            count++;
                        } else if (TextUtils.isEmpty(original.mp4_size)) {
                            this.data.remove(i);
                            count++;
                        } else if (Integer.parseInt(original.mp4_size) > 1048576) {
                            this.data.remove(i);
                            count++;
                        } else if (TextUtils.isEmpty(original.width) || TextUtils.isEmpty(original.height)) {
                            this.data.remove(i);
                            count++;
                        } else {
                            double proportion = (((double) Integer.parseInt(original.height)) * 1.0d) / ((double) Integer.parseInt(original.width));
                            if (proportion < 0.4d || proportion > 1.3333333333333333d) {
                                this.data.remove(i);
                                count++;
                            }
                        }
                    }
                } else {
                    this.data.remove(i);
                    count++;
                }
            }
        }
        return count;
    }
}