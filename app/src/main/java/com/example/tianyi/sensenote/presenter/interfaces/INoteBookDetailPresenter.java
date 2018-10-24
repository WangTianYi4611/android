package com.example.tianyi.sensenote.presenter.interfaces;

import com.example.tianyi.sensenote.bean.NoteBookDetailBean;
import com.sendtion.xrichtext.RichTextEditor;

import java.util.List;

public interface INoteBookDetailPresenter {

    boolean addNoteBookDetail(List<RichTextEditor.EditData> editData, String title,Long noteBookId);


    List<NoteBookDetailBean> getAllNoteBookDetails();

    List<NoteBookDetailBean> getNoteBookDetailsById(Long noteBookId);

    NoteBookDetailBean getNoteBookDetailByDetailId(Long noteBookDetailId);

    boolean updateNoteBookDetail(List<RichTextEditor.EditData> editData, String title, Long id, Long mNoteBookDetailId);

    Integer getNoteBookDetailsCount();

    List<NoteBookDetailBean> getNoteBookDetailsBySearchString(String searchString,int from,int size);

}
