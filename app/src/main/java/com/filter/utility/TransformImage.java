package com.filter.utility;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.filter.ImagePreviewActivity;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.VignetteSubfilter;

import java.net.ContentHandler;
import java.security.PublicKey;

public class TransformImage {
    public static final int MAX_BRIGHTNESS = 100;
    public static final int MAX_VIGNETTE = 255;
    public static final int MAX_SATURATION = 5;
    public static final int MAX_CONTRAST = 100;

    public static final int DEFAULT_BRIGHTNESS = 70;
    public static final int DEFAULT_VIGNETTE = 100;
    public static final int DEFAULT_SATURATION = 5;
    public static final int DEFAULT_CONTRAST = 60;

    private String mFilename;
    private Bitmap mBitmap;
    private Context mContext;


    private Bitmap brightnessFilteredBitmap;
    private Bitmap VignetteFilteredBitmap;
    private Bitmap SaturationFilteredBitmap;
    private Bitmap ContrastFilteredBitmap;

    public static int FILTER_BRIGHTNESS = 0;
    public static int FILTER_VIGNETTE = 1;
    public static int FILTER_SATURATION = 2;
    public static int FILTER_CONTRAST = 3;

    public String getFilename(int filter) {
        if (filter == FILTER_BRIGHTNESS){
            return mFilename+"_brightness.JPEG";
        }else if(filter == FILTER_VIGNETTE){
            return mFilename+"_vignette.JPEG";
        }else if(filter == FILTER_SATURATION){
            return mFilename+"_saturation.JPEG";
        }else if(filter == FILTER_CONTRAST){
            return mFilename+"_contrast.JPEG";
        }
        return mFilename;
    }
    public Bitmap getmBitmap(int filter) {
        if (filter == FILTER_BRIGHTNESS) {
            return brightnessFilteredBitmap;
        } else if (filter == FILTER_VIGNETTE) {
            return VignetteFilteredBitmap;
        } else if (filter == FILTER_SATURATION) {
            return SaturationFilteredBitmap;
        } else if (filter == FILTER_CONTRAST) {
            return ContrastFilteredBitmap;
        }
        return mBitmap;
    }

    public TransformImage(Context context, Bitmap bitmap) {
        mContext = context;
        mBitmap = bitmap;
        mFilename = System.currentTimeMillis() + "";
    }
    public Bitmap applyBrightnessSubfilter(int brightness) {
        Filter myFilter = new Filter();

        Bitmap workingBitmap = Bitmap.createBitmap(mBitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888,true);

        myFilter.addSubFilter(new BrightnessSubfilter(brightness));

        Bitmap outputImage = myFilter.processFilter(mutableBitmap);

        brightnessFilteredBitmap = outputImage;

        return outputImage;
    }
    public Bitmap applyVignetteSubfilter(int vignette) {
        Filter myFilter = new Filter();

        Bitmap workingBitmap = Bitmap.createBitmap(mBitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888,true);

        myFilter.addSubFilter(new VignetteSubfilter(mContext, vignette));

        Bitmap outputImage = myFilter.processFilter(mutableBitmap);

        VignetteFilteredBitmap = outputImage;

        return outputImage;

    }
    public Bitmap applySaturationSubfilter(int saturation) {
        Filter myFilter = new Filter();

        Bitmap workingBitmap = Bitmap.createBitmap(mBitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888,true);

        myFilter.addSubFilter(new SaturationSubfilter(saturation));

        Bitmap outputImage = myFilter.processFilter(mutableBitmap);

        SaturationFilteredBitmap = outputImage;

        return outputImage;

    }
    public Bitmap applyContrastSubfilter(int contrast) {
        Filter myFilter = new Filter();

        Bitmap workingBitmap = Bitmap.createBitmap(mBitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888,true);

        myFilter.addSubFilter(new ContrastSubfilter(contrast));

        Bitmap outputImage = myFilter.processFilter(mutableBitmap);

        ContrastFilteredBitmap = outputImage;

        return outputImage;

    }
}

