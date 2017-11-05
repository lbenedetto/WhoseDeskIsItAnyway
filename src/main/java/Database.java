import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;

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
		} catch (SQLException | NullPointerException | IllegalArgumentException e) {
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

	ArrayList<String> search(String VIN, boolean logging) {
		String SQL = "SELECT TABLE_NAME FROM FOLDERS WHERE VIN = ?";
		try {
			PreparedStatement ps = con.prepareStatement(SQL);
			ps.setString(1, VIN);
			ResultSet rs = ps.executeQuery();
			boolean found = false;
			StringBuilder sb = new StringBuilder(String.format("Look for %s in: ", VIN));
			ArrayList<String> locations = new ArrayList<>();
			while (rs.next()) {
				String loc = rs.getString(1);
				sb.append(loc).append(", ");
				locations.add(loc);
				found = true;
			}
			if (logging) Main.log(sb.toString());
			if (!found) {
				Main.log(String.format("%s not found in any location", VIN));
			}
			return locations;
		} catch (SQLException e) {
			Main.log(e.getMessage());
		}
		return null;
	}

	void crossReference() {
		String SQL = "SELECT VIN FROM FOLDERS GROUP BY VIN HAVING COUNT(TABLE_NAME) >= 2;";
		try {
			ResultSet rs = con.prepareStatement(SQL).executeQuery();
			HashMap<String, ArrayList<String>> resultsMap = new HashMap<>();
			while (rs.next()) {
				String vin = rs.getString(1);
				resultsMap.put(vin, search(vin, false));
			}
			ArrayList<Map.Entry<String, ArrayList<String>>> results = new ArrayList<>(resultsMap.entrySet());

			results.sort(Comparator.comparing(o -> {
				o.getValue().sort(String.CASE_INSENSITIVE_ORDER);
				return o.getValue().get(0);
			}));

			results.forEach(result -> {
				StringBuilder sb = new StringBuilder(String.format("Look for %s in: ", result.getKey()));
				result.getValue().forEach(loc -> sb.append(loc).append(", "));
				Main.log(sb.toString());
			});
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

	String[] list(boolean log) {
		String SQL = "SELECT DISTINCT TABLE_NAME FROM FOLDERS";
		try {
			ResultSet rs = con.prepareStatement(SQL).executeQuery();
			ArrayList<String> locations = new ArrayList<>();
			while (rs.next()) {
				if (log) Main.log(rs.getString(1));
				else locations.add(rs.getString(1));
			}
			return locations.toArray(new String[locations.size()]);
		} catch (SQLException e) {
			Main.log(e.getMessage());
		}
		return new String[0];
	}

	void export(String location) {
		String SQL = "SELECT VIN FROM FOLDERS WHERE TABLE_NAME = ?";
		try {
			PreparedStatement ps = con.prepareStatement(SQL);
			ps.setString(1, location);
			ResultSet rs = ps.executeQuery();
			FileWriter fw = new FileWriter(location + ".txt");
			fw.write(String.format("VINs in %s\r\n", location));
			while (rs.next()) {
				String stock = rs.getString(1);
				ArrayList<Vehicle> results = Main.lotus.get(stock);
				if (results != null) {
					for (Vehicle vehicle : results) {
						String result = vehicle.toString();
						Main.log(result);
						fw.write(result + "\r\n");
					}
				} else {
					fw.write("---------" + stock + "\r\n");
				}
			}
			fw.close();
		} catch (SQLException | IOException e) {
			Main.log(e.getMessage());
		}
	}

	void sql(String SQL) {
		if (SQL.toUpperCase().contains("DROP TABLE")) return;
		try {
			ResultSet rs = con.prepareStatement(SQL).executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int cols = md.getColumnCount();
			StringBuilder columns = new StringBuilder();
			for (int i = 1; i <= cols; i++)
				columns.append(md.getColumnName(i)).append("::");
			Main.log(columns.toString());
			rs.getMetaData().getColumnName(1);
			while (rs.next()) {
				columns = new StringBuilder();
				for (int i = 1; i <= cols; i++) {
					columns.append(rs.getString(i)).append("::");
				}
				Main.log(columns.toString());
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
