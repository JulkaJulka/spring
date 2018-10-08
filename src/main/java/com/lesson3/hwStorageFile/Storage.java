package com.lesson3.hwStorageFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "STORAGE")
public class Storage {

    private long id;
    private String formatsSupported;
    private String storageCountry;
    private long storageSize;

    @JsonIgnore
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
    public String getFormatsSupported() {
        return formatsSupported;
    }

    public void setFormatsSupported(String formatsSupported) {
        this.formatsSupported = formatsSupported;
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
                Objects.equals(formatsSupported, storage.formatsSupported) &&
                Objects.equals(storageCountry, storage.storageCountry);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, formatsSupported, storageCountry, storageSize);
    }

    @Override
    public String toString() {
        return "Storage{" +
                "id=" + id +
                ", formatsSupported='" + formatsSupported + '\'' +
                ", storageCountry='" + storageCountry + '\'' +
                ", storageSize=" + storageSize +
                '}';
    }
}
