package formatter;

import java.io.InputStream;
import java.io.Writer;

public interface PrintScriptFormatter {
    /**
     * executes a PrintScript file handling its resulting messages and errors.
     * @param src Source file.
     * @param version PrintScript version, 1.0 and 1.1 must be supported.
     * @param config config file.
     * @param writer Writer, where the formatted output should be written
     */
    void format(InputStream src, String version, InputStream config, Writer writer);
}
