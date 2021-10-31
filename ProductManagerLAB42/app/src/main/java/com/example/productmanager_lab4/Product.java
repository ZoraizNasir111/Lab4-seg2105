package com.example.productmanager_lab4;

public class Product {
    private int _productId;
    private String _productName;
    private double _productPrice;

    public Product(){

    }
    public Product(int productId, String productName, double productPrice){

        _productId= productId;
        _productName = productName;
        _productPrice = productPrice;
    }
    public Product(String productName, double productPrice){
        _productName=productName;
        _productPrice=productPrice;
    }
    public void setID(int id){ _productId=id;}
    public int getID(){return _productId; }
    public void setProductName(String productName){_productName=productName;}
    public String getProductName(){return _productName;}
    public void setPrice(double productPrice){_productPrice=productPrice;}
    public double getPrice(){return _productPrice;}

}
