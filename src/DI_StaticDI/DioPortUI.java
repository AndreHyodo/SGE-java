package DI_StaticDI;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DioPortUI extends JPanel{
	// define the serialization number.
	private static final long serialVersionUID = 1L;
	
	private JPanel highBytes = new JPanel();
	private JPanel lowBytes = new JPanel();
	private JLabel[] bytesLevel = new JLabel[8];
	private JLabel lblPortIndex = new JLabel();
	private JLabel lblHexValue = new JLabel("00");
	private ImageIcon[] imageIcon ;

	private int  key;
	private byte mask = 0;
	private byte state = 0;
	private StateChangeListener listener = null;
	private boolean isEditable = false;
	
	public interface StateChangeListener{
		public void StateChangeAction(Object sender);
	}
	
	
	public DioPortUI(int index, JPanel parrent, Point location, ImageIcon[] image, byte state) {
		key = index;
		this.state = state;
		imageIcon = image;

		for (int a = 0; a < 4; a++) {
			bytesLevel[a] = new JLabel(imageIcon[(state >> a) & 0x1]);
			bytesLevel[a].setBounds(5 + a * 25, 3, 26, 26);
			bytesLevel[a].addMouseListener(new MouseClickListener());
			highBytes.add(bytesLevel[a]);

			bytesLevel[a + 4] = new JLabel(imageIcon[(state >> a) & 0x1]);
			bytesLevel[a + 4].setBounds(5 + a * 25, 3, 26, 26);
			bytesLevel[a + 4].addMouseListener(new MouseClickListener());
			lowBytes.add(bytesLevel[a + 4]);
		}
	}

	public int getKey(){
		return key;
	}
	
	public byte getMask(){
		return mask;
	}
	
	public void setMask(byte value){
		mask = value;
	}
	
	public byte getState(){
		return state;
	}
	
	public void setState(byte state){
		this.state = state;
		System.out.print(state +"\n");
		Refresh();
	}
	
	public void setState2(byte state, int high_low){
		this.state = state;
		RefreshAct(high_low);
	}
	
	public boolean getIsEditable(){
		return isEditable;
	}
	
	public void setIsEditable(boolean value){
		isEditable = value;
	}
	
	private void Refresh() {
		lblPortIndex.setText(String.valueOf(key));

		int bitValue = 0;
		for (int i = 7; i >= 0; i--) {
			bitValue = (state >> (7 - i)) & 0x1;
			bytesLevel[i].setIcon(imageIcon[bitValue]); 
			System.out.print(bitValue + "\n");
		}
		System.out.print("\n----------------------------------------\n");
		lblHexValue.setText(Integer.toHexString(state | 0xFFFFFF00).toUpperCase().substring(6));
	}
	
	public void RefreshAct(int portIndex) {
		lblPortIndex.setText(String.valueOf(key));

		int bitValue = 0;
		
		for (int i = 7; i >= 0; i--) {
			bitValue = (state >> (7 - i)) & 0x1;
			bytesLevel[i].setIcon(imageIcon[bitValue]); 
			if(portIndex == 1) {
				StaticDI.actStateHigh[i] = (byte)bitValue;
			}else {
				StaticDI.actStateLow[i] = (byte)bitValue;
			}
			
		}
		
		
		
		lblHexValue.setText(Integer.toHexString(state | 0xFFFFFF00).toUpperCase().substring(6));
		
	}
	
	public void JudgeDirection(byte portDirection){
		int bitValue = 0;
		mask = portDirection;
		for(int i = 7; i >= 0; i--){
			bitValue = (portDirection >> i) & 0x1;
			if(bitValue == 0){
				bytesLevel[i].setIcon(imageIcon[2]);
				bytesLevel[i].setEnabled(false);
			}else{
				bytesLevel[i].setEnabled(true);
			}
		}
		lblHexValue.setText(Integer.toHexString((byte)(state & mask) | 0xFFFFFF00).toUpperCase().substring(6));
	}
	
	public void AddStateChangeListener(StateChangeListener listener){
		this.listener = listener;
	}
	
	class MouseClickListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			int bitIndex = 0;
			JLabel source = (JLabel) e.getSource();
			if (isEditable) {
				for (int i = 0; i < 8; i++) {
					if (source == bytesLevel[i]) {
						bitIndex = i;
					}
				}
				if ((mask >> 7 - bitIndex & 0x1) == 1) {
					if ((state >> 7 - bitIndex & 0x1) == 1) {
						state = (byte) (state & ~(0x1 << 7 - bitIndex));
						bytesLevel[bitIndex].setIcon(imageIcon[0]);

					} else {
						state = (byte) (state | (0x1 << 7 - bitIndex));
						bytesLevel[bitIndex].setIcon(imageIcon[1]);
					}
					lblHexValue.setText(Integer.toHexString(state | 0xFFFFFF00).toUpperCase().substring(6));
				}
				//invoke the listener here
				if(listener != null){
					listener.StateChangeAction(DioPortUI.this);
				}
			}
		}
	}
}
