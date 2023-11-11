package senac.alphagames.model;

public class User {
    private int USUARIO_ID;
    private String USUARIO_NOME;
    private String USUARIO_EMAIL;
    private String USUARIO_SENHA;
    private String USUARIO_CPF;

    public User() {
    }

    public User(String USUARIO_NOME, String USUARIO_EMAIL, String USUARIO_SENHA, String USUARIO_CPF) {
        this.USUARIO_NOME = USUARIO_NOME;
        this.USUARIO_EMAIL = USUARIO_EMAIL;
        this.USUARIO_SENHA = USUARIO_SENHA;
        this.USUARIO_CPF = USUARIO_CPF;
    }

    public User(String USUARIO_EMAIL, String USUARIO_SENHA) {
        this.USUARIO_EMAIL = USUARIO_EMAIL;
        this.USUARIO_SENHA = USUARIO_SENHA;
    }

    public int getUSUARIO_ID() {
        return USUARIO_ID;
    }

    public String getUSUARIO_NOME() {
        return USUARIO_NOME;
    }

    public void setUSUARIO_NOME(String USUARIO_NOME) {
        this.USUARIO_NOME = USUARIO_NOME;
    }

    public String getUSUARIO_EMAIL() {
        return USUARIO_EMAIL;
    }

    public void setUSUARIO_EMAIL(String USUARIO_EMAIL) {
        this.USUARIO_EMAIL = USUARIO_EMAIL;
    }

    public String getUSUARIO_SENHA() {
        return USUARIO_SENHA;
    }

    public void setUSUARIO_SENHA(String USUARIO_SENHA) {
        this.USUARIO_SENHA = USUARIO_SENHA;
    }

    public String getUSUARIO_CPF() {
        return USUARIO_CPF;
    }

    public void setUSUARIO_CPF(String USUARIO_CPF) {
        this.USUARIO_CPF = USUARIO_CPF;
    }
}
