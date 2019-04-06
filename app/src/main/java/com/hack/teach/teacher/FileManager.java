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
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    public static File getOutputFile(Context context, String filename) throws IOException {
        final File imgFile = new File(getDirPath(context) + filename);
        if (!imgFile.exists()) {
            imgFile.createNewFile();
        }
        return imgFile;
    }

    public static File compressPhotoAndGetFile(Context context, String filePath) {
        try {
            String filename = new File(filePath).getName() + "lolx";
            final Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            Log.d("converter", filePath);
            final FileOutputStream out = new FileOutputStream(getOutputFile(context, filename));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
            out.flush();
            out.close();

            return new File(getDirPath(context) + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
