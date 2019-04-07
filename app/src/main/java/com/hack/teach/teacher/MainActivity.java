package com.hack.teach.teacher;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ListActivityFragment laf = (ListActivityFragment) getFragmentManager().findFragmentById(R.id.fragment);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            PermissionsManager.verifyPermissions(this);
        }

        final FloatingActionButton cameraActionButton = (FloatingActionButton) findViewById(R.id.fab);
        cameraActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filenamePopUp();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void filenamePopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String filenamePopup = input.getText().toString();
                dispatchTakePictureIntent(filenamePopup);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void updateFragment() {

    }

    static final int REQUEST_TAKE_PHOTO = 1;

    String mCurrentPhotoPath;

    private File createImageFile(String filename) throws IOException {
        // Create an image file name
        String imageFileName = filename + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("createIm", mCurrentPhotoPath);
        return image;
    }

    private void dispatchTakePictureIntent(String filename) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                Log.d("uri", photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onacres", requestCode + " " + String.valueOf(resultCode == RESULT_OK));
        if (REQUEST_TAKE_PHOTO == requestCode && resultCode == RESULT_OK) {
            Log.d("currentphotopath", mCurrentPhotoPath);
            File imgFile = new File(mCurrentPhotoPath);
            if (imgFile.exists()) {
                String path = FileManager.getPicturesDirPath(this) + FileManager.getName(imgFile) + ".jpg";
                Log.d("path", path);
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    new FileUploader(this).postContentToUrl(imageBitmap, path, laf);
                } else {
                    new FileUploader(this).postContentToUrl(imgFile);
                    Log.d("wft", "extras null");
                }
            }
        }
    }
}
