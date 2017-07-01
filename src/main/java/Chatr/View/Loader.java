package Chatr.View;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.ResourceBundle.getBundle;
import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * @author adam-bien.com, Maximilian Frühauf
 */
public class Loader {
	private final static String DEFAULT_ENDING = "Controller";
	private final static ExecutorService PARENT_CREATION_POOL = getExecutorService();
	private static Executor FX_PLATFORM_EXECUTOR = Platform::runLater;
	private static Logger log = LogManager.getLogger(Loader.class);
	private ObjectProperty<Object> presenterProperty;
	private javafx.fxml.FXMLLoader fxmlLoader;
	private String bundleName;
	private ResourceBundle bundle;
	private URL resource;

	/**
	 * Constructs the view lazily (fxml is not loaded) with empty injection
	 * context.
	 */
	public Loader() {
		this.init(getFXMLName());
	}

	static String stripEnding(String clazz) {
		if (!clazz.endsWith(DEFAULT_ENDING)) {
			return clazz;
		}
		int viewIndex = clazz.lastIndexOf(DEFAULT_ENDING);
		return clazz.substring(0, viewIndex);
	}

	public static ResourceBundle getResourceBundle(String name) {
		try {
			return getBundle(name);
		} catch (MissingResourceException ex) {
			return null;
		}
	}

	static ExecutorService getExecutorService() {
		return Executors.newCachedThreadPool((r) -> {
			Thread thread = Executors.defaultThreadFactory().newThread(r);
			String name = thread.getName();
			thread.setName("afterburner.fx-" + name);
			thread.setDaemon(true);
			return thread;
		});
	}

	private void init(final String conventionalName) {
		this.presenterProperty = new SimpleObjectProperty<>();
		this.resource = getClass().getResource(conventionalName);
		this.bundleName = getBundleName();
		this.bundle = getResourceBundle(bundleName);
	}

	private FXMLLoader loadSynchronously(final URL resource, final String conventionalName) throws IllegalStateException {
		final FXMLLoader loader = new FXMLLoader(resource);
		return loadFXML(loader, conventionalName);
	}

	private FXMLLoader loadSynchronously(final URL resource, final String conventionalName, Loader controller) throws IllegalStateException {
		final FXMLLoader loader = new FXMLLoader(resource);
		loader.setController(controller);
		return loadFXML(loader, conventionalName);
	}

	private FXMLLoader loadFXML(FXMLLoader loader, String conventionalName) {
		try {
			loader.load();
		} catch (IOException ex) {
			throw new IllegalStateException("Cannot load " + conventionalName, ex);
		}
		return loader;
	}

	public void load() {
		if (this.fxmlLoader == null) {
			this.fxmlLoader = this.loadSynchronously(resource, bundleName);
			this.presenterProperty.set(this.fxmlLoader.getController());
		}
	}

	public void load(Loader controller) {
		if (this.fxmlLoader == null) {
			this.fxmlLoader = this.loadSynchronously(resource, bundleName, controller);
			this.presenterProperty.set(this.fxmlLoader.getController());
		}
	}

	/**
	 * Initializes the view by loading the FXML (if not happened yet) and
	 * returns the top Node (parent) specified in
	 *
	 * @return the node loaded by Loader
	 */
	public Parent getView() {
		this.load();
		Parent parent = fxmlLoader.getRoot();
		addCSSIfAvailable(parent);
		return parent;
	}

	/**
	 * Initializes the view synchronously and invokes and passes the created
	 * parent Node to the consumer within the FX UI thread.
	 *
	 * @param consumer - an object interested in received the Parent as callback
	 */
	public void getView(Consumer<Parent> consumer) {
		Supplier<Parent> supplier = this::getView;
		supplyAsync(supplier, FX_PLATFORM_EXECUTOR).
				thenAccept(consumer).
				exceptionally(this::exceptionReporter);
	}

	/**
	 * Creates the view asynchronously using an internal thread pool and passes
	 * the parent node within the UI Thread.
	 *
	 * @param consumer - an object interested in received the Parent as callback
	 */
	public void getViewAsync(Consumer<Parent> consumer) {
		Supplier<Parent> supplier = this::getView;
		CompletableFuture.supplyAsync(supplier, PARENT_CREATION_POOL).
				thenAcceptAsync(consumer, FX_PLATFORM_EXECUTOR).
				exceptionally(this::exceptionReporter);
	}

	/**
	 * Scene Builder creates for each FXML document a root container. This
	 * method omits the root container (e.g. AnchorPane) and gives you the
	 * access to its first child.
	 *
	 * @return the first child of the AnchorPane
	 */
	public Node getViewWithoutRootContainer() {
		final ObservableList<Node> children = getView().getChildrenUnmodifiable();
		if (children.isEmpty()) {
			return null;
		}
		return children.listIterator().next();
	}

	void addCSSIfAvailable(Parent parent) {
		URL uri = getClass().getResource(getStyleSheetName());
		if (uri == null) {
			return;
		}
		String uriToCss = uri.toExternalForm();
		parent.getStylesheets().add(uriToCss);
	}

	String getStyleSheetName() {
		return getResourceCamelOrLowerCase(false, ".css");
	}

	/**
	 * @return the name of the fxml file derived from the FXML view. e.g. The
	 * name for the AirhacksView is going to be airhacks.fxml.
	 */
	final String getFXMLName() {
		return getResourceCamelOrLowerCase(true, ".fxml");
	}

	String getResourceCamelOrLowerCase(boolean mandatory, String ending) {
		String name = getConventionalName(false, ending);
		URL found = getClass().getResource(name);
		if (found != null) {
			return name;
		}
		log.debug("File: " + name + " not found, attempting with lowercase");
		name = getConventionalName(true, ending);
		found = getClass().getResource(name);
		if (mandatory && found == null) {
			final String message = "Cannot load file " + name;
			log.error(message);
			log.error("Stopping initialization phase...");
			throw new IllegalStateException(message);
		}
		return name;
	}

	/**
	 * In case the view was not initialized yet, the conventional fxml
	 * (airhacks.fxml for the AirhacksView and AirhacksPresenter) are loaded and
	 * the specified presenter / controller is going to be constructed and
	 * returned.
	 *
	 * @return the corresponding controller / presenter (usually for a
	 * AirhacksView the AirhacksPresenter)
	 */
	public Object getController() {
		this.load();
		return this.presenterProperty.get();
	}

	/**
	 * Does not initialize the view. Only registers the Consumer and waits until
	 * the the view is going to be created / the method FXMLView#getView or
	 * FXMLView#getViewAsync invoked.
	 *
	 * @param presenterConsumer listener for the presenter construction
	 */
	public void getController(Consumer<Object> presenterConsumer) {
		this.presenterProperty.addListener((ObservableValue<? extends Object> o, Object oldValue, Object newValue) -> {
			presenterConsumer.accept(newValue);
		});
	}

	/**
	 * @param lowercase indicates whether the simple class name should be
	 *                  converted to lowercase of left unchanged
	 * @param ending    the suffix to append
	 * @return the conventional name with stripped ending
	 */
	protected String getConventionalName(boolean lowercase, String ending) {
		return getConventionalName(lowercase) + ending;
	}

	/**
	 * @param lowercase indicates whether the simple class name should be
	 * @return the name of the view without the "View" prefix.
	 */
	protected String getConventionalName(boolean lowercase) {
		final String clazzWithEnding = this.getClass().getSimpleName();
		String clazz = stripEnding(clazzWithEnding);
		if (lowercase) {
			clazz = clazz.toLowerCase();
		}
		return clazz;
	}

	String getBundleName() {
		String conventionalName = getConventionalName(true);
		return this.getClass().getPackage().getName() + "." + conventionalName;
	}

	/**
	 * @return an existing resource bundle, or null
	 */
	public ResourceBundle getResourceBundle() {
		return this.bundle;
	}

	/**
	 * @param t exception to report
	 * @return nothing
	 */
	public Void exceptionReporter(Throwable t) {
		log.error(t);
		return null;
	}

}
