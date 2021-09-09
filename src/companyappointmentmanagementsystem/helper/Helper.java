package companyappointmentmanagementsystem.helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

/**
 * This is class contains some repeatedly used methods
 */
public class Helper {
    /**
     * {@link java.time.ZoneId} for UTC time zone
     */
    public static final ZoneId UTC = ZoneId.of("UTC");
    /**
     * {@link java.time.ZoneId} for EST time zone
     */
    public static final ZoneId EST = ZoneId.of("EST",ZoneId.SHORT_IDS);

    /**
     * Converts date time value in local time zone to UTC time zone
     *
     * @param from the date time value
     * @return non null {@link java.time.LocalDateTime} in UTC time zone
     * @see #convertTZ(LocalDateTime, ZoneId, ZoneId)
     */
    public static LocalDateTime toUTC(LocalDateTime from) { return convertTZ(from,ZoneId.systemDefault(),UTC); }

    /**
     * Converts date time value in EST zone to local time zone
     *
     * @param from the date time value
     * @return non null {@link java.time.LocalDateTime} in local time zone
     * @see #convertTZ(LocalDateTime, ZoneId, ZoneId)
     */
    public static LocalDateTime estToLocal(LocalDateTime from) {
        return convertTZ(from,EST,ZoneId.systemDefault());
    }

    /**
     * Converts date time value in UTC to local time zone i.e. JVM default time zone
     *
     * @param from the date time value of apply transformation
     * @return non null {@link java.time.LocalDateTime} in local time zone
     * @see #convertTZ(LocalDateTime, ZoneId, ZoneId)
     */
    public static LocalDateTime utcToLocal(LocalDateTime from) { return convertTZ(from,UTC,ZoneId.systemDefault()); }

    /**
     * Coverts the time zone of {@link java.time.LocalDateTime}
     *
     * @param from the {@link java.time.LocalDateTime} to apply time zone transformation
     * @param fromZoneId the current time zone of the given date time value
     * @param toZoneId the target time zone value of the given date time
     * @return non null instance of {@link java.time.LocalDateTime} in new time zone
     */
    public static LocalDateTime convertTZ(LocalDateTime from, ZoneId fromZoneId, ZoneId toZoneId) {
        return from.atZone(fromZoneId).withZoneSameInstant(toZoneId).toLocalDateTime();
    }

    /**
     * Checks weather the input string is non-empty i.e. non-null and length greater than 0,
     * otherwise throws {@link ValidationException}
     *
     * @param value the string to check for non empty
     * @param errorMessage the error message to throw if the input string is empty
     * @see #isTrue(boolean, String)
     */
    public static void nonEmptyString(String value, String errorMessage) {
        isTrue(null != value && !"".equals(value),errorMessage);
    }

    /**
     * Checks weather the input string is non-null, otherwise throws {@link ValidationException}
     *
     * @param value the object to check for non nullity
     * @param errorMessage the error message to throw if the input object is null
     * @see #isTrue(boolean, String)
     */
    public static void nonNull(Object value, String errorMessage) {
        isTrue(null != value,errorMessage);
    }

    /**
     * Checks weather input boolean value is {@literal true}, otherwise throws {@link ValidationException}
     *
     * @param value the boolean to check
     * @param errorMessage the error message to throw when input boolean is false
     */
    public static void isTrue(boolean value, String errorMessage) {
        if (!value) {
            throw new ValidationException(errorMessage);
        }
    }

    /**
     * Show an error message in {@link javafx.scene.control.Alert}
     *
     * @param title title of the alert
     * @param message main content of the alert
     */
    public static void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.show();
    }

    /**
     * Show an {@link javafx.scene.control.Alert} with two confirmation buttons,
     * one for positive confirmation and another for negative confirmation
     *
     * @param title title of the alert
     * @param message main content of the alert
     * @param positiveText text for positive confirmation button
     * @param negativeText text for negative confirmation button
     * @param listener {@link companyappointmentmanagementsystem.helper.OnClickConfirmAlertButtonListener} listener to notify
     *                                                                                                      button click
     */
    public static void showConfirmAlert(String title, String message,
                                        String positiveText, String negativeText,
                                        OnClickConfirmAlertButtonListener listener) {
        ButtonType yes = new ButtonType(positiveText, ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType(negativeText, ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert confirmAlert = new Alert(Alert.AlertType.WARNING,message,yes,no);
        confirmAlert.setHeaderText(title);
        Optional<ButtonType> button = confirmAlert.showAndWait();
        button.ifPresent(c -> listener.onClickConfirmAlertButton(ButtonBar.ButtonData.YES == c.getButtonData()));
    }
}
