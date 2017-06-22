package Chatr.View.CurrentChat.MessageCell;

import Chatr.Controller.Manager;
import Chatr.Helper.DateFormatter;
import Chatr.Helper.ImageLoader;
import Chatr.Model.Message;
import Chatr.View.Loader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * renders the Message items in the current Chat box
 */
class GIFCellController extends Loader {
    @FXML
    private HBox parent;
    @FXML
    private Label userName;
    @FXML
    private GridPane gifBox;
    @FXML
    private Label timestamp;
    @FXML
    private Pane spacer;
    @FXML
    private Pane background;
    @FXML
    private ImageView userThumbnail;
    @FXML
    private ImageView gifIV;

    private Executor pool = Executors.newFixedThreadPool(4);

    private final static int MAX_WIDTH = 600;
    private final static int MIN_WIDTH = 50;
    private final static int MAX_HEIGHT = Integer.MAX_VALUE;
    private  static int MIN_HEIGHT = 40;
    private final static int WIDTH_PADDING = 20;

    private static Logger log = LogManager.getLogger(GIFCellController.class);

    GIFCellController() {
        load(this);
        linkProperties();
        addListeners();
    }

    /**
     * Same as setInfo in MessageCellCOntroller
     * @param message The message
     */
    public void setInfo(Message message) {
        resetData();
        userThumbnail.setManaged(false);
        userName.setText(message.getSender());
        timestamp.setText(DateFormatter.convertTimestamp(message.getTime()));
        gifIV.setFitWidth(message.getWidth());
        gifIV.setFitHeight(message.getHigth());
        pool.execute(()->{
            Image img = ImageLoader.loadImageNoData(message.getContent());
            Platform.runLater(()->{

                gifIV.imageProperty().set(img);
            });
        });


        if (!Manager.getLocalUserID().contentEquals(message.getSender())) {
            userThumbnail.setManaged(true);
            displayUserThumbnail(message.getSender());
            alignLeft();
        }
    }

    @Override
    public Parent getView() {
        return parent;
    }

    /**
     * resets all internal data to allow for object reuse
     */
    private void resetData() {
        userName.setText("");
        gifIV.imageProperty().unbind();
        gifIV.setImage(null);
        timestamp.setText("");
        userName.setText("");
        alignRight();
        userThumbnail.imageProperty().unbind();
        userThumbnail.setImage(null);
        gifBox.setPrefWidth(MIN_WIDTH);
        gifBox.setMaxWidth(MAX_WIDTH);
        parent.setPrefHeight(MIN_HEIGHT);
        parent.setMaxHeight(MAX_HEIGHT);
    }

    /**
     * aligns the message to the Right and changes the CSS appropriately
     */
    private void alignLeft() {
        spacer.toFront();
        userName.setVisible(true);
        userThumbnail.toBack();
        gifIV.setId("text-left");
        userName.setId("name-background");
        timestamp.setId("timestamp-background");
        timestamp.toFront();
        userName.toFront();
    }

    /**
     * aligns the message to the Left and changes the CSS appropriately
     */
    private void alignRight() {
        spacer.toBack();
        userName.setVisible(false);
        userThumbnail.toFront();
        gifIV.setId("text-right");
        userName.setId("text-right");
    }

    /**
     * adjusts cell size to match the image in the imageview
     */
    private void addListeners() {
        // gets called when the image is updated
        //see message cell
        gifIV.imageProperty().addListener(((observable, oldValue, newValue) -> {
            double width = gifIV.getLayoutBounds().getWidth();
            gifIV.setFitWidth(width);
            gifBox.setPrefWidth(width);
            gifBox.setMaxWidth(width);
            double height = gifIV.getLayoutBounds().getHeight();
            height += 15;
            parent.setPrefHeight(height);
        }));
    }

    private void linkProperties(){
        userName.managedProperty().bind(userName.visibleProperty());
    }

    private void displayUserThumbnail(String sender) {
        userThumbnail.imageProperty().unbind();
        userThumbnail.setImage(null);
        userThumbnail.imageProperty().bind(Manager.getUserImage(sender));
    }
}
