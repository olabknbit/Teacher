package com.hack.teach.teacher;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

public class FileUploader implements DataService {
    private Context context;

    public FileUploader(Context context) {
        this.context = context;

    }

    private final static String uri = "http://783dddb3.ngrok.io/convert";

    private void inputStreamToMP3(InputStream inputStream) {
        FileOutputStream fileOutputStream;
        try {
            File f = new File(FileManager.getDirPath(context), "sound.mp3");
            Log.d("uploader", "saving path " + f.getAbsolutePath());
            fileOutputStream = new FileOutputStream(f);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }

            inputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postContentToUrl(final File file1) {
        final File file = FileManager.compressPhotoAndGetFile(context, file1.getAbsolutePath());
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                HttpClient client = HttpClientBuilder.create().build();
                HttpPost httpPost = new HttpPost(uri);
                //httpPost.setHeader("Content-Type", "audio/mp3");
                try {
                    Log.d("uploader", "starting upload");
//                    MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
//                   // entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        entityBuilder.addBinaryBody("file", Files.readAllBytes(file.toPath()));
//                    }
//
                    MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                   // entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                    ContentType contentType = ContentType.APPLICATION_OCTET_STREAM;
                   // entityBuilder.setBoundary(boundary);
                    Log.d("uploader", "filename" + file.getAbsolutePath());
                    entityBuilder.addPart("file", new FileBody(file, contentType, file.getAbsolutePath()));

                    HttpEntity entity = entityBuilder.build();


                    httpPost.setEntity(entity);


                    Log.d("uploader", "entity" + entityBuilder.build().toString());
                    Log.d("uploader", "request" + httpPost.toString());

                    HttpResponse response = client.execute(httpPost);
                    Log.d("uploader", "got response");
                    Log.d("uploader", response.getEntity().toString());
                    HttpEntity httpEntity = response.getEntity();
                    inputStreamToMP3(httpEntity.getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }
}
