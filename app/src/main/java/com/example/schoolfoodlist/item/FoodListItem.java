package com.example.schoolfoodlist.item;

public class FoodListItem {
    String fDay;
    String fList;

    public FoodListItem(String day, String list){
        this.fDay = day;
        this.fList = list;
    }

    public String getfDay() {
        return fDay;
    }

    public String getfList() {
        return fList;
    }

    public void setfDay(String fDay) {
        this.fDay = fDay;
    }

    public void setfList(String fList) {
        this.fList = fList;
    }
}
