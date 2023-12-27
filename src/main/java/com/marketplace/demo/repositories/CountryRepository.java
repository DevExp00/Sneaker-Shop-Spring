package com.marketplace.demo.repositories;

import com.marketplace.demo.entities.Countries;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface CountryRepository extends JpaRepository<Countries, Long> {

}
