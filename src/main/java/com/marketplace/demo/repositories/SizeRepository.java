package com.marketplace.demo.repositories;

import com.marketplace.demo.entities.ItemSize;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface SizeRepository extends JpaRepository<ItemSize, Long> {
}
