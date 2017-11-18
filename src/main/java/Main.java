import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
	private static Interface i;
	static Database database;
	static HashMap<String, ArrayList<Vehicle>> lotus = new HashMap<>();
	private static String lastVIN = "NOVIN";

	public static void main(String[] args) {
		try {
			i = new Interface();
			i.setTitle("Whose Desk Is It Anyway");
			i.setSize(640, 720);
			i.setTextAreaColor();
			i.setVisible(true);
			database = new Database("U:\\Filing\\desks.db");
			loadLotus();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	static void process(EMode mode, String location, String input) {
		input = input.toUpperCase();
		location = location.toUpperCase();
		try {
			if (!input.isEmpty()) {
				if (mode == EMode.SQL) {
					database.sql(input);
					return;
				}
				if (input.equals("*")) input = lastVIN;
				if (input.startsWith("OVERRIDE")) {
					input = input.split(" ")[1];
				} else {
					input = verifyVINLength(input);
				}
				lastVIN = input;
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
			log("No input", true);
		} catch (Exception e) {
			//Ignore
		}
	}

	private static String processLocation(String location, String input) throws Exception {
		ArrayList<Vehicle> va = lotus.get(input);
		if (va != null) {
			va.forEach(v -> log(v.toString(), true));
			if (location.isEmpty()) {
				if (va.size() > 1) {
					log("Please specify location to add to", true);
					throw new Exception();
				} else if (va.size() == 1) {
					return getLocation(va.get(0));
				}
			}
		} else {
			log("Not found in LOTUS", true);
			if (location.isEmpty()) {
				log("Please specify location", true);
				throw new Exception();
			}

		}
		return location;
	}

	static String verifyVINLength(String input) throws Exception {
		if (input.length() < 8) {
			log("Must enter last 8 of VIN: " + input, true);
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

	static void log(String s, boolean isWhite) {
		i.log(s, isWhite);
	}

	public enum EMode {
		ADD, DELETE, SEARCH, SQL
	}
}
