package jikkenD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.stream.Stream;

public class HttpRequest {

    private static final String CRLF = "\r\n";

    private final String headerText;
    private final String bodyText;

    public HttpRequest(InputStream input) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        ) {

            this.headerText = this.readHeader(in);
            this.bodyText = this.readBody(in);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String readHeader(BufferedReader in) throws IOException {
        String line = in.readLine();
        StringBuilder header = new StringBuilder();

        while (line != null && !line.isEmpty()) {
            header.append(line + CRLF);
            line = in.readLine();
        }

        return header.toString();
    }

    private String readBody(BufferedReader in) throws IOException {
        final int contentLength = this.getContentLength();

        if (contentLength <= 0) {
            return null;
        }

        char[] c = new char[contentLength];
        in.read(c);

        return new String(c);
    }

    private int getContentLength() {
        return Stream.of(this.headerText.split(CRLF))
                .filter(headerLine -> headerLine.startsWith("Content-Length"))
                .map(contentLengthHeader -> contentLengthHeader.split(":")[1].trim())
                .mapToInt(Integer::parseInt)
                .findFirst().orElse(0);
    }

    public String getHeaderText() {
        return this.headerText;
    }

    public String getBodyText() {
        return this.bodyText;
    }
}
