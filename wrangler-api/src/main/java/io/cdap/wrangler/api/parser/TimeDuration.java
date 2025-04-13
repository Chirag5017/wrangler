package io.cdap.wrangler.api.parser;

public class TimeDuration extends Token {
    private final long millis;

    public TimeDuration(String value) {
        super("TIME_DURATION", value);
        this.millis = parse(value.trim().toLowerCase());
    }

    private long parse(String value) {
        if (value.endsWith("ms")) return (long)Double.parseDouble(value.replace("ms", ""));
        if (value.endsWith("s")) return (long)(Double.parseDouble(value.replace("s", "")) * 1000);
        if (value.endsWith("m")) return (long)(Double.parseDouble(value.replace("m", "")) * 60 * 1000);
        if (value.endsWith("h")) return (long)(Double.parseDouble(value.replace("h", "")) * 60 * 60 * 1000);
        return 0;
    }

    public long getMillis() {
        return millis;
    }
}
