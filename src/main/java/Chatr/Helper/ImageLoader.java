package Chatr.Helper;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class ImageLoader {
    private static Logger log = LogManager.getLogger(ImageLoader.class);

    /**
     * Loads an Image from a URL and returns it.
     * @param urlStr The image url
     * @param width The width the image shall have
     * @param height The hight the image shall have
     * @param preserveRatio If the ratio shall be preserved
     * @param smooth If a smooth filter shall be applied
     * @return Returns loaded image
     */
    public static Image loadImage(String urlStr, int width, int height, boolean preserveRatio, boolean smooth){
        return httpsLoad(urlStr, width, height, preserveRatio, smooth);
    }

    /**
     * Loads an Image from a URL and returns it as ObjectProperty.
     * @param urlStr The image url
     * @param width The width the image shall have
     * @param height The hight the image shall have
     * @param preserveRatio If the ratio shall be preserved
     * @param smooth If a smooth filter shall be applied
     * @return Returns loaded image as objectproperty
     */
    public static ObjectProperty<Image> loadImageOP(String urlStr, int width, int height, boolean preserveRatio, boolean smooth){
        ObjectProperty<Image> objImg = new SimpleObjectProperty<>();
        Image img = httpsLoad(urlStr, width, height, preserveRatio, smooth);
        objImg.set(img);
        return objImg;
    }

    /**
     * Loads an Image over an https connection
     * @param urlStr The image url
     * @param width The width the image shall have
     * @param height The hight the image shall have
     * @param preserveRatio If the ratio shall be preserved
     * @param smooth If a smooth filter shall be applied
     * @return Returns loaded image
     */
    private static Image httpsLoad(String urlStr, int width, int height, boolean preserveRatio, boolean smooth){
        Image img = null;
        try {
            URLConnection conn;
            URL url = new URL(urlStr);
            conn = url.openConnection();
            HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
            httpsConn.setRequestProperty("User-Agent", "Wget/1.9.1");
            httpsConn.setRequestProperty("Accept", "image/gif");
            img = new Image(httpsConn.getInputStream(), width, height, preserveRatio, smooth);
        } catch (IOException e) {
            log.error("Error while loading gif! urlString is: " + urlStr + ", Exception is " + e);
        }
        return img;
    }

    /**
     * Loads an Image without width, height, ratio or smooth.
     * @param urlStr The image url
     * @return Returns the loaded image
     */
    public static Image loadImageNoData(String urlStr){
        Image img = null;
        try {
            URL url;
            URLConnection conn;
            url = new URL(urlStr);
            conn = url.openConnection();
            HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
            httpsConn.setRequestProperty("User-Agent", "Wget/1.9.1");
            httpsConn.setRequestProperty("Accept", "image/gif");
            img = new Image(httpsConn.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

}
