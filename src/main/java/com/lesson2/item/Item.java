package com.lesson2.item;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.Date;

@Entity
@Table(name = "ITEMS")
public class Item {

    private  Long id;
    private  String name;
    private Date dateCreated;
    private Date lastUpdatedDate;
    private String description;

    public Item() {
    }

    @Id
    @SequenceGenerator(name = "ITM_SEQ", sequenceName = "ITEM_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITM_SEQ")
    @Column(name = "ID")

    public Long getId() {
        return id;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    @Column(name = "DATE_CREATED")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    public Date getDateCreated() {
        return dateCreated;
    }

    @Column(name = "LAST_UPDATED_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    @Column(name="DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateCreated=" + dateCreated +
                ", lastUpdatedDate=" + lastUpdatedDate +
                ", description='" + description + '\'' +
                '}';
    }

    public boolean validateItemOnExistanceFields(Item item) throws BadRequestException {
        try {
            for (Field f : item.getClass().getDeclaredFields()) {
                if (f.get(item) == null) {
                    throw new BadRequestException("Wrong data of field: " + f.getName());

                }
            }
            return true;
        } catch (IllegalAccessException e) {
            e.getMessage();
        }

        return false;
    }
}
