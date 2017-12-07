package com.tk.tdroiddemo.sample;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import com.tk.tdroid.base.BaseActivity;
import com.tk.tdroid.utils.SpannableHelper;
import com.tk.tdroid.utils.ViewUtils;
import com.tk.tdroiddemo.R;

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

        Bitmap bitmap_1 = ViewUtils.drawable2Bitmap(android.R.drawable.ic_input_add, 24, 24);
        Bitmap bitmap_2 = ViewUtils.drawable2Bitmap(R.mipmap.ic_launcher, 160, 160);

        Shader linearShader = new LinearGradient(0, 0, text.getPaint().measureText("这是一个渐变文本") * 2, 0,
                new int[]{Color.GREEN, Color.BLUE, Color.RED},
                new float[]{0F, 0.5F, 1F}, Shader.TileMode.CLAMP);

        text.setText(new SpannableHelper.Builder()
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
                .append("这是一个").appendLine("边框").border(2, Color.RED)
                .append("这是一个").appendLine("点击事件").click(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(getApplicationContext(), "点击事件", Toast.LENGTH_SHORT).show();
                    }
                })
                .append("这是一个").appendLine("超链接").url("https://www.github.com/")
                .append("对齐方式：")
                .appendImage(bitmap_1, SpannableHelper.Builder.Align.ALIGN_TOP)
                .appendImage(bitmap_1, SpannableHelper.Builder.Align.ALIGN_CENTER)
                .appendImage(bitmap_1, SpannableHelper.Builder.Align.ALIGN_BASELINE)
                .appendImage(bitmap_1, SpannableHelper.Builder.Align.ALIGN_BOTTOM)
                .appendLine()
                .append("顶部对齐：").appendImage(bitmap_2, SpannableHelper.Builder.Align.ALIGN_TOP).appendLine("：顶部对齐 ")
                .append("居中对齐：").appendImage(bitmap_2, SpannableHelper.Builder.Align.ALIGN_CENTER).appendLine("：居中对齐 ")
                .append("基线对齐：").appendImage(bitmap_2, SpannableHelper.Builder.Align.ALIGN_BASELINE).appendLine("：基线对齐 ")
                .append("底部对齐：").appendImage(bitmap_2, SpannableHelper.Builder.Align.ALIGN_BOTTOM).appendLine("：底部对齐 ")
                .appendLine("行内顶部对齐").backgroundColor(Color.RED).lineHeight(150, SpannableHelper.Builder.Align.ALIGN_TOP)
                .appendLine("行内居中对齐").backgroundColor(Color.BLUE).lineHeight(150, SpannableHelper.Builder.Align.ALIGN_CENTER)
                .appendLine("行内底部对齐").backgroundColor(Color.YELLOW).lineHeight(150, SpannableHelper.Builder.Align.ALIGN_BOTTOM)
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
                .build());
    }

}
