import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

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

		comboBoxMode.setSelectedIndex(0);
	}

	private void process(boolean clearLocation) {
		Main.process((String) comboBoxMode.getSelectedItem(), textFieldLocation.getText(), textFieldInput.getText());
		textFieldInput.setText("");
		if(clearLocation)textFieldLocation.setText("");
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
		Alert dialog = new Alert(locations);
		dialog.pack();
		dialog.setVisible(true);
	}

	private void onImport() {
		JFileChooser picker = new JFileChooser();
		picker.setDialogTitle("Pick file to import");
		picker.setMultiSelectionEnabled(true);
		picker.setFileFilter(new FileNameExtensionFilter("text files (*.txt)", "txt"));
		picker.showOpenDialog(this);
		File[] infiles = picker.getSelectedFiles();
		for (File file : infiles) {
			String location = file.getName().split("\\.")[0];
			try {
				Files.readAllLines(Paths.get(file.getAbsolutePath())).forEach(line -> {
					try {
						String vin = Main.verifyVINLength(line.split(",")[0]);
						Main.database.add(vin, location);
					} catch (Exception e1) {
						//Ignore
					}
				});
			} catch (IOException e2) {
				e2.printStackTrace();
				log(e2.getMessage());
			}
		}
	}

	void log(String s) {
		System.out.println(s);
		textAreaOutput.setText(textAreaOutput.getText() + s + "\n");
	}
}
