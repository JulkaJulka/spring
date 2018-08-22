package com.lesson2.item;

import org.springframework.beans.factory.annotation.Autowired;

public class ItemService {
    @Autowired
    ItemDAO itemDAO;

    public ItemService() {
    }

    public Item save(Item item) throws BadRequestException {
        validateItem(item);
        item.validateItemOnExistanceFields(item);

        if (itemDAO.findEqualItemInDB(item) != null)
            throw new BadRequestException("Item already exist in DB");

        return itemDAO.save(item);
    }

    public Item update(Item item) throws BadRequestException {
        validateItem(item);
       item.validateItemOnExistanceFields(item);

        if (itemDAO.findById(item.getId()) == null)
            throw new BadRequestException("Item id " + item.getId() + " doesn't exist in DB");

        return itemDAO.update(item);
    }

    public void delete(long id) throws BadRequestException {
        if(itemDAO.findById(id) == null)
            throw new BadRequestException("Item id " + id + " doesn't exist in DB");
        itemDAO.delete(id);
    }

    public Item findById(Long id) {
        return itemDAO.findById(id);
    }

    public void validateItem(Item item) throws BadRequestException {
        if (item == null)
            throw new BadRequestException("Wrong enter data");

    }

}
