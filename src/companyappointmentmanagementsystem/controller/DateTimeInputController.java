package companyappointmentmanagementsystem.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;

import static companyappointmentmanagementsystem.CompanyAppointmentManagementSystemApp.getUITextResource;

/**
 * Controller for date time input form. This form is responsible to picking
 * date and time. The picked value is zone independent.
 */
public class DateTimeInputController extends BaseController {

    private static final String[] MONTHS = {
            "January","February","March","April",
            "May","June","July","August",
            "September","October","November","December"
    };
    private static final Integer[] YEARS = {2015,2016,2017,2018,2019,2020,2021,2022,2023,2024,2025};
    private static final Integer[] HOURS = {1,2,3,4,5,6,7,8,9,10,11,12};
    private static final Integer[] MINUTES = {
            0,1,2,3,4,5,6,7,8,9,
            10, 11,12,13,14,15,16,17,18,19,
            20,21,22,23,24,25,26,27,28,29,
            30,31,32,33,34,35,36,37,38,39,
            40,41,42,43,44,45,46,47,48,49,
            50,51,52,53,54,55,56,57,58,59
    };
    private static final String[] AM_PM = {"AM","PM"};

    public Label labelTitle;
    public ComboBox<String> inputMonth;
    public ComboBox<Integer> inputDay;
    public ComboBox<Integer> inputYear;
    public ComboBox<Integer> inputHour;
    public ComboBox<Integer> inputMinute;
    public ComboBox<String> inputAmPm;

    // currently selected date time value, caller of this form
    // may bind to this property to listen to date time. Value of
    // of this property is only changed when the date time is finally picked
    private ObjectProperty<LocalDateTime> dateTimeProperty = new SimpleObjectProperty<>();
    // the content title, could be the short message to show the purpose of the date time
    private String contentTitle;

    @Override
    public String getTitle() {
        return getUITextResource().getString("dateinput.window.title");
    }

    /**
     * Set the content title
     *
     * @param title the content title value
     */
    public void setContentTitle(String title) { this.contentTitle = title; }

    /**
     * Returns the picked date time property to bind and listen for change
     *
     * @return {@link ObjectProperty} of {@link LocalDateTime}
     */
    public ObjectProperty<LocalDateTime> getDateTimeProperty() {
        return dateTimeProperty;
    }

    /**
     * Set the input fields values for the given date time. The parameter must
     * be non null instance otherwise may throw {@link NullPointerException}
     *
     * @param dateTime non null {@link LocalDateTime} instance of new value
     */
    public void populateUI(final LocalDateTime dateTime) {
        final String month = dateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        final int day = dateTime.getDayOfMonth();
        final int year = dateTime.getYear();
        final int hour = dateTime.getHour();
        final int minute = dateTime.getMinute();
        final String amPm = hour >= 0 && hour < 12 ? "AM" : "PM";
        inputMonth.getSelectionModel().select(month);
        inputYear.getSelectionModel().select(Integer.valueOf(year));
        inputDay.getSelectionModel().select(Integer.valueOf(day));
        inputHour.getSelectionModel().select(Integer.valueOf(0 == hour || 12 == hour ? 12 : hour%12));
        inputMinute.getSelectionModel().select(Integer.valueOf(minute));
        inputAmPm.getSelectionModel().select(amPm);
    }

    @Override
    protected URL getViewLocation() {
        return getClass().getResource("/view/datetime_input_form.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getWindow().setResizable(false);
        labelTitle.setText(contentTitle);
        inputMonth.getItems().addAll(MONTHS);
        inputYear.getItems().addAll(YEARS);
        inputHour.getItems().addAll(HOURS);
        inputMinute.getItems().addAll(MINUTES);
        inputAmPm.getItems().addAll(AM_PM);
        inputMonth.valueProperty().addListener(l -> populateDays());
        inputYear.valueProperty().addListener(l -> populateDays());
        // initially set the value as current date time
        populateUI(LocalDateTime.now());
    }

    /**
     * Call when pick button is clicked
     */
    public void onClickPick() {
        final boolean isAM = 0 == inputAmPm.getSelectionModel().getSelectedIndex();
        final int hour = inputHour.getValue();
        // change the hour value into hour of day value
        final int hourOfDay = isAM ? hour : (hour+12)%24;
        LocalDateTime selection = LocalDateTime.of(inputYear.getValue(),
                inputMonth.getSelectionModel().getSelectedIndex()+1, inputDay.getValue(),
                hourOfDay,inputMinute.getValue());
        dateTimeProperty.setValue(selection);
        exit();
    }

    /**
     * Call when cancel button is clicked and exit this window
     */
    public void onClickCancel() {exit();}

    /**
     * Call when any of month combo box or year combo box value is changed,
     * this method populated the days combo box accordingly
     */
    private void populateDays() {
        final int selectedDayIdx = inputDay.getSelectionModel().getSelectedIndex();
        final int month = inputMonth.getSelectionModel().getSelectedIndex();
        final Integer year = inputYear.getValue();
        if (null == year || month < 0) return;
        int maxDays;
        if (1 == month) {
            maxDays = (0 == year%4 || 0 == year%400) && 0 != year%100 ? 29 : 28;
        }
        else if (3 == month || 5 == month || 8 == month || 10 == month) {
            maxDays = 30;
        }
        else {
            maxDays = 31;
        }
        inputDay.getItems().clear();
        for (int day = 1; day <= maxDays; day++) {
            inputDay.getItems().add(day);
        }
        // if old selection is still valid then keep it, otherwise select the first
        if (selectedDayIdx < maxDays)
            inputDay.getSelectionModel().select(selectedDayIdx);
        else
            inputDay.getSelectionModel().select(0);
    }
}
