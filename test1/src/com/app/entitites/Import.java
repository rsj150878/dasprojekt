package com.app.entitites;

// default package
// Generated 30.09.2019 14:19:33 by Hibernate Tools 5.4.3.Final



/**
 * import generated by hbm2java
 */
public class Import  implements java.io.Serializable {


     private ImportId id;
     private int ringnummer;
     private String richter;
     private String klasse;
     private String rasse;
     private char geschlecht;
     private String hundename;
     private String wurftag;
     private String zuchtbuchnummer;
     private String vater;
     private String mutter;
     private String zuechter;
     private String besitzer;

    public Import() {
    }

    public Import(ImportId id, int ringnummer, String richter, String klasse, String rasse, char geschlecht, String hundename, String wurftag, String zuchtbuchnummer, String vater, String mutter, String zuechter, String besitzer) {
       this.id = id;
       this.ringnummer = ringnummer;
       this.richter = richter;
       this.klasse = klasse;
       this.rasse = rasse;
       this.geschlecht = geschlecht;
       this.hundename = hundename;
       this.wurftag = wurftag;
       this.zuchtbuchnummer = zuchtbuchnummer;
       this.vater = vater;
       this.mutter = mutter;
       this.zuechter = zuechter;
       this.besitzer = besitzer;
    }
   
    public ImportId getId() {
        return this.id;
    }
    
    public void setId(ImportId id) {
        this.id = id;
    }
    public int getRingnummer() {
        return this.ringnummer;
    }
    
    public void setRingnummer(int ringnummer) {
        this.ringnummer = ringnummer;
    }
    public String getRichter() {
        return this.richter;
    }
    
    public void setRichter(String richter) {
        this.richter = richter;
    }
    public String getKlasse() {
        return this.klasse;
    }
    
    public void setKlasse(String klasse) {
        this.klasse = klasse;
    }
    public String getRasse() {
        return this.rasse;
    }
    
    public void setRasse(String rasse) {
        this.rasse = rasse;
    }
    public char getGeschlecht() {
        return this.geschlecht;
    }
    
    public void setGeschlecht(char geschlecht) {
        this.geschlecht = geschlecht;
    }
    public String getHundename() {
        return this.hundename;
    }
    
    public void setHundename(String hundename) {
        this.hundename = hundename;
    }
    public String getWurftag() {
        return this.wurftag;
    }
    
    public void setWurftag(String wurftag) {
        this.wurftag = wurftag;
    }
    public String getZuchtbuchnummer() {
        return this.zuchtbuchnummer;
    }
    
    public void setZuchtbuchnummer(String zuchtbuchnummer) {
        this.zuchtbuchnummer = zuchtbuchnummer;
    }
    public String getVater() {
        return this.vater;
    }
    
    public void setVater(String vater) {
        this.vater = vater;
    }
    public String getMutter() {
        return this.mutter;
    }
    
    public void setMutter(String mutter) {
        this.mutter = mutter;
    }
    public String getZuechter() {
        return this.zuechter;
    }
    
    public void setZuechter(String zuechter) {
        this.zuechter = zuechter;
    }
    public String getBesitzer() {
        return this.besitzer;
    }
    
    public void setBesitzer(String besitzer) {
        this.besitzer = besitzer;
    }




}
