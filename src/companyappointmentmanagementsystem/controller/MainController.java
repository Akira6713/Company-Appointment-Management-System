package companyappointmentmanagementsystem.controller;

import companyappointmentmanagementsystem.model.Appointment;
import companyappointmentmanagementsystem.model.Customer;
import companyappointmentmanagementsystem.model.Dao;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;
import java.util.ResourceBundle;

import static companyappointmentmanagementsystem.CompanyAppointmentManagementSystemApp.*;
import static companyappointmentmanagementsystem.helper.Helper.*;

/**
 * Controller for main form. This form opens after a successful login.
 */
public class MainController extends BaseController {

    private static final DateTimeFormatter SCHEDULE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yy hh:mm a");

    public Label labelUpcomingAppointment;
    public RadioButton optionMonth;
    public RadioButton optionWeek;
    public TableView<Appointment> appointmentsTable;
    public TableColumn<Appointment,Integer> colAppointmentId;
    public TableColumn<Appointment,String> colTitle;
    public TableColumn<Appointment,String> colDescription;
    public TableColumn<Appointment,String> colLocation;
    public TableColumn<Appointment, String> colContact;
    public TableColumn<Appointment,String> colType;
    public TableColumn<Appointment, String> colStartDateTime;
    public TableColumn<Appointment, String> colEndDateTime;
    public TableColumn<Appointment, Integer> colCustomerId;
    public TableView<Customer> customersTable;
    public TableColumn<Customer,Integer> colCustomerId1;
    public TableColumn<Customer,String> colCustomerName;
    public TableColumn<Customer,String> colPhone;
    public TableColumn<Customer,String> colAddress;
    public TableColumn<Customer,String> colPostalCode;
    public TableColumn<Customer,String> colCountry;
    public TableColumn<Customer,String> colDivision;

    public MainController() { super(getMainWindow()); }

    @Override
    protected URL getViewLocation() {
        return getClass().getResource("/view/main_form.fxml");
    }

    @Override
    public String getTitle() {
        return getUITextResource().getString("main.title");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getWindow().setMaximized(true);
        colCustomerId1.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        colCountry.setCellValueFactory(param -> new SimpleObjectProperty<>(){
            @Override
            public String getValue() {
                return param.getValue().getDivision().getCountryName();
            }
        });
        colDivision.setCellValueFactory(param -> new SimpleObjectProperty<>(){
            @Override
            public String getValue() {
                return param.getValue().getDivision().getDivision();
            }
        });
        customersTable.setItems(Dao.getInstance().getAllCustomers());
        colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colContact.setCellValueFactory(param -> new SimpleStringProperty() {
            @Override
            public String getValue() {
                return param.getValue().getContactName();
            }
        });
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colStartDateTime.setCellValueFactory(param -> new SimpleObjectProperty<>(){
            @Override
            public String getValue() {
                return convertTZ(param.getValue().getStart(),UTC, ZoneId.systemDefault()).format(SCHEDULE_DATE_TIME_FORMATTER);
            }
        });
        colEndDateTime.setCellValueFactory(param -> new SimpleObjectProperty<>(){
            @Override
            public String getValue() {
                return convertTZ(param.getValue().getEnd(),UTC, ZoneId.systemDefault()).format(SCHEDULE_DATE_TIME_FORMATTER);
            }
        });
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        ToggleGroup group = new ToggleGroup();
        group.getToggles().addAll(optionMonth,optionWeek);
        group.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> onChangeAppointViewBy()));
        group.selectToggle(optionMonth);

        // check for upcoming appoint
        final Optional<Appointment> upcomingAppointment = Dao.getInstance().getUpComingAppointment(
                toUTC(LocalDateTime.now().plusMinutes(15)).toLocalTime()
                ,getCurrentUser().getUserId());
        upcomingAppointment.ifPresentOrElse(c -> {
            labelUpcomingAppointment.setText(String.format(getUIMessagesResource().getString("main.message.upcomingappointment"),
                    utcToLocal(c.getStart()).format(SCHEDULE_DATE_TIME_FORMATTER),c.getAppointmentId()));
        },()->{
            labelUpcomingAppointment.setText(getUIMessagesResource().getString("main.message.noupcomingappointment"));
        });
    }

    /**
     * Call when add appointment button is clicked
     */
    public void onClickAddAppointment() {
        new AppointmentInputController()
                .show(getWindow());
    }

    /**
     * Call when modify appointment button is clicked
     */
    public void onClickModifyAppointment() {
        final Appointment selected = appointmentsTable.getSelectionModel().getSelectedItem();
        if (null != selected) {
            AppointmentInputController controller = new AppointmentInputController();
            controller.show(getWindow());
            controller.populateUI(selected);
        }
    }

    /**
     * Call when delete appointment button is clicked
     */
    public void onClickDeleteAppointment() {
        final Appointment selected = appointmentsTable.getSelectionModel().getSelectedItem();
        if (null != selected) {
            String message = String.format(getUIMessagesResource().getString("confirmdeleteappointment.message"),
                    selected.getTitle(),
                    selected.getCustomerName(),
                    utcToLocal(selected.getStart()).format(SCHEDULE_DATE_TIME_FORMATTER),
                    utcToLocal(selected.getEnd()).format(SCHEDULE_DATE_TIME_FORMATTER));
            showConfirmAlert(getUITextResource().getString("confirmdeleteappointment.title"),
                    message,getUITextResource().getString("confirmedeleteappointment.button.delete"),
                    getUITextResource().getString("confirmedeleteappointment.button.cancel"),
                    isConfirmed -> {
                if (isConfirmed)
                    Dao.getInstance().deleteAppointment(selected);
            });
        }
    }

    /**
     * Call when add customer button is clicked
     */
    public void onClickAddCustomer() {
        new CustomerInputController().show(getWindow());
    }

    /**
     * Call when modify customer button is clicked
     */
    public void onClickModifyCustomer() {
        final Customer selection = customersTable.getSelectionModel().getSelectedItem();
        if (null != selection) {
            CustomerInputController controller = new CustomerInputController();
            controller.show(getWindow());
            controller.populateUI(selection);
        }
    }

    /**
     * Call when delete customer button is clicked
     */
    public void onClickDeleteCustomer() {
        final Customer selection = customersTable.getSelectionModel().getSelectedItem();
        if (null != selection) {
            final String message = String.format(getUIMessagesResource().getString("confirmdeletecustomer.message"),
                    selection.getCustomerName());
            showConfirmAlert(getUITextResource().getString("confirmdeletecustomer.title"),
                    message,
                    getUITextResource().getString("confirmdeletecustomer.button.delete"),
                    getUITextResource().getString("confirmdeletecustomer.button.cancel"), isConfirmed -> {
                if (isConfirmed)
                    Dao.getInstance().deleteCustomer(selection);
                    });
        }
    }

    /**
     * Call when refresh customers button is clicked. It will
     * populate the customers table with updated and new customers.
     */
    public void onClickRefreshCustomersTable() {customersTable.setItems(Dao.getInstance().getAllCustomers());}

    /**
     * Call when refresh appointment button is clicked. It will
     * populate the appointments table with updated and new appointments.
     */
    public void onClickRefreshAppointmentsTable() {onChangeAppointViewBy();}

    /**
     * Call when generate report button is clicked. It will show
     * the form to choose which type of report to show and the
     * parameters for creating the report.
     */
    public void onClickGenerateReport() {
        new ReportParameterInputController().show(getWindow());
    }

    /**
     * Call when the appointment view by option is changed. Options are
     * view by current week of year or view by current month.
     */
    private void onChangeAppointViewBy() {
        LocalDate dateStart, dateEnd;
        if (optionMonth.isSelected()) {
            dateStart = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
            dateEnd = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        }
        else {
            dateStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            dateEnd = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        }
        final ObservableList<Appointment> appointments = Dao.getInstance().getAllAppointments(dateStart,dateEnd,getCurrentUser().getUserId());
        appointmentsTable.setItems(appointments);
    }
}
