import java.sql.*;

class Database {
	private Connection con;

	Database(String path) {
		try {
			// db parameters
			String url = "jdbc:sqlite:" + path;
			// create a connection to the database
			con = DriverManager.getConnection(url);

			Main.println("Connection to database has been established.");

			Statement statement = con.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS FOLDERS (TABLE_NAME VARCHAR(20) NOT NULL, VIN CHARACTER(8) NOT NULL PRIMARY KEY);");

		} catch (SQLException e) {
			Main.println(e.getMessage());
			close();
		}
	}

	void insert(String VIN, String desk) {
		String SQL = "INSERT INTO FOLDERS(VIN, TABLE_NAME) VALUES(?,?);";
		try {
			PreparedStatement ps = con.prepareStatement(SQL);
			ps.setString(1, VIN);
			ps.setString(2, desk);
			ps.executeUpdate();
			Main.println(String.format("Added %s to %s", VIN, desk));
		} catch (SQLException e) {
			Main.println(e.getMessage());
		}
	}

	void select(String VIN) {
		String SQL = "SELECT TABLE_NAME FROM FOLDERS WHERE VIN = ?";
		try {
			PreparedStatement ps = con.prepareStatement(SQL);
			ps.setString(1, VIN);
			ResultSet rs = ps.executeQuery();
			Main.println(String.format("Look in %s for %s", rs.getString("TABLE_NAME"), VIN));
		} catch (SQLException e) {
			Main.println(e.getMessage());
		}
	}

	void delete(String VIN) {
		String SQL = "DELETE FROM FOLDERS WHERE VIN = ?";
		try {
			PreparedStatement ps = con.prepareStatement(SQL);
			ps.setString(1, VIN);
			ps.executeUpdate();
			Main.println(String.format("Deleted %s from database", VIN));
		} catch (SQLException e) {
			Main.println(e.getMessage());
		}
	}

	void close() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
