package aufgabe6_7;

import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class HochschuleTable implements ActionListener {

	ResultSetMetaData rsetmd;
	int numcols, numrows;
	String password, user;
	Connection con;
	Statement stmt;
	ResultSet rset;
	JComboBox box1;
	

	public Object[][] initTableData() throws SQLException {
		numcols = rsetmd.getColumnCount();
		setNumberRows();
		Object[][] tabledata = new Object[numrows][rsetmd.getColumnCount()+1];
		
		for (int i = 0;i < numrows; i++ ) {
			for ( int k = 1; k < numcols+1 ; k++) {
				 tabledata[i][k] = rset.getObject(k+1);
			}
		}
		return tabledata;
	}
	
	
	public void initCombobox() throws SQLException{
	 Statement statementCombo = con.createStatement();
	 ResultSet comboSet = statementCombo.executeQuery("Select * from user_tables"); 
	 ResultSetMetaData comboSetmd = comboSet.getMetaData();
	 String[] tables = new String[comboSetmd.getColumnCount()];
	 for (int i = 0; i < comboSetmd.getColumnCount(); i++){
	   tables[i] = comboSetmd.getColumnTypeName(i);
	 }
	 box1 = new JComboBox(tables);
	 box1.addActionListener(this);
	}
	
	public String[] initTableColumnNames() throws SQLException {
		String[] columnNames = new String[rsetmd.getColumnCount()+1];
		columnNames[0] = ("Tabelle");
		for (int i = 1; i < columnNames.length; i++) {
			columnNames [i]= rsetmd.getColumnName(i+1);
		}
		return columnNames;
	}
	


	public void getUserLogin() {
		user = JOptionPane.showInputDialog("User");
		password = JOptionPane.showInputDialog("Passwort");

	}

	public void connect() throws SQLException, ClassNotFoundException, IOException {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		con = DriverManager.getConnection("jdbc:oracle:thin:@ora14.informatik.haw-hamburg.de:1521:inf14", user,
				password);
	}



	private void setNumberRows(){
	    try{
	       
	       if(rset.last()){
	          numrows= rset.getRow();
	          rset.beforeFirst();
	       } else {
	    	   numrows= 0;
	       }
	    } catch (Exception e){
	       System.out.println("Error getting row count");
	       e.printStackTrace();
	    }
	}
	
	  @Override
	  public void actionPerformed(ActionEvent e) {
	     if (e.getSource() == box1){
	      try {
          selectAllDatafromTable();
        } catch (ClassNotFoundException | SQLException | IOException e1) {
          e1.printStackTrace();
        }
	     }
	    
	  }
	  
	   public void initAllDatafromTable() throws SQLException, ClassNotFoundException, IOException {
	        stmt = con.createStatement();
	        rset = stmt.executeQuery("Select * from Student" ); // hier richtiges Statement einfügen.
	        rsetmd = rset.getMetaData();
	    }
	    
	  
	  public void selectAllDatafromTable() throws SQLException, ClassNotFoundException, IOException {
        stmt = con.createStatement();
        rset = stmt.executeQuery("Select * from "+ box1.getSelectedItem().toString() ); // hier richtiges Statement einfügen.
        rsetmd = rset.getMetaData();
    }
	
	  
	public void closeConnection() throws SQLException{
	  con.close();
	}
	
	public JComboBox getBox1() {
      return box1;
    }


  public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException {
		HochschuleTable hochschulTabelle = new HochschuleTable();
		hochschulTabelle.getUserLogin();
		hochschulTabelle.connect();
		hochschulTabelle.initAllDatafromTable(); // CHECKBOX SCHREIBEN MIT ARRAY FÜLLEN ÜBER SELECT *FROM USER_TABLES UND DAS AUSGEWÄHLTE VON DER CHECKBOX IN DIE QUERY 
		JTable table = new JTable(hochschulTabelle.initTableData(),hochschulTabelle.initTableColumnNames());
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		TableColumn comboBoxColumn = table.getColumnModel().getColumn(0);
		hochschulTabelle.initCombobox();
		comboBoxColumn.setCellEditor(new DefaultCellEditor(hochschulTabelle.getBox1()));
		hochschulTabelle.closeConnection();
	}




}
