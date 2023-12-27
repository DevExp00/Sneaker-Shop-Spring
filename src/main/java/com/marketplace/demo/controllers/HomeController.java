package com.marketplace.demo.controllers;

import com.marketplace.demo.entities.*;
import com.marketplace.demo.services.ItemService;
import com.marketplace.demo.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Value("${file.photo.viewPath}")
    private String viewPath;

    @Value("${file.photo.uploadPath}")
    private String uploadPath;

    @Value("${file.photo.defaultPicture}")
    private String defaultPicture;

    @GetMapping(value = "/")
    public String index(Model model){
        model.addAttribute("currentUser",getUserData());

        List<ShopItems> items = itemService.getAllItems();
        model.addAttribute("tovary", items);

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);

        return "index";
    }

    @GetMapping(value = "/items")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String items(Model model){

        model.addAttribute("currentUser",getUserData());

        List<ShopItems> items = itemService.getAllItems();
        model.addAttribute("tovary", items);

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);
        return "items";
    }

    @GetMapping(value = "/about")
    public String about(Model model){
        model.addAttribute("currentUser",getUserData());
        return "about";
    }

    @GetMapping(value ="/addItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public  String addItem(Model model){

        model.addAttribute("currentUser",getUserData());

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);
        return "addItem";
    }

    @PostMapping(value = "/addItems")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addItem(@RequestParam(name = "category_id", defaultValue = "0") List<Long> categoryId,
                          @RequestParam(name = "country_id", defaultValue = "0") Long countryId,
                          @RequestParam(name = "item_name", defaultValue = "No") String name,
                          @RequestParam(name = "item_price", defaultValue = "0") int price,
                          @RequestParam(name = "item_amount", defaultValue = "0") int amount,
                          @RequestParam(name = "photo") MultipartFile file) {
        try {
            Countries countries = itemService.getCountry(countryId);
            if (countries != null) {
                // Сохранение файла
                String picName = DigestUtils.sha1Hex("item_" + System.currentTimeMillis() + "_!Picture");
                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPath + picName + ".jpg");
                Files.write(path, bytes);

                // Создание товара
                ShopItems items = new ShopItems();
                items.setName(name);
                items.setPrice(price);
                items.setAmount(amount);
                items.setCountry(countries);
                items.setImg(picName);
                List<Categories> categories = new ArrayList<>();
                for (Long Ids : categoryId) {
                    Categories category = itemService.getCategory(Ids);
                    if (category != null) {
                        categories.add(category);
                    }
                }
                items.setCategory(categories);
                items.setImg(picName); // Сохранение имени файла в поле товара
                itemService.addItem(items);
            }
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/?errorUpload";  // Обработка ошибки загрузки
        }
    }


    @PostMapping(value = "/saveItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String saveItem(@RequestParam(name = "category_id") List<Long> categoryId,
                           @RequestParam(name = "country_id") Long country_id,
                           @RequestParam(name = "item_id") Long id,
                           @RequestParam(name = "item_name") String name,
                           @RequestParam(name = "item_price") int price,
                           @RequestParam(name = "item_amount") int amount,
                           @RequestParam(name = "item_img") MultipartFile imageFile) {

        ShopItems item = itemService.getItem(id);
        Countries countries = itemService.getCountry(country_id);

        if (item != null && countries != null) {
            item.setName(name);
            item.setPrice(price);
            item.setAmount(amount);
            item.setCountry(countries);

            List<Categories> categories = new ArrayList<>();
            for (Long Ids : categoryId) {
                Categories category = itemService.getCategory(Ids);
                if (category != null) {
                    categories.add(category);
                }
            }
            item.setCategory(categories);

            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    String imageName = DigestUtils.sha1Hex("item_" + id + "_!Picture");

                    byte[] imageBytes = imageFile.getBytes();

                    Path imagePath = Paths.get(uploadPath + imageName + ".jpg");

                    Files.write(imagePath, imageBytes);

                    item.setImg(imageName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            itemService.saveItem(item);
        }

        return "redirect:/";
    }


    @PostMapping(value = "/deleteItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String deleteItem(@RequestParam(name = "item_id") Long id){
        ShopItems item = itemService.getItem(id);
        if(item != null){
            itemService.deleteItem(item);
        }
        return "redirect:/";
    }

    @GetMapping(value = "/details/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String details(Model model , @PathVariable(name = "id") Long id){

        model.addAttribute("currentUser",getUserData());

        ShopItems items = itemService.getItem(id);
        model.addAttribute("items", items);

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);

        return "details";
    }

    @GetMapping(value = "/info/{id}")
    public String info(Model model , @PathVariable(name = "id") Long id){

        model.addAttribute("currentUser",getUserData());

        ShopItems items = itemService.getItem(id);
        model.addAttribute("items", items);

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);

        List<ItemSize> sizes = itemService.getAllSizes();
        model.addAttribute("sizes", sizes);

        return "info";
    }


    private Users getUserData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User) authentication.getPrincipal();
            Users myUser = userService.getUserByEmail(secUser.getUsername());
            return myUser;
        }
        return null;
    }

}
