package pt.up.hs.pbanalysis.reporting.workbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface Workbook {

    /**
     * Save the report and get an input stream to it.
     *
     * @return {@link ByteArrayInputStream} the input stream to the report
     *                                      contents'.
     * @throws IOException if an error occurs while saving the report and get
     *                     an input stream out of it.
     */
    ByteArrayInputStream saveToStream() throws IOException;

    /**
     * Save the report to a file.
     *
     * @param to {@link String} path to save the file to.
     * @param basename {@link String} the base name of the report.
     * @throws IOException if an error occurs while saving the report and get
     *                     an input stream out of it.
     */
    void save(String to, String basename) throws IOException;
}
