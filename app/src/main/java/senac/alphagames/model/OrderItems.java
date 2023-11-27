package senac.alphagames.model;

public class OrderItems {
    private int PRODUTO_ID;
    private int ITEM_QTD;
    private int PEDIDO_ID;
    private double ITEM_PRECO;
    private Product product;

    public int getPRODUTO_ID() {
        return PRODUTO_ID;
    }

    public int getITEM_QTD() {
        return ITEM_QTD;
    }

    public int getPEDIDO_ID() {
        return PEDIDO_ID;
    }

    public double getITEM_PRECO() {
        return ITEM_PRECO;
    }

    public Product getProduct() {
        return product;
    }
}
