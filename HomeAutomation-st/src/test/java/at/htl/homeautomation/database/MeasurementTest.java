package at.htl.homeautomation.database;

import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MeasurementTest {

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
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM HA_MEASUREMENT");
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsMeta = rs.getMetaData();
            assertThat(rsMeta.getColumnCount(), is(6));
            assertThat(rsMeta.getColumnName(1), is("ID"));
            assertThat(rsMeta.getColumnTypeName(1), is("BIGINT"));
            assertThat(rsMeta.getColumnName(2), is("TIME"));
            assertThat(rsMeta.getColumnTypeName(2), is("TIMESTAMP"));
            assertThat(rsMeta.getColumnName(3), is("UNIT"));
            assertThat(rsMeta.getColumnTypeName(3), is("VARCHAR"));
            assertThat(rsMeta.getColumnName(4), is("VALUE"));
            assertThat(rsMeta.getColumnTypeName(4), is("DOUBLE"));
            assertThat(rsMeta.getColumnName(5), is("VERSION"));
            assertThat(rsMeta.getColumnTypeName(5), is("INTEGER"));
            assertThat(rsMeta.getColumnName(6), is("SENSOR_ID"));
            assertThat(rsMeta.getColumnTypeName(6), is("BIGINT"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDml() {
        int countInserts = 0;

        try {
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO HA_MEASUREMENT (TIME, UNIT, VALUE, VERSION, SENSOR_ID)" +
                    "VALUES (CURRENT_TIMESTAMP, '%', 23.4, 0, 2)";
            countInserts += stmt.executeUpdate(sql);
            sql = "INSERT INTO HA_MEASUREMENT (TIME, UNIT, VALUE, VERSION, SENSOR_ID)" +
                    "VALUES (CURRENT_TIMESTAMP, 'ca', 26, 0, 3)";
            countInserts += stmt.executeUpdate(sql);
            sql = "INSERT INTO HA_MEASUREMENT (TIME, UNIT, VALUE, VERSION, SENSOR_ID)" +
                    "VALUES (CURRENT_TIMESTAMP, 'degC', 22.0, 0, 1)";
            countInserts += stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertThat(countInserts, is(3));

        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT UNIT, VALUE, NAME FROM HA_MEASUREMENT measurement" +
                    " JOIN HA_SENSOR sensor ON(SENSOR_ID = sensor.ID)" +
                    " JOIN HA_DEVICE device ON(sensor.ID = device.ID)" +
                    " ORDER BY measurement.ID DESC");
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            assertThat(rs.getString("UNIT"), is("degC"));
            assertThat(rs.getDouble("VALUE"), is(22.0));
            assertThat(rs.getString("NAME"), is("TEMP1"));
            rs.next();
            assertThat(rs.getString("UNIT"), is("ca"));
            assertThat(rs.getDouble("VALUE"), is(26.0));
            assertThat(rs.getString("NAME"), is("HUMID1"));
            rs.next();
            assertThat(rs.getString("UNIT"), is("%"));
            assertThat(rs.getDouble("VALUE"), is(23.4));
            assertThat(rs.getString("NAME"), is("LIGHT1"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
