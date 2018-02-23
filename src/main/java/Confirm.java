import javax.swing.*;
import java.awt.event.*;

public class Confirm extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JLabel jLabel;
	private boolean confirmed = false;
	public Confirm(String message) {
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
		contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		jLabel.setText(message);
	}

	private void onOK() {
		// add your code here
		confirmed = true;
		dispose();
	}

	private void onCancel() {
		// add your code here if necessary
		confirmed = false;
		dispose();
	}

	public boolean isConfirmed() {
		return confirmed;
	}
}
