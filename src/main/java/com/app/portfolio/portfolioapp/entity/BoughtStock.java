package com.app.portfolio.portfolioapp.entity;

import java.util.List;

public class BoughtStock {
    List<BoughtStockDetails> boughtStockDetails;

    public List<BoughtStockDetails> getBoughtStockDetails() {
        return boughtStockDetails;
    }

    public void setBoughtStockDetails(List<BoughtStockDetails> boughtStockDetails) {
        this.boughtStockDetails = boughtStockDetails;
    }
}

