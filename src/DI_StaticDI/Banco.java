package DI_StaticDI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;

public class Banco {
    
	public static String JDBC_URL = "jdbc:mysql://localhost:3306/sge";
	public static final String USER = "root";
	public static final String PASSWORD = "Stellantis@2023";
    private static String causal = "";
    private static Time hora_final;
    
    private static void Connection(String roomName, String column) {
    	try {
            Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            
            String query = "SELECT " + column +  " FROM " + roomName + " ORDER BY id DESC LIMIT 1";

            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
            	if(column == "causal") {
            		causal = resultSet.getString(column);
            	}else if(column == "hora_final") {
            		hora_final = resultSet.getTime(column);
            	}
                // System.out.println(roomName + ": " + causal);
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String  fetchAndDisplayCausal(String roomName) {

        
    	
    	Connection(roomName, "causal");        
        
        return causal;
        
    }
    
    public static Time Hora_final(String roomName) {
    	
    	Connection(roomName, "hora_final");        
        
        return hora_final;
        
    }

}