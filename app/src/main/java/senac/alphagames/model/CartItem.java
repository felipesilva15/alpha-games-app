package senac.alphagames.model;

public class CartItem {
    private int PRODUTO_ID;
    private int USUARIO_ID;
    private int ITEM_QTD;
    private Product product;

    public CartItem(int PRODUTO_ID, int ITEM_QTD) {
        this.PRODUTO_ID = PRODUTO_ID;
        this.ITEM_QTD = ITEM_QTD;
    }

    public int getPRODUTO_ID() {
        return PRODUTO_ID;
    }

    public void setPRODUTO_ID(int PRODUTO_ID) {
        this.PRODUTO_ID = PRODUTO_ID;
    }

    public int getUSUARIO_ID() {
        return USUARIO_ID;
    }

    public void setUSUARIO_ID(int USUARIO_ID) {
        this.USUARIO_ID = USUARIO_ID;
    }

    public int getITEM_QTD() {
        return ITEM_QTD;
    }

    public void setITEM_QTD(int ITEM_QTD) {
        this.ITEM_QTD = ITEM_QTD;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
