package com.app.portfolio.portfolioapp.entity;


public class BoughtStockDetails {
    private long quantity;
    private Stock stock;

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}

