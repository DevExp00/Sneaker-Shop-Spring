package com.marketplace.demo.services;

import com.marketplace.demo.entities.Categories;
import com.marketplace.demo.entities.Countries;
import com.marketplace.demo.entities.ItemSize;
import com.marketplace.demo.entities.ShopItems;
import org.hibernate.engine.jdbc.Size;

import java.util.List;

public interface ItemService {

    //Items
    ShopItems addItem(ShopItems item);
    List<ShopItems> getAllItems();
    ShopItems getItem(Long id);
    void deleteItem(ShopItems item);
    ShopItems saveItem(ShopItems item);


    //Countries
    List<Countries> getAllCountries();
    Countries addCountry(Countries country);
    Countries saveCountry(Countries country);
    Countries getCountry(Long id);


    //Categories
    List<Categories> getAllCategories();
    Categories getCategory(Long id);
    Categories addCategory(Categories category);
    Categories saveCategory(Categories category);


    //Size
    List<ItemSize> getAllSizes();
    ItemSize getSize(Long id);
    ItemSize saveSize(ItemSize size);



}
