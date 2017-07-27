import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Interface extends JDialog {
	private JPanel contentPane;
	private JButton buttonDone;
	private JTextArea textAreaOutput;
	private JTextField textFieldInput;

	Interface() {
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonDone);
		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onClose();
			}
		});

		buttonDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onDone();
			}
		});

		textFieldInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.process(textFieldInput.getText());
				textFieldInput.setText("");
			}
		});
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
