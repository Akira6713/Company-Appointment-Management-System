package companyappointmentmanagementsystem.controller;

import companyappointmentmanagementsystem.helper.ValidationException;
import companyappointmentmanagementsystem.model.Appointment;
import companyappointmentmanagementsystem.model.Contact;
import companyappointmentmanagementsystem.model.Customer;
import companyappointmentmanagementsystem.model.Dao;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import static companyappointmentmanagementsystem.CompanyAppointmentManagementSystemApp.*;
import static companyappointmentmanagementsystem.helper.Helper.*;

/**
 * Controller class to handle the view appointment input form
 */
public class AppointmentInputController extends BaseController {

    private final DateTimeFormatter SCHEDULE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MMMM dd, yy hh:mm a");

    public Label labelAction;
    public TextField inputTitle;
    public TextField inputDescription;
    public TextField inputLocation;
    public TextField inputType;
    public TextField inputStartDateTime;
    public TextField inputEndDateTime;
    public ComboBox<Customer> inputCustomer;
    public ComboBox<Contact> inputContact;

    // store the last selected schedule start date time in EST
    private final ObjectProperty<LocalDateTime> startDateTimeProperty = new SimpleObjectProperty<>();
    // store the last selected schedule end date time in EST
    private final ObjectProperty<LocalDateTime> endDateTimeProperty = new SimpleObjectProperty<>();
    // this java.util.Optional will store the Appointment object to update if it's a modify operation
    // otherwise it will be empty
    private Optional<Appointment> oAppointment = Optional.empty();

    /**
     * Set the current value of the {@link Appointment} in the input fields.
     * The parameter instance must be non null otherwise it may throw
     * {@link NullPointerException}
     *
     * @param appointment non null instance of {@link Appointment},
     */
    public void populateUI(Appointment appointment) {
        oAppointment = Optional.of(appointment);
        labelAction.setText(getUITextResource().getString("appointmentinput.label.modifyappointment"));
        inputTitle.setText(appointment.getTitle());
        inputDescription.setText(appointment.getDescription());
        inputLocation.setText(appointment.getLocation());
        inputType.setText(appointment.getType());
        selectContactByContactId(appointment.getContactId());
        selectCustomerByCustomerId(appointment.getCustomerId());
        startDateTimeProperty.setValue(convertTZ(appointment.getStart(),UTC,EST));
        endDateTimeProperty.setValue(convertTZ(appointment.getEnd(),UTC,EST));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inputCustomer.setCellFactory(cb -> new ListCell<>(){
                @Override
                protected void updateItem(Customer customer, boolean empty) {
                    super.updateItem(customer, empty);
                    if (empty || null == customer)
                        setText(null);
                    else
                        setText(customer.getCustomerName());
                }
            });
        inputCustomer.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                if (empty || null == customer)
                    setText(null);
                else
                    setText(customer.getCustomerName());
            }
        });
        inputContact.setCellFactory(cb -> new ListCell<>(){
            @Override
            protected void updateItem(Contact contact, boolean empty) {
                super.updateItem(contact, empty);
                if (empty || null == contact)
                    setText(null);
                else
                    setText(contact.getContactName());
            }
        });
        inputContact.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(Contact contact, boolean empty) {
                super.updateItem(contact, empty);
                if (empty || null == contact)
                    setText(null);
                else
                    setText(contact.getContactName());
            }
        });
        startDateTimeProperty.addListener(((observable, oldValue, newValue) -> {
            if(null != newValue)
                inputStartDateTime.setText(estToLocal(newValue).format(SCHEDULE_DATE_TIME_FORMATTER));
        }));
        endDateTimeProperty.addListener(((observable, oldValue, newValue) -> {
            if (null != newValue)
                inputEndDateTime.setText(estToLocal(newValue).format(SCHEDULE_DATE_TIME_FORMATTER));
        }));
        inputCustomer.setItems(Dao.getInstance().getAllCustomers());
        inputContact.setItems(Dao.getInstance().getAllContacts());
        // initially select the first elements of all ComboBox
        inputCustomer.getSelectionModel().select(0);
        inputContact.getSelectionModel().select(0);
        // initially set the start and end date time to current data time
        startDateTimeProperty.setValue(LocalDateTime.now());
        endDateTimeProperty.setValue(LocalDateTime.now());
    }

    @Override
    protected URL getViewLocation() {
        return getClass().getResource("/view/appointment_input_form.fxml");
    }

    @Override
    public String getTitle() {
        return getUITextResource().getString("appointmentinput.window.title");
    }

    /**
     * Call when appointment start date time input field is clicked
     */
    public void onPickStartDateTime() {
        DateTimeInputController controller = new DateTimeInputController();
        controller.setContentTitle(getUITextResource().getString("appointmentinput.label.startdatetime"));
        controller.show(getWindow());
        controller.getDateTimeProperty().addListener(((observable, oldValue, newValue) ->
                startDateTimeProperty.setValue(newValue)));
        // set the current date time of the DateTimePicker as last selected start date time
        controller.populateUI(startDateTimeProperty.get());
    }

    /**
     * Call when appointment end date time input field is clicked
     */
    public void onPickEndDateTime() {
        DateTimeInputController controller = new DateTimeInputController();
        controller.setContentTitle(getUITextResource().getString("appointmentinput.label.enddatetime"));
        controller.show(getWindow());
        controller.getDateTimeProperty().addListener(((observable, oldValue, newValue) ->
                endDateTimeProperty.setValue(newValue)));
        // set the current date time of the DateTimePicker as last selected end date time
        controller.populateUI(endDateTimeProperty.get());
    }

    /**
     * Call when save button is clicked
     */
    public void onClickSave() {
        // before saving check all inputs are valid
        if (validate()) {
            final String title = inputTitle.getText();
            final String description = inputDescription.getText();
            final String location = inputLocation.getText();
            final String type = inputType.getText();
            final LocalDateTime startDateTime = convertTZ(startDateTimeProperty.getValue(),EST,UTC);
            final LocalDateTime endDateTime = convertTZ(endDateTimeProperty.getValue(),EST,UTC);
            final Contact contact = inputContact.getValue();
            final Customer customer = inputCustomer.getValue();

            // store that save operation is successful or fail
            boolean successfullySave;
            if (oAppointment.isEmpty()) {
                // no instance of Appointment provided by the caller, hence it's a create operation
                successfullySave = Dao.getInstance().addAppointment(new Appointment(title,description,location,type,
                        startDateTime, endDateTime,
                        customer, getCurrentUser(),contact));
            }
            else {
                // an instance of Appointment found, so it's a modify operation
                Appointment appointment = oAppointment.get();
                appointment.setTitle(title);
                appointment.setDescription(description);
                appointment.setLocation(location);
                appointment.setType(type);
                appointment.setStart(startDateTime);
                appointment.setEnd(endDateTime);
                appointment.setContact(contact);
                appointment.setCustomer(customer);
                appointment.setLastUpdatedBy(getCurrentUser().getUserName());
                appointment.setLastUpdate(toUTC(LocalDateTime.now()));
                successfullySave = Dao.getInstance().updateAppointment(appointment);
            }
            // if save operation fails then alert it
            if (!successfullySave) {
                showErrorAlert(getUITextResource().getString("appointmentinput.saveerror.title"),
                        getUIMessagesResource().getString("appointmentinput.error.save"));
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
        final String title = inputTitle.getText();
        final String description = inputDescription.getText();
        final String location = inputLocation.getText();
        final String type = inputType.getText();
        final LocalDateTime startDateTime = startDateTimeProperty.getValue();
        final LocalDateTime endDateTime = endDateTimeProperty.getValue();
        final Customer customer = inputCustomer.getValue();
        final Contact contact = inputContact.getValue();

        StringBuilder message = new StringBuilder();
        try {
            nonEmptyString(title,getUIMessagesResource().getString("appointmentinput.error.empty.title"));
        }
        catch (ValidationException e) {
            message.append(e.getMessage()).append("\n");
        }
        try {
            nonEmptyString(description,getUIMessagesResource().getString("appointmentinput.error.empty.description"));
        }
        catch (ValidationException e) {
            message.append(e.getMessage()).append("\n");
        }
        try {
            nonEmptyString(location, getUIMessagesResource().getString("appointmentinput.error.empty.location"));
        }
        catch (ValidationException e) {
            message.append(e.getMessage()).append("\n");
        }
        try {
            nonEmptyString(type, getUIMessagesResource().getString("appointmentinput.error.empty.type"));
        }
        catch (ValidationException e) {
            message.append(e.getMessage()).append("\n");
        }
        try {
            nonNull(startDateTime,getUIMessagesResource().getString("appointment.error.noselection.startdatetime"));
        }
        catch (ValidationException e) {
            message.append(e.getMessage()).append("\n");
        }
        try {
            nonNull(endDateTime, getUIMessagesResource().getString("appointmentinput.error.noselection.enddatetime"));
        }
        catch (ValidationException e) {
            message.append(e.getMessage()).append("\n");
        }
        try {
            nonNull(customer, getUIMessagesResource().getString("appointmentinput.error.noselection.customer"));
        }
        catch (ValidationException e) {
            message.append(e.getMessage()).append("\n");
        }
        try {
            nonNull(contact, getUIMessagesResource().getString("appointmentinput.error.noselection.contact"));
        }
        catch (ValidationException e) {
            message.append(e.getMessage()).append("\n");
        }

        if (null != startDateTime && null != endDateTime) {
            LocalTime est8 = LocalTime.of(8,0);
            LocalTime est22 = LocalTime.of(22,0);
            LocalTime estStartTime = startDateTime.toLocalTime();
            LocalTime estEndTime = endDateTime.toLocalTime();

            // check that schedule start time is between 8 AM in EST
            // and 10 PM in EST, otherwise it is invalid
            if (estStartTime.isBefore(est8) || estStartTime.isAfter(est22)) {
                message.append(getUIMessagesResource().getString("appointmentinput.error.notinrange.startdatetime"))
                        .append("\n");
            }
            // check that schedule end time is between 8 AM in EST
            // and 10 PM in EST, otherwise it is invalid
            if (estEndTime.isBefore(est8) || estEndTime.isAfter(est22)) {
                message.append(getUIMessagesResource().getString("appointmentinput.error.notinrange.enddatetime"))
                        .append("\n");
            }
            // check that start datetime is a date time before end date time
            // otherwise it is invalid
            if (!startDateTime.isBefore(endDateTime)) {
                message.append(getUIMessagesResource().getString("appointmentinput.error.startnotbeforeend"))
                        .append("\n");
            }
            // check if the start and end date time overlaps with any other schedule
            // of the customer or the current user, if overlaps then it's invalid
            if (null != customer &&
                    Dao.getInstance().checkAppointmentOverlapping(
                            oAppointment.isPresent() ? oAppointment.get().getAppointmentId() : -1,
                            customer.getCustomerId(),
                            convertTZ(startDateTime,EST,UTC),
                            convertTZ(endDateTime,EST,UTC))) {
                message.append(getUIMessagesResource().getString("appointmentinput.error.customerscheduleoverlap"))
                        .append("\n");
            }
        }
        if (message.length() > 0) {
            // has atleast one error, show the alert
            showErrorAlert(getUITextResource().getString("appointment.validationerror.title"),
                    message.toString());
            return false;
        }
        return true;
    }

    /**
     * Select the contacts combo box by contact id
     *
     * @param contactId the contact id of the contact to select
     */
    private void selectContactByContactId(int contactId) {
        for (Contact c : inputContact.getItems()) {
            if (c.getContactId() == contactId) {
                inputContact.getSelectionModel().select(c);
                break;
            }
        }
    }

    /**
     * select the customers combo box by customer id
     *
     * @param customerId the customer id of the customer to select
     */
    private void selectCustomerByCustomerId(int customerId) {
        for (Customer c : inputCustomer.getItems()) {
            if (c.getCustomerId() == customerId) {
                inputCustomer.getSelectionModel().select(c);
                break;
            }
        }
    }
}
