package companyappointmentmanagementsystem.model;

import java.time.LocalDateTime;

/**
 * This class represents a single record in appointments table
 */
public class Appointment extends BaseModel{

    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerId;
    private String customerName;
    private int userId;
    private int contactId;
    private String contactName;

    /**
     * Constructor to create an instance for an existsing record
     *
     * @param appointmentId appointment id
     * @param title appointment title
     * @param description appointment description
     * @param location appointment location
     * @param type appointment type
     * @param start appointment start date time in UTC
     * @param end appointment end date time in UTC
     * @param customerId customer id
     * @param customerName customer name
     * @param userId user id
     * @param contactId contact id
     * @param contactName contact name
     * @param createDate record create date time
     * @param createdBy creator of the record
     * @param lastUpdated record most recent recent update date time
     * @param lastUpdatedBy record most recent updater
     * @see BaseModel#BaseModel(LocalDateTime, String, LocalDateTime, String)
     */
    public Appointment(int appointmentId,
                       String title, String description, String location, String type,
                       LocalDateTime start, LocalDateTime end,
                       int customerId, String customerName,
                       int userId, int contactId, String contactName,
                       LocalDateTime createDate, String createdBy,
                       LocalDateTime lastUpdated, String lastUpdatedBy) {
        super(createDate, createdBy, lastUpdated, lastUpdatedBy);
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.customerName = customerName;
        this.userId = userId;
        this.contactId = contactId;
        this.contactName = contactName;
    }

    /**
     * Constructor to create an instance for new record
     *
     * @param title appointment title
     * @param description appointment description
     * @param location appointment location
     * @param type appointment type
     * @param start appointment start date time in UTC
     * @param end appointment end date time in UTC
     * @param customer non null instance of {@link Customer} related to this appointment
     * @param user non null inatance of {@link User} of current user
     * @param contact non null instance of {@link Contact} related to this appointment
     * @see BaseModel#BaseModel(String)
     */
    public Appointment( String title, String description, String location, String type,
                        LocalDateTime start, LocalDateTime end,
                        Customer customer, User user, Contact contact) {
        super(user.getUserName());
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customer.getCustomerId();
        this.customerName = customer.getCustomerName();
        this.userId = user.getUserId();
        this.contactId = contact.getContactId();
        this.contactName = contact.getContactName();
    }

    /**
     * Returns appointment id of this appointment
     *
     * @return the appointment id
     */
    public int getAppointmentId() { return appointmentId; }

    /**
     * Returns title of this appointment
     *
     * @return the title
     */
    public String getTitle() { return title; }

    /**
     * Set title of the appointment
     *
     * @param title the appointment title
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Returns description of this appointment
     *
     * @return the description
     */
    public String getDescription() { return description; }

    /**
     * Set description of this appointment
     *
     * @param description the appointment description
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Returns location of this appointment
     *
     * @return the location
     */
    public String getLocation() { return location; }

    /**
     * Set location of this appointment
     *
     * @param location the appointment location
     */
    public void setLocation(String location) { this.location = location; }

    /**
     * Returns type of this appointment
     *
     * @return the appointment type
     */
    public String getType() { return type; }

    /**
     * Set type of this appointment
     *
     * @param type appointment type
     */
    public void setType(String type) { this.type = type; }

    /**
     * Returns appointment start date time in UTC of this appointment
     *
     * @return non null inatance of {@link LocalDateTime}
     */
    public LocalDateTime getStart() { return start; }

    /**
     * Set start date time in UTC of this appointment
     *
     * @param start non null instance of {@link LocalDateTime}
     */
    public void setStart(LocalDateTime start) { this.start = start; }

    /**
     * Returns appointment end date time in UTC of this appointment
     *
     * @return non null inatance of {@link LocalDateTime}
     */
    public LocalDateTime getEnd() { return end; }

    /**
     * Set end date time in UTC of this appointment
     *
     * @param end non null instance of {@link LocalDateTime}
     */
    public void setEnd(LocalDateTime end) { this.end = end; }

    /**
     * Returns user id of user related this appointment
     *
     * @return the user id
     */
    public int getUserId() { return userId; }

    /**
     * Returns customer id of customer related this appointment
     *
     * @return the customer id
     */
    public int getCustomerId() { return customerId; }

    /**
     * Set customer related to this appointment
     *
     * @param customer non null instance of {@link Customer}
     */
    public void setCustomer(Customer customer) {
        this.customerId = customer.getCustomerId();
        this.customerName = customer.getCustomerName();
    }

    /**
     * Returns customer name of customer related this appointment
     *
     * @return the customer name
     */
    public String getCustomerName() { return customerName; }

    /**
     * Returns contact id of contact related this appointment
     *
     * @return the contact id
     */
    public int getContactId() { return contactId; }

    /**
     * Returns contact name of contact related this appointment
     *
     * @return the contact name
     */
    public String getContactName() { return contactName; }

    /**
     * Set contact related to this appointment
     *
     * @param contact non null instance of {@link Contact}
     */
    public void setContact(Contact contact) {
        this.contactName = contact.getContactName();
        this.contactId = contact.getContactId();
    }
}
