package urlinq.android.com.edu_chat;


import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Kai on 9/4/2015.
 */
public class LoginFragment extends Fragment {
    int numberOfViewPagerChildren = 3;
    int lastIndexOfViewPagerChildren = numberOfViewPagerChildren;
    private ViewPager mViewPager;
    private PagerAdapter mCustomPagerAdapter;
    private ImageButton signUpBtnToggle;
    private ImageButton loginBtnToggle;
    private ImageButton loginBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    /**
     * Load the login_main.xml layout.
     * In addition, load the ViewPager.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initialize viewpager object.
        View v = inflater.inflate(R.layout.login_main, container, false);
        mViewPager = (ViewPager) v.findViewById(R.id.loginPager);
        mViewPager.setAdapter(new LoginAdapter(getActivity().getSupportFragmentManager()));


        final LayerDrawable background = (LayerDrawable) mViewPager.getBackground();

        background.getDrawable(0).setAlpha(0);
        //lowest drawable
        background.getDrawable(1).setAlpha(0);
        background.getDrawable(2).setAlpha(1); //outermost drawable.

        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                int index = (Integer) view.getTag();
                Drawable currentDrawableInLayerDrawable;
                currentDrawableInLayerDrawable = background.getDrawable(index);
                // Change the visibility of each of the fragment layers when swiping left and right.

                if (position <= -1 || position >= 1) {
                    currentDrawableInLayerDrawable.setAlpha(0);
                } else if (position == 0) {
                    currentDrawableInLayerDrawable.setAlpha(255);
                } else {
                    currentDrawableInLayerDrawable.setAlpha((int) (255 - Math.abs(position * 255)));
                }
            }
        });

        return v;
    }

    /**
     * Adapter created for the ViewPager to flip through all the fragments.
     */
    class LoginAdapter extends FragmentStatePagerAdapter {
        public LoginAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            if (i == 0) {
                fragment = new HeroFragment1();
            }
            if (i == 1) {
                fragment = new HeroFragment2();
            }
            if (i == 2) {
                fragment = new HeroFragment3();
            }
            return fragment;

        }

        @Override
        public int getCount() {
            return numberOfViewPagerChildren;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            if (object instanceof HeroFragment1) {
                view.setTag(2);
            }
            if (object instanceof HeroFragment2) {
                view.setTag(1);
            }
            if (object instanceof HeroFragment3) {
                view.setTag(0);
            }
            return super.isViewFromObject(view, object);
        }
    }


}







