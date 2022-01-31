package com.app.portfolio.portfolioapp.entity;


import com.app.portfolio.portfolioapp.util.BoughtStockConverter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private BigDecimal balance;
    @Lob
    @Convert(converter = BoughtStockConverter.class)
    private BoughtStock boughtStock;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BoughtStock getBoughtStock() {
        return boughtStock;
    }

    public void setBoughtStock(BoughtStock boughtStock) {
        this.boughtStock = boughtStock;
    }
}
