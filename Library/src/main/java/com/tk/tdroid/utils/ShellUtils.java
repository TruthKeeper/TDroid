package com.tk.tdroid.utils;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * <pre>
 *      author : TK
 *      time : 2017/10/24
 *      desc : Shell命令工具类
 * </pre>
 */

public final class ShellUtils {
    private ShellUtils() {
        throw new IllegalStateException();
    }

    /**
     * 执行命令
     *
     * @param cmd    命令
     * @param isRoot 是否需要root权限
     * @return
     */
    public static CmdResult executeCmd(@NonNull String cmd, boolean isRoot) {
        return executeCmd(new String[]{cmd}, isRoot, true);
    }

    /**
     * 执行命令
     *
     * @param cmds   多条命令
     * @param isRoot 是否需要root权限
     * @return
     */
    public static CmdResult executeCmd(@NonNull List<String> cmds, boolean isRoot) {
        return executeCmd(cmds.toArray(new String[]{}), isRoot, true);
    }

    /**
     * 执行命令
     *
     * @param cmds   多条命令
     * @param isRoot 是否需要root权限
     * @return
     */
    public static CmdResult executeCmd(@NonNull String[] cmds, boolean isRoot) {
        return executeCmd(cmds, isRoot, true);
    }


    /**
     * 是否是在root下执行命令
     *
     * @param cmds   命令数组
     * @param isRoot 是否需要root权限执行
     * @param result 是否需要执行结果消息
     * @return
     */
    public static CmdResult executeCmd(@NonNull String[] cmds, boolean isRoot, final boolean result) {
        int resultCode = -1;
        if (EmptyUtils.isEmpty(cmds)) {
            return new CmdResult(resultCode, null, null);
        }
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        DataOutputStream outputStream = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? "su" : "sh");
            outputStream = new DataOutputStream(process.getOutputStream());
            for (String command : cmds) {
                if (command == null) continue;
                outputStream.write(command.getBytes());
                outputStream.writeBytes("\n");
                outputStream.flush();
            }
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            resultCode = process.waitFor();
            if (result) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
                String s;
                while ((s = successResult.readLine()) != null) {
                    successMsg.append(s);
                }
                while ((s = errorResult.readLine()) != null) {
                    errorMsg.append(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outputStream, successResult, errorResult);
            if (process != null) {
                process.destroy();
            }
        }
        return new CmdResult(resultCode,
                successMsg == null ? null : successMsg.toString(),
                errorMsg == null ? null : errorMsg.toString());
    }

    /**
     * 返回的Cmd结果
     */
    public static class CmdResult {
        /**
         * 结果码
         **/
        private final int result;
        /**
         * 成功信息
         **/
        private final String successMsg;
        /**
         * 错误信息
         **/
        private final String errorMsg;

        public CmdResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }

        public int getResult() {
            return result;
        }

        public String getSuccessMsg() {
            return successMsg;
        }

        public String getErrorMsg() {
            return errorMsg;
        }
    }
}
