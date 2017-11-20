package com.tk.tdroid.utils;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import com.tk.tdroid.constants.TConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import static com.tk.tdroid.utils.Logger.Type.A;
import static com.tk.tdroid.utils.Logger.Type.D;
import static com.tk.tdroid.utils.Logger.Type.E;
import static com.tk.tdroid.utils.Logger.Type.I;
import static com.tk.tdroid.utils.Logger.Type.V;
import static com.tk.tdroid.utils.Logger.Type.W;


/**
 * <pre>
 *     author : TK
 *     time   : 2017/9/13
 *     desc   : 打Log,默认记录日志的路径，在{@link Context#getFilesDir()}目录下{@link TConstants#LOGGER_DIR}
 * </pre>
 */
public final class Logger {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({V, D, I, W, E, A})
    public @interface Type {
        int V = Log.VERBOSE;
        int D = Log.DEBUG;
        int I = Log.INFO;
        int W = Log.WARN;
        int E = Log.ERROR;
        int A = Log.ASSERT;
    }

    /**
     * 默认TAG
     */
    public static final String TAG = "Logger";
    /**
     * 默认总开关
     */
    private static final boolean LOG = true;
    /**
     * 默认是否打印日志到控制台
     */
    private static final boolean PRINT = true;
    /**
     * 默认是否显示边框
     */
    private static final boolean BORDER = true;
    /**
     * 默认是否显示方法栈
     */
    private static final boolean HEADER = true;
    /**
     * Log头的方法栈深度
     */
    private static final int LOG_STACK_DEPTH = 1;
    /**
     * 默认是否记录日志
     */
    private static final boolean SAVE_LOG = false;
    /**
     * 单行Log最大长度
     */
    private static final int MAX_LENGTH = 3200;

    private static final String LINE_SEP = System.getProperty("line.separator");
    private static final String TOP_BORDER = "╔═══════════════════════════════════════════════════════════════════════════════════════════════════";
    private static final String SPLIT_BORDER = "╟───────────────────────────────────────────────────────────────────────────────────────────────────";
    private static final String LEFT_BORDER = "║ ";
    private static final String BOTTOM_BORDER = "╚═══════════════════════════════════════════════════════════════════════════════════════════════════";

    private static final String PARAM = "Param";
    private static final String NULL = "null";

    private static final int HIGH = 0xF0;
    private static final int LOW = 0x0F;

    private static final int JSON = 0x10;
    private static final int XML = 0x20;
    private static ArrayMap<Integer, String> map = null;
    /**
     * 全局配置
     */
    private static Config globalConfig = new Builder().build();
    /**
     * Log写入文件单例线程池
     */
    private static ExecutorService executorService;

    /**
     * 初始化
     *
     * @param globalConfig
     */
    public static void init(@NonNull Config globalConfig) {
        Logger.globalConfig = globalConfig;
    }

    /**
     * 获取全局配置
     *
     * @return
     */
    public static Config getGlobalConfig() {
        return globalConfig;
    }

    /**
     * 初始化日志写入
     */
    private static void initLogWriter() {
        if (map == null) {
            map = new ArrayMap<>(8);
            map.put(V, "VERBOSE");
            map.put(D, "DEBUG");
            map.put(I, "INFO");
            map.put(W, "WARN");
            map.put(E, "ERROR");
            map.put(A, "ASSERT");
            map.put(JSON, "JSON");
            map.put(XML, "XML");
        }
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
    }

    public static void v(@Nullable Object msg) {
        printLog(V, null, msg);
    }

    public static void v(@NonNull String tag, @Nullable Object... objects) {
        printLog(V, globalConfig.newBuilder().tag(tag).build(), objects);
    }

    public static void v(@Nullable Config config, @Nullable Object... objects) {
        printLog(V, config, objects);
    }

    public static void d(@Nullable Object msg) {
        printLog(D, null, msg);
    }

    public static void d(@NonNull String tag, @Nullable Object... objects) {
        printLog(D, globalConfig.newBuilder().tag(tag).build(), objects);
    }

    public static void d(@Nullable Config config, @Nullable Object... objects) {
        printLog(D, config, objects);
    }

    public static void i(@Nullable Object msg) {
        printLog(I, null, msg);
    }

    public static void i(@NonNull String tag, @Nullable Object... objects) {
        printLog(I, globalConfig.newBuilder().tag(tag).build(), objects);
    }

    public static void i(@Nullable Config config, @Nullable Object... objects) {
        printLog(I, config, objects);
    }

    public static void w(@Nullable Object msg) {
        printLog(W, null, msg);
    }

    public static void w(@NonNull String tag, @Nullable Object... objects) {
        printLog(W, globalConfig.newBuilder().tag(tag).build(), objects);
    }

    public static void w(@Nullable Config config, @Nullable Object... objects) {
        printLog(W, config, objects);
    }

    public static void e(@Nullable Object msg) {
        printLog(E, null, msg);
    }

    public static void e(@NonNull String tag, @Nullable Object... objects) {
        printLog(E, globalConfig.newBuilder().tag(tag).build(), objects);
    }

    public static void e(@Nullable Config config, @Nullable Object... objects) {
        printLog(E, config, objects);
    }

    public static void a(@Nullable Object msg) {
        printLog(A, null, msg);
    }

    public static void a(@NonNull String tag, @Nullable Object... objects) {
        printLog(A, globalConfig.newBuilder().tag(tag).build(), objects);
    }

    public static void a(@Nullable Config config, @Nullable Object... objects) {
        printLog(A, config, objects);
    }

    public static void json(@Nullable String json) {
        printLog(JSON | D, null, json);
    }

    public static void json(@NonNull String tag, @Nullable String json) {
        printLog(JSON | D, globalConfig.newBuilder().tag(tag).build(), json);
    }

    public static void json(@Type int type, @Nullable Config config, @Nullable String json) {
        printLog(JSON | type, config, json);
    }

    public static void xml(@Nullable String xml) {
        printLog(XML | D, null, xml);
    }

    public static void xml(@NonNull String tag, String xml) {
        printLog(XML | D, globalConfig.newBuilder().tag(tag).build(), xml);
    }

    public static void xml(@Type int type, @Nullable Config config, @Nullable String xml) {
        printLog(XML | type, config, xml);
    }

    /**
     * 输出日志
     *
     * @param type
     * @param config
     * @param objects
     */
    private static void printLog(int type, @Nullable Config config, @Nullable Object... objects) {
        Config realConfig = config == null ? globalConfig : config;

        String tagStr = realConfig == null ? TAG : realConfig.tag;
        String[] headers = generateHeader(realConfig == null ? LOG_STACK_DEPTH : realConfig.logStackDepth);
        String bodyStr = generateBody(type & HIGH, objects);

        boolean print = realConfig == null ? PRINT : realConfig.print;
        boolean saveLog = realConfig == null ? SAVE_LOG : realConfig.saveLog;
        boolean border = realConfig == null ? BORDER : realConfig.border;
        boolean header = realConfig == null ? HEADER : realConfig.header;
        String logPath = realConfig == null ? null : realConfig.logPath;
        if (TextUtils.isEmpty(logPath)) {
            logPath = Utils.getApp().getFilesDir().getAbsolutePath() + File.separator + TConstants.LOGGER_DIR;
        }
        if (print) {
            print2console(type & LOW, tagStr, headers, bodyStr, border, header);
        }
        if (saveLog) {
            print2file(type, tagStr, headers, bodyStr, logPath);
        }
    }

    /**
     * 生成头
     *
     * @param logStackDepth
     * @return
     */
    private static String[] generateHeader(int logStackDepth) {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        int lastStack = Math.max(logStackDepth, 1) + 3;
        lastStack = lastStack > stackTrace.length ? stackTrace.length : lastStack;

        String[] headers = new String[lastStack - 3];
        StackTraceElement targetElement;
        for (int i = 3; i < lastStack; i++) {
            targetElement = stackTrace[i];
            String threadName = Thread.currentThread().getName();
            String fileName = targetElement.getFileName();
            String className = targetElement.getClassName();
            String methodName = targetElement.getMethodName();
            int lineNumber = targetElement.getLineNumber();
            headers[i - 3] = String.format(Locale.getDefault(), "%s, %s(%s:%d), %s", threadName, methodName, fileName, lineNumber, className);
        }
        return headers;
    }

    /**
     * 生成主体
     *
     * @param type
     * @param objects
     * @return
     */
    private static String generateBody(final int type, @Nullable final Object... objects) {
        String body = NULL;
        if (!EmptyUtils.isEmpty(objects)) {
            if (objects.length == 1) {
                if (type == JSON) {
                    body = objects[0] == null ? NULL : fromJson(objects[0].toString());
                    body += LINE_SEP;
                    return body;
                } else if (type == XML) {
                    body = objects[0] == null ? NULL : fromXml(objects[0].toString());
                    body += LINE_SEP;
                    return body;
                }
            }
            StringBuilder sb = new StringBuilder();
            Object obj;
            for (int i = 0, len = objects.length; i < len; ++i) {
                obj = objects[i];
                sb.append(PARAM).append("[").append(i).append("]")
                        .append(" = ");
                if (obj == null) {
                    sb.append(NULL);
                } else if (obj instanceof File) {
                    sb.append(fromFile((File) obj));
                } else {
                    sb.append(obj.toString());
                }
                sb.append(LINE_SEP);
            }
            body = sb.toString();
        }
        return body;
    }

    /**
     * File
     *
     * @param file
     * @return
     */
    private static String fromFile(@NonNull File file) {
        return String.format(Locale.getDefault(), "File: %s:%s", file.getAbsolutePath(), FileUtils.getFileSize(file));
    }

    /**
     * Json
     *
     * @param json
     * @return
     */
    private static String fromJson(@NonNull String json) {
        try {
            if (json.startsWith("{")) {
                json = new JSONObject(json).toString(4);
            } else if (json.startsWith("[")) {
                json = new JSONArray(json).toString(4);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * XML
     *
     * @param xml
     * @return
     */
    private static String fromXml(@NonNull String xml) {
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(xmlInput, xmlOutput);
            xml = xmlOutput.getWriter().toString().replaceFirst(">", ">" + LINE_SEP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xml;
    }

    /**
     * 输出到控制台
     *
     * @param type
     * @param tagStr
     * @param headers
     * @param bodyStr
     * @param border
     * @param header
     */
    private static void print2console(int type, @NonNull String tagStr, @NonNull String[] headers, @NonNull String bodyStr,
                                      boolean border, boolean header) {
        if (border) {
            Log.println(type, tagStr, TOP_BORDER);
        }
        if (header) {
            for (String headerStr : headers) {
                Log.println(type, tagStr, border ? LEFT_BORDER + headerStr : headerStr);
            }
            if (border) {
                Log.println(type, tagStr, SPLIT_BORDER);
            }
        }
        printBody(type, tagStr, bodyStr, border);
        if (border) {
            Log.println(type, tagStr, BOTTOM_BORDER);
        }
    }

    /**
     * 打印主体部分
     *
     * @param type
     * @param tagStr
     * @param bodyStr
     * @param border
     */
    private static void printBody(int type, @NonNull String tagStr, @NonNull String bodyStr, boolean border) {
        int len = bodyStr.length();
        int count = len / MAX_LENGTH;
        if (count > 0) {
            int index = 0;
            for (int i = 0; i < count; i++) {
                printContent(type, tagStr, bodyStr.substring(index, index + MAX_LENGTH), border);
                index += MAX_LENGTH;
            }
            if (index != len) {
                printContent(type, tagStr, bodyStr.substring(index, len), border);
            }
        } else {
            printContent(type, tagStr, bodyStr, border);
        }
    }

    /**
     * 逐一打印内容
     *
     * @param type
     * @param tagStr
     * @param contentStr
     * @param border
     */
    private static void printContent(int type, @NonNull String tagStr, @NonNull String contentStr, boolean border) {
        if (!border) {
            Log.println(type, tagStr, contentStr);
            return;
        }
        String[] lines = contentStr.split(LINE_SEP);
        for (String line : lines) {
            Log.println(type, tagStr, LEFT_BORDER + line);
        }
    }

    /**
     * 输出到存储位置
     *
     * @param type
     * @param tagStr
     * @param headers
     * @param bodyStr
     * @param logPath
     */
    private static void print2file(int type, @NonNull String tagStr,
                                   @NonNull String[] headers, @NonNull String bodyStr, @NonNull String logPath) {
        String date = TimeUtils.formatExactDate(System.currentTimeMillis());
        String day = date.substring(0, date.lastIndexOf(" "));
        final File file = new File(logPath, String.format(Locale.getDefault(), "%s_%s.log", TAG, day));
        if (!FileUtils.createOrExistsFile(file)) {
            Log.e(tagStr, "print2file failure !");
            return;
        }
        initLogWriter();
        final StringBuilder builder = new StringBuilder();
        builder.append(date)
                .append(" ");
        if (type < LOW) {
            builder.append(map.get(type));
        } else {
            builder.append(map.get(type & HIGH))
                    .append("+")
                    .append(map.get(type & LOW));
        }
        builder.append(" ")
                .append(tagStr)
                .append(LINE_SEP);
        for (String headerStr : headers) {
            builder.append(headerStr)
                    .append(LINE_SEP);
        }
        builder.append(bodyStr)
                .append(LINE_SEP);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                FileIOUtils.writeStringByNIO(file, builder.toString(), true);
            }
        });
    }

    /**
     * 配置
     */
    public static class Config {
        private String tag;
        private boolean log;
        private boolean print;
        private boolean border;
        private boolean header;
        private int logStackDepth;
        private boolean saveLog;
        private String logPath;

        private Config(Builder builder) {
            tag = builder.tag;
            log = builder.log;
            print = builder.print;
            border = builder.border;
            header = builder.header;
            logStackDepth = builder.logStackDepth;
            saveLog = builder.saveLog;
            logPath = builder.logPath;
        }

        public Builder newBuilder() {
            return new Builder()
                    .tag(tag)
                    .log(log)
                    .print(print)
                    .border(border)
                    .header(header)
                    .logStackDepth(logStackDepth)
                    .saveLog(saveLog)
                    .logPath(logPath);
        }
    }

    public static final class Builder {
        private String tag = Logger.TAG;
        private boolean log = Logger.LOG;
        private boolean print = Logger.PRINT;
        private boolean border = Logger.BORDER;
        private boolean header = Logger.HEADER;
        private int logStackDepth = Logger.LOG_STACK_DEPTH;
        private boolean saveLog = Logger.SAVE_LOG;
        private String logPath = null;

        public Builder() {
        }

        /**
         * 通用Tag头
         *
         * @param tag
         * @return
         */
        public Builder tag(@NonNull String tag) {
            this.tag = tag;
            return this;
        }

        /**
         * 是否启用日志功能
         *
         * @param log
         * @return
         */
        public Builder log(boolean log) {
            this.log = log;
            return this;
        }

        /**
         * 是否打印日志到控制台显示
         *
         * @param print
         * @return
         */
        public Builder print(boolean print) {
            this.print = print;
            return this;
        }

        /**
         * 打印的日志是否带有边框
         *
         * @param border
         * @return
         */
        public Builder border(boolean border) {
            this.border = border;
            return this;
        }

        /**
         * 打印的日志是否带有头信息（方法栈）
         *
         * @param header
         * @return
         */
        public Builder header(boolean header) {
            this.header = header;
            return this;
        }

        /**
         * 打印的日志头，方法栈深度
         *
         * @param logStackDepth 默认深度1，当前调用者的信息
         * @return
         */
        public Builder logStackDepth(@IntRange(from = 1) int logStackDepth) {
            this.logStackDepth = logStackDepth;
            return this;
        }

        /**
         * 是否记录日志
         *
         * @param saveLog
         * @return
         */
        public Builder saveLog(boolean saveLog) {
            this.saveLog = saveLog;
            return this;
        }

        /**
         * 记录日志的路径，默认在应用缓存目录下
         *
         * @param logPath
         * @return
         */
        public Builder logPath(@Nullable String logPath) {
            this.logPath = logPath;
            return this;
        }

        public Config build() {
            return new Config(this);
        }
    }
}
