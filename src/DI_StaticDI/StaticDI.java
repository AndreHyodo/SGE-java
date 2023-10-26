package DI_StaticDI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import Automation.BDaq.DeviceInformation;
import Automation.BDaq.ErrorCode;
import Automation.BDaq.InstantDiCtrl;
import Common.Global;

public class StaticDI extends JFrame implements ActionListener {

	/**
	 *  define the serialization number.
	 */

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel allPanel;
	private JPanel headerPanel;
	private JPanel spt;
	private JLabel room_spt;
	private JLabel roomLabel[] = new JLabel[19];
	private JPanel motorPanel[] = new JPanel[19];
	private JLabel causeLabel[] = new JLabel[19];
	private JLabel StartCart_id[] = new JLabel[4];
	private JLabel StoppedTimeTotal[] = new JLabel[15];
	private JLabel StoppedTimeAtual[] = new JLabel[15];
	private JPanel RunPanel[] = new JPanel[15];
	private JPanel TitleSC[] = new JPanel[4];
	private JPanel Title[] = new JPanel[15];
	
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
	
	String roomName[] = {"A01", "A02", "A03", "A04", "A05", "A10", "A06", "A07", "A08", "A09", "A11", "A12", "B01", "B02", "B03", "B04", "B05", "B06", "SPT"};
	String causal[] = new String[19];
	private String months[] = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
	String Hist_Eff_Dev[] = new String[12];
	String Hist_Eff_Dur[] = new String[12];
	String Hist_Eff_SC[] = new String[12];

	private JLabel Label_Eff_Dev[] = new JLabel[12];
	private JLabel Label_Eff_Dur[] = new JLabel[12];
	private JLabel Label_Eff_SC[] = new JLabel[12];

	private JLabel Label_months_DEV[] = new JLabel[12];
	private JLabel Label_months_DUR[] = new JLabel[12];	
	private JLabel Label_months_SC[] = new JLabel[12];				

	private JPanel Panel_Eff_Dev[] = new JPanel[12];
	private JPanel Panel_Eff_Dur[] = new JPanel[12];
	private JPanel Panel_Eff_SC[] = new JPanel[12];

	public boolean causalOK;
	private boolean registroCausalOK;

	public static Color colorStatus[] = {Color.BLACK ,Color.BLACK , Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK};

	public static Color colorStatusEFF_DEV[] = {Color.BLACK ,Color.BLACK , Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK};

	public static Color colorStatusEFF_DUR[] = {Color.BLACK ,Color.BLACK , Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK};
	
	public static Color colorStatusEFF_SC[] = {Color.BLACK ,Color.BLACK , Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK};
	
	public static int TimerETB[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	String color = "BLACK";
	
	public static byte[] actStateHigh = new byte[8];
	public static byte[] actStateLow = new byte[8];
	

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

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        configureDialog = new ConfigureDialog(this);
		configureDialog.setModal(true);
		configureDialog.setVisible(true);
		
        // Header
        headerPanel = new JPanel(new GridLayout(1, 3));
        headerPanel.setPreferredSize(new Dimension(getWidth(), (getHeight()/16)));
        headerPanel.setBackground(new Color(0x24405d));
		headerPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentPane.add(headerPanel, BorderLayout.NORTH); 

		//LOGO STELLANTIS
		ImageIcon originalIcon = new ImageIcon(getClass().getResource("logoStellantis.png")); 
		int larguraDesejada = 200; 
		int alturaDesejada = 100; 
		Image imagemRedimensionada = originalIcon.getImage().getScaledInstance(larguraDesejada, alturaDesejada, Image.SCALE_SMOOTH);
		ImageIcon iconRedimensionado = new ImageIcon(imagemRedimensionada);

		JLabel logo = new JLabel();
		logo.setIcon(iconRedimensionado);
        headerPanel.add(logo);

		JLabel title = new JLabel("Efficiency Management System");
		title.setFont(new Font("Open Sans Bold", Font.BOLD, 42));
		title.setForeground(new Color(0xf6f7f9));

		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		titlePanel.setOpaque(false); 
		title.setHorizontalAlignment(SwingConstants.RIGHT); 
		titlePanel.add(title);

		headerPanel.add(titlePanel, BorderLayout.LINE_END);

		//Logo AutomAH System
		ImageIcon AutomAH = new ImageIcon(getClass().getResource("logoAutomAH.png")); 
		int larguraDesejadaLogo = 240; 
		int alturaDesejadaLogo = 70; 
		Image imagemRedimensionada2 = AutomAH.getImage().getScaledInstance(larguraDesejadaLogo, alturaDesejadaLogo, Image.SCALE_SMOOTH);
		ImageIcon iconRedimensionadoLogo = new ImageIcon(imagemRedimensionada2);

		JLabel logo2 = new JLabel();
		logo2.setIcon(iconRedimensionadoLogo);

		JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		FlowLayout layout = (FlowLayout)logoPanel.getLayout();
        layout.setVgap(0);
		logoPanel.setBackground(new Color(0x24405d));
		logoPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		logoPanel.add(logo2);

		headerPanel.add(logoPanel, BorderLayout.EAST);

		JPanel footerPanel = new JPanel();
		footerPanel.setLayout(new GridLayout(1, 4, 0, 0));
		footerPanel.setBackground(Color.LIGHT_GRAY);
		contentPane.add(footerPanel, BorderLayout.SOUTH);

		// HISTÓRICO DESENVOLVIMENTO -----------------------------------------------------------------------------------

		JPanel histDEV = new JPanel();
		histDEV.setBorder(BorderFactory.createLineBorder(colorStatus[18], 2));  
		histDEV.setBorder(new EmptyBorder(0, 0, 0, 20));
		histDEV.setLayout(new BorderLayout());
		histDEV.setBackground(Color.LIGHT_GRAY);

		JLabel name_histDEV = new JLabel("Desenvolvimento");
		name_histDEV.setHorizontalAlignment(SwingConstants.CENTER);
		name_histDEV.setOpaque(true);
		name_histDEV.setFont(new Font("Arial", Font.PLAIN, 30));
		histDEV.add(name_histDEV, BorderLayout.NORTH);

		JPanel histDEV_data = new JPanel(new GridLayout(2, 6));


		histDEV.add(histDEV_data, BorderLayout.CENTER);

		for(int i=0;i<12;i++){
			Panel_Eff_Dev[i] = new JPanel();      
            Panel_Eff_Dev[i].setBorder(BorderFactory.createLineBorder(colorStatusEFF_DEV[i], 1)); 
            Panel_Eff_Dev[i].setLayout(new BorderLayout());

			Label_months_DEV[i] = new JLabel(months[i]);      
            Label_months_DEV[i].setHorizontalAlignment(SwingConstants.CENTER);
			Label_months_DEV[i].setFont(new Font("Arial", Font.PLAIN, 15));
            Label_months_DEV[i].setLayout(new BorderLayout());
			Panel_Eff_Dev[i].add(Label_months_DEV[i],BorderLayout.NORTH);
			
			Label_Eff_Dev[i] = new JLabel("90%");
			Label_Eff_Dev[i].setHorizontalAlignment(SwingConstants.CENTER);
			Label_Eff_Dev[i].setFont(new Font("Arial", Font.PLAIN, 20));
			Panel_Eff_Dev[i].add(Label_Eff_Dev[i],BorderLayout.SOUTH);

			
			histDEV_data.add(Panel_Eff_Dev[i]);
		}


		footerPanel.add(histDEV);
		

		// HISTÓRICO DURABILIDADE -----------------------------------------------------------------------------------

		JPanel histDUR = new JPanel();       
		histDUR.setBorder(BorderFactory.createLineBorder(colorStatus[18], 1)); 
		histDEV.setBorder(new EmptyBorder(0, 0, 0, 20));
		histDUR.setLayout(new BorderLayout());
		histDUR.setBackground(Color.LIGHT_GRAY);

		JLabel name_histDUR = new JLabel("Durabilidade");
		name_histDUR.setHorizontalAlignment(SwingConstants.CENTER);
		name_histDUR.setOpaque(true);
		name_histDUR.setFont(new Font("Arial", Font.PLAIN, 30));
		histDUR.add(name_histDUR, BorderLayout.NORTH);

		JPanel histDUR_data = new JPanel(new GridLayout(2, 6));


		histDUR.add(histDUR_data, BorderLayout.CENTER);

		for(int i=0;i<12;i++){
			Panel_Eff_Dur[i] = new JPanel();      
            Panel_Eff_Dur[i].setBorder(BorderFactory.createLineBorder(colorStatusEFF_DUR[i], 1)); 
            Panel_Eff_Dur[i].setLayout(new BorderLayout());

			Label_months_DUR[i] = new JLabel(months[i]);      
            Label_months_DUR[i].setHorizontalAlignment(SwingConstants.CENTER);
			Label_months_DUR[i].setFont(new Font("Arial", Font.PLAIN, 15));
            Label_months_DUR[i].setLayout(new BorderLayout());
			Panel_Eff_Dur[i].add(Label_months_DUR[i],BorderLayout.NORTH);
			
			Label_Eff_Dur[i] = new JLabel("90%");
			Label_Eff_Dur[i].setHorizontalAlignment(SwingConstants.CENTER);
			Label_Eff_Dur[i].setFont(new Font("Arial", Font.PLAIN, 20));
			Panel_Eff_Dur[i].add(Label_Eff_Dur[i],BorderLayout.SOUTH);

			
			histDUR_data.add(Panel_Eff_Dur[i]);
		}


		footerPanel.add(histDUR);
		// HISTÓRICO STARTCART -----------------------------------------------------------------------------------

		JPanel histSC = new JPanel();       
		histSC.setBorder(BorderFactory.createLineBorder(colorStatus[18], 2)); 
		histSC.setBorder(new EmptyBorder(0, 20, 0, 20));
		histSC.setLayout(new BorderLayout());
		histSC.setBackground(Color.LIGHT_GRAY);

		JLabel name_histSC = new JLabel("Start Cart");
		name_histSC.setHorizontalAlignment(SwingConstants.CENTER);
		name_histSC.setOpaque(true);
		name_histSC.setFont(new Font("Arial", Font.PLAIN, 30));
		histSC.add(name_histSC, BorderLayout.NORTH);

		JPanel histSC_data = new JPanel(new GridLayout(2, 6));


		histSC.add(histSC_data, BorderLayout.CENTER);

		for(int i=0;i<12;i++){
			Panel_Eff_SC[i] = new JPanel();      
            Panel_Eff_SC[i].setBorder(BorderFactory.createLineBorder(colorStatusEFF_SC[i], 1)); 
            Panel_Eff_SC[i].setLayout(new BorderLayout());

			Label_months_SC[i] = new JLabel(months[i]);      
            Label_months_SC[i].setHorizontalAlignment(SwingConstants.CENTER);
			Label_months_SC[i].setFont(new Font("Arial", Font.PLAIN, 15));
            Label_months_SC[i].setLayout(new BorderLayout());
			Panel_Eff_SC[i].add(Label_months_SC[i],BorderLayout.NORTH);
			
			Label_Eff_SC[i] = new JLabel("90%");
			Label_Eff_SC[i].setHorizontalAlignment(SwingConstants.CENTER);
			Label_Eff_SC[i].setFont(new Font("Arial", Font.PLAIN, 20));
			Panel_Eff_SC[i].add(Label_Eff_SC[i],BorderLayout.SOUTH);

			
			histSC_data.add(Panel_Eff_SC[i]);
		}


		footerPanel.add(histSC);

		// SALA PROVA TRANSMISSOES -----------------------------------------------------------------------------------

		spt = new JPanel();        
		spt.setBorder(BorderFactory.createLineBorder(colorStatus[18], 2)); 
		spt.setLayout(new BorderLayout());
		
		
			// First row: Room name
		JPanel titleSPT = new JPanel(new GridLayout(1,2));

		room_spt = new JLabel(roomName[18]);
		room_spt.setHorizontalAlignment(SwingConstants.CENTER);
		room_spt.setBorder(BorderFactory.createLineBorder(colorStatus[18], 2));
		room_spt.setOpaque(true);
		room_spt.setFont(new Font("Arial", Font.PLAIN, 20));

		// EFF
		JPanel eff_Trans = new JPanel();
		eff_Trans.setPreferredSize(new Dimension((getWidth()/10),getHeight()));
		eff_Trans.setLayout(new GridLayout(1 ,2));
		eff_Trans.setPreferredSize(new Dimension((getWidth()/3), 25));
		eff_Trans.setBackground(Color.GREEN);
		
		
		JLabel effLabelT = new JLabel("Eficiência");
		effLabelT.setHorizontalAlignment(SwingConstants.CENTER);
		effLabelT.setFont(new Font("Arial", Font.BOLD, 20));
		eff_Trans.add(effLabelT);

		JLabel num_EffT = new JLabel("90%");
		num_EffT.setHorizontalAlignment(SwingConstants.CENTER);
		num_EffT.setFont(new Font("Arial", Font.BOLD, 25));
		eff_Trans.add(num_EffT);

		titleSPT.add(room_spt);
		titleSPT.add(eff_Trans, BorderLayout.EAST);
		spt.add(titleSPT, BorderLayout.NORTH);

		JPanel SPT_data = new JPanel();
		SPT_data.setLayout(new GridLayout(1,2));
		spt.add(SPT_data);

		// Second row: Cause
		causal[18] = Banco.fetchAndDisplayCausal(roomName[18]);
		System.out.println(causal[18]);
		causeLabel[18] = new JLabel("<html>" + causal[18] + "</html>");
		causeLabel[18].setHorizontalAlignment(SwingConstants.CENTER);
		causeLabel[18].setFont(new Font("Arial", Font.BOLD, 25));
		SPT_data.add(causeLabel[18], BorderLayout.CENTER);

		// Third row: EFF and Data List
		JPanel effAndDataPanelTrans = new JPanel();
		effAndDataPanelTrans.setLayout(new GridLayout(3, 2));
		
		JLabel numTransLabel = new JLabel("Transmissão");
		numTransLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		effAndDataPanelTrans.add(numTransLabel);
		
		JLabel getNumTrans = new JLabel("124563");
		getNumTrans.setFont(new Font("Arial", Font.PLAIN, 20));
		effAndDataPanelTrans.add(getNumTrans);

		JLabel projetoLabelT = new JLabel("Projeto");
		projetoLabelT.setFont(new Font("Arial", Font.PLAIN, 20));
		effAndDataPanelTrans.add(projetoLabelT);
		
		JLabel getProjetoLabelT = new JLabel("B1025");
		getProjetoLabelT.setFont(new Font("Arial", Font.PLAIN, 20));
		effAndDataPanelTrans.add(getProjetoLabelT);

		JLabel testeLabelT = new JLabel("Teste");
		testeLabelT.setFont(new Font("Arial", Font.PLAIN, 20));
		effAndDataPanelTrans.add(testeLabelT);
		
		JLabel getTesteLabelT = new JLabel("DUR112");
		getTesteLabelT.setFont(new Font("Arial", Font.PLAIN, 20));
		effAndDataPanelTrans.add(getTesteLabelT);

		SPT_data.add(effAndDataPanelTrans, BorderLayout.CENTER);

		footerPanel.add(spt);

		// -------------------------------------------------------------------------------------------------

        allPanel = new JPanel();
        allPanel.setLayout(new GridLayout(3, 6, 10, 10)); // 3 rows, 6 columns
		allPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        allPanel.setBackground(Color.LIGHT_GRAY);

        JScrollPane portPanel = new JScrollPane(allPanel);
        contentPane.add(portPanel, BorderLayout.CENTER);

     // Add motor data containers to the grid
        for (int i = 0; i < 18; i++) { // 3 rows * 6 columns = 18 containers
        	
            motorPanel[i] = new JPanel();
            motorPanel[i].setPreferredSize(new Dimension(200, 200));         
            motorPanel[i].setBorder(BorderFactory.createLineBorder(colorStatus[i], 4)); // 2 é a largura da borda
            motorPanel[i].setLayout(new BorderLayout());
            
			 // First row: Room name            
            roomLabel[i] = new JLabel(roomName[i]);
            roomLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
            roomLabel[i].setBorder(BorderFactory.createLineBorder(colorStatus[i], 4));
            roomLabel[i].setOpaque(true);
			//roomLabel.setBackground(Color.LIGHT_GRAY);
        	roomLabel[i].setFont(new Font("Arial", Font.PLAIN, 45));
        	

			if(i>5 && i<10){
				TitleSC[i-6] = new JPanel();
				TitleSC[i-6].setLayout(new GridLayout(2,1));
				TitleSC[i-6].setPreferredSize(new Dimension(getWidth(), (getHeight()/12)));

				StartCart_id[i-6] = new JLabel("StartCart");
           		StartCart_id[i-6].setHorizontalAlignment(SwingConstants.CENTER);
				StartCart_id[i-6].setBackground(Color.lightGray);
				StartCart_id[i-6].setOpaque(true);
				StartCart_id[i-6].setFont(new Font("Arial", Font.PLAIN, 20));

				TitleSC[i-6].add(roomLabel[i]);
				TitleSC[i-6].add(StartCart_id[i-6]);

				motorPanel[i].add(TitleSC[i-6], BorderLayout.NORTH);

			}else if(i<=5){
				// motorPanel[i].add(roomLabel[i], BorderLayout.NORTH);
				Title[i] = new JPanel();
				Title[i].setLayout(new GridLayout(2,1));
				Title[i].setPreferredSize(new Dimension(getWidth(), (getHeight()/12)));

				RunPanel[i] = new JPanel();
				RunPanel[i].setLayout(new GridLayout(1,2));

				StoppedTimeTotal[i] = new JLabel("Parada Total:");
           		StoppedTimeTotal[i].setHorizontalAlignment(SwingConstants.CENTER);
				StoppedTimeTotal[i].setBackground(Color.lightGray);
				StoppedTimeTotal[i].setOpaque(true);
				StoppedTimeTotal[i].setFont(new Font("Arial", Font.PLAIN, 15));

				StoppedTimeAtual[i] = new JLabel("Atual:");
				StoppedTimeAtual[i].setHorizontalAlignment(SwingConstants.CENTER);
				StoppedTimeAtual[i].setBackground(Color.lightGray);
				StoppedTimeAtual[i].setOpaque(true);
				StoppedTimeAtual[i].setFont(new Font("Arial", Font.PLAIN, 15));

				RunPanel[i].add(StoppedTimeTotal[i]);
				RunPanel[i].add(StoppedTimeAtual[i]);

				Title[i].add(roomLabel[i]);
				Title[i].add(RunPanel[i]);

				motorPanel[i].add(Title[i], BorderLayout.NORTH);
			}else{
				// motorPanel[i].add(roomLabel[i], BorderLayout.NORTH);
				Title[i-4] = new JPanel();
				Title[i-4].setLayout(new GridLayout(2,1));
				Title[i-4].setPreferredSize(new Dimension(getWidth(), (getHeight()/12)));

				RunPanel[i-4] = new JPanel();
				RunPanel[i-4].setLayout(new GridLayout(1,2));

				StoppedTimeTotal[i-4] = new JLabel("Parada Total:" + i);
           		StoppedTimeTotal[i-4].setHorizontalAlignment(SwingConstants.CENTER);
				StoppedTimeTotal[i-4].setBackground(Color.lightGray);
				StoppedTimeTotal[i-4].setOpaque(true);
				StoppedTimeTotal[i-4].setFont(new Font("Arial", Font.PLAIN, 15));

				StoppedTimeAtual[i-4] = new JLabel("Atual:");
				StoppedTimeAtual[i-4].setHorizontalAlignment(SwingConstants.CENTER);
				StoppedTimeAtual[i-4].setBackground(Color.lightGray);
				StoppedTimeAtual[i-4].setOpaque(true);
				StoppedTimeAtual[i-4].setFont(new Font("Arial", Font.PLAIN, 15));

				RunPanel[i-4].add(StoppedTimeTotal[i-4]);
				RunPanel[i-4].add(StoppedTimeAtual[i-4]);

				Title[i-4].add(roomLabel[i]);
				Title[i-4].add(RunPanel[i-4]);

				motorPanel[i].add(Title[i-4], BorderLayout.NORTH);
			}
    		
            // Second row: Cause
            causal[i] = Banco.fetchAndDisplayCausal(roomName[i]);
            causeLabel[i] = new JLabel("<html>" + causal[i] + "</html>");
            causeLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
            causeLabel[i].setFont(new Font("Arial", Font.PLAIN, 30));
            motorPanel[i].add(causeLabel[i], BorderLayout.CENTER);

            // Third row: EFF and Data List
            JPanel effAndDataPanel = new JPanel();

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
			if (i % 2 == 0) {
				location.y += 40;
			} else {
				location.y += 55;
			}
		}

		if (timer == null) {
			timer = new Timer(1000, this);
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

		for(int i=0; i<19; i++){
			TimerETB[i]++;
		}
		
		if (!Global.BioFaild(errorCode)) {
			for (int b = 0; b < portCount; b++) {
				portCountAct = b+1;
				DiPorts[b].setState2(data[b],portCountAct);
			}
			int aux1=0, aux2=12;
			int room=0;
			for (int i = 17; i >= 0; i--) { 
	        	if(i<4) {
	        		if(actStateLow[i+4]== (byte)0) {
						TimerETB[aux2]=0;
	        			colorStatus[aux2] = Color.GREEN;
	        			causal[aux2] = "Motor em funcionamento";
	        			AjustaCausal.SetHoraFinal(roomName[room]);
	        			aux2++;
	        		}else {
						registroCausalOK = AjustaCausal.VerificaCausal(roomName[room]);
						causalOK = AjustaCausal.AguardandoCausal(roomName[room], i, aux1, aux2);

						if(TimerETB[aux2]<300){
							colorStatus[aux2] = Color.YELLOW;
							if(registroCausalOK == true){
								causal[aux2] = Banco.fetchAndDisplayCausal(roomName[room]);
							}else{
								causal[aux2] = "Aguardando Causal";
							}
						}else{
							colorStatus[aux2] = Color.RED;
							if(registroCausalOK == true){
								causal[aux2] = Banco.fetchAndDisplayCausal(roomName[room]);
							}else{
								AjustaCausal.FaltaOperador(roomName[room]);
								causal[aux2] = Banco.fetchAndDisplayCausal(roomName[room]);
							}
						}
	        			aux2++;
	        		}
	        	}else if(i>=4 && i<6){
	        		if(actStateHigh[i-4]==(byte)0) {
						TimerETB[aux2]=0;
	        			colorStatus[aux2] = Color.GREEN;
	        			causal[aux2] = "Motor em funcionamento";
						AjustaCausal.SetHoraFinal(roomName[room]);
	        			aux2++;
	        		}else {
						//registroCausalOK -> true = ultimo causal em aberto ----- flase = ultimo causal já fechado
						registroCausalOK = AjustaCausal.VerificaCausal(roomName[room]);
						// causalOK -> false = mais de 300s do registro do causal
						causalOK = AjustaCausal.AguardandoCausal(roomName[room], i, aux1, aux2);

						if(TimerETB[aux2]<300){
							colorStatus[aux2] = Color.YELLOW;
							if(registroCausalOK == true){
								causal[aux2] = Banco.fetchAndDisplayCausal(roomName[room]);
							}else{
								causal[aux2] = "Aguardando Causal";
							}
						}else{
							colorStatus[aux2] = Color.RED;
							if(registroCausalOK == true){
								causal[aux2] = Banco.fetchAndDisplayCausal(roomName[room]);
							}else{
								AjustaCausal.FaltaOperador(roomName[room]);
								causal[aux2] = Banco.fetchAndDisplayCausal(roomName[room]);
							}
						}
	        			aux2++;
	        		}
	        	}else if(i>11){
	        		if(actStateHigh[i-10]==(byte)0) {
						TimerETB[aux1]=0;
	        			colorStatus[aux1] = Color.GREEN;
	        			causal[aux1] = "Motor em funcionamento";
						AjustaCausal.SetHoraFinal(roomName[room]);
	        			aux1++;
	        		}else {
						registroCausalOK = AjustaCausal.VerificaCausal(roomName[room]);
						causalOK = AjustaCausal.AguardandoCausal(roomName[room], i, aux1, aux2);

						if(TimerETB[aux1]<300){
							colorStatus[aux1] = Color.YELLOW;
							if(registroCausalOK == true){
								causal[aux1] = Banco.fetchAndDisplayCausal(roomName[room]);
							}else{
								causal[aux1] = "Aguardando Causal";
							}
						}else{
							colorStatus[aux1] = Color.RED;
							if(registroCausalOK == true){
								causal[aux1] = Banco.fetchAndDisplayCausal(roomName[room]);
							}else{
								AjustaCausal.FaltaOperador(roomName[room]);
								causal[aux1] = Banco.fetchAndDisplayCausal(roomName[room]);
							}
						}
	        			aux1++;
	        		}
	        	}
	       
	        	if(motorPanel[i] != null) {
	        		motorPanel[i].setBorder(BorderFactory.createLineBorder(colorStatus[i], 4));
		            motorPanel[i].repaint();
		            roomLabel[i].setBorder(BorderFactory.createLineBorder(colorStatus[i], 4));
		            roomLabel[i].setBackground(colorStatus[i]);
		            roomLabel[i].repaint();
					spt.setBorder(BorderFactory.createLineBorder(colorStatus[18], 4));
					spt.repaint();
		            room_spt.setBorder(BorderFactory.createLineBorder(colorStatus[18], 4));
		            room_spt.setBackground(colorStatus[18]);
					room_spt.repaint();
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
