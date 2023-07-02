package com.example.sepada.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DivisiModel implements Serializable {
    @SerializedName("nama_divisi")
    private String namaDivisi;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;

    public DivisiModel(String namaDivisi, String createdAt, String updatedAt) {
        this.namaDivisi = namaDivisi;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getNamaDivisi() {
        return namaDivisi;
    }

    public void setNamaDivisi(String namaDivisi) {
        this.namaDivisi = namaDivisi;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
