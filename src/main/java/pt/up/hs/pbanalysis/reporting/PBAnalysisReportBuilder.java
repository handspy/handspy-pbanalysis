package pt.up.hs.pbanalysis.reporting;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import pt.up.hs.pbanalysis.reporting.sheet.ExcelSheet;
import pt.up.hs.pbanalysis.reporting.workbook.ExcelWorkbook;
import pt.up.hs.pbanalysis.service.dto.LengthUnit;
import pt.up.hs.pbanalysis.service.dto.TimeUnit;

import java.util.*;

public class PBAnalysisReportBuilder {

    private final MessageSource i18n;

    private final Map<String, ExcelSheet> pbAnalysisSheets = new HashMap<>();

    private String current = null;

    public PBAnalysisReportBuilder(
        MessageSource i18n
    ) {
        this.i18n = i18n;
    }

    public PBAnalysisReportBuilder createPBAnalysisSheet(String code, TimeUnit timeUnit, LengthUnit lengthUnit) {
        ExcelSheet pbAnalysisSheet = new ExcelSheet();
        pbAnalysisSheet.setName(
            i18n.getMessage("report.pbAnalysis", new String[] { code }, LocaleContextHolder.getLocale())
        );
        pbAnalysisSheet.setHeader(
            i18n.getMessage(
                "report.pbAnalysis.order",
                null,
                LocaleContextHolder.getLocale()
            ),
            i18n.getMessage(
                "report.pbAnalysis.burstDuration",
                new String[] { timeUnit.name().toLowerCase() },
                LocaleContextHolder.getLocale()
            ),
            i18n.getMessage(
                "report.pbAnalysis.pauseDuration",
                new String[] { timeUnit.name().toLowerCase() },
                LocaleContextHolder.getLocale()
            ),
            i18n.getMessage(
                "report.pbAnalysis.distance",
                new String[] { lengthUnit.name().toLowerCase() },
                LocaleContextHolder.getLocale()
            ),
            i18n.getMessage(
                "report.pbAnalysis.totalTime",
                new String[] { timeUnit.name().toLowerCase() },
                LocaleContextHolder.getLocale()
            ),
            i18n.getMessage(
                "report.pbAnalysis.speed",
                new String[] {
                    lengthUnit.name().toLowerCase(),
                    timeUnit.name().toLowerCase()
                },
                LocaleContextHolder.getLocale()
            ),
            i18n.getMessage(
                "report.pbAnalysis.length",
                null,
                LocaleContextHolder.getLocale()
            ),
            i18n.getMessage(
                "report.pbAnalysis.text",
                null,
                LocaleContextHolder.getLocale()
            )
        );
        pbAnalysisSheets.put(code, pbAnalysisSheet);
        this.current = code;
        return this;
    }

    public PBAnalysisReportBuilder newBurstLine(
            Integer order,
            Double burstDuration, Double pauseDuration,
            Double distance, Double totalTime, Double speed,
            Double length, String text
    ) {
        if (current == null) {
            return this;
        }
        pbAnalysisSheets.get(current).addRow(new Object[] {
            order,
            burstDuration, pauseDuration,
            distance, totalTime, speed, length,
            text
        });
        return this;
    }

    public PBAnalysisReportBuilder concludeSheet() {
        this.current = null;
        return this;
    }

    public ExcelWorkbook conclude() {
        ExcelWorkbook workbook = new ExcelWorkbook();

        List<String> codes = new ArrayList<>(pbAnalysisSheets.keySet());
        codes.sort(Comparator.naturalOrder());

        for (String code: codes) {
            workbook.addSheet(pbAnalysisSheets.get(code));
        }

        return workbook;
    }

}
