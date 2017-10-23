public class Main {
	private static Interface i;
	private static EMode mode = EMode.search;
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
		String[] inputs = input.split(" ");
		if (inputs.length == 0) {
			log("No inputs");
			return;
		}
		try {
			mode = EMode.valueOf(inputs[0]);
			switch (mode) {
				case add:
					if (inputs.length == 2)
						desk = inputs[1];
					else {
						log("Error: Must specify table name to add to (no spaces)");
						return;
					}
					log("=== ADD MODE ===");
					break;
				case exit:
					database.close();
					System.exit(0);
					break;
				case search:
					log("=== SEARCH MODE ===");
					break;
				case delete:
					log("=== DELETE MODE ===");
				case x:
					database.crossReference();
					break;
				case list:
					Main.log("=== List of Tables ===");
					database.list();
					Main.log("=== End List ===");
					break;
			}
		} catch (IllegalArgumentException e) {
			//If its not a mode switch command
			switch (mode) {
				case add:
					database.add(inputs[0], desk);
					break;
				case delete:
					database.delete(inputs[0]);
					break;
				case search:
					database.search(inputs[0]);
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
