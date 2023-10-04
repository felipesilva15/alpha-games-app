package senac.alphagames.model;

public class ApiError {
    private String code;
    private String endpoint;
    private String message = "Erro desconhecido.";

    public String getCode() {
        return code;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getMessage() {
        return message;
    }
}
