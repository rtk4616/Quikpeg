package com.bowstringLLP.quikpeg;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

public class DryFragment extends Fragment {
	private DryListAdapter adapter;
	Spinner spinner;
	Button spinnerButton;
	private static final float ROTATE_FROM = 0.0f;
	private static final float ROTATE_TO = -180.0f;
	Boolean flag = false;
	private ArrayAdapter<CharSequence> spinnerAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new DryListAdapter(getActivity());
		setRetainInstance(true);
	}

	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
		return paramLayoutInflater.inflate(R.layout.dry_layout, paramViewGroup,
				false);
	}

	@Override
	public void onActivityCreated(Bundle paramBundle) {
		super.onActivityCreated(paramBundle);

		spinner = (Spinner) getActivity().findViewById(R.id.state_spinner);
		ListView dryList = (ListView) getActivity().findViewById(R.id.dry_list);
		dryList.setAdapter(adapter);

		spinnerAdapter = ArrayAdapter
				.createFromResource(getActivity(), R.array.DryDaysStateNames,
						R.layout.spinner_layout);
		spinner.setAdapter(spinnerAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				CharSequence ch = (CharSequence) parent
						.getItemAtPosition(position);

				adapter.clear();
				List<DryDay> dryList = MainActivity.builder
						.getDryDaysForState(ch.toString());

				for (int i = 0; i < dryList.size(); i++)
					adapter.add(dryList.get(i));

				if (flag) {
					RotateAnimation r = new RotateAnimation(ROTATE_TO,
							ROTATE_FROM, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					r.setDuration((long) 500);
					r.setRepeatCount(0);
					spinnerButton.startAnimation(r);
					r.setFillAfter(true);
				}

				flag = true;
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});

		View.OnTouchListener spinnerOnTouch = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					listener.onClick(v);
				}
				return true;
			}
		};

		View.OnKeyListener spinnerOnKey = new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
					listener.onClick(v);
					return true;
				} else {
					return false;
				}
			}
		};

		spinner.setOnTouchListener(spinnerOnTouch);
		spinner.setOnKeyListener(spinnerOnKey);
		spinnerButton = (Button) getActivity()
				.findViewById(R.id.spinner_button);
		spinnerButton.setHeight(spinner.getHeight());
		spinnerButton.setOnClickListener(listener);
	}

	android.view.View.OnClickListener listener = new OnClickListener() {
		boolean flag = false;

		@Override
		public void onClick(View v) {
			spinner.performClick();
			if (flag) {
				RotateAnimation r = new RotateAnimation(ROTATE_TO, ROTATE_FROM,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				r.setDuration((long) 500);
				r.setRepeatCount(0);
				spinnerButton.startAnimation(r);
				r.setFillAfter(true);
				flag= !flag;
			} else {
				RotateAnimation r = new RotateAnimation(ROTATE_FROM, ROTATE_TO,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				r.setDuration((long) 500);
				r.setRepeatCount(0);
				spinnerButton.startAnimation(r);
				r.setFillAfter(true);
				flag= !flag;
			}
		}
	};

	public void onStop() {
		super.onStop();
		flag = false;
	}
}
