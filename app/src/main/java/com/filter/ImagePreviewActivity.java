package com.filter;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.filter.utility.Helper;
import com.filter.utility.TransformImage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImagePreviewActivity extends AppCompatActivity {
    Toolbar mControlToolbar;
    ImageView imageview8;
    Bitmap b;
    ImageView save;
    String filename;
    Uri uri2;

    private void createDirectory(Bitmap b,String filename) {
        File direct = new File(Environment.getExternalStorageDirectory() + "/Filters2");

        if (!direct.exists()) {
            File wallpaper = new File("/sdcard/Filters2/");
            wallpaper.mkdirs();
        }

        File file = new File(new File("/sdcard/Filters2/"), filename);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);


        if (getIntent().hasExtra("byteArray")) {
            imageview8 = (ImageView) findViewById(R.id.imageView8);
            b = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("byteArray"), 0, getIntent().getByteArrayExtra("byteArray").length);
            imageview8.setImageBitmap(b);
        }

        save = (ImageView) findViewById(R.id.imageView9);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filename = String.format("%d.jpeg",System.currentTimeMillis());
                createDirectory(b,filename);
                Toast.makeText(getApplicationContext(), "Image Saved Successfully in /sdcard/Filters2.", Toast.LENGTH_SHORT).show();

            }
        });

    }
}

