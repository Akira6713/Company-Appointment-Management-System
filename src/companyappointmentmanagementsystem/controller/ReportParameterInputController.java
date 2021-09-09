package companyappointmentmanagementsystem.controller;

import companyappointmentmanagementsystem.model.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import static companyappointmentmanagementsystem.CompanyAppointmentManagementSystemApp.*;
import static companyappointmentmanagementsystem.helper.Helper.*;

/**
 * Controller for report generation parameter input form. This form
 * takes different inputs to generate report.
 */
public class ReportParameterInputController extends BaseController {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MMMM dd, yy hh:mm a");
    private static final String[] BY_VALUES = {
            getUITextResource().getString("viewreport.label.type"),
            getUITextResource().getString("viewreport.label.month")
    };
    private static final String[] MONTHS = {
            "January","February","March","April",
            "May","June","July","August",
            "September","October","November","December"
    };

    public ComboBox<Customer> inputCustomer;
    public ComboBox<String> inputBy;
    public ComboBox<String> inputTypeMonth;
    public Label labelTypeMonth;
    public ComboBox<Contact> inputContact;

    // all available appointment types
    private List<String> APPOINTMENT_TYPES;

    @Override
    protected URL getViewLocation() {
        return getClass().getResource("/view/report_parameters_input_form.fxml");
    }

    @Override
    public String getTitle() {
        return getUITextResource().getString("reportparameterinput.widow.title");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        APPOINTMENT_TYPES = Dao.getInstance().getAllAppointmentTypes();
        getWindow().setResizable(false);
        inputCustomer.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || null == item)
                    setText(null);
                else
                    setText(item.getCustomerName());
            }
        });
        inputCustomer.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || null == item)
                    setText(null);
                else
                    setText(item.getCustomerName());
            }
        });
        inputContact.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(Contact item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || null == item)
                    setText(null);
                else
                    setText(item.getContactName());
            }
        });
        inputContact.setCellFactory(param -> new ListCell<>(){
            @Override
            protected void updateItem(Contact item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || null == item)
                    setText(null);
                else
                    setText(item.getContactName());
            }
        });
        inputTypeMonth.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (null == item || empty)
                    setText(null);
                else
                    setText(item);
            }
        });
        inputTypeMonth.setCellFactory(param -> new ListCell<>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (null == item || empty)
                    setText(null);
                else
                    setText(item);
            }
        });
        inputBy.getItems().addAll(BY_VALUES);
        inputBy.valueProperty().addListener(((observable, oldValue, newValue) -> onChangeByValue(newValue)));
        inputCustomer.setItems(Dao.getInstance().getAllCustomers());
        inputContact.setItems(Dao.getInstance().getAllContacts());
        inputCustomer.getSelectionModel().select(0);
        inputBy.getSelectionModel().select(0);
        inputContact.getSelectionModel().select(0);
    }

    /**
     * Call when button clicked to show report on number of appointments of
     * a customer of selected type or in selected month
     */
    public void onClickCountCustomerAppointment() {
        final Customer selection = inputCustomer.getValue();
        final int byWhichIndex = inputTypeMonth.getSelectionModel().getSelectedIndex();
        final String byWhichValue = inputTypeMonth.getValue();
        final String by = inputBy.getValue();
        // either no customer is selected or no type/month is selected, then no report to show
        if (null == selection || byWhichIndex < 0) return;
        if (by == BY_VALUES[0]) {
            new GeneratedReportOutputController(String.format(getUIMessagesResource().getString("report.message.countcustomerappointmentstype"),
                    selection.getCustomerName(),
                    Dao.getInstance().countAppointmentsByTypeForCustomer(selection.getCustomerId(),
                            byWhichValue),byWhichValue))
                    .show(getWindow());
        }
        else if (by == BY_VALUES[1]) {
            new GeneratedReportOutputController(String.format(getUIMessagesResource().getString("report.message.countcustomerappointmentsmonth"),
                    selection.getCustomerName(),
                    Dao.getInstance().countAppointmentsOfMonthForCustomer(selection.getCustomerId(),
                            byWhichIndex+1),byWhichValue))
                    .show(getWindow());
        }
    }

    /**
     * Call when button clicked to show report on all appointments of the selected contact
     */
    public void onClickViewAppointmentsOfContact() {
        final Contact selection = inputContact.getValue();
        if (null != selection) {
            final List<Appointment> appointments = Dao.getInstance().getAppointmentsOfContact(selection.getContactId());
            final StringBuilder report = new StringBuilder();
            appointments.forEach(c -> {
                report.append(getUITextResource().getString("report.appointment.id")+": "+c.getAppointmentId()).append("\n");
                report.append(getUITextResource().getString("report.appointment.title")+": "+c.getTitle()).append("\n");
                report.append(getUITextResource().getString("report.appointment.description")+": "+c.getDescription()).append("\n");
                report.append(getUITextResource().getString("report.appointment.type")+": "+c.getType()).append("\n");
                report.append(getUITextResource().getString("report.appointment.startdatetime")+": "+convertTZ(c.getStart(),UTC, ZoneId.systemDefault()).format(DATE_TIME_FORMATTER))
                        .append("\n");
                report.append(getUITextResource().getString("report.appointment.enddatetime")+": "+convertTZ(c.getEnd(),UTC, ZoneId.systemDefault()).format(DATE_TIME_FORMATTER))
                        .append("\n");
                report.append(getUITextResource().getString("report.appointment.customer.id")+": "+c.getCustomerId()).append("\n");
                report.append("====================================================\n");
            });
            new GeneratedReportOutputController(report.toString()).show(getWindow());
        }
    }

    /**
     * Call when button clicked to show report on all appointments of the current user
     */
    public void onClickAppointmentsWithUser() {
        final User selection = getCurrentUser();
        final List<Appointment> appointments = Dao.getInstance().getAppointmentsWithUser(selection.getUserId());
        final StringBuilder report = new StringBuilder();
        appointments.forEach(c -> {
            report.append(getUITextResource().getString("report.appointment.id")+": "+c.getAppointmentId()).append("\n");
            report.append(getUITextResource().getString("report.appointment.title")+": "+c.getTitle()).append("\n");
            report.append(getUITextResource().getString("report.appointment.description")+": "+c.getDescription()).append("\n");
            report.append(getUITextResource().getString("report.appointment.type")+": "+c.getType()).append("\n");
            report.append(getUITextResource().getString("report.appointment.startdatetime")+": "+ utcToLocal(c.getStart()).format(DATE_TIME_FORMATTER))
                    .append("\n");
            report.append(getUITextResource().getString("report.appointment.enddatetime")+": "+ utcToLocal(c.getEnd()).format(DATE_TIME_FORMATTER))
                    .append("\n");
            report.append(getUITextResource().getString("report.appointment.customer.id")+": "+c.getCustomerId()).append("\n");
            report.append("======================================================\n");
        });
        new GeneratedReportOutputController(report.toString()).show(getWindow());
    }

    /**
     * Call when the value of "By" combo box is changed
     *
     * @param newValue the currently selected by value
     */
    private void onChangeByValue(String newValue) {
        final int oldSelection = inputTypeMonth.getSelectionModel().getSelectedIndex();
        inputTypeMonth.getItems().clear();
        if (newValue == BY_VALUES[1]) {
            // by month
            labelTypeMonth.setText(getUITextResource().getString("viewreport.label.month"));
            inputTypeMonth.getItems().addAll(MONTHS);
        }
        else if (newValue == BY_VALUES[0]) {
            // by type
            labelTypeMonth.setText(getUITextResource().getString("viewreport.label.type"));
            inputTypeMonth.getItems().addAll(APPOINTMENT_TYPES);
        }
        else {
            // no selection
            labelTypeMonth.setText(null);
        }
        // if old selection is still valid then keep it, otherwise select the first element
        if (oldSelection >= 0 && oldSelection < inputTypeMonth.getItems().size())
            inputTypeMonth.getSelectionModel().select(oldSelection);
        else
            inputTypeMonth.getSelectionModel().select(0);
    }
}
