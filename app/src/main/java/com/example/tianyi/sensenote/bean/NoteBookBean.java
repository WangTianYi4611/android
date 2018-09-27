package com.example.tianyi.sensenote.bean;

public class NoteBookBean {

    private Long id;
    private String noteBookName;
    private Integer count;

    public NoteBookBean() {
    }

    public NoteBookBean(Long id, String noteBookName, Integer count) {
        this.id = id;
        this.noteBookName = noteBookName;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNoteBookName() {
        return noteBookName;
    }

    public void setNoteBookName(String noteBookName) {
        this.noteBookName = noteBookName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
