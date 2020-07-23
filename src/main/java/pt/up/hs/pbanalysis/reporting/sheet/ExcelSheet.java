package pt.up.hs.pbanalysis.reporting.sheet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * A sheet builder for Excel.
 *
 * @author José Carlos Paiva
 */
public class ExcelSheet extends TabularSheet {

    /**
     * Add sheet to a pre-created workbook.
     *
     * @param workbook {@link HSSFWorkbook} the workbook.
     */
    public void addTo(HSSFWorkbook workbook) {
        HSSFSheet sheet = workbook.createSheet(name);

        HSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        for (int i = 1; i <= rows.size(); i++) {
            HSSFRow valueRow = sheet.createRow(i);
            Object[] values = rows.get(i - 1);
            for (int j = 0; j < values.length; j++) {
                HSSFCell cell = valueRow.createCell(j);
                if (values[j] == null) {
                    cell.setCellValue("");
                    continue;
                }
                if (values[j] instanceof String) {
                    cell.setCellValue((String) values[j]);
                } else if (values[j] instanceof Double) {
                    cell.setCellValue((double) values[j]);
                } else if (values[j] instanceof Integer) {
                    cell.setCellValue((int) values[j]);
                } else if (values[j] instanceof Boolean) {
                    cell.setCellValue((boolean) values[j]);
                }
            }
        }
    }
}
