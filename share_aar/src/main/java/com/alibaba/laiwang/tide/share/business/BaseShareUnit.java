package com.alibaba.laiwang.tide.share.business;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by zengchan.lzc on 2015/1/17.
 */
public abstract class BaseShareUnit{
    private ShareUnitInfo mShareUnitInfo;

    public BaseShareUnit(ShareUnitInfo shareUnitInfo){
        this.mShareUnitInfo = shareUnitInfo;
    }
    public ShareUnitInfo getmShareUnitInfo() {
        return mShareUnitInfo;
    }

    public void setmShareUnitInfo(ShareUnitInfo mShareUnitInfo) {
        this.mShareUnitInfo = mShareUnitInfo;
    }

    public abstract void share(ShareInfo shareInfo);

    public  byte[] compressImageBySize(Bitmap bitmap, int size) {
        return compressImageBySize(bitmap, size, 800f, 480f);
    }

    /**
     * 对图片尺寸进行压缩
     *
     * @param bitmap
     * @param size
     * @return
     */
    public  byte[] compressImageBySize(Bitmap bitmap, int size,
                                             float height, float width) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bt = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = height;// 这里设置高度为800f
        float ww = width;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bt = BitmapFactory.decodeStream(isBm, null, newOpts);

        return compressImage(bt, size);// 压缩好比例大小后再进行质量压缩
    }
    /**
     * 对图片质量压缩
     *
     * @param image
     *            图片的 bitmap
     * @param size
     *            想要得到的大小尺寸 单位是kb 如果压缩不到，则返回null
     * @return
     */
    public static byte[] compressImage(Bitmap image, int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        image.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        System.out.println("图片压缩前大小：" + baos.toByteArray().length + "byte");
        while (baos.toByteArray().length / 1024 > size && quality > 10) {
            quality -= 10;
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            System.out.println("质量压缩到原来的" + quality + "%时大小为："
                    + baos.toByteArray().length + "byte");
        }
        if (baos.toByteArray().length / 1024 > size) {
            return null;
        }
        System.out.println("图片压缩后大小：" + baos.toByteArray().length + "byte");
        return baos.toByteArray();
    }
}
