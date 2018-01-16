package aufgabe6_7;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.CachedRowSet;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class HochschuleFrame {
	String password, user;
	Connection con;
	Statement stmt;
	ResultSet rset;
	HochschuleTable hochschultabelle;
	ResultSetMetaData rsetmd;
	String[] tabellenNamen;
	JComboBox tabellenauswahl;

	public HochschuleFrame() throws ClassNotFoundException, SQLException, IOException {
		JFrame fenster = new JFrame("Datenbank Tabellen");

		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		getUserLogin();
		connect();
		
	 tabellenauswahl = new JComboBox<String>(getMetaTablefirstColumn());
		tabellenauswahl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JComboBox<String> comboBox = (JComboBox) event.getSource();
				Object selected = comboBox.getSelectedItem();
				
					try {
						hochschultabelle = new HochschuleTable(selected, con);
					} catch (ClassNotFoundException | SQLException | IOException e) {
						System.out.println("Fehler beim Erstellen der Tabelle");
		
						e.printStackTrace();
					}
			

			}
		});

		panel.add(hochschultabelle.getTable(), BorderLayout.CENTER);
		fenster.getContentPane().add(panel);

		fenster.pack();
		fenster.setVisible(true);

	};

	public void getUserLogin() {
		user = JOptionPane.showInputDialog("User");
		password = JOptionPane.showInputDialog("Passwort");

	}

	public void connect() throws SQLException, ClassNotFoundException, IOException {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		con = DriverManager.getConnection("jdbc:oracle:thin:@ora14.informatik.haw-hamburg.de:1521:inf14", user,
				password);
	}

	public String[] getMetaTablefirstColumn() throws SQLException, ClassNotFoundException, IOException {
		stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rset = stmt.executeQuery("Select Table_Name from user_tables"); // hier
																		// richtiges
		int rows = 0;
		while(rset.next()){
			rows ++;
		}
		rset.beforeFirst();
		tabellenNamen = new String[rows];

		int i = 0;
		while (rset.next()) {
			tabellenNamen[i] = rset.getString(i + 1);
		}
		return tabellenNamen;
	}

	public void closeConnection() throws SQLException {
		con.close();
	}

}
