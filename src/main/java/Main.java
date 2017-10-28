import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Vector;

public class Main {
	private static Interface i;
	static Database database;
	private static HashMap<String, Vehicle> lotus = new HashMap<>();

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
				String vin = datum[0].substring(datum[0].length() - 8, datum[0].length());
				lotus.put(vin, new Vehicle(data));
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
		if (input.isEmpty()) {
			log("No inputs");
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
		Vehicle v = lotus.get(input);
		if (v != null)
			log(v.toString());
		switch (mode) {
			case ADD_FOLDER:
				if (verifyLocation(location)) return;
				database.add(input, location);
				break;
			case ADD_FILE:
				if (v != null) {
					String[] ed = v.getEntryDate().split("/");
					location = String.format("%s-%sNOFIND", ed[0], ed[2]);
				} else {
					location = "NOT-FOUND-LOTUS";
				}
				database.add(input, location);
				break;
			case DELETE:
				if (verifyLocation(location)) return;
				database.delete(input, location);
				break;
			case SEARCH:
				database.search(input);
				break;
		}
	}

	private static boolean verifyLocation(String location) {
		if (location.isEmpty()) {
			log("Error: Must specify table name to modify (no spaces)");
			return false;
		}
		return true;
	}

	static void log(String s) {
		System.out.println(s);
		i.setTextAreaOutput(i.getTextAreaOutput() + s + "\n");
	}
}
