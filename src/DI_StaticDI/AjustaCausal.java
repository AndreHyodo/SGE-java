package DI_StaticDI;

import Common.*;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;

import Automation.BDaq.*;
import DI_StaticDI.Banco;
import DI_StaticDI.StaticDI.ButtonConfigureActionListener;
import DI_StaticDI.StaticDI.WindowCloseActionListener;
import DI_StaticDI.ColorStatus.*;

public class AjustaCausal {
	
	public static void SetHoraFinal(String ETB) {
		
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
            
            // System.out.print(ETB + ": Hora final: " + sqlhora_final + "\n"+ ETB +": Hora atual: " + sqlHoraAtual + "\n");

            if (sqlhora_final.equals(zero)){
                // System.out.print("\n " + ETB + ": HORA FINAL ZERADA ");
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

    public static boolean AguardandoCausal(String ETB){

        boolean ret = false;

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
            
            // LocalTime horaAtual = LocalTime.now();

            // // Converter LocalTime para Time
            // Time sqlHoraAtual = Time.valueOf(horaAtual);
            
            // // System.out.print(ETB + ": Hora final: " + sqlhora_final + "\n"+ ETB +": Hora atual: " + sqlHoraAtual + "\n");

            // Time Dif = sqlhora_final-horaAtual;

            LocalTime horaAtual = LocalTime.now();

            // Converter Time para LocalTime
            LocalTime sqlHoraAtual = sqlhora_final.toLocalTime();

            // Calcular a diferença entre as duas LocalTime
            Duration diff = Duration.between(sqlHoraAtual, horaAtual);

            // // Converter a diferença de volta para LocalTime
            // LocalTime diffLocalTime = horaAtual.minusNanos(diff.toNanos());

            // // Converter LocalTime de volta para Time
            // Time Dif = Time.valueOf(diffLocalTime);

            // Time horaAt = Time.valueOf(horaAtual);
            // Time horaAtSQL = Time.valueOf(sqlHoraAtual);
            
            

            if(diff.get(ChronoUnit.SECONDS) > 900){
                ret = true;
            }else{
                ret = false;
            }
            
            // Printing difference between time in seconds
            System.out.println(ETB + " : " +diff.get(ChronoUnit.SECONDS));  

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.print(" -> " + ret);
        return ret;
    }
	
}




