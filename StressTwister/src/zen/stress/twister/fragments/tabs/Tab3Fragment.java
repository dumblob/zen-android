package zen.stress.twister.fragments.tabs;

import zen.stress.twister.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Tab3Fragment extends Fragment {
	public static final String[] pranks = new String[] { "Fart", "FalseAlarm", "Vibration", "SexyGirl" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		View view = inflater.inflate(R.layout.tab_frag3_layout, container, false);
		RadioGroup group = (RadioGroup) view.findViewById(R.id.radioGroupPranksBuiltin);

		for (String prank : pranks) {
			RadioButton btn = new RadioButton(inflater.getContext());
			btn.setText(prank);
			btn.setTag(prank);
			btn.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			btn.setPadding(0, 10, 0, 10);

			group.addView(btn);
		}

		return view;
	}

}
