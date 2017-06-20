package Chatr.Helper;

import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.giphy.GiphyImage;
import at.mukprojects.giphy4j.entity.search.SearchFeed;
import at.mukprojects.giphy4j.exception.GiphyException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executors;

public class GIFLoader {
    private static Logger log = LogManager.getLogger(GIFLoader.class);

    public static SearchFeed getGIFUrl(String searchString, int limit, int offset) {
        Giphy giphy = new Giphy("dc6zaTOxFJmzC");
        SearchFeed feed = null;
        try {
            if (searchString.isEmpty()) {
                feed = giphy.trend();
            } else {
                feed = giphy.search(searchString, limit, offset);
            }
        } catch (GiphyException e) {
            log.error("Could not load gif feed!" + e);
        }
        return feed;
    }

    public static ObjectProperty<Image> loadGIF(GiphyImage gifImage) {
        final ObjectProperty<Image> gifObj = new SimpleObjectProperty<>();
        String urlStr = gifImage.getUrl();
        Executors.newSingleThreadExecutor().execute(() -> {
            Image gifImg = new Image("icons/default_user.png", Integer.parseInt(gifImage.getWidth()), Integer.parseInt(gifImage.getHeight()), false, true);
            gifObj.set(gifImg);
            gifImg = ImageLoader.loadImage(urlStr, Integer.parseInt(gifImage.getWidth()), Integer.parseInt(gifImage.getHeight()), true, true);
            gifObj.set(gifImg);
        });
        return gifObj;
    }
}
