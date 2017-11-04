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
	private JButton btnCrossreference;
	private JButton btnList;

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

		textFieldInput.addActionListener(e -> {
			process();
		});
		textFieldLocation.addActionListener(e -> {
			process();
			textFieldLocation.setText("");
		});
		btnCrossreference.addActionListener(e -> Main.crossReference());
		btnList.addActionListener(e -> Main.list());
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

	void setTextAreaOutput(String s) {
		textAreaOutput.setText(s);
	}

	String getTextAreaOutput() {
		return textAreaOutput.getText();
	}
}
