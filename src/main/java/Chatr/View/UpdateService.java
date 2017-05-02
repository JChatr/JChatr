package Chatr.View;

import Chatr.Helper.CONFIG;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Links two Properties together and guarantees that the given update Function is executed in a set interval
 */
public class UpdateService extends ScheduledService<Void> {
	private static Map<ObservableList, Consumer<ObservableList>> highPriorityList = new HashMap<>();
	private static Map<StringProperty, Consumer<StringProperty>> lowPriority = new HashMap<>();
	private static UpdateService instance;

	private UpdateService() {
	}

	/**
	 * links the two properties unidirectionally at a slow update Interval. Every update the given function gets executed.
	 * @param source source Property to be written to
	 * @param target target Property to be read from
	 * @param updateTask task to be executed on update
	 */
	public static void linkLowPriority(StringProperty source, StringProperty target, Consumer<StringProperty> updateTask) {
		source.bind(target);
		lowPriority.put(target, updateTask);
		startService();
	}
	/**
	 * links the two properties unidirectionally at a fast update Interval. Every update the given function gets executed.
	 * @param source source Property to be written to
	 * @param target target Property to be read from
	 * @param updateTask task to be executed on update
	 */
	public static <T> void linkHighPriority(ObjectProperty<ObservableList<T>> source, ObservableList<T> target, Consumer<ObservableList> updateTask) {
		ListProperty<T> property = new SimpleListProperty<T>(target);
		source.bind(property);
		highPriorityList.put(target, updateTask);
		startService();
	}

	/**
	 * private Method enforcing the singleton pattern and doing some setup for the Scheduled Service
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
	 * creates the internal task to be executed when duration has expired. Calls the given Functions.
	 * @return instance of the UpdateService
	 */
	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				for (Map.Entry<ObservableList, Consumer<ObservableList>> job : highPriorityList.entrySet()) {
					job.getValue().accept(job.getKey());
				}
				for (Map.Entry<StringProperty, Consumer<StringProperty>> job : lowPriority.entrySet()) {
					job.getValue().accept(job.getKey());
				}
				return null;
			}
		};
	}
}
