package Chatr.Helper;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Links two Properties together and guarantees that the given startService Function is executed in a set interval
 */
public class UpdateService {
	private static Map<ListProperty, Function> listProcesses = new ConcurrentHashMap<>();
	private static Map<ObjectProperty, Function> propertyProcesses = new ConcurrentHashMap<>();
	private static ScheduledExecutorService executor;
	private static Logger log = LogManager.getLogger(UpdateService.class);

	private UpdateService() {
	}

//	public static <T> void schedule(ObjectProperty<T> targetProperty,
//	                                Function<ObjectProperty<T>, ObjectProperty<T>> updateFunction) {
//		submitJob(targetProperty, updateFunction);
//		propertyProcesses.put(targetProperty, updateFunction);
//	}

	/**
	 * schedules the given function to be executed at a specified interval. Update is performed on the Property
	 *
	 * @param targetProperty Property to update
	 * @param updateFunction Function to execute on update
	 * @param <T>            Type of the list
	 */
	public static <T> void schedule(ObservableList<T> targetProperty,
	                                Function<List<T>, List<T>> updateFunction) {
		submitJob(targetProperty, updateFunction);
//		listProcesses.put(targetProperty, updateFunction);
	}

	private static void startService() {
		if (executor == null) {
			executor = Executors.newScheduledThreadPool(4, runnable -> {
				Thread t = Executors.defaultThreadFactory().newThread(runnable);
				t.setName("Update Service Worker");
				t.setDaemon(true);
				return t;
			});
		}
	}

	/**
	 * submits the jobs and executes them on a separate thread. The result of the function gets
	 * passed to the JavaFX Thread via Platform.runLater()
	 *
	 * @param property property to write the changes of the update to
	 * @param updateFunction function to update given property
	 */
	@SuppressWarnings("unchecked")
	private static void submitJob(final ObservableList property, Function updateFunction) {
		startService();
		executor.scheduleWithFixedDelay(() -> {
			try {
				List list = new ArrayList(property);
				List inserted = new ArrayList();
				final List result = (List) updateFunction.apply(list);
				result.forEach(res -> {
					if (!property.contains(res))
						inserted.add(res);
				});
				Platform.runLater(() -> {
					property.addAll(inserted);
				});
				log.trace("updated " + updateFunction);
			} catch (Throwable t) {
				log.error("unable to update " + updateFunction, t);
			}
		}, 100, CONFIG.CLIENT_PULL_TIMER, TimeUnit.MILLISECONDS);
	}
}