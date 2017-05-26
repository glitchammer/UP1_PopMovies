package es.glitch.and.bugs.popmovies.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import es.glitch.and.bugs.popmovies.Movie;
import es.glitch.and.bugs.popmovies.R;

/**
 * Created by dbahls on 10/04/2017.
 */
public class ImageUtils {

    public static byte[] getBytes(Bitmap bitmap)
    {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] image)
    {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static void loadMovieThumbnail(Context context, final Movie movie, final ImageView imgView) {
        Picasso.with(context)
                .load(movie.thumbnailUrl)
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_missing)
                .into(imgView, new Callback() {
                    @Override
                    public void onSuccess() {
                        movie.imgDataThumbnail = getBytes(((BitmapDrawable) imgView.getDrawable()).getBitmap());
                    }
                    @Override
                    public void onError() {
                        // ignore
                    }
                });
    }


    public static void loadMoviePoster(Context context, final Movie movie, final ImageView imgView) {
        Picasso.with(context)
                .load(movie.posterUrl)
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_missing)
                .into(imgView, new Callback() {
                    @Override
                    public void onSuccess() {
                        movie.imgDataPoster = getBytes(((BitmapDrawable) imgView.getDrawable()).getBitmap());
                    }
                    @Override
                    public void onError() {
                        // ignore
                    }
                });
    }


}
