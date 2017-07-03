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

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GIFLoader {
	private ExecutorService executor;
	private volatile SearchFeed feed;
	private Giphy giphy;
	private AtomicInteger trendingFeed;
	private static Logger log = LogManager.getLogger(GIFLoader.class);

	public GIFLoader() {
		executor = new ThreadPoolExecutor(
				4,
				8,
				10 * 60,
				TimeUnit.SECONDS,
				new ArrayBlockingQueue<>(200));
		this.giphy = new Giphy("dc6zaTOxFJmzC");
		trendingFeed = new AtomicInteger();
		preloadTrending();
	}

	private void preloadTrending() {
		executor.submit(() -> {
			log.info("Test");
			try {
				if (feed == null) {
					feed = giphy.trend();
					trendingFeed.set(1);
				}
			} catch (GiphyException e) {
				log.error("Could not load trending gifs!", e);
			}
		});
	}

	/**
	 * Loads the gif feed from giphy
	 *
	 * @param searchString The search query. If empty get trending gifs
	 * @param limit        The limit of how many gifs you want to load (giphy max is 100)
	 * @param offset       The offset in the searchfeed
	 * @return Returns the searchfeed
	 */
	private SearchFeed loadURLs(SearchFeed feed, String searchString, int limit, int offset) {
		try {
			if (!searchString.trim().isEmpty()) {
				feed = giphy.search(searchString, limit, offset);
				trendingFeed.set(0);
			}
		} catch (GiphyException e) {
			log.error("Could not load gif feed!", e);
		}
		return feed;
	}

	public ObservableList<GifImage> getGIFs(String searchString, int limit, int offset) {
		ObservableList<GifImage> images = FXCollections.observableArrayList();
		executor.submit(() -> {
			SearchFeed gifFeed = loadURLs(feed, searchString, limit, offset);
			int feedSize = gifFeed.getDataList().size();
			if (feedSize == 0 || trendingFeed.get() == 2) {
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
			if (trendingFeed.get() == 1) trendingFeed.set(2);
		});
		return images;
	}

	/**
	 * Loads an gif as an Objectproperty<GifImage>
	 *
	 * @param gifImage The Gifimage that should be loaded
	 * @return Returns gifimage as objectporperty
	 */
	private ObjectProperty<Image> loadGIF(GiphyImage gifImage) {
		final ObjectProperty<Image> gifObj = new SimpleObjectProperty<>();
		String urlStr = gifImage.getUrl();
		executor.submit(() -> {
			Image gifImg = new Image("icons/loading.gif",
					Integer.parseInt(gifImage.getWidth()),
					Integer.parseInt(gifImage.getHeight()),
					false,
					true);
			gifObj.set(gifImg);
			gifImg = ImageLoader.loadImage(urlStr,
					Integer.parseInt(gifImage.getWidth()),
					Integer.parseInt(gifImage.getHeight()),
					true,
					true);
			gifObj.set(gifImg);
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
	 * @return
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