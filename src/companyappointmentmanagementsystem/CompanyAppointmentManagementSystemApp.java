package companyappointmentmanagementsystem;

import companyappointmentmanagementsystem.controller.LogInController;
import companyappointmentmanagementsystem.model.Dao;
import companyappointmentmanagementsystem.model.User;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * An {@link Application} sub class to create a new application instance
 */
public class CompanyAppointmentManagementSystemApp extends Application {

    // app locale currently in use
    // currently French (fr) and English (en)
    // are the only supported locale
    // If system locale if other than these,
    // then default locale set to English (en)
    private static Locale appLocale;
    // application main window
    private static Stage mainWindow;
    // currently logged in user
    private static User currentUser;
    // java.util.ResourceBundle of ui text
    private static ResourceBundle uiTextResource;
    // java.util.ResourceBundle of ui messages
    private static ResourceBundle uiMessagesResource;

    // check and set app default locale
    // and load the resource according the app locale
    static {
        final Locale systemLocale = Locale.getDefault();
        if (systemLocale.getLanguage().equals(Locale.FRENCH)) {
            System.out.println("Setting language to French");

            appLocale = Locale.FRENCH;
        }
        else
        {
            System.out.println("Setting language to English");
            appLocale = Locale.ENGLISH;
        }
        Locale.setDefault(appLocale);
        uiTextResource = ResourceBundle.getBundle("UIText",appLocale);
        uiMessagesResource = ResourceBundle.getBundle("UIMessages",appLocale);
    }

    /**
     * Returns application main window
     *
     * @return non null instance of {@link Stage}
     */
    public static Stage getMainWindow() { return mainWindow; }

    /**
     * Returns current logged in user, if any, or null
     *
     * @return an instance of {@link User}
     */
    public static User getCurrentUser() { return currentUser; }

    /*public static boolean isLoggedIn() { return null != currentUser; }*/

    /**
     * Set current logged in user
     *
     * @param user an instance of {@link User}
     */
    public static void setCurrentUser(final User user) { currentUser = user; }

    /**
     * Reads values from UIText_*.properties file in resources folder
     * in project root and return as {@link ResourceBundle}
     *
     * @return non null instance of {@link ResourceBundle}
     */
    public static ResourceBundle getUIMessagesResource() { return uiMessagesResource; }

    /**
     * Reads values from UIMessages_*.properties file in resources folder
     * in project root and return as {@link ResourceBundle}
     *
     * @return non null instance of {@link ResourceBundle}
     */
    public static ResourceBundle getUITextResource() { return uiTextResource; }

    @Override
    public void start(Stage stage) {
        mainWindow = stage;
        stage.setOnHiding(e -> Dao.getInstance().close());
        new LogInController().show();
    }

    public static void main(String[] args) { launch(args); }
}
