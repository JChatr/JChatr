package Chatr.View;

import Chatr.Helper.CONFIG;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Links two Properties together and guarantees that the given update Function is executed in a set interval
 */
public class UpdateService extends ScheduledService<Void> {
	private static Map<ListProperty, Supplier<Collection>> highPriorityList = new HashMap<>();
	private static Map<StringProperty, Supplier<String>> lowPriority = new HashMap<>();
	private static UpdateService instance;

	private UpdateService() {
	}

	/**
	 * links the two properties unidirectionally at a slow update Interval. Every update the given function gets executed.
	 *
	 * @param source     source Property to be written to
	 * @param updateTask task to be executed on update
	 */
	public static void linkLowPriority(StringProperty source, Supplier<String> updateTask) {
		StringProperty target = new SimpleStringProperty();
		source.bind(target);
		lowPriority.put(target, updateTask);
		startService();
	}

	/**
	 * links the two properties unidirectionally at a fast update Interval. Every update the given function gets executed.
	 *
	 * @param source     source Property to be written to
	 * @param updateTask task to be executed on update
	 */
	public static <T> void linkHighPriority(ObjectProperty<ObservableList<T>> source, Supplier<Collection> updateTask) {
		ObservableList<T> list = FXCollections.observableArrayList();
		ListProperty<T> property = new SimpleListProperty<>(list);
		source.bind(property);
		highPriorityList.put(property, updateTask);
		startService();
	}

	/**
	 * private Method enforcing the singleton pattern and doing some setup for the Scheduled Service
	 *
	 * @return
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
	 * creates the internal task to be executed when duration has expired. Calls the given functions.
	 *
	 * @return instance of the UpdateService
	 */
	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				for (final Map.Entry<ListProperty, Supplier<Collection>> job : highPriorityList.entrySet()) {
					final Collection result = job.getValue().get();
					Platform.runLater(() -> result.forEach(
							value -> job.getKey().add(value)
					));
				}
				for (final Map.Entry<StringProperty, Supplier<String>> job : lowPriority.entrySet()) {
					final String result = job.getValue().get();
					Platform.runLater(() -> job.getKey().setValue(result));
				}
				return null;
			}
		};
	}
}
