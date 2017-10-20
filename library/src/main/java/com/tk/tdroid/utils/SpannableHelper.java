package com.tk.tdroid.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineHeightSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ReplacementSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.text.style.UpdateAppearance;
import android.util.Log;

import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

import static com.tk.tdroid.utils.SpannableHelper.Builder.Align.ALIGN_BASELINE;
import static com.tk.tdroid.utils.SpannableHelper.Builder.Align.ALIGN_BOTTOM;
import static com.tk.tdroid.utils.SpannableHelper.Builder.Align.ALIGN_CENTER;
import static com.tk.tdroid.utils.SpannableHelper.Builder.Align.ALIGN_TOP;

/**
 * <pre>
 *      author : TK
 *      time : 2017/9/30
 *      desc : Spannable辅助类
 * </pre>
 * 支持：
 * <ul>
 * <li>前景色（字体颜色）</li>
 * <li>背景色</li>
 * <li>加粗、倾斜、粗斜体</li>
 * <li>删除线</li>
 * <li>下划线</li>
 * <li>上标、下标</li>
 * <li>字体大小</li>
 * <li>点击事件</li>
 * <li>超链接</li>
 * <li>图文混排（支持对齐方式）</li>
 * <li>行高设置</li>
 * <li>首行缩进</li>
 * <li>引用线</li>
 * <li>列表项</li>
 * <li>支持空格</li>
 * <li>文字对齐方式</li>
 * <li>Shader模式</li>
 * <li>阴影</li>
 * </ul>
 * <a href="http://blog.csdn.net/liao277218962/article/details/50623722">PS: Android中各种Span的用法</a>
 */

public final class SpannableHelper {
    private SpannableHelper() {
    }

    public static Builder create(String text) {
        return new Builder();
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        @IntDef({ALIGN_BOTTOM, ALIGN_BASELINE, ALIGN_CENTER, ALIGN_TOP})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Align {
            int ALIGN_BOTTOM = 0;
            int ALIGN_BASELINE = 1;
            int ALIGN_CENTER = 2;
            int ALIGN_TOP = 3;
        }

        /**
         * 默认颜色
         */
//        private static final int DEFAULT_COLOR = -1;
        private static final int FLAG = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        /**
         * 换行符
         */
        private static final String LINE_SEPARATOR = System.getProperty("line.separator");

        private CharSequence tempText;


        private int foregroundColor;
        private int backgroundColor;
        private boolean isBold;
        private boolean isItalic;
        private boolean isBoldItalic;
        private boolean isStrikeThrough;
        private boolean isUnderline;
        private boolean isSuperscript;
        private boolean isSubscript;
        private int fontSize;
        private boolean fontSizeIsDp;
        private float proportion;
        private float xProportion;
        private ClickableSpan clickSpan;
        private String url;
        private Drawable imageDrawable;
        private Uri imageUri;
        private int imageAlign;
        /**
         * 行高配置
         */
        private int lineHeight;
        private int lineAlign;
        /**
         * 首行缩进配置
         */
        private int lineFirst;
        private int lineRest;
        /**
         * 引用线配置
         */
        private int quoteColor;
        private int quoteWidth;
        private int quoteMargin;
        /**
         * 列表项
         */
        private int bulletColor;
        private int bulletRadius;
        private int bulletMargin;
        private boolean bulletCircle;
        /**
         * 空格
         */
        private int spaceWidth;
        private int spaceColor;
        /**
         * 对齐方式
         */
        private Layout.Alignment alignment;

        private Shader shader;
        /**
         * 阴影
         */
        private float shadowRadius;
        private float shadowDx;
        private float shadowDy;
        private int shadowColor;

        private SpannableStringBuilder mBuilder;

        private int mType;
        private static final int TYPE_TEXT = 0;
        private static final int TYPE_IMAGE = 1;
        private static final int TYPE_SPACE = 2;

        public Builder() {
            mBuilder = new SpannableStringBuilder();
            tempText = "";
            reset();
        }

        private void reset() {

            foregroundColor = -1;
            backgroundColor = -1;
            isBold = false;
            isItalic = false;
            isBoldItalic = false;
            isStrikeThrough = false;
            isUnderline = false;
            isSuperscript = false;
            isSubscript = false;
            fontSize = -1;
            proportion = -1;
            xProportion = -1;
            clickSpan = null;
            url = null;
            imageDrawable = null;
            imageUri = null;
            lineHeight = -1;

            lineFirst = -1;
            quoteColor = -1;
            bulletColor = -1;
            alignment = null;
            shader = null;
            shadowColor = -1;
        }

        /**
         * 设置前景色（字体颜色）
         *
         * @param color 色值
         * @return
         */
        public Builder foregroundColor(@ColorInt final int color) {
            this.foregroundColor = color;
            return this;
        }

        /**
         * 设置前景色（字体颜色）
         *
         * @param color 资源
         * @return
         */
        public Builder foregroundResColor(@ColorRes final int color) {
            this.foregroundColor = ContextCompat.getColor(Utils.getApp(), color);
            return this;
        }

        /**
         * 设置背景色
         *
         * @param color 色值
         * @return
         */
        public Builder backgroundColor(@ColorInt final int color) {
            this.backgroundColor = color;
            return this;
        }

        /**
         * 设置背景色
         *
         * @param color 资源
         * @return
         */
        public Builder backgroundResColor(@ColorRes final int color) {
            this.backgroundColor = ContextCompat.getColor(Utils.getApp(), color);
            return this;
        }

        /**
         * 设置粗体
         *
         * @return
         */
        public Builder bold() {
            isBold = true;
            return this;
        }

        /**
         * 设置斜体
         *
         * @return
         */
        public Builder italic() {
            isItalic = true;
            return this;
        }

        /**
         * 设置粗斜体
         *
         * @return
         */
        public Builder boldItalic() {
            isBoldItalic = true;
            return this;
        }

        /**
         * 设置删除线
         *
         * @return
         */
        public Builder strikeThrough() {
            this.isStrikeThrough = true;
            return this;
        }

        /**
         * 设置下划线
         *
         * @return
         */
        public Builder underline() {
            this.isUnderline = true;
            return this;
        }

        /**
         * 设置上标
         *
         * @return
         */
        public Builder superscript() {
            this.isSuperscript = true;
            return this;
        }

        /**
         * 设置下标
         *
         * @return
         */
        public Builder subscript() {
            this.isSubscript = true;
            return this;
        }

        /**
         * 设置字体尺寸
         *
         * @param size 尺寸
         * @return
         */
        public Builder fontSize(@IntRange(from = 0) final int size) {
            return fontSize(size, false);
        }

        /**
         * 设置字体尺寸
         *
         * @param size 尺寸
         * @param isDp 是否使用dip
         * @return
         */
        public Builder fontSize(@IntRange(from = 0) final int size, final boolean isDp) {
            this.fontSize = size;
            this.fontSizeIsDp = isDp;
            return this;
        }

        /**
         * 设置字体比例
         *
         * @param proportion 比例
         * @return
         */
        public Builder fontProportion(@FloatRange(from = 0) final float proportion) {
            this.proportion = proportion;
            return this;
        }

        /**
         * 设置字体横向比例
         *
         * @param proportion 比例
         * @return
         */
        public Builder fontXProportion(@FloatRange(from = 0) final float proportion) {
            this.xProportion = proportion;
            return this;
        }


        /**
         * 设置点击事件
         * 需添加textview.setMovementMethod(LinkMovementMethod.getInstance())
         *
         * @param clickSpan 点击事件
         * @return
         */
        public Builder click(@NonNull final ClickableSpan clickSpan) {
            this.clickSpan = clickSpan;
            return this;
        }

        /**
         * 设置超链接
         * 需添加textview.setMovementMethod(LinkMovementMethod.getInstance())
         *
         * @param url 超链接
         * @return
         */
        public Builder url(@NonNull final String url) {
            this.url = url;
            return this;
        }

        /**
         * 设置行高
         * 行高 > 字体高度时，默认垂直居中
         *
         * @param lineHeight 行高
         * @return
         */
        public Builder lineHeight(@IntRange(from = 0) final int lineHeight) {
            return lineHeight(lineHeight, ALIGN_CENTER);
        }

        /**
         * 设置行高
         * 高 > 字体高度时，默认垂直居中
         *
         * @param lineHeight 行高
         * @param align      行内对齐方式
         *                   <ul>
         *                   <li>{@link Align#ALIGN_TOP}顶部对齐</li>
         *                   <li>{@link Align#ALIGN_CENTER}居中对齐</li>
         *                   <li>{@link Align#ALIGN_BOTTOM}底部对齐</li>
         *                   </ul>
         * @return
         */
        public Builder lineHeight(@IntRange(from = 0) final int lineHeight, @Align final int align) {
            this.lineHeight = lineHeight;
            this.lineAlign = align;
            return this;
        }

        /**
         * 设置缩进
         *
         * @param first 首行缩进
         * @param rest  剩余行缩进
         * @return
         */
        public Builder leadingMargin(@IntRange(from = 0) final int first, @IntRange(from = 0) final int rest) {
            this.lineFirst = first;
            this.lineRest = rest;
            return this;
        }

        /**
         * 设置引用线的颜色
         *
         * @param quoteColor  引用线的颜色
         * @param quoteWidth  引用线线宽
         * @param quoteMargin 引用线和文字间距
         * @return
         */
        public Builder quote(@ColorInt final int quoteColor, @IntRange(from = 1) final int quoteWidth, @IntRange(from = 0) final int quoteMargin) {
            this.quoteColor = quoteColor;
            this.quoteWidth = quoteWidth;
            this.quoteMargin = quoteMargin;
            return this;
        }

        /**
         * 设置列表项
         *
         * @param bulletColor  列表项的颜色
         * @param bulletRadius 列表项的半径
         * @param bulletMargin 列表项和文字间距
         * @return
         */
        public Builder bullet(@ColorInt final int bulletColor, @IntRange(from = 1) final int bulletRadius, @IntRange(from = 0) final int bulletMargin) {
            return bullet(bulletColor, bulletRadius, bulletMargin, true);
        }

        /**
         * 设置列表项
         *
         * @param bulletColor  列表项的颜色
         * @param bulletRadius 列表项的半径
         * @param bulletMargin 列表项和文字间距
         * @return
         */
        public Builder bullet(@ColorInt final int bulletColor, @IntRange(from = 1) final int bulletRadius, @IntRange(from = 0) final int bulletMargin, boolean bulletCircle) {
            this.bulletColor = bulletColor;
            this.bulletRadius = bulletRadius;
            this.bulletMargin = bulletMargin;
            this.bulletCircle = bulletCircle;
            return this;
        }


        /**
         * 文字对齐方式
         *
         * @param alignment 对齐方式
         *                  <ul>
         *                  <li>{@link Layout.Alignment#ALIGN_NORMAL}正常对齐</li>
         *                  <li>{@link Layout.Alignment#ALIGN_CENTER}居中对齐</li>
         *                  <li>{@link Layout.Alignment#ALIGN_OPPOSITE}反向对齐</li>
         *                  </ul>
         * @return
         */
        public Builder alignment(@NonNull final Layout.Alignment alignment) {
            this.alignment = alignment;
            return this;
        }

        /**
         * Shader模式
         *
         * @param shader 模式
         * @return
         */
        public Builder shader(@NonNull final Shader shader) {
            this.shader = shader;
            return this;
        }

        /**
         * 设置阴影
         *
         * @param shadowRadius 阴影半径
         * @param shadowDx
         * @param shadowDy     y轴偏移量
         * @param shadowColor  阴影颜色
         * @return
         */
        public Builder shadow(@FloatRange(from = 0) final float shadowRadius,
                              final float shadowDx,
                              final float shadowDy,
                              @ColorInt final int shadowColor) {
            this.shadowRadius = shadowRadius;
            this.shadowDx = shadowDx;
            this.shadowDy = shadowDy;
            this.shadowColor = shadowColor;
            return this;
        }

        /**
         * 追加字符串
         *
         * @param text 文本
         * @return
         */
        public Builder append(@NonNull final CharSequence text) {
            update(TYPE_TEXT);
            tempText = text;
            return this;
        }

        /**
         * 追加一行（等同换行）
         *
         * @return
         */
        public Builder appendLine() {
            update(TYPE_TEXT);
            tempText = LINE_SEPARATOR;
            return this;
        }

        /**
         * 追加字符串并换行
         *
         * @return
         */
        public Builder appendLine(@NonNull final CharSequence text) {
            update(TYPE_TEXT);
            tempText = text + LINE_SEPARATOR;
            return this;
        }

        /**
         * 追加图片
         *
         * @param bitmap 位图
         * @return
         */
        public Builder appendImage(@NonNull final Bitmap bitmap) {
            return appendImage(bitmap, ALIGN_BOTTOM);
        }

        /**
         * 追加图片
         *
         * @param bitmap 位图
         * @param align  对齐方式
         *               <ul>
         *               <li>{@link Align#ALIGN_TOP}顶部对齐</li>
         *               <li>{@link Align#ALIGN_CENTER}居中对齐</li>
         *               <li>{@link Align#ALIGN_BASELINE}基线对齐</li>
         *               <li>{@link Align#ALIGN_BOTTOM}底部对齐</li>
         *               </ul>
         * @return
         */
        public Builder appendImage(@NonNull final Bitmap bitmap, @Align final int align) {
            update(TYPE_IMAGE);
            this.imageDrawable = new BitmapDrawable(Utils.getApp().getResources(), bitmap);
            this.imageAlign = align;
            return this;
        }

        /**
         * 追加图片
         *
         * @param drawable 图片
         * @return
         */
        public Builder appendImage(@NonNull final Drawable drawable) {
            return appendImage(drawable, ALIGN_BOTTOM);
        }

        /**
         * 追加图片
         *
         * @param drawable 图片
         * @param align    对齐方式
         *                 <ul>
         *                 <li>{@link Align#ALIGN_TOP}顶部对齐</li>
         *                 <li>{@link Align#ALIGN_CENTER}居中对齐</li>
         *                 <li>{@link Align#ALIGN_BASELINE}基线对齐</li>
         *                 <li>{@link Align#ALIGN_BOTTOM}底部对齐</li>
         *                 </ul>
         * @return
         */
        public Builder appendImage(@NonNull final Drawable drawable, @Align final int align) {
            update(TYPE_IMAGE);
            this.imageDrawable = drawable;
            this.imageAlign = align;
            return this;
        }

        /**
         * 追加图片
         *
         * @param uri 图片uri
         * @return
         */
        public Builder appendImage(@NonNull final Uri uri) {
            return appendImage(uri, ALIGN_BOTTOM);
        }

        /**
         * 追加图片
         *
         * @param uri   图片uri
         * @param align 对齐
         *              <ul>
         *              <li>{@link Align#ALIGN_TOP}顶部对齐</li>
         *              <li>{@link Align#ALIGN_CENTER}居中对齐</li>
         *              <li>{@link Align#ALIGN_BASELINE}基线对齐</li>
         *              <li>{@link Align#ALIGN_BOTTOM}底部对齐</li>
         *              </ul>
         * @return
         */
        public Builder appendImage(@NonNull final Uri uri, @Align final int align) {
            update(TYPE_IMAGE);
            this.imageUri = uri;
            this.imageAlign = align;
            return this;
        }

        /**
         * 追加图片
         *
         * @param resourceId 图片资源id
         * @return
         */
        public Builder appendImage(@DrawableRes final int resourceId) {
            return appendImage(resourceId, ALIGN_BOTTOM);
        }

        /**
         * 追加图片
         *
         * @param resourceId 图片资源id
         * @param align      对齐
         * @return
         */
        public Builder appendImage(@DrawableRes final int resourceId, @Align final int align) {
            update(TYPE_IMAGE);
            this.imageDrawable = ContextCompat.getDrawable(Utils.getApp(), resourceId);
            this.imageAlign = align;
            return this;
        }

        /**
         * 追加空格
         *
         * @param spaceWidth
         * @return
         */
        public Builder appendSpace(@IntRange(from = 0) final int spaceWidth) {
            return appendSpace(spaceWidth, Color.TRANSPARENT);
        }

        /**
         * 追加空格
         *
         * @param spaceWidth
         * @param spaceColor
         * @return
         */
        public Builder appendSpace(@IntRange(from = 0) final int spaceWidth, @ColorInt int spaceColor) {
            update(TYPE_SPACE);
            this.spaceWidth = spaceWidth;
            this.spaceColor = spaceColor;
            return this;
        }

        /**
         * 更新
         *
         * @param type
         */
        private void update(final int type) {
            updateAndReset();
            mType = type;
        }

        /**
         * 更新并恢复状态
         */
        private void updateAndReset() {
            if (mType == TYPE_TEXT) {
                updateText();
            } else if (mType == TYPE_IMAGE) {
                updateImage();
            } else if (mType == TYPE_SPACE) {
                updateSpace();
            }
            reset();
        }

        /**
         * 更新文本
         */
        private void updateText() {
            if (tempText.length() == 0) {
                return;
            }
            int from = mBuilder.length();
            mBuilder.append(tempText);
            int to = mBuilder.length();

            if (foregroundColor != -1) {
                mBuilder.setSpan(new ForegroundColorSpan(foregroundColor), from, to, FLAG);
            }
            if (backgroundColor != -1) {
                mBuilder.setSpan(new BackgroundColorSpan(backgroundColor), from, to, FLAG);
            }
            if (isBold) {
                mBuilder.setSpan(new StyleSpan(Typeface.BOLD), from, to, FLAG);
            }
            if (isItalic) {
                mBuilder.setSpan(new StyleSpan(Typeface.ITALIC), from, to, FLAG);
            }
            if (isBoldItalic) {
                mBuilder.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), from, to, FLAG);
            }
            if (isStrikeThrough) {
                mBuilder.setSpan(new StrikethroughSpan(), from, to, FLAG);
            }
            if (isUnderline) {
                mBuilder.setSpan(new UnderlineSpan(), from, to, FLAG);
            }
            if (isSuperscript) {
                mBuilder.setSpan(new SuperscriptSpan(), from, to, FLAG);
            }
            if (isSubscript) {
                mBuilder.setSpan(new SubscriptSpan(), from, to, FLAG);
            }
            if (fontSize != -1) {
                mBuilder.setSpan(new AbsoluteSizeSpan(fontSize, fontSizeIsDp), from, to, FLAG);
            }
            if (proportion != -1) {
                mBuilder.setSpan(new RelativeSizeSpan(proportion), from, to, FLAG);
            }
            if (xProportion != -1) {
                mBuilder.setSpan(new ScaleXSpan(xProportion), from, to, FLAG);
            }
            if (clickSpan != null) {
                mBuilder.setSpan(clickSpan, from, to, FLAG);
            }
            if (url != null) {
                mBuilder.setSpan(new URLSpan(url), from, to, FLAG);
            }
            if (lineHeight != -1) {
                mBuilder.setSpan(new TLineHeightSpan(lineHeight, lineAlign), from, to, FLAG);
            }
            if (lineFirst != -1) {
                mBuilder.setSpan(new LeadingMarginSpan.Standard(lineFirst, lineRest), from, to, FLAG);
            }
            if (quoteColor != -1) {
                mBuilder.setSpan(new TQuoteSpan(quoteColor, quoteWidth, quoteMargin), from, to, FLAG);
            }
            if (bulletColor != -1) {
                mBuilder.setSpan(new TBulletSpan(bulletColor, bulletRadius, bulletMargin, bulletCircle), from, to, FLAG);
            }
            if (alignment != null) {
                mBuilder.setSpan(new AlignmentSpan.Standard(alignment), from, to, FLAG);
            }
            if (shader != null) {
                mBuilder.setSpan(new TShaderSpan(shader), from, to, FLAG);
            }
            if (shadowColor != -1) {
                mBuilder.setSpan(new TShadowSpan(shadowRadius, shadowDx, shadowDy, shadowColor), from, to, FLAG);
            }
        }

        /**
         * 更新图片
         */
        private void updateImage() {
            int from = mBuilder.length();
            mBuilder.append("<Img>");
            int to = mBuilder.length();
            if (imageDrawable != null) {
                mBuilder.setSpan(new TImageSpan(imageDrawable, imageAlign), from, to, FLAG);
            } else if (imageUri != null) {
                mBuilder.setSpan(new TImageSpan(imageUri, imageAlign), from, to, FLAG);
            }
        }

        /**
         * 更新空格占位
         */
        private void updateSpace() {
            int from = mBuilder.length();
            mBuilder.append("<Space>");
            int to = mBuilder.length();
            mBuilder.setSpan(new TSpaceSpan(spaceWidth, spaceColor), from, to, FLAG);
        }

        /**
         * 创建
         *
         * @return
         */
        public SpannableStringBuilder build() {
            updateAndReset();
            return mBuilder;
        }

        /**
         * 图片的继承扩展
         */
        private static class TImageSpan extends TDynamicDrawableSpan {
            private Drawable mDrawable;
            private Uri mContentUri;

            private TImageSpan(@NonNull final Drawable drawable, final int verticalAlignment) {
                super(verticalAlignment);
                mDrawable = drawable;
                mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
            }

            private TImageSpan(@NonNull final Uri uri, final int verticalAlignment) {
                super(verticalAlignment);
                mContentUri = uri;
            }

            @Override
            public Drawable getDrawable() {
                Drawable drawable = null;
                if (mDrawable != null) {
                    drawable = mDrawable;
                } else if (mContentUri != null) {
                    Bitmap bitmap;
                    try {
                        InputStream is = Utils.getApp().getContentResolver().openInputStream(mContentUri);
                        bitmap = BitmapFactory.decodeStream(is);
                        drawable = new BitmapDrawable(Utils.getApp().getResources(), bitmap);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        if (is != null) {
                            is.close();
                        }
                    } catch (Exception e) {
                        Log.e("sms", "Failed to loaded content " + mContentUri, e);
                    }
                }
                return drawable;
            }
        }

        /**
         * 扩展，使其支持多种对齐方式
         */
        private static abstract class TDynamicDrawableSpan extends ReplacementSpan {

            private final int mVerticalAlignment;
            private WeakReference<Drawable> mDrawableRef;

            private TDynamicDrawableSpan() {
                mVerticalAlignment = ALIGN_BOTTOM;
            }

            private TDynamicDrawableSpan(final int verticalAlignment) {
                mVerticalAlignment = verticalAlignment;
            }

            public abstract Drawable getDrawable();

            @Override
            public int getSize(@NonNull final Paint paint, final CharSequence text,
                               final int start, final int end,
                               final Paint.FontMetricsInt fm) {
                Drawable d = getCachedDrawable();
                Rect rect = d.getBounds();
                final int fontHeight = (int) (paint.getFontMetrics().descent - paint.getFontMetrics().ascent);
                if (fm != null) {
                    if (rect.height() > fontHeight) {
                        if (mVerticalAlignment == ALIGN_TOP) {
                            fm.descent += rect.height() - fontHeight;
                        } else if (mVerticalAlignment == ALIGN_CENTER) {
                            fm.ascent -= (rect.height() - fontHeight) / 2;
                            fm.descent += (rect.height() - fontHeight) / 2;
                        } else {
                            fm.ascent -= rect.height() - fontHeight;
                        }
                    }
                }
                return rect.right;
            }

            @Override
            public void draw(@NonNull final Canvas canvas, final CharSequence text,
                             final int start, final int end, final float x,
                             final int top, final int y, final int bottom, @NonNull final Paint paint) {
                Drawable d = getCachedDrawable();
                Rect rect = d.getBounds();
                canvas.save();
                final float fontHeight = paint.getFontMetrics().descent - paint.getFontMetrics().ascent;
                int transY = bottom - rect.bottom;
                if (rect.height() < fontHeight) {
                    if (mVerticalAlignment == ALIGN_BASELINE) {
                        transY -= paint.getFontMetricsInt().descent;
                    } else if (mVerticalAlignment == ALIGN_CENTER) {
                        transY -= (fontHeight - rect.height()) / 2;
                    } else if (mVerticalAlignment == ALIGN_TOP) {
                        transY -= fontHeight - rect.height();
                    }
                }
                canvas.translate(x, transY);
                d.draw(canvas);
                canvas.restore();
            }

            private Drawable getCachedDrawable() {
                WeakReference<Drawable> wr = mDrawableRef;
                Drawable d = null;

                if (wr != null) {
                    d = wr.get();
                }
                if (d == null) {
                    d = getDrawable();
                    mDrawableRef = new WeakReference<>(d);
                }
                return getDrawable();
            }
        }

        /**
         * 行高的扩展
         */
        private static class TLineHeightSpan extends CharacterStyle implements LineHeightSpan {

            private final int height;
            private final int mVerticalAlignment;

            private TLineHeightSpan(final int height, final int verticalAlignment) {
                this.height = height;
                mVerticalAlignment = verticalAlignment;
            }

            @Override
            public void chooseHeight(final CharSequence text, final int start, final int end, final int spanstartv, final int v, final Paint.FontMetricsInt fm) {
                int need = height - (v + fm.descent - fm.ascent - spanstartv);
                if (need > 0) {
                    if (mVerticalAlignment == ALIGN_TOP) {
                        fm.descent += need;
                    } else if (mVerticalAlignment == ALIGN_CENTER) {
                        fm.descent += need / 2;
                        fm.ascent -= need / 2;
                    } else {
                        fm.ascent -= need;
                    }
                }
                need = height - (v + fm.bottom - fm.top - spanstartv);
                if (need > 0) {
                    if (mVerticalAlignment == ALIGN_TOP) {
                        fm.top += need;
                    } else if (mVerticalAlignment == ALIGN_CENTER) {
                        fm.bottom += need / 2;
                        fm.top -= need / 2;
                    } else {
                        fm.top -= need;
                    }
                }
            }

            @Override
            public void updateDrawState(final TextPaint tp) {
            }
        }

        /**
         * 引用线的扩展
         */
        private static class TQuoteSpan implements LeadingMarginSpan {

            private int quoteColor;
            private int quoteWidth;
            private int quoteMargin;

            private TQuoteSpan(@ColorInt final int quoteColor, final int quoteWidth, final int quoteMargin) {
                super();
                this.quoteColor = quoteColor;
                this.quoteWidth = quoteWidth;
                this.quoteMargin = quoteMargin;
            }

            @Override
            public int getLeadingMargin(boolean first) {
                return quoteWidth + quoteMargin;
            }

            @Override
            public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                          int top, int baseline, int bottom, CharSequence text,
                                          int start, int end, boolean first, Layout layout) {
                Paint.Style style = p.getStyle();
                int color = p.getColor();

                p.setStyle(Paint.Style.FILL);
                p.setColor(quoteColor);

                c.drawRect(x, top, x + dir * quoteWidth, bottom, p);

                p.setStyle(style);
                p.setColor(color);
            }
        }

        /**
         * 列表项的扩展
         */
        private static class TBulletSpan implements LeadingMarginSpan {

            private final int bulletColor;
            private final int bulletRadius;
            private final int bulletMargin;
            /**
             * 圆点 or 矩形
             */
            private boolean bulletCircle = true;

            private Path mPath = null;

            private TBulletSpan(@ColorInt final int bulletColor, final int bulletRadius, final int bulletMargin, boolean bulletCircle) {
                this.bulletColor = bulletColor;
                this.bulletRadius = bulletRadius;
                this.bulletMargin = bulletMargin;
                this.bulletCircle = bulletCircle;
            }

            @Override
            public int getLeadingMargin(boolean first) {
                return 2 * bulletRadius + bulletMargin;
            }

            public void drawLeadingMargin(final Canvas c, final Paint p, final int x, final int dir,
                                          final int top, final int baseline, final int bottom,
                                          final CharSequence text, final int start, final int end,
                                          final boolean first, final Layout l) {
                if (((Spanned) text).getSpanStart(this) == start) {
                    Paint.Style style = p.getStyle();
                    int oldColor = 0;
                    oldColor = p.getColor();
                    p.setColor(bulletColor);
                    p.setStyle(Paint.Style.FILL);
                    if (c.isHardwareAccelerated()) {
                        if (mPath == null) {
                            mPath = new Path();
                            if (bulletCircle) {
                                mPath.addCircle(0.0f, 0.0f, bulletRadius, Path.Direction.CW);
                            } else {
                                mPath.addRect(-bulletRadius, -bulletRadius, bulletRadius, bulletRadius, Path.Direction.CW);
                            }
                        }
                        c.save();
                        c.translate(x + dir * bulletRadius, (top + bottom) / 2.0f);
                        c.drawPath(mPath, p);
                        c.restore();
                    } else {
                        if (bulletCircle) {
                            c.drawCircle(x + dir * bulletRadius, (top + bottom) / 2.0f, bulletRadius, p);
                        } else {
                            c.drawRect(x + (dir - 1) * bulletRadius, top, x + (dir + 1) * bulletRadius, bottom, p);
                        }
                    }
                    p.setColor(oldColor);
                    p.setStyle(style);
                }
            }
        }

        /**
         * 空格的扩展
         */
        private static class TSpaceSpan extends ReplacementSpan {

            private final int spaceWidth;
            private final int spaceColor;

            private TSpaceSpan(final int spaceWidth) {
                this(spaceWidth, Color.TRANSPARENT);
            }

            private TSpaceSpan(final int spaceWidth, @ColorInt final int spaceColor) {
                super();
                this.spaceWidth = spaceWidth;
                this.spaceColor = spaceColor;
            }

            @Override
            public int getSize(@NonNull final Paint paint, final CharSequence text,
                               @IntRange(from = 0) final int start,
                               @IntRange(from = 0) final int end,
                               @Nullable final Paint.FontMetricsInt fm) {
                return spaceWidth;
            }

            @Override
            public void draw(@NonNull final Canvas canvas, final CharSequence text,
                             @IntRange(from = 0) final int start,
                             @IntRange(from = 0) final int end,
                             final float x, final int top, final int y, final int bottom,
                             @NonNull final Paint paint) {
                Paint.Style style = paint.getStyle();
                int color = paint.getColor();

                paint.setStyle(Paint.Style.FILL);
                paint.setColor(spaceColor);

                canvas.drawRect(x, top, x + spaceWidth, bottom, paint);

                paint.setStyle(style);
                paint.setColor(color);
            }
        }

        /**
         * Shader的扩展
         */
        private static class TShaderSpan extends CharacterStyle implements UpdateAppearance {
            private final Shader mShader;

            private TShaderSpan(@NonNull final Shader shader) {
                this.mShader = shader;
            }

            @Override
            public void updateDrawState(final TextPaint tp) {
                tp.setShader(mShader);
            }
        }

        /**
         * 阴影的扩展
         */
        private static class TShadowSpan extends CharacterStyle implements UpdateAppearance {
            private final float radius;
            private final float dx, dy;
            private final int shadowColor;

            private TShadowSpan(final float radius, final float dx, final float dy, @ColorInt final int shadowColor) {
                this.radius = radius;
                this.dx = dx;
                this.dy = dy;
                this.shadowColor = shadowColor;
            }

            @Override
            public void updateDrawState(final TextPaint tp) {
                tp.setShadowLayer(radius, dx, dy, shadowColor);
            }
        }
    }
}
