public class Main {
	private static Interface i;
	private static EMode mode = EMode.SEARCH;
	private static String desk = "Shouldn't be possible";
	static Database database;

	public static void main(String[] args) {
		i = new Interface();
		i.setTitle("Whose Desk Is It Anyway");
		i.setSize(640, 360);
		database = new Database("U:\\Filing\\desks.db");
		i.setVisible(true);
	}

	static void process(String input) {
		input = input.toUpperCase();
		String[] inputs = input.split(" ");
		if (inputs.length == 0) {
			log("No inputs");
			return;
		}
		try {
			mode = EMode.valueOf(inputs[0]);
			switch (mode) {
				case ADD:
					if (inputs.length == 2)
						desk = inputs[1];
					else {
						log("Error: Must specify table name to add to (no spaces)");
						return;
					}
					log("=== ADD MODE ===");
					break;
				case DELETE:
					if (inputs.length == 2)
						desk = inputs[1];
					else {
						log("Error: Must specify table name to delete from (no spaces)");
						return;
					}
					log("=== DELETE MODE ===");
					break;
				case EXIT:
					database.close();
					System.exit(0);
					break;
				case SEARCH:
					log("=== SEARCH MODE ===");
					break;
				case X:
					database.crossReference();
					break;
				case LIST:
					Main.log("=== List of Tables ===");
					database.list();
					Main.log("=== End List ===");
					break;
			}
		} catch (IllegalArgumentException e) {
			String vin = inputs[0];
			//If its not a mode switch command
			if (vin.length() < 8) {
				Main.log("Must enter last 8 of VIN");
				return;
			} else if (inputs[0].length() > 8) {
				vin = vin.substring(vin.length() - 8, vin.length());
			}
			switch (mode) {
				case ADD:
					database.add(vin, desk);
					break;
				case DELETE:
					database.delete(vin, desk);
					break;
				case SEARCH:
					database.search(vin);
					break;
				default:
					log("Please retype command");
			}
		}
	}

	static void log(String s) {
		System.out.println(s);
		i.setTextAreaOutput(i.getTextAreaOutput() + s + "\n");
	}
}
