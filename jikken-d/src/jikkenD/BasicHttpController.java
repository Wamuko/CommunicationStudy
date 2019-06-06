package jikkenD;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import jikkenD.SimpleHttpServer.Request;
import jikkenD.SimpleHttpServer.Response;

/**
 * 通常の HTTP リクエストを処理するコントローラ
 */
public class BasicHttpController extends Controller {
    public static String protocolVersion = "HTTP/1.1";

    @Override
    public Response doGet(Request request) {
        Path target = Paths.get(SimpleHttpServer.getDocumentRoot(), request.getTarget()).normalize();

        // ドキュメントルート以下のみアクセス可能にする
        if (!target.startsWith(SimpleHttpServer.getDocumentRoot())) {
            return new Response(protocolVersion, HttpStatus.BadRequest);
        }

        if (Files.isDirectory(target)) {
            target = target.resolve("index.html");
        }

        Response response;
        try {
            response = new Response(protocolVersion, HttpStatus.OK);
            response.setBody(Files.readAllBytes(target));
            response.addHeaderField("Content-Length", Integer.toString(response.getBody().length));

            String ext = StringUtils.getFileExtension(target.getFileName().toString());
            String contentType = SimpleHttpServer.extensionToContentType(ext);

            response.addHeaderField("Content-Type", contentType);
        } catch (IOException e) {
            response = new Response(protocolVersion, HttpStatus.NotFound);
            response.setBody(SimpleHttpServer.readErrorPage(HttpStatus.NotFound));
            response.addHeaderField("Content-Length", Integer.toString(response.getBody().length));
        }

        return response;
    }

    @Override
    public Response doPost(Request request) {
        // nothing to do
        System.out.println("POST body: " + new String(request.getBody(), StandardCharsets.UTF_8));
        Response response = new Response(protocolVersion, HttpStatus.NoContent);
        return response;
    }
}