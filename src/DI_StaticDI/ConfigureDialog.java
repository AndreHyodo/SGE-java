package DI_StaticDI;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import Automation.BDaq.*;

public class ConfigureDialog extends JDialog {
	// define the serialization number.
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JComboBox cmbDevice;
	
	public boolean isFirstLoad = true;

	/**
	 * 
	 * Build Date:2011-9-13 
	 * Author:Administrator
	 * Function Description: Create the dialog.
	 */
	public ConfigureDialog(StaticDI parrent) {
		super(parrent);
//		// Add window close action listener.
		addWindowListener(new WindowCloseActionListener());
		
		setResizable(false);
		setTitle("Static DI - Configuration");
		setBounds(100, 100, 292, 152);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(SystemColor.control);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		cmbDevice = new JComboBox();
		cmbDevice.setBounds(87, 25, 166, 21);
		contentPanel.add(cmbDevice);
//		
		JLabel lblDevice = new JLabel("Device:");
		lblDevice.setBounds(28, 28, 54, 15);
		contentPanel.add(lblDevice);
//		
		JButton btnOK = new JButton("OK");
		btnOK.setBounds(87, 76, 69, 23);
		btnOK.addActionListener(new ButtonOKActionListener());
		contentPanel.add(btnOK);
//		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(178, 76, 75, 23);
		btnCancel.addActionListener(new ButtonCancelActionListener());
		contentPanel.add(btnCancel);
		
		Initialization();
	}
	
	/**
	 * 
	 *Build Date:2011-9-14
	 *Author:Administrator
	 *Function Description: this function is used to initialize the configure dialog.
	 */
	private void Initialization(){
		InstantDiCtrl instantDiCtrl = new InstantDiCtrl();
		ArrayList<DeviceTreeNode> installedDevice = instantDiCtrl.getSupportedDevices();
		
		if(installedDevice.size() <= 0){
			ShowMessage("No device to support the currently demonstrated function!");
			System.exit(0);
		}else{
			for(DeviceTreeNode installed : installedDevice){
				cmbDevice.addItem(installed.toString());
			}
			//0 - demo device
			//1 - usb device
			cmbDevice.setSelectedIndex(0);
		}
	}
	
	/**
	 * 
	 *Build Date:2011-9-14
	 *Author:Administrator
	 *Function Description: this function is used to get device name.
	 * @return String device name
	 */
	public String GetDeviceName(){
		return cmbDevice.getSelectedItem().toString();
	}
	
	/**
	 * 
	 * Build Date:2011-9-13 
	 * Author:Administrator
	 * Function Description: This function is used to show the error massage to user!
	 * 
	 * @param message: the message shown to users!
	 */
	protected void ShowMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Warning MessageBox",
				JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * 
	 * @author Administrator
	 * Class Function Description: This class is used to listen the OK button's action! 
	 */
	class ButtonOKActionListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			StaticDI parrent = (StaticDI) getParent();
//			System.out.print(parrent + "\n");
			parrent.Initialization();
			parrent.setVisible(true);
			setVisible(false);
			
		}
	}
	
	/**
	 * 
	 * @author Administrator
	 * Class Function Description: This class is used to listen the Cancel button's action! 
	 */
	class ButtonCancelActionListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			if (isFirstLoad) {
				System.exit(0);
			} else {
				setVisible(false);
			}
		}
	}
	
	/**
	 * 
	 * @author Administrator
	 * Class Function Description: This class is used to listen the configure dialog's closing event.
	 */
	class WindowCloseActionListener extends WindowAdapter{
		@Override
		public void windowClosing(WindowEvent e) {
			if (isFirstLoad) {
				System.exit(0);
			}
		}
	}
}
