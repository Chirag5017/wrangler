package io.cdap.wrangler.plugin.aggregate;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.TestingRig;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AggregateStatsTest {

    @Test
    public void testAggregateStats() throws Exception {
        List<Row> rows = Arrays.asList(
                new Row("data_transfer_size", "1MB").add("response_time", "1s"),
                new Row("data_transfer_size", "512KB").add("response_time", "500ms")
        );

        String[] recipe = new String[] {
                "aggregate-stats :data_transfer_size :response_time total_size_mb total_time_sec"
        };

        List<Row> results = TestingRig.execute(recipe, rows);

        Assert.assertEquals(1, results.size());

        double expectedSize = (1 * 1024 * 1024 + 512 * 1024) / (1024.0 * 1024.0);
        double expectedTime = (1000 + 500) / 1000.0;

        Assert.assertEquals(expectedSize, (double) results.get(0).getValue("total_size_mb"), 0.001);
        Assert.assertEquals(expectedTime, (double) results.get(0).getValue("total_time_sec"), 0.001);
    }
}