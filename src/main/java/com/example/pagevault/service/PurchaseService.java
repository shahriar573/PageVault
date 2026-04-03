package com.example.pagevault.service;

import com.example.pagevault.model.Purchase;
import com.example.pagevault.model.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    public Purchase savePurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    public List<Purchase> getAllPurchasesLatestFirst() {
        return purchaseRepository.findAll(Sort.by(Sort.Direction.DESC, "purchaseDate"));
    }

    public List<Purchase> getPurchasesForUser(String username) {
        return purchaseRepository.findByUserUsernameOrderByPurchaseDateDesc(username);
    }

    public Optional<Purchase> getPurchaseById(Long id) {
        return purchaseRepository.findById(id);
    }

    public void deletePurchase(Long id) {
        purchaseRepository.deleteById(id);
    }
}

