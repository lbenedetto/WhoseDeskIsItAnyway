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
			statement.execute("CREATE TABLE IF NOT EXISTS FOLDERS (TABLE_NAME VARCHAR(20) NOT NULL, VIN CHARACTER(8) NOT NULL);");
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

	void add(String VIN, String desk) {
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

	void search(String VIN) {
		String SQL = "SELECT TABLE_NAME FROM FOLDERS WHERE VIN = ?";
		try {
			PreparedStatement ps = con.prepareStatement(SQL);
			ps.setString(1, VIN);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsMeta = rs.getMetaData();
			while (rs.next()) {
				for (int i = 1; i <= rsMeta.getColumnCount(); i++) {
					Main.log(String.format("Look in %s for %s", rs.getString(i), VIN));
				}
			}
		} catch (SQLException e) {
			Main.log(e.getMessage());
		}
	}

	void delete(String VIN, String desk) {
		String SQL = "DELETE FROM FOLDERS WHERE VIN = ? AND TABLE_NAME = ?";
		try {
			PreparedStatement ps = con.prepareStatement(SQL);
			ps.setString(1, VIN);
			ps.setString(2, desk);
			ps.executeUpdate();
			Main.log(String.format("Deleted %s from %s", VIN, desk));
		} catch (SQLException e) {
			Main.log(e.getMessage());
		}
	}

	void list() {
		String SQL = "SELECT DISTINCT TABLE_NAME FROM FOLDERS";
		try {
			ResultSet rs = con.prepareStatement(SQL).executeQuery();
			rs.getString("TABLE_NAME");
			ResultSetMetaData rsMeta = rs.getMetaData();
			while (rs.next()) {
				for (int i = 1; i <= rsMeta.getColumnCount(); i++) {
					Main.log(rs.getString(i));
				}
			}
		} catch (SQLException e) {
			Main.log(e.getMessage());
		}
	}

	void crossReference() {
		String SQL = "SELECT VIN FROM FOLDERS GROUP BY VIN HAVING COUNT(TABLE_NAME) >= 2;";
		try {
			ResultSet rs = con.prepareStatement(SQL).executeQuery();
			rs.getString("VIN");
			ResultSetMetaData rsMeta = rs.getMetaData();
			while (rs.next()) {
				for (int i = 1; i <= rsMeta.getColumnCount(); i++) {
					search(rs.getString(i));
				}
			}
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
