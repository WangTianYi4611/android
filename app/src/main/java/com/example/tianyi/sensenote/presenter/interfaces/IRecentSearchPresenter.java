package com.example.tianyi.sensenote.presenter.interfaces;

import java.util.List;

public interface IRecentSearchPresenter {

    boolean insertRecentSearch(String searchString);

    List<String> getRecentSearchString(Integer num);

}
