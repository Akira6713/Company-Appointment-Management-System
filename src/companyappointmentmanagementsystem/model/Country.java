package companyappointmentmanagementsystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class represents a single record in countries table
 */
public class Country {

    private int countryId;
    private String country;
    private ObservableList<Division> divisions;

    /**
     * Constructor methos to create a new instance of country of
     * an existing record
     *
     * @param countryId the country id
     * @param country the country name
     */
    public Country(int countryId, String country) {
        this.countryId = countryId;
        this.country = country;
        this.divisions = FXCollections.observableArrayList();
    }

    /**
     * Returns the country id of this country
     *
     * @return the country id
     */
    public int getCountryId() { return countryId; }

    /**
     * Returns the country name of this country
     *
     * @return the country name
     */
    public String getCountry() { return country; }

    /**
     * Returns a list of divisions of this country
     *
     * @return non null instance of {@link ObservableList} of {@link Division} of this country
     */
    public ObservableList<Division> getDivisions() {
        return divisions;
    }
}
