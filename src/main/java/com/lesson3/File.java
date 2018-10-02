package com.lesson3;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonAppend;

import javax.persistence.*;
import java.util.Objects;
import java.util.Random;

@Entity
@Table(name = "\"FILE\"")
public class File{
    private Long id;
    private String name;
    private String format;
    private long size;

    @JsonIgnore
     private Storage storage;

    public File() {
    }

    @Id
    @SequenceGenerator(name = "FILE_SEQ", sequenceName = "FILE_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "FILE_SEQ", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    @Column(name = "\"FORMAT\"")
    public String getFormat() {
        return format;
    }

    @Column(name = "\"SIZE\"")
    public long getSize() {
        return size;
    }


    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "STORAGE_ID")

    public Storage getStorage() {
        return storage;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", format='" + format + '\'' +
                ", size='" + size +'\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return size == file.size &&
                Objects.equals(id, file.id) &&
                Objects.equals(name, file.name) &&
                Objects.equals(format, file.format);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, format, size);
    }
}
