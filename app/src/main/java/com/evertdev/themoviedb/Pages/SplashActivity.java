package com.evertdev.themoviedb.Pages;

import android.content.Intent;
import android.view.WindowManager;

import com.daimajia.androidanimations.library.Techniques;
import com.evertdev.themoviedb.R;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class SplashActivity extends AwesomeSplash {

    @Override
    public void initSplash(ConfigSplash configSplash) {
        getWindow().setFlags(WindowManager.LayoutParams.ROTATION_ANIMATION_CROSSFADE, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Background
        configSplash.setBackgroundColor(R.color.white);
        configSplash.setAnimCircularRevealDuration(500);
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);
        configSplash.setRevealFlagX(Flags.REVEAL_BOTTOM);


        configSplash.setOriginalHeight(200); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(200); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(3000);
        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.white); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(3000);
        configSplash.setPathSplashFillColor(R.color.white); //path object filling color

        //Logo
        configSplash.setLogoSplash(R.drawable.movie);
        configSplash.setAnimLogoSplashDuration(500);
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeInDown);
        configSplash.setTitleSplash("");
    }

    @Override
    public void animationsFinished() {
        startActivity(new Intent(SplashActivity.this,MainActivity.class));
        finish();
    }
}
