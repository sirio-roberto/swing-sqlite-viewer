package viewer;

import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DB {
    private final String databasePath;

    public DB(String databasePath) {
        this.databasePath = "jdbc:sqlite:" + databasePath.trim();
    }

    private Connection getConnection() throws SQLException {
        Connection conn = null;
            File file = new File(databasePath.replaceAll("jdbc:sqlite:", ""));
            if (!file.exists()) {
                throw new SQLException("File doesn't exist!");
            }
            SQLiteDataSource dataSource = new SQLiteDataSource();
            dataSource.setUrl(databasePath);
            conn = dataSource.getConnection();
        return conn;
    }

    public List<String> getTables() throws SQLException {
        String query = "SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';";
        List<String> result = new ArrayList<>();
        try (Statement st = getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                String tableName = rs.getString("name");
                if (tableName != null && !tableName.trim().isBlank()) {
                    result.add(tableName);
                }
            }
        }
        return result;
    }

    public String[] getColumns(String query) {
        String[] result = null;

        try (Statement st = getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            ResultSetMetaData rsMetaData = rs.getMetaData();
            result = new String[rsMetaData.getColumnCount()];
            for (int i = 0; i < result.length; i++) {
                result[i] = rsMetaData.getColumnName(i + 1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public Object[][] getAllTableValues(String query) {
        List<List<Object>> rowValues = new ArrayList<>();

        try (Statement st = getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int j = 0;
            while (rs.next()) {
                rowValues.add(new ArrayList<>());
                for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
                    rowValues.get(j).add(rs.getObject(rsMetaData.getColumnName(i + 1)));
                }
                j++;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        int numberOfColumns = rowValues.size() > 0 ? rowValues.get(0).size() : 0;
        Object[][] result = new Object[rowValues.size()][numberOfColumns];
        for (int i = 0; i < result.length; i++) {
            result[i] = rowValues.get(i).toArray();
        }
        return result;
    }
}
