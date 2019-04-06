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
    public static String getDirPath(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + '/';
    }

    public static String getName(File file) {
        return file.getName().replace(".jpg", "").replace(".mp3", "");
    }

    public static File getOutputFile(Context context, String filename) throws IOException {
        final File imgFile = new File(getDirPath(context) + filename);
        if (!imgFile.exists()) {
            imgFile.createNewFile();
        }
        return imgFile;
    }

    public static File compressPhotoAndGetFile(Context context, File file) {
        String inputFilename = file.getAbsolutePath();
        try {
            String outputFilename = FileManager.getName(file) + "_lolx.jpg";
            final Bitmap bitmap = BitmapFactory.decodeFile(inputFilename);
            Log.d("converter", outputFilename);
            final FileOutputStream out = new FileOutputStream(getOutputFile(context, outputFilename));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
            out.flush();
            out.close();

            return new File(getDirPath(context) + outputFilename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
