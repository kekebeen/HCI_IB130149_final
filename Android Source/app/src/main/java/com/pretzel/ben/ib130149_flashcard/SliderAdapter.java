package com.pretzel.ben.ib130149_flashcard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    // arrays for desc and icons and images
    public int[] slide_images = {
            R.drawable.icon_2,
            R.drawable.icon_3,
            R.drawable.icon_4
    };

    public String[] slide_headings = {
            "EAT",
            "SLEEP",
            "REPEAT"
    };

    public String[] slide_descs = {
            "Creating apps is not just about writing code. It’s also about creating something that users want to use and are comfortable using. Everyone should.",
            "Most Android developers know about the ViewPager and its ability to swipe between Fragments without much of a hassle when it comes to setting it up.",
            "Therefore, let’s see what it looks like if I were to implement a simple intro screen naively using a ViewPager:"
    };


    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        // set image, desc and heading
        ImageView slideImageView = view.findViewById(R.id.slide_image);
        TextView slideHeading = view.findViewById(R.id.slide_heading);
        TextView slideDescription = view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);


        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
