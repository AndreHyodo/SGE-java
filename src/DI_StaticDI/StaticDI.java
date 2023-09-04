package DI_StaticDI;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.Font;
import java.awt.Graphics;

import javax.lang.model.type.NullType;
import javax.print.DocFlavor.URL;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
// import javax.swing.UIManager;
// import javax.swing.border.EmptyBorder;
// import java.awt.SystemColor;
// import java.awt.Taskbar.State;

// import javax.swing.JButton;
// import javax.swing.border.LineBorder;
import javax.swing.JScrollPane;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import Common.*;
import Automation.BDaq.*;
// import DI_StaticDI.Banco;
// import DI_StaticDI.AjustaCausal;
// import DI_StaticDI.StaticDI.ButtonConfigureActionListener;
// import DI_StaticDI.StaticDI.WindowCloseActionListener;
// import DI_StaticDI.ColorStatus.*;

public class StaticDI extends JFrame implements ActionListener {

	/**
	 *  define the serialization number.
	 */

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JScrollPane portPanel;
	private JPanel allPanel;
	private JPanel headerPanel;
	private JLabel startCart;
	private JLabel roomLabel[] = new JLabel[18];
	private JPanel motorPanel[] = new JPanel[18];
	private JLabel causeLabel[] = new JLabel[18];
	public ImageIcon[] imageIcon = { 
			new BackgroundPanel("ledLow.png", "Low").getImageIcon(),
			new BackgroundPanel("ledHigh.png", "High").getImageIcon() 
			};
	public ImageIcon logo = new ImageIcon("logoStellantis.png");
	
	private InstantDiCtrl instantDiCtrl = new InstantDiCtrl();
	private ConfigureDialog configureDialog;
	public int portCount;
	private DioPortUI[] DiPorts = new DioPortUI[18];
	private byte[] data;
	
	String roomName[] = {"A01", "A02", "A03", "A04", "A05", "A10", "A06", "A07", "A08", "A09", "A11", "A12", "B01", "B02", "B03", "B04", "B05", "B06"};
	String causal[] = new String[18];
//	public static String colorStatus[] = {"Black", "Black", "Black", "Black", "Black", "Black", "Black", "Black", "Black", "Black", "Black", "Black", "Black", "Black", "Black", "Black", "Black", "Black"};
	
	public static Color colorStatus[] = {Color.BLACK ,Color.BLACK , Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK};
	
	
	String color = "BLACK";
	
	public static byte[] actStateHigh = new byte[8];
	public static byte[] actStateLow = new byte[8];
	
//	public static int portCount;

	
	JPanel painel[] = new JPanel[18];
	
	

	private Timer timer;
	


	/**
	 * 
	 * Build Date:2011-9-14 
	 * Author:Administrator 
	 * Function Description: Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StaticDI frame = new StaticDI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 
	 * Build Date:2011-9-14 
	 * Author:Administrator
	 * Function Description: Create the frame.
	 */
	public StaticDI() {
		portCount = instantDiCtrl.getPortCount();
		
//		try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);
        
        Point location = new Point(0, 0);

        configureDialog = new ConfigureDialog(this);
		configureDialog.setModal(true);
		configureDialog.setVisible(true);
		
        // Header
        headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(getWidth(), (getHeight()/20)));
        headerPanel.setBackground(new Color(0x24405d));
        contentPane.add(headerPanel, BorderLayout.NORTH); 
        

//		COLOCAR AQUI A LOGO STELLANTIS E A LOGO AUTOMAH SYSTEM
        
        JLabel title = new JLabel("Efficiency Management System");
//      COLOCAR O TITLE COM COR DA LOGO STELLANTIS
        title.setFont(new Font("Arial", Font.ITALIC, 40));
        title.setForeground(new Color(0xf6f7f9));
        headerPanel.add(title, BorderLayout.CENTER);

        // Footer
        // JPanel footerPanel = new JPanel();
        // footerPanel.setPreferredSize(new Dimension(getWidth(), (getHeight()/6)));
        // footerPanel.setBackground(Color.LIGHT_GRAY);
        // contentPane.add(footerPanel, BorderLayout.SOUTH);

		JPanel footerPanel = new JPanel();
		footerPanel.setPreferredSize(new Dimension(getWidth(), (getHeight() / 6)));
		footerPanel.setBackground(Color.LIGHT_GRAY);
		contentPane.add(footerPanel, BorderLayout.SOUTH);

		// Create an instance of EfficiencyChartPanel and add it to the footer
		EfficiencyChartPanel chartPanel = new EfficiencyChartPanel();
		footerPanel.add(chartPanel);

        
//      COLOCAR AQUI OS GRÁFICOS DE EFICIENCIA E A ASSINATURA NO FOOTER

        allPanel = new JPanel();
        allPanel.setLayout(new GridLayout(3, 6, 10, 10)); // 3 rows, 6 columns
        allPanel.setBackground(Color.LIGHT_GRAY);
        
        


        JScrollPane portPanel = new JScrollPane(allPanel);
        contentPane.add(portPanel, BorderLayout.CENTER);

     // Add motor data containers to the grid
        for (int i = 0; i < 18; i++) { // 3 rows * 6 columns = 18 containers
        	int state = getState();	
        	
            motorPanel[i] = new JPanel();
            motorPanel[i].setPreferredSize(new Dimension(200, 200));         
            motorPanel[i].setBorder(BorderFactory.createLineBorder(colorStatus[i], 4)); // 2 é a largura da borda
            motorPanel[i].setLayout(new BorderLayout());
            
            
            
            
//            for (int j = 0; j < portCount; j++) {
//    			DiPorts[j] = new DioPortUI(j, roomLabel, location, imageIcon, (byte) 0);
//    			
//    		}
            
            // Indicação de START CART - 6 a 9
            
//            if(i>5 && i<10) {
//            	
//            	JPanel identif = new JPanel();
//            	identif.setLayout(new GridLayout(2, 1));
//            	identif.setPreferredSize(new Dimension(getWidth(),20));
//            	motorPanel[i].add(identif);
//            	
//            	
//            	// First row: Room name            
//                roomLabel[i] = new JLabel(roomName[i]);
//                roomLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
//                roomLabel[i].setBorder(BorderFactory.createLineBorder(colorStatus[i], 4));
//                roomLabel[i].setOpaque(true);
////                roomLabel.setBackground(Color.LIGHT_GRAY);
//                roomLabel[i].setFont(new Font("Arial", Font.PLAIN, 35));
//                identif.add(roomLabel[i], BorderLayout.NORTH);
//            	
//            	startCart = new JLabel("START CART");
//            	startCart.setHorizontalAlignment(SwingConstants.CENTER);
//            	startCart.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
//            	startCart.setFont(new Font("Arial", Font.ITALIC, 5));
//            	identif.add(startCart, BorderLayout.CENTER);                
            
			 // First row: Room name            
            roomLabel[i] = new JLabel(roomName[i]);
            roomLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
            roomLabel[i].setBorder(BorderFactory.createLineBorder(colorStatus[i], 4));
            roomLabel[i].setOpaque(true);
			//          roomLabel.setBackground(Color.LIGHT_GRAY);
        	roomLabel[i].setFont(new Font("Arial", Font.PLAIN, 35));
        	motorPanel[i].add(roomLabel[i], BorderLayout.NORTH);
    		
            // Second row: Cause
            causal[i] = Banco.fetchAndDisplayCausal(roomName[i]);
            causeLabel[i] = new JLabel("<html>" + causal[i] + "</html>");
            causeLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
            causeLabel[i].setFont(new Font("Arial", Font.PLAIN, 25));
            motorPanel[i].add(causeLabel[i], BorderLayout.CENTER);

            // Third row: EFF and Data List
            JPanel effAndDataPanel = new JPanel();
//            effAndDataPanel.setLayout(new GridLayout(3, 2));

            // EFF
            JPanel eff = new JPanel();
            eff.setLayout(new GridLayout(2, 1));
            eff.setPreferredSize(new Dimension((getWidth()/12), 70));
            eff.setBackground(Color.GREEN);
            effAndDataPanel.add(eff, BorderLayout.WEST);
            
            JLabel effLabel = new JLabel("Eficiência");
            effLabel.setHorizontalAlignment(SwingConstants.CENTER);
            effLabel.setFont(new Font("Arial", Font.BOLD, 15));
            eff.add(effLabel);

            JLabel num_Eff = new JLabel("90%");
            num_Eff.setHorizontalAlignment(SwingConstants.CENTER);
            num_Eff.setFont(new Font("Arial", Font.BOLD, 35));
            eff.add(num_Eff);
            

            // Data List
            JPanel data = new JPanel();
            data.setPreferredSize(new Dimension((getWidth()/15), 70));
//            data.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
            data.setLayout(new GridLayout(3, 2));
            effAndDataPanel.add(data, BorderLayout.EAST);
            
            JLabel numMotLabel = new JLabel("Motor");
            numMotLabel.setFont(new Font("Arial", Font.BOLD, 15));
            data.add(numMotLabel);
            
            JLabel getNumMot = new JLabel("124563");
            getNumMot.setFont(new Font("Arial", Font.BOLD, 15));
            data.add(getNumMot);

            JLabel projetoLabel = new JLabel("Projeto");
            projetoLabel.setFont(new Font("Arial", Font.BOLD, 15));
            data.add(projetoLabel);
            
            JLabel getProjetoLabel = new JLabel("B1025");
            getProjetoLabel.setFont(new Font("Arial", Font.BOLD, 15));
            data.add(getProjetoLabel);

            JLabel testeLabel = new JLabel("Teste");
            testeLabel.setFont(new Font("Arial", Font.BOLD, 15));
            data.add(testeLabel);
            
            JLabel getTesteLabel = new JLabel("DUR112");
            getTesteLabel.setFont(new Font("Arial", Font.BOLD, 15));
            data.add(getTesteLabel);

            motorPanel[i].add(effAndDataPanel, BorderLayout.SOUTH);

            allPanel.add(motorPanel[i]);
            
           
            
        }
        
        
        

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
        
    }
	
//	public void DialogBox(StaticDI parrent) {
//		configureDialog = new ConfigureDialog(parrent);
//		configureDialog.setModal(true);
//		configureDialog.setVisible(true);
//	}
//	
	
	/**
	 * 
	 * Build Date:2011-9-14
	 * Author:Administrator
	 * Function Description: This function is used to initialize the main frame.
	 */
	public void Initialization() {
		
		setTitle("Static DI - Run(" + configureDialog.GetDeviceName() + ")");

		try {
			instantDiCtrl.setSelectedDevice(new DeviceInformation(configureDialog.GetDeviceName()));
		} catch (Exception ex) {
			ShowMessage("Sorry, there're some errors occured: " + ex.getMessage());
		}
		
		portCount = instantDiCtrl.getPortCount();
		DiPorts = new DioPortUI[portCount];
		Point location = new Point(0, 0);
		contentPane.removeAll();
		contentPane.repaint();
		
		
		
		for (int i = 0; i < portCount; i++) {
			DiPorts[i] = new DioPortUI(i, contentPane, location, imageIcon, (byte) 0);
			System.out.print("num portas: " + portCount + "\n" + DiPorts[i]);
			if (i % 2 == 0) {
				location.y += 40;
			} else {
				location.y += 55;
			}
		}
//		headerPanel.setPreferredSize(new Dimension(350, 48 * portCount));

		if (timer == null) {
			timer = new Timer(100, this);
		}
		timer.start();
	}

	/**
	 * 
	 * Build Date:2011-9-14 
	 * Author:Administrator
	 * Function Description: This function is overload the interface ActionListener,and 
	 * 						 is implementation need this function!
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (data == null || data.length < portCount) {
			data = new byte[portCount];
		}
		int portCountAct;
		ErrorCode errorCode = instantDiCtrl.Read(0, portCount, data);
		
		if (!Global.BioFaild(errorCode)) {
			for (int b = 0; b < portCount; b++) {
				portCountAct = b+1;
				DiPorts[b].setState2(data[b],portCountAct);
			}
			int aux1=0, aux2=12;
			int room=0;
			for (int i = 17; i >= 0; i--) { // 3 rows * 6 columns = 18 containers
//	        	int state = getState();
	        	if(i<4) {
	        		if(actStateLow[i+4]== (byte)0) {
	        			colorStatus[aux2] = Color.GREEN;
	        			causal[aux2] = "Motor em funcionamento";
	        			AjustaCausal.SetHoraFinal(roomName[room]);
	        			aux2++;
	        		}else {
	        			colorStatus[aux2] = Color.RED;
	        			causal[aux2] = Banco.fetchAndDisplayCausal(roomName[i]);
	        			aux2++;
	        		}
//	        		System.out.print("\n High channel: "+ i + " = " +actStateHigh[i] + " ");
	        	}else if(i>=4 && i<6){
	        		if(actStateHigh[i-4]==(byte)0) {
	        			colorStatus[aux2] = Color.GREEN;
	        			causal[aux2] = "Motor em funcionamento";
	        			aux2++;
	        		}else {
	        			colorStatus[aux2] = Color.RED;
	        			causal[aux2] = Banco.fetchAndDisplayCausal(roomName[i]);
	        			aux2++;
	        		}
//					System.out.print("\n Low channel: "+ i + " = " +actStateLow[i-10] + " ");
	        	}else if(i>11){
	        		if(actStateHigh[i-10]==(byte)0) {
	        			colorStatus[aux1] = Color.GREEN;
	        			causal[aux1] = "Motor em funcionamento";
	        			aux1++;
	        		}else {
	        			colorStatus[aux1] = Color.RED;
	        			causal[aux1] = Banco.fetchAndDisplayCausal(roomName[i]);
	        			aux1++;
	        		}
//					System.out.print("\n Low channel: "+ i + " = " +actStateLow[i-10] + " ");
	        	}
	        	
	        	
//	        	System.out.print("1: " + aux1 + "& "+ "2: "+ aux2 + "\n");
	       
	        	if(motorPanel[i] != null) {
	        		motorPanel[i].setBorder(BorderFactory.createLineBorder(colorStatus[i], 4));
		            motorPanel[i].repaint();
		            roomLabel[i].setBorder(BorderFactory.createLineBorder(colorStatus[i], 4));
		            roomLabel[i].setBackground(colorStatus[i]);
		            roomLabel[i].repaint();
		            contentPane.repaint();
	        	}
	        	
	        	if(colorStatus[i] != null) {
	        		causeLabel[i].setText("<html>" + causal[i] + "</html>");
	                causeLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
	        		causeLabel[i].repaint();
	        		contentPane.repaint();
	        	}
	        	
	        	room++;
			}
		} else {
			ShowMessage("Sorry, there're some errors occred, ErrorCode: " + errorCode.toString());
			return;
		}

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
	 * Class Function Description: This class is used to listen the configure button 's action.
	 */
	class ButtonConfigureActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(timer != null){
				timer.stop();
			}
			configureDialog.isFirstLoad = false;
			configureDialog.setVisible(true);
		}
	}
	
	/**
	 * 
	 * @author Administrator
	 * Class Function Description: This class is used to listen the main frame's closing event.
	 */
	class WindowCloseActionListener extends WindowAdapter{
		@Override
		public void windowClosing(WindowEvent e) {
			if (instantDiCtrl != null) {
				instantDiCtrl.Cleanup();
			}
			if(timer != null){
				timer.stop();
			}
		}
	}
	
	
}
