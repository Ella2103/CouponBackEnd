package jb.CouponsBack.beans;


import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {
    SPORT("sport"),
    TECHNOLOGY("technology"),
    FOOD("food"),
    CLOTHING("clothing"),
    EDUCATION("education");

    private String category;

    Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    @JsonValue
    public String toJson() {
        return category;
    }

}
