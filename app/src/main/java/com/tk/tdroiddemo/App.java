package com.tk.tdroiddemo;

import android.app.Application;

import com.tk.tdroid.utils.internal.Utils;
import com.tk.tdroid.widget.http.HttpUtils;


/**
 * <pre>
 *      author : TK
 *      time : 2017/9/13
 *      desc :
 * </pre>
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        HttpUtils.init(this);
//        Logger.e(getClass().getSimpleName(), "123", true);
//        Logger.json(Logger.Type.E,
//                Logger.getGlobalConfig().newBuilder()
//                        .tag("Json_Test")
//                        .logStackDepth(3),
//                "{\"name\":\"test\"}");
//        Logger.json(Logger.Type.I, null, "{\"name\":\"test\"}");

//        Logger.init(new Logger.Config()
//                .logPath(Environment.getExternalStorageDirectory() + File.separator + "test"));
//        Logger.d(TimeUtils.formatExactDate(new Date().getTime() + 1000 * 100 + 30 * 1000 * 60));
//        Logger.d(TimeUtils.formatHourMinute(1000 * 100 + 30 * 1000 * 60));
//        Logger.d(TimeUtils.formatHourMinuteSecond(1000 * 100 + 30 * 1000 * 60, true));
//        Logger.d(TimeUtils.formatBySurplus(4 * 60 * 60 * 1000 + 1000 * 100 + 30 * 1000 * 60, true));
//        Logger.d(TimeUtils.formatSpanByNow(new Date().getTime() - 1000 * 1000*10));

//        ABC abc = InstanceFactory.create(ABC.class);

//        AppCompatTextView text = (AppCompatTextView) findViewById(R.id.text);
//        text.setMovementMethod(LinkMovementMethod.getInstance());
//        text.setTextSize(16);
//        text.setTextColor(0xFF969696);
//
//        Drawable d1 = toDrawable(this, android.R.drawable.ic_input_add, 32, 32);
//        Drawable d2 = toDrawable(this, R.mipmap.ic_launcher, 160, 160);
//
//        Shader linearShader = new LinearGradient(0, 0, text.getPaint().measureText("这是一个渐变文本") * 2, 0,
//                new int[]{Color.GREEN, Color.BLUE, Color.RED},
//                new float[]{0F, 0.5F, 1F}, Shader.TileMode.CLAMP);
//
//        text.setText(new SpannableHelper.Builder()
//                .appendLine(SpannableHelper.class.getSimpleName()).foregroundResColor(R.color.colorPrimary).bold().fontProportion(2)
//                .appendLine("前景色_色值").foregroundColor(Color.BLUE)
//                .appendLine("前景色_资源").foregroundResColor(R.color.colorAccent)
//                .appendLine("背景色_色值").backgroundColor(Color.BLUE)
//                .appendLine("背景色_资源").backgroundResColor(R.color.colorAccent)
//                .appendLine("加粗").bold()
//                .appendLine("倾斜").italic()
//                .appendLine("粗斜体").boldItalic()
//                .appendLine("删除线").strikeThrough()
//                .appendLine("下划线").underline()
//                .append("这是一个").appendLine("上标").superscript()
//                .append("这是一个").appendLine("下标").subscript()
//                .appendLine("32sp字体").fontSize(32)
//                .appendLine("32dp字体").fontSize(32, true)
//                .appendLine("1.5倍字体").fontProportion(1.5F)
//                .appendLine("横向2倍字体").fontXProportion(2F)
//                .append("这是一个").appendLine("点击事件").click(new ClickableSpan() {
//                    @Override
//                    public void onClick(View widget) {
//                        Toast.makeText(getApplicationContext(), "点击事件", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .append("这是一个").appendLine("超链接").url("https://www.baidu.com/")
//                .append("对齐方式：")
//                .appendImage(d1, SpannableHelper.Builder.Align.ALIGN_TOP)
//                .appendImage(d1, SpannableHelper.Builder.Align.ALIGN_CENTER)
//                .appendImage(d1, SpannableHelper.Builder.Align.ALIGN_BASELINE)
//                .appendImage(d1, SpannableHelper.Builder.Align.ALIGN_BOTTOM)
//                .appendLine()
//                .append("顶部对齐：").appendImage(d2, SpannableHelper.Builder.Align.ALIGN_TOP).appendLine("：顶部对齐 ")
//                .append("居中对齐：").appendImage(d2, SpannableHelper.Builder.Align.ALIGN_CENTER).appendLine("：居中对齐 ")
//                .append("基线对齐：").appendImage(d2, SpannableHelper.Builder.Align.ALIGN_BASELINE).appendLine("：基线对齐 ")
//                .append("底部对齐：").appendImage(d2, SpannableHelper.Builder.Align.ALIGN_BOTTOM).appendLine("：底部对齐 ")
//                .appendLine("行内顶部对齐").backgroundColor(Color.RED).lineHeight(150, SpannableHelper.Builder.Align.ALIGN_TOP)
//                .appendLine("行内居中对齐").backgroundColor(Color.BLUE).lineHeight(150, SpannableHelper.Builder.Align.ALIGN_CENTER)
//                .appendLine("行内底部对齐").backgroundColor(Color.YELLOW).lineHeight(150, SpannableHelper.Builder.Align.ALIGN_BOTTOM)
//                .appendLine("首行缩进首行缩进首行缩进首行缩进首行缩进首行缩进首行缩进首行缩进首行缩进").backgroundColor(Color.BLUE).leadingMargin((int) (text.getTextSize() * 2), 0)
//                .appendLine("引用线，可配置颜色、宽度、间距\n换行效果").quote(Color.CYAN, 10, 20)
//                .appendLine("列表项，可配置颜色、大小、距离\n换行效果").bullet(Color.RED, 16, 8)
//                .appendLine("列表项，可配置颜色、大小、距离\n换行效果").bullet(Color.BLUE, 24, 16, false)
//                .append("追加20px空格").appendSpace(20).appendLine("end")
//                .append("追加50px有色空格").appendSpace(50, Color.YELLOW).appendLine("end")
//                .appendLine("正常对齐").alignment(Layout.Alignment.ALIGN_NORMAL).backgroundColor(Color.RED)
//                .appendLine("居中对齐").alignment(Layout.Alignment.ALIGN_CENTER).backgroundColor(Color.BLUE)
//                .appendLine("反向对齐").alignment(Layout.Alignment.ALIGN_OPPOSITE).backgroundColor(Color.YELLOW)
//                .appendLine("这是一个渐变文本").fontProportion(2).shader(linearShader)
//                .appendLine("这是一个阴影文本").foregroundColor(Color.RED).fontProportion(2).shadow(4, 10, 10, Color.GRAY)
//                .build());

//        GlideApp.with(this)
//                .load(new CacheTokenUrl( ))
//                .load(NetImageUtils.netCrop())
//                .override(image)
//                .into(image);

    }

//    private BitmapDrawable toDrawable(Context context, int resId, int width, int height) {
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        Drawable drawable = ContextCompat.getDrawable(context, resId);
//        drawable.setBounds(0, 0, width, height);
//        drawable.draw(canvas);
//        return new BitmapDrawable(context.getResources(), bitmap);
//    }


//    @Instance
//    public static class ABC {
//    }
}
