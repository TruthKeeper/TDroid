package com.tk.tdroid.utils;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


/**
 * <pre>
 *     author : TK
 *     time   : 2017/9/13
 *     desc   : 打Log
 * </pre>
 */
public final class Logger {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({V, D, I, W, E, A})
    public @interface Type {

    }

    public static final int V = Log.VERBOSE;
    public static final int D = Log.DEBUG;
    public static final int I = Log.INFO;
    public static final int W = Log.WARN;
    public static final int E = Log.ERROR;
    public static final int A = Log.ASSERT;
    /**
     * 默认TAG
     */
    private static String TAG = "Logger";
    /**
     * 总开关
     */
    private static boolean log = true;
    /**
     * 是否打印日志到控制台
     */
    private static boolean print = true;
    /**
     * 是否显示边框
     */
    private static boolean border = true;
    /**
     * 是否显示方法栈
     */
    private static boolean header = true;
    /**
     * 是否记录日志
     */
    private static boolean saveLog = true;
    /**
     * 记录日志的路径，默认在缓存文件下
     */
    private static String logPath = null;
    /**
     * 单行最大长度
     */
    private static final int MAX_LENGTH = 3200;

    private static final String LINE_SEP = System.getProperty("line.separator");
    private static final String TOP_BORDER = "╔═══════════════════════════════════════════════════════════════════════════════════════════════════";
    private static final String SPLIT_BORDER = "╟───────────────────────────────────────────────────────────────────────────────────────────────────";
    private static final String LEFT_BORDER = "║ ";
    private static final String BOTTOM_BORDER = "╚═══════════════════════════════════════════════════════════════════════════════════════════════════";

    private static final String PARAM = "Param";
    private static final String NULL = "null";

    private static ArrayMap<Integer, String> map = null;

    private static final int HIGH = 0xF0;
    private static final int LOW = 0x0F;

    private static final int FILE = 0x10;
    private static final int JSON = 0x20;
    private static final int XML = 0x30;

    private static ExecutorService executorService;

    /**
     * 初始化
     *
     * @param config
     */
    public static void init(@NonNull Config config) {
        TAG = config.getTag();
        log = config.isLog();
        print = config.isPrint();
        border = config.isBorder();
        header = config.isHeader();
        saveLog = config.isSaveLog();
        logPath = config.getLogPath();
    }

    /**
     * 初始化日志写入
     */
    private static void initLogWriter() {
        if (map == null) {
            map = new ArrayMap<>();
            map.put(V, "VERBOSE");
            map.put(D, "DEBUG");
            map.put(I, "INFO");
            map.put(W, "WARN");
            map.put(E, "ERROR");
            map.put(A, "ASSERT");
            map.put(FILE, "FILE");
            map.put(JSON, "JSON");
            map.put(XML, "XML");
        }
        if (executorService == null) {
            //单线程的线程池
            executorService = Executors.newSingleThreadExecutor();
        }
    }

    public static void v(Object msg) {
        printLog(V, null, msg);
    }

    public static void v(String tag, Object... objects) {
        printLog(V, tag, objects);
    }

    public static void d(Object msg) {
        printLog(D, null, msg);
    }

    public static void d(String tag, Object... objects) {
        printLog(D, tag, objects);
    }

    public static void i(Object msg) {
        printLog(I, null, msg);
    }

    public static void i(String tag, Object... objects) {
        printLog(I, tag, objects);
    }

    public static void w(Object msg) {
        printLog(W, null, msg);
    }

    public static void w(String tag, Object... objects) {
        printLog(W, tag, objects);
    }

    public static void e(Object msg) {
        printLog(E, null, msg);
    }

    public static void e(String tag, Object... objects) {
        printLog(E, tag, objects);
    }


    public static void a(Object msg) {
        printLog(A, null, msg);
    }

    public static void a(String tag, Object... objects) {
        printLog(A, tag, objects);
    }

    public static void file(File file) {
        printLog(FILE | D, null, file);
    }

    public static void file(String tag, File file) {
        printLog(FILE | D, tag, file);
    }

    public static void file(@Type int type, String tag, File file) {
        printLog(FILE | type, tag, file);
    }

    public static void json(String json) {
        printLog(JSON | D, null, json);
    }

    public static void json(String tag, String json) {
        printLog(JSON | D, tag, json);
    }

    public static void json(@Type int type, String tag, String json) {
        printLog(JSON | type, tag, json);
    }

    public static void xml(String xml) {
        printLog(XML | D, null, xml);
    }

    public static void xml(String tag, String xml) {
        printLog(XML | D, tag, xml);
    }

    public static void xml(@Type int type, String tag, String xml) {
        printLog(XML | type, tag, xml);
    }

    /**
     * 输出日志
     *
     * @param type
     * @param tagStr
     * @param objects
     */
    private static void printLog(int type, String tagStr, Object... objects) {
        String header = generateHeader();
        String body = generateBody(type & HIGH, objects);
        String tag = TextUtils.isEmpty(tagStr) ? TAG : tagStr;
        if (print) {
            print2console(type & LOW, tag, header, body);
        }
        if (saveLog) {
            print2file(type, tag, header, body);
        }
    }

    /**
     * 生成头
     *
     * @return
     */
    private static String generateHeader() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        StackTraceElement targetElement = stackTrace[3];
        String threadName = Thread.currentThread().getName();
        String fileName = targetElement.getFileName();
        String className = targetElement.getClassName();
        String methodName = targetElement.getMethodName();
        int lineNumber = targetElement.getLineNumber();
        return String.format("%s, %s(%s:%d), %s", threadName, methodName, fileName, lineNumber, className);
    }

    /**
     * 生成主体
     *
     * @param type
     * @param objects
     * @return
     */
    private static String generateBody(final int type, final Object... objects) {
        String body = NULL;
        if (!CollectionUtils.isEmpty(objects)) {
            if (objects.length == 1) {
                Object object = objects[0];
                body = object == null ? NULL : object.toString();
                if (type == FILE && object instanceof File) {
                    body = fromFile((File) object);
                } else if (type == JSON) {
                    body = fromJson(body);
                } else if (type == XML) {
                    body = fromXml(body);
                }
                body += LINE_SEP;
            } else {
                StringBuilder sb = new StringBuilder();
                Object obj;
                for (int i = 0, len = objects.length; i < len; ++i) {
                    obj = objects[i];
                    sb.append(PARAM).append("[").append(i).append("]")
                            .append(" = ")
                            .append(obj == null ? NULL : obj.toString())
                            .append(LINE_SEP);
                }
                body = sb.toString();
            }
        }
        return body;
    }

    /**
     * File
     *
     * @param file
     * @return
     */
    private static String fromFile(File file) {
        return String.format("File: %s %s", file.getAbsolutePath(), FileUtils.getFileSizeFormat(file, 2));
    }

    /**
     * Json
     *
     * @param json
     * @return
     */
    private static String fromJson(String json) {
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
    private static String fromXml(String xml) {
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
     * @param tag
     * @param header
     * @param body
     */
    private static void print2console(int type, String tag, String header, String body) {
        if (border) {
            Log.println(type, tag, TOP_BORDER);
        }
        if (Logger.header) {
            Log.println(type, tag, border ? LEFT_BORDER + header : header);
            if (border) {
                Log.println(type, tag, SPLIT_BORDER);
            }
        }
        printBody(type, tag, body);
        if (border) {
            Log.println(type, tag, BOTTOM_BORDER);
        }
    }

    /**
     * 打印主体部分
     *
     * @param type
     * @param tag
     * @param body
     */
    private static void printBody(int type, String tag, String body) {
        int len = body.length();
        int count = len / MAX_LENGTH;
        if (count > 0) {
            int index = 0;
            for (int i = 0; i < count; i++) {
                printContent(type, tag, body.substring(index, index + MAX_LENGTH));
                index += MAX_LENGTH;
            }
            if (index != len) {
                printContent(type, tag, body.substring(index, len));
            }
        } else {
            printContent(type, tag, body);
        }
    }

    /**
     * 逐一打印内容
     *
     * @param type
     * @param tag
     * @param content
     */
    private static void printContent(int type, String tag, String content) {
        if (!border) {
            Log.println(type, tag, content);
            return;
        }
        String[] lines = content.split(LINE_SEP);
        for (String line : lines) {
            Log.println(type, tag, LEFT_BORDER + line);
        }
    }

    /**
     * 输出到存储位置
     *
     * @param type
     * @param tag
     * @param header
     * @param body
     */
    private static void print2file(int type, String tag, String header, String body) {
        String date = FormatUtils.formatExactDate(System.currentTimeMillis());
        String day = date.substring(0, date.lastIndexOf("\t"));
        final File file = new File(TextUtils.isEmpty(logPath) ? Utils.getApp().getCacheDir().getAbsolutePath() : logPath,
                String.format("Log-%s.log", day));
        if (!createLogFile(file)) {
            Log.e(tag, "print2file failure !");
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
                .append(tag)
                .append(LINE_SEP)
                .append(header)
                .append(LINE_SEP)
                .append(body)
                .append(LINE_SEP);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new FileWriter(file, true));
                    writer.write(builder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(writer);
                }
            }
        });
    }

    /**
     * 创建日志
     *
     * @param file
     * @return
     */
    private static boolean createLogFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                return true;
            }
            file.delete();
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 配置
     */
    public static class Config {
        private String tag = Logger.TAG;
        private boolean log = true;
        private boolean print = true;
        private boolean border = true;
        private boolean header = true;
        private boolean saveLog = true;
        private String logPath = null;

        /**
         * 通用Tag头
         *
         * @param tag
         * @return
         */
        public Config tag(String tag) {
            this.tag = tag;
            return this;
        }

        /**
         * 是否启用日志功能
         *
         * @param log
         * @return
         */
        public Config log(boolean log) {
            this.log = log;
            return this;
        }

        /**
         * 是否打印日志到控制台显示
         *
         * @param print
         * @return
         */
        public Config print(boolean print) {
            this.print = print;
            return this;
        }

        /**
         * 打印的日志是否带有边框
         *
         * @param border
         * @return
         */
        public Config border(boolean border) {
            this.border = border;
            return this;
        }

        /**
         * 打印的日志是否带有头信息（方法栈）
         *
         * @param header
         * @return
         */
        public Config header(boolean header) {
            this.header = header;
            return this;
        }

        /**
         * 是否记录日志
         *
         * @param saveLog
         * @return
         */
        public Config saveLog(boolean saveLog) {
            this.saveLog = saveLog;
            return this;
        }

        /**
         * 记录日志的路径
         *
         * @param logPath
         * @return
         */
        public Config logPath(String logPath) {
            this.logPath = logPath;
            return this;
        }

        public Config build() {
            return new Config();
        }

        public String getTag() {
            return tag;
        }

        public boolean isLog() {
            return log;
        }

        public boolean isPrint() {
            return print;
        }

        public boolean isBorder() {
            return border;
        }

        public boolean isHeader() {
            return header;
        }

        public boolean isSaveLog() {
            return saveLog;
        }

        public String getLogPath() {
            return logPath;
        }
    }
}
