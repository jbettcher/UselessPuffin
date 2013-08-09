package com.android4all.uselesspuffin;

import android.content.res.Configuration;
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

        if ( savedInstanceState != null ) {
            currentPhoto = savedInstanceState.getString("currentPhoto");
            if ( photos.indexOf(currentPhoto) < 0 ) {
                currentPhoto = null;
            }
        }
        if ( currentPhoto == null ) currentPhoto = photos.get(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( currentPhoto != null ) loadPhoto(currentPhoto);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("currentPhoto", currentPhoto);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (currentPhoto != null) loadPhoto(currentPhoto);
        super.onConfigurationChanged(newConfig);
    }

    private void loadPhoto(String photoName) {
        try {
            int resId = R.drawable.class.getField(photoName).getInt(R.drawable.class);
            BitmapWorkerTask task = new BitmapWorkerTask(mainImage);
            task.execute(resId);
            currentPhoto = photoName;
        }catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


}
