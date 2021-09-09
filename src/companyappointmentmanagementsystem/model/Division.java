package companyappointmentmanagementsystem.model;

/**
 * This class represents a single record in first level divisions table
 */
public class Division{

    private int divisionId;
    private String division;
    private String countryName;

    /**
     * Constructor method to create a new instance of an existing record
     *
     * @param divisionId division id
     * @param division division name
     * @param country non null instance of {@link Country} of belonging country
     */
    public Division(int divisionId, String division, Country country) {
        this.divisionId = divisionId;
        this.division = division;
        this.countryName = country.getCountry();
    }

    /**
     * Returns the division id of this division
     *
     * @return the division id
     */
    public int getDivisionId() { return divisionId; }

    /**
     * Returns the division name of this division
     *
     * @return the division name
     */
    public String getDivision() { return division; }

    /**
     * Returns belonging country name of this division
     *
     * @return belonging country name
     */
    public String getCountryName() { return countryName; }
}
