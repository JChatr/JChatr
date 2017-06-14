package Chatr.View.CurrentChat.GIFCell;

import Chatr.View.Loader;
import at.mukprojects.giphy4j.Giphy;
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

import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

/**
 * Created by Matthias on 10.06.2017.
 */
public class GIFCellController extends Loader{
    private static Logger log = LogManager.getLogger(GIFCellController.class);
    @FXML
    private HBox parent;
    @FXML
    private ImageView gif;

    private void changeResolution(){

    }

    public static ObjectProperty<Image> getGIFs(String searchString, int offset){
        //  public static void getGIFs(String searchString, int limit){
        ObjectProperty<Image> gifObj = new SimpleObjectProperty();
        Giphy giphy = new Giphy("dc6zaTOxFJmzC");
        SearchFeed feed = null;
        try {
            if (searchString.isEmpty()) {
                feed = giphy.trend();
            } else {
                feed = giphy.search(searchString, 25, offset);
            }
        } catch (GiphyException e) {
            log.error("Could not load gif feed!" + e);
        }
        String url = "https://i.giphy.com/" + feed.getDataList().get(offset).getId() + ".gif";
        Image gifImg = new Image(url, 200, 100, true, false, true);
        gifObj.set(gifImg);
        return gifObj;
    }

    public static ListProperty<Image> getGIFsList(String searchString, int offset){
        ListProperty<Image> gifList = null;
        return gifList;
    }


}
