package companyappointmentmanagementsystem.model;

/**
 * This class represents a single record in contacts table
 */
public class Contact {

    private int contactId;
    private String contactName;

    /**
     * Constructor method to create new instance of an existing record
     *
     * @param contactId the contact id
     * @param contactName the contact name
     */
    public Contact(int contactId, String contactName) {
        this.contactId = contactId;
        this.contactName = contactName;
    }

    /**
     * Returns contact id of this contact
     *
     * @return the contact id
     */
    public int getContactId() { return contactId; }

    /**
     * Returns contact name of this contact
     *
     * @return the contact name
     */
    public String getContactName() { return contactName; }
}
