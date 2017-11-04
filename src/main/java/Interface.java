import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Interface extends JFrame {
	private JPanel contentPane;
	private JButton buttonDone;
	private JTextArea textAreaOutput;
	private JTextField textFieldInput;
	private JComboBox comboBoxMode;
	private JTextField textFieldLocation;
	private JButton btnCrossReference;
	private JButton btnList;
	private JButton btnExport;
	private JButton btnImport;


	Interface() {
		setContentPane(contentPane);
		getRootPane().setDefaultButton(buttonDone);
		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onClose();
			}
		});
		buttonDone.addActionListener(e -> onDone());

		textFieldInput.addActionListener(e -> process());
		textFieldLocation.addActionListener(e -> {
			process();
			textFieldLocation.setText("");
		});
		btnCrossReference.addActionListener(e -> Main.database.crossReference());
		btnList.addActionListener(e -> {
			log("=== List of Tables ===");
			Main.database.list(true);
			log("=== End List ===");
		});
		btnExport.addActionListener(e -> {
			String[] locations = Main.database.list(false);
			Alert dialog = new Alert(locations);
			dialog.pack();
			dialog.setVisible(true);

		});
		btnImport.addActionListener(e -> {

		});
		comboBoxMode.setSelectedIndex(0);
	}

	private void process() {
		Main.process((String) comboBoxMode.getSelectedItem(), textFieldLocation.getText(), textFieldInput.getText());
		textFieldInput.setText("");
	}

	private void onDone() {
		Main.database.close();
		dispose();
	}

	private void onClose() {
		dispose();
	}


	void log(String s) {
		System.out.println(s);
		textAreaOutput.setText(textAreaOutput.getText() + s + "\n");
	}
}
