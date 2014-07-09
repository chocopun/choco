package jp.chnr.hearthstonetimer;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class HearthStoneReadyActivity extends Activity implements OnClickListener{
	private AdView adView;
	private static final String AD_UNIT_ID = "ca-app-pub-1152697570124074/2794941949";
	int playordraw = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ready);

		adView = new AdView(this);
		adView.setAdUnitId(AD_UNIT_ID);
		adView.setAdSize(AdSize.BANNER);

		LinearLayout layout = (LinearLayout) findViewById(R.id.layout_ad);
		layout.addView(adView);
		AdRequest adRequest = new AdRequest.Builder().build();
	    adView.loadAd(adRequest);

		Button mGameStartBtn = (Button)findViewById(R.id.GameStart);
		mGameStartBtn.setOnClickListener(this);
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.RadioGroup1);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) { 
				RadioButton PlayOrDraw = (RadioButton) findViewById(checkedId);
				if(PlayOrDraw==(RadioButton) findViewById(R.id.PlayFirst))
					playordraw = 1;
				else if (PlayOrDraw==(RadioButton) findViewById(R.id.DrawFirst))
					playordraw = 2;
			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(HearthStoneReadyActivity.this, HearthStoneTimerActivity.class);
		switch(playordraw) {
		case 0: 
			Toast.makeText(HearthStoneReadyActivity.this, "Please check 'Play Fisrt' or 'Draw Fisrt'", Toast.LENGTH_LONG).show();
			break;
		case 1:
			intent.putExtra("key", "PlayFirst");
			startActivity(intent);
			break;
		case 2:
			intent.putExtra("key", "DrawFirst");
			startActivity(intent);
			break;
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
