package jp.chnr.hearthstonetimer;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.CountDownTimer;

public class HearthStoneTimerActivity extends ActionBarActivity implements OnClickListener {
	private AdView adView;
	private static final String AD_UNIT_ID = "ca-app-pub-1152697570124074/2794941949";

	Chronometer enemyTime, totalTime, myTime;
	Button   mEnemyEndBtn, mMyEndBtn, mGameEndBtn;
	float    mLaptime = 0.0f;
	long enemyPlayTime, myPlayTime;
	long enemyStartTime, enemyStopTime;
	long myStartTime, myStopTime;
	long myAllPlayTime, enemyAllPlayTime;
	TextView enemyTimer, myTimer;
	MyCountDownTimer mcdt = new MyCountDownTimer(90000,1000);
	EnemyCountDownTimer ecdt = new EnemyCountDownTimer(90000,1000);
	int clickTimes = 0;
	boolean firstClick = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);

		Intent intent = getIntent();
		String playordraw = intent.getStringExtra("key");

		adView = new AdView(this);
		adView.setAdUnitId(AD_UNIT_ID);
		adView.setAdSize(AdSize.BANNER);

		LinearLayout layout = (LinearLayout) findViewById(R.id.layout_ad);
		layout.addView(adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);

		//ID�̎擾
		enemyTime = (Chronometer) findViewById(R.id.EnemyTime);
		totalTime = (Chronometer) findViewById(R.id.TotalTime);
		myTime = (Chronometer) findViewById(R.id.MyTime);
		mEnemyEndBtn = (Button)   findViewById(R.id.EnemyEndBtn);
		mMyEndBtn  = (Button)   findViewById(R.id.MyEndBtn);
		mGameEndBtn = (Button)   findViewById(R.id.GameEndBtn);
		enemyTimer = (TextView) findViewById(R.id.EnemyTimer);
		myTimer = (TextView) findViewById(R.id.MyTimer);

		enemyTime.setBase(SystemClock.elapsedRealtime());
		totalTime.setBase(SystemClock.elapsedRealtime());
		myTime.setBase(SystemClock.elapsedRealtime());
		mEnemyEndBtn.setOnClickListener(this);
		mMyEndBtn.setOnClickListener(this);
		mGameEndBtn.setOnClickListener(this);

		totalTime.start();
		if(playordraw.equals("PlayFirst")){
			myTime.start();
			mcdt.start();
			myStartTime = SystemClock.elapsedRealtime();
			mMyEndBtn.setBackgroundResource(R.drawable.active_button);
		}
		else if(playordraw.equals("DrawFirst")){
			enemyTime.start();
			ecdt.start();
			enemyStartTime = SystemClock.elapsedRealtime();
			mEnemyEndBtn.setBackgroundResource(R.drawable.active_button);
			clickTimes++;
		}
	}

	public void onClick(View v) {
		Button btn = (Button)v;
		switch( btn.getId() ){
		case R.id.EnemyEndBtn:
			if(clickTimes%2==1){
				ecdt.cancel();
				enemyTime.stop();

				enemyStopTime = SystemClock.elapsedRealtime();
				if(firstClick == false){
					myTime.setBase(SystemClock.elapsedRealtime());
					firstClick = true;
				}
				else{
					myPlayTime = myStopTime - myStartTime;
					myAllPlayTime += myPlayTime;
					myTime.setBase(SystemClock.elapsedRealtime() - myAllPlayTime);
				}
				myStartTime = SystemClock.elapsedRealtime();

				mEnemyEndBtn.setBackgroundResource(R.drawable.inactive_button);
				mMyEndBtn.setBackgroundResource(R.drawable.active_button);

				myTime.start();
				mcdt.start();
				clickTimes++;
			}
			break;
		case R.id.MyEndBtn:
			if(clickTimes%2==0){
				mcdt.cancel();
				myTime.stop();
				myStopTime = SystemClock.elapsedRealtime();

				if(firstClick == false){
					enemyTime.setBase(SystemClock.elapsedRealtime());
					firstClick = true;
				}
				else{
					enemyPlayTime = enemyStopTime - enemyStartTime;
					enemyAllPlayTime += enemyPlayTime;
					enemyTime.setBase(SystemClock.elapsedRealtime() - enemyAllPlayTime);
				}
				enemyStartTime = SystemClock.elapsedRealtime();

				mMyEndBtn.setBackgroundResource(R.drawable.inactive_button);
				mEnemyEndBtn.setBackgroundResource(R.drawable.active_button);

				enemyTime.start();
				ecdt.start();
				clickTimes++;
			}
			break;
		case R.id.GameEndBtn:
			finish();
			break;
		}
	}

	public class MyCountDownTimer extends CountDownTimer{
		boolean Turn = false;

		public MyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			myTimer.setText("0");
		}

		@Override
		public void onTick(long millisUntilFinished) {
			myTimer.setText(Long.toString(millisUntilFinished/1000/60) + ":" + Long.toString(millisUntilFinished/1000%60));
		}
	}

	public class EnemyCountDownTimer extends CountDownTimer{
		boolean Turn = false;

		public EnemyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			enemyTimer.setText("0");
		}

		@Override
		public void onTick(long millisUntilFinished) {
			enemyTimer.setText(Long.toString(millisUntilFinished/1000/60) + ":" + Long.toString(millisUntilFinished/1000%60));
		}
	}

	@Override
	public void onPause() {
		adView.pause();
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		adView.resume();
	}

	@Override
	public void onDestroy() {
		adView.destroy();
		super.onDestroy();
	}
}
