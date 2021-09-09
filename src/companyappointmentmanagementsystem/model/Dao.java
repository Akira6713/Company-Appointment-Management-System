package companyappointmentmanagementsystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static companyappointmentmanagementsystem.helper.Helper.toUTC;

/**
 * This class contains method to access the database. Use the methods of
 * this class to perform CRUD operation on different table. MySQL database
 * is used for this application. So, the database configuration is read from
 * a configuration file 'config/mysql_database_connection_parameters.properties'
 * under project root.
 */
public final class Dao {

    // singleton instance
    private static Dao instance;
    // java.sql.Connection object for the MySQL db
    private final Connection connection;

    /**
     * Constructor method to read connection parameters from configuration file
     * and connect to database
     *
     * @throws IOException throws if error occurs during reading configuration file
     * @throws ClassNotFoundException throws if error occurs during loading MySQL driver class
     * @throws SQLException throws if error occurs during connecting to database
     */
    private Dao() throws IOException, ClassNotFoundException, SQLException {
        Properties params = new Properties();
        params.load(new FileReader("config/mysql_database_connection_parameters.properties"));
        final String host = params.getProperty("mysql.host");
        final String port = params.getProperty("mysql.port");
        final String database = params.getProperty("mysql.database");
        final String user = params.getProperty("mysql.user");
        final String password = params.getProperty("mysql.password");

        Class.forName("com.mysql.jdbc.Driver");
        this.connection = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database+"?useSSL=false",
                user,password);
    }

    /**
     * Creates an singleton instance if not exists and returns
     *
     * @return singleton instance of this object
     */
    public static Dao getInstance() {
        if (null == instance) {
            try {
                instance = new Dao();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    /**
     * Query an user by user name, and returns an instance of
     * {@link User} if found, or null
     *
     * @param userName the username of the user to find
     * @return {@link Optional} of {@link User}
     *
     */
    public Optional<User> login(String userName) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT user_id,user_name,password FROM users WHERE user_name = ?;")) {
            ps.setString(1,userName);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                return Optional.of(new User(rs.getInt("user_id"),
                        rs.getString("user_name"),rs.getString("password")));
            }
        } catch (SQLException ignore) {}
        return Optional.empty();
    }

    /**
     * Inserts a new {@link Customer}
     *
     * @param newCustomer an instance of {@link Customer} to insert. It must a non null instance,
     *                   otherwise will throw {@link NullPointerException}
     * @return {@literal true} if insert is successful, {@literal false} otherwise
     */
    public boolean addCustomer(Customer newCustomer) {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO customers " +
                "(customer_name,address,postal_code,phone,create_date,created_by,last_update,last_updated_by,division_id) " +
                "VALUES(?,?,?,?,?,?,?,?,?);")) {
            ps.setString(1,newCustomer.getCustomerName());
            ps.setString(2,newCustomer.getAddress());
            ps.setString(3,newCustomer.getPostalCode());
            ps.setString(4,newCustomer.getPhone());
            ps.setTimestamp(5,Timestamp.valueOf(newCustomer.getCreateDate()));
            ps.setString(6,newCustomer.getCreatedBy());
            ps.setTimestamp(7,Timestamp.valueOf(newCustomer.getLastUpdate()));
            ps.setString(8,newCustomer.getLastUpdatedBy());
            ps.setInt(9,newCustomer.getDivisionID());
            ps.execute();
            return true;
        }
        catch (SQLException ignore) {}
        return false;
    }

    /**
     * Inserts a new {@link Appointment}
     *
     * @param newAppointment an instance of {@link Appointment} to insert. It must a non null instance,
     *                   otherwise will throw {@link NullPointerException}
     * @return {@literal true} if insert is successful, {@literal false} otherwise
     */
    public boolean addAppointment(Appointment newAppointment) {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO appointments " +
                "(title,description,location,type,start,end," +
                "create_date,created_by,last_update,last_updated_by," +
                "customer_id,user_id,contact_id) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);")) {
            ps.setString(1,newAppointment.getTitle());
            ps.setString(2,newAppointment.getDescription());
            ps.setString(3,newAppointment.getLocation());
            ps.setString(4,newAppointment.getType());
            ps.setTimestamp(5,Timestamp.valueOf(newAppointment.getStart()));
            ps.setTimestamp(6,Timestamp.valueOf(newAppointment.getEnd()));
            ps.setTimestamp(7,Timestamp.valueOf(newAppointment.getCreateDate()));
            ps.setString(8,newAppointment.getCreatedBy());
            ps.setTimestamp(9,Timestamp.valueOf(newAppointment.getLastUpdate()));
            ps.setString(10,newAppointment.getLastUpdatedBy());
            ps.setInt(11,newAppointment.getCustomerId());
            ps.setInt(12,newAppointment.getUserId());
            ps.setInt(13,newAppointment.getContactId());
            ps.execute();
            return true;
        }
        catch (SQLException ignore) {}
        return false;
    }

    /**
     * Updates a new {@link Customer}
     *
     * @param customer an instance of {@link Customer} with new values. It must a non null instance,
     *                   otherwise will throw {@link NullPointerException}
     * @return {@literal true} if update is successful, {@literal false} otherwise
     */
    public boolean updateCustomer(Customer customer) {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE customers  SET " +
                        "customer_name = ?, address = ?, phone = ?, postal_code = ?, last_update = ?, last_updated_by = ?, division_id = ? " +
                        "WHERE customer_id = ?")) {
            ps.setString(1,customer.getCustomerName());
            ps.setString(2,customer.getAddress());
            ps.setString(3,customer.getPhone());
            ps.setString(4,customer.getPostalCode());
            ps.setTimestamp(5,Timestamp.valueOf(customer.getLastUpdate()));
            ps.setString(6,customer.getLastUpdatedBy());
            ps.setInt(7,customer.getDivisionID());
            ps.setInt(8,customer.getCustomerId());
            ps.execute();
            return true;
        }
        catch (SQLException ignore) {}
        return false;
    }

    /**
     * Updates a new {@link Appointment}
     *
     * @param appointment an instance of {@link Appointment} with new values. It must a non null instance,
     *                   otherwise will throw {@link NullPointerException}
     * @return {@literal true} if update is successful, {@literal false} otherwise
     */
    public boolean updateAppointment(Appointment appointment) {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE appointments SET" +
                " title = ?, description = ?, location = ?, type = ?, start = ?, end = ?," +
                " last_update = ?, last_updated_by = ?, customer_id = ?, user_id = ?, contact_id = ?" +
                " WHERE appointment_id = ?")) {
            ps.setString(1,appointment.getTitle());
            ps.setString(2,appointment.getDescription());
            ps.setString(3,appointment.getLocation());
            ps.setString(4,appointment.getType());
            ps.setTimestamp(5,Timestamp.valueOf(appointment.getStart()));
            ps.setTimestamp(6,Timestamp.valueOf(appointment.getEnd()));
            ps.setTimestamp(7,Timestamp.valueOf(toUTC(LocalDateTime.now())));
            ps.setString(8,appointment.getLastUpdatedBy());
            ps.setInt(9,appointment.getCustomerId());
            ps.setInt(10,appointment.getUserId());
            ps.setInt(11,appointment.getContactId());
            ps.setInt(12,appointment.getAppointmentId());
            ps.execute();
            return true;
        }
        catch (SQLException ignore) {ignore.printStackTrace();}
        return false;
    }

    /**
     * deletes a {@link Customer}
     *
     * @param customer an instance of {@link Customer} to delete. It must a non null instance,
     *                   otherwise will throw {@link NullPointerException}
     * @return {@literal true} if delete is successful, {@literal false} otherwise
     */
    public boolean deleteCustomer(Customer customer) {
        String sql = "DELETE FROM customers WHERE customer_id = ?;";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1,customer.getCustomerId());
            ps.execute();
            return true;
        }
        catch (SQLException ignore) {}
        return false;
    }

    /**
     * deletes a {@link Appointment}
     *
     * @param appointment an instance of {@link Appointment} to delete. It must a non null instance,
     *                   otherwise will throw {@link NullPointerException}
     * @return {@literal true} if delete is successful, {@literal false} otherwise
     */
    public boolean deleteAppointment(Appointment appointment) {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?;";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1,appointment.getAppointmentId());
            ps.execute();
            return true;
        }
        catch (SQLException ignore) {}
        return false;
    }

    /**
     * Fetch all appointments of the given user in the range of given dates
     *
     * @param startDate the minimum date of appointment start date
     * @param endDate the maximum date of appointment start date
     * @param userId the user id of the user
     * @return non null instance {@link ObservableList} of {@link Appointment}
     */
    public ObservableList<Appointment> getAllAppointments(final LocalDate startDate, final LocalDate endDate, final int userId) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try (PreparedStatement ps = connection.prepareStatement("SELECT a.*, c.contact_name, c1.customer_name " +
                "FROM appointments AS a INNER JOIN contacts AS c ON a.contact_id = c.contact_id " +
                "INNER JOIN customers as c1 ON c1.customer_id = a.customer_id " +
                "WHERE user_id = ? AND DATE(a.start) BETWEEN ? AND ? ORDER BY start ASC;")) {
            ps.setInt(1,userId);
            ps.setDate(2,Date.valueOf(startDate));
            ps.setDate(3,Date.valueOf(endDate));
            ResultSet ars = ps.executeQuery();
            if (null != ars) {
                while (ars.next()) {
                    appointments.add(new Appointment(ars.getInt("appointment_id"),
                            ars.getString("title"),
                            ars.getString("description"),
                            ars.getString("location"),
                            ars.getString("type"),
                            ars.getTimestamp("start").toLocalDateTime(),
                            ars.getTimestamp("end").toLocalDateTime(),
                            ars.getInt("customer_id"),ars.getString("customer_name"),
                            ars.getInt("user_id"),
                            ars.getInt("contact_id"), ars.getString("contact_name"),
                            ars.getTimestamp("create_date").toLocalDateTime(),ars.getString("created_by"),
                            ars.getTimestamp("last_update").toLocalDateTime(),ars.getString("last_updated_by")));
                }
            }
        }
        catch (SQLException ignore) {}
        return appointments;
    }

    /**
     * Fetch all customers
     *
     * @return non null instance {@link ObservableList} of {@link Customer}
     */
    public ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT c1.*,c2.country,c2.country_id,d.division " +
                    "FROM customers AS c1 INNER JOIN first_level_divisions AS d ON c1.division_id = d.division_id " +
                    "INNER JOIN countries AS c2 ON d.country_id = c2.country_id;");
            if (null != rs) {
                while (rs.next()) {
                    customers.add(new Customer(rs.getInt("customer_id"), rs.getString("customer_name"),
                            rs.getString("address"),rs.getString("postal_code"),rs.getString("phone"),
                            new Division(rs.getInt("division_id"),rs.getString("division"),
                                    new Country(rs.getInt("country_id"),rs.getString("country"))),
                            rs.getTimestamp("create_date").toLocalDateTime(),rs.getString("created_by"),
                            rs.getTimestamp("last_update").toLocalDateTime(),rs.getString("last_updated_by")));
                }
            }
        }
        catch (SQLException ignore) {}
        return customers;
    }

    /**
     * Fetch all contacts
     *
     * @return non null instance {@link ObservableList} of {@link Contact}
     */
    public ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT contact_id, contact_name FROM contacts;");
            if (null != rs) {
                while (rs.next()) {
                    contacts.add(new Contact(rs.getInt("contact_id"),rs.getString("contact_name")));
                }
            }
        }
        catch (SQLException ignore) {}
        return contacts;
    }

    /**
     * Fetch all country
     *
     * @return non null instance {@link ObservableList} of {@link Country}
     */
    public ObservableList<Country> getAllCountry() {
        ObservableList<Country> countries = FXCollections.observableArrayList();
        try (Statement stmt = connection.createStatement()) {
            ResultSet crs = stmt.executeQuery("SELECT country_id, country FROM countries;");
            if (null != crs) {
                while (crs.next()) {
                    Country country = new Country(crs.getInt("country_id"),
                            crs.getString("country"));
                    try (Statement stmt2 = connection.createStatement()) {
                        ResultSet drs = stmt2.executeQuery("SELECT division_id, division FROM first_level_divisions " +
                                "WHERE country_id = "+country.getCountryId()+";");
                        if (null != drs) {
                            while (drs.next()) {
                                country.getDivisions().add(new Division(drs.getInt("division_id"),
                                        drs.getString("division"),
                                        country));
                            }
                        }
                    }
                    catch (SQLException ignore) {}
                    countries.add(country);
                }
            }
        }
        catch (SQLException ignore) {}
        return countries;
    }

    /**
     * Checks weather given appointment start and end date time overlaps with other
     * appoint of the user or the customer
     *
     * @return {@literal true} if overlaps, {@literal false} otherwise
     */
    /**
     * Checks weather given appointment start and end date time overlaps with other
     * appoint of the user or the customer
     *
     * @param appointmentId the id of the appointment to exclude from this checking,
     *                      use -1 to check every records
     * @param customerId customer id
     * @param startDateTime start date time in UTC
     * @param endDateTime end date time in UTC
     * @return {@literal true} if overlaps, {@literal false} otherwise
     */
    public boolean checkAppointmentOverlapping(final int appointmentId, final int customerId, final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(appointment_id) AS c " +
                "FROM appointments WHERE appointment_id != ? AND customer_id = ? AND ((start <= ? AND ? <= end) OR (start <= ? AND ? <= end));")) {
            ps.setInt(1,appointmentId);
            ps.setInt(2,customerId);
            ps.setTimestamp(3,Timestamp.valueOf(startDateTime));
            ps.setTimestamp(4,Timestamp.valueOf(startDateTime));
            ps.setTimestamp(5,Timestamp.valueOf(endDateTime));
            ps.setTimestamp(6,Timestamp.valueOf(endDateTime));
            ResultSet rs = ps.executeQuery();
            if (null != rs && rs.next()) {
                return 0 < rs.getInt(1);
            }
        }
        catch (SQLException ignore){}
        return false;
    }

    /**
     * Fetch all appointment types
     *
     * @return non null instance of {@link List} of appointment types
     */
    public List<String> getAllAppointmentTypes() {
        List<String> types = new ArrayList<>();
        try (Statement stm = connection.createStatement()) {
            ResultSet rs = stm.executeQuery("SELECT type FROM appointments GROUP BY type;");
            if (null != rs) {
                while (rs.next()) {
                    types.add(rs.getString(1));
                }
            }
        }
        catch (SQLException ignore) {}
        return types;
    }

    /**
     * Fetch all appointments of the user
     *
     * @param userId the user id to user
     * @return non null instance of {@link List} of {@link Appointment}
     */
    public List<Appointment> getAppointmentsWithUser(int userId) {
        List<Appointment> appointments = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT appointment_id,title,description,type,start,end,customer_id" +
                " FROM appointments WHERE user_id = ?;")){
            ps.setInt(1, userId);
            ResultSet ars = ps.executeQuery();
            if (null != ars) {
                while (ars.next()) {
                    appointments.add(new Appointment(ars.getInt("appointment_id"),
                            ars.getString("title"),
                            ars.getString("description"),
                            null,
                            ars.getString("type"),
                            ars.getTimestamp("start").toLocalDateTime(),
                            ars.getTimestamp("end").toLocalDateTime(),
                            ars.getInt("customer_id"),null,
                            userId,
                            0, null,
                            null,null,
                            null,null));
                }
            }
        }
        catch (SQLException ignore) {}
        return appointments;
    }

    /**
     * Fetch all appoints having the contact
     *
     * @param contactId the contact id of the contact
     * @return non null instance of {@link List} of {@link Appointment}
     */
    public List<Appointment> getAppointmentsOfContact(int contactId) {
        List<Appointment> appointments = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT appointment_id,title,description,type,start,end,customer_id" +
                " FROM appointments WHERE contact_id = ?;")){
            ps.setInt(1, contactId);
            ResultSet ars = ps.executeQuery();
            if (null != ars) {
                while (ars.next()) {
                    appointments.add(new Appointment(ars.getInt("appointment_id"),
                            ars.getString("title"),
                            ars.getString("description"),
                            null,
                            ars.getString("type"),
                            ars.getTimestamp("start").toLocalDateTime(),
                            ars.getTimestamp("end").toLocalDateTime(),
                            ars.getInt("customer_id"),null,
                            0,
                            contactId, null,
                            null,null,
                            null,null));
                }
            }
        }
        catch (SQLException ignore) {}
        return appointments;
    }

    /**
     * Counts the number of appointments of the type for the customer are available
     *
     * @param customerId the customer id of the customer
     * @param type the appoint type
     * @return the number of appointments
     */
    public int countAppointmentsByTypeForCustomer(int customerId, String type) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(appointment_id) AS c " +
                "FROM appointments WHERE customer_id = ? AND type = ?;")) {
            ps.setInt(1,customerId);
            ps.setString(2,type);
            ResultSet rs = ps.executeQuery();
            if (null != rs && rs.next()) {
                return rs.getInt(1);
            }
        }
        catch (SQLException ignore) {}
        return 0;
    }

    /**
     * Counts the number of appointments in the month for the customer are available
     *
     * @param customerId the customer id of the customer
     * @param month the month 1 = January, 2 = February and so on
     * @return the number of appointments
     */
    public int countAppointmentsOfMonthForCustomer(int customerId, int month) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(appointment_id) AS c " +
                "FROM appointments WHERE customer_id = ? AND MONTH(start) = ?;")) {
            ps.setInt(1,customerId);
            ps.setInt(2,month);
            ResultSet rs = ps.executeQuery();
            if (null != rs && rs.next()) {
                return rs.getInt(1);
            }
        }
        catch (SQLException ignore) {}
        return 0;
    }

    /**
     * Returns appointment, if any, for the user which are going to start within the given
     * UTC time
     *
     * @param maxTime upper bound of time in UTC
     * @param userId the user id of the user
     * @return non null instance of {@link Optional} of {@link Appointment}
     */
    public Optional<Appointment> getUpComingAppointment(LocalTime maxTime, int userId) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT appointment_id,start " +
                "FROM appointments WHERE user_id = ? AND MINUTE(TIMEDIFF(?,start)) <= 15;")) {
            ps.setInt(1,userId);
            ps.setTime(2,Time.valueOf(maxTime));
            ResultSet ars = ps.getResultSet();
            if (null != ars && ars.next()) {
                return Optional.of(new Appointment(ars.getInt("appointment_id"),
                        null,null, null, null,
                        ars.getTimestamp("start").toLocalDateTime(),null,
                        0,null, userId, 0,null,
                        null,null,null,null));
            }
        }
        catch (SQLException ignore) {}
        return Optional.empty();
    }

    /**
     * Close the current database connection and makes the singleton instance null.
     * Call this method only when application exits
     */
    public void close() {
        try {
            connection.close();
        } catch (Exception ignore) {}
        finally { instance = null; }
    }
}
