package jikkenD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleHttpServer {
    public static final int PORT = 80;
    private static final String documentRoot;
    private static final Map<String, String> mimeTypes;
    private static final Map<HttpStatus, Path> errorPages;
    private static final byte[] EMPTY_BYTE_ARRAY = {};

    static {
        documentRoot = Paths.get(System.getProperty("C:\\Users\\Wamuro\\IdeaProjects\\CommunicationStudy\\jikken-d\\public"), "files", "www").toString();

        mimeTypes = new HashMap<>();
        mimeTypes.put("html", "text/html");
        mimeTypes.put("css", "text/css");
        mimeTypes.put("js", "application/js");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("txt", "text/plain");

        errorPages = new HashMap<>();
        errorPages.put(HttpStatus.BadRequest, Paths.get(documentRoot, "error/400.html"));
        errorPages.put(HttpStatus.NotFound, Paths.get(documentRoot, "error/404.html"));
    }

    public static String getDocumentRoot() {
        return documentRoot;
    }

    /**
     * 拡張子に対応した Content-Type を返します
     *
     * @param ext 拡張子
     * @return Content-Type
     */
    public static String extensionToContentType(String ext) {
        return mimeTypes.getOrDefault(ext, "");
    }

    public static byte[] readErrorPage(HttpStatus status) {
        if (!errorPages.containsKey(status)) {
            return EMPTY_BYTE_ARRAY;
        }

        byte[] ret;
        try {
            Path path = errorPages.get(status);
            ret = Files.readAllBytes(path);
        } catch (IOException e) {
            ret = EMPTY_BYTE_ARRAY;
        }

        return ret;
    }
    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(PORT)) {
            ExecutorService executor = Executors.newCachedThreadPool();

            while (true) {
                Socket socket = server.accept();

                // socket オブジェクトを渡して各リクエストの処理は別スレッドで
                executor.submit(new WorkerThread(socket));
            }
            // HTTPリクエストをぶん投げる


        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    // リクエストをHTTP Requestの形に成型する
    public static class Request extends AbstractHttpMessage {
        Method method;
        String target;
        String version;

        public Request(Method method, String target, String version) {
            super();
            this.method = method;
            this.target = target;
            this.version = version;
        }

        public Method getMethod() {
            return method;
        }

        public String getTarget() {
            return target;
        }

        public String getVersion() {
            return version;
        }

        @Override
        public String getStartLine() {
            return method.toString() + " " + target + " " + version;
        }
    }

    // レスポンスをHTTPレスポンス形式に成型
    public static class Response extends AbstractHttpMessage {
        String version;
        HttpStatus status;

        public Response(String version, HttpStatus status) {
            super();
            this.version = version;
            this.status = status;
        }

        public String getVersion() {
            return version;
        }

        public int getStatusCode() {
            return status.getCode();
        }

        public String getStatusName() {
            return status.getName();
        }

        @Override
        public String getStartLine() {
            return version + " " + getStatusCode() + " " + getStatusName();
        }
    }
}

