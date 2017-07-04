package Chatr.Helper;

import javafx.collections.ObservableList;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class GIFLoaderTest {
	@Test
	public void noFeedsize() {
		GIFLoader gifload = new GIFLoader();
		ObservableList<GifImage> gifImages = gifload.getGIFs("qwertzuikjhgfdxcvbhgfdrtzhgfe34567uhgzuikjhuijhgfedcvgz7ztg", 25, 0);
		assertEquals(0, gifImages.size());
	}

	@Test
	public void gifLoadingTest() {
		GIFLoader gifload = new GIFLoader();
		ObservableList<GifImage> gifImages = gifload.getGIFs("cat", 25, 0);
		//getGIFs loads in another thread, needs time to complete
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//Checks if every GIF is a different one
		Set<String> stringSet = new HashSet<>();
		for (GifImage gifImage : gifImages) {
			stringSet.add(gifImage.getId());
		}
		assertEquals(gifImages.size(), stringSet.size());
		//Checks all 25 GIFs were loaded
		assertEquals(25, gifImages.size());
	}
}
