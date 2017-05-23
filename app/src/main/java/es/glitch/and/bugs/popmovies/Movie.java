package es.glitch.and.bugs.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by dnlbh on 19/03/2017.
 */

public class Movie implements Parcelable {

    public static HashMap<Long, Movie> MOVIES_CACHE = new HashMap<Long, Movie>();

    public final long id;

    //todo dear reviewers, i wanted to keep this simple POJO light-weight. do we always need getters and setters? I'm tired of it, isn't that oldschool practice? I come across this style more and more often
    public String title;
    public String releaseDate;
    public String plotSynopsis;

    public double voteAvg;
    public int voteCount;

    public String thumbnailUrl;
    public String posterUrl;

    protected boolean isFavorite = false;
    public byte[] imgDataThumbnail;
    public byte[] imgDataPoster;

    public Movie(long id) {
        this.id = id;
        MOVIES_CACHE.put(id, this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(id);
        out.writeString(title);
        out.writeString(releaseDate);
        out.writeDouble(voteAvg);
        out.writeInt(voteCount);
        out.writeString(thumbnailUrl);
        out.writeString(posterUrl);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        releaseDate = in.readString();
        voteAvg = in.readDouble();
        voteCount = in.readInt();
        thumbnailUrl = in.readString();
        posterUrl = in.readString();
    }

}
