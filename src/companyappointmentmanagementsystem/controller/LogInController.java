package companyappointmentmanagementsystem.controller;

import companyappointmentmanagementsystem.helper.ValidationException;
import companyappointmentmanagementsystem.model.Dao;
import companyappointmentmanagementsystem.model.User;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static companyappointmentmanagementsystem.CompanyAppointmentManagementSystemApp.*;
import static companyappointmentmanagementsystem.helper.Helper.nonEmptyString;
import static companyappointmentmanagementsystem.helper.Helper.showErrorAlert;

/**
 * Controller for login form
 */
public class LogInController extends BaseController {

    public TextField inputUsername;
    public PasswordField inputPassword;

    @Override
    public String getTitle() { return getUITextResource().getString("loginform.window.title"); }

    @Override
    protected URL getViewLocation() { return getClass().getResource("/view/login_form.fxml"); }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { getWindow().setResizable(false); }

    /**
     * Call when login button is clicked
     */
    public void onClickLogIn() {
        if (validate()) {
            final String username = inputUsername.getText();
            final String password = inputPassword.getText();

            // fetch user with given username
            final Optional<User> oUser = Dao.getInstance().login(username);
            oUser.ifPresentOrElse(user -> {
                // user found
                if (!user.getPassword().equals(password)) {
                    // the password of the user does not match with user input,
                    // alert password not matching
                    saveLogInHistory(username,password,false);
                    showErrorAlert(getUITextResource().getString("loginform.validationerror.title"),
                            getUIMessagesResource().getString("loginform.error.nomatch.password"));
                }
                else {
                    // login successful
                    saveLogInHistory(username,password,true);
                    setCurrentUser(user);
                    new MainController().show();
                    exit();
                }
            }, () -> {
                // no user found with the given username, alert no user
                saveLogInHistory(username, password, false);
                showErrorAlert(getUITextResource().getString("loginform.validationerror.title"),
                        getUIMessagesResource().getString("loginform.error.nomatch.username"));
            });
        }
    }

    /**
     * Call when cancel button is clicked and exit the window
     */
    public void onClickCancel() {exit();}

    /**
     * Validate the inputs
     *
     * @return {@literal true} if all inputs are valid, {@literal false} otherwise
     */
    private boolean validate() {
        final String username = inputUsername.getText();
        final String password = inputPassword.getText();

        StringBuilder message = new StringBuilder();
        try {
            nonEmptyString(username,getUIMessagesResource().getString("loginform.error.empty.username"));
        }
        catch (ValidationException e) {
            message.append(e.getMessage()).append("\n");
        }
        try {
            nonEmptyString(password,getUIMessagesResource().getString("loginform.error.empty.password"));
        }
        catch (ValidationException e) {
            message.append(e.getMessage()).append("\n");
        }
        if (message.length() > 0) {
            showErrorAlert(getUITextResource().getString("loginform.validationerror.title"),message.toString());
            return false;
        }
        return true;
    }

    /**
     * Store the login activity, either it's successful or fail
     *
     * @param username the login username
     * @param password the login password
     * @param successful the login status {@literal true} if successful, {@literal false} otherwise
     */
    private void saveLogInHistory(String username, String password, boolean successful) {
        try (FileWriter writer  = new FileWriter("login_activity.txt",true)) {
            writer.write("["+LocalDateTime.now()+"] username=\""+username+"\",password=\""+password+"\",status="+successful+System.lineSeparator());
            writer.flush();
        }
        catch (IOException e) {}
    }
}
