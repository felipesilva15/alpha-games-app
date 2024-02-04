package senac.alphagames.model;

import java.util.Date;
import java.util.List;

public class Order {
    private int PEDIDO_ID;
    private int USUARIO_ID;
    private int ENDERECO_ID;
    private Date PEDIDO_DATA;
    private int STATUS_ID;
    private Address address;
    private OrderStatus status;
    private List<OrderItems> items;

    public Order() { }

    public Order(int ENDERECO_ID) {
        this.ENDERECO_ID = ENDERECO_ID;
    }

    public int getPEDIDO_ID() {
        return PEDIDO_ID;
    }

    public int getUSUARIO_ID() {
        return USUARIO_ID;
    }

    public int getENDERECO_ID() {
        return ENDERECO_ID;
    }

    public Date getPEDIDO_DATA() {
        return PEDIDO_DATA;
    }

    public int getSTATUS_ID() {
        return STATUS_ID;
    }

    public Address getAddress() {
        return address;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderItems> getItems() {
        return items;
    }
}
