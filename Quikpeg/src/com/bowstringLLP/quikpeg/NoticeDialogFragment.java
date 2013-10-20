package com.bowstringLLP.quikpeg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;

public class NoticeDialogFragment extends DialogFragment
{
  public static final String DIALOG = "com.bowstringLLP.oneclickalcohol.DIALOG";
  NoticeDialogListener mListener;
  String s;

  public static NoticeDialogFragment newInstance(String paramString)
  {
    NoticeDialogFragment localNoticeDialogFragment = new NoticeDialogFragment();
    Bundle localBundle = new Bundle();
    localBundle.putString("com.bowstringLLP.oneclickalcohol.DIALOG", paramString);
    localNoticeDialogFragment.setArguments(localBundle);
    return localNoticeDialogFragment;
  }

  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    try
    {
      this.mListener = ((NoticeDialogListener)paramActivity);
      return;
    }
    catch (ClassCastException localClassCastException)
    {
    }
    throw new ClassCastException(paramActivity.toString() + " must implement NoticeDialogListener");
  }

  public Dialog onCreateDialog(Bundle paramBundle)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
    TextView localTextView = new TextView(getActivity());
    localTextView.setPadding(10, 5, 0, 5);
    localTextView.setTextSize(15.0F);
    localTextView.setText(getArguments().getString("com.bowstringLLP.oneclickalcohol.DIALOG"));
    localTextView.setSingleLine(false);
    localTextView.setHorizontallyScrolling(false);
    localBuilder.setView(localTextView);
    localBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        NoticeDialogFragment.this.mListener.onDialogContinueClick(NoticeDialogFragment.this);
      }
    }).setNeutralButton("Retry", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        NoticeDialogFragment.this.mListener.onDialogRetryClick(NoticeDialogFragment.this);
      }
    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        NoticeDialogFragment.this.mListener.onDialogNegativeClick(NoticeDialogFragment.this);
      }
    });
    return localBuilder.create();
  }

  public static abstract interface NoticeDialogListener
  {
    public abstract void onDialogContinueClick(DialogFragment paramDialogFragment);

    public abstract void onDialogNegativeClick(DialogFragment paramDialogFragment);

    public abstract void onDialogRetryClick(DialogFragment paramDialogFragment);
  }
}

/* Location:           C:\apktool1.5.2\dex2jar-0.0.9.15\quikpeg_dex2jar.jar
 * Qualified Name:     com.bowstringLLP.quikpeg.NoticeDialogFragment
 * JD-Core Version:    0.6.0
 */