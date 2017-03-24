package com.example.range.hashingvideo;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    File directory;
    final int REQUEST_CODE_VIDEO = 2;
    final String TAG = "myLogs";
    ImageView ivPhoto;
    public static Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDirectory();
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
    }

    public void onClickVideo(View view) {
        uri = generateFileUri();
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CODE_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == REQUEST_CODE_VIDEO) {
            if (resultCode == RESULT_OK) {
                if (intent == null) {
                    Log.d(TAG, "Intent is null");
                } else {
                    getHash(uri);
                    Log.d(TAG, "Video uri: " + intent.getData());
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "Canceled");
            }
        }
    }

    private Uri generateFileUri() {
        File file = new File(directory.getPath() + "/" + "video_" + System.currentTimeMillis() + ".mp4");
        Log.d(TAG, "fileName = " + file);
        return Uri.fromFile(file);
    }

    private void createDirectory() {
        directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "Lab2");
        if (!directory.exists())
            directory.mkdirs();
    }

    private void getHash(Uri uri){
/*        FileInputStream is;
        FileOutputStream os;
        try {
            is = new FileInputStream(new File(uri.getPath()));
            os = new FileOutputStream(new File(uri.getPath() + "copy"));
            byte[] buffer = new byte[32];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
                Log.d("copy", buffer.toString());
            }
        }catch (Exception e){
            Log.d("copy", e.getMessage());
        }
*/
        try {
            Log.d("Hash", "start");
            Date d = new Date();
            long startTime = d.getTime();

            File file = new File(uri.getPath());
            byte[] data = HashFunc.getHashFromFile(file);
            File fl = new File(uri.getPath() + ".hash");
            fl.createNewFile();
            OutputStream os = new FileOutputStream(fl);
            os.write(data);
            os.close();

            d = new Date();
            long time = d.getTime() - startTime;
            Log.d("Hash", "end " + time + " s");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
