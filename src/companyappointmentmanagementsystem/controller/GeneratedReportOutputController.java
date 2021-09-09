package companyappointmentmanagementsystem.controller;

import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

import static companyappointmentmanagementsystem.CompanyAppointmentManagementSystemApp.getUITextResource;

/**
 * Controller for generated report output form. This form simply show the report
 * as text.
 */
public class GeneratedReportOutputController extends  BaseController {

    public TextArea reportOutput;

    private final String report;

    /**
     * The text report to show
     *
     * @param report the report
     */
    public GeneratedReportOutputController(String report) {
        super();
        this.report = report;
    }

    @Override
    protected URL getViewLocation() {
        return getClass().getResource("/view/generated_report_output_form.fxml");
    }

    @Override
    public String getTitle() {
        return getUITextResource().getString("reportoutput.window.title");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getWindow().setResizable(false);
        reportOutput.setText(report);
    }
}
