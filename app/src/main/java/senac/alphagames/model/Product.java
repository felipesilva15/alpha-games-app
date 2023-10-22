package senac.alphagames.model;

import java.util.List;

public class Product {
    private int PRODUTO_ID;
    private String PRODUTO_NOME;
    private String PRODUTO_DESC;
    private double PRODUTO_PRECO;
    private double PRODUTO_DESCONTO;
    private int CATEGORIA_ID;
    private int PRODUTO_ATIVO;
    private Category CATEGORIA;
    private List<ProductImage> PRODUTO_IMAGEM;

    public Product() {
    }

    public int getPRODUTO_ID() {
        return PRODUTO_ID;
    }

    public String getPRODUTO_NOME() {
        return PRODUTO_NOME;
    }

    public String getPRODUTO_DESC() {
        return PRODUTO_DESC;
    }

    public double getPRODUTO_PRECO() {
        return PRODUTO_PRECO;
    }

    public double getPRODUTO_DESCONTO() {
        return PRODUTO_DESCONTO;
    }

    public int getCATEGORIA_ID() {
        return CATEGORIA_ID;
    }

    public int getPRODUTO_ATIVO() {
        return PRODUTO_ATIVO;
    }

    public Category getCATEGORIA() {
        return CATEGORIA;
    }

    public List<ProductImage> getPRODUTO_IMAGEM() {
        return PRODUTO_IMAGEM;
    }
}
