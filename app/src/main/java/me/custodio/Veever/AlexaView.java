package me.custodio.Veever;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.custodio.Veever.R;

/**
 * Created by Admin on 20,May,2019
 */
public class AlexaView extends View {

    Paint paint;

    public AlexaView(Context context) {
        super(context);

        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.lime1));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(500, 500, 100, paint);

    }
}
