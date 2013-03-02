/**
 * 
 */
package zen.stress.twister;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

/**
 * @author mwho
 *
 */
public class TitleBar extends LinearLayout {

	/**
	 * @param context
	 * @param attrs
	 */
	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(HORIZONTAL);
		setGravity(Gravity.CENTER);
		setWeightSum(1.0f);
		/*
		LayoutInflater.from(context).inflate(R.layout.titlebar, this, true);
		
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitleBar, 0, 0);
		
		String text = array.getString(R.styleable.TitleBar_android_text);
		if (text == null) text = "Title Here";
		((TextView) findViewById(R.id.titlebar_label)).setText(text);
		///don't forget this
		array.recycle();
		*/
	}
	
}
