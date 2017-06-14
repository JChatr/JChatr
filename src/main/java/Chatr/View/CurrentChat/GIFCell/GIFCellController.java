package Chatr.View.CurrentChat.GIFCell;

import Chatr.View.Loader;
import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.giphy.GiphyImage;
import at.mukprojects.giphy4j.entity.search.SearchFeed;
import at.mukprojects.giphy4j.exception.GiphyException;
import com.sun.deploy.ui.ImageLoader;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

/**
 * Created by Matthias on 10.06.2017.
 */
public class GIFCellController extends Loader {
    private static Logger log = LogManager.getLogger(GIFCellController.class);
    @FXML
    private HBox parent;
    @FXML
    private ImageView gif;

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

    public static ObjectProperty<Image> loadGIF(GiphyImage gifImage, int offset) {
        final ObjectProperty<Image> gifObj = new SimpleObjectProperty<>();
        String urlStr = gifImage.getUrl();
        Executors.newSingleThreadExecutor().execute(() -> {
            Image gifImg = new Image("icons/default_user.png", Integer.parseInt(gifImage.getWidth()), Integer.parseInt(gifImage.getHeight()), false, true);
            gifObj.set(gifImg);
            URL url;
            URLConnection conn;
            try {
                url = new URL(urlStr);
                conn = url.openConnection();
                HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
                httpsConn.setRequestProperty("User-Agent", "Wget/1.9.1");
                httpsConn.setRequestProperty("Accept", "image/gif");
                //      gifImg = new Image(urlStr, Integer.parseInt(gifImage.getWidth()), Integer.parseInt(gifImage.getHeight()), false, true);
                gifImg = new Image(httpsConn.getInputStream(), Integer.parseInt(gifImage.getWidth()), Integer.parseInt(gifImage.getHeight()), true, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            gifObj.set(gifImg);
        });


        //Image gifImg = new Image(url, Integer.parseInt(gifImage.getWidth()), Integer.parseInt(gifImage.getHeight()), false, true);



        /*URL url;
        URLConnection conn;
        try {
            url = new URL(urlStr);
            conn = url.openConnection();
            HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
            System.out.println("gif url" + urlStr);
            httpsConn.setRequestProperty("User-Agent", "Wget/1.9.1");
            httpsConn.setRequestProperty("Accept", "image/gif");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return gifObj;

    }


}
