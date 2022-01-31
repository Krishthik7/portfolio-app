package com.app.portfolio.portfolioapp.web;

import com.app.portfolio.portfolioapp.entity.BoughtStock;
import com.app.portfolio.portfolioapp.entity.BoughtStockDetails;
import com.app.portfolio.portfolioapp.entity.Portfolio;
import com.app.portfolio.portfolioapp.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {
    @Autowired
    PortfolioRepository portfolioRepository;

    @PostConstruct
    public Portfolio createPortfolio() {
        Portfolio portfolio = new Portfolio();
        portfolio.setName("Test User");
        portfolio.setBalance(BigDecimal.ZERO);
        portfolioRepository.save(portfolio);
        return portfolio;
    }

    @GetMapping("/{portfolioId}")
    public Optional<Portfolio> getPortfolio(@PathVariable Long portfolioId) {
        Optional<Portfolio> portfolio = portfolioRepository.findById(portfolioId);
        return portfolio;
    }

    @PutMapping("/add-balance/{portfolioId}/{amount}")
    public void addBalance(@PathVariable long portfolioId, @PathVariable BigDecimal amount) {
        Optional<Portfolio> portfolio = portfolioRepository.findById(portfolioId);
        portfolio.ifPresentOrElse(portfolio1 -> {
            portfolio1.setBalance(portfolio1.getBalance().add(amount));
            portfolioRepository.save(portfolio1);
        }, () -> {
            System.out.println("Portfolio with id " + portfolioId + " doesnt exist");
        });
    }

    @PutMapping("/remove-balance/{portfolioId}/{amount}")
    public void removeBalance(@PathVariable long portfolioId, @PathVariable BigDecimal amount) {
        Optional<Portfolio> portfolio = portfolioRepository.findById(portfolioId);
        portfolio.ifPresentOrElse(portfolio1 -> {
            portfolio1.setBalance(portfolio1.getBalance().subtract(amount));
            portfolioRepository.save(portfolio1);
        }, () -> {
            System.out.println("Portfolio with id " + portfolioId + " doesnt exist");
        });
    }

    @PostMapping("/get-stock/{name}")
    public Stock getStock(@PathVariable String name) throws IOException {
        Stock stock = YahooFinance.get(name, Calendar.getInstance());
        return stock;

    }

    @PostMapping("/buy-stock/{portfolioId}")
    public BoughtStockDetails buyStock(@RequestBody BoughtStockDetails boughtStockDetails, @PathVariable long portfolioId) {
        Optional<Portfolio> portfolio = getPortfolio(portfolioId);
        if (portfolio.isPresent()) {
            Portfolio portfolioObject = portfolio.get();
            BoughtStock boughtStock = portfolioObject.getBoughtStock();
            if (boughtStock == null) {
                boughtStock = new BoughtStock();
                boughtStock.setBoughtStockDetails(new ArrayList<>());
                portfolioObject.setBoughtStock(boughtStock);
            }
            long total = (long) (boughtStockDetails.getQuantity() * boughtStockDetails.getStock().getPrice());
            if (portfolioObject.getBalance().longValue() < total) {
                System.out.println("Balance is less");
            } else {
                portfolioObject.setBalance(portfolioObject.getBalance().subtract(new BigDecimal(total)));
            }
            boughtStock.getBoughtStockDetails().add(boughtStockDetails);
            portfolioRepository.save(portfolioObject);
        } else {
            System.out.println("User portfolio not found");
        }
        return boughtStockDetails;
    }

    @PostMapping("/sell-stock/{portfolioId}/{stockIndex}")
    public void sellStock(@PathVariable long portfolioId, @PathVariable int stockIndex) {
        Optional<Portfolio> portfolio = getPortfolio(portfolioId);
        if (portfolio.isPresent()) {
            Portfolio portfolioObject = portfolio.get();
            BoughtStock boughtStock = portfolioObject.getBoughtStock();
            BoughtStockDetails soldStock = boughtStock.getBoughtStockDetails().get(stockIndex);
            long total = (long) (soldStock.getQuantity() * soldStock.getStock().getPrice());
            portfolioObject.setBalance(portfolioObject.getBalance().add(new BigDecimal(total)));
            boughtStock.getBoughtStockDetails().remove(stockIndex);
            portfolioRepository.save(portfolioObject);
        } else {
            System.out.println("User portfolio not found");
        }
    }
}
