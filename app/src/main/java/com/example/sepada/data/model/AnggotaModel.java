package com.example.sepada.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AnggotaModel implements Serializable {
    @SerializedName("anggota_id")
    private String anggotaId;
    @SerializedName("nama_lengkap")
    private String namaLengkap;
    @SerializedName("divisi_id")
    private String divisiId;
    @SerializedName("no_telp")
    private String noTelp;
    @SerializedName("nama_divisi")
    private String namaDivisi;
    @SerializedName("day")
    private String day;

    public AnggotaModel(String namaLengkap, String day, String anggotaId, String divisiId, String noTelp, String namaDivisi) {
        this.namaLengkap = namaLengkap;
        this.divisiId = divisiId;
        this.noTelp = noTelp;
        this.namaDivisi = namaDivisi;
        this.anggotaId = anggotaId;
        this.day = day;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getDivisiId() {
        return divisiId;
    }

    public void setDivisiId(String divisiId) {
        this.divisiId = divisiId;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getNamaDivisi() {
        return namaDivisi;
    }

    public void setNamaDivisi(String namaDivisi) {
        this.namaDivisi = namaDivisi;
    }

    public String getAnggotaId() {
        return anggotaId;
    }

    public void setAnggotaId(String anggotaId) {
        this.anggotaId = anggotaId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}

