package viewer;

import javax.swing.table.AbstractTableModel;

public class CustomTableModel extends AbstractTableModel {
    String tableName;
    String[] columns;
    Object[][] data;

    public CustomTableModel(String tableName, DB db) {
        this.tableName = tableName;
        this.columns = db.getColumns(tableName);
        this.data = db.getAllTableValues(tableName);
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }
}
