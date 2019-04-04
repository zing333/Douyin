package cn.nineton.onetake.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.model.StatusBarModel;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.events.ChangeStatusBarColorEvent;
//import com.blink.academy.onetake.support.events.StartTopViewDisMissEvent;
//import com.blink.academy.onetake.support.events.StartTopViewShowEvent;
//import com.blink.academy.onetake.support.utils.StatusBarUtil;
//import de.greenrobot.event.EventBus;
import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.WeakHashMap;

import cn.nineton.onetake.App;
import cn.nineton.onetake.R;
import cn.nineton.onetake.bean.StatusBarModel;
import cn.nineton.onetake.event.ChangeStatusBarColorEvent;
import cn.nineton.onetake.event.StartTopViewDisMissEvent;
import cn.nineton.onetake.event.StartTopViewShowEvent;

class MessageManager extends Handler implements Comparator<AppMessage> {
    private static final int MESSAGE_ADD_VIEW = -1040157475;
    private static final int MESSAGE_DISPLAY = 794631;
    private static final int MESSAGE_REMOVE = -1040155167;
    private static int count = 0;
    private static WeakHashMap<Activity, MessageManager> sManagers;
    private static ReleaseCallbacks sReleaseCallbacks;
    Runnable clearRunnable = new Runnable() {
        public void run() {
            MessageManager.access$110();
            LogUtil.d("setColorNav", "clearRunnable count : " + MessageManager.count);
        }
    };
    private final Queue<AppMessage> msgQueue = new PriorityQueue(1, this);
    private final Queue<AppMessage> stickyQueue = new LinkedList();

    private static class OutAnimationListener implements AnimationListener {
        private final AppMessage appMessage;

//        /* synthetic */ OutAnimationListener(AppMessage x0, AnonymousClass1 x1) {
//            this(x0);
//        }

        private OutAnimationListener(AppMessage appMessage) {
            this.appMessage = appMessage;
        }

        public void onAnimationStart(Animation animation) {
            LogUtil.d("setColorNav", "onAnimationStart");
        }

        public void onAnimationEnd(Animation animation) {
            LogUtil.d("setColorNav", "onAnimationEnd count : " + MessageManager.count);
            final View view = this.appMessage.getView();
            if (this.appMessage.isFloating()) {
                LogUtil.d("setColorNav", "appMessage.isFloating()");
                final ViewGroup parent = (ViewGroup) view.getParent();
                if (parent != null) {
                    parent.post(new Runnable() {
                        public void run() {
                            parent.removeView(view);
                        }
                    });
                }
            } else {
                LogUtil.d("setColorNav", "appMessage.isFloating() else ");
                view.setVisibility(View.GONE);
            }
            if (MessageManager.count == 0) {
                if (this.appMessage.getShowType() != 1) {
                    StatusBarUtil.setColorNav(this.appMessage.getActivity());
                }
                StatusBarModel.getInstance().setColorAndState(0, 1);
                EventBus.getDefault().post(new ChangeStatusBarColorEvent(-1));
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    interface ReleaseCallbacks {
        void register(Application application);
    }

    @TargetApi(14)
    static class ReleaseCallbacksIcs implements ActivityLifecycleCallbacks, ReleaseCallbacks {
        private WeakReference<Application> mLastApp;

        ReleaseCallbacksIcs() {
        }

        public void register(Application app) {
            if (this.mLastApp == null || this.mLastApp.get() != app) {
                this.mLastApp = new WeakReference(app);
                app.registerActivityLifecycleCallbacks(this);
            }
        }

        public void onActivityDestroyed(Activity activity) {
            MessageManager.release(activity);
        }

        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        public void onActivityStarted(Activity activity) {
        }

        public void onActivityResumed(Activity activity) {
        }

        public void onActivityPaused(Activity activity) {
        }

        public void onActivityStopped(Activity activity) {
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }
    }

    static /* synthetic */ int access$110() {
        int i = count;
        count = i - 1;
        return i;
    }

    private MessageManager() {
    }

    static synchronized MessageManager obtain(Activity activity) {
        MessageManager manager;
        synchronized (MessageManager.class) {
            if (sManagers == null) {
                sManagers = new WeakHashMap(1);
            }
            manager = (MessageManager) sManagers.get(activity);
            if (manager == null) {
                manager = new MessageManager();
                ensureReleaseOnDestroy(activity);
                sManagers.put(activity, manager);
            }
        }
        return manager;
    }

    static void ensureReleaseOnDestroy(Activity activity) {
        if (VERSION.SDK_INT >= 14) {
            if (sReleaseCallbacks == null) {
                sReleaseCallbacks = new ReleaseCallbacksIcs();
            }
            sReleaseCallbacks.register(activity.getApplication());
        }
    }

    static synchronized void release(Activity activity) {
        synchronized (MessageManager.class) {
            if (sManagers != null) {
                MessageManager manager = (MessageManager) sManagers.remove(activity);
                if (manager != null) {
                    manager.clearAllMessage();
                }
            }
        }
    }

    static synchronized void clearAll() {
        synchronized (MessageManager.class) {
            if (sManagers != null) {
                Iterator<MessageManager> iterator = sManagers.values().iterator();
                while (iterator.hasNext()) {
                    MessageManager manager = (MessageManager) iterator.next();
                    if (manager != null) {
                        manager.clearAllMessage();
                    }
                    iterator.remove();
                }
                sManagers.clear();
            }
        }
    }

    void add(AppMessage appMessage) {
        if (!this.msgQueue.contains(appMessage)) {
            this.msgQueue.add(appMessage);
            if (appMessage.mInAnimation == null) {
                appMessage.mInAnimation = AnimationUtils.loadAnimation(appMessage.getActivity(), R.anim.actionbar_translate_enter);
            }
            if (appMessage.mOutAnimation == null) {
                appMessage.mOutAnimation = AnimationUtils.loadAnimation(appMessage.getActivity(), R.anim.actionbar_translate_exit);
            }
            displayMessage();
        }
    }

    void clearMessage(AppMessage appMessage) {
        if (this.msgQueue.contains(appMessage) || this.stickyQueue.contains(appMessage)) {
            removeMessages(MESSAGE_DISPLAY, appMessage);
            removeMessages(MESSAGE_ADD_VIEW, appMessage);
            removeMessages(MESSAGE_REMOVE, appMessage);
            this.msgQueue.remove(appMessage);
            this.stickyQueue.remove(appMessage);
            removeMessage(appMessage);
        }
    }

    void clearAllMessage() {
        removeMessages(MESSAGE_DISPLAY);
        removeMessages(MESSAGE_ADD_VIEW);
        removeMessages(MESSAGE_REMOVE);
        clearShowing();
        this.msgQueue.clear();
        this.stickyQueue.clear();
    }

    void clearShowing() {
        Collection<AppMessage> showing = new HashSet();
        obtainShowing(this.msgQueue, showing);
        obtainShowing(this.stickyQueue, showing);
        for (AppMessage msg : showing) {
            clearMessage(msg);
        }
    }

    static void obtainShowing(Collection<AppMessage> from, Collection<AppMessage> appendTo) {
        for (AppMessage msg : from) {
            if (msg.isShowing()) {
                appendTo.add(msg);
            }
        }
    }

    private void displayMessage() {
        if (!this.msgQueue.isEmpty()) {
            AppMessage appMessage = (AppMessage) this.msgQueue.peek();
            if (!appMessage.isShowing()) {
                Message msg = obtainMessage(MESSAGE_ADD_VIEW);
                msg.obj = appMessage;
                sendMessage(msg);
                LogUtil.d("ljc", "timea" + System.currentTimeMillis());
            } else if (appMessage.getDuration() != -1) {
                sendMessageDelayed(obtainMessage(MESSAGE_DISPLAY), (long) appMessage.getDuration());
            }
        }
    }

    private void removeMessage(AppMessage appMessage) {
        clearMessage(appMessage);
        View view = appMessage.getView();
        if (((ViewGroup) view.getParent()) != null) {
            appMessage.mOutAnimation.setAnimationListener(new OutAnimationListener(appMessage));
            view.clearAnimation();
            EventBus.getDefault().post(new StartTopViewDisMissEvent());
            view.startAnimation(appMessage.mOutAnimation);
        }
        sendMessage(obtainMessage(MESSAGE_DISPLAY));
    }

    private void addMessageToView(final AppMessage appMessage) {
        final int duration = appMessage.getDuration();
        View view = appMessage.getView();
        if (view.getParent() == null) {
            count++;
            LogUtil.d("setColorNav", "addMessageToView count : " + count);
            postDelayed(this.clearRunnable, (long) duration);
            ViewGroup targetParent = appMessage.getParent();
            LayoutParams params = appMessage.getLayoutParams();
            if (targetParent != null) {
                targetParent.addView(view, params);
            } else {
                appMessage.getActivity().addContentView(view, params);
            }
        }
        view.clearAnimation();
        EventBus.getDefault().post(new StartTopViewShowEvent(appMessage.messageText, App.getResource().getColor(appMessage.getStyle().getBackground())));
        view.startAnimation(appMessage.mInAnimation);
        appMessage.mInAnimation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                if (duration != -1) {
                    Message msg = MessageManager.this.obtainMessage(MessageManager.MESSAGE_REMOVE);
                    msg.obj = appMessage;
                    MessageManager.this.sendMessageDelayed(msg, (long) duration);
                    return;
                }
                MessageManager.this.stickyQueue.add(MessageManager.this.msgQueue.poll());
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
            LogUtil.d("setColorNav", "addMessageToView  : view.setVisibility(View.VISIBLE)");
        }
        if (appMessage.getShowType() != 1) {
            ContextCompat.getColor(appMessage.getActivity(), appMessage.getStyle().getBackground());
        }
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MESSAGE_ADD_VIEW /*-1040157475*/:
                addMessageToView((AppMessage) msg.obj);
                return;
            case MESSAGE_REMOVE /*-1040155167*/:
                removeMessage((AppMessage) msg.obj);
                return;
            case MESSAGE_DISPLAY /*794631*/:
                displayMessage();
                return;
            default:
                super.handleMessage(msg);
                return;
        }
    }

    public int compare(AppMessage lhs, AppMessage rhs) {
        return inverseCompareInt(lhs.mPriority, rhs.mPriority);
    }

    static int inverseCompareInt(int lhs, int rhs) {
        if (lhs < rhs) {
            return 1;
        }
        return lhs == rhs ? 0 : -1;
    }
}