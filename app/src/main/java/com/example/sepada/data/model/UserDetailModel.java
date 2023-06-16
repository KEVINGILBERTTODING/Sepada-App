package com.example.sepada.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDetailModel implements Serializable {
    @SerializedName("id")
    String id;

    @SerializedName("id_user_detail")
    String userDetailId;

    @SerializedName("nama_lengkap")
    String namaLengkap;

    @SerializedName("id_jenis_kelamin")
    String jenisKelaminId;

    @SerializedName("no_telp")
    String noTelp;

    @SerializedName("alamat")
    String alamat;

    @SerializedName("nip")
    String nip;

    @SerializedName("pangkat")
    String pangkat;

    @SerializedName("jabatan")
    String jabatan;

    @SerializedName("id_user")
    String userId;

    @SerializedName("username")
    String username;

    @SerializedName("password")
    String password;

    @SerializedName("email")
    String email;

    @SerializedName("id_user_level")
    String userLevelId;

    public UserDetailModel(String id, String userDetailId, String namaLengkap, String jenisKelaminId, String noTelp, String alamat, String nip, String pangkat, String jabatan, String userId, String username, String password, String email, String userLevelId) {
        this.id = id;
        this.userDetailId = userDetailId;
        this.namaLengkap = namaLengkap;
        this.jenisKelaminId = jenisKelaminId;
        this.noTelp = noTelp;
        this.alamat = alamat;
        this.nip = nip;
        this.pangkat = pangkat;
        this.jabatan = jabatan;
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.userLevelId = userLevelId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserDetailId() {
        return userDetailId;
    }

    public void setUserDetailId(String userDetailId) {
        this.userDetailId = userDetailId;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getJenisKelaminId() {
        return jenisKelaminId;
    }

    public void setJenisKelaminId(String jenisKelaminId) {
        this.jenisKelaminId = jenisKelaminId;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getPangkat() {
        return pangkat;
    }

    public void setPangkat(String pangkat) {
        this.pangkat = pangkat;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserLevelId() {
        return userLevelId;
    }

    public void setUserLevelId(String userLevelId) {
        this.userLevelId = userLevelId;
    }
}
