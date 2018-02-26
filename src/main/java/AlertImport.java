import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class AlertImport extends JDialog {
	private JPanel contentPane;
	private JButton btnImport;
	private JButton btnCancel;
	private JComboBox<Main.EMode> comboBoxMode;
	private JCheckBox checkBoxReAdd;

	AlertImport() {
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(btnImport);

		btnImport.addActionListener(e -> onImport());
		btnCancel.addActionListener(e -> onCancel());

		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(e -> onCancel(),
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		comboBoxMode.setModel(new DefaultComboBoxModel<>(new Main.EMode[]{Main.EMode.ADD, Main.EMode.DELETE}));
		comboBoxMode.setSelectedIndex(0);
	}

	private void onImport() {
		File dir = new File("\\\\vip-fs1\\Users");
		FileSystemView fsv = FileSystemView.getFileSystemView();
		dir = fsv.getParentDirectory(dir);
		dir = fsv.getChild(dir, "Users");
		dir = fsv.getChild(dir, "vipemp");
		dir = fsv.getChild(dir, "Filing");

		JFileChooser picker = new JFileChooser(dir);
		picker.setDialogTitle("Pick file to import");
		picker.setMultiSelectionEnabled(true);
		picker.setFileFilter(new FileNameExtensionFilter("text files (*.txt)", "txt"));
		picker.showOpenDialog(this);
		File[] infiles = picker.getSelectedFiles();
		final Main.EMode mode = (Main.EMode) comboBoxMode.getSelectedItem();
		for (File file : infiles) {
			try {
				Files.readAllLines(Paths.get(file.getAbsolutePath())).forEach(line -> {
					line = line.replace("Look for ", "");
					line = line.replace(" in:", ",");
					try {
						String[] lineParts = line.split(",");
						String vin = Main.verifyVINLength(lineParts[0]);
						String location;
						assert mode != null;
						switch (mode) {
							case ADD:
								location = file.getName().split("\\.")[0];
								Main.database.add(vin, location);
								break;
							case DELETE:
								for(int i = 1; i < lineParts.length; i++){
									//Delete all duplicates and if checkbox, readd a single one
									location = lineParts[i].trim();
									Main.database.delete(vin, location);
									if (checkBoxReAdd.isSelected()) Main.database.add(vin, location);
								}
								break;
						}


					} catch (Exception e1) {
						//Ignore
					}
				});
			} catch (IOException e2) {
				e2.printStackTrace();
				Main.log(e2.getMessage(), true);
			}
		}
		dispose();
	}

	private void onCancel() {
		dispose();
	}
}
