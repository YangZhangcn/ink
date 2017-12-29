package com.zy.ink.mvp.base;

/**
 * Created by zhangyang on 2017/12/27.
 */

public interface MvpPresenter<V extends MvpView> {

    void onAttach(V view);

    void onDetach();


}
