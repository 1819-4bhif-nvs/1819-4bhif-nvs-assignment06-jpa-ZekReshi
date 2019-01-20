package at.htl.homeautomation.database;

import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LocationTest {

    public static final String DRIVER_STRING = "org.apache.derby.jdbc.ClientDriver";
    static final String CONNECTION_STRING = "jdbc:derby://localhost:1527/db;create=true";
    static final String USER = "app";
    static final String PASSWORD = "app";
    private static Connection conn;

    @BeforeClass
    public static void initJdbc() {
        try{
            Class.forName(DRIVER_STRING);
            conn = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Verbindung zur Datenbank nicht möglich:\n"
                    + e.getMessage() + "\n");
            System.exit(1);
        }

    }

    @Test
    public void testDdl() {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM HA_LOCATION");
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsMeta = rs.getMetaData();
            assertThat(rsMeta.getColumnCount(), is(3));
            assertThat(rsMeta.getColumnName(1), is("ID"));
            assertThat(rsMeta.getColumnTypeName(1), is("BIGINT"));
            assertThat(rsMeta.getColumnName(2), is("NAME"));
            assertThat(rsMeta.getColumnTypeName(2), is("VARCHAR"));
            assertThat(rsMeta.getColumnName(3), is("VERSION"));
            assertThat(rsMeta.getColumnTypeName(3), is("INTEGER"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDml() {
        int countInserts = 0;

        try {
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO HA_LOCATION (NAME, VERSION)" +
                    "VALUES ('Lagerraum', 0)";
            countInserts += stmt.executeUpdate(sql);
            sql = "INSERT INTO HA_LOCATION (NAME, VERSION)" +
                    "VALUES ('Terrasse', 0)";
            countInserts += stmt.executeUpdate(sql);
            sql = "INSERT INTO HA_LOCATION (NAME, VERSION)" +
                    "VALUES ('Kueche', 0)";
            countInserts += stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertThat(countInserts, is(3));

        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT NAME FROM HA_LOCATION" +
                    " ORDER BY ID DESC");
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            assertThat(rs.getString("NAME"), is("Kueche"));
            rs.next();
            assertThat(rs.getString("NAME"), is("Terrasse"));
            rs.next();
            assertThat(rs.getString("NAME"), is("Lagerraum"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
