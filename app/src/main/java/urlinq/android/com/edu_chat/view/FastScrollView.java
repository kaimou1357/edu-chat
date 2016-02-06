package urlinq.android.com.edu_chat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by kai on 2/6/16.
 */
public class FastScrollView extends ScrollView {
    public FastScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FastScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FastScrollView(Context context) {
        super(context);
    }

    /**
     * Sliding events
     */
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY *3);
    }
}
