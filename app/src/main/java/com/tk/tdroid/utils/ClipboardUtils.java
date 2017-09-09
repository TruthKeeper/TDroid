package com.tk.tdroid.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/03/17
 *     desc   : 粘贴板工具
 * </pre>
 */
public final class ClipboardUtils {
    private ClipboardUtils() {
        throw new IllegalStateException();
    }

    /**
     * 复制文本到剪贴板
     *
     * @param context
     * @param text
     */
    public static void copyText(@NonNull Context context, @NonNull CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("text", text));
    }

    /**
     * 获取剪贴板的文本
     *
     * @param context
     * @return
     */
    public static CharSequence getText(@NonNull Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).coerceToText(context);
        }
        return null;
    }

    /**
     * 复制uri到剪贴板
     *
     * @param context
     * @param uri
     */
    public static void copyUri(@NonNull Context context, @NonNull Uri uri) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newUri(context.getContentResolver(), "uri", uri));
    }

    /**
     * 获取剪贴板的uri
     *
     * @param context
     * @return
     */
    public static Uri getUri(@NonNull Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).getUri();
        }
        return null;
    }
}
