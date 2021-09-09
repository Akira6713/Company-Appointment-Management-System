package companyappointmentmanagementsystem.model;

import java.time.LocalDateTime;

/**
 * This class represents a single record in customers table
 */
public class Customer extends BaseModel {

    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private Division division;

    /**
     * Constructor method for creating a new instance of an existing record
     *
     * @param customerId customer id
     * @param customerName customer name
     * @param address customer address
     * @param postalCode customer postal code
     * @param phone customer phone
     * @param division customer division name
     * @param createDate record create date time in UTC
     * @param createdBy record creator
     * @param lastUpdated record most recent update date time in UTC
     * @param lastUpdatedBy record most recent updater
     * @see BaseModel#BaseModel(LocalDateTime, String, LocalDateTime, String)
     */
    public Customer(int customerId, String customerName, String address, String postalCode, String phone,
                    Division division,
                    LocalDateTime createDate, String createdBy,
                    LocalDateTime lastUpdated, String lastUpdatedBy) {
        super(createDate, createdBy, lastUpdated, lastUpdatedBy);
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.division = division;
    }

    /**
     * Constructor method for creating a new instance of new record
     *
     * @param customerName customer name
     * @param address customer address
     * @param postalCode customer postal code
     * @param phone customer phone
     * @param division customer division name
     * @param current_user the username of the current user
     * @see BaseModel#BaseModel(String)
     */
    public Customer(String customerName, String address, String postalCode, String phone, Division division, String current_user) {
        super(current_user);
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.division = division;
    }

    /**
     * Returns customer id of this customer
     *
     * @return customer id
     */
    public int getCustomerId() { return customerId; }

    /**
     * Returns customer name of this customer
     *
     * @return the customer name
     */
    public String getCustomerName() { return customerName; }

    /**
     * Set customer name of this customer
     *
     * @param customerName the customer name
     */
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    /**
     * Returns address of this customer
     *
     * @return the address
     */
    public String getAddress() { return address; }

    /**
     * Set address of this customer
     *
     * @param address the address
     */
    public void setAddress(String address) { this.address = address; }

    /**
     * Returns postal code of this customer
     *
     * @return the postal code
     */
    public String getPostalCode() { return postalCode; }

    /**
     * Set postal code of this customer
     *
     * @param postalCode the postal code
     */
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    /**
     * Returns phone number of this customer
     *
     * @return the phone number
     */
    public String getPhone() { return phone; }

    /**
     * Set the phone number of this customer
     *
     * @param phone new phone number
     */
    public void setPhone(String phone) { this.phone = phone; }

    /**
     * Returns division id of this customer
     *
     * @return the division id
     */
    public int getDivisionID() { return division.getDivisionId(); }

    /**
     * Returns the division of this customer
     *
     * @return non null instance of {@link Division}
     */
    public Division getDivision() { return division; }

    /**
     * Set the division of the customer
     *
     * @param division non null instance of {@link Division}
     */
    public void setDivisionID(Division division) { this.division = division; }
}
