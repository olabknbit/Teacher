package com.hack.teach.teacher;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class PermissionsManager {

    // Storage Permissions
    private static final int REQUEST_PERMS = 1;
    private static String[] PERMISSIONS = { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET };

    public static void verifyPermissions(Activity activity) {
        Log.d("PERM", "premissions");

        for (String perm : PERMISSIONS) {
            // Check if we have write permission.
            final int permission = ActivityCompat.checkSelfPermission(activity, perm);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                Log.d("PERM", "yes");
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(activity, PERMISSIONS, REQUEST_PERMS);
                Log.d("PERM", "pls");
            }
        }

    }
}
