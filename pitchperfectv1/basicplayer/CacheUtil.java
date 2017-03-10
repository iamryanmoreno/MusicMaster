
package pitchperfectv1.basicplayer;

import java.io.File;

//Manages temporary cache directories
public class CacheUtil {
    private static File mp3gCacheDir;
    private static File albumCacheDir;
    //Get mp3g cache dir. Make sure it exists.
    public static File getMP3GCacheDir() {
        if (mp3gCacheDir == null) {
            mp3gCacheDir = new File(getTopLevelCacheDir(), "mp3g");
            if (mp3gCacheDir.exists() == false) {
                System.out.println("Creating temp dir: " + mp3gCacheDir.getName());
                mp3gCacheDir.mkdir();
            }
        }
        return mp3gCacheDir;
    }
    
    //Get album cache dir. Make sure it exists.
    public static File getAlbumCacheDir() {
        if (albumCacheDir == null) {
            albumCacheDir = new File(getTopLevelCacheDir(), "album");
            if (albumCacheDir.exists() == false) {
                System.out.println("Creating temp dir: " + albumCacheDir.getName());
                albumCacheDir.mkdir();
            }
        }
        return albumCacheDir;
    }
    //Get top level cache dir(that hold all other cache dirs)
    private static File getTopLevelCacheDir() {
        // Make sure pitchperfect temp dir exists
        File topLevelCacheDir = new File(System.getProperty("java.io.tmpdir") + "/plarpebu");
        if (topLevelCacheDir.exists() == false) {
            System.out.println("Creating temp dir: " + topLevelCacheDir.getName());
            topLevelCacheDir.mkdir();
        }
        return topLevelCacheDir;
    }
}

