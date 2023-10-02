package DI_StaticDI;

import java.sql.Time;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;

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

            if (sqlhora_final.equals(zero)){
                String updateQuery = "UPDATE " + ETB + " SET hora_final = ? WHERE hora_final = '0'";
                preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setTime(1, sqlHoraAtual); 
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    public static boolean AguardandoCausal(String ETB,int i, int aux1, int aux2){

        boolean ret = false;

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

            LocalTime horaAtual = LocalTime.now();
            LocalDate dataAtual = LocalDate.now();
            Date DatetypeAtual = Date.valueOf(dataAtual);

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String stringSqlDate = format.format(sqldate);
            String stringAtualData = format.format(DatetypeAtual);

            // Converter Time para LocalTime
            LocalTime sqlHoraAtual = sqlhora_final.toLocalTime();

            // Calcular a diferença entre as duas LocalTime
            Duration diff = Duration.between(sqlHoraAtual, horaAtual);
            
            if(stringSqlDate.equalsIgnoreCase(stringAtualData)){
                if(diff.get(ChronoUnit.SECONDS) > 300){
                    ret = true;
                }else{
                    ret = false;
                }
            }else{
                ret = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }
	

    public static boolean VerificaCausal(String ETB) {

        boolean ok=false;

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

            if (sqlhora_final.equals(zero)){
                ok = true;
            }else{
                ok = false;
            }

            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ok;
	}

    public static void FaltaOperador(String ETB) {
        // Create a PreparedStatement to insert the new row into the database.
        java.sql.PreparedStatement preparedStatement = null;

        // Connect to the database.
        try (Connection connection = DriverManager.getConnection(Banco.JDBC_URL, Banco.USER, Banco.PASSWORD)) {

            String FaltaCausal = "Ausência de Operador";
            String obsCausal = "Operador não registrou causal dentro de 5 minutos.";

            // Get the current time and date.
            LocalTime horaAtual = LocalTime.now();
            LocalDate dataAtual = LocalDate.now();

            // Create a Date object from the current date.
            Date DatetypeAtual = Date.valueOf(dataAtual);

            // Create a Time object from the current time.
            Time sqlHoraAtual = Time.valueOf(horaAtual);

            Time time = Time.valueOf("00:00:00");

            // Create the SQL query to insert the new row.
            String query = "INSERT INTO " + ETB +" (`TestCell`, `causal`, `hora_inicio`, `hora_final` , `obs`, `date`) VALUES (?, ?, ?, ?, ?, ?)";

            // Prepare the statement.
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, "A04");
            preparedStatement.setString(2, FaltaCausal);
            preparedStatement.setTime(3, sqlHoraAtual);
            preparedStatement.setTime(4, time);
            preparedStatement.setString(5, obsCausal);
            preparedStatement.setDate(6, DatetypeAtual);

            // Execute the statement.
            preparedStatement.executeUpdate();

            preparedStatement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}




