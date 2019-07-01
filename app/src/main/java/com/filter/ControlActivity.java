package com.filter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.filter.utility.Helper;
import com.filter.utility.TransformImage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.VignetteSubfilter;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.filter.utility.Helper.writeDataIntoExternalStorage;

public class ControlActivity extends AppCompatActivity {
    static
    {
        System.loadLibrary("NativeImageProcessor");
    }

    Toolbar mControlToolbar;
    ImageView mCenterImageView;
    TransformImage mTransformImage;

    int mcurrentFilter;

    SeekBar mSeekbar;
    ProgressBar mprogressBar;
    ImageView mTickImageView;
    ImageView mCancelImageView;


    Uri mSelectedImageUri;

    Bitmap bitmap1;
    Bitmap bitmap2;
    Bitmap bitmap3;
    Bitmap bitmap4;

    int mScreenHeight;
    int mScreenWidth;

    Target mApplySingleFilter = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            int currentFilterValue = mSeekbar.getProgress();

            if(mcurrentFilter == TransformImage.FILTER_BRIGHTNESS) {
                bitmap1 = mTransformImage.applyBrightnessSubfilter(currentFilterValue);
                writeDataIntoExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_BRIGHTNESS),mTransformImage.getmBitmap(TransformImage.FILTER_BRIGHTNESS));

                Picasso.with(ControlActivity.this).invalidate(Helper.getFilefromExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_BRIGHTNESS)));
                Picasso.with(ControlActivity.this).load(Helper.getFilefromExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_BRIGHTNESS))).resize(0, mScreenHeight / 2).into(mCenterImageView);
            }else if(mcurrentFilter == TransformImage.FILTER_VIGNETTE) {
                bitmap2 = mTransformImage.applyVignetteSubfilter(currentFilterValue);
                writeDataIntoExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_VIGNETTE),mTransformImage.getmBitmap(TransformImage.FILTER_VIGNETTE));

                Picasso.with(ControlActivity.this).invalidate(Helper.getFilefromExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_VIGNETTE)));
                Picasso.with(ControlActivity.this).load(Helper.getFilefromExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_VIGNETTE))).resize(0, mScreenHeight / 2).into(mCenterImageView);


            }else if(mcurrentFilter == TransformImage.FILTER_SATURATION) {
                bitmap3 = mTransformImage.applySaturationSubfilter(currentFilterValue);
                writeDataIntoExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_SATURATION),mTransformImage.getmBitmap(TransformImage.FILTER_SATURATION));

                Picasso.with(ControlActivity.this).invalidate(Helper.getFilefromExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_SATURATION)));
                Picasso.with(ControlActivity.this).load(Helper.getFilefromExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_SATURATION))).resize(0, mScreenHeight / 2).into(mCenterImageView);

            }else if(mcurrentFilter ==  TransformImage.FILTER_CONTRAST) {
                bitmap4 = mTransformImage.applyContrastSubfilter(currentFilterValue);
                Helper.writeDataIntoExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_CONTRAST),mTransformImage.getmBitmap(TransformImage.FILTER_CONTRAST));

                Picasso.with(ControlActivity.this).invalidate(Helper.getFilefromExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_CONTRAST)));
                Picasso.with(ControlActivity.this).load(Helper.getFilefromExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_CONTRAST))).resize(0, mScreenHeight / 2).into(mCenterImageView);
                }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    private ImageView preview;



    Target mSmallTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            mTransformImage = new TransformImage(ControlActivity.this, bitmap);
            bitmap1 = mTransformImage.applyBrightnessSubfilter(TransformImage.DEFAULT_BRIGHTNESS);

            writeDataIntoExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_BRIGHTNESS), mTransformImage.getmBitmap(TransformImage.FILTER_BRIGHTNESS));
            Picasso.with(ControlActivity.this).load(Helper.getFilefromExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_BRIGHTNESS))).resize(115,115).centerInside().into(mFirstFilterPreviewImageView, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap imagebitmap = ((BitmapDrawable)mFirstFilterPreviewImageView.getDrawable()).getBitmap();
                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(),imagebitmap);
                    imageDrawable.setCircular(true);
                    imageDrawable.setCornerRadius(Math.max(imagebitmap.getWidth(),imagebitmap.getHeight())/2.0f);
                    mFirstFilterPreviewImageView.setImageDrawable(imageDrawable);
                }

                @Override
                public void onError() {
                    mFirstFilterPreviewImageView.setImageResource(R.drawable.effect1);

                }
            });
        //
            bitmap2 = mTransformImage.applyVignetteSubfilter(TransformImage.DEFAULT_VIGNETTE);

            writeDataIntoExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_VIGNETTE), mTransformImage.getmBitmap(TransformImage.FILTER_VIGNETTE));
            Picasso.with(ControlActivity.this).load(Helper.getFilefromExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_VIGNETTE))).fit().centerInside().into(mSecondFilterPreviewImageView);
            //
            bitmap3 = mTransformImage.applySaturationSubfilter(TransformImage.DEFAULT_SATURATION);

            writeDataIntoExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_SATURATION), mTransformImage.getmBitmap(TransformImage.FILTER_SATURATION));
            Picasso.with(ControlActivity.this).load(Helper.getFilefromExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_SATURATION))).fit().centerInside().into(mThirdFilterPreviewImageView);
         //
            bitmap4 = mTransformImage.applyContrastSubfilter(TransformImage.DEFAULT_CONTRAST);

            writeDataIntoExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_CONTRAST), mTransformImage.getmBitmap(TransformImage.FILTER_CONTRAST));
            Picasso.with(ControlActivity.this).load(Helper.getFilefromExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_CONTRAST))).fit().centerInside().into(mFourthFilterPreviewImageView);

            mprogressBar.setVisibility(View.INVISIBLE);

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    final static int PICK_IMAGE = 2;
    final static int MY_PERMISSIONS_REQUEST_STORAGE_PERMISSION = 3;
    CircleImageView mFirstFilterPreviewImageView;
    CircleImageView mSecondFilterPreviewImageView;
    CircleImageView mThirdFilterPreviewImageView;
    CircleImageView mFourthFilterPreviewImageView;
    private static final String TAG = ControlActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        preview = (ImageView) findViewById(R.id.imageView8);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ControlActivity.this, ImagePreviewActivity.class);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                if (mcurrentFilter == TransformImage.FILTER_BRIGHTNESS) {
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 90, bs);
            }
                else if(mcurrentFilter == TransformImage.FILTER_VIGNETTE) {
                    bitmap2.compress(Bitmap.CompressFormat.JPEG, 90, bs);
                }
                else if(mcurrentFilter == TransformImage.FILTER_SATURATION) {
                    bitmap3.compress(Bitmap.CompressFormat.JPEG, 90, bs);
                }
                else if(mcurrentFilter == TransformImage.FILTER_CONTRAST) {
                    bitmap4.compress(Bitmap.CompressFormat.JPEG, 90, bs);
                }
                intent.putExtra("byteArray", bs.toByteArray());
                startActivity(intent);
            }
        });

        mControlToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCenterImageView = (ImageView) findViewById(R.id.imageView);
        mSeekbar = (SeekBar) findViewById(R.id.seekBar);
        mprogressBar = (ProgressBar) findViewById(R.id.progressBar2);
        mprogressBar.setVisibility(View.GONE);

        mControlToolbar.setTitle(getString(R.string.app_name));
        mControlToolbar.setNavigationIcon(R.drawable.icon);
        mControlToolbar.setTitleTextColor(getResources().getColor(R.color.colorwhite));

        mFirstFilterPreviewImageView = (CircleImageView) findViewById(R.id.imageView4);
        mSecondFilterPreviewImageView = (CircleImageView) findViewById(R.id.imageView5);
        mThirdFilterPreviewImageView = (CircleImageView) findViewById(R.id.imageView6);
        mFourthFilterPreviewImageView = (CircleImageView) findViewById(R.id.imageView7);

        mTickImageView = (ImageView) findViewById(R.id.imageView3);
        mCancelImageView = (ImageView) findViewById(R.id.imageView2);

        mCenterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestStoragePrmissions();
                if(ContextCompat.checkSelfPermission(ControlActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                       Intent intent = new Intent();
                       intent.setType("image/*");
                       intent.setAction(Intent.ACTION_GET_CONTENT);
                       startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                mprogressBar.setVisibility(View.VISIBLE);

                       }
        });



        mFirstFilterPreviewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekbar.setMax(TransformImage.MAX_BRIGHTNESS);
                mSeekbar.setProgress(TransformImage.DEFAULT_BRIGHTNESS);
                mcurrentFilter = TransformImage.FILTER_BRIGHTNESS;
                    Picasso.with(ControlActivity.this).load(Helper.getFilefromExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_BRIGHTNESS))).resize(0,mScreenHeight / 2).into(mCenterImageView);
            }
        });

        mSecondFilterPreviewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekbar.setMax(TransformImage.MAX_VIGNETTE);
                mSeekbar.setProgress(TransformImage.DEFAULT_VIGNETTE);
                mcurrentFilter = TransformImage.FILTER_VIGNETTE;


                    Picasso.with(ControlActivity.this).load(Helper.getFilefromExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_VIGNETTE))).resize(0,mScreenHeight / 2).into(mCenterImageView);
            }
        });

        mThirdFilterPreviewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekbar.setMax(TransformImage.MAX_SATURATION);
                mSeekbar.setProgress(TransformImage.DEFAULT_SATURATION);
                mcurrentFilter = TransformImage.FILTER_SATURATION;

                    Picasso.with(ControlActivity.this).load(Helper.getFilefromExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_SATURATION))).resize(0,mScreenHeight / 2).into(mCenterImageView);
            }
        });

        mFourthFilterPreviewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekbar.setMax(TransformImage.MAX_CONTRAST);
                mSeekbar.setProgress(TransformImage.DEFAULT_CONTRAST);
                mcurrentFilter = TransformImage.FILTER_CONTRAST;

                    Picasso.with(ControlActivity.this).load(Helper.getFilefromExternalStorage(ControlActivity.this, mTransformImage.getFilename(TransformImage.FILTER_CONTRAST))).resize(0,mScreenHeight / 2).into(mCenterImageView);
            }
        });

        mTickImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(ControlActivity.this).load(mSelectedImageUri).into(mApplySingleFilter);
            }
        });

        mCancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
    }


        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data){
           if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
               mSelectedImageUri = data.getData();

               Picasso.with(ControlActivity.this).load(mSelectedImageUri).fit().centerInside().into(mCenterImageView);
               Picasso.with(ControlActivity.this).load(mSelectedImageUri).resize(1080,0).onlyScaleDown().into(mSmallTarget);

           }
        }

        public void requestStoragePrmissions() {
            if (ContextCompat.checkSelfPermission(ControlActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ControlActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    new MaterialDialog.Builder(ControlActivity.this).title(R.string.permission_title)
                            .content(R.string.permission_content)
                            .negativeText(R.string.permission_cancel)
                            .positiveText(R.string.permission_agree_settings)
                            .canceledOnTouchOutside(true)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);
                                }
                            })
                            .show();
                } else {
                    ActivityCompat.requestPermissions(ControlActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_STORAGE_PERMISSION);
                }
                return;
            }

        }
        private long lastPressedTime;
        private static final int PERIOD = 2000;
        @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
                switch (event.getAction()) {
                    case KeyEvent.ACTION_DOWN:
                        if(event.getDownTime() - lastPressedTime < PERIOD) {
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Press again to exit.", Toast.LENGTH_SHORT).show();
                            lastPressedTime = event.getEventTime();
                        }
                        return true;
                }
            }
            return false;
        }
    }



