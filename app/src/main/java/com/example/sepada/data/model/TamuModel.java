package com.example.sepada.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TamuModel implements Serializable {
    @SerializedName("id_tamu")
    private String idTamu;

    @SerializedName("id_user")
    private String idUser;

    @SerializedName("tujuan")
    private String tujuan;

    @SerializedName("alasan")
    private String alasan;

    @SerializedName("tanggal")
    private String tanggal;

    @SerializedName("tgl_diajukan")
    private String tglDiajukan;

    @SerializedName("jam")
    private String jam;

    @SerializedName("jumlah")
    private String jumlah;

    @SerializedName("id_status")
    private String idStatus;

    @SerializedName("alasan_verifikasi")
    private String alasanVerifikasi;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("email")
    private String email;

    @SerializedName("id_user_level")
    private String idUserLevel;

    @SerializedName("id_user_detail")
    private String idUserDetail;

    @SerializedName("nama_lengkap")
    private String namaLengkap;

    @SerializedName("id_jenis_kelamin")
    private String idJenisKelamin;

    @SerializedName("no_telp")
    private String noTelp;

    @SerializedName("alamat")
    private String alamat;

    @SerializedName("nip")
    private String nip;

    @SerializedName("pangkat")
    private String pangkat;

    @SerializedName("jabatan")
    private String jabatan;
    @SerializedName("file")
    private String file;
    @SerializedName("nama_anggota")
    private String namaAnggota;

    public TamuModel(String idTamu, String namaAnggota, String file, String idUser, String tujuan, String alasan, String tanggal, String tglDiajukan, String jam, String jumlah, String idStatus, String alasanVerifikasi, String username, String password, String email, String idUserLevel, String idUserDetail, String namaLengkap, String idJenisKelamin, String noTelp, String alamat, String nip, String pangkat, String jabatan) {
        this.idTamu = idTamu;
        this.idUser = idUser;
        this.tujuan = tujuan;
        this.alasan = alasan;
        this.tanggal = tanggal;
        this.tglDiajukan = tglDiajukan;
        this.jam = jam;
        this.jumlah = jumlah;
        this.namaAnggota = namaAnggota;
        this.idStatus = idStatus;
        this.alasanVerifikasi = alasanVerifikasi;
        this.username = username;
        this.password = password;
        this.email = email;
        this.idUserLevel = idUserLevel;
        this.idUserDetail = idUserDetail;
        this.namaLengkap = namaLengkap;
        this.idJenisKelamin = idJenisKelamin;
        this.noTelp = noTelp;
        this.alamat = alamat;
        this.nip = nip;
        this.pangkat = pangkat;
        this.jabatan = jabatan;
        this.file = file;
    }

    public String getIdTamu() {
        return idTamu;
    }

    public void setIdTamu(String idTamu) {
        this.idTamu = idTamu;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public String getAlasan() {
        return alasan;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTglDiajukan() {
        return tglDiajukan;
    }

    public void setTglDiajukan(String tglDiajukan) {
        this.tglDiajukan = tglDiajukan;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(String idStatus) {
        this.idStatus = idStatus;
    }

    public String getAlasanVerifikasi() {
        return alasanVerifikasi;
    }

    public void setAlasanVerifikasi(String alasanVerifikasi) {
        this.alasanVerifikasi = alasanVerifikasi;
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

    public String getIdUserLevel() {
        return idUserLevel;
    }

    public void setIdUserLevel(String idUserLevel) {
        this.idUserLevel = idUserLevel;
    }

    public String getIdUserDetail() {
        return idUserDetail;
    }

    public void setIdUserDetail(String idUserDetail) {
        this.idUserDetail = idUserDetail;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getIdJenisKelamin() {
        return idJenisKelamin;
    }

    public void setIdJenisKelamin(String idJenisKelamin) {
        this.idJenisKelamin = idJenisKelamin;
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getNamaAnggota() {
        return namaAnggota;
    }

    public void setNamaAnggota(String namaAnggota) {
        this.namaAnggota = namaAnggota;
    }
}
