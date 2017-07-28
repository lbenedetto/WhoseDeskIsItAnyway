import java.sql.*;

class Database {
	private Connection con;

	Database(String path) {
		try {
			if (path == null || path.equals("")) throw new IllegalArgumentException("No path specified");
			String url = "jdbc:sqlite:" + path;
			con = DriverManager.getConnection(url);
			if (con == null) Main.log("DriverManager returned null connection");
			else Main.log("Connection to database has been established.");
			Statement statement = con.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS FOLDERS (TABLE_NAME VARCHAR(20) NOT NULL, VIN CHARACTER(8) NOT NULL PRIMARY KEY);");
		} catch (SQLException e) {
			Main.log(e.getMessage());
			close();
		} catch (NullPointerException e) {
			Main.log(e.getMessage());
			close();
		} catch (IllegalArgumentException e) {
			Main.log(e.getMessage());
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
			Main.log(String.format("Added %s to %s", VIN, desk));
		} catch (SQLException e) {
			Main.log(e.getMessage());
		}
	}

	void select(String VIN) {
		String SQL = "SELECT TABLE_NAME FROM FOLDERS WHERE VIN = ?";
		try {
			PreparedStatement ps = con.prepareStatement(SQL);
			ps.setString(1, VIN);
			ResultSet rs = ps.executeQuery();
			Main.log(String.format("Look in %s for %s", rs.getString("TABLE_NAME"), VIN));
		} catch (SQLException e) {
			Main.log(e.getMessage());
		}
	}

	void delete(String VIN) {
		String SQL = "DELETE FROM FOLDERS WHERE VIN = ?";
		try {
			PreparedStatement ps = con.prepareStatement(SQL);
			ps.setString(1, VIN);
			ps.executeUpdate();
			Main.log(String.format("Deleted %s from database", VIN));
		} catch (SQLException e) {
			Main.log(e.getMessage());
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
