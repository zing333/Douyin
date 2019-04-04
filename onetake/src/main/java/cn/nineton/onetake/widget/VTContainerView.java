package cn.nineton.onetake.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.Config;
//import com.blink.academy.onetake.bean.VTFontDesBean;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.EmojiUtil;
//import com.blink.academy.onetake.support.utils.FontsUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.widgets.VideoText.VTContainerView$.Lambda.1;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import cn.nineton.onetake.App;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.bean.VTFontDesBean;
import cn.nineton.onetake.util.Config;
import cn.nineton.onetake.util.EmojiUtil;
import cn.nineton.onetake.util.FontsUtil;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.TextUtil;

public class VTContainerView extends View {
    public static final String TAG = VTContainerView.class.getSimpleName();
    private static final int YOffset = 0;
    private static final int mDefaultUnderLinePadding = 50;
    private static final int mDefaultUnderLineWidth = 2;
    private int DafaultMoveYOffset;
    private final RectF _availableSpaceRect = new RectF();
    private SizeTester _sizeTester;
    private int allHeight;
    private Paint centerPaint;
    private List<String> curMaxTextNumLineText;
    private List<String> curMaxWidthLineText;
    private boolean isEdit = false;
    private AlignType mAlignType = AlignType.M;
    private int mBorderWidth = 6;
    private Path mBottomPath;
    private int mDefaultBorderWidth = 6;
    private List<List<String>> mFinalTexts;
    private String mFontName;
    private FontSizeType mFontSizeType = FontSizeType.M;
    private int mHorizontalPaddingSpacing = 0;
    private Path mLeftPath;
    private int mLetterSpacing = 0;
    private LetterSpacingType mLetterSpacingType = LetterSpacingType.XS;
    private float mLineSpacePercentOffset = StaticLayoutUtil.DefaultSpacingadd;
    private int mLineSpacing = 0;
    private LineSpacingType mLineSpacingType = LineSpacingType.S;
    private int mMaxLines = 0;
    private int mMaxTextSize = 0;
    private Paint mPaint;
    private float mRadius;
    private Path mRightPath;
    private float mScale = 1.0f;
    private int mShadowColor;
    private float mShadowDx;
    private float mShadowDy;
    private ShadowType mShadowType = ShadowType.NONE;
    private int mShortLength;
    private TextColorType mTextColorType = TextColorType.White;
    private int mTextPaddingBottom;
    private int mTextPaddingTop;
    private TextPaint mTextPaint;
    private Path mTopPath;
    private int mUnderLinePadding = 50;
    private int mUnderLinerWidth = 2;
    private int mVerticalPaddingSpacing = 0;
    private int mVerticalPos = 0;
    private int mVideoHeight;
    private int mVideoWidth;
    private int mViewHeight;
    private int mViewWidth;
    private int maxLineWidth;
    private CharSequence originalText = "";
    private int startX;
    private int startY;
    private VTFontDesBean vtFontDesBean;

    private interface SizeTester {
        int onTestSize(int i, RectF rectF, List<String> list, List<String> list2);

        int onTestSize(TextPaint textPaint, int i, RectF rectF, List<String> list, List<String> list2);
    }

    public enum AlignType {
        L,
        M,
        R
    }

    public class DecoModel implements Serializable {
        public String align;
        public String fontName;
        public String fontSize;
        public String letterSpace;
        public String lineSpace;
        public String pos;
        public String shadow;
        public String text;
        public String textColor;
    }

    public static class EmojiInfo {
        private int end;
        private String group;
        private int start;

        public EmojiInfo(int start, int end, String group) {
            this.start = start;
            this.end = end;
            this.group = group;
        }

        public int getStart() {
            return this.start;
        }

        public int getEnd() {
            return this.end;
        }

        public String getGroup() {
            return this.group;
        }
    }

    public enum FontSizeType {
        XS,
        S,
        M,
        L,
        XL,
        XXL,
        XL3,
        XL4
    }

    public enum LetterSpacingType {
        XS,
        S,
        M,
        L,
        XL,
        XXL,
        XL3,
        XL4
    }

    public enum LineSpacingType {
        XS,
        S,
        M,
        L,
        XL,
        XXL,
        XL3,
        XL4
    }

    public interface OnFingerIconLocationListener {
        void onLocation(int i, int i2, boolean z);
    }

    public enum ShadowType {
        NONE,
        AROUND,
        SMOOTH,
        UPADDDOWN,
        AROUNDBORDER,
        INLINE,
        SOLID,
        BOX
    }

    public enum TextColorType {
        White,
        Black
    }

    public VTContainerView(Context context) {
        super(context);
        init();
    }

    public VTContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VTContainerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.DafaultMoveYOffset = 1;
        this.mTextPaint = new TextPaint();
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setColor(Color.parseColor("#EEEEEE"));
        this.mTextPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/avenirnextregular.ttf"));
        this.mTextPaint.setStyle(Style.FILL);
        this.mShadowColor = Color.parseColor("#000000");
        this.mTextPaint.setShadowLayer(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, this.mShadowColor);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(Color.parseColor("#000000"));
        this.centerPaint = new Paint();
        this.centerPaint.setStyle(Style.STROKE);
        this.centerPaint.setAntiAlias(true);
        this.centerPaint.setStrokeWidth(1.0f);
        this.centerPaint.setColor(Color.parseColor("#ffffff"));
        this.centerPaint.setPathEffect(new DashPathEffect(new float[]{10.0f, 5.0f}, StaticLayoutUtil.DefaultSpacingadd));
        this.mBottomPath = new Path();
        this.mTopPath = new Path();
        this.mLeftPath = new Path();
        this.mRightPath = new Path();
        setLetterSpacingType(LetterSpacingType.XS);
        setLineSpacingType(LineSpacingType.S);
        this._sizeTester = new SizeTester() {
            final RectF textRect = new RectF();

            public int onTestSize(TextPaint paint, int suggestedSize, RectF availableSpace, List<String> list, List<String> list2) {
                return 0;
            }

            public int onTestSize(int suggestedSize, RectF availableSpace, List<String> curMaxWidthLineText, List<String> curMaxTextNumLineText) {
                if (curMaxWidthLineText == null || curMaxTextNumLineText == null) {
                    return 0;
                }
                int index;
                float maxWidth;
                this.textRect.left = StaticLayoutUtil.DefaultSpacingadd;
                this.textRect.top = StaticLayoutUtil.DefaultSpacingadd;
                TextPaint textPaint = new TextPaint(VTContainerView.this.mTextPaint);
                textPaint.setTextSize(TypedValue.applyDimension(0, (float) suggestedSize, VTContainerView.this.getContext().getResources().getDisplayMetrics()));
                float letterSpacing = VTContainerView.this.processLetterSpacing(suggestedSize);
                float curMaxWidthLineTextWidth = -letterSpacing;
                for (index = 0; index < curMaxWidthLineText.size(); index++) {
                    curMaxWidthLineTextWidth += textPaint.measureText((String) curMaxWidthLineText.get(index)) + letterSpacing;
                }
                float curMaxTextNumLineTextWidth = -letterSpacing;
                for (index = 0; index < curMaxTextNumLineText.size(); index++) {
                    curMaxTextNumLineTextWidth += textPaint.measureText((String) curMaxTextNumLineText.get(index)) + letterSpacing;
                }
                if (curMaxWidthLineTextWidth > curMaxTextNumLineTextWidth) {
                    maxWidth = curMaxWidthLineTextWidth;
                } else {
                    maxWidth = curMaxTextNumLineTextWidth;
                }
                this.textRect.right = VTContainerView.this.getAllShadowOffset(suggestedSize) + maxWidth;
                int lineSpacing = (int) VTContainerView.this.processLineSpacing(suggestedSize);
                int verticalPaddingSpacing = (int) (((float) suggestedSize) * 0.5f);
                int borderWidth = Math.round(((((1.0f * VTContainerView.this.mScale) * ((float) VTContainerView.this.mDefaultBorderWidth)) * ((float) suggestedSize)) * 10.0f) / ((float) VTContainerView.this.mShortLength));
                int underLinerWidth = Math.round(((((1.0f * VTContainerView.this.mScale) * 2.0f) * ((float) suggestedSize)) * 10.0f) / ((float) VTContainerView.this.mShortLength));
                int underLinePadding = Math.round(((((1.0f * VTContainerView.this.mScale) * 50.0f) * ((float) suggestedSize)) * 10.0f) / ((float) VTContainerView.this.mShortLength));
                if (borderWidth < 1) {
                    borderWidth = 1;
                }
                if (underLinerWidth < 1) {
                    underLinerWidth = 1;
                }
                int allLineHeight = (VTContainerView.this.mFinalTexts.size() * ((int) (Math.abs(textPaint.ascent()) + Math.abs(textPaint.descent())))) + ((VTContainerView.this.mFinalTexts.size() - 1) * lineSpacing);
                if (VTContainerView.this.mShadowType == ShadowType.INLINE) {
                    if (VTContainerView.this.mMaxLines <= 1) {
                        allLineHeight += (lineSpacing + underLinerWidth) + underLinePadding;
                    } else {
                        allLineHeight += ((lineSpacing + underLinerWidth) + (underLinePadding * 2)) * (VTContainerView.this.mMaxLines - 1);
                    }
                }
                int realBorderWidth = 0;
                int realVerticalPaddingSpacing = 0;
//                switch (AnonymousClass4.$SwitchMap$com$blink$academy$onetake$widgets$VideoText$VTContainerView$ShadowType[VTContainerView.this.mShadowType.ordinal()]) {
                switch (mShadowType){
                    case NONE:
                    case AROUND:
                        realBorderWidth = borderWidth;
                        realVerticalPaddingSpacing = verticalPaddingSpacing;
                        break;
                    case SMOOTH:
                        realVerticalPaddingSpacing = verticalPaddingSpacing;
                        break;
                }
                allLineHeight += (realBorderWidth * 2) + (realVerticalPaddingSpacing * 2);
                this.textRect.bottom = (float) allLineHeight;
                if (availableSpace.contains(this.textRect)) {
                    return -1;
                }
                return 1;
            }
        };
    }

    public void setText(CharSequence text) {
        this.originalText = text;
        applySpacing();
    }

    private static List<String> getLineText(String lineText) {
        return getLineText(lineText, true);
    }

    public static List<String> getLineText(String lineText, boolean isAddEmoji) {
        List<String> lineTextList = new ArrayList();
        if (lineText == null || lineText.length() == 0) {
            lineTextList.add("");
        } else {
            List<EmojiInfo> emojiCharacters = new ArrayList();
            Matcher matcher = EmojiUtil.PATTERN.matcher(lineText);
            while (matcher.find()) {
                emojiCharacters.add(new EmojiInfo(matcher.start(), matcher.end(), matcher.group()));
            }
            int index;
            if (emojiCharacters.size() > 0) {
                int emojiCount = emojiCharacters.size();
                int curEmojiInfiIndex = 0;
                int start = ((EmojiInfo) emojiCharacters.get(0)).getStart();
                int end = ((EmojiInfo) emojiCharacters.get(0)).getEnd();
                index = 0;
                while (index < lineText.length()) {
                    if (start > index || index >= end) {
                        lineTextList.add(String.valueOf(lineText.charAt(index)));
                    } else {
                        if (isAddEmoji) {
                            lineTextList.add(lineText.substring(start, end));
                        }
                        index = end - 1;
                        curEmojiInfiIndex++;
                        if (curEmojiInfiIndex < emojiCount) {
                            start = ((EmojiInfo) emojiCharacters.get(curEmojiInfiIndex)).getStart();
                            end = ((EmojiInfo) emojiCharacters.get(curEmojiInfiIndex)).getEnd();
                        }
                    }
                    index++;
                }
            } else {
                for (index = 0; index < lineText.length(); index++) {
                    lineTextList.add(String.valueOf(lineText.charAt(index)));
                }
            }
            LogUtil.d("emoji", "lineTextList length: " + lineTextList.size());
        }
        return lineTextList;
    }

    public CharSequence getText() {
        return this.originalText;
    }

    @SuppressLint({"DrawAllocation"})
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        this.startX = width;
        if (this.originalText != null && this.originalText.length() != 0 && this.mFinalTexts != null && this.mFinalTexts.size() != 0) {
            int curLineDrawWidth;
            int i;
            List<String> lineTextList;
            int lineHeight = (int) (Math.abs(this.mTextPaint.ascent()) + Math.abs(this.mTextPaint.descent()));
            int allLineHeight = (this.mFinalTexts.size() * lineHeight) + ((this.mFinalTexts.size() - 1) * this.mLineSpacing);
            if (this.mShadowType == ShadowType.INLINE) {
                if (this.mMaxLines <= 1) {
                    allLineHeight += (this.mLineSpacing + this.mUnderLinerWidth) + this.mUnderLinePadding;
                } else {
                    allLineHeight += ((this.mLineSpacing + this.mUnderLinerWidth) + (this.mUnderLinePadding * 2)) * (this.mMaxLines - 1);
                }
            }
            this.allHeight = allLineHeight;
            int y = (int) ((((float) ((height - allLineHeight) / 2)) + Math.abs(this.mTextPaint.ascent())) + StaticLayoutUtil.DefaultSpacingadd);
            int curLineWidth = this.mBorderWidth;
            switch (this.mShadowType) {
                case UPADDDOWN:
                case NONE:
                case SOLID:
                case SMOOTH:
                case AROUND:
                case INLINE:
                    this.mHorizontalPaddingSpacing = 0;
                    curLineWidth = 0;
                    break;
                case BOX:
                    curLineWidth = 0;
                    break;
            }
            int maxLineDrawWidth = 0;
            for (List<String> lineTextList2 : this.mFinalTexts) {
                curLineDrawWidth = -this.mLetterSpacing;
                for (i = 0; i < lineTextList2.size(); i++) {
                    curLineDrawWidth = (int) ((this.mTextPaint.measureText((String) lineTextList2.get(i)) + ((float) this.mLetterSpacing)) + ((float) curLineDrawWidth));
                }
                if (curLineDrawWidth > maxLineDrawWidth) {
                    maxLineDrawWidth = curLineDrawWidth;
                }
            }
            this.maxLineWidth = maxLineDrawWidth;
            int rectLeft = 0;
            switch (this.mAlignType) {
                case L:
                    if (this.mShadowDx <= this.mRadius / 2.0f) {
                        rectLeft = (int) ((((float) ((this.mShortLength / 10) + curLineWidth)) + (this.mRadius / 2.0f)) - this.mShadowDx);
                        break;
                    } else {
                        rectLeft = (this.mShortLength / 10) + curLineWidth;
                        break;
                    }
                case M:
                    rectLeft = ((width - maxLineDrawWidth) / 2) - this.mHorizontalPaddingSpacing;
                    break;
                case R:
                    rectLeft = (int) (((float) ((((width - maxLineDrawWidth) - (this.mShortLength / 10)) - (this.mHorizontalPaddingSpacing * 2)) - curLineWidth)) - (this.mShadowDx + (this.mRadius / 2.0f)));
                    break;
            }
            int verticalCenter = y;
            y += this.mVerticalPos;
            int borderWidth = 0;
            int verticalPaddingSpacing = 0;
            switch (this.mShadowType) {
                case UPADDDOWN:
                case AROUNDBORDER:
                    borderWidth = this.mBorderWidth;
                    verticalPaddingSpacing = this.mVerticalPaddingSpacing;
                    break;
                case BOX:
                    verticalPaddingSpacing = this.mVerticalPaddingSpacing;
                    break;
            }
            int rectTop = (int) ((((float) y) - (Math.abs(this.mTextPaint.ascent()) + StaticLayoutUtil.DefaultSpacingadd)) - ((float) verticalPaddingSpacing));
            if (rectTop < (this.mShortLength / 10) + borderWidth) {
                rectTop = (this.mShortLength / 10) + borderWidth;
                this.mVerticalPos = ((int) (((((float) rectTop) + Math.abs(this.mTextPaint.ascent())) + StaticLayoutUtil.DefaultSpacingadd) + ((float) verticalPaddingSpacing))) - verticalCenter;
            }
            if ((rectTop + allLineHeight) + (verticalPaddingSpacing * 2) > (height - (this.mShortLength / 10)) - borderWidth) {
                this.mVerticalPos = ((int) (((((float) ((((height - (this.mShortLength / 10)) - borderWidth) - allLineHeight) - (verticalPaddingSpacing * 2))) + Math.abs(this.mTextPaint.ascent())) + StaticLayoutUtil.DefaultSpacingadd) + ((float) verticalPaddingSpacing))) - verticalCenter;
            }
            this.mVerticalPos = (int) (Math.ceil((double) (this.mVerticalPos / this.DafaultMoveYOffset)) * ((double) this.DafaultMoveYOffset));
            y = verticalCenter + this.mVerticalPos;
            this.startY = (int) (((float) y) - (Math.abs(this.mTextPaint.ascent()) + StaticLayoutUtil.DefaultSpacingadd));
            rectTop = (int) ((((float) y) - (Math.abs(this.mTextPaint.ascent()) + StaticLayoutUtil.DefaultSpacingadd)) - ((float) verticalPaddingSpacing));
            int rectBottom = (rectTop + allLineHeight) + (verticalPaddingSpacing * 2);
            switch (this.mShadowType) {
                case UPADDDOWN:
                    switch (this.mTextColorType) {
                        case White:
                            this.mPaint.setColor(Color.parseColor("#EEEEEE"));
                            break;
                        case Black:
                            this.mPaint.setColor(Color.parseColor("#131211"));
                            break;
                    }
                    canvas.drawRect((float) rectLeft, (float) (rectTop - this.mBorderWidth), (float) (rectLeft + maxLineDrawWidth), (float) rectTop, this.mPaint);
                    canvas.drawRect((float) rectLeft, (float) rectBottom, (float) (rectLeft + maxLineDrawWidth), (float) (this.mBorderWidth + rectBottom), this.mPaint);
                    break;
                case AROUNDBORDER:
                    switch (this.mTextColorType) {
                        case White:
                            this.mPaint.setColor(Color.parseColor("#EEEEEE"));
                            break;
                        case Black:
                            this.mPaint.setColor(Color.parseColor("#131211"));
                            break;
                    }
                    canvas.drawRect((float) (rectLeft - this.mBorderWidth), (float) (rectTop - this.mBorderWidth), (float) rectLeft, (float) ((((this.mVerticalPaddingSpacing * 2) + rectTop) + allLineHeight) + this.mBorderWidth), this.mPaint);
                    canvas.drawRect((float) rectLeft, (float) (rectTop - this.mBorderWidth), (float) ((rectLeft + maxLineDrawWidth) + (this.mHorizontalPaddingSpacing * 2)), (float) rectTop, this.mPaint);
                    canvas.drawRect((float) ((rectLeft + maxLineDrawWidth) + (this.mHorizontalPaddingSpacing * 2)), (float) (rectTop - this.mBorderWidth), (float) (((rectLeft + maxLineDrawWidth) + (this.mHorizontalPaddingSpacing * 2)) + this.mBorderWidth), (float) ((((this.mVerticalPaddingSpacing * 2) + rectTop) + allLineHeight) + this.mBorderWidth), this.mPaint);
                    canvas.drawRect((float) rectLeft, (float) rectBottom, (float) ((rectLeft + maxLineDrawWidth) + (this.mHorizontalPaddingSpacing * 2)), (float) (this.mBorderWidth + rectBottom), this.mPaint);
                    break;
                case BOX:
                    switch (this.mTextColorType) {
                        case White:
                            this.mPaint.setColor(Color.parseColor("#131211"));
                            break;
                        case Black:
                            this.mPaint.setColor(Color.parseColor("#EEEEEE"));
                            break;
                    }
                    canvas.drawRect((float) rectLeft, (float) rectTop, (float) ((rectLeft + maxLineDrawWidth) + (this.mHorizontalPaddingSpacing * 2)), (float) rectBottom, this.mPaint);
                    break;
                case INLINE:
                    switch (this.mTextColorType) {
                        case White:
                            this.mPaint.setColor(Color.parseColor("#EEEEEE"));
                            break;
                        case Black:
                            this.mPaint.setColor(Color.parseColor("#131211"));
                            break;
                    }
                    break;
            }
            int index = 0;
            while (index < this.mFinalTexts.size()) {
                List<String> lineTextList2 = (List) this.mFinalTexts.get(index);
                int textLength = lineTextList2.size();
                int x = 0;
                switch (this.mAlignType) {
                    case L:
                        if (this.mShadowDx <= this.mRadius / 2.0f) {
                            x = (int) ((((float) (((this.mShortLength / 10) + this.mHorizontalPaddingSpacing) + curLineWidth)) + (this.mRadius / 2.0f)) - this.mShadowDx);
                            break;
                        } else {
                            x = ((this.mShortLength / 10) + this.mHorizontalPaddingSpacing) + curLineWidth;
                            break;
                        }
                    case M:
                        curLineDrawWidth = -this.mLetterSpacing;
                        for (i = 0; i < textLength; i++) {
                            curLineDrawWidth = (int) ((this.mTextPaint.measureText((String) lineTextList2.get(i)) + ((float) this.mLetterSpacing)) + ((float) curLineDrawWidth));
                        }
                        x = (width - curLineDrawWidth) / 2;
                        break;
                    case R:
                        curLineDrawWidth = -this.mLetterSpacing;
                        for (i = 0; i < textLength; i++) {
                            curLineDrawWidth = (int) ((this.mTextPaint.measureText((String) lineTextList2.get(i)) + ((float) this.mLetterSpacing)) + ((float) curLineDrawWidth));
                        }
                        x = (int) (((float) ((((width - curLineDrawWidth) - (this.mShortLength / 10)) - this.mHorizontalPaddingSpacing) - curLineWidth)) - (this.mShadowDx + (this.mRadius / 2.0f)));
                        break;
                }
                if (x < this.startX) {
                    this.startX = x;
                }
                for (i = 0; i < textLength; i++) {
                    canvas.drawText((String) lineTextList2.get(i), (float) x, (float) y, this.mTextPaint);
                    x = (int) ((this.mTextPaint.measureText((String) lineTextList2.get(i)) + ((float) this.mLetterSpacing)) + ((float) x));
                }
                if (this.mShadowType == ShadowType.INLINE) {
                    int curY = (int) (((((((float) y) - Math.abs(this.mTextPaint.ascent())) - StaticLayoutUtil.DefaultSpacingadd) + ((float) lineHeight)) + ((float) this.mLineSpacing)) + ((float) this.mUnderLinePadding));
                    if (this.mMaxLines == 1 || index != this.mFinalTexts.size() - 1) {
                        canvas.drawRect((float) rectLeft, (float) curY, (float) (rectLeft + maxLineDrawWidth), (float) (this.mUnderLinerWidth + curY), this.mPaint);
                    }
                    y += (this.mUnderLinerWidth + this.mLineSpacing) + (this.mUnderLinePadding * 2);
                }
                y += (this.mLineSpacing + lineHeight) + 0;
                index++;
            }
            if (this.isEdit) {
                this.mLeftPath.reset();
                this.mRightPath.reset();
                this.mTopPath.reset();
                this.mBottomPath.reset();
                this.mTopPath.moveTo((float) this.startX, (float) this.startY);
                this.mTopPath.lineTo((float) (this.startX + this.maxLineWidth), (float) this.startY);
                this.mBottomPath.moveTo((float) this.startX, (float) (this.startY + this.allHeight));
                this.mBottomPath.lineTo((float) (this.startX + this.maxLineWidth), (float) (this.startY + allLineHeight));
                this.mRightPath.moveTo((float) (this.startX + this.maxLineWidth), (float) this.startY);
                this.mRightPath.lineTo((float) (this.startX + this.maxLineWidth), (float) (this.startY + allLineHeight));
                this.mLeftPath.moveTo((float) this.startX, (float) this.startY);
                this.mLeftPath.lineTo((float) this.startX, (float) (this.startY + allLineHeight));
                canvas.drawPath(this.mLeftPath, this.centerPaint);
                canvas.drawPath(this.mRightPath, this.centerPaint);
                canvas.drawPath(this.mTopPath, this.centerPaint);
                canvas.drawPath(this.mBottomPath, this.centerPaint);
            }
        }
    }

    public int getStartY() {
        return this.startY;
    }

    public boolean isEdit() {
        return this.isEdit;
    }

    public void setEdit(boolean edit) {
        this.isEdit = edit;
//        App.runOnUiThread(1.lambdaFactory$(this));
        App.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    public boolean isOnTouchArea(float x, float y, int offset) {
        x -= (float) offset;
        LogUtil.d(TAG, String.format("isOnTouchArea  x : %s , y : %s , startX : %s , startY : %s ,maxLineWidth : %s , allHeight : %s ", new Object[]{Float.valueOf(x), Float.valueOf(y), Integer.valueOf(this.startX), Integer.valueOf(this.startY), Integer.valueOf(this.maxLineWidth), Integer.valueOf(this.allHeight)}));
        if (x < ((float) this.startX) || y < ((float) this.startY) || x > ((float) (this.startX + this.maxLineWidth)) || y > ((float) (this.startY + this.allHeight))) {
            return false;
        }
        return true;
    }

    public void geticonLocations(OnFingerIconLocationListener listener) {
        if (!TextUtil.isNull(getText())) {
            int i;
            int curLineDrawWidth;
            int width = getWidth();
            int height = getHeight();
            int allLineHeight = (this.mFinalTexts.size() * ((int) (Math.abs(this.mTextPaint.ascent()) + Math.abs(this.mTextPaint.descent())))) + ((this.mFinalTexts.size() - 1) * this.mLineSpacing);
            if (this.mShadowType == ShadowType.INLINE) {
                if (this.mMaxLines <= 1) {
                    allLineHeight += (this.mLineSpacing + this.mUnderLinerWidth) + this.mUnderLinePadding;
                } else {
                    allLineHeight += ((this.mLineSpacing + this.mUnderLinerWidth) + (this.mUnderLinePadding * 2)) * (this.mMaxLines - 1);
                }
            }
            int y = (int) ((((float) ((height - allLineHeight) / 2)) + Math.abs(this.mTextPaint.ascent())) + StaticLayoutUtil.DefaultSpacingadd);
            int verticalCenter = y;
            y += this.mVerticalPos;
            int borderWidth = 0;
            int verticalPaddingSpacing = 0;
            switch (this.mShadowType) {
                case UPADDDOWN:
                case AROUNDBORDER:
                    borderWidth = this.mBorderWidth;
                    verticalPaddingSpacing = this.mVerticalPaddingSpacing;
                    break;
                case BOX:
                    verticalPaddingSpacing = this.mVerticalPaddingSpacing;
                    break;
            }
            int rectTop = (int) ((((float) y) - (Math.abs(this.mTextPaint.ascent()) + StaticLayoutUtil.DefaultSpacingadd)) - ((float) verticalPaddingSpacing));
            if (rectTop < (this.mShortLength / 10) + borderWidth) {
                rectTop = (this.mShortLength / 10) + borderWidth;
                this.mVerticalPos = ((int) (((((float) rectTop) + Math.abs(this.mTextPaint.ascent())) + StaticLayoutUtil.DefaultSpacingadd) + ((float) verticalPaddingSpacing))) - verticalCenter;
            }
            if ((rectTop + allLineHeight) + (verticalPaddingSpacing * 2) > (height - (this.mShortLength / 10)) - borderWidth) {
                this.mVerticalPos = ((int) (((((float) ((((height - (this.mShortLength / 10)) - borderWidth) - allLineHeight) - (verticalPaddingSpacing * 2))) + Math.abs(this.mTextPaint.ascent())) + StaticLayoutUtil.DefaultSpacingadd) + ((float) verticalPaddingSpacing))) - verticalCenter;
            }
            this.mVerticalPos = (int) (Math.ceil((double) (this.mVerticalPos / this.DafaultMoveYOffset)) * ((double) this.DafaultMoveYOffset));
            verticalCenter = (height / 2) + 0;
            y = verticalCenter + this.mVerticalPos;
            int maxWidthLineTextWidth = -this.mLetterSpacing;
            for (i = 0; i < this.curMaxWidthLineText.size(); i++) {
                maxWidthLineTextWidth = (int) ((this.mTextPaint.measureText((String) this.curMaxWidthLineText.get(i)) + ((float) this.mLetterSpacing)) + ((float) maxWidthLineTextWidth));
            }
            int curMaxTextNumLineTextWidth = -this.mLetterSpacing;
            for (i = 0; i < this.curMaxTextNumLineText.size(); i++) {
                curMaxTextNumLineTextWidth = (int) ((this.mTextPaint.measureText((String) this.curMaxTextNumLineText.get(i)) + ((float) this.mLetterSpacing)) + ((float) curMaxTextNumLineTextWidth));
            }
            if (maxWidthLineTextWidth > curMaxTextNumLineTextWidth) {
                curLineDrawWidth = maxWidthLineTextWidth;
            } else {
                curLineDrawWidth = curMaxTextNumLineTextWidth;
            }
            switch (this.mShadowType) {
                case AROUNDBORDER:
                    curLineDrawWidth += (this.mHorizontalPaddingSpacing * 2) + (this.mBorderWidth * 2);
                    break;
                case BOX:
                    curLineDrawWidth += this.mHorizontalPaddingSpacing * 2;
                    break;
            }
            int x = 0;
            switch (this.mAlignType) {
                case L:
                    if (this.mShadowDx <= this.mRadius / 2.0f) {
                        x = ((int) ((((this.mRadius / 2.0f) - this.mShadowDx) + ((float) curLineDrawWidth)) / 2.0f)) + (this.mShortLength / 10);
                        break;
                    } else {
                        x = (curLineDrawWidth / 2) + (this.mShortLength / 10);
                        break;
                    }
                case M:
                    x = width / 2;
                    break;
                case R:
                    x = (int) ((((float) width) - ((((float) curLineDrawWidth) + (this.mShadowDx + (this.mRadius / 2.0f))) / 2.0f)) - ((float) (this.mShortLength / 10)));
                    break;
            }
            if (listener != null) {
                listener.onLocation(x, y, verticalCenter == y);
            }
        } else if (listener != null) {
            listener.onLocation(0, 0, false);
        }
    }

    private void applySpacing() {
        if (this.originalText == null || this.originalText.length() == 0 || this.mViewWidth == 0 || this.mViewHeight == 0) {
            this.curMaxWidthLineText = new ArrayList();
            this.curMaxTextNumLineText = new ArrayList();
            this.mFinalTexts = new ArrayList();
            setMaxLines(0);
            invalidate();
            return;
        }
        int index;
        int curMaxTextWidth;
        this.curMaxWidthLineText = new ArrayList();
        this.curMaxTextNumLineText = new ArrayList();
        String[] lineTexts = this.originalText.toString().split("\n");
        this.mFinalTexts = new ArrayList();
        for (String linetext : lineTexts) {
            this.mFinalTexts.add(getLineText(linetext));
        }
        setMaxLines(this.mFinalTexts.size());
        setTextSize(0, this.mMaxTextSize);
        float maxWidth = StaticLayoutUtil.DefaultSpacingadd;
        int maxTextNum = 0;
        for (index = 0; index < this.mMaxLines; index++) {
            StringBuilder curLineText = new StringBuilder("");
            List<String> curLineTextList = (List) this.mFinalTexts.get(index);
            for (String str : curLineTextList) {
                curLineText.append(str);
            }
            float curMaxWidth = this.mTextPaint.measureText(curLineText.toString());
            if (curMaxWidth > maxWidth) {
                maxWidth = curMaxWidth;
                this.curMaxWidthLineText = curLineTextList;
            }
            if (curLineText.length() > maxTextNum) {
                maxTextNum = curLineText.length();
                this.curMaxTextNumLineText = curLineTextList;
            }
        }
        int curMaxWidthLineTextWidth = -this.mLetterSpacing;
        for (index = 0; index < this.curMaxWidthLineText.size(); index++) {
            curMaxWidthLineTextWidth = (int) ((this.mTextPaint.measureText((String) this.curMaxWidthLineText.get(index)) + ((float) this.mLetterSpacing)) + ((float) curMaxWidthLineTextWidth));
        }
        int curMaxTextNumLineTextwidth = -this.mLetterSpacing;
        for (index = 0; index < this.curMaxTextNumLineText.size(); index++) {
            curMaxTextNumLineTextwidth = (int) ((this.mTextPaint.measureText((String) this.curMaxTextNumLineText.get(index)) + ((float) this.mLetterSpacing)) + ((float) curMaxTextNumLineTextwidth));
        }
        int drawMaxWidyth = ((this.mViewWidth - (this.mShortLength / 5)) - getPaddingLeft()) - getPaddingRight();
        int drawMaxHeight = ((this.mViewHeight - (this.mShortLength / 5)) - getPaddingLeft()) - getPaddingRight();
        if (curMaxWidthLineTextWidth > curMaxTextNumLineTextwidth) {
            curMaxTextWidth = curMaxWidthLineTextWidth;
        } else {
            curMaxTextWidth = curMaxTextNumLineTextwidth;
        }
        curMaxTextWidth = (int) (((float) curMaxTextWidth) + getAllShadowOffset(this.mMaxTextSize));
        int allLineHeight = (this.mFinalTexts.size() * ((int) (Math.abs(this.mTextPaint.ascent()) + Math.abs(this.mTextPaint.descent())))) + ((this.mFinalTexts.size() - 1) * this.mLineSpacing);
        if (this.mShadowType == ShadowType.INLINE) {
            if (this.mMaxLines <= 1) {
                allLineHeight += (this.mLineSpacing + this.mUnderLinerWidth) + this.mUnderLinePadding;
            } else {
                allLineHeight += ((this.mLineSpacing + this.mUnderLinerWidth) + (this.mUnderLinePadding * 2)) * (this.mMaxLines - 1);
            }
        }
        int realBorderWidth = 0;
        int realVerticalPaddingSpacing = 0;
        switch (this.mShadowType) {
            case UPADDDOWN:
            case AROUNDBORDER:
                realBorderWidth = this.mBorderWidth;
                realVerticalPaddingSpacing = this.mVerticalPaddingSpacing;
                break;
            case BOX:
                realVerticalPaddingSpacing = this.mVerticalPaddingSpacing;
                break;
        }
        allLineHeight += (realBorderWidth * 2) + (realVerticalPaddingSpacing * 2);
        if (curMaxTextWidth > drawMaxWidyth || allLineHeight > drawMaxHeight) {
            this._availableSpaceRect.left = StaticLayoutUtil.DefaultSpacingadd;
            this._availableSpaceRect.top = StaticLayoutUtil.DefaultSpacingadd;
            this._availableSpaceRect.right = (float) drawMaxWidyth;
            this._availableSpaceRect.bottom = (float) drawMaxHeight;
            superSetTextSize(1, this.curMaxWidthLineText, this.curMaxTextNumLineText);
        }
        invalidate();
    }

    public static int getScaledStartY(Context context, LongVideosModel model, int viewHeight, int viewWidth) {
        int mShortLength;
        int mVerticalPos = model.getTextVerticalPos();
        List<List<String>> mFinalTexts = model.getFinalTexts();
        if (viewHeight > viewWidth) {
            mShortLength = viewWidth;
        } else {
            mShortLength = viewHeight;
        }
        ShadowType mShadowType = model.getTextShadowType();
        float mScale = (1.0f * ((float) mShortLength)) / 1010.0f;
        TextPaint mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.parseColor("#EEEEEE"));
        float textSize = model.getTextSize();
        mTextPaint.setTextSize(TypedValue.applyDimension(0, textSize, context.getResources().getDisplayMetrics()));
        int YOffset = model.getyOffset();
        int mLineSpacing = model.getLineSpacing();
        int mMaxLines = mFinalTexts.size();
        int mUnderLinerWidth = Math.round(((((1.0f * mScale) * 2.0f) * textSize) * 10.0f) / ((float) mShortLength));
        int mUnderLinePadding = Math.round(((((1.0f * mScale) * 50.0f) * textSize) * 10.0f) / ((float) mShortLength));
        if (mUnderLinerWidth < 1) {
            mUnderLinerWidth = 1;
        }
        int allLineHeight = (mFinalTexts.size() * ((int) (Math.abs(mTextPaint.ascent()) + Math.abs(mTextPaint.descent())))) + ((mFinalTexts.size() - 1) * mLineSpacing);
        if (mShadowType == ShadowType.INLINE) {
            if (mMaxLines <= 1) {
                allLineHeight += (mLineSpacing + mUnderLinerWidth) + mUnderLinePadding;
            } else {
                allLineHeight += ((mLineSpacing + mUnderLinerWidth) + (mUnderLinePadding * 2)) * (mMaxLines - 1);
            }
        }
        int mBorderWidth = model.getTextTypeface().borderwidth;
        int borderWidth = 0;
        int verticalPaddingSpacing = 0;
        switch (mShadowType) {
            case UPADDDOWN:
            case AROUNDBORDER:
                borderWidth = mBorderWidth;
                verticalPaddingSpacing = (int) (0.5f * textSize);
                break;
            case BOX:
                verticalPaddingSpacing = (int) (0.5f * textSize);
                break;
        }
        int y = (int) ((((float) ((viewHeight - allLineHeight) / 2)) + Math.abs(mTextPaint.ascent())) + ((float) YOffset));
        int verticalCenter = y;
        int rectTop = (int) ((((float) (y + mVerticalPos)) - (Math.abs(mTextPaint.ascent()) + ((float) YOffset))) - ((float) verticalPaddingSpacing));
        if (rectTop < (mShortLength / 10) + borderWidth) {
            rectTop = (mShortLength / 10) + borderWidth;
            mVerticalPos = ((int) (((((float) rectTop) + Math.abs(mTextPaint.ascent())) + ((float) YOffset)) + ((float) verticalPaddingSpacing))) - verticalCenter;
        }
        if ((rectTop + allLineHeight) + (verticalPaddingSpacing * 2) > (viewHeight - (mShortLength / 10)) - borderWidth) {
            mVerticalPos = ((int) (((((float) ((((viewHeight - (mShortLength / 10)) - borderWidth) - allLineHeight) - (verticalPaddingSpacing * 2))) + Math.abs(mTextPaint.ascent())) + ((float) YOffset)) + ((float) verticalPaddingSpacing))) - verticalCenter;
        }
        return (int) (((float) (verticalCenter + mVerticalPos)) - (Math.abs(mTextPaint.ascent()) + ((float) YOffset)));
    }

    public static float getScaledFontSize(Context context, LongVideosModel model, int viewHeight, int viewWidth) {
        final int mShortLength;
        int index;
        int curMaxTextWidth;
        final List<List<String>> mFinalTexts = model.getFinalTexts();
        int mLetterSpacing = model.getLetterSpacing();
        int mLineSpacing = model.getLineSpacing();
        if (viewHeight > viewWidth) {
            mShortLength = viewWidth;
        } else {
            mShortLength = viewHeight;
        }
        final float mScale = (1.0f * ((float) mShortLength)) / 1010.0f;
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("#EEEEEE"));
        VTFontDesBean vtFontDesBean = model.getTextTypeface();
        final ShadowType mShadowType = model.getTextShadowType();
        final double lineheightoffset = vtFontDesBean.lineheightoffset;
        final LineSpacingType textLineSpacingType = model.getTextLineSpacingType();
        final LetterSpacingType mLetterSpacingType = model.getTextLetterSpacingType();
        final int mBorderWidth = vtFontDesBean.borderwidth;
        final int mMaxLines = mFinalTexts.size();
        textPaint.setTypeface(Typeface.createFromFile(Config.getFontFile(vtFontDesBean.language, vtFontDesBean.filename, vtFontDesBean.fonttype)));
        textPaint.setStyle(Style.FILL);
        textPaint.setShadowLayer(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, model.getShadowColor());
        int textSize = 0;
        switch (model.getTextFontSizeType()) {
            case XS:
                textSize = (int) ((((float) mShortLength) * 0.4f) / 10.0f);
                break;
            case S:
                textSize = (int) ((((float) mShortLength) * 0.7f) / 10.0f);
                break;
            case M:
                textSize = mShortLength / 10;
                break;
            case L:
                textSize = (int) ((((float) mShortLength) * 1.5f) / 10.0f);
                break;
            case XL:
                textSize = (int) ((((float) mShortLength) * 2.0f) / 10.0f);
                break;
            case XXL:
                textSize = (int) ((((float) mShortLength) * 3.0f) / 10.0f);
                break;
            case XL3:
                textSize = (int) ((((float) mShortLength) * 4.5f) / 10.0f);
                break;
            case XL4:
                textSize = (int) ((((float) mShortLength) * 6.0f) / 10.0f);
                break;
        }
        textPaint.setTextSize(TypedValue.applyDimension(0, (float) textSize, context.getResources().getDisplayMetrics()));
        List<String> curMaxWidthLineText = new ArrayList();
        List<String> curMaxTextNumLineText = new ArrayList();
        float maxWidth = StaticLayoutUtil.DefaultSpacingadd;
        int maxTextNum = 0;
        for (index = 0; index < mFinalTexts.size(); index++) {
            StringBuilder stringBuilder = new StringBuilder("");
            List<String> curLineTextList = (List) mFinalTexts.get(index);
            for (String str : curLineTextList) {
                stringBuilder.append(str);
            }
            float curMaxWidth = textPaint.measureText(stringBuilder.toString());
            if (curMaxWidth > maxWidth) {
                maxWidth = curMaxWidth;
                curMaxWidthLineText = curLineTextList;
            }
            if (stringBuilder.length() > maxTextNum) {
                maxTextNum = stringBuilder.length();
                curMaxTextNumLineText = curLineTextList;
            }
        }
        int curMaxWidthLineTextWidth = -mLetterSpacing;
        for (index = 0; index < curMaxWidthLineText.size(); index++) {
            curMaxWidthLineTextWidth = (int) ((textPaint.measureText((String) curMaxWidthLineText.get(index)) + ((float) mLetterSpacing)) + ((float) curMaxWidthLineTextWidth));
        }
        int curMaxTextNumLineTextwidth = -mLetterSpacing;
        for (index = 0; index < curMaxTextNumLineText.size(); index++) {
            curMaxTextNumLineTextwidth = (int) ((textPaint.measureText((String) curMaxTextNumLineText.get(index)) + ((float) mLetterSpacing)) + ((float) curMaxTextNumLineTextwidth));
        }
        int drawMaxWidyth = viewWidth - (mShortLength / 5);
        int drawMaxHeight = viewHeight - (mShortLength / 5);
        if (curMaxWidthLineTextWidth > curMaxTextNumLineTextwidth) {
            curMaxTextWidth = curMaxWidthLineTextWidth;
        } else {
            curMaxTextWidth = curMaxTextNumLineTextwidth;
        }
        curMaxTextWidth = (int) (((float) curMaxTextWidth) + getShadowOffset(mShadowType, mScale, (float) textSize, mShortLength, mBorderWidth));
        int allLineHeight = (mFinalTexts.size() * ((int) (Math.abs(textPaint.ascent()) + Math.abs(textPaint.descent())))) + ((mFinalTexts.size() - 1) * mLineSpacing);
        int realBorderWidth = 0;
        switch (mShadowType) {
            case UPADDDOWN:
            case AROUNDBORDER:
                realBorderWidth = mBorderWidth;
                break;
        }
        allLineHeight += (realBorderWidth * 2) + 0;
        RectF _availableSpaceRect = new RectF();
        _availableSpaceRect.left = StaticLayoutUtil.DefaultSpacingadd;
        _availableSpaceRect.top = StaticLayoutUtil.DefaultSpacingadd;
        _availableSpaceRect.right = (float) drawMaxWidyth;
        _availableSpaceRect.bottom = (float) drawMaxHeight;
        final Context context2 = context;
        SizeTester sizeTester = new SizeTester() {
            final RectF textRect = new RectF();

            public int onTestSize(int suggestedSize, RectF availableSpace, List<String> list, List<String> list2) {
                return 0;
            }

            public int onTestSize(TextPaint paint, int suggestedSize, RectF availableSpace, List<String> curMaxWidthLineText, List<String> curMaxTextNumLineText) {
                if (curMaxWidthLineText == null || curMaxTextNumLineText == null) {
                    return 0;
                }
                int index;
                float maxWidth;
                this.textRect.left = StaticLayoutUtil.DefaultSpacingadd;
                this.textRect.top = StaticLayoutUtil.DefaultSpacingadd;
                TextPaint textPaint = new TextPaint(paint);
                textPaint.setTextSize(TypedValue.applyDimension(0, (float) suggestedSize, context2.getResources().getDisplayMetrics()));
                float letterSpacingPercent = StaticLayoutUtil.DefaultSpacingadd;
//                switch (AnonymousClass4.$SwitchMap$com$blink$academy$onetake$widgets$VideoText$VTContainerView$LetterSpacingType[mLetterSpacingType.ordinal()]) {
                switch (mLetterSpacingType){
                    case XS:
                        letterSpacingPercent = StaticLayoutUtil.DefaultSpacingadd;
                        break;
                    case S:
                        letterSpacingPercent = 0.2f;
                        break;
                    case M:
                        letterSpacingPercent = 0.5f;
                        break;
                    case L:
                        letterSpacingPercent = 1.0f;
                        break;
                    case XL:
                        letterSpacingPercent = 1.5f;
                        break;
                    case XXL:
                        letterSpacingPercent = 2.0f;
                        break;
                    case XL3:
                        letterSpacingPercent = 2.5f;
                        break;
                    case XL4:
                        letterSpacingPercent = 3.0f;
                        break;
                }
                float letterSpacing = letterSpacingPercent * ((float) suggestedSize);
                float curMaxWidthLineTextWidth = -letterSpacing;
                for (index = 0; index < curMaxWidthLineText.size(); index++) {
                    curMaxWidthLineTextWidth += textPaint.measureText((String) curMaxWidthLineText.get(index)) + letterSpacing;
                }
                float curMaxTextNumLineTextWidth = -letterSpacing;
                for (index = 0; index < curMaxTextNumLineText.size(); index++) {
                    curMaxTextNumLineTextWidth += textPaint.measureText((String) curMaxTextNumLineText.get(index)) + letterSpacing;
                }
                if (curMaxWidthLineTextWidth > curMaxTextNumLineTextWidth) {
                    maxWidth = curMaxWidthLineTextWidth;
                } else {
                    maxWidth = curMaxTextNumLineTextWidth;
                }
                this.textRect.right = VTContainerView.getShadowOffset(mShadowType, mScale, (float) suggestedSize, mShortLength, mBorderWidth) + maxWidth;
                int lineSpacing = (int) VTContainerView.proLineSpacing(textLineSpacingType, suggestedSize, lineheightoffset);
                int verticalPaddingSpacing = (int) (((float) suggestedSize) * 0.5f);
                int borderWidth = Math.round(((((1.0f * mScale) * ((float) mBorderWidth)) * ((float) suggestedSize)) * 10.0f) / ((float) mShortLength));
                int underLinerWidth = Math.round(((((1.0f * mScale) * 2.0f) * ((float) suggestedSize)) * 10.0f) / ((float) mShortLength));
                int underLinePadding = Math.round(((((1.0f * mScale) * 50.0f) * ((float) suggestedSize)) * 10.0f) / ((float) mShortLength));
                if (borderWidth < 1) {
                    borderWidth = 1;
                }
                if (underLinerWidth < 1) {
                    underLinerWidth = 1;
                }
                int allLineHeight = (mFinalTexts.size() * ((int) (Math.abs(textPaint.ascent()) + Math.abs(textPaint.descent())))) + ((mFinalTexts.size() - 1) * lineSpacing);
                if (mShadowType == ShadowType.INLINE) {
                    if (mMaxLines <= 1) {
                        allLineHeight += (lineSpacing + underLinerWidth) + underLinePadding;
                    } else {
                        allLineHeight += ((lineSpacing + underLinerWidth) + (underLinePadding * 2)) * (mMaxLines - 1);
                    }
                }
                int realBorderWidth = 0;
                int realVerticalPaddingSpacing = 0;
//                switch (AnonymousClass4.$SwitchMap$com$blink$academy$onetake$widgets$VideoText$VTContainerView$ShadowType[mShadowType.ordinal()]) {
                switch (mShadowType){
                    case NONE:
                    case AROUND:
                        realBorderWidth = borderWidth;
                        realVerticalPaddingSpacing = verticalPaddingSpacing;
                        break;
                    case SMOOTH:
                        realVerticalPaddingSpacing = verticalPaddingSpacing;
                        break;
                }
                allLineHeight += (realBorderWidth * 2) + (realVerticalPaddingSpacing * 2);
                this.textRect.bottom = (float) allLineHeight;
                if (availableSpace.contains(this.textRect)) {
                    return -1;
                }
                return 1;
            }
        };
        if (curMaxTextWidth > drawMaxWidyth || allLineHeight > drawMaxHeight) {
            return sSTextSize(textPaint, sizeTester, 1, textSize, _availableSpaceRect, curMaxWidthLineText, curMaxTextNumLineText);
        }
        return (float) textSize;
    }

    private static float sSTextSize(TextPaint paint, SizeTester sizeTester, int start, int end, RectF availableSpace, List<String> curMaxWidthLineText, List<String> curMaxTextNumLineText) {
        int textSize = start;
        int lo = start;
        int hi = end - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int midValCmp = sizeTester.onTestSize(paint, mid, availableSpace, curMaxWidthLineText, curMaxTextNumLineText);
            if (midValCmp < 0) {
                textSize = lo;
                lo = mid + 1;
            } else if (midValCmp <= 0) {
                return (float) mid;
            } else {
                hi = mid - 1;
                textSize = hi;
            }
        }
        return (float) textSize;
    }

    private static float getShadowOffset(ShadowType mShadowType, float mScale, float textSize, int mShortLength, int mDefaultBorderWidth) {
        float radius = StaticLayoutUtil.DefaultSpacingadd;
        float dx = StaticLayoutUtil.DefaultSpacingadd;
        float scale;
        switch (mShadowType) {
            case UPADDDOWN:
                return StaticLayoutUtil.DefaultSpacingadd;
            case AROUNDBORDER:
                int borderWidth = Math.round(((((1.0f * mScale) * ((float) mDefaultBorderWidth)) * textSize) * 10.0f) / ((float) mShortLength));
                if (borderWidth < 1) {
                    borderWidth = 1;
                }
                return (((float) borderWidth) + (0.5f * textSize)) * 2.0f;
            case BOX:
                return (0.5f * textSize) * 2.0f;
            case NONE:
                radius = StaticLayoutUtil.DefaultSpacingadd;
                dx = StaticLayoutUtil.DefaultSpacingadd;
                break;
            case SOLID:
                scale = ((1.0f * mScale) * textSize) / (((float) mShortLength) / 10.0f);
                radius = 0.5f * scale;
                if (StaticLayoutUtil.DefaultSpacingadd < radius && radius < 1.0f) {
                    radius = 1.0f;
                }
                if (radius > 25.0f) {
                    radius = 25.0f;
                }
                radius = (float) Math.round(radius);
                dx = (float) Math.round(2.0f * scale);
                break;
            case SMOOTH:
                scale = ((1.0f * mScale) * textSize) / (((float) mShortLength) / 10.0f);
                radius = 2.0f * scale;
                if (StaticLayoutUtil.DefaultSpacingadd < radius && radius < 1.0f) {
                    radius = 1.0f;
                }
                if (radius > 25.0f) {
                    radius = 25.0f;
                }
                radius = (float) Math.round(radius);
                dx = (float) Math.round(1.0f * scale);
                break;
            case AROUND:
                radius = 25.0f * (((1.0f * mScale) * textSize) / (((float) mShortLength) / 10.0f));
                if (StaticLayoutUtil.DefaultSpacingadd < radius && radius < 1.0f) {
                    radius = 1.0f;
                }
                if (radius > 25.0f) {
                    radius = 25.0f;
                }
                radius = (float) Math.round(radius);
                dx = StaticLayoutUtil.DefaultSpacingadd;
                break;
            case INLINE:
                return StaticLayoutUtil.DefaultSpacingadd;
        }
        if (dx > radius / 2.0f) {
            return (radius / 2.0f) + dx;
        }
        return radius;
    }

    private void superSetTextSize(int startSize, List<String> curMaxWidthLineText, List<String> curMaxTextNumLineText) {
        setTextSize(0, binarySearch(startSize, this.mMaxTextSize, this._sizeTester, this._availableSpaceRect, curMaxWidthLineText, curMaxTextNumLineText));
    }

    private int binarySearch(int start, int end, SizeTester sizeTester, RectF availableSpace, List<String> curMaxWidthLineText, List<String> curMaxTextNumLineText) {
        int lastBest = start;
        int lo = start;
        int hi = end - 1;
        while (lo <= hi) {
            int i = (lo + hi) >>> 1;
            int midValCmp = sizeTester.onTestSize(i, availableSpace, curMaxWidthLineText, curMaxTextNumLineText);
            if (midValCmp < 0) {
                lastBest = lo;
                lo = i + 1;
            } else if (midValCmp <= 0) {
                return i;
            } else {
                hi = i - 1;
                lastBest = hi;
            }
        }
        return lastBest;
    }

    private void setTextSize(int complexUnitPx, int textSize) {
        int i = 1;
        this.mTextPaint.setTextSize(TypedValue.applyDimension(complexUnitPx, (float) textSize, getContext().getResources().getDisplayMetrics()));
        this.mLetterSpacing = (int) processLetterSpacing(textSize);
        this.mLineSpacing = (int) processLineSpacing(textSize);
        this.mHorizontalPaddingSpacing = (int) (((float) textSize) * 0.5f);
        this.mVerticalPaddingSpacing = (int) (((float) textSize) * 0.5f);
        this.mBorderWidth = Math.round(((((this.mScale * 1.0f) * ((float) this.mDefaultBorderWidth)) * ((float) textSize)) * 10.0f) / ((float) this.mShortLength));
        this.mUnderLinerWidth = Math.round(((((this.mScale * 1.0f) * 2.0f) * ((float) textSize)) * 10.0f) / ((float) this.mShortLength));
        this.mUnderLinePadding = Math.round(((((this.mScale * 1.0f) * 50.0f) * ((float) textSize)) * 10.0f) / ((float) this.mShortLength));
        this.mBorderWidth = this.mBorderWidth < 1 ? 1 : this.mBorderWidth;
        if (this.mUnderLinerWidth >= 1) {
            i = this.mUnderLinerWidth;
        }
        this.mUnderLinerWidth = i;
        setShadowType(textSize, this.mShadowType);
    }

    private void setLetterSpacing(int textSize, float letterSpacingPercent) {
        this.mLetterSpacing = (int) (((float) textSize) * letterSpacingPercent);
    }

    private float processLetterSpacing(int textSize) {
        float letterSpacingPercent = StaticLayoutUtil.DefaultSpacingadd;
        switch (this.mLetterSpacingType) {
            case XS:
                letterSpacingPercent = StaticLayoutUtil.DefaultSpacingadd;
                break;
            case S:
                letterSpacingPercent = 0.2f;
                break;
            case M:
                letterSpacingPercent = 0.5f;
                break;
            case L:
                letterSpacingPercent = 1.0f;
                break;
            case XL:
                letterSpacingPercent = 1.5f;
                break;
            case XXL:
                letterSpacingPercent = 2.0f;
                break;
            case XL3:
                letterSpacingPercent = 2.5f;
                break;
            case XL4:
                letterSpacingPercent = 3.0f;
                break;
        }
        return ((float) textSize) * letterSpacingPercent;
    }

    public void setLetterSpacingType(LetterSpacingType letterSpacingType) {
        this.mLetterSpacingType = letterSpacingType;
        switch (this.mLetterSpacingType) {
            case XS:
                setLetterSpacing(this.mMaxTextSize, StaticLayoutUtil.DefaultSpacingadd);
                break;
            case S:
                setLetterSpacing(this.mMaxTextSize, 0.2f);
                break;
            case M:
                setLetterSpacing(this.mMaxTextSize, 0.5f);
                break;
            case L:
                setLetterSpacing(this.mMaxTextSize, 1.0f);
                break;
            case XL:
                setLetterSpacing(this.mMaxTextSize, 1.5f);
                break;
            case XXL:
                setLetterSpacing(this.mMaxTextSize, 2.0f);
                break;
            case XL3:
                setLetterSpacing(this.mMaxTextSize, 2.5f);
                break;
            case XL4:
                setLetterSpacing(this.mMaxTextSize, 3.0f);
                break;
        }
        applySpacing();
    }

    public void setLineSpacing(int textSize, float lineSpacingPercent) {
        this.mLineSpacing = (int) (((float) textSize) * lineSpacingPercent);
    }

    public float processLineSpacing(int textSize) {
        float lineSpacingPercent = StaticLayoutUtil.DefaultSpacingadd;
        switch (this.mLineSpacingType) {
            case XS:
                lineSpacingPercent = -0.2f;
                break;
            case S:
                lineSpacingPercent = StaticLayoutUtil.DefaultSpacingadd;
                break;
            case M:
                lineSpacingPercent = 0.2f;
                break;
            case L:
                lineSpacingPercent = 0.5f;
                break;
            case XL:
                lineSpacingPercent = 1.0f;
                break;
            case XXL:
                lineSpacingPercent = 1.5f;
                break;
            case XL3:
                lineSpacingPercent = 2.0f;
                break;
            case XL4:
                lineSpacingPercent = 3.0f;
                break;
        }
        return ((float) textSize) * (lineSpacingPercent + this.mLineSpacePercentOffset);
    }

    private static float proLineSpacing(LineSpacingType mLineSpacingType, int textSize, double mLineSpacePercentOffset) {
        float lineSpacingPercent = StaticLayoutUtil.DefaultSpacingadd;
        switch (mLineSpacingType) {
            case XS:
                lineSpacingPercent = -0.2f;
                break;
            case S:
                lineSpacingPercent = StaticLayoutUtil.DefaultSpacingadd;
                break;
            case M:
                lineSpacingPercent = 0.2f;
                break;
            case L:
                lineSpacingPercent = 0.5f;
                break;
            case XL:
                lineSpacingPercent = 1.0f;
                break;
            case XXL:
                lineSpacingPercent = 1.5f;
                break;
            case XL3:
                lineSpacingPercent = 2.0f;
                break;
            case XL4:
                lineSpacingPercent = 3.0f;
                break;
        }
        return ((float) textSize) * ((float) (((double) lineSpacingPercent) + mLineSpacePercentOffset));
    }

    public void setLineSpacingType(LineSpacingType lineSpacingType) {
        this.mLineSpacingType = lineSpacingType;
        float lineSpacingPercent = StaticLayoutUtil.DefaultSpacingadd;
        switch (this.mLineSpacingType) {
            case XS:
                lineSpacingPercent = -0.2f;
                break;
            case S:
                lineSpacingPercent = StaticLayoutUtil.DefaultSpacingadd;
                break;
            case M:
                lineSpacingPercent = 0.2f;
                break;
            case L:
                lineSpacingPercent = 0.5f;
                break;
            case XL:
                lineSpacingPercent = 1.0f;
                break;
            case XXL:
                lineSpacingPercent = 1.5f;
                break;
            case XL3:
                lineSpacingPercent = 2.0f;
                break;
            case XL4:
                lineSpacingPercent = 3.0f;
                break;
        }
        setLineSpacing(this.mMaxTextSize, lineSpacingPercent + this.mLineSpacePercentOffset);
        applySpacing();
    }

    @SuppressLint({"RtlHardcoded"})
    public void setAlignType(AlignType alignType) {
        this.mAlignType = alignType;
        invalidate();
    }

    public void setVerticalPos(int verticalPos) {
        this.mVerticalPos = verticalPos;
        invalidate();
    }

    public void setTextAlign(AlignType alignType, int verticalPos) {
        this.mAlignType = alignType;
        this.mVerticalPos = verticalPos;
        invalidate();
    }

    @SuppressLint({"RtlHardcoded"})
    public void setTextColorType(TextColorType textColorType) {
        this.mTextColorType = textColorType;
        switch (this.mTextColorType) {
            case White:
                this.mTextPaint.setColor(Color.parseColor("#EEEEEE"));
                break;
            case Black:
                this.mTextPaint.setColor(Color.parseColor("#131211"));
                break;
        }
        applySpacing();
    }

    public static int getTextSize(int shortLenth, FontSizeType fontSizeType) {
        switch (fontSizeType) {
            case XS:
                return (int) ((((float) shortLenth) * 0.4f) / 10.0f);
            case S:
                return (int) ((((float) shortLenth) * 0.7f) / 10.0f);
            case M:
                return shortLenth / 10;
            case L:
                return (int) ((((float) shortLenth) * 1.5f) / 10.0f);
            case XL:
                return (int) ((((float) shortLenth) * 2.0f) / 10.0f);
            case XXL:
                return (int) ((((float) shortLenth) * 3.0f) / 10.0f);
            case XL3:
                return (int) ((((float) shortLenth) * 4.5f) / 10.0f);
            case XL4:
                return (int) ((((float) shortLenth) * 6.0f) / 10.0f);
            default:
                return 0;
        }
    }

    public void setFontSizeType(int shortLenth, FontSizeType fontSizeType) {
        this.mFontSizeType = fontSizeType;
        this.mShortLength = shortLenth;
        this.mScale = (1.0f * ((float) this.mShortLength)) / 1010.0f;
        if (shortLenth != 0) {
            int textSize = 0;
            switch (this.mFontSizeType) {
                case XS:
                    textSize = (int) ((((float) shortLenth) * 0.4f) / 10.0f);
                    break;
                case S:
                    textSize = (int) ((((float) shortLenth) * 0.7f) / 10.0f);
                    break;
                case M:
                    textSize = shortLenth / 10;
                    break;
                case L:
                    textSize = (int) ((((float) shortLenth) * 1.5f) / 10.0f);
                    break;
                case XL:
                    textSize = (int) ((((float) shortLenth) * 2.0f) / 10.0f);
                    break;
                case XXL:
                    textSize = (int) ((((float) shortLenth) * 3.0f) / 10.0f);
                    break;
                case XL3:
                    textSize = (int) ((((float) shortLenth) * 4.5f) / 10.0f);
                    break;
                case XL4:
                    textSize = (int) ((((float) shortLenth) * 6.0f) / 10.0f);
                    break;
            }
            setTextSize(0, textSize);
            this.mMaxTextSize = textSize;
            applySpacing();
        }
    }

    public void setFontSizeType(FontSizeType fontSizeType) {
        setFontSizeType(this.mShortLength, fontSizeType);
    }

    private void re_downloadFont(final VTFontDesBean vtFontDesBean) {
        new Thread() {
            public void run() {
                super.run();
                FontsUtil.redownloadFont(vtFontDesBean);
            }
        }.start();
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x00a7  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0097 A:{SYNTHETIC, Splitter: B:22:0x0097} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x009c A:{SYNTHETIC, Splitter: B:25:0x009c} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00a7  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00fa A:{SYNTHETIC, Splitter: B:49:0x00fa} */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00ff A:{SYNTHETIC, Splitter: B:52:0x00ff} */
    public void setTypeface(VTFontDesBean r18) {
        /*
        r17 = this;
        if (r18 != 0) goto L_0x0003;
    L_0x0002:
        return;
    L_0x0003:
        r0 = r18;
        r14 = r0.language;
        r0 = r18;
        r15 = r0.filename;
        r0 = r18;
        r0 = r0.fonttype;
        r16 = r0;
        r8 = com.blink.academy.onetake.Config.getFontFile(r14, r15, r16);
        r7 = new java.io.File;
        r7.<init>(r8);
        r14 = r7.exists();
        if (r14 != 0) goto L_0x009f;
    L_0x0020:
        r17.re_downloadFont(r18);
        r0 = r18;
        r14 = r0.language;
        r15 = "NanJingXiLu";
        r0 = r18;
        r0 = r0.fonttype;
        r16 = r0;
        r5 = com.blink.academy.onetake.Config.getFontFile(r14, r15, r16);
        r4 = new java.io.File;
        r4.<init>(r5);
        r14 = r4.exists();
        if (r14 != 0) goto L_0x010d;
    L_0x003e:
        r9 = new java.io.File;
        r0 = r18;
        r14 = r0.language;
        r15 = "NanJingXiLu";
        r0 = r18;
        r0 = r0.fonttype;
        r16 = r0;
        r14 = com.blink.academy.onetake.Config.getFontFile(r14, r15, r16);
        r9.<init>(r14);
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r15 = com.blink.academy.onetake.Config.APP_FONTS_ASSETS_PATH;
        r14 = r14.append(r15);
        r15 = "NanJingXiLu";
        r14 = r14.append(r15);
        r2 = r14.toString();
        r10 = 0;
        r12 = 0;
        r14 = r17.getContext();	 Catch:{ IOException -> 0x0112 }
        r14 = r14.getAssets();	 Catch:{ IOException -> 0x0112 }
        r10 = r14.open(r2);	 Catch:{ IOException -> 0x0112 }
        if (r10 == 0) goto L_0x00d7;
    L_0x0078:
        r13 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x0112 }
        r13.<init>(r9);	 Catch:{ IOException -> 0x0112 }
        r14 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
        r3 = new byte[r14];	 Catch:{ IOException -> 0x008e, all -> 0x010f }
        r11 = -1;
    L_0x0082:
        r11 = r10.read(r3);	 Catch:{ IOException -> 0x008e, all -> 0x010f }
        r14 = -1;
        if (r11 == r14) goto L_0x00d6;
    L_0x0089:
        r14 = 0;
        r13.write(r3, r14, r11);	 Catch:{ IOException -> 0x008e, all -> 0x010f }
        goto L_0x0082;
    L_0x008e:
        r6 = move-exception;
        r12 = r13;
    L_0x0090:
        r6.printStackTrace();	 Catch:{ all -> 0x00f7 }
        r8 = "";
        if (r12 == 0) goto L_0x009a;
    L_0x0097:
        r12.close();	 Catch:{ IOException -> 0x00ed }
    L_0x009a:
        if (r10 == 0) goto L_0x009f;
    L_0x009c:
        r10.close();	 Catch:{ IOException -> 0x00f2 }
    L_0x009f:
        r14 = "";
        r14 = r14.equals(r8);
        if (r14 != 0) goto L_0x00b2;
    L_0x00a7:
        r0 = r17;
        r14 = r0.mTextPaint;
        r15 = android.graphics.Typeface.createFromFile(r8);
        r14.setTypeface(r15);
    L_0x00b2:
        r0 = r18;
        r1 = r17;
        r1.vtFontDesBean = r0;
        r0 = r18;
        r14 = r0.name;
        r0 = r17;
        r0.mFontName = r14;
        r0 = r18;
        r14 = r0.borderwidth;
        r0 = r17;
        r0.mDefaultBorderWidth = r14;
        r0 = r18;
        r14 = r0.lineheightoffset;
        r14 = (float) r14;
        r0 = r17;
        r0.mLineSpacePercentOffset = r14;
        r17.applySpacing();
        goto L_0x0002;
    L_0x00d6:
        r12 = r13;
    L_0x00d7:
        r8 = r5;
        if (r12 == 0) goto L_0x00dd;
    L_0x00da:
        r12.close();	 Catch:{ IOException -> 0x00e8 }
    L_0x00dd:
        if (r10 == 0) goto L_0x009f;
    L_0x00df:
        r10.close();	 Catch:{ IOException -> 0x00e3 }
        goto L_0x009f;
    L_0x00e3:
        r6 = move-exception;
        r6.printStackTrace();
        goto L_0x009f;
    L_0x00e8:
        r6 = move-exception;
        r6.printStackTrace();
        goto L_0x00dd;
    L_0x00ed:
        r6 = move-exception;
        r6.printStackTrace();
        goto L_0x009a;
    L_0x00f2:
        r6 = move-exception;
        r6.printStackTrace();
        goto L_0x009f;
    L_0x00f7:
        r14 = move-exception;
    L_0x00f8:
        if (r12 == 0) goto L_0x00fd;
    L_0x00fa:
        r12.close();	 Catch:{ IOException -> 0x0103 }
    L_0x00fd:
        if (r10 == 0) goto L_0x0102;
    L_0x00ff:
        r10.close();	 Catch:{ IOException -> 0x0108 }
    L_0x0102:
        throw r14;
    L_0x0103:
        r6 = move-exception;
        r6.printStackTrace();
        goto L_0x00fd;
    L_0x0108:
        r6 = move-exception;
        r6.printStackTrace();
        goto L_0x0102;
    L_0x010d:
        r8 = r5;
        goto L_0x009f;
    L_0x010f:
        r14 = move-exception;
        r12 = r13;
        goto L_0x00f8;
    L_0x0112:
        r6 = move-exception;
        goto L_0x0090;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.widgets.VideoText.VTContainerView.setTypeface(com.blink.academy.onetake.bean.VTFontDesBean):void");
    }

    public VTFontDesBean getVtFontDesBean() {
        return this.vtFontDesBean;
    }

    public void setMaxLines(int maxLines) {
        this.mMaxLines = maxLines;
    }

    public void setViewWidth(int viewWidth) {
        this.mViewWidth = viewWidth;
    }

    public void setViewHeight(int viewHeight) {
        this.mViewHeight = viewHeight;
    }

    public void setViewHeight(int viewHeight, int verticalPos) {
        this.mVerticalPos = (int) (((1.0f * ((float) verticalPos)) * ((float) viewHeight)) / ((float) this.mViewHeight));
        this.mViewHeight = viewHeight;
    }

    public int getViewHeight() {
        return this.mViewHeight;
    }

    public void setVideoWidth(int videoWidth) {
        this.mVideoWidth = videoWidth;
    }

    public void setVideoHeight(int videoHeight) {
        this.mVideoHeight = videoHeight;
    }

    public ShadowType getShadowType() {
        return this.mShadowType;
    }

    public void setShadowType(ShadowType shadowType) {
        setShadowType(this.mMaxTextSize, shadowType);
        applySpacing();
    }

    public void setShadowType(int textSize, ShadowType shadowType) {
        this.mShadowType = shadowType;
        float scale;
        switch (this.mShadowType) {
            case UPADDDOWN:
            case AROUNDBORDER:
            case BOX:
            case INLINE:
                setShadowLayer(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 0);
                return;
            case NONE:
                setShadowLayer(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 0);
                return;
            case SOLID:
                scale = ((this.mScale * 1.0f) * ((float) textSize)) / (((float) this.mShortLength) / 10.0f);
                setShadowLayer(0.5f * scale, 2.0f * scale, 2.0f * scale, 255);
                return;
            case SMOOTH:
                scale = ((this.mScale * 1.0f) * ((float) textSize)) / (((float) this.mShortLength) / 10.0f);
                setShadowLayer(2.0f * scale, 1.0f * scale, 1.0f * scale, 127);
                return;
            case AROUND:
                setShadowLayer(25.0f * (((this.mScale * 1.0f) * ((float) textSize)) / (((float) this.mShortLength) / 10.0f)), StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 51);
                return;
            default:
                return;
        }
    }

    public void setShadowLayer(float radius, float dx, float dy, int alpha) {
        if (StaticLayoutUtil.DefaultSpacingadd < radius && radius < 1.0f) {
            radius = 1.0f;
        }
        radius = (float) Math.round(radius);
        dx = (float) Math.round(dx);
        dy = (float) Math.round(dy);
        if (radius > 25.0f) {
            radius = 25.0f;
        }
        this.mRadius = radius;
        this.mShadowDx = dx;
        this.mShadowDy = dy;
        int color = Color.parseColor("#000000");
        this.mShadowColor = Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
        this.mTextPaint.setShadowLayer(this.mRadius, this.mShadowDx, this.mShadowDy, this.mShadowColor);
    }

    private float getAllShadowOffset(int textSize) {
        float radius = StaticLayoutUtil.DefaultSpacingadd;
        float dx = StaticLayoutUtil.DefaultSpacingadd;
        float scale;
        switch (this.mShadowType) {
            case UPADDDOWN:
                return StaticLayoutUtil.DefaultSpacingadd;
            case AROUNDBORDER:
                int borderWidth = Math.round(((((this.mScale * 1.0f) * ((float) this.mDefaultBorderWidth)) * ((float) textSize)) * 10.0f) / ((float) this.mShortLength));
                if (borderWidth < 1) {
                    borderWidth = 1;
                }
                return (((float) borderWidth) + (((float) textSize) * 0.5f)) * 2.0f;
            case BOX:
                return (((float) textSize) * 0.5f) * 2.0f;
            case NONE:
                radius = StaticLayoutUtil.DefaultSpacingadd;
                dx = StaticLayoutUtil.DefaultSpacingadd;
                break;
            case SOLID:
                scale = ((this.mScale * 1.0f) * ((float) textSize)) / (((float) this.mShortLength) / 10.0f);
                radius = 0.5f * scale;
                if (StaticLayoutUtil.DefaultSpacingadd < radius && radius < 1.0f) {
                    radius = 1.0f;
                }
                if (radius > 25.0f) {
                    radius = 25.0f;
                }
                radius = (float) Math.round(radius);
                dx = (float) Math.round(2.0f * scale);
                break;
            case SMOOTH:
                scale = ((this.mScale * 1.0f) * ((float) textSize)) / (((float) this.mShortLength) / 10.0f);
                radius = 2.0f * scale;
                if (StaticLayoutUtil.DefaultSpacingadd < radius && radius < 1.0f) {
                    radius = 1.0f;
                }
                if (radius > 25.0f) {
                    radius = 25.0f;
                }
                radius = (float) Math.round(radius);
                dx = (float) Math.round(1.0f * scale);
                break;
            case AROUND:
                radius = 25.0f * (((this.mScale * 1.0f) * ((float) textSize)) / (((float) this.mShortLength) / 10.0f));
                if (StaticLayoutUtil.DefaultSpacingadd < radius && radius < 1.0f) {
                    radius = 1.0f;
                }
                if (radius > 25.0f) {
                    radius = 25.0f;
                }
                radius = (float) Math.round(radius);
                dx = StaticLayoutUtil.DefaultSpacingadd;
                break;
            case INLINE:
                return StaticLayoutUtil.DefaultSpacingadd;
        }
        if (dx > radius / 2.0f) {
            return (radius / 2.0f) + dx;
        }
        return radius;
    }

    public AlignType getAlignType() {
        return this.mAlignType;
    }

    public float getMoveXOffset() {
        return ((((float) getWidth()) - (((((float) this.mShortLength) * 1.0f) / 10.0f) * 2.0f)) * 1.0f) / 3.0f;
    }

    public int getVerticalPos() {
        return this.mVerticalPos;
    }

    public LetterSpacingType getLetterSpacingType() {
        return this.mLetterSpacingType;
    }

    public LineSpacingType getLineSpacingType() {
        return this.mLineSpacingType;
    }

    public TextColorType getTextColorType() {
        return this.mTextColorType;
    }

    public float getTextSize() {
        return this.mTextPaint.getTextSize();
    }

    public FontSizeType getFontSizeType() {
        return this.mFontSizeType;
    }

    public boolean isMultiLines() {
        return this.mMaxLines > 1;
    }

    public void setTextPaddingTop(int testPaddingTop) {
        this.mTextPaddingTop = testPaddingTop;
    }

    public void setTextPaddingBottom(int textPaddingBottom) {
        this.mTextPaddingBottom = textPaddingBottom;
    }

    public void setScale(float scale) {
        this.mScale = scale;
    }

    public int getShadowColor() {
        return this.mShadowColor;
    }

    public int getLetterSpacing() {
        return this.mLetterSpacing;
    }

    public int getLineSpacing() {
        return this.mLineSpacing;
    }

    public int getVerticalPaddingSpacing() {
        return this.mVerticalPaddingSpacing;
    }

    public int getYOffset() {
        return 0;
    }

    public float getRadius() {
        return this.mRadius;
    }

    public float getShadowDx() {
        return this.mShadowDx;
    }

    public float getShadowDy() {
        return this.mShadowDy;
    }

    public List<List<String>> getFinalTexts() {
        return this.mFinalTexts;
    }

    public static Bitmap getFontBitmap(Context context, int viewHeight, int mVideoWidth, int mVideoHeight, LongVideosModel model) {
        if (mVideoWidth == 0 || mVideoHeight == 0) {
            return null;
        }
        int curLineDrawWidth;
        int i;
        List<String> lineTextList;
        float scale = (((float) mVideoHeight) * 1.0f) / ((float) viewHeight);
        Paint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("#EEEEEE"));
        VTFontDesBean vtFontDesBean = model.getTextTypeface();
        textPaint.setTypeface(Typeface.createFromFile(Config.getFontFile(vtFontDesBean.language, vtFontDesBean.filename, vtFontDesBean.fonttype)));
        textPaint.setStyle(Style.FILL);
        textPaint.setShadowLayer(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, model.getShadowColor());
        textPaint.setTextSize(model.getTextSize() * scale);
        int letterSpacing = (int) (((float) model.getLetterSpacing()) * scale);
        int lineSpacing = (int) (((float) model.getLineSpacing()) * scale);
        int verticalPaddingSpacing = (int) (((float) model.getVerticalPaddingSpacing()) * scale);
        int yOffset = (int) (((float) model.getyOffset()) * scale);
        float radius = (float) Math.round(model.getRadius() * scale);
        if (radius > 25.0f) {
            radius = 25.0f;
        }
        textPaint.setShadowLayer(radius, (float) Math.round(model.getShadowDx() * scale), (float) Math.round(model.getShadowDy() * scale), model.getShadowColor());
        int lineHeight = (int) (Math.abs(textPaint.ascent()) + Math.abs(textPaint.descent()));
        int allLineHeight = (model.getFinalTexts().size() * lineHeight) + ((model.getFinalTexts().size() - 1) * lineSpacing);
        int y = (int) (Math.abs(textPaint.ascent()) + ((float) yOffset));
        int maxLineDrawWidth = 0;
        for (List<String> lineTextList2 : model.getFinalTexts()) {
            curLineDrawWidth = -letterSpacing;
            for (i = 0; i < lineTextList2.size(); i++) {
                curLineDrawWidth = (int) ((textPaint.measureText((String) lineTextList2.get(i)) + ((float) letterSpacing)) + ((float) curLineDrawWidth));
            }
            if (curLineDrawWidth > maxLineDrawWidth) {
                maxLineDrawWidth = curLineDrawWidth;
            }
        }
        if (maxLineDrawWidth < 60) {
            maxLineDrawWidth = 60;
        }
        Bitmap bitmap = Bitmap.createBitmap(maxLineDrawWidth, allLineHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        for (int index = 0; index < model.getFinalTexts().size(); index++) {
            List lineTextList2 = model.getFinalTexts().get(index);
            int textLength = lineTextList2.size();
            curLineDrawWidth = -letterSpacing;
            for (i = 0; i < textLength; i++) {
                curLineDrawWidth = (int) ((textPaint.measureText((String) lineTextList2.get(i)) + ((float) letterSpacing)) + ((float) curLineDrawWidth));
            }
            int x = (maxLineDrawWidth / 2) - (curLineDrawWidth / 2);
            switch (model.getTextAlignType()) {
                case L:
                    x = 0;
                    break;
                case M:
                    x = (maxLineDrawWidth / 2) - (curLineDrawWidth / 2);
                    break;
                case R:
                    x = maxLineDrawWidth - curLineDrawWidth;
                    break;
            }
            for (i = 0; i < textLength; i++) {
                canvas.drawText((String) lineTextList2.get(i), (float) x, (float) y, textPaint);
                x = (int) ((textPaint.measureText((String) lineTextList2.get(i)) + ((float) letterSpacing)) + ((float) x));
            }
            y += (lineHeight + lineSpacing) + yOffset;
        }
        return bitmap;
    }

    public boolean hasText() {
        return (this.originalText == null || this.originalText.length() == 0) ? false : true;
    }

    public DecoModel getDecoModel() {
        if (getText() == null || getText().length() == 0) {
            return null;
        }
        DecoModel decoModel = new DecoModel();
        decoModel.text = (String) getText();
        decoModel.fontName = getFontName();
        decoModel.fontSize = getFontSize();
        decoModel.align = getAlign();
        decoModel.textColor = getTextColor();
        decoModel.shadow = getShadow();
        decoModel.letterSpace = getLetterSpace();
        decoModel.lineSpace = getLineSpace();
        decoModel.pos = getPos();
        return decoModel;
    }

    private String getFontName() {
        return this.mFontName;
    }

    private String getFontSize() {
        switch (this.mFontSizeType) {
            case XS:
                return "XS";
            case S:
                return "S";
            case M:
                return "M";
            case L:
                return "L";
            case XL:
                return "XL";
            case XXL:
                return "XXL";
            case XL3:
                return "3XL";
            case XL4:
                return "4XL";
            default:
                return "";
        }
    }

    private String getAlign() {
        switch (this.mAlignType) {
            case L:
                return "L";
            case M:
                return "M";
            case R:
                return "R";
            default:
                return "";
        }
    }

    private String getTextColor() {
        switch (this.mTextColorType) {
            case White:
                return "EEEEEE";
            case Black:
                return "131211";
            default:
                return "";
        }
    }

    private String getShadow() {
        switch (this.mShadowType) {
            case UPADDDOWN:
                return "UPANDDOWN";
            case AROUNDBORDER:
                return "4BORDERS";
            case NONE:
                return "OFF";
            case SMOOTH:
                return "SMOOTH";
            case AROUND:
                return "AROUND";
            case INLINE:
                return "INLINE";
            default:
                return "";
        }
    }

    private String getLetterSpace() {
        switch (this.mLetterSpacingType) {
            case XS:
                return "XS";
            case S:
                return "S";
            case M:
                return "M";
            case L:
                return "L";
            case XL:
                return "XL";
            case XXL:
                return "XXL";
            case XL3:
                return "3XL";
            case XL4:
                return "4XL";
            default:
                return "";
        }
    }

    private String getLineSpace() {
        switch (this.mLineSpacingType) {
            case XS:
                return "XS";
            case S:
                return "S";
            case M:
                return "M";
            case L:
                return "L";
            case XL:
                return "XL";
            case XXL:
                return "XXL";
            case XL3:
                return "3XL";
            case XL4:
                return "4XL";
            default:
                return "";
        }
    }

    private String getPos() {
        return "" + this.mVerticalPos;
    }

    public ShadowType getShadowTypes(String showType) {
        int obj = -1;
        switch (showType.hashCode()) {
            case -2130667879:
                if (showType.equals("INLINE")) {
                    obj = 5;
                    break;
                }
                break;
            case -1845204562:
                if (showType.equals("SMOOTH")) {
                    obj = 2;
                    break;
                }
                break;
            case -1467725858:
                if (showType.equals("UPANDDOWN")) {
                    obj = 3;
                    break;
                }
                break;
            case 78159:
                if (showType.equals("OFF")) {
                    obj = -1;
                    break;
                }
                break;
            case 1217051379:
                if (showType.equals("4BORDERS")) {
                    obj = 4;
                    break;
                }
                break;
            case 1939061197:
                if (showType.equals("AROUND")) {
                    obj = 1;
                    break;
                }
                break;
        }
        switch (obj) {
            case -1:
                return ShadowType.NONE;
            case 1:
                return ShadowType.AROUND;
            case 2:
                return ShadowType.SMOOTH;
            case 3:
                return ShadowType.UPADDDOWN;
            case 4:
                return ShadowType.AROUNDBORDER;
            case 5:
                return ShadowType.INLINE;
            default:
                return ShadowType.NONE;
        }
    }

    public LineSpacingType getLineSpaceTypes(String lineSpace) {
        int obj = -1;
        switch (lineSpace.hashCode()) {
            case 76:
                if (lineSpace.equals("L")) {
                    obj = 3;
                    break;
                }
                break;
            case 77:
                if (lineSpace.equals("M")) {
                    obj = 2;
                    break;
                }
                break;
            case 83:
                if (lineSpace.equals("S")) {
                    obj = 1;
                    break;
                }
                break;
            case 2804:
                if (lineSpace.equals("XL")) {
                    obj = 4;
                    break;
                }
                break;
            case 2811:
                if (lineSpace.equals("XS")) {
                    obj = -1;
                    break;
                }
                break;
            case 51815:
                if (lineSpace.equals("3XL")) {
                    obj = 6;
                    break;
                }
                break;
            case 52776:
                if (lineSpace.equals("4XL")) {
                    obj = 7;
                    break;
                }
                break;
            case 87372:
                if (lineSpace.equals("XXL")) {
                    obj = 5;
                    break;
                }
                break;
        }
        switch (obj) {
            case -1:
                return LineSpacingType.XS;
            case 1:
                return LineSpacingType.S;
            case 2:
                return LineSpacingType.M;
            case 3:
                return LineSpacingType.L;
            case 4:
                return LineSpacingType.XL;
            case 5:
                return LineSpacingType.XXL;
            case 6:
                return LineSpacingType.XL3;
            case 7:
                return LineSpacingType.XL4;
            default:
                return LineSpacingType.M;
        }
    }

    public LetterSpacingType getLetterSpaceTypes(String letterSpace) {
        int obj = -1;
        switch (letterSpace.hashCode()) {
            case 76:
                if (letterSpace.equals("L")) {
                    obj = 3;
                    break;
                }
                break;
            case 77:
                if (letterSpace.equals("M")) {
                    obj = 2;
                    break;
                }
                break;
            case 83:
                if (letterSpace.equals("S")) {
                    obj = 1;
                    break;
                }
                break;
            case 2804:
                if (letterSpace.equals("XL")) {
                    obj = 4;
                    break;
                }
                break;
            case 2811:
                if (letterSpace.equals("XS")) {
                    obj = -1;
                    break;
                }
                break;
            case 51815:
                if (letterSpace.equals("3XL")) {
                    obj = 6;
                    break;
                }
                break;
            case 52776:
                if (letterSpace.equals("4XL")) {
                    obj = 7;
                    break;
                }
                break;
            case 87372:
                if (letterSpace.equals("XXL")) {
                    obj = 5;
                    break;
                }
                break;
        }
        switch (obj) {
            case -1:
                return LetterSpacingType.XS;
            case 1:
                return LetterSpacingType.S;
            case 2:
                return LetterSpacingType.M;
            case 3:
                return LetterSpacingType.L;
            case 4:
                return LetterSpacingType.XL;
            case 5:
                return LetterSpacingType.XXL;
            case 6:
                return LetterSpacingType.XL3;
            case 7:
                return LetterSpacingType.XL4;
            default:
                return LetterSpacingType.M;
        }
    }

    public TextColorType getColorTypes(String colorType) {
        int obj = -1;
        switch (colorType.hashCode()) {
            case 1451437347:
                if (colorType.equals("131211")) {
                    obj = 1;
                    break;
                }
                break;
            case 2041258464:
                if (colorType.equals("EEEEEE")) {
                    obj = -1;
                    break;
                }
                break;
        }
        switch (obj) {
            case -1:
                return TextColorType.White;
            case 1:
                return TextColorType.Black;
            default:
                return TextColorType.White;
        }
    }

    public FontSizeType getFontSizeTypes(String sizeType) {
        int obj = -1;
        switch (sizeType.hashCode()) {
            case 76:
                if (sizeType.equals("L")) {
                    obj = 3;
                    break;
                }
                break;
            case 77:
                if (sizeType.equals("M")) {
                    obj = 2;
                    break;
                }
                break;
            case 83:
                if (sizeType.equals("S")) {
                    obj = 1;
                    break;
                }
                break;
            case 2804:
                if (sizeType.equals("XL")) {
                    obj = 4;
                    break;
                }
                break;
            case 2811:
                if (sizeType.equals("XS")) {
                    obj = -1;
                    break;
                }
                break;
            case 51815:
                if (sizeType.equals("3XL")) {
                    obj = 6;
                    break;
                }
                break;
            case 52776:
                if (sizeType.equals("4XL")) {
                    obj = 7;
                    break;
                }
                break;
            case 87372:
                if (sizeType.equals("XXL")) {
                    obj = 5;
                    break;
                }
                break;
        }
        switch (obj) {
            case -1:
                return FontSizeType.XS;
            case 1:
                return FontSizeType.S;
            case 2:
                return FontSizeType.M;
            case 3:
                return FontSizeType.L;
            case 4:
                return FontSizeType.XL;
            case 5:
                return FontSizeType.XXL;
            case 6:
                return FontSizeType.XL3;
            case 7:
                return FontSizeType.XL4;
            default:
                return FontSizeType.XS;
        }
    }

    public AlignType getAlignTypes(String alignType) {
        int obj = -1;
        switch (alignType.hashCode()) {
            case 76:
                if (alignType.equals("L")) {
                    obj = -1;
                    break;
                }
                break;
            case 77:
                if (alignType.equals("M")) {
                    obj = 1;
                    break;
                }
                break;
            case 82:
                if (alignType.equals("R")) {
                    obj = 2;
                    break;
                }
                break;
        }
        switch (obj) {
            case -1:
                return AlignType.L;
            case 1:
                return AlignType.M;
            case 2:
                return AlignType.R;
            default:
                return AlignType.M;
        }
    }
}