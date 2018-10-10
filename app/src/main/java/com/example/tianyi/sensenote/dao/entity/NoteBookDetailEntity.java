package com.example.tianyi.sensenote.dao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class NoteBookDetailEntity {

    @Id(autoincrement = true)
    private Long id;

    private Long noteBookId;

    @NotNull
    private String noteBookDetailTitle;

    private String content;

    private Date createTime;

    private Date updateTime;

    private Integer isDeleted = 0;


    @Generated(hash = 436385945)
    public NoteBookDetailEntity(Long id, Long noteBookId,
            @NotNull String noteBookDetailTitle, String content, Date createTime,
            Date updateTime, Integer isDeleted) {
        this.id = id;
        this.noteBookId = noteBookId;
        this.noteBookDetailTitle = noteBookDetailTitle;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isDeleted = isDeleted;
    }

    @Generated(hash = 1847999327)
    public NoteBookDetailEntity() {
    }




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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "NoteBookDetailEntity{" +
                "id=" + id +
                ", noteBookId=" + noteBookId +
                ", noteBookDetailTitle='" + noteBookDetailTitle + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
