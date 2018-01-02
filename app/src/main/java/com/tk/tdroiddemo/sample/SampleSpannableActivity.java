package com.tk.tdroiddemo.sample;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;

import com.tk.tdroid.base.BaseActivity;
import com.tk.tdroid.utils.DensityUtil;
import com.tk.tdroid.utils.ImageUtils;
import com.tk.tdroid.utils.SpannableFactory;
import com.tk.tdroid.utils.Toasty;
import com.tk.tdroiddemo.R;

import java.util.List;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/7
 *      desc :
 * </pre>
 */

public class SampleSpannableActivity extends BaseActivity {
    private Toolbar toolbar;
    private AppCompatTextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_spannable);
        toolbar = findViewById(R.id.toolbar);
        text = findViewById(R.id.text);
        text.setMovementMethod(LinkMovementMethod.getInstance());
        text.setTextSize(16);
        text.setTextColor(0xFF969696);

        int largeSize = DensityUtil.dp2px(60);
        int smallSize = DensityUtil.dp2px(8);

        Drawable emoji0 = ContextCompat.getDrawable(this, R.drawable.emoji0);
        emoji0.setBounds(0, 0, DensityUtil.dp2px(24), DensityUtil.dp2px(24));
        Drawable emoji1 = ContextCompat.getDrawable(this, R.drawable.emoji1);
        emoji1.setBounds(0, 0, DensityUtil.dp2px(24), DensityUtil.dp2px(24));
        Drawable emoji2 = ContextCompat.getDrawable(this, R.drawable.emoji2);
        emoji2.setBounds(0, 0, DensityUtil.dp2px(24), DensityUtil.dp2px(24));

        Shader linearShader = new LinearGradient(0, 0, text.getPaint().measureText("这是一个渐变文本") * 2, 0,
                new int[]{Color.GREEN, Color.BLUE, Color.RED},
                new float[]{0F, 0.5F, 1F}, Shader.TileMode.CLAMP);
        text.setText(new SpannableFactory.Builder()
                .appendLine("前景色_色值").foregroundColor(Color.BLUE)
                .appendLine("前景色_资源").foregroundResColor(R.color.colorAccent)
                .appendLine("背景色_色值").backgroundColor(Color.BLUE)
                .appendLine("背景色_资源").backgroundResColor(R.color.colorAccent)
                .appendLine("加粗").bold()
                .appendLine("倾斜").italic()
                .appendLine("粗斜体").boldItalic()
                .appendLine("删除线").strikeThrough()
                .appendLine("下划线").underline()
                .append("这是一个").appendLine("上标").superscript()
                .append("这是一个").appendLine("下标").subscript()
                .appendLine("32sp字体").fontSize(32)
                .appendLine("32dp字体").fontSize(32, true)
                .appendLine("1.5倍字体").fontProportion(1.5F)
                .appendLine("横向2倍字体").fontXProportion(2F)
                .append("这是一个").append("边框").border(2, Color.RED, 10, 12).appendLine()
                .append("这是一个").appendLine("点击事件").click(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Toasty.show("点击事件", null);
                    }
                })
                .append("这是一个").appendLine("超链接").url("https://www.github.com/")
                .append("顶部、居中、基线、底部对齐：")
                .appendImage(ImageUtils.color2Bitmap(Color.RED, smallSize, smallSize), SpannableFactory.Align.ALIGN_TOP)
                .appendImage(ImageUtils.color2Bitmap(Color.YELLOW, smallSize, smallSize), SpannableFactory.Align.ALIGN_CENTER)
                .appendImage(ImageUtils.color2Bitmap(Color.BLUE, smallSize, smallSize), SpannableFactory.Align.ALIGN_BASELINE)
                .appendImage(ImageUtils.color2Bitmap(Color.BLACK, smallSize, smallSize), SpannableFactory.Align.ALIGN_BOTTOM)
                .appendLine()
                .append("顶部对齐：").appendImage(ImageUtils.color2Bitmap(Color.RED, largeSize, largeSize), SpannableFactory.Align.ALIGN_TOP).append("：顶部对齐").appendLine()
                .append("居中对齐：").appendImage(ImageUtils.color2Bitmap(Color.YELLOW, largeSize, largeSize), SpannableFactory.Align.ALIGN_CENTER).append("：居中对齐").appendLine()
                .append("基线对齐：").appendImage(ImageUtils.color2Bitmap(Color.BLUE, largeSize, largeSize), SpannableFactory.Align.ALIGN_BASELINE).append("：基线对齐").appendLine()
                .append("底部对齐：").appendImage(ImageUtils.color2Bitmap(Color.BLACK, largeSize, largeSize), SpannableFactory.Align.ALIGN_BOTTOM).append("：底部对齐").appendLine()
                .appendLine("行内顶部对齐").backgroundColor(Color.RED).lineHeight(150, SpannableFactory.Align.ALIGN_TOP)
                .appendLine("行内居中对齐").backgroundColor(Color.BLUE).lineHeight(150, SpannableFactory.Align.ALIGN_CENTER)
                .appendLine("行内底部对齐").backgroundColor(Color.YELLOW).lineHeight(150, SpannableFactory.Align.ALIGN_BOTTOM)
                .appendLine("首行缩进首行缩进首行缩进首行缩进首行缩进首行缩进首行缩进首行缩进首行缩进").backgroundColor(Color.BLUE).leadingMargin((int) (text.getTextSize() * 2), 0)
                .appendLine("引用线，可配置颜色、宽度、间距\n换行效果").quote(Color.CYAN, 10, 20)
                .appendLine("列表项，可配置颜色、大小、距离\n换行效果").bullet(Color.RED, 16, 8)
                .appendLine("列表项，可配置颜色、大小、距离\n换行效果").bullet(Color.BLUE, 24, 16, false)
                .append("追加20px空格").appendSpace(20).appendLine("end")
                .append("追加50px有色空格").appendSpace(50, Color.YELLOW).appendLine("end")
                .appendLine("正常对齐").alignment(Layout.Alignment.ALIGN_NORMAL).backgroundColor(Color.RED)
                .appendLine("居中对齐").alignment(Layout.Alignment.ALIGN_CENTER).backgroundColor(Color.BLUE)
                .appendLine("反向对齐").alignment(Layout.Alignment.ALIGN_OPPOSITE).backgroundColor(Color.YELLOW)
                .appendLine("这是一个渐变文本").fontProportion(2).shader(linearShader)
                .appendLine("这是一个阴影文本").foregroundColor(Color.RED).fontProportion(2).shadow(4, 10, 10, Color.GRAY)
                .appendLine("支持Emoji场景下的ImageTags设置:")
                .appendImage(emoji1, SpannableFactory.Align.ALIGN_CENTER, "[撇嘴]")
                .appendImage(emoji0, SpannableFactory.Align.ALIGN_CENTER, "[微笑]")
                .appendImage(emoji2, SpannableFactory.Align.ALIGN_CENTER, "[色]")
                .appendImage(emoji1, SpannableFactory.Align.ALIGN_CENTER, "[撇嘴]")
                .appendImage(emoji2, SpannableFactory.Align.ALIGN_CENTER, "[色]")
                .build());

        List<String> imageTags = SpannableFactory.findImageTagByText(text.getText());
        text.append("\n↑获取到的ImageTags: " + TextUtils.join(",", imageTags));
    }

}
