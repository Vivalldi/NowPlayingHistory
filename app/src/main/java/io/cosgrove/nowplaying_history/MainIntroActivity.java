package io.cosgrove.nowplaying_history;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.heinrichreimersoftware.materialintro.slide.Slide;

import java.util.Set;

import io.cosgrove.nowplaying_history.intro.CustomSlideFragment;
import io.cosgrove.nowplaying_history.utils.Constants;

public class MainIntroActivity extends IntroActivity {

    private static final int ENABLE_NOTIFICATION_ACCESS_INTENT = 1;

    private Slide mGrantNotificationAccessSlide;

    private void addWelcomeSlide() {
        SimpleSlide welcomeSlide = new SimpleSlide.Builder()
                .title(R.string.intro_welcome_title)
                .description(R.string.intro_welcome_message)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .canGoForward(true)
                .buttonCtaLabel(R.string.intro_get_started)
                .buttonCtaClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextSlide();
                    }
                })
                .build();
        addSlide(welcomeSlide);
    }

    private void addGrantNotificationAccessSlide() {
        Set<String> EnabledListenerPackagesSet =
                NotificationManagerCompat.getEnabledListenerPackages(this);
        if (EnabledListenerPackagesSet.contains(Constants.PACKAGE_NAME)) {
            CustomSlideFragment fragment = new CustomSlideFragment();
            fragment.setCanGoBackward(true)
                    .setCanGoForward(true)
                    .setTitleText(R.string.intro_grant_permission_success)
                    .setDescriptionText(R.string.intro_grant_permission_success);

            mGrantNotificationAccessSlide = new FragmentSlide.Builder()
                    .fragment(fragment)
                    .background(R.color.colorSecondary)
                    .backgroundDark(R.color.colorSecondaryDark)
                    .build();
        } else {
            CustomSlideFragment fragment = new CustomSlideFragment();
            fragment.setCanGoBackward(true)
                    .setCanGoForward(false)
                    .setTitleText(R.string.intro_grant_permission_title)
                    .setDescriptionText(R.string.intro_grant_permission_message);

            mGrantNotificationAccessSlide = new FragmentSlide.Builder()
                    .fragment(fragment)
                    .background(R.color.colorWarning)
                    .backgroundDark(R.color.colorWarningDark)
                    .buttonCtaLabel(R.string.grant_permissions)
                    .buttonCtaClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Set<String> EnabledListenerPackagesSet = NotificationManagerCompat
                                    .getEnabledListenerPackages(MainIntroActivity.this);

                            if (!EnabledListenerPackagesSet.contains(Constants.PACKAGE_NAME)) {
                                startActivityForResult(
                                        new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"),
                                        ENABLE_NOTIFICATION_ACCESS_INTENT
                                );
                            }
                        }
                    })
                    .build();

        }
        addSlide(mGrantNotificationAccessSlide);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean setupDone = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                getString(R.string.done_first_launch_key),
                false
        );

        if(setupDone) {
            new AlertDialog.Builder(MainIntroActivity.this)
                    .setTitle(getString(R.string.intro_setup_issues_title))
                    .setMessage(getString(R.string.intro_setup_issues_message))
                    .setPositiveButton(getString(R.string.intro_setup_issues_ok), null)
                    .create()
                    .show();
        }

        addWelcomeSlide();
        addGrantNotificationAccessSlide();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ENABLE_NOTIFICATION_ACCESS_INTENT: {
                Set<String> EnabledListenerPackageSet = NotificationManagerCompat
                        .getEnabledListenerPackages(MainIntroActivity.this);
                if (EnabledListenerPackageSet.contains(Constants.PACKAGE_NAME)) {
                    CustomSlideFragment fragment = (CustomSlideFragment) mGrantNotificationAccessSlide.getFragment();
                    fragment.setCanGoForward(true)
                            .setTitleText(R.string.intro_grant_permission_success)
                            .setDescriptionText(R.string.intro_grant_permission_success);
                }
                return;
            }
        }
    }
}
