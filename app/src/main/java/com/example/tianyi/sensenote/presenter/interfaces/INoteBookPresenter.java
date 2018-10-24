package com.example.tianyi.sensenote.presenter.interfaces;

import com.example.tianyi.sensenote.bean.NoteBookBean;
import com.example.tianyi.sensenote.dao.entity.NoteBookEntity;

import java.util.List;

public interface INoteBookPresenter {

    boolean addNoteBook(String noteBookName);

    void deleteNoteBook(Long id);

    List<NoteBookBean> getAllNoteBook(boolean refresh);

    List<NoteBookBean> getChooseNoteBook();

    List<NoteBookBean> getSearchNoteBook(String noteBookName);

    NoteBookBean getNoteBookById(Long notebookId);

    NoteBookBean getDefaultNoteBook();

}
