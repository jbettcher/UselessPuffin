package com.android4all.uselesspuffin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private ArrayList<String> photos = new ArrayList<String>();
    private ImageView mainImage;
    private String currentPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainImage = (ImageView) findViewById(R.id.main_image_view);
        mainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idx = photos.indexOf(currentPhoto);
                if ( idx < 0 || idx >= (photos.size() - 1) ) {
                    idx = 0;
                }
                else {
                    idx++;
                }
                loadPhoto(photos.get(idx));
            }
        });

        // Populate the list of available images
        Field[] drawables = R.drawable.class.getFields();
        for (Field field : drawables) {
           if ( field.getName().matches("photo_\\d+")) {
               photos.add(field.getName());
           }
        }

        loadPhoto(photos.get(0));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void loadPhoto(String photoName) {
        try {
            loadPhotoFromResource(R.drawable.class.getField(photoName).getInt(R.drawable.class));
            currentPhoto = photoName;
        }catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private void loadPhotoFromResource(int resourceId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resourceId, options);

        int destHeight = mainImage.getHeight();
        int destWidth = mainImage.getWidth();
        int sampleSize = 1;

        if ( options.outHeight > destHeight || options.outWidth < destWidth ) {
            int heightRatio = Math.round((float) options.outHeight / (float) destHeight);
            int widthRatio = Math.round((float) options.outWidth / (float) destWidth);

            sampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;

        Bitmap bm =  BitmapFactory.decodeResource(getResources(), resourceId, options);
        mainImage.setImageBitmap(bm);
    }
    
}
