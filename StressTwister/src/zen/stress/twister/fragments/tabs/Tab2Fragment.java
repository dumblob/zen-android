package zen.stress.twister.fragments.tabs;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import zen.stress.twister.R;

/**
 * @author mwho
 * 
 */
public class Tab2Fragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		return inflater.inflate(R.layout.tab_frag2_layout, container, false);
	}

}
