package companyappointmentmanagementsystem.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static companyappointmentmanagementsystem.helper.Helper.toUTC;

/**
 * An abstract model class containing getter and setter methods common to
 * all model class
 */
public abstract class BaseModel {

    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;

    /**
     * Constructor method to create a new instance with create date and last update
     * value set as UTC current date and time and created by and last updated by set
     * as current user. Use this constructor while creating a new instance of a non
     * existing record.
     *
     * @param current_user username of the current user
     */
    public BaseModel(String current_user) {
        this(toUTC(LocalDateTime.now()),current_user,toUTC(LocalDateTime.now()),current_user);
    }

    /**
     * Constructor method to create a new instance with the given value. Use this constructor
     * when creating an new instance of an existing record
     *
     * @param createDate the create date time
     * @param createdBy the creator
     * @param lastUpdate the last update date time
     * @param lastUpdatedBy the updater
     */
    public BaseModel(LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy) {
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Returns the {@link LocalDateTime} instance in UTC for record creation date time
     *
     * @return date time of record creation
     */
    public LocalDateTime getCreateDate() { return createDate; }

    /**
     * Returns the {@link LocalDateTime} instance in UTC for record last update date time
     *
     * @return date time of record last update
     */
    public LocalDateTime getLastUpdate() { return lastUpdate; }

    /**
     * Sets record creation date time in UTC
     *
     * @param createDate non null instance of {@link LocalDateTime} creation date time
     */
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }

    /**
     * Returns the creator
     *
     * @return returns the creator
     */
    public String getCreatedBy() { return createdBy; }

    /**
     * Sets last update date time in UTC
     *
     * @param lastUpdate non null instance of  {@link LocalDateTime} in UTC of last update date time
     */
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }

    /**
     * Returns last updater
     *
     * @return returns last updater
     */
    public String getLastUpdatedBy() { return lastUpdatedBy; }

    /**
     * Set most recent updater
     *
     * @param lastUpdatedBy the most recent updater
     */
    public void setLastUpdatedBy(String lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }
}
