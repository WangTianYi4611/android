package com.example.tianyi.sensenote.presenter.interfaces;

import com.example.tianyi.sensenote.bean.NoteBookDetailBean;
import com.sendtion.xrichtext.RichTextEditor;

import java.util.List;

public interface INoteBookDetailPresenter {

    boolean addNoteBookDetail(List<RichTextEditor.EditData> editData, String title,Long noteBookId);


    List<NoteBookDetailBean> getAllNoteBookDetails();

    List<NoteBookDetailBean> getNoteBookDetailsById(Long noteBookId);
}
