package entity;

public enum OrderStatus {
    WAITING("WAITING"),
    PREPARING("PREPARING"),
    READY("READY"),
    DELIVERED("DELIVERED"),
    DONE("DONE");

    private String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public static OrderStatus getOrderStatus(String value){
        if(value.equals(WAITING)){
            return WAITING;
        }else if(value.equals(PREPARING)){
            return PREPARING;
        }else if(value.equals(READY)){
            return READY;
        }else if(value.equals(DELIVERED)){
            return DELIVERED;
        }else if(value.equals(DONE)){
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
