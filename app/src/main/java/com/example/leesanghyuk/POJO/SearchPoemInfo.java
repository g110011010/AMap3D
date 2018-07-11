package com.example.leesanghyuk.POJO;

public class SearchPoemInfo {
    private String content;
    private String title;
    private int id;
    private int poet_id;
    private String dynasty = "唐朝";
    private int age;
    private String city_of_acient;
    private String city_of_current;
    private String county_of_acient;
    private String county_of_current;
    private double lattitude;
    private double longtitude;
    private String name;

    public String getCounty_of_current() {
        return this.county_of_current;
    }

    public void setCounty_of_current(String county_of_current) {
        this.county_of_current = county_of_current;
    }

    public String getDynasty() {
        return this.dynasty;
    }

    public void setDynasty(String dynasty) {
        this.dynasty = dynasty;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity_of_acient() {
        return this.city_of_acient;
    }

    public void setCity_of_acient(String city_of_acient) {
        this.city_of_acient = city_of_acient;
    }

    public String getCity_of_current() {
        return this.city_of_current;
    }

    public void setCity_of_current(String city_of_current) {
        this.city_of_current = city_of_current;
    }

    public String getCounty_of_acient() {
        return this.county_of_acient;
    }

    public void setCounty_of_acient(String county_of_acient) {
        this.county_of_acient = county_of_acient;
    }

    public double getLattitude() {
        return this.lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongtitude() {
        return this.longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SearchPoemInfo() {
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoet_id() {
        return this.poet_id;
    }

    public void setPoet_id(int poet_id) {
        this.poet_id = poet_id;
    }
}
