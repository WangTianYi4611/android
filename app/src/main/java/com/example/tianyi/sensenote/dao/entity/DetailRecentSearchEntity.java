package com.example.tianyi.sensenote.dao.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DetailRecentSearchEntity {
    @Id(autoincrement = true)
    private Long id;

    private String searchString;

    private Date createTime;

    private Date updateTime;

    private Integer isDeleted = 0;


    @Generated(hash = 408982010)
    public DetailRecentSearchEntity(Long id, String searchString, Date createTime,
            Date updateTime, Integer isDeleted) {
        this.id = id;
        this.searchString = searchString;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isDeleted = isDeleted;
    }

    @Generated(hash = 324509932)
    public DetailRecentSearchEntity() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
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
}
