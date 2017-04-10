package es.glitch.and.bugs.popmovies;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by dbahls on 10/04/2017.
 */

public class NetworkTest {

    private final Context mContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void create_network_test() throws Exception {

        boolean isOnline = Utilities.isOnline(mContext);

        Log.i(NetworkTest.class.getName(), "isOnline = " + isOnline);

        assertTrue("You have to validate this yourself", true);

    }
}
