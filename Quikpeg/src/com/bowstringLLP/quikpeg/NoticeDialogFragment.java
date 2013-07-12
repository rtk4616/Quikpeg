package com.bowstringLLP.quikpeg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;

public class NoticeDialogFragment extends DialogFragment {
    
	String s;

	public final static String DIALOG = "com.bowstringLLP.oneclickalcohol.DIALOG";
    
    public interface NoticeDialogListener {
        public void onDialogContinueClick(DialogFragment dialog);
        public void onDialogRetryClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
 
    NoticeDialogListener mListener;
    
    public static NoticeDialogFragment newInstance(String s)
    {
    	NoticeDialogFragment not = new NoticeDialogFragment();
    	Bundle bun = new Bundle();
        bun.putString(DIALOG, s);
        not.setArguments(bun);
        return not;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        TextView txt = new TextView(this.getActivity());
        txt.setPadding(10, 5, 0, 5);
        txt.setTextSize(15);
        txt.setText(getArguments().getString(DIALOG));
        txt.setSingleLine(false);
        txt.setHorizontallyScrolling(false);
        builder.setView(txt);
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int id) {
        		// Send the positive button event back to the host activity
        		mListener.onDialogContinueClick(NoticeDialogFragment.this);
        	}
        })
        .setNeutralButton("Retry", new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int id) {
        		// Send the positive button event back to the host activity
        		mListener.onDialogRetryClick(NoticeDialogFragment.this);
        	}
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int id) {
        		// Send the negative button event back to the host activity
        		mListener.onDialogNegativeClick(NoticeDialogFragment.this);
        	}
               });
        return builder.create();
    }
}