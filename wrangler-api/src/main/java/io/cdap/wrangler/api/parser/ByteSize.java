package io.cdap.wrangler.api.parser;

public class ByteSize extends Token {
    private final long bytes;

    public ByteSize(String value) {
        super("BYTE_SIZE", value);
        this.bytes = parse(value.trim().toUpperCase());
    }

    private long parse(String value) {
        if (value.endsWith("KB")) return (long)(Double.parseDouble(value.replace("KB", "")) * 1024);
        if (value.endsWith("MB")) return (long)(Double.parseDouble(value.replace("MB", "")) * 1024 * 1024);
        if (value.endsWith("GB")) return (long)(Double.parseDouble(value.replace("GB", "")) * 1024 * 1024 * 1024);
        if (value.endsWith("TB")) return (long)(Double.parseDouble(value.replace("TB", "")) * 1024L * 1024 * 1024 * 1024);
        return Long.parseLong(value.replace("B", ""));
    }

    public long getBytes() {
        return bytes;
    }
}
