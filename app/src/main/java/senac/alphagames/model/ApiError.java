package senac.alphagames.model;

public class ApiError {
    private int statusCode;
    private String endpoint;
    private String message = "Erro desconhecido.";

    public int getStatusCode() {
        return statusCode;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getMessage() {
        return message;
    }
}
