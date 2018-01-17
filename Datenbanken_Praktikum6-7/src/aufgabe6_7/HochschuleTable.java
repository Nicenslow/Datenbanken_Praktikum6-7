package aufgabe6_7;

import java.sql.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class HochschuleTable {

	JTable tabelle;
	String[][] daten;
	ResultSet rset;
	ResultSetMetaData rsetmd;
	Connection con;
	String query;
	String[] columnNames;
	int numcols, numrows;

	public HochschuleTable(Object selectedfromBox, Connection con)
			throws ClassNotFoundException, SQLException, IOException {
		query = String.valueOf(selectedfromBox);
		this.con = con;
		selectAllDatafromTable();
		columnNames = initTableColumnNames();
		initTableData();
		 tabelle = new JTable(daten,columnNames);
		
	}

	public void initTableData() throws SQLException {
		numcols = rsetmd.getColumnCount();
		setNumberRows();
		daten = new String[numrows][numcols];
		rset.beforeFirst();
		for (int i = 0; i < numrows; i++) {
			for (int k = 0; k < numcols ; k++) {
				rset.next();
				daten[i][k] = rset.getString(1);
			}

		}
		rset.beforeFirst();
	}

	public JTable getTable() {
		return tabelle;
	}

	public String[] initTableColumnNames() throws SQLException {
		String[] columnNames = new String[rsetmd.getColumnCount()];
		for (int i = 0; i < columnNames.length; i++) {
			columnNames[i] = rsetmd.getColumnName(i + 1);
		}
		return columnNames;
	}

	private void setNumberRows() throws SQLException {
		try {

			if (rset.last()) {
				numrows = rset.getRow();
				rset.beforeFirst();
			} else {
				numrows = 0;
			}
		} catch (Exception e) {
			System.out.println("Error getting row count");
			e.printStackTrace();
		}
		rset.beforeFirst();
	}

	public void selectAllDatafromTable() throws SQLException, ClassNotFoundException, IOException {
		Statement stmt;
		stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rset = stmt.executeQuery("Select * from " + query);
		rsetmd = rset.getMetaData();
	}

	public void closeConnection() throws SQLException {
		con.close();
	}

}
