package com.lesson3;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "STORAGE")
public class Storage<T> {
    //public  long SIZEMAX_STORAGE ;
    private long id;
    private String[] formatsSupported;
    private String storageCountry;
    private long storageSize;

    private List<File> files;

    public Storage() {
    }

    /*public long getSIZEMAX_STORAGE() {
        return SIZEMAX_STORAGE = 3000;
    }

    public void setSIZEMAX_STORAGE(long SIZEMAX_STORAGE) {
        this.SIZEMAX_STORAGE = SIZEMAX_STORAGE;
    }*/

    @Id
    @SequenceGenerator(name = "ST_SEQ", sequenceName = "STORAGE_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "ST_SEQ", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public long getId() {
        return id;
    }

    @Column(name = "FORMATS_SUPPORTED")
    public String[] getFormatsSupported() {
        return formatsSupported;
    }

    public String[] getFormatsSupported(String str) {
        String[] format = str.split(",");
        return format;
    }

    public String getFormatsSupportedString() {
        String string = new String("");
        for (String el : this.getFormatsSupported()) {
            string = string + el + ",";
        }
        return string.substring(0, string.length() - 1);
    }

    @Column(name = "STORAGE_COUNTRY")
    public String getStorageCountry() {
        return storageCountry;
    }

    @Column(name = "STORAGE_SIZE")
    public long getStorageSize() {
        return storageSize;
    }

    @OneToMany(targetEntity = File.class, mappedBy = "storage", fetch = FetchType.LAZY)
    public List<File> getFiles() {
        return files;
    }



    public void setId(long id) {
        this.id = id;
    }

    public void setFormatsSupported(String[] formatsSupported) {
        this.formatsSupported = formatsSupported;
    }



    public void setStorageCountry(String storageCountry) {
        this.storageCountry = storageCountry;
    }

    public void setStorageSize(long storageSize) {
        this.storageSize = storageSize;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Storage storage = (Storage) o;
        return id == storage.id &&
                storageSize == storage.storageSize &&
                Arrays.equals(formatsSupported, storage.formatsSupported) &&
                Objects.equals(storageCountry, storage.storageCountry);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(id, storageCountry);
        result = 31 * result + Arrays.hashCode(formatsSupported);
        return result;
    }
}
