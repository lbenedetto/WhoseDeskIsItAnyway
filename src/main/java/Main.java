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
		database = new Database("U:\\Filing\\desks.db");
		i.setVisible(true);
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
		//log(String.format("m:%s, l:%s, i:%s", m, location, input));
		input = input.toUpperCase();
		location = location.toUpperCase();
		EMode mode = EMode.valueOf(m);
		if (input.isEmpty() && mode != EMode.EXPORT) {
			log("No inputs");
			return;
		}
		if (mode == EMode.SQL) {
			database.sql(input);
			return;
		}
		if (input.equals("X")) {
			database.crossReference();
			return;
		} else if (input.equals("LIST")) {
			Main.log("=== List of Tables ===");
			database.list();
			Main.log("=== End List ===");
			return;
		}
		//Its not a command, interpret as VIN
		if (input.length() < 8) {
			Main.log("Must enter last 8 of VIN");
			return;
		} else if (input.length() > 8) {
			input = input.substring(input.length() - 8, input.length());
		}
		if (mode != EMode.SEARCH) {
			ArrayList<Vehicle> va = lotus.get(input);
			if (va != null) {
				va.forEach(v -> log(v.toString()));
				if (location.isEmpty()) {
					if (va.size() > 1) {
						log("Please specify location to add to");
						return;
					} else if (va.size() == 1) {
						location = getLocation(va.get(0));
					}
				}
			} else {
				log("Not found in LOTUS");
				if (location.isEmpty()) {
					log("Please specify location");
					return;
				}

			}
		}
		switch (mode) {
			case ADD:
				database.add(input, location);
				break;
			case DELETE:
				database.delete(input, location);
				break;
			case SEARCH:
				database.search(input, true);
				break;
			case EXPORT:
				database.export(location);
				break;
		}
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
		System.out.println(s);
		i.setTextAreaOutput(i.getTextAreaOutput() + s + "\n");
	}
}
