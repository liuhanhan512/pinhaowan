/*
 * 
 * Copyright (c) 2015, alipay.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hwand.pinhaowanr.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;

import org.apache.http.protocol.HTTP;
import org.apache.http.util.EncodingUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * file utility
 * 
 */
public class FileUtil {

	private static final String TAG = "FileUtil";
	public static final String BASE_PATH = ".babylon";
	public final static int VOICE_MINENOUGH_CACHESIZE = 1000000;//语音最小剩余空间1M
	public final static int VOICE_MAXSDCARD_CACHESIZE = 20000000;//语音sd卡最大缓存20M
	public final static int IMAGE_MAXSDCARD_CACHESIZE = 30000000;//图片sd卡最大缓存30M
	public final static int IMAGE_MAXMEMORY_CACHESIZE = 40000000;//全局内存最大缓存40M，在内存2G的手机上，因为GC时间太长，会出 GcWatcher.finalize() timed out after 10 seconds
	public static final String FILE_SCHEME = "file://";

	private static final int BUFFER_SIZE = 8 * 1024; // 8 KB

	public static void copyStream(InputStream is, OutputStream os) throws IOException {
		byte[] bytes = new byte[BUFFER_SIZE];
		while (true) {
			int count = is.read(bytes, 0, BUFFER_SIZE);
			if (count == -1) {
				break;
			}
			os.write(bytes, 0, count);
		}
	}

	public static void byteToMd5(InputStream is) throws IOException {
		byte[] bytes = new byte[is.available()];
		while (true) {
			int count = is.read(bytes, 0, is.available());
			if (count == -1) {
				break;
			}
		}
	}

	/**
	 * 获取页面数据的缓存的路径（调用laiwang api返回的cache）
	 *
	 * @param context
	 * @return
	 */
	public static File getXiamiCacheDir(Context context) {
		return getCacheDirByType(context, "xiami");
	}


	@SuppressLint("NewApi")
	public static File getFilesDir(Context context) {
		File filesDir = null;
		if (isCanUseSDCard()) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
				filesDir = context.getExternalFilesDir(null);

			} else {
				filesDir = new File(Environment.getExternalStorageDirectory(), "/Android/data/"
						+ context.getApplicationInfo().packageName + "/files/");
				filesDir.mkdirs();
			}
		} else {
			filesDir = context.getFilesDir();
		}
		if (filesDir != null && !filesDir.exists()) {
			filesDir.mkdirs();
		}
		return filesDir;
	}

	public static File getDbDir(Context context) {

		File filesDir = new File(Environment.getDataDirectory(), "/data/"
				+ context.getApplicationInfo().packageName + "/databases/");
		return filesDir;
	}

	/**
	 * 将字符转附加到文件
	 *
	 * @param str
	 * @param file
	 * @param charset 字符编码
	 */
	public static void appendStringToFile(String str, File file, String charset) {
		saveStringToFile(str, file, charset, true);
	}


	public static boolean copyFile(File sourceFile, File targetFile) {
		FileInputStream input = null;
		BufferedInputStream inBuff = null;

		FileOutputStream output = null;
		BufferedOutputStream outBuff = null;
		try {
			input = new FileInputStream(sourceFile);
			inBuff = new BufferedInputStream(input);
			output = new FileOutputStream(targetFile);
			outBuff = new BufferedOutputStream(output);
			byte[] b = new byte[1024 * 5];
			int len;
			int icount = 0;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
				icount++;
				if (icount % 5 == 0) {
					outBuff.flush();
				}
			}
			outBuff.flush();
			return true;
		} catch (Exception e) {

		}  finally {
			close(inBuff);
			close(outBuff);
			close(input);
			close(output);
		}
		return false;
	}

	/**
	 * 判断文件是否已经存在
	 *
	 * @param file
	 * @return
	 */
	public static boolean isExist(String file) {
		if (TextUtils.isEmpty(file)) {
			return false;
		}
		if (new File(file).exists()) {
			return true;
		}
		return false;
	}

	/**
	 * 获得指定文件
	 *
	 * @param path
	 * @return
	 */
	public static String getFileDir(String path) {
		if (TextUtils.isEmpty(path)) {
			return "";
		}
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), path);
		file.mkdirs();
		return file.getAbsolutePath();
	}

	/**
	 * 判定SDCard是否可读写
	 *
	 * @return
	 */
	public static boolean isCanUseSDCard() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取Android的Cache目录，如果安装了SDCard，Cache目录会在SDCard上，否则会从内存获取
	 *
	 * @return
	 */
	public static File getCacheDir(Context context) {
		File cacheDir = null;
		if (isCanUseSDCard()) {
			// SDCard 可读写情况下
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
				cacheDir = getExternalCacheDir8(context);
			} else {
				cacheDir = getExternalCacheDir7(context);
			}
		} else {
			// 没有SDCard
			cacheDir = context.getCacheDir();
		}
		if (cacheDir != null && !cacheDir.exists()) {
			cacheDir.mkdirs();
		}

		if (cacheDir == null) {
			cacheDir = context.getCacheDir();
		}
		return cacheDir;
	}

	public static File getExternalCacheDir(Context context) {
		File cacheDir = null;
		if (isCanUseSDCard()) {
			// SDCard 可读写情况下
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
				cacheDir = getExternalCacheDir8(context);
			} else {
				cacheDir = getExternalCacheDir7(context);
			}
		}
		if (cacheDir != null && !cacheDir.exists()) {
			cacheDir.mkdirs();
		}

		if (cacheDir == null) {
			cacheDir = context.getCacheDir();
		}
		return cacheDir;
	}

	private static File getExternalCacheDir8(Context context) {
		return context.getExternalCacheDir();
	}

	/**
	 * 版本低于7的外部缓存路径的设置
	 *
	 * @param context
	 * @return
	 */
	private static final File getExternalCacheDir7(Context context) {
		return new File(Environment.getExternalStorageDirectory(), "/Android/data/"
				+ context.getApplicationInfo().packageName + "/cache/");
	}


	/**
	 * 获取不同类型Cache的路径
	 *
	 * @param context
	 * @param type
	 * @return
	 */
	private static File getCacheDirByType(Context context, String type) {
		File cacheDir = getCacheDir(context);
		File typeCacheDir = new File(cacheDir, type);
		if (typeCacheDir != null && !typeCacheDir.exists()) {
			typeCacheDir.mkdirs();
		}
		return typeCacheDir;
	}

	/**
	 * 获取图片缓存的路径
	 *
	 * @param context
	 * @return
	 */
	public static File getImageCacheDir(Context context) {
		return getCacheDirByType(context, "uil-images");
	}

	/**
	 * 获取音频缓存的路径
	 *
	 * @param context
	 * @return
	 */
	public static File getAudioCacheDir(Context context) {
		return getCacheDirByType(context, "audios");
	}

	/**
	 * 获取页面数据的缓存的路径（调用laiwang api返回的cache）
	 *
	 * @param context
	 * @return
	 */
	public static File getPageCacheDir(Context context) {
		return getCacheDirByType(context, "pages");
	}

	/**
	 * 获取网络请求日志存放路径
	 *
	 * @param context
	 * @return
	 */
	public static File getrequestNetLogDir(Context context) {
		return getCacheDirByType(context, "requestNetLog");
	}

	/**
	 * 将字符转附加到文件
	 *
	 * @param str
	 * @param file
	 */
	public static void appendStringToFile(String str, File file) {
		saveStringToFile(str, file, HTTP.UTF_8, true);
	}

	/**
	 * 将byte数组保存到文本
	 *
	 * @param bytes    文件内容
	 * @param file     文件
	 * @param isAppend 是否采用附加的方式
	 */
	public static void saveBytesToFile(byte[] bytes, File file, boolean isAppend) {
		if (null == file) {
			return;
		}
		File parentFile = file.getParentFile();
		if (parentFile != null && (!parentFile.exists())) {
			parentFile.mkdirs();
		}
		FileOutputStream outStream = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			outStream = new FileOutputStream(file, isAppend);
			outStream.write(bytes);
			outStream.flush();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			close(outStream);
		}
	}

	/**
	 * 将字符串保存到文本
	 *
	 * @param str      字符内容
	 * @param file     文件
	 * @param charset  字符编码
	 * @param isAppend 是否采用附加的方式
	 */
	public static void saveStringToFile(String str, File file, String charset, boolean isAppend) {
		saveBytesToFile(EncodingUtils.getBytes(str, charset), file, isAppend);
	}

	/**
	 * 获得sdcard上的路径
	 */
	public static File getBasePath() throws IOException {
		File basePath = new File(Environment.getExternalStorageDirectory(), BASE_PATH);
		if (!basePath.exists()) {
			if (!basePath.mkdirs()) {
				throw new IOException("file cannot be created!" + basePath.toString());
			}
		}
		if (!basePath.isDirectory()) {
			throw new IOException("file is not a directory!" + basePath.toString());
		}
		return basePath;
	}

	/**
	 * 根据uri获得文件的确切路径, 如果不存在则返回null
	 *
	 * @return
	 */
	public static String getRealPathFromURI(Uri contentUri, Activity a) {
		String[] proj = {MediaStore.MediaColumns.DATA};
		Cursor cursor = a.managedQuery(contentUri, proj, null, null, null);
		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
			String realPath = null;
			if (cursor.moveToFirst()) {
				realPath = cursor.getString(column_index);
				//此处的游标不能关闭，关闭会导致4.0以上的版本的activity crash，另外，关闭动作activity会自动完成
//				cursor.close();
			}
			return realPath;
		} else {
			return null;
		}
	}

	public static String getUserPageString(Context context, String uid, String file) {
		File userPageDir = getUserPageDir(context, uid);
		File filePath = new File(userPageDir, file);
		ByteArrayOutputStream arrayOutputStream = null;
		FileInputStream inputStream = null;
		if (!filePath.exists()) {
			return null;
		}
		try {
			arrayOutputStream = new ByteArrayOutputStream();
			inputStream = new FileInputStream(filePath);
			int len = 0;
			byte[] b = new byte[128];
			while ((len = inputStream.read(b)) != -1) {
				arrayOutputStream.write(b, 0, len);
			}
			String content = arrayOutputStream.toString();
			return content;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			close(arrayOutputStream);
			close(inputStream);
		}
		return null;
	}

	public static Object getUserPageObject(Context context, String uid, String file) {
		File userPageDir = getUserPageDir(context, uid);
		File userPageFile = new File(userPageDir, file);
		ObjectInputStream inputStream = null;
		if (!userPageFile.exists()) {
			return null;
		}
		try {
			inputStream = new ObjectInputStream(new FileInputStream(userPageFile));
			Object obj = inputStream.readObject();
			return obj;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		} finally {
			close(inputStream);
		}
		return null;
	}

	public static void saveUserPageString(Context context, String uid, String str, String file) {
		if (null == file) {
			return;
		}
		File userPageDir = getUserPageDir(context, uid);
		File userPageFile = new File(userPageDir, file);
		OutputStream outStream = null;
		try {
			userPageFile.createNewFile();
			outStream = new FileOutputStream(userPageFile);
			outStream.write(str.getBytes());
			outStream.flush();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			close(outStream);
		}
	}


	/**
	 * 保存 指定用户所关联的页面缓存
	 *
	 * @param context
	 * @param uid
	 * @param object
	 * @param filename
	 */
	public static void saveUserPageObject(Context context, String uid, Object object, String filename) {
		File dir = getUserPageDir(context, uid);
		File file = new File(dir, filename);
		ObjectOutputStream outStream = null;
		try {
			file.createNewFile();
			outStream = new ObjectOutputStream(new FileOutputStream(file));
			outStream.writeObject(object);
			outStream.flush();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			close(outStream);
		}
	}


	/**
	 * 获取 指定用户所关联的页面缓存
	 *
	 * @param context
	 * @param uid
	 * @return
	 */
	private static File getUserPageDir(Context context, String uid) {
		File pageCacheDir = getPageCacheDir(context);
		File userPageCacheDir = new File(pageCacheDir, uid);
		if (!userPageCacheDir.exists()) {
			userPageCacheDir.mkdir();
		}
		return userPageCacheDir;
	}

	/**
	 * 删除用户的页面数据缓存
	 *
	 * @param context
	 * @param file
	 * @param uid
	 */
	public static void deleteUserPage(Context context, String file, String uid) {
		File userPageDir = getUserPageDir(context, uid);
		File filePath = new File(userPageDir, file);
		if (filePath.exists()) {
			filePath.delete();
		}
	}

	/**
	 * 删除文件 删除目录下的全部文件和目录
	 *
	 * @param path 文件或目录名绝对路径
	 */
	public static void deleteAll(File path) {
		if (path == null) return;
		if (!path.exists())
			return;
		if (path.isFile()) {
			path.delete();
			return;
		}
		File[] files = path.listFiles();
		for (int i = 0; i < files.length; i++) {
			deleteAll(files[i]);
		}
		path.delete();
	}

	public static String downloadToAlbum(Context context, File file) {
		String newFileDir = Environment.getExternalStorageDirectory()
				.toString() + "/dcim/Camera/";
		File dirFile = new File(newFileDir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		File targetFile = new File(newFileDir
				+ UUID.randomUUID().toString() + ".jpg");
		if (copyFile(file, targetFile)) {
			String filePath = targetFile.getAbsolutePath();
			return filePath;
		}
		return "";
	}

	/**
	 * 关闭输入输出流
	 *
	 * @param closeable
	 */
	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException exp) {
			}
		}
	}

	/**
	 * 获得文件名后缀
	 *
	 * @param file
	 * @return
	 */
	public static String getFileNameSuffix(File file) {
		String suffix = "";
		if (file != null) {
			String filePath = file.getAbsolutePath();
			if (!TextUtils.isEmpty(filePath)) {
				int dotIndex = filePath.lastIndexOf(".");
				if (dotIndex > 0) {
					suffix = filePath.substring(dotIndex + 1);
				}
			}
		}
		return suffix;
	}

	/**
	 * 获得文件名后缀
	 *
	 * @param filePath
	 * @return
	 */
	public static String getFileNameSuffix(String filePath) {
		String suffix = "";
		if (!TextUtils.isEmpty(filePath)) {
			int dotIndex = filePath.lastIndexOf(".");
			if (dotIndex > 0) {
				suffix = filePath.substring(dotIndex + 1);
			}
		}
		return suffix;
	}

	/**
	 * 获取除扩展名外的文件路径
	 *
	 * @param filename
	 * @return
	 */
	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}

	public static File creatFolderWithFile(String fileFolder) {
		File file = new File(fileFolder);
		if (file != null && !file.exists()) {
			file.mkdirs();
			return file;
		}
		return null;
	}

	/**
	 * 是否是本地文件
	 *
	 * @param file
	 * @return
	 */
	public static boolean isLocalFile(String file) {
		if (!TextUtils.isEmpty(file)) {
			if (file.startsWith(FILE_SCHEME)) {
				file = file.replace(FILE_SCHEME, "");
			}
			if (isExist(file)) {
				return true;
			}
		}
		return false;
	}

	public static String getLocalFilePath(String file) {
		if (isLocalFile(file)) {
			file = file.replace(FILE_SCHEME, "");
			return file;
		}
		return "";
	}

	/**
	 * 获取该路径下的可用空间
	 *
	 * @param path
	 * @return
	 */
	public static long getUsableSpace(String path) {
//		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//	            return new File(path).getUsableSpace();
//	     }
		StatFs statfs = new StatFs(path);
		return (long) statfs.getBlockSize() * (long) statfs.getAvailableBlocks();
	}


	/**
	 * 获取文件大小
	 *
	 * @param file
	 * @return the length of this file in bytes
	 */
	public static long getFileSize(File file) {
		if (file == null || !file.exists()) {
			return 0;
		}

		long size = 0;
		if (file.isFile()) {
			size = file.length();
		} else {
			for (File f : file.listFiles()) {
				size += getFileSize(f);
			}
		}
		return size;
	}

	/**
	 * 获取来往打点配置文件路径
	 *
	 * @param context
	 * @param uid
	 * @return
	 */
	public static File getLWConfigFileDir(Context context, String uid) {
		File file = getFilesDir(context);
		if (TextUtils.isEmpty(uid))
			return file;
		File typeCacheDir = new File(file, uid);
		if (typeCacheDir != null && !typeCacheDir.exists()) {
			typeCacheDir.mkdirs();
		}
		return typeCacheDir;
	}

	/**
	 * 读取表情配置文件
	 *
	 * @param context
	 * @return
	 */
	public static List<String> getEmojiFile(Context context) {
		BufferedReader br = null;
		try {
			List<String> list = new ArrayList<String>();
			InputStream in = context.getResources().getAssets().open("emoji");
			br = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
			String str;
			while ((str = br.readLine()) != null) {
				list.add(str);
			}
			return list;
		} catch (IOException e) {
		} finally {
			close(br);
		}
		return null;
	}

	/**
	 * 判断是否是有效的文件路径（包括文件是否存在）
	 * TODO:有效性判断的实现
	 * @param path
	 * @return
	 */
	public final static boolean isValidFilePath(String path) {

		if (path == null || path.trim().length() == 0 || path.startsWith("http://") || path.startsWith("https://")) {

			return false;
		}

		boolean isExists = false;

		try {

			isExists = path.startsWith("file://") ? new File(new URI(path)).exists() : new File(path).exists();
		} catch (Exception e) {
			// do nothing ...
		}

		return isExists;
	}

	public static boolean isAssignFileType(String fileName, String fileType){
		if(fileName != null && fileType != null){
			return fileName.toLowerCase().endsWith("."+fileType.toLowerCase());
		}
		return false;
	}

	public static boolean isAvailableSpace(int sizeMb) {

		boolean isHasSpace = false;

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			String sdcard = Environment.getExternalStorageDirectory().getPath();
			StatFs statFs = new StatFs(sdcard);
			long blockSize = statFs.getBlockSize();
			long blocks = statFs.getAvailableBlocks();
			long availableSpare = (blocks * blockSize) / (1024 * 1024);

			if (availableSpare > sizeMb) {

				isHasSpace = true;
			}
		}

		return isHasSpace;
	}

	public static String getFormattedFileSize(String filePath) {
		return getFormattedFileSize(new File(filePath));
	}

	public static final String B = "B";
	public static final String KB = "K";
	public static final String MB = "M";
	public static final String GB = "G";

	public static String getFormattedFileSize(File file) {
		String format = "%.1f%s";
		if (file == null || !file.exists()) {
			return String.format(format, 0f, KB);
		}

		return getFormattedFileSize(file.length());
	}

	public static String getFormattedFileSize(long size) {
		String format = "%.1f%s";
		String result = null;
		if (size < 1024) {
			result = String.format(format, size*1.0f, B);
		} else if (size < 1024 * 1024){
			result = String.format(format, size / 1024f, KB);
		} else if (size < 1024 * 1024 * 1024) {
			result = String.format(format, size / (1024f * 1024), MB);
		} else {
			result = String.format(format, size / (1024f * 1024 * 1024), GB);
		}

		return result;
	}

	/**
	 * 
	 * copy file
	 * 
	 * @param src
	 *            source file
	 * @param dest
	 *            target file
	 * @throws IOException
	 */
//	public static void copyFile(File src, File dest) throws IOException {
//		FileChannel inChannel = null;
//		FileChannel outChannel = null;
//		try {
//			if (!dest.exists()) {
//				dest.createNewFile();
//			}
//			inChannel = new FileInputStream(src).getChannel();
//			outChannel = new FileOutputStream(dest).getChannel();
//			inChannel.transferTo(0, inChannel.size(), outChannel);
//		} finally {
//			if (inChannel != null) {
//				inChannel.close();
//			}
//			if (outChannel != null) {
//				outChannel.close();
//			}
//		}
//	}

	/**
	 * delete file
	 * 
	 * @param file
	 *            file
	 * @return true if delete success
	 */
	public static boolean deleteFile(File file) {
		if (!file.exists()) {
			return true;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				deleteFile(f);
			}
		}
		return file.delete();
	}
}
