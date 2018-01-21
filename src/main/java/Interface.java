import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class Interface extends JFrame {
	private JPanel contentPane;
	private JButton buttonDone;
	private JTextPane textAreaOutput;
	private JTextField textFieldInput;
	private JComboBox<Main.EMode> comboBoxMode;
	private JTextField textFieldLocation;
	private JButton btnCrossReference;
	private JButton btnList;
	private JButton btnExport;
	private JButton btnImport;
	private JButton btnClear;
	private AttributeSet asWhite;
	private AttributeSet asOffWhite;
	private BufferedWriter bw;

	Interface() throws IOException {
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
		btnCrossReference.addActionListener(e -> onCrossReference());
		btnList.addActionListener(e -> onList());
		btnExport.addActionListener(e -> onExport());
		btnImport.addActionListener(e -> onImport());
		btnClear.addActionListener(e -> onClear());
		comboBoxMode.setModel(new DefaultComboBoxModel<>(Main.EMode.values()));
		comboBoxMode.setSelectedIndex(0);

		StyleContext sc = StyleContext.getDefaultStyleContext();
		//Color 1 - White
		asWhite = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(255, 255, 255));
		asWhite = sc.addAttribute(asWhite, StyleConstants.FontFamily, "Lucida Console");
		asWhite = sc.addAttribute(asWhite, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
		sc = StyleContext.getDefaultStyleContext();
		//Color 2 - Off White
		asOffWhite = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(200, 200, 200));
		asOffWhite = sc.addAttribute(asOffWhite, StyleConstants.FontFamily, "Lucida Console");
		asOffWhite = sc.addAttribute(asOffWhite, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

		bw = new BufferedWriter(new FileWriter("WhoseDeskIsItAnyway.log", true));
	}

	public void setTextAreaColor() {
		textAreaOutput.setBackground(new Color(43, 43, 43));
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

	private void onCrossReference() {
		log("=== Cross Reference Results ===", false);
		Main.database.crossReference();
		log("===       End Results       ===", false);
	}

	private void onList() {
		log("=== List of Tables ===", false);
		Main.database.list(true);
		log("===    End List    ===", false);
	}

	private void onExport() {
		log("Exporting...", false);
		String[] locations = Main.database.list(false);
		AlertExport dialog = new AlertExport(locations);
		dialog.pack();
		dialog.setVisible(true);
	}

	private void onImport() {
		log("=== Begin Import ===", false);
		AlertImport dialog = new AlertImport();
		dialog.pack();
		dialog.setVisible(true);
		log("===  End Import  ===", false);
	}

	void log(String msg, boolean white) {
		System.out.println(msg);
		try {
			bw.write(String.format("%s %s\r\n", new Timestamp(System.currentTimeMillis()).toString(), msg));
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int len = textAreaOutput.getDocument().getLength();
		textAreaOutput.setCaretPosition(len);
		textAreaOutput.setCharacterAttributes(white ? asWhite : asOffWhite, false);
		textAreaOutput.replaceSelection(msg + "\n");
	}
}
