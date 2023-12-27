package com.marketplace.demo.services.impl;

import com.marketplace.demo.entities.Categories;
import com.marketplace.demo.entities.Countries;
import com.marketplace.demo.entities.ItemSize;
import com.marketplace.demo.entities.ShopItems;
import com.marketplace.demo.repositories.CategoryRepository;
import com.marketplace.demo.repositories.CountryRepository;
import com.marketplace.demo.repositories.ShopItemRepository;
import com.marketplace.demo.repositories.SizeRepository;
import com.marketplace.demo.services.ItemService;
import org.hibernate.engine.jdbc.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ShopItemRepository shopItemRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SizeRepository sizeRepository;



    @Override
    public ShopItems addItem(ShopItems item) {
        return shopItemRepository.save(item);
    }

    @Override
    public List<ShopItems> getAllItems() {
        return shopItemRepository.findAllByAmountGreaterThan(0);
    }

    @Override
    public ShopItems getItem(Long id) {
        return shopItemRepository.getOne(id);
    }

    @Override
    public void deleteItem(ShopItems item) {
        shopItemRepository.delete(item);
    }

    @Override
    public ShopItems saveItem(ShopItems item) {
        return shopItemRepository.save(item);
    }

    @Override
    public List<Countries> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Countries addCountry(Countries country) {
        return countryRepository.save(country);
    }

    @Override
    public Countries saveCountry(Countries country) {
        return countryRepository.save(country);
    }

    @Override
    public Countries getCountry(Long id) {
        return countryRepository.getOne(id);
    }

    @Override
    public List<Categories> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Categories getCategory(Long id) {
        return categoryRepository.getOne(id);
    }

    @Override
    public Categories addCategory(Categories category) {
        return categoryRepository.save(category);
    }

    @Override
    public Categories saveCategory(Categories category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<ItemSize> getAllSizes() {
        return sizeRepository.findAll();
    }

    @Override
    public ItemSize getSize(Long id) {
        return sizeRepository.getReferenceById(id);
    }

    @Override
    public ItemSize saveSize(ItemSize size) {
        return sizeRepository.save(size);
    }

}
