import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LeadDAO {

    public static void addLead(Lead lead) throws Exception {
        Connection connection = DatabaseConnection.getConnection();
        String sql = "INSERT INTO leads (name, email, phone, company) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, lead.name);
        statement.setString(2, lead.email);
        statement.setString(3, lead.phone);
        statement.setString(4, lead.company);
        statement.executeUpdate();

    }

    public static String viewLeads() throws Exception {

    Connection connection = DatabaseConnection.getConnection();

    String sql = "SELECT * FROM leads";

    PreparedStatement statement = connection.prepareStatement(sql);

    ResultSet resultSet = statement.executeQuery();

    String leads = "[";

    while (resultSet.next()) {

        leads += "{"
                + "\"id\":" + resultSet.getInt("id") + ","
                + "\"name\":\"" + resultSet.getString("name") + "\","
                + "\"email\":\"" + resultSet.getString("email") + "\","
                + "\"phone\":\"" + resultSet.getString("phone") + "\","
                + "\"company\":\"" + resultSet.getString("company") + "\""
                + "},";
    }

    if (leads.endsWith(",")) {
        leads = leads.substring(0, leads.length() - 1);
    }

    leads += "]";

    return leads;
}
    public static void updateLead(
    int id,
    String newName,
    String newEmail,
    String newPhone,
    String newCompany
) throws Exception {

    Connection connection = DatabaseConnection.getConnection();

    String sql = "UPDATE leads SET name = ?, email = ?, phone = ?, company = ? WHERE id = ?";

    PreparedStatement statement = connection.prepareStatement(sql);

    statement.setString(1, newName);
    statement.setString(2, newEmail);
    statement.setString(3, newPhone);
    statement.setString(4, newCompany);
    statement.setInt(5, id);

    statement.executeUpdate();
    statement.close();
    connection.close();
}

    public static void deleteLead(int id) throws Exception {
        Connection connection = DatabaseConnection.getConnection();
        String sql = "DELETE FROM leads WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

}