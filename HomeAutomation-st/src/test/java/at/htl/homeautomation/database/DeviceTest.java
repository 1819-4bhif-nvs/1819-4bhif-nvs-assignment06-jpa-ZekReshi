package at.htl.homeautomation.database;

import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DeviceTest {

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
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM HA_DEVICE");
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsMeta = rs.getMetaData();
            assertThat(rsMeta.getColumnCount(), is(5));
            assertThat(rsMeta.getColumnName(1), is("DTYPE"));
            assertThat(rsMeta.getColumnTypeName(1), is("VARCHAR"));
            assertThat(rsMeta.getColumnName(2), is("ID"));
            assertThat(rsMeta.getColumnTypeName(2), is("BIGINT"));
            assertThat(rsMeta.getColumnName(3), is("NAME"));
            assertThat(rsMeta.getColumnTypeName(3), is("VARCHAR"));
            assertThat(rsMeta.getColumnName(4), is("VERSION"));
            assertThat(rsMeta.getColumnTypeName(4), is("INTEGER"));
            assertThat(rsMeta.getColumnName(5), is("LOCATION_ID"));
            assertThat(rsMeta.getColumnTypeName(5), is("BIGINT"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
