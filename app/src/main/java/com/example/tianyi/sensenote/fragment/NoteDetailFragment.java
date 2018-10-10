package com.example.tianyi.sensenote.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tianyi.sensenote.R;
import com.example.tianyi.sensenote.activity.LoginActivity;
import com.example.tianyi.sensenote.activity.MainActivity;
import com.example.tianyi.sensenote.activity.NoteDetailActivity;
import com.example.tianyi.sensenote.bean.NoteBookBean;
import com.example.tianyi.sensenote.presenter.impl.NoteBookDetailPresenter;
import com.example.tianyi.sensenote.presenter.impl.NoteBookPresenter;
import com.example.tianyi.sensenote.presenter.interfaces.INoteBookDetailPresenter;
import com.example.tianyi.sensenote.presenter.interfaces.INoteBookPresenter;
import com.example.tianyi.sensenote.util.PhotoUtil;
import com.example.tianyi.sensenote.util.StringUtil;
import com.example.tianyi.sensenote.util.ToastUtil;
import com.sendtion.xrichtext.RichTextEditor;

import java.io.File;
import java.io.FileNotFoundException;

public class NoteDetailFragment extends BaseFragment{

    private static final String ARG_NOTE_BOOK_ID = "noteBookId";
    private static final String ARG_NOTE_DETAIL_ID = "noteDetailId";
    private static final String ARG_NOTE_BOOK = "noteBook";
    private static final int RC_CAMERA = 1;
    private static final int RC_ALBUM = 2;
    private static final int CROP_REQUEST_CODE = 3;
    private TextView notebookTxtView;
    private Button addImgBtn;
    private RichTextEditor richTextEditor;
    private EditText noteBookDetailTitle;
    private File lastInsertFile;
    private boolean mHasLoadedOnce;
    private INoteBookPresenter noteBookPresenter;
    private INoteBookDetailPresenter noteBookDetailPresenter;
    private NoteBookBean mNoteBook;


    public static NoteDetailFragment newInstance(NoteBookBean noteBook, Long noteBookDetailId){
        Bundle bundle = new Bundle();
        if(noteBook != null){
            bundle.putSerializable(ARG_NOTE_BOOK,noteBook);
        }
        if(noteBookDetailId != null){
            bundle.putLong(ARG_NOTE_DETAIL_ID, noteBookDetailId);
        }
        NoteDetailFragment fragment = new NoteDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.common_finish_menu, menu);
    }

    private void setActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("");
        //actionBar.setIcon(R.drawable.ic_note_back);
        actionBar.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {   //返回键的id
                getActivity().finish();
                break;
            }
            case R.id.action_finish:{
                processCreateNoteBook();
                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void processCreateNoteBook() {
        String title = noteBookDetailTitle.getText().toString();
        if(StringUtil.isEmpty(title)){
            ToastUtil.toastMsgShort(getContext(),"标题不能为空");
            return;
        }
        boolean result = noteBookDetailPresenter.addNoteBookDetail(richTextEditor.buildEditData(),title,mNoteBook.getId());
        if(!result){
            ToastUtil.toastMsgShort(getContext(),"插入失败 联系管理员");
        }
        getActivity().finish();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            // 需要inflate一个布局文件 填充Fragment
            mView = inflater.inflate(R.layout.fragment_note_detail, container, false);
            initView();
            initPresenter();
//        实现懒加载
            lazyLoad();
        }
        //缓存的mView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个mView已经有parent的错误。
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }

        return mView;
    }

    private void initPresenter() {
        noteBookPresenter = NoteBookPresenter.getInstance();
        noteBookDetailPresenter = new NoteBookDetailPresenter(getContext());
    }

    private void initView() {
        noteBookDetailTitle = find(R.id.edtTxt_notedetail_title);
        notebookTxtView = find(R.id.textview_notedetail_notebook);
        addImgBtn = find(R.id.btn_notedetail_addImg);
        richTextEditor = find(R.id.rtextet_notedetail);
        addImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processAddImgBtnClick();
            }
        });
        notebookTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NoteDetailActivity) getActivity()).setChooseNoteBookFragement(notebookTxtView.getText().toString());
            }
        });
    }

    private void processAddImgBtnClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("请选择");
        builder.setItems(new String[]{"使用相机拍照", "从相册中选择"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lastInsertFile = PhotoUtil.generateRandomPhotoFile(getContext());
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.example.tianyi.sensenote.fileprovieder", lastInsertFile);
                switch (which) {
                    case 0: {                     //相机
                        Intent intent_camera = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent_camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent_camera, RC_CAMERA);
                        break;
                    }
                    case 1: {
                        Intent intent_album = new Intent("android.intent.action.GET_CONTENT");
                        intent_album.setType("image/*");
                        startActivityForResult(intent_album, RC_ALBUM);
                        break;
                    }
                }
            }
        });
        builder.create().show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) return;
        switch (requestCode){
            case RC_CAMERA:{
                Log.d("sensenote","camera data:"+data);//相机没有返回值
                addImageViewIntoRichText(lastInsertFile.getPath());
                break;
            }
            case RC_ALBUM:{
//                    long startTime = System.currentTimeMillis();
//                    Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(data.getData()));
//                    Log.i("time","decode costtime"+ (System.currentTimeMillis() - startTime));
//                    startTime = System.currentTimeMillis();
//                    String filePath = PhotoUtil.savePhotoToLocal(bitmap,getContext());
//                    Log.i("time","save costtime"+ (System.currentTimeMillis() - startTime));
                Uri uri = (Uri) data.getData();
                //Log.d("sensenote","uri:"+uri.toString());
                addImageViewIntoRichText(PhotoUtil.getRealFilePath(getContext(),uri));
                break;
            }
            case CROP_REQUEST_CODE:{

                break;
            }
        }
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }


    private void addImageViewIntoRichText(String filePath) {
        richTextEditor.addImageViewAtIndex(richTextEditor.getLastIndex(),filePath);
        richTextEditor.addEditTextAtIndex(richTextEditor.getLastIndex(),"");
    }

    @Override
    public void lazyLoad() {
        if (mHasLoadedOnce) {
            return;
        }
        //填充内容
        //Integer noteBookId = getArguments().getInt(ARG_NOTE_BOOK_ID);
        mNoteBook = (NoteBookBean) getArguments().getSerializable(ARG_NOTE_BOOK);
        if(mNoteBook == null){//新建
            mNoteBook = noteBookPresenter.getDefaultNoteBook();
        }
        notebookTxtView.setText(mNoteBook.getNoteBookName());
        mHasLoadedOnce = true;
    }
}
