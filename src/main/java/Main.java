import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Database database = new Database();
		Scanner kb = new Scanner(System.in);
		String input;
		EMode mode = EMode.search;
		String desk = "Shouldn't be possible";
		while (true) {
			input = kb.nextLine();
			String[] inputs = input.split(" ");
			try {
				mode = EMode.valueOf(inputs[0]);
				if (mode == EMode.add) {
					if (inputs.length == 2)
						desk = inputs[1];
					else {
						System.out.println("No spaces is location name");
						continue;
					}
				}
				if (mode == EMode.exit) database.close();
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
	}
}
