package SpringProject.persistences;

import SpringProject.entities.Subscription;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SubscriptionDaoImpl implements SubscriptionDao {

    private final Connector connector;

    public SubscriptionDaoImpl(Connector connector) {
        this.connector = connector;
    }

    @Override
    public int addSubscription(Subscription subscription) {

        int rowsAffected = 0;
        Connection conn = connector.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO subscription(username, sub_startDate, sub_endDate) VALUES (?, ?, ?)"
        )) {

            ps.setString(1, subscription.getUsername());
            ps.setTimestamp(2, Timestamp.valueOf(subscription.getSub_startDate()));
            ps.setTimestamp(3, Timestamp.valueOf(subscription.getSub_endDate()));

            rowsAffected = ps.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Constraint Exception occurred: " + e.getMessage());
            rowsAffected = -1;

        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare/execute SQL");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();

        } finally {
            connector.freeConnection(conn);
        }

        return rowsAffected;
    }

    @Override
    public Subscription getSubscriptionFromUsername(String username) {

        Subscription subUser = null;
        Connection conn = connector.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM subscription WHERE username = ?"
        )) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    subUser = mapRow(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare/execute SQL");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();

        } finally {
            connector.freeConnection(conn);
        }

        return subUser;
    }

    @Override
    public ArrayList<Subscription> getAllSubscriptions() {

        ArrayList<Subscription> subscriptions = new ArrayList<>();
        Connection conn = connector.getConnection();

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM subscription");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Subscription s = mapRow(rs);
                subscriptions.add(s);
            }

        } catch (SQLException e) {
            System.out.println(LocalDateTime.now() + ": An SQLException occurred while preparing/executing SQL.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();

        } finally {
            connector.freeConnection(conn);
        }

        return subscriptions;
    }

    @Override
    public boolean updateUserSubscriptionEndDate(LocalDateTime endDate, String username) throws RuntimeException {

        int rowsAffected = 0;
        Connection conn = connector.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE subscription SET sub_endDate = ? WHERE username = ?"
        )) {

            ps.setTimestamp(1, Timestamp.valueOf(endDate));
            ps.setString(2, username);

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(LocalDateTime.now() + ": An SQLException occurred while preparing/executing SQL.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();

        } finally {
            connector.freeConnection(conn);
        }

        if (rowsAffected > 1) {
            throw new RuntimeException(LocalDateTime.now()
                    + " ERROR: Multiple rows affected on primary key selection.");
        } else return rowsAffected == 1;
    }

    private Subscription mapRow(ResultSet rs) throws SQLException {
        return new Subscription(
                rs.getString("username"),
                rs.getTimestamp("sub_startDate").toLocalDateTime(),
                rs.getTimestamp("sub_endDate").toLocalDateTime()
        );
    }
}
