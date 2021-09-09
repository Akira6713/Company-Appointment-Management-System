package companyappointmentmanagementsystem.controller;

import companyappointmentmanagementsystem.CompanyAppointmentManagementSystemApp;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Base class for all controller class. The class contains useful methods
 * from loading fxml to showing in a window. Never set the controller value
 * in fxml otherwise it will throw exception.
 */
public abstract class BaseController implements Initializable {

    // the window for showing current view
    private final Stage window;

    /**
     * Constructor method to create the controller and show
     * the new view in a new window
     *
     * @see #BaseController(Stage)
     */
    protected BaseController() { this(new Stage()); }

    /**
     * Constructor method to create the controller and show
     * the new view in an existing window.
     *
     * @param window the existing {@link javafx.stage.Stage},
     *               must be non null otherwise will throw
     *               {@link NullPointerException}
     */
    protected BaseController(final Stage window) {
        if (null == window) {
            throw new NullPointerException("javafx.state.Stage in constructor must be non null");
        }
        this.window = window;
    }

    /**
     * Returns the current window
     *
     * @return the current window
     */
    public final Stage getWindow() { return window; }

    /**
     * Returns the title of the window
     *
     * @return the window title
     */
    public String getTitle() { return ""; }

    /**
     * Show the window without any parent
     *
     * @see #show(Stage)
     */
    public final void show() { show(null); }

    /**
     * Show the current window have the parent
     *
     * @param parent the parent window
     * @throws RuntimeException if any error occurs during loading fxml
     */
    public final void show(Stage parent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getViewLocation());
            loader.setController(this);
            loader.setResources(CompanyAppointmentManagementSystemApp.getUITextResource());
            if (null != parent) {
                window.initModality(Modality.WINDOW_MODAL);
                window.initOwner(parent);
            }
            Pane root = loader.load();
            window.setScene(new Scene(root));
            window.setTitle(getTitle());
            window.show();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Close the current window
     */
    public void exit() { window.close(); }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * Returns the location of the fxml file
     *
     * @return location to fxml file as {@link URL}
     */
    protected abstract URL getViewLocation();
}
