package com.android4all.uselesspuffin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import java.lang.ref.WeakReference;

/**
 * Created by jbettcher on 8/9/13.
 */
class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {

    private static final String TAG = BitmapWorkerTask.class.getSimpleName();
    private final WeakReference<ImageView> imageViewReference;

    public BitmapWorkerTask(ImageView imageView) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(Integer... params) {
        int resourceId = params[0];
        return loadPhotoFromResource(resourceId);
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private Bitmap loadPhotoFromResource(int resourceId) {
        final ImageView imageView = imageViewReference.get();

        if ( imageView != null ) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPurgeable = true;

            BitmapFactory.decodeResource(imageView.getResources(), resourceId, options);

            int destHeight = imageView.getResources().getDisplayMetrics().heightPixels;
            int destWidth = imageView.getResources().getDisplayMetrics().widthPixels;
            int sampleSize = 1;
            if ( options.outHeight > destHeight || options.outWidth > destWidth ) {
                int heightRatio = Math.round((float) options.outHeight / (float) destHeight);
                int widthRatio = Math.round((float) options.outWidth / (float) destWidth);

                sampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
                //Log.d(TAG, "sampleSize for " + options.outWidth + "x" + options.outHeight + " image -> " + destWidth + "x" + destHeight + ": " + sampleSize );
            }
            options.inSampleSize = sampleSize;
            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeResource(imageView.getResources(), resourceId, options);
        }
        else {
            return null;
        }


    }

}
