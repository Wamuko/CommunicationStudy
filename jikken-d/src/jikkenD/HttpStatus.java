package jikkenD;

public enum HttpStatus {
    OK(200, "OK"),
    NoContent(204, "No Content"),
    BadRequest(400, "Bad Request"),
    NotFound(404, "Not Found");

    private int code;
    private String reasonPhrase;
    private HttpStatus(int code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }
    public int getCode() {
        return code;
    }
    public String getName() {
        return reasonPhrase;
    }
}