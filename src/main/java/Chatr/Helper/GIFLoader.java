package Chatr.Helper;

import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.giphy.GiphyContainer;
import at.mukprojects.giphy4j.entity.giphy.GiphyImage;
import at.mukprojects.giphy4j.entity.search.SearchFeed;
import at.mukprojects.giphy4j.exception.GiphyException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Provides Methods to load a List of GIF Images from the giphy API.
 * All requests are asynchronous.
 * Returns an Observable list of all loaded Images
 */
public class GIFLoader {
	private ExecutorService gifPool;
	private volatile SearchFeed trendingFeed;
	private Giphy giphy;
	private AtomicInteger trendingCounter;
	private static Logger log = LogManager.getLogger(GIFLoader.class);
	private volatile Image loadingImage;

	public GIFLoader() {
		gifPool = new ThreadPoolExecutor(
				4,
				4,
				60L,
				TimeUnit.SECONDS,
				new ArrayBlockingQueue<>(200));
		this.giphy = new Giphy("dc6zaTOxFJmzC");
		trendingCounter = new AtomicInteger();
		preloadTrending();
		loadDefaultImage();
	}


	/**
	 * loads and caches the default loading animation asynchronously
	 */
	private void loadDefaultImage() {
		gifPool.submit(() -> {
			loadingImage = new Image("icons/loading.gif",
					300,
					300,
					false,
					true);
		});
	}

	/**
	 * pre-loads the trending Feed to be able to display some gif images as soon as the User clicks the GIF button
	 */
	private void preloadTrending() {
		gifPool.submit(() -> {
			log.info("Test");
			try {
				if (trendingFeed == null) {
					trendingFeed = giphy.trend();
					trendingCounter.set(1);
				}
			} catch (GiphyException e) {
				log.error("Could not load trending gifs!", e);
			}
		});
	}

	/**
	 * loads the GIF trendingFeed for the given searchString from the GIPHY API
	 *
	 * @param searchString The search query. If empty get trending gifs
	 * @param limit        The limit of how many gifs you want to load (giphy max is 100)
	 * @param offset       The offset in the searchfeed
	 * @return Returns the searchfeed
	 */
	private SearchFeed loadURLs(String searchString, int limit, int offset) {
		SearchFeed feed = null;
		try {
			feed = giphy.search(searchString, limit, offset);
			resetTrending();
		} catch (GiphyException e) {
			log.error("Could not load gif trendingFeed!", e);
		}
		return feed;
	}

	/**
	 * Loads the gifs for the given search string, limit and offset from the giphy API
	 *
	 * @param searchString Search query to send to GIFPHY
	 * @param limit        maximal amount of gifs to return
	 * @param offset       offset from the starting point of the query result
	 * @return an ObservableList of GifImages that get populated as soon as they are loaded from the server
	 */
	public synchronized ObservableList<GifImage> getGIFs(String searchString, int limit, int offset) {
		ObservableList<GifImage> images = FXCollections.observableArrayList();
		gifPool.submit(() -> {
			SearchFeed gifFeed = trendingFeed;
			if (!searchString.trim().isEmpty()) {
				System.out.println("loading gifs");
				gifFeed = loadURLs(searchString, limit, offset);
			}
			int feedSize = gifFeed.getDataList().size();
			if (feedSize == 0 || trendingCounter.get() == 2) {
				System.out.println("returned trending");
				return;
			}
			int gifSize[] = new int[feedSize];
			gifSize = calcWidth(gifSize, gifFeed, 315, feedSize, 2);
			for (int i = 0; i < feedSize; i++) {
				GifImage imageView = new GifImage();
				GiphyContainer container = gifFeed.getDataList()
						.get(i)
						.getImages();
				GiphyImage gifImage = container.getFixedHeightSmall();
				imageView.setFitWidth(gifSize[i]);
				imageView.setFitHeight(Double.parseDouble(gifImage.getHeight()));
				imageView.setId(container
						.getFixedHeight()
						.getUrl());
				imageView.setWidth(
						Integer.parseInt(container
								.getFixedHeight()
								.getWidth())
				);
				imageView.setHeight(
						Integer.parseInt(container
								.getFixedHeight()
								.getHeight())
				);
				images.add(imageView);
				imageView.imageProperty().bind(loadGIF(gifImage));
			}
			if (trendingCounter.get() == 1) trendingCounter.set(2);
		});
		return images;
	}

	public void resetTrending() {
		trendingCounter.set(1);
	}

	/**
	 * Loads an gif as an ObjectProperty<GifImage>
	 *
	 * @param gifImage The Gifimage that should be loaded
	 * @return Returns gifimage as ObjectProperty
	 */
	private ObjectProperty<Image> loadGIF(final GiphyImage gifImage) {
		final ObjectProperty<Image> gifObj = new SimpleObjectProperty<>();
		gifObj.set(loadingImage);
		String urlStr = gifImage.getUrl();
		gifPool.submit(() -> {
			gifObj.set(ImageLoader.loadImage(urlStr,
					Integer.parseInt(gifImage.getWidth()),
					Integer.parseInt(gifImage.getHeight()),
					true,
					true));
			log.trace("loaded gif " + gifImage.getUrl());
		});
		return gifObj;

	}

	/**
	 * Calculates the width of the gifs in the Searchfeed, so that they align in the flowpane
	 *
	 * @param gifSize             The sizes of the gifs in an array
	 * @param gifFeed             The Searchfeed
	 * @param widthSizeScrollPane The size of the flowpane
	 * @param feedSize            The size of the SearchFeed
	 * @param gifPaneHGap         The HGap of the flowpane
	 * @return the array of calculated widths
	 */
	private int[] calcWidth(int[] gifSize, SearchFeed gifFeed, int widthSizeScrollPane, int feedSize, int gifPaneHGap) {
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
					int gifSizeToBig = Integer.valueOf(gifFeed.getDataList().get((i + 1)).getImages().getFixedHeight().getWidth());
					if (gifSizeToBig > widthSizeScrollPane) {
						i++;
						gifSize[i] = widthSizeScrollPane;
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