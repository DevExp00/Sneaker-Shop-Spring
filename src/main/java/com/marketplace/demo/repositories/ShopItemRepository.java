package com.marketplace.demo.repositories;

import com.marketplace.demo.entities.ShopItems;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ShopItemRepository extends JpaRepository<ShopItems, Long> {
    List<ShopItems> findAllByAmountGreaterThan(int amount);
}
