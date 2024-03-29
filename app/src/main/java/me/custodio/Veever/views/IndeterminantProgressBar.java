package me.custodio.Veever.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import me.custodio.Veever.R;


/**
 * Created by Andrews on 18,September,2019
 */
public class IndeterminantProgressBar extends ProgressBar {

    public IndeterminantProgressBar(Context context) {
        super(context);

    }

    public IndeterminantProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndeterminantProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.veeverwhite),
                android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
