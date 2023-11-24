package senac.alphagames.model;

public class Address {
    private int ENDERECO_ID;
    private String ENDERECO_CEP;
    private String ENDERECO_NOME;
    private String ENDERECO_LOGRADOURO;
    private String ENDERECO_NUMERO;
    private String ENDERECO_COMPLEMENTO;
    private String ENDERECO_CIDADE;
    private String ENDERECO_ESTADO;
    private int USUARIO_ID;
    private User user;

    public Address(String ENDERECO_CEP, String ENDERECO_NOME, String ENDERECO_LOGRADOURO, String ENDERECO_NUMERO, String ENDERECO_COMPLEMENTO, String ENDERECO_CIDADE, String ENDERECO_ESTADO) {
        this.ENDERECO_CEP = ENDERECO_CEP;
        this.ENDERECO_NOME = ENDERECO_NOME;
        this.ENDERECO_LOGRADOURO = ENDERECO_LOGRADOURO;
        this.ENDERECO_NUMERO = ENDERECO_NUMERO;
        this.ENDERECO_COMPLEMENTO = ENDERECO_COMPLEMENTO;
        this.ENDERECO_CIDADE = ENDERECO_CIDADE;
        this.ENDERECO_ESTADO = ENDERECO_ESTADO;
    }

    public int getENDERECO_ID() {
        return ENDERECO_ID;
    }

    public void setENDERECO_ID(int ENDERECO_ID) {
        this.ENDERECO_ID = ENDERECO_ID;
    }

    public String getENDERECO_CEP() {
        return ENDERECO_CEP;
    }

    public void setENDERECO_CEP(String ENDERECO_CEP) {
        this.ENDERECO_CEP = ENDERECO_CEP;
    }

    public String getENDERECO_NOME() {
        return ENDERECO_NOME;
    }

    public void setENDERECO_NOME(String ENDERECO_NOME) {
        this.ENDERECO_NOME = ENDERECO_NOME;
    }

    public String getENDERECO_LOGRADOURO() {
        return ENDERECO_LOGRADOURO;
    }

    public void setENDERECO_LOGRADOURO(String ENDERECO_LOGRADOURO) {
        this.ENDERECO_LOGRADOURO = ENDERECO_LOGRADOURO;
    }

    public String getENDERECO_NUMERO() {
        return ENDERECO_NUMERO;
    }

    public void setENDERECO_NUMERO(String ENDERECO_NUMERO) {
        this.ENDERECO_NUMERO = ENDERECO_NUMERO;
    }

    public String getENDERECO_COMPLEMENTO() {
        return ENDERECO_COMPLEMENTO;
    }

    public void setENDERECO_COMPLEMENTO(String ENDERECO_COMPLEMENTO) {
        this.ENDERECO_COMPLEMENTO = ENDERECO_COMPLEMENTO;
    }

    public String getENDERECO_CIDADE() {
        return ENDERECO_CIDADE;
    }

    public void setENDERECO_CIDADE(String ENDERECO_CIDADE) {
        this.ENDERECO_CIDADE = ENDERECO_CIDADE;
    }

    public String getENDERECO_ESTADO() {
        return ENDERECO_ESTADO;
    }

    public void setENDERECO_ESTADO(String ENDERECO_ESTADO) {
        this.ENDERECO_ESTADO = ENDERECO_ESTADO;
    }

    public int getUSUARIO_ID() {
        return USUARIO_ID;
    }

    public void setUSUARIO_ID(int USUARIO_ID) {
        this.USUARIO_ID = USUARIO_ID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
