import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Interface extends JFrame {
	private JPanel contentPane;
	private JButton buttonDone;
	private JTextArea textAreaOutput;
	private JTextField textFieldInput;
	private JComboBox<Main.EMode> comboBoxMode;
	private JTextField textFieldLocation;
	private JButton btnCrossReference;
	private JButton btnList;
	private JButton btnExport;
	private JButton btnImport;
	private JButton btnClear;

	Interface() {
		setContentPane(contentPane);
		getRootPane().setDefaultButton(buttonDone);
		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onDone();
			}
		});
		buttonDone.addActionListener(e -> onDone());
		textFieldInput.addActionListener(e -> process(false));
		textFieldLocation.addActionListener(e -> process(true));
		btnCrossReference.addActionListener(e -> Main.database.crossReference());
		btnList.addActionListener(e -> onList());
		btnExport.addActionListener(e -> onExport());
		btnImport.addActionListener(e -> onImport());
		btnClear.addActionListener(e -> onClear());
		comboBoxMode.setModel(new DefaultComboBoxModel<>(Main.EMode.values()));
		comboBoxMode.setSelectedIndex(0);
	}

	private void process(boolean clearLocation) {
		Main.process((Main.EMode) comboBoxMode.getSelectedItem(),
				textFieldLocation.getText(),
				textFieldInput.getText());
		textFieldInput.setText("");
		if (clearLocation) textFieldLocation.setText("");
	}

	private void onClear() {
		textAreaOutput.setText("");
	}

	private void onDone() {
		System.out.println("Saving database");
		Main.database.close();
		dispose();
	}

	private void onList() {
		log("=== List of Tables ===");
		Main.database.list(true);
		log("=== End List ===");
	}

	private void onExport() {
		String[] locations = Main.database.list(false);
		AlertExport dialog = new AlertExport(locations);
		dialog.pack();
		dialog.setVisible(true);
	}

	private void onImport() {
		AlertImport dialog = new AlertImport();
		dialog.pack();
		dialog.setVisible(true);
	}

	void log(String s) {
		System.out.println(s);
		textAreaOutput.setText(textAreaOutput.getText() + s + "\n");
	}
}
