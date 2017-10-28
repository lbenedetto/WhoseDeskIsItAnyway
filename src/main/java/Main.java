public class Main {
	private static Interface i;
	static Database database;

	public static void main(String[] args) {
		i = new Interface();
		i.setTitle("Whose Desk Is It Anyway");
		i.setSize(640, 360);
		database = new Database("U:\\Filing\\desks.db");
		i.setVisible(true);
	}

	static void process(String m, String location, String input) {
		//log(String.format("m:%s, l:%s, i:%s", m, location, input));
		input = input.toUpperCase();
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
		switch (mode) {
			case ADD:
				if (location.isEmpty()) {
					log("Error: Must specify table name to add to (no spaces)");
					return;
				}
				database.add(input, location);
				break;
			case DELETE:
				if (location.isEmpty()) {
					log("Error: Must specify table name to delete from (no spaces)");
					return;
				}
				database.delete(input, location);
				break;
			case SEARCH:
				database.search(input);
				break;
		}
	}

	static void log(String s) {
		System.out.println(s);
		i.setTextAreaOutput(i.getTextAreaOutput() + s + "\n");
	}
}
