package DI_StaticDI;

import Common.*;

import java.sql.Time;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.time.temporal.ChronoUnit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.*;


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

    public static boolean AguardandoCausal(String ETB,int i, int aux1, int aux2){

        boolean ret = false;

        java.sql.PreparedStatement preparedStatement;

        try (Connection connection = DriverManager.getConnection(Banco.JDBC_URL, Banco.USER, Banco.PASSWORD)) {
        	
        	Time sqlhora_final = null;
            Date sqldate = null;
        	
        	Statement statement = connection.createStatement();
            
            String query = "SELECT hora_final, date FROM " + ETB + " ORDER BY id DESC LIMIT 1";

            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
            	sqlhora_final = resultSet.getTime("hora_final");
                sqldate = resultSet.getDate("date");
            }

            // Instant instant = sqldate.atStartOfDay(ZoneOffset.UTC).toInstant();

            // Converter Instant para LocalDate usando um ZoneId (você pode escolher o fuso horário apropriado)
            // LocalDate localSqlDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

            // LocalDate localSqlDate = convertDateObject(sqldate);
            
            LocalTime horaZero = LocalTime.MIDNIGHT;
            Time zero = Time.valueOf(horaZero);

            LocalTime horaAtual = LocalTime.now();
            LocalDate dataAtual = LocalDate.now();
            Date DatetypeAtual = Date.valueOf(dataAtual);

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String stringSqlDate = format.format(sqldate);
            String stringAtualData = format.format(DatetypeAtual);

            // System.out.println(stingSqlDate + " -> " + stringAtualData);

            // Converter Time para LocalTime
            LocalTime sqlHoraAtual = sqlhora_final.toLocalTime();

            // Calcular a diferença entre as duas LocalTime
            Duration diff = Duration.between(sqlHoraAtual, horaAtual);

            // Duration diffDate = Duration.between(dataAtual, localSqlDate);

            // // Converter a diferença de volta para LocalTime
            // LocalTime diffLocalTime = horaAtual.minusNanos(diff.toNanos());

            // // Converter LocalTime de volta para Time
            // Time Dif = Time.valueOf(diffLocalTime);

            // Time horaAt = Time.valueOf(horaAtual);
            // Time horaAtSQL = Time.valueOf(sqlHoraAtual);

            // System.out.println("\n----------Aqui dif date : " + diffDate.get(ChronoUnit.DAYS));
            
            if(stringAtualData != stringSqlDate){
                ret = true;
            }else{
                if(diff.get(ChronoUnit.SECONDS) > 30){
                    System.out.println("\n----------Aqui ret = true : " + diff.get(ChronoUnit.SECONDS));
                    ret = true;
                }else{
                    System.out.println("\n----------Aqui ret = false : " + diff.get(ChronoUnit.SECONDS));
                    ret = false;
                }
            }

            
            
            // Printing difference between time in seconds
            // System.out.println(ETB + " : " +diff.get(ChronoUnit.SECONDS)+ " -> " + ret + " aux1-> " + aux1 + " aux2-> " + aux2); 

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }
	
}




