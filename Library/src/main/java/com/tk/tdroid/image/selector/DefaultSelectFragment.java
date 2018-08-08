package com.tk.tdroid.image.selector;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;
import android.widget.Toast;

import com.tk.tdroid.utils.FileUtils;
import com.tk.tdroid.utils.Utils;

import java.io.File;
import java.io.IOException;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/7/26
 *     desc   :
 * </pre>
 */
public class DefaultSelectFragment extends Fragment implements ISelector {
    private static final String IMG_PREFIX = "IMG_";
    private static final String IMG_SUFFIX = ".jpeg";

    private File tempFile;
    private File tempCropFile;
    private final SparseArrayCompat<SingleCallback> cameraArray = new SparseArrayCompat<>(2);
    private final SparseArrayCompat<SingleCallback> albumArray = new SparseArrayCompat<>(2);
    private final SparseArrayCompat<SingleCallback> cropCameraArray = new SparseArrayCompat<>(2);
    private final SparseArrayCompat<SingleCallback> cropAlbumArray = new SparseArrayCompat<>(2);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * 开启拍照
     *
     * @param requestCode
     * @param crop
     * @param singleCallback
     */
    @Override
    public void startCamera(int requestCode, boolean crop, @NonNull SingleCallback singleCallback) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (null != intent.resolveActivity(getActivity().getPackageManager())) {
            try {
                tempFile = createTmpImageFile();
                if (tempFile.exists()) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                    if (crop) {
                        cropCameraArray.put(requestCode, singleCallback);
                    } else {
                        cameraArray.put(requestCode, singleCallback);
                    }
                    startActivityForResult(intent, requestCode);
                } else {
                    Toast.makeText(Utils.getApp(), "相册无法保存照片", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(Utils.getApp(), "相册无法保存照片", Toast.LENGTH_SHORT).show();
                FileUtils.deleteFile(tempFile);
            }
        } else {
            Toast.makeText(Utils.getApp(), "您的手机不支持相机", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 开启相册单选
     *
     * @param requestCode
     * @param crop
     * @param singleCallback
     */
    @Override
    public void startSingleAlbum(int requestCode, boolean crop, @NonNull SingleCallback singleCallback) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        albumArray.put(requestCode, singleCallback);
        if (crop) {
            cropAlbumArray.put(requestCode, singleCallback);
        } else {
            albumArray.put(requestCode, singleCallback);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 开启多选相册
     *
     * @param requestCode
     * @param selectCount
     * @param multiCallback
     */
    @Override
    public void startMultiAlbum(int requestCode, int selectCount, @NonNull MultiCallback multiCallback) {
        //默认不支持
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            cameraArray.delete(requestCode);
            albumArray.delete(requestCode);
            cropCameraArray.delete(requestCode);
            cropAlbumArray.delete(requestCode);
            return;
        }
        SingleCallback singleCallback = cropCameraArray.get(requestCode);
        if (singleCallback != null) {
            if (FileUtils.exist(tempCropFile)) {
                //裁剪结束
                cropCameraArray.delete(requestCode);
                singleCallback.onSelect(tempCropFile);
                tempCropFile = null;
            } else {
                //去裁剪
                if (tempFile != null) {
                    tempCropFile = startCrop(requestCode, tempFile);
                    tempFile = null;
                }
            }
            return;
        }
        singleCallback = cropAlbumArray.get(requestCode);
        if (singleCallback != null) {
            if (FileUtils.exist(tempCropFile)) {
                //裁剪结束
                cropAlbumArray.delete(requestCode);
                singleCallback.onSelect(tempCropFile);
                tempCropFile = null;
            } else {
                //去裁剪
                File albumImageFile = findAlbumImageFile(data);
                if (albumImageFile != null) {
                    tempCropFile = startCrop(requestCode, albumImageFile);
                }
            }
            return;
        }
        singleCallback = cameraArray.get(requestCode);
        if (singleCallback != null) {
            if (tempFile != null) {
                singleCallback.onSelect(tempFile);
                tempFile = null;
            }
            return;
        }
        singleCallback = albumArray.get(requestCode);
        if (singleCallback != null) {
            File albumImageFile = findAlbumImageFile(data);
            if (albumImageFile != null) {
                singleCallback.onSelect(albumImageFile);
            }
        }
    }

    /**
     * 创建临时文件
     *
     * @return
     * @throws IOException
     */
    private static File createTmpImageFile() throws IOException {
        File dir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera/");
        } else {
            dir = new File(Utils.getApp().getCacheDir(), "Camera");
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return File.createTempFile(IMG_PREFIX, IMG_SUFFIX, dir);
    }

    /**
     * 裁剪
     *
     * @param requestCode
     * @param src
     * @return
     */
    private File startCrop(int requestCode, File src) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        // TODO: 2018/7/26 适配API25时需注意
        intent.setDataAndType(Uri.fromFile(src), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        if (android.os.Build.MODEL.contains("HUAWEI")
                || android.os.Build.BRAND.contains("HUAWEI")) {
            //华为特殊处理 不然会显示圆
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        } else {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        File output = null;
        try {
            File dir = new File(Utils.getApp().getExternalCacheDir(), "Crop");
            if (FileUtils.createOrExistsDir(dir)) {
                output = File.createTempFile(IMG_PREFIX, IMG_SUFFIX, dir);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        startActivityForResult(intent, requestCode);
        return output;
    }

    /**
     * 找到映射的相册文件
     *
     * @param data
     * @return
     */
    @Nullable
    private File findAlbumImageFile(Intent data) {
        Uri selectedImage = data.getData();
        if (selectedImage != null) {
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            if (null != c && c.moveToFirst()) {
                String imagePath = c.getString(c.getColumnIndex(filePathColumns[0]));
                c.close();
                return new File(imagePath);
            }
        }
        return null;
    }
}
