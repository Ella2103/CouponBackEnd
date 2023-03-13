package jb.CouponsBack.services;


import com.fasterxml.jackson.annotation.JsonValue;

public enum ClientType {
    Admin("admin"),
    Customer("customer"),
    Company("company");

    private String type;

    ClientType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @JsonValue
    public String toJson() {
        return type;
    }
}
