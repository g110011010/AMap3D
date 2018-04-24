package com.example.leesanghyuk.POJO;

public class SearchPoemInfo {
    //诗信息的POJO
    private String content;//诗的内容
    private String title;//诗的题目
    private int id;//诗的id号
    private int poet_id;//诗人的id号，索引里面没有存储诗人的名字，需要去数据库中拿

    public SearchPoemInfo() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoet_id() {
        return poet_id;
    }

    public void setPoet_id(int poet_id) {
        this.poet_id = poet_id;
    }
}
