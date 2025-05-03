package com.erpnext.dto;

public class RateUpdateRequestDTO {
    private String quotationName;

    private String itemName;
    private String itemCode;
    private double newRate;

    private String uom;
    private double qty;

    public String getItemName() {
        return  itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    // Getters et setters
    public String getQuotationName() {
        return quotationName;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }
    public void setQuotationName(String quotationName) {
        this.quotationName = quotationName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public double getNewRate() {
        return newRate;
    }

    public void setNewRate(double newRate) {
        this.newRate = newRate;
    }
}
