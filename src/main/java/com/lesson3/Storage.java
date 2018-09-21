package com.lesson3;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "STORAGE")
public class Storage<T> {
    public static long SIZEMAX_STORAGE = 3000;
    private long id;
    private String[] formatsSupported;
    private String storageCountry;
    private long storageSize;

    private List<File> files;

    public Storage() {
    }


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

    @OneToMany(targetEntity = File.class, mappedBy = "storage", fetch = FetchType.LAZY)
    public List<File> getFiles() {
        return files;
    }

    public long getStorageSize() {
        return storageSize;
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
