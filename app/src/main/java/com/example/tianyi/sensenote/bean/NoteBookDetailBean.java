package com.example.tianyi.sensenote.bean;

import com.sendtion.xrichtext.RichTextEditor;

import java.util.Date;
import java.util.List;

public class NoteBookDetailBean {

    private Long id;

    //暂时没用
    private Long noteBookId;

    private String noteBookDetailTitle;

    private Date createTime;

    private List<String> content;

    private NoteBookBean noteBookBean;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoteBookId() {
        return noteBookId;
    }

    public void setNoteBookId(Long noteBookId) {
        this.noteBookId = noteBookId;
    }

    public String getNoteBookDetailTitle() {
        return noteBookDetailTitle;
    }

    public void setNoteBookDetailTitle(String noteBookDetailTitle) {
        this.noteBookDetailTitle = noteBookDetailTitle;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public NoteBookBean getNoteBookBean() {
        return noteBookBean;
    }

    public void setNoteBookBean(NoteBookBean noteBookBean) {
        this.noteBookBean = noteBookBean;
    }

    @Override
    public String toString() {
        return "NoteBookDetailBean{" +
                "id=" + id +
                ", noteBookId=" + noteBookId +
                ", noteBookDetailTitle='" + noteBookDetailTitle + '\'' +
                ", createTime=" + createTime +
                ", content=" + content +
                ", noteBookBean=" + noteBookBean +
                '}';
    }
}
