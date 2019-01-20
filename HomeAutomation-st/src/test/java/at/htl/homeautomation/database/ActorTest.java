package at.htl.homeautomation.database;

import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ActorTest {

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
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM HA_ACTOR");
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsMeta = rs.getMetaData();
            assertThat(rsMeta.getColumnCount(), is(1));
            assertThat(rsMeta.getColumnName(1), is("ID"));
            assertThat(rsMeta.getColumnTypeName(1), is("BIGINT"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
