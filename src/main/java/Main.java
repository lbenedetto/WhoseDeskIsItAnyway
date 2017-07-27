public class Main {
	private static Interface i;
	private static EMode mode = EMode.search;
	private static String desk = "Shouldn't be possible";
	static Database database;

	public static void main(String[] args) {
		i = new Interface();
		i.setTitle("Whose Desk Is It Anyway");
		i.setSize(640, 360);
		database = new Database(args[0]);
		i.setVisible(true);
	}

	static void process(String input) {
		String[] inputs = input.split(" ");
		try {
			mode = EMode.valueOf(inputs[0]);
			switch (mode) {
				case add:
					if (inputs.length == 2)
						desk = inputs[1];
					else {
						println("No spaces in location name");
						return;
					}
					println("Switched to add mode");
					break;
				case exit:
					database.close();
					System.exit(0);
					break;
				case search:
					println("Switched to search mode");
					break;
				case delete:
					println("Switched to delete mode");
			}
		} catch (IllegalArgumentException e) {
			//If its not a mode switch command
			switch (mode) {
				case add:
					database.insert(inputs[0], desk);
					break;
				case delete:
					database.delete(inputs[0]);
					break;
				case search:
					database.select(inputs[0]);
					break;
			}
		}
	}

	static void println(String s) {
		i.setTextAreaOutput(i.getTextAreaOutput() + s + "\n");
	}
}
