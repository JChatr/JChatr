package Chatr.View.CurrentChat.GIFCell;

import Chatr.View.Loader;
import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.search.SearchFeed;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Created by Matthias on 10.06.2017.
 */
public class GIFCellController extends Loader{
    @FXML
    private HBox parent;
    @FXML
    private ImageView gif;


    private void changeResolution(){

    }

    public ObjectProperty<Image>[] getGIFs(String searchString){
        Giphy giphy = new Giphy("dc6zaTOxFJmzC");
        SearchFeed feed = giphy.trend();

        feed.getDataList().get(0).getImages().getOriginal().getUrl();

    }

}
