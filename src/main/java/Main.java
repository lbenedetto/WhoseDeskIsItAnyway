import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
	private static Interface i;
	static Database database;
	static HashMap<String, ArrayList<Vehicle>> lotus = new HashMap<>();

	public static void main(String[] args) {
		i = new Interface();
		i.setTitle("Whose Desk Is It Anyway");
		i.setSize(640, 360);
		i.setVisible(true);
		database = new Database("U:\\Filing\\desks.db");
		loadLotus();
	}

	private static void loadLotus() {
		try {
			Files.lines(Paths.get("U:\\Filing\\Easy Lookup Database.txt")).forEach(data -> {
				String[] datum = data.split(",");
				String vin = datum[0].trim();
				String stock;
				if (vin.length() >= 8)
					stock = vin.substring(vin.length() - 8, vin.length());
				else
					stock = vin;
				Vehicle vehicle = new Vehicle(data);
				lotus.putIfAbsent(stock, new ArrayList<>());
				lotus.get(stock).add(vehicle);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void process(String m, String location, String input) {
		input = input.toUpperCase();
		location = location.toUpperCase();
		EMode mode = EMode.valueOf(m);
		try {
			if (!input.isEmpty()) {
				if (mode == EMode.SQL) {
					database.sql(input);
					return;
				}
				input = verifyVINLength(input);
				if (mode == EMode.SEARCH) {
					database.search(input, true);
					return;
				}
				location = processLocation(location, input);
				switch (mode) {
					case ADD:
						database.add(input, location);
						return;
					case DELETE:
						database.delete(input, location);
						return;
				}
			}
			log("No input");
		} catch (Exception e) {
			//Ignore
		}
	}

	private static String processLocation(String location, String input) throws Exception {
		ArrayList<Vehicle> va = lotus.get(input);
		if (va != null) {
			va.forEach(v -> log(v.toString()));
			if (location.isEmpty()) {
				if (va.size() > 1) {
					log("Please specify location to add to");
					throw new Exception();
				} else if (va.size() == 1) {
					return getLocation(va.get(0));
				}
			}
		} else {
			log("Not found in LOTUS");
			if (location.isEmpty()) {
				log("Please specify location");
				throw new Exception();
			}

		}
		return location;
	}

	static String verifyVINLength(String input) throws Exception {
		if (input.length() < 8) {
			log("Must enter last 8 of VIN");
			throw new Exception();
		} else if (input.length() > 8) {
			return input.substring(input.length() - 8, input.length());
		}
		return input;
	}

	private static String getLocation(Vehicle v) {
		if (v != null) {
			String[] ed = v.getEntryDate().split("/");
			return String.format("%s-%sNOFIND", ed[0], ed[2]);
		} else {
			return "NOT-FOUND-LOTUS";
		}
	}

	static void log(String s) {
		i.log(s);
	}

	public enum EMode {
		ADD, DELETE, SEARCH, SQL
	}

}
