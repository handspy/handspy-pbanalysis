package pt.up.hs.pbanalysis.reporting.sheet;

import java.util.ArrayList;
import java.util.List;

/**
 * Tabular-based sheet builder.
 *
 * @author José Carlos Paiva
 */
public abstract class TabularSheet implements Sheet {

    protected String name;
    protected String[] headers;
    protected List<Object[]> rows = new ArrayList<>();

    public TabularSheet() {}

    /**
     * Set the name of the sheet.
     *
     * @param name {@link String} the name of the sheet.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the header row values of the sheet.
     *
     * @param headers {@link String[]} the header row values of the sheet.
     */
    public void setHeader(String...headers) {
        this.headers = headers;
    }

    /**
     * Add one row of values to the sheet.
     *
     * @param values {@link Object[]} the row of values to add to the sheet.
     */
    public void addRow(Object[]values) {
        rows.add(values);
    }
}
