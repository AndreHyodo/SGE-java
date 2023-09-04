package DI_StaticDI;

public class ColorStatus {
	
	public static String getStatusColor(int i) {
			
			String color = new String();
			
			color = StaticDI.colorStatus[i];
			
			return color;
		}
	
}
