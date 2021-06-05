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

    public String value() {
        return value;
    }
   }
