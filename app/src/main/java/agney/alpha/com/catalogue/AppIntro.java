package agney.alpha.com.catalogue;

import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by Agney on 23-01-2016.
 */
public class AppIntro extends AppIntro2 {

    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(AppIntroFragment.newInstance("No more tabs","No Longer do you need to open multiple tabs", R.drawable.tabs,R.color.colorIntro) );
        addSlide(AppIntroFragment.newInstance("Easy","Enter your search term. category. GO!", R.drawable.uielement,R.color.colorIntro) );
        addSlide(AppIntroFragment.newInstance("Ecommerce","We go through 20 ecommerce sites for you",R.drawable.sites,R.color.colorIntro));
        addSlide(AppIntroFragment.newInstance("Introducing..","START",R.drawable.last,R.color.colorIntro));
        showStatusBar(true);
        setFadeAnimation();
    }

    @Override
    public void onDonePressed() {
        finish();
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onSlideChanged() {

    }
}
