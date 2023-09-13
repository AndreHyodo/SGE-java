package DI_StaticDI;

import Common.*;

import java.sql.Time;
import java.time.LocalTime;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Automation.BDaq.*;
import DI_StaticDI.Banco;
import DI_StaticDI.StaticDI.ButtonConfigureActionListener;
import DI_StaticDI.StaticDI.WindowCloseActionListener;
import DI_StaticDI.ColorStatus.*;

public class AjustaCausal {
	
	public static void SetHoraFinal(String ETB) {
		
//		Time hora_final = Banco.Hora_final(ETB);  // Suponha que isso retorna um objeto Time
//
////		// Comparar com "00:00:00 (MIDNIGHT)"
////		if (localTimeHoraFinal.equals(LocalTime.MIDNIGHT)) {
////		    //registra o retorno do motor
////		}
//		
//			// Estabeleça a conexão com o banco de dados
//			Connection connection = DriverManager.getConnection(Banco.JDBC_URL, Banco.USER, Banco.PASSWORD);
//	
//			// Execute a consulta para recuperar a última linha da tabela
//			String query = "SELECT hora_final FROM " + ETB + " ORDER BY id DESC LIMIT 1";
//			Statement statement = connection.createStatement();
//			ResultSet resultSet = statement.executeQuery(query);
//	
//			// Verifique se o horário final é 00:00:00
//			LocalTime localTimeHoraFinal = hora_final.toLocalTime();
//			LocalTime horaAtual =  LocalTime.now();
//			Time sqlHoraAtual = Time.valueOf(horaAtual);
//			if (localTimeHoraFinal.equals(LocalTime.MIDNIGHT)) {
//			    // Atualize a última linha da tabela para definir o horário final como um novo valor
//			    String updateQuery = "UPDATE " + ETB + " SET hora_final = ? WHERE id = (SELECT id FROM " + ETB + " ORDER BY id DESC LIMIT 1)";
//			    java.sql.PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
//			    preparedStatement.setTime(1, sqlHoraAtual); // Defina a nova hora final aqui
//			    preparedStatement.executeUpdate();
//			    preparedStatement.close();
//			}
//	
//			// Feche os recursos
//				resultSet.close();
//				statement.close();
//				connection.close();
		java.sql.PreparedStatement preparedStatement;

        try (Connection connection = DriverManager.getConnection(Banco.JDBC_URL, Banco.USER, Banco.PASSWORD)) {
        	
        	Time sqlhora_final = null;
        	
        	Statement statement = connection.createStatement();
            
            String query = "SELECT hora_final FROM " + ETB + " ORDER BY id DESC LIMIT 1";

            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
            	sqlhora_final = resultSet.getTime("hora_final");
            }
            
            LocalTime horaZero = LocalTime.MIDNIGHT;
            Time zero = Time.valueOf(horaZero);
            
            LocalTime horaAtual = LocalTime.now();

            // Converter LocalTime para Time
            Time sqlHoraAtual = Time.valueOf(horaAtual);
            
            System.out.print(ETB + ": Hora final: " + sqlhora_final + "\n"+ ETB +": Hora atual: " + sqlHoraAtual + "\n");

            if (sqlhora_final.equals(zero)){
                System.out.print("\n " + ETB + ": HORA FINAL ZERADA ");
                // Atualize a última linha da tabela para definir o horário final como um novo valor
                String updateQuery = "UPDATE " + ETB + " SET hora_final = ? WHERE hora_final = '0'";
                preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setTime(1, sqlHoraAtual); // Defina a nova hora final aqui
                preparedStatement.executeUpdate();
                // preparedStatement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
}




