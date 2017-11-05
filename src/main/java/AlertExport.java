import javax.swing.*;
import java.awt.event.*;

public class AlertExport extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JComboBox<String> comboBox;

	AlertExport(String[] locations) {
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

		buttonOK.addActionListener(e -> onOK());

		buttonCancel.addActionListener(e -> onCancel());

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
		comboBox.setModel(new DefaultComboBoxModel<>(locations));
	}

	private void onOK() {
		Main.database.export((String) comboBox.getSelectedItem());
		dispose();
	}

	private void onCancel() {
		dispose();
	}
}
