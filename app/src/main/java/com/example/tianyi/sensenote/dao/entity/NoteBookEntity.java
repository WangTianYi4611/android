package com.example.tianyi.sensenote.dao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;


@Entity
public class NoteBookEntity {
    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String noteBookName;

    private Date createTime;

    private Date updateTime;

    private Integer isDeleted = 0;

    private Integer count = 0;


    @Generated(hash = 1400891832)
    public NoteBookEntity(Long id, @NotNull String noteBookName, Date createTime,
            Date updateTime, Integer isDeleted, Integer count) {
        this.id = id;
        this.noteBookName = noteBookName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isDeleted = isDeleted;
        this.count = count;
    }

    @Generated(hash = 1580943861)
    public NoteBookEntity() {
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "NoteBookEntity{" +
                "id=" + id +
                ", noteBookName='" + noteBookName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDeleted=" + isDeleted +
                ", count=" + count +
                '}';
    }
}
