package io.cdap.wrangler.steps;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.Step;
import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.annotations.Aggregator;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.Token;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.UsageDefinition;
import io.cdap.wrangler.api.parser.UsageDefinitions;

import java.util.List;
import java.util.ArrayList;

@Aggregator(name = "aggregate-stats", usage = "aggregate-stats <byteCol> <timeCol> <outByteCol> <outTimeCol>")
public class AggregateStats implements Directive {
    private String byteColumn;
    private String timeColumn;
    private String outputByteColumn;
    private String outputTimeColumn;

    private long totalBytes = 0;
    private long totalMillis = 0;
    private int rowCount = 0;

    @Override
    public UsageDefinition define() {
        return UsageDefinitions.builder()
            .define("byteCol", ColumnName.class)
            .define("timeCol", ColumnName.class)
            .define("outByteCol", ColumnName.class)
            .define("outTimeCol", ColumnName.class)
            .build();
    }

    @Override
    public void initialize(List<Token> args) {
        byteColumn = ((ColumnName) args.get(0)).value();
        timeColumn = ((ColumnName) args.get(1)).value();
        outputByteColumn = ((ColumnName) args.get(2)).value();
        outputTimeColumn = ((ColumnName) args.get(3)).value();
    }

    @Override
    public List<Row> execute(List<Row> rows) {
        for (Row row : rows) {
            Object byteVal = row.getValue(byteColumn);
            Object timeVal = row.getValue(timeColumn);

            if (byteVal != null && timeVal != null) {
                totalBytes += new ByteSize(byteVal.toString()).getBytes();
                totalMillis += new TimeDuration(timeVal.toString()).getMillis();
                rowCount++;
            }
        }

        Row result = new Row();
        result.add(outputByteColumn, totalBytes / (1024.0 * 1024)); // Convert to MB
        result.add(outputTimeColumn, totalMillis / 1000.0);         // Convert to seconds

        List<Row> resultList = new ArrayList<>();
        resultList.add(result);
        return resultList;
    }
}
