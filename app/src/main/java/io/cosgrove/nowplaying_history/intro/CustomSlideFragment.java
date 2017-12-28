package io.cosgrove.nowplaying_history.intro;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.heinrichreimersoftware.materialintro.app.SlideFragment;

import io.cosgrove.nowplaying_history.R;

/**
 * Created by tylercosgrove on 12/28/17.
 */

public class CustomSlideFragment extends SlideFragment {

    private boolean mCanGoForward;
    private boolean mCanGoBackward;

    private TextView mTitleTV;
    private TextView mDescTV;

    private int mTitleResId;
    private int mDescResId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mi_fragment_simple_slide, container, false);
        mTitleTV = (TextView) v.findViewById(R.id.mi_title);
        mDescTV = (TextView) v.findViewById(R.id.mi_description);

        setupViews();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupViews();
    }

    @Override
    public boolean canGoForward() {
        return mCanGoForward;
    }

    @Override
    public boolean canGoBackward() {
        return mCanGoBackward;
    }

    private void setupViews() {

        if (mTitleResId != 0) {
            mTitleTV.setText(mTitleResId);
            mTitleTV.setTextColor(ContextCompat.getColor(this.getContext(),
                    com.heinrichreimersoftware.materialintro.R.color.mi_text_color_primary_dark));
        }

        if (mDescResId != 0) {
            mDescTV.setText(mDescResId);
            mDescTV.setTextColor(ContextCompat.getColor(this.getContext(),
                    com.heinrichreimersoftware.materialintro.R.color.mi_text_color_secondary_dark));
        }
    }

    public CustomSlideFragment setCanGoForward(boolean canGoFwd) {
        mCanGoForward = canGoFwd;
        return this;
    }

    public CustomSlideFragment setCanGoBackward(boolean canGoBwd) {
        mCanGoBackward = canGoBwd;
        return this;
    }

    public CustomSlideFragment setTitleText(@StringRes int resId) {
        mTitleResId = resId;
        return this;
    }

    public CustomSlideFragment setDescriptionText(@StringRes int resId) {
        mDescResId = resId;
        return this;
    }
}
