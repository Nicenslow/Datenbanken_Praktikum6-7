package aufgabe6_7;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class Hochschule implements TableModel {
	
	CachedRowSet hochschuleRowSet;
	ResultSetMetaData metadata;
	int numcols,numrows;
	
	public Hochschule (CachedRowSet rowSetArg) throws SQLException {
		
		this.hochschuleRowSet = rowSetArg;
		this.metadata = this.hochschuleRowSet.getMetaData();
		numcols = metadata.getColumnCount();
		
		this.hochschuleRowSet.beforeFirst();
		this.numrows = 0;
		while (this.hochschuleRowSet.next()) {
			this.numrows ++;
		}
		this.hochschuleRowSet.beforeFirst();
		
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return numcols;
	}

	@Override
	public String getColumnName(int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRowCount() {
		return numrows;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
	    try {
	        this.hochschuleRowSet.absolute(rowIndex + 1);
	        Object o = this.hochschuleRowSet.getObject(columnIndex + 1);
	        if (o == null)
	            return null;
	        else
	            return o.toString();
	    } catch (SQLException e) {
	        return e.toString();
	    }
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {

		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		
	}

}
