package cn.nineton.onetake.adapter;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
//import com.chad.library.R;
//import com.chad.library.adapter.base.listener.OnItemDragListener;
//import com.chad.library.adapter.base.listener.OnItemSwipeListener;
//import com.umeng.commonsdk.stateless.d;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;

import java.util.Collections;
import java.util.List;

import cn.nineton.onetake.R;


public abstract class BaseItemDraggableAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {
    private static final String ERROR_NOT_SAME_ITEMTOUCHHELPER = "Item drag and item swipe should pass the same ItemTouchHelper";
    private static final int NO_TOGGLE_VIEW = 0;
    protected boolean itemDragEnabled = false;
    protected boolean itemSwipeEnabled = false;
    protected boolean mDragOnLongPress = true;
    protected ItemTouchHelper mItemTouchHelper;
    protected OnItemDragListener mOnItemDragListener;
    protected OnItemSwipeListener mOnItemSwipeListener;
    protected OnLongClickListener mOnToggleViewLongClickListener;
    protected OnTouchListener mOnToggleViewTouchListener;
    protected int mToggleViewId = 0;

    public BaseItemDraggableAdapter(List<T> data) {
        super(data);
    }

    public BaseItemDraggableAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    public void onBindViewHolder(K holder, int positions) {
        super.onBindViewHolder(holder, positions);
        int viewType = holder.getItemViewType();
        if (this.mItemTouchHelper != null && this.itemDragEnabled && viewType != 546 && viewType != 273 && viewType != 1365 && viewType != 819) {
            if (this.mToggleViewId != 0) {
                View toggleView = holder.getView(this.mToggleViewId);
                if (toggleView != null) {
                    toggleView.setTag(R.id.BaseQuickAdapter_viewholder_support, holder);
                    if (this.mDragOnLongPress) {
                        toggleView.setOnLongClickListener(this.mOnToggleViewLongClickListener);
                        return;
                    } else {
                        toggleView.setOnTouchListener(this.mOnToggleViewTouchListener);
                        return;
                    }
                }
                return;
            }
            holder.itemView.setTag(R.id.BaseQuickAdapter_viewholder_support, holder);
            holder.itemView.setOnLongClickListener(this.mOnToggleViewLongClickListener);
        }
    }

    public void setToggleViewId(int toggleViewId) {
        this.mToggleViewId = toggleViewId;
    }

    public void setToggleDragOnLongPress(boolean longPress) {
        this.mDragOnLongPress = longPress;
        if (this.mDragOnLongPress) {
            this.mOnToggleViewTouchListener = null;
            this.mOnToggleViewLongClickListener = new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    if (BaseItemDraggableAdapter.this.mItemTouchHelper != null && BaseItemDraggableAdapter.this.itemDragEnabled) {
                        BaseItemDraggableAdapter.this.mItemTouchHelper.startDrag((ViewHolder) v.getTag(R.id.BaseQuickAdapter_viewholder_support));
                    }
                    return true;
                }
            };
            return;
        }
        this.mOnToggleViewTouchListener = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) != 0 || BaseItemDraggableAdapter.this.mDragOnLongPress) {
                    return false;
                }
                if (BaseItemDraggableAdapter.this.mItemTouchHelper != null && BaseItemDraggableAdapter.this.itemDragEnabled) {
                    BaseItemDraggableAdapter.this.mItemTouchHelper.startDrag((ViewHolder) v.getTag(R.id.BaseQuickAdapter_viewholder_support));
                }
                return true;
            }
        };
        this.mOnToggleViewLongClickListener = null;
    }

    public void enableDragItem(@NonNull ItemTouchHelper itemTouchHelper) {
        enableDragItem(itemTouchHelper, 0, true);
    }

    public void enableDragItem(@NonNull ItemTouchHelper itemTouchHelper, int toggleViewId, boolean dragOnLongPress) {
        this.itemDragEnabled = true;
        this.mItemTouchHelper = itemTouchHelper;
        setToggleViewId(toggleViewId);
        setToggleDragOnLongPress(dragOnLongPress);
    }

    public void disableDragItem() {
        this.itemDragEnabled = false;
        this.mItemTouchHelper = null;
    }

    public boolean isItemDraggable() {
        return this.itemDragEnabled;
    }

    public void enableSwipeItem() {
        this.itemSwipeEnabled = true;
    }

    public void disableSwipeItem() {
        this.itemSwipeEnabled = false;
    }

    public boolean isItemSwipeEnable() {
        return this.itemSwipeEnabled;
    }

    public void setOnItemDragListener(OnItemDragListener onItemDragListener) {
        this.mOnItemDragListener = onItemDragListener;
    }

    public int getViewHolderPosition(ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition() - getHeaderLayoutCount();
    }

    public void onItemDragStart(ViewHolder viewHolder) {
        if (this.mOnItemDragListener != null && this.itemDragEnabled) {
            this.mOnItemDragListener.onItemDragStart(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void onItemDragMoving(ViewHolder source, ViewHolder target) {
        int from = getViewHolderPosition(source);
        int to = getViewHolderPosition(target);
        int i;
        if (from < to) {
            for (i = from; i < to; i++) {
                Collections.swap(this.mData, i, i + 1);
            }
        } else {
            for (i = from; i > to; i--) {
                Collections.swap(this.mData, i, i - 1);
            }
        }
        notifyItemMoved(source.getAdapterPosition(), target.getAdapterPosition());
        if (this.mOnItemDragListener != null && this.itemDragEnabled) {
            this.mOnItemDragListener.onItemDragMoving(source, from, target, to);
        }
    }

    public void onItemDragEnd(ViewHolder viewHolder) {
        if (this.mOnItemDragListener != null && this.itemDragEnabled) {
            this.mOnItemDragListener.onItemDragEnd(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void setOnItemSwipeListener(OnItemSwipeListener listener) {
        this.mOnItemSwipeListener = listener;
    }

    public void onItemSwipeStart(ViewHolder viewHolder) {
        if (this.mOnItemSwipeListener != null && this.itemSwipeEnabled) {
            this.mOnItemSwipeListener.onItemSwipeStart(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void onItemSwipeClear(ViewHolder viewHolder) {
        if (this.mOnItemSwipeListener != null && this.itemSwipeEnabled) {
            this.mOnItemSwipeListener.clearView(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void onItemSwiped(ViewHolder viewHolder) {
        if (this.mOnItemSwipeListener != null && this.itemSwipeEnabled) {
            this.mOnItemSwipeListener.onItemSwiped(viewHolder, getViewHolderPosition(viewHolder));
        }
        this.mData.remove(getViewHolderPosition(viewHolder));
        notifyItemRemoved(viewHolder.getAdapterPosition());
    }

    public void onItemSwiping(Canvas canvas, ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
        if (this.mOnItemSwipeListener != null && this.itemSwipeEnabled) {
            this.mOnItemSwipeListener.onItemSwipeMoving(canvas, viewHolder, dX, dY, isCurrentlyActive);
        }
    }
}