package jikkenD;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.util.Date;

public class WorkerThread extends Thread {
    private Socket socket;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
    private Router router;

    public WorkerThread(Socket socket) {
        super();
        this.socket = socket;
        this.router = Router.getInstance();
    }

    @Override
    public void run() {
        try (InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
            SimpleHttpServer.Response response = handleRequest(in);

            byte[] message = Parser.serializeResponse(response);
            out.write(message);
        } catch (IOException e) {
            errorLog("socket closed by client?");
        }
    }

    private SimpleHttpServer.Response handleRequest(InputStream in) throws IOException {
        SimpleHttpServer.Response response;

        try {
            SimpleHttpServer.Request request = Parser.parseRequest(in);
            Controller controller = router.route(request.getTarget());
            response = controller.handle(request);

            accessLog(request.getStartLine(), response.getStatusCode());
        } catch (ParseException | UnsupportedMethodException e) {
            response = new SimpleHttpServer.Response(Parser.PROTOCOL_VERSION, HttpStatus.BadRequest);
            response.setBody(SimpleHttpServer.readErrorPage(HttpStatus.BadRequest));

            errorLog(e.getMessage());
        }

        return response;
    }

    /**
     * アクセスログを出力します
     *
     * @param requestLine
     * @param responseCode
     */
    private void accessLog(String requestLine, int responseCode) {
        Date date = new Date();
        System.out.printf("[%s] \"%s\" %d%n", dateFormat.format(date), requestLine, responseCode);
    }

    /**
     * エラーログを出力します
     *
     * @param message
     */
    private void errorLog(String message) {
        Date date = new Date();
        System.out.printf("[%s] [ERROR] %s%n", dateFormat.format(date), message);
    }
}