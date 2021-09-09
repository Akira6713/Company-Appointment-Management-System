package companyappointmentmanagementsystem.controller;

import companyappointmentmanagementsystem.helper.ValidationException;
import companyappointmentmanagementsystem.model.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static companyappointmentmanagementsystem.CompanyAppointmentManagementSystemApp.*;
import static companyappointmentmanagementsystem.helper.Helper.*;

/**
 * Controller for customer input form. This form is responsible for creating a
 * new customer and modifying an existing customer.
 */
public class CustomerInputController extends BaseController {

    public Label labelAction;
    public TextField inputName;
    public TextField inputAddress;
    public TextField inputPostalCode;
    public TextField inputPhone;
    public ComboBox<Country> inputCountry;
    public ComboBox<Division> inputDivision;

    // this java.util.Optional will store the Customer object to update if it's
    // a modify operation, otherwise it will be empty
    private Optional<Customer> oCustomer = Optional.empty();

    @Override
    protected URL getViewLocation() {
        return getClass().getResource("/view/customer_input_form.fxml");
    }

    @Override
    public String getTitle() {
        return getUITextResource().getString("customerinput.window.title");
    }

    /**
     * Set the current value of the {@link Customer} in the input fields.
     * The parameter instance must be non null otherwise it may throw
     * {@link NullPointerException}
     *
     * @param customer non null instance of {@link Customer},
     */
    public void populateUI(final Customer customer) {
        oCustomer = Optional.of(customer);
        labelAction.setText(getUITextResource().getString("customerinput.label.modifycustomer"));
        inputName.setText(customer.getCustomerName());
        inputAddress.setText(customer.getAddress());
        inputPhone.setText(customer.getPhone());
        inputPostalCode.setText(customer.getPostalCode());
        selectCountry(customer.getDivisionID());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inputCountry.setCellFactory(c -> new ListCell<>(){
            @Override
            protected void updateItem(Country item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || null == item)
                    setText(null);
                else
                    setText(item.getCountry());
            }
        });
        inputCountry.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(Country item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || null == item)
                    setText(null);
                else
                    setText(item.getCountry());
            }
        });
        inputDivision.setCellFactory(c -> new ListCell<>(){
            @Override
            protected void updateItem(Division item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || null == item)
                    setText(null);
                else
                    setText(item.getDivision());
            }
        });
        inputDivision.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(Division item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || null == item)
                    setText(null);
                else
                    setText(item.getDivision());
            }
        });
        inputCountry.setItems(Dao.getInstance().getAllCountry());
        inputCountry.valueProperty().addListener(((observable, oldValue, newValue) -> populateDivisions(newValue)));
        inputCountry.getSelectionModel().select(0);
    }

    /**
     * Call when save button is clicked
     */
    public void onClickSave() {
        if (validate()) {
            final String customerName = inputName.getText();
            final String address = inputAddress.getText();
            final String postalCode = inputPostalCode.getText();
            final String phone = inputPhone.getText();
            final Division division = inputDivision.getValue();

            // store that save operation is successful or fail
            boolean successfullySaved;
            if (oCustomer.isEmpty()) {
                // no instance of Customer provided by the caller, hence it's a create operation
                successfullySaved = Dao.getInstance().addCustomer(new Customer(customerName,address,postalCode,
                        phone,division,getCurrentUser().getUserName()));
            }
            else {
                // an instance of Customer found, so it's a modify operation
                Customer customer = oCustomer.get();
                customer.setCustomerName(customerName);
                customer.setAddress(address);
                customer.setPhone(phone);
                customer.setPostalCode(postalCode);
                customer.setDivisionID(division);
                customer.setLastUpdatedBy(getCurrentUser().getUserName());
                customer.setLastUpdate(toUTC(LocalDateTime.now()));
                successfullySaved = Dao.getInstance().updateCustomer(customer);
            }
            // if save operation fails then alert it
            if (!successfullySaved) {
                showErrorAlert(getUITextResource().getString("customerinput.saveerror.title"),
                        getUIMessagesResource().getString("customerinput.error.save"));
                return;
            }
            exit();
        }
    }

    /**
     * Call when cancel button is clicked and exit the window
     */
    public void onClickCancel() {exit();}

    /**
     * Validate the user inputs
     *
     * @return {@literal true} if all the user inputs are valid, {@link false} otherwise
     */
    private boolean validate() {
        final String customerName = inputName.getText();
        final String address = inputAddress.getText();
        final String postalCode = inputPostalCode.getText();
        final String phone = inputPhone.getText();
        final Country country = inputCountry.getValue();
        final Division division = inputDivision.getValue();

        StringBuilder message = new StringBuilder();
        try {
            nonEmptyString(customerName,getUIMessagesResource().getString("customerinput.error.empty.customername"));
        }
        catch (ValidationException e) {
            message.append(e.getMessage()).append("\n");
        }
        try {
            nonEmptyString(address,getUIMessagesResource().getString("customerinput.error.empty.address"));
        }
        catch (ValidationException e) {
            message.append(e.getMessage()).append("\n");
        }
        try {
            nonEmptyString(postalCode,getUIMessagesResource().getString("customerinput.error.empty.postalcode"));
        }
        catch (ValidationException e) {
            message.append(e.getMessage()).append("\n");
        }
        try {
            nonEmptyString(phone,getUIMessagesResource().getString("customerinput.error.empty.phone"));
        }
        catch (ValidationException e) {
            message.append(e.getMessage()).append("\n");
        }
        try {
            nonNull(country,getUIMessagesResource().getString("customerinput.error.noselection.country"));
        }
        catch (ValidationException e) {
            message.append(e.getMessage()).append("\n");
        }
        try {
            nonNull(division,getUIMessagesResource().getString("customerinput.error.noselection.division"));
        }
        catch (ValidationException e) {
            message.append(e.getMessage()).append("\n");
        }
        if (message.length() > 0) {
            showErrorAlert(getUITextResource().getString("customerinput.validationerror.title"),
                    message.toString());
            return false;
        }
        return true;
    }

    /**
     * Find country by division id and select the country in combo box
     *
     * @param divisionId the division id to find the country for
     */
    private void selectCountry(int divisionId) {
        for (Country c : inputCountry.getItems()) {
            for (Division d : c.getDivisions())
                if (d.getDivisionId() == divisionId) {
                    inputCountry.getSelectionModel().select(c);
                    return;
                }
        }
    }

    /**
     * Set the items of divisions combo box according to the selected
     * country
     *
     * @param country currently selected country
     */
    private void populateDivisions(final Country country) {
        final int selection = inputDivision.getSelectionModel().getSelectedIndex();
        if (null == country) return;
        inputDivision.setItems(country.getDivisions());
        int newSelection = 0;
        if (selection >= 0 && selection < inputDivision.getItems().size()) {
            newSelection = selection;
        }
        else if (oCustomer.isPresent()) {
            for (Division d : inputDivision.getItems()) {
                if (d.getDivisionId() == oCustomer.get().getDivisionID())
                    inputDivision.getSelectionModel().select(d);
            }
        }
        inputDivision.getSelectionModel().select(newSelection);
    }
}
