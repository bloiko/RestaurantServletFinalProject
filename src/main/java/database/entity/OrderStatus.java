package database.entity;

import java.io.Serializable;

/**
 * Order status entity.
 *
 * @author B.Loiko
 *
 */
public enum OrderStatus implements Serializable {
    WAITING("WAITING","В очікуванні"),
    PREPARING("PREPARING","Готується"),
    READY("READY","Готове"),
    DELIVERED("DELIVERED","Доставляється"),
    DONE("DONE","Доставлено");

    private String value;
    private String valueUa;
    OrderStatus(String value,String valueUa) {
        this.value = value;
        this.valueUa = valueUa;
    }

    public String getValue() {
        return value;
    }

    public String getValueUa() {
        return valueUa;
    }

    public static OrderStatus getOrderStatus(String value){
        if(value.equals(WAITING.value)){
            return WAITING;
        }else if(value.equals(PREPARING.value)){
            return PREPARING;
        }else if(value.equals(READY.value)){
            return READY;
        }else if(value.equals(DELIVERED.value)){
            return DELIVERED;
        }else if(value.equals(DONE.value)){
            return DONE;
        }
        return DONE;
    }

    public boolean equalsTo(String name) {
        return value.equals(name);
    }

    public String value() {
        return value;
    }
    public String valueUa() {
        return valueUa;
    }
    public OrderStatus nextStatus(){
        if(this.equals(OrderStatus.WAITING)){
            return OrderStatus.PREPARING;
        }else if(this.equals(OrderStatus.PREPARING)){
            return OrderStatus.READY;
        }else if(this.equals(OrderStatus.READY)){
            return OrderStatus.DELIVERED;
        }else if(this.equals(OrderStatus.DELIVERED)){
            return OrderStatus.DONE;
        }
        return DONE;
    }
}
