package senac.alphagames.model;

public class Category {
    private int CATEGORIA_ID;
    private String CATEGORIA_NOME;
    private String CATEGORIA_DESC;
    private int CATEGORIA_ATIVO;

    public Category() {
    }

    public int getCATEGORIA_ID() {
        return CATEGORIA_ID;
    }

    public String getCATEGORIA_NOME() {
        return CATEGORIA_NOME;
    }

    public String getCATEGORIA_DESC() {
        return CATEGORIA_DESC;
    }

    public int getCATEGORIA_ATIVO() {
        return CATEGORIA_ATIVO;
    }
}
