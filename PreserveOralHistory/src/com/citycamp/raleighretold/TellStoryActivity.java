package com.citycamp.raleighretold;

import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class TellStoryActivity extends Activity {

	private static final String LOG_TAG = TellStoryActivity.class.getSimpleName();

	private static final int CAPTURE_RETURN = 1;
	private static final int GALLERY_RETURN = 2;
	private static final int SUBMIT_RETURN = 3;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tellstory);
		Intent intent = this.getIntent();

		findViewById(R.id.btn_record_video).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				Intent i = new Intent();
				i.setAction("android.media.action.VIDEO_CAPTURE");
				startActivityForResult(i,CAPTURE_RETURN);
			}
		});

		findViewById(R.id.btn_gallery_select).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v){
				Intent i = new Intent();
				i.setAction(Intent.ACTION_PICK);
				i.setType("video/*");
				List<ResolveInfo> list = getPackageManager().queryIntentActivities(i,
						PackageManager.MATCH_DEFAULT_ONLY);
				if (list.size() <= 0) {
					Log.d(LOG_TAG,"No Gallery select on hardware");
					return;
				}
				startActivityForResult(i, GALLERY_RETURN);
			}

		});

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CAPTURE_RETURN:
		case GALLERY_RETURN:
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent(this, UploadStoryActivity.class);
				intent.setData(data.getData());
				startActivityForResult(intent, SUBMIT_RETURN);
			}
			break;
		case SUBMIT_RETURN:
			if (resultCode == RESULT_OK) {
				Toast.makeText(TellStoryActivity.this, "thank you!", Toast.LENGTH_LONG).show();
			} else {
				//Toast.makeText(DetailsActivity.this, "submit failed or cancelled",
				// Toast.LENGTH_LONG).show();
			}
			break;
		}
	}
}
