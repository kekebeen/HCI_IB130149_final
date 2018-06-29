package com.pretzel.ben.ib130149_flashcard;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

public class IntroWizardActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private SliderAdapter sliderAdapter;
    private Button mNextBtn;
    private Button mPrevBtn;
    private Button mFinishBtn;
    private int mCurrentPage;

    private TextView[] mDots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_wizard);

        mSlideViewPager = findViewById(R.id.slideViewPager);
        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);

        mNextBtn = findViewById(R.id.nextBtn);
        mPrevBtn = findViewById(R.id.prevBtn);
        mFinishBtn = findViewById(R.id.finishBtn);

        // dots
        addDots(0);

        // event listeners
        mSlideViewPager.addOnPageChangeListener(viewListener);

        // next btn logic
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentPage + 1);
            }
        });

        mPrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });

        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    public void addDots(int position) {
        mDots = new TextView[3];
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDots(i);

            mCurrentPage = i;

            if(i == 0) {
                mNextBtn.setEnabled(true);
                mNextBtn.setVisibility(View.VISIBLE);
                mPrevBtn.setEnabled(false);
                mPrevBtn.setVisibility(View.INVISIBLE);

                mNextBtn.setText("NEXT");
                mPrevBtn.setText("");

                // set finish btn
                mFinishBtn.setEnabled(false);
                mFinishBtn.setVisibility(View.INVISIBLE);
                mFinishBtn.setText("");
            } else if(i == mDots.length - 1) {
                mNextBtn.setEnabled(false);
                mNextBtn.setVisibility(View.INVISIBLE);
                mPrevBtn.setEnabled(true);
                mPrevBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("FINISH");
                mPrevBtn.setText("BACK");

                // set finish btn
                mFinishBtn.setEnabled(true);
                mFinishBtn.setVisibility(View.VISIBLE);
                mFinishBtn.setText("FINISH");
            } else {
                mNextBtn.setEnabled(true);
                mPrevBtn.setEnabled(true);
                mPrevBtn.setVisibility(View.VISIBLE);
                mNextBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("NEXT");
                mPrevBtn.setText("BACK");

                mFinishBtn.setEnabled(false);
                mFinishBtn.setVisibility(View.INVISIBLE);
                mFinishBtn.setText("");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

}
