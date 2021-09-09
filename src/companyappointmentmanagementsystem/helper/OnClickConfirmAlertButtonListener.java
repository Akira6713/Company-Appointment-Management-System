package companyappointmentmanagementsystem.helper;

/**
 * Callback interface which is called when any button is clicked
 * in the confirmation alert.
 *
 * @see Helper#showConfirmAlert(String, String, String, String, OnClickConfirmAlertButtonListener)
 */
public interface OnClickConfirmAlertButtonListener {

    /**
     * Call when confirmation button is clicked
     *
     * @param isConfirmed {@literal true} is positive button is clicked,
     *                      {@literal false} otherwise
     */
    void onClickConfirmAlertButton(boolean isConfirmed);
}
