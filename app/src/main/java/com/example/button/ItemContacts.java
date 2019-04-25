package com.example.button;


public class ItemContacts {
    private String nama;
    private String notelp;
    private String user;

    public ItemContacts(String n, String i,String user) {
        setNama(n);
        setNotelp(i);
        setUser(user);
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNotelp() {
        return notelp;
    }

    public void setNotelp(String notelp) {
        this.notelp = notelp;
    }
    public void setUser(String u)
    {
        this.user=u;
    }
    public String getUser() {return user;}
}
