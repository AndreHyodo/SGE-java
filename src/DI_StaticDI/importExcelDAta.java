package DI_StaticDI;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class importExcelDAta {

	public static void main(String[] args) {
        String excelFilePath = "caminho/para/o/arquivo.xlsx";
        String sheetName = "NomeDaPlanilha";
        int rowNumber = 1; // Número da linha que contém os dados

        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(rowNumber);

            if (row != null) {
                Cell cell = row.getCell(1); // Coluna onde estão os números de motor

                if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                    double numMotDouble = cell.getNumericCellValue();
                    int getNumMot = (int) numMotDouble;

                    // Atualizar a variável com o valor obtido da planilha
                    // StaticDI.getNumMot = getNumMot;

                    System.out.println("Número de Motor: " + getNumMot);
                } else {
                    System.out.println("Célula não contém um valor numérico");
                }
            } else {
                System.out.println("Linha não encontrada");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
	}


}
