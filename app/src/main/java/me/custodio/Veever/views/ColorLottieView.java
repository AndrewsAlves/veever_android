package me.custodio.Veever.views;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;

import me.custodio.Veever.R;

/**
 * Created by Admin on 21,September,2019
 */

public class ColorLottieView extends LottieAnimationView {

    public ColorLottieView(Context context) {
        super(context);
    }

    public ColorLottieView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorLottieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void updateColor(int color) {
        addValueCallback(
                new KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    }
                }
        );
    }
}
