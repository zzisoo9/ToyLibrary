package com.zzisoo.toylibrary.popup;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zzisoo.toylibrary.R;

public class IntroProgressPopupDialog extends Dialog {

	ImageView bgAnim;
	ProgressBar m_ProgressBar;
	AnimationDrawable m_FraneAnimation;
	
	public IntroProgressPopupDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
		setContentView(R.layout.popup_intro_progress);

		m_ProgressBar = (ProgressBar) findViewById(R.id.pb_intro);

//		bgAnim = (ImageView)findViewById(R.id.iv_intro_progress);
//		m_FraneAnimation = (AnimationDrawable) bgAnim.getBackground();
//		bgAnim.post(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				m_FraneAnimation.start();
//			}
//		});
		m_ProgressBar.setProgress(0);
		
	}
	
	public void setProgressBarVisible(boolean bFlag) {
		if(bFlag) {
			m_ProgressBar.setVisibility(View.VISIBLE);
		} else {
			m_ProgressBar.setVisibility(View.GONE);
		}
		
	}
	
	public void setProgress(int nProgress) {
		m_ProgressBar.setProgress(nProgress);
	}

	public void setText(String strText) {
		TextView tvTitle = (TextView) findViewById(R.id.tv_intro_progress);
		tvTitle.setText(strText);
	}
}
