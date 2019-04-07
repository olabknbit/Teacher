package com.hack.teach.teacher;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {
    public static String getPicturesDirPath(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + '/';
    }

    public static String getMP3sDirPath(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + '/';
    }

    public static String getCompressedDirPath(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + '/';
    }

    public static String getName(File file) {
        return file.getName().replace(".jpg", "").replace(".mp3", "");
    }

    public static File getOutputCompressedFile(Context context, String filename) throws IOException {
        final File imgFile = new File(getCompressedDirPath(context) + filename);
        if (!imgFile.exists()) {
            imgFile.createNewFile();
        }
        return imgFile;
    }

    public static File compressPhotoAndGetFile(Context context, File file) {
        String inputFilename = file.getAbsolutePath();
        try {
            String outputFilename = FileManager.getName(file) + ".jpg";
            final Bitmap bitmap = BitmapFactory.decodeFile(inputFilename);
            Log.d("converter", outputFilename);
            final FileOutputStream out = new FileOutputStream(getOutputCompressedFile(context, outputFilename));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
            out.flush();
            out.close();

            return new File(getCompressedDirPath(context) + outputFilename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File compressPhotoAndGetFile(Context context, Bitmap bitmap, String filename) {
        Log.d("compress", filename);
        //String inputFilename = file.getAbsolutePath();
        try {
            String outputFilename = filename + "_lolx.jpg";
            //final Bitmap bitmap = BitmapFactory.decodeFile(inputFilename);
            Log.d("converter", outputFilename);
            final FileOutputStream out = new FileOutputStream(getOutputCompressedFile(context, outputFilename));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
            out.flush();
            out.close();

            return new File(getCompressedDirPath(context) + outputFilename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deletePhotoFromMemory(Context context, String photoPath) {
        try {
            File file = new File(photoPath);
            file.delete();
            if (file.exists()) {
                context.deleteFile(file.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
