package com.example.inventorymanagement;

public class Inventory {

    private String productname,suppliername,mobile;
    private int id,price,quantity;

    public Inventory(String product_name, String supplier_name, String mobile, int id, int price, int quantity) {
        this.productname = product_name;
        this.suppliername = supplier_name;
        this.mobile = mobile;
        this.id = id;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProduct_name() {
        return productname;
    }

    public void setProduct_name(String product_name) {
        this.productname = product_name;
    }

    public String getSupplier_name() {
        return suppliername;
    }

    public void setSupplier_name(String supplier_name) {
        this.suppliername = supplier_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
