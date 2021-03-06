package indi.yume.tools.fragmentmanager.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import indi.yume.tools.fragmentmanager.BaseManagerFragment;
import indi.yume.tools.fragmentmanager.anno.OnHideMode;
import indi.yume.tools.fragmentmanager.anno.OnShowMode;

/**
 * Created by yume on 16-4-21.
 */
public class BlankFragment22 extends BaseManagerFragment {


    public BlankFragment22() {
        // Required empty public constructor
    }

//    @Override
//    protected int provideEnterAnim() {
//        return R.anim.fragment_bottom_enter;
//    }
//
//    @Override
//    protected int provideExitAnim() {
//        return R.anim.fragment_bottom_exit;
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println(this.getClass().getSimpleName() + ": onCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank_fragment22, container, false);
    }

    @Override
    public void preBackResultData() {
        super.preBackResultData();

        Bundle data = new Bundle();
        data.putString("result", "Observable result OK");
        setResult(1, data);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println(this.getClass().getSimpleName() + ": onViewCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println(this.getClass().getSimpleName() + ": onResume");
    }

    @Override
    public void onShow(@NonNull OnShowMode callMode) {
        super.onShow(callMode);
        System.out.println(this.getClass().getSimpleName() + ": onShow " + OnShowMode.toString(callMode));
    }

    @Override
    public void onHide(@NonNull OnHideMode hideMode) {
        super.onHide(hideMode);
        System.out.println(this.getClass().getSimpleName() + ": onHide " + OnHideMode.toString(hideMode));
    }
}
