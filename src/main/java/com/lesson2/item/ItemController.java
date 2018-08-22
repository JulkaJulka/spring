package com.lesson2.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class ItemController {
    @Autowired
    ItemService itemService;

    @RequestMapping(method = RequestMethod.GET, value = "/item")
    protected @ResponseBody
    String getItem(@RequestParam("id") Long idItem) {
        try {
            Item item = itemService.findById(idItem);
            if (item == null)
                return "Item with id " + idItem + " doesn't exist in DB";
            return item.toString();

        } catch (HibernateException e) {
            e.printStackTrace();
            return "Getting unsuccessful " + e.getMessage();
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/createItem", produces = "text/plain")
    protected @ResponseBody
    String createItem(@RequestBody String str) {

        try {
            Item item = convertJSONStringToObject(str);
            itemService.save(item);

            return item.toString();

        } catch (BadRequestException | HibernateException | IOException e) {
            e.printStackTrace();
            return "Saving unsuccessful " + e.getMessage();
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/updateItem", produces = "text/plain")
    protected @ResponseBody
    String updateItem(@RequestBody String req) {

        try {
            Item itemUpdate = convertJSONStringToObject(req);
            itemService.update(itemUpdate);
            return itemUpdate.toString();
        } catch (BadRequestException | HibernateException | IOException e) {
            e.printStackTrace();
            return "Updating unsuccessful " + e.getMessage();
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteItem", produces = "text/plain")
    protected @ResponseBody
    String deleteItem(@RequestParam Long id) {

        try {
            itemService.delete(id);
            return "Item id " + id + " was deleted successfully";
        } catch (BadRequestException | HibernateException e) {
            e.printStackTrace();
            return "Deleting unsuccessful " + e.getMessage();
        }
    }

    private Item convertJSONStringToObject(String str) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Item item = mapper.readValue(str, Item.class);

        return item;
    }

}
