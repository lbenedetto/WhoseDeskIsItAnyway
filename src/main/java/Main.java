public class Main {
	private static Interface i;
	private static EMode mode = EMode.search;
	private static String desk = "Shouldn't be possible";
	static Database database;

	public static void main(String[] args) {
		i = new Interface();
		i.setTitle("Whose Desk Is It Anyway");
		i.setSize(640, 360);
		i.setVisible(true);
		database = new Database();
	}

	static void process(String input) {
		String[] inputs = input.split(" ");
		try {
			mode = EMode.valueOf(inputs[0]);
			if (mode == EMode.add) {
				if (inputs.length == 2)
					desk = inputs[1];
				else {
					println("No spaces is location name");
					return;
				}
			}
			if (mode == EMode.exit) {
				database.close();
				System.exit(0);
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
