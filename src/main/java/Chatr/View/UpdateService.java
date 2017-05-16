package Chatr.View;

import Chatr.Helper.CONFIG;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Links two Properties together and guarantees that the given startService Function is executed in a set interval
 */
public class UpdateService extends ScheduledService<Void> {
	// order of the K, V of the maps is inverted because otherwise the uniqueness of the keys is violated when adding a
	// new high priority link
	private static Map<Supplier<Collection>, ListProperty> highPriorityList = new HashMap<>();
	private static Map<Supplier<String>, StringProperty> lowPriority = new HashMap<>();
	private static Logger log = LogManager.getLogger(UpdateService.class);
	private static UpdateService instance;

	private UpdateService() {
	}

	/**
	 * links the two properties unidirectionally at a slow startService Interval. Every startService the given function gets executed.
	 *
	 * @param source     source Property to be written to
	 * @param updateTask task to be executed on startService
	 */
	public static void linkLowPriority(StringProperty source, Supplier<String> updateTask) {
		StringProperty target = new SimpleStringProperty();
		source.bind(target);
		lowPriority.put(updateTask, target);
		startService();
		log.debug("created low priority link", source, target);
	}

	/**
	 * links the two properties unidirectionally at a fast startService Interval. Every startService the given function gets executed.
	 *
	 * @param source     source Property to be written to
	 * @param updateTask task to be executed on startService
	 */
	public static <T> void linkHighPriority(ObjectProperty<ObservableList<T>> source, Supplier<Collection> updateTask) {
		ObservableList<T> list = FXCollections.observableArrayList();
		ListProperty<T> property = new SimpleListProperty<>(list);
		source.bind(property);
		highPriorityList.put(updateTask, property);
		startService();
		log.debug("created high priority link", source, property);
	}

	/**
	 * private Method enforcing the singleton pattern and doing some setup for the Scheduled Service
	 *
	 * @return singleton Instance of the UpdateService
	 */
	private static UpdateService startService() {
		if (instance == null) {
			instance = new UpdateService();
			instance.setPeriod(Duration.millis(CONFIG.CLIENT_PULL_TIMER));
			instance.start();
		}
		return instance;
	}

	/**
	 * forces an Update Cycle to be run at some point in time in the Future
	 */
	public static void forceUpdate() {
		instance.restart();
	}

	/**
	 * creates the internal task to be executed when duration has expired. Calls the given functions.
	 *
	 * @return instance of the UpdateService
	 */
	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				long start = System.currentTimeMillis();
				for (final Map.Entry<Supplier<Collection>, ListProperty> job : highPriorityList.entrySet()) {
					final Collection result = job.getKey().get();
					Platform.runLater(() -> result.forEach(
							value -> job.getValue().add(value)
					));

				}
				log.trace("Updated high priority in " + (System.currentTimeMillis() - start) + "ms");
				start = System.currentTimeMillis();
				for (final Map.Entry<Supplier<String>, StringProperty> job : lowPriority.entrySet()) {
					final String result = job.getKey().get();
					Platform.runLater(() -> job.getValue().setValue(result));
				}
				log.trace("Updated low priority in " + (System.currentTimeMillis() - start) + "ms");
				return null;
			}
		};
	}
}
