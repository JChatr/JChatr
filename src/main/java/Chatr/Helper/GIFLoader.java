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

import java.util.concurrent.Executors;

public class GIFLoader {
	private static Logger log = LogManager.getLogger(GIFLoader.class);

	/**
	 * Loads the gif feed from giphy
	 * @param searchString The search query. If empty get trending gifs
	 * @param limit The limit of how many gifs you want to load (giphy max is 100)
	 * @param offset The offset in the searchfeed
	 * @return Returns the searchfeed
	 */
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

	/**
	 * Loads an gif as an Objectproperty<Image>
	 * @param gifImage The Gifimage that should be loaded
	 * @return Returns gifimage as objectporperty
	 */
	public static ObjectProperty<Image> loadGIF(GiphyImage gifImage) {
		final ObjectProperty<Image> gifObj = new SimpleObjectProperty<>();
		String urlStr = gifImage.getUrl();
		Executors.newSingleThreadExecutor().execute(() -> {
			Image gifImg = new Image("icons/loading.gif", Integer.parseInt(gifImage.getWidth()), Integer.parseInt(gifImage.getHeight()), false, true);
			gifObj.set(gifImg);
			gifImg = ImageLoader.loadImage(urlStr, Integer.parseInt(gifImage.getWidth()), Integer.parseInt(gifImage.getHeight()), true, true);
			gifObj.set(gifImg);
		});
		return gifObj;
	}

	/**
	 * Calculates the width of the gifs in the Searchfeed, so that they align in the flowpane
	 * @param gifSize The sizes of the gifs in an array
	 * @param gifFeed The Searchfeed
	 * @param widthSizeScrollPane The size of the flowpane
	 * @param feedSize The size of the SearchFeed
	 * @param gifPaneHGap The HGap of the flowpane
	 * @return
	 */
	public static int[] calcWidth(int[] gifSize, SearchFeed gifFeed, int widthSizeScrollPane, int feedSize, int gifPaneHGap){
		for (int i = 0; i < feedSize; i++) {
			gifSize[i] = Integer.valueOf(gifFeed.getDataList().get(i).getImages().getFixedHeightSmall().getWidth());
		}
		int maxSize = widthSizeScrollPane;
		int sizeSum = 0;
		int indexSum = 0;
		for (int i = 0; i < feedSize; i++) {
			maxSize -= (2 * gifPaneHGap);
			indexSum++;
			sizeSum += gifSize[i];
			if (sizeSum > maxSize) {
				int runter = sizeSum - maxSize;
				int hoch = maxSize - (sizeSum - gifSize[i]);
				if (hoch < runter) {
					sizeSum -= gifSize[i];
					double scale = (double) sizeSum / maxSize;
					i--;
					indexSum--;
					for (int u = 0; u < indexSum; u++) {
						gifSize[i - u] = (int) (gifSize[i - u] / scale);
					}
					int gifSizeToBig = Integer.valueOf(gifFeed.getDataList().get((i+1)).getImages().getFixedHeight().getWidth());
					if(gifSizeToBig > widthSizeScrollPane){
						i++;
						gifSize[i] = (int) widthSizeScrollPane;
					}
				} else {
					double scale = (double) sizeSum / maxSize;
					for (int u = 0; u < indexSum; u++) {
						gifSize[i - u] = (int) (gifSize[i - u] / scale);
					}
				}
				indexSum = 0;
				sizeSum = 0;
				maxSize = widthSizeScrollPane;
			}
		}
		return gifSize;
	}
}
