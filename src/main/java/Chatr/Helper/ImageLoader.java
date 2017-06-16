package Chatr.Helper;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class ImageLoader {

    public static Image loadImage(String urlStr, int width, int height, boolean preserveRatio, boolean smooth){
        return httpsLoad(urlStr, width, height, preserveRatio, smooth);
    }

    public static ObjectProperty<Image> loadImageOP(String urlStr, int width, int height, boolean preserveRatio, boolean smooth){
        ObjectProperty<Image> objImg = new SimpleObjectProperty<>();
        Image img = httpsLoad(urlStr, width, height, preserveRatio, smooth);
        objImg.set(img);
        return objImg;
    }

    private static Image httpsLoad(String urlStr, int width, int height, boolean preserveRatio, boolean smooth){
        Image img = null;
        try {
            URL url;
            URLConnection conn;
            url = new URL(urlStr);
            conn = url.openConnection();
            HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
            httpsConn.setRequestProperty("User-Agent", "Wget/1.9.1");
            httpsConn.setRequestProperty("Accept", "image/gif");
            img = new Image(httpsConn.getInputStream(), width, height, preserveRatio, smooth);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

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
