package pt.up.hs.pbanalysis.reporting.workbook;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import pt.up.hs.pbanalysis.reporting.sheet.ExcelSheet;

import java.io.*;
import java.nio.file.Paths;
import java.util.Date;

public class ExcelWorkbook implements Workbook {

    private final Long creationTimestamp;
    private final HSSFWorkbook workbook;

    public ExcelWorkbook() {
        this.workbook = new HSSFWorkbook();
        this.creationTimestamp = new Date().getTime();
    }

    public void addSheet(ExcelSheet sheet) {
        sheet.addTo(workbook);
    }

    @Override
    public ByteArrayInputStream saveToStream() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public void save(String to, String basename) throws IOException {
        File out = new File(
            Paths
                .get(to, basename + "_" + creationTimestamp + ".xls")
                .toString()
        );
        try (FileOutputStream fos = new FileOutputStream(out)) {
            workbook.write(fos);
        }
    }
}
