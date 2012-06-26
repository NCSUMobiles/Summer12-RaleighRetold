package com.citycamp.raleighretold;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.citycamp.raleighretold.Authorizer.AuthorizationListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Video;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UploadStoryActivity extends Activity {
	private String LOG_TAG = UploadStoryActivity.class.getSimpleName(); 
	private static final String INITIAL_UPLOAD_URL = 
			"http://uploads.gdata.youtube.com/resumable/feeds/api/users/default/uploads";
	private static final int MAX_RETRIES = 5;
	private static final int BACKOFF = 4;
	private ProgressDialog dialog = null;
	private Authorizer authorizer = null;
	private Uri videoUri = null;
	private Date dateTaken = null;
	private Location videoLocation = null;
	private String tags = null;
	private LocationListener locationListener = null;
	private LocationManager locationManager = null;
	private SharedPreferences preferences = null;

	private double currentFileSize = 0;
	private double totalBytesUploaded = 0;
	private int numberOfRetries = 0;
	protected String clientLoginToken;
	private String youTubeName = "raleighretold@gmail.com";

	static class YouTubeAccountException extends Exception {
		public YouTubeAccountException(String msg) {
			super(msg);
		}
	}
	class Internal500ResumeException extends Exception {
		Internal500ResumeException(String message) {
			super(message);
		}
	}

	class ResumeInfo {
		int nextByteToUpload;
		String videoId;
		ResumeInfo(int nextByteToUpload) {
			this.nextByteToUpload = nextByteToUpload;
		}
		ResumeInfo(String videoId) {
			this.videoId = videoId;
		}
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.tellstory);

		this.authorizer = new ClientLoginAuthorizer.ClientLoginAuthorizerFactory().getAuthorizer(this, ClientLoginAuthorizer.YOUTUBE_AUTH_TOKEN_TYPE);

		Intent intent = this.getIntent();
		this.videoUri = intent.getData();

		Cursor cursor = this.managedQuery(this.videoUri, null, null, null, null);

		if (cursor.getCount() == 0) {
			Log.d(LOG_TAG, "not a valid video uri");
			Toast.makeText(UploadStoryActivity.this, "not a valid video uri", Toast.LENGTH_LONG).show();
		} else {
			getVideoLocation();

			if (cursor.moveToFirst()) {

				long id = cursor.getLong(cursor.getColumnIndex(Video.VideoColumns._ID));
				this.dateTaken = new Date(cursor.getLong(cursor
						.getColumnIndex(Video.VideoColumns.DATE_TAKEN)));

				SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm aaa");
				Configuration userConfig = new Configuration();
				Settings.System.getConfiguration(getContentResolver(), userConfig);
				Calendar cal = Calendar.getInstance(userConfig.locale);
				TimeZone tz = cal.getTimeZone();

				dateFormat.setTimeZone(tz);
				getAuthTokenWithPermission("raleighretold@gmail.com");
			}
		}
	}

	private void getVideoLocation() {
		this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);

		String provider = locationManager.getBestProvider(criteria, true);

		this.locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					videoLocation = location;
					double lat = location.getLatitude();
					double lng = location.getLongitude();
					Log.d(LOG_TAG, "lat=" + lat);
					Log.d(LOG_TAG, "lng=" + lng);

					locationManager.removeUpdates(this);
				} else {
					Log.d(LOG_TAG, "location is null");
				}
			}

			@Override
			public void onProviderDisabled(String provider) {
			}

			@Override
			public void onProviderEnabled(String provider) {
			}
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

		};

		if (provider != null) {
			locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
		}
	}

	private void getAuthTokenWithPermission(String accountName) {
		accountName = "raleighretold@gmail.com";
		this.authorizer.fetchAuthToken(accountName, this, new AuthorizationListener<String>() {
			@Override
			public void onCanceled() {
			}

			@Override
			public void onError(Exception e) {
			}

			@Override
			public void onSuccess(String result) {
				UploadStoryActivity.this.clientLoginToken = result;
				upload(UploadStoryActivity.this.videoUri);
			}});
	}

	public void upload(Uri videoUri) {
		this.dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setMessage("uploading ...");
		dialog.setCancelable(false);
		dialog.show();

		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				dialog.dismiss();
				String videoId = msg.getData().getString("videoId");

				if (!isNullOrEmpty(videoId)) {
					currentFileSize = 0;
					totalBytesUploaded = 0;
					Intent result = new Intent();
					result.putExtra("videoId", videoId);
					setResult(RESULT_OK, result);
					finish();
				} else {
					String error = msg.getData().getString("error");
					if (!isNullOrEmpty(error)) {
						Toast.makeText(UploadStoryActivity.this, error, Toast.LENGTH_LONG).show();
						Log.d(LOG_TAG, error);
					}
				}
			}
		};

		asyncUpload(videoUri, handler);
	}

	public void asyncUpload(final Uri uri, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				Bundle bundle = new Bundle();
				msg.setData(bundle);

				String videoId = null;
				int submitCount=0;
				try {
					while (submitCount<=MAX_RETRIES && videoId == null) {
						try {
							submitCount++;
							videoId = startUpload(uri);
							assert videoId!=null;
						} catch (Internal500ResumeException e500) { // TODO - this should not really happen                                               
							if (submitCount<MAX_RETRIES) {
								Log.w(LOG_TAG, e500.getMessage());
								Log.d(LOG_TAG, String.format("Upload retry :%d.",submitCount));
							} else {
								Log.d(LOG_TAG, "Giving up");
								Log.e(LOG_TAG, e500.getMessage());
								throw new IOException(e500.getMessage());
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					bundle.putString("error", e.getMessage());
					handler.sendMessage(msg);
					return;
				} catch (YouTubeAccountException e) {
					e.printStackTrace();
					bundle.putString("error", e.getMessage());
					handler.sendMessage(msg);
					return;
				} catch (SAXException e) {
					e.printStackTrace();
					bundle.putString("error", e.getMessage());
					handler.sendMessage(msg);
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					bundle.putString("error", e.getMessage());
					handler.sendMessage(msg);
				}

				bundle.putString("videoId", videoId);
				handler.sendMessage(msg);
			}
		}).start();
	}

	private String startUpload(Uri uri) throws IOException, YouTubeAccountException, SAXException, ParserConfigurationException, Internal500ResumeException{
		File file = getFileFromUri(uri);

		if (this.clientLoginToken == null) {
			// The stored gmail account is not linked to YouTube                                                                                    
			throw new YouTubeAccountException(this.youTubeName + " is not linked to a YouTube account.");
		}

		String uploadUrl = uploadMetaData(file.getAbsolutePath(), true);

		Log.d(LOG_TAG, "uploadUrl=" + uploadUrl);
		Log.d(LOG_TAG, String.format("Client token : %s ",this.clientLoginToken));


		this.currentFileSize = file.length();
		this.totalBytesUploaded = 0;
		this.numberOfRetries = 0;

		int uploadChunk = 1024 * 1024 * 3; // 3MB                                                                                                 

		int start = 0;
		int end = -1;

		String videoId = null;
		double fileSize = this.currentFileSize;
		while (fileSize > 0) {
			if (fileSize - uploadChunk > 0) {
				end = start + uploadChunk - 1;
			} else {
				end = start + (int) fileSize - 1;
			}
			Log.d(LOG_TAG, String.format("start=%s end=%s total=%s", start, end, file.length()));
			try {
				videoId = gdataUpload(file, uploadUrl, start, end);
				fileSize -= uploadChunk;
				start = end + 1;
				this.numberOfRetries = 0; // clear this counter as we had a succesfull upload                                                         
			} catch (IOException e) {
				Log.d(LOG_TAG,"Error during upload : " + e.getMessage());
				ResumeInfo resumeInfo = null;
				do {
					if (!shouldResume()) {
						Log.d(LOG_TAG, String.format("Giving up uploading '%s'.", uploadUrl));
						throw e;
					}
					try {
						resumeInfo = resumeFileUpload(uploadUrl);
					} catch (IOException re) {
						// ignore                                                                                                                         
						Log.d(LOG_TAG, String.format("Failed retry attempt of : %s due to: '%s'.", uploadUrl, re.getMessage()));
					}
				} while (resumeInfo == null);
				Log.d(LOG_TAG, String.format("Resuming stalled upload to: %s.", uploadUrl));
				if (resumeInfo.videoId != null) { // upload actually complted despite the exception                                                   
					videoId = resumeInfo.videoId;
					Log.d(LOG_TAG, String.format("No need to resume video ID '%s'.", videoId));
					break;
				} else {
					int nextByteToUpload = resumeInfo.nextByteToUpload;
					Log.d(LOG_TAG, String.format("Next byte to upload is '%d'.", nextByteToUpload));
					this.totalBytesUploaded = nextByteToUpload; // possibly rolling back the previosuly saved value                                     
					fileSize = this.currentFileSize - nextByteToUpload;
					start = nextByteToUpload;
				}
			}
		}

		if (videoId != null) {
			return videoId;
		}

		return null;
	}

	private File getFileFromUri(Uri uri) throws IOException {
		Cursor cursor = managedQuery(uri, null, null, null, null);
		if (cursor.getCount() == 0) {
			throw new IOException(String.format("cannot find data from %s", uri.toString()));
		} else {
			cursor.moveToFirst();
		}

		String filePath = cursor.getString(cursor.getColumnIndex(Video.VideoColumns.DATA));

		File file = new File(filePath);
		cursor.close();
		return file;
	}

	private String gdataUpload(File file, String uploadUrl, int start, int end) throws IOException {
		int chunk = end - start + 1;
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		FileInputStream fileStream = new FileInputStream(file);

		HttpURLConnection urlConnection = getGDataUrlConnection(uploadUrl);
		// some mobile proxies do not support PUT, using X-HTTP-Method-Override to get around this problem                                        
		if (isFirstRequest()) {
			Log.d(LOG_TAG, String.format("Uploaded %d bytes so far, using POST method.", (int)totalBytesUploaded));
			urlConnection.setRequestMethod("POST");
		} else {
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("X-HTTP-Method-Override", "PUT");
			Log.d(LOG_TAG, String.format("Uploaded %d bytes so far, using POST with X-HTTP-Method-Override PUT method.",
					(int)totalBytesUploaded));
		}
		urlConnection.setDoOutput(true);
		urlConnection.setFixedLengthStreamingMode(chunk);
		urlConnection.setRequestProperty("Content-Type", "video/3gpp");
		urlConnection.setRequestProperty("Content-Range", String.format("bytes %d-%d/%d", start, end,
				file.length()));
		Log.d(LOG_TAG, urlConnection.getRequestProperty("Content-Range"));

		OutputStream outStreamWriter = urlConnection.getOutputStream();

		fileStream.skip(start);

		int bytesRead;
		int totalRead = 0;
		while ((bytesRead = fileStream.read(buffer, 0, bufferSize)) != -1) {
			outStreamWriter.write(buffer, 0, bytesRead);
			totalRead += bytesRead;
			this.totalBytesUploaded += bytesRead;
			double percent = (totalBytesUploaded / currentFileSize) * 99;

			/*                                                                                                                                      
		      Log.d(LOG_TAG, String.format(                                                                                                           
		      "fileSize=%f totalBytesUploaded=%f percent=%f", currentFileSize,                                                                        
		      totalBytesUploaded, percent));                                                                                                          
			 */

			dialog.setProgress((int) percent);

			if (totalRead == (end - start + 1)) {
				break;
			}
		}

		outStreamWriter.close();

		int responseCode = urlConnection.getResponseCode();

		Log.d(LOG_TAG, "responseCode=" + responseCode);
		Log.d(LOG_TAG, "responseMessage=" + urlConnection.getResponseMessage());

		try {
			if (responseCode == 201) {
				String videoId = parseVideoId(urlConnection.getInputStream());

				String latLng = null;
				if (this.videoLocation != null) {
					latLng = String.format("lat=%f lng=%f", this.videoLocation.getLatitude(),
							this.videoLocation.getLongitude());
				}
				dialog.setProgress(100);
				return videoId;
			} else if (responseCode == 200) {
				Set<String> keySet = urlConnection.getHeaderFields().keySet();
				String keys = urlConnection.getHeaderFields().keySet().toString();
				Log.d(LOG_TAG, String.format("Headers keys %s.", keys));
				for (String key : keySet) {
					Log.d(LOG_TAG, String.format("Header key %s value %s.", key, urlConnection.getHeaderField(key)));
				}
				Log.w(LOG_TAG, "Received 200 response during resumable uploading");
				throw new IOException(String.format("Unexpected response code : responseCode=%d responseMessage=%s", responseCode,
						urlConnection.getResponseMessage()));
			} else {
				if ((responseCode + "").startsWith("5")) {
					String error = String.format("responseCode=%d responseMessage=%s", responseCode,
							urlConnection.getResponseMessage());
					Log.w(LOG_TAG, error);
					throw new IOException(error);
				} else if (responseCode == 308) {
					// OK, the chunk completed succesfully                                                                                              
					Log.d(LOG_TAG, String.format("responseCode=%d responseMessage=%s", responseCode,
							urlConnection.getResponseMessage()));
				} else {
					// TODO - this case is not handled properly yet                                                                                     
					Log.w(LOG_TAG, String.format("Unexpected return code : %d %s while uploading :%s", responseCode,
							urlConnection.getResponseMessage(), uploadUrl));
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String uploadMetaData(String filePath, boolean retry) throws IOException {
		String uploadUrl = INITIAL_UPLOAD_URL;

		HttpURLConnection urlConnection = getGDataUrlConnection(uploadUrl);
		urlConnection.setRequestMethod("POST");
		urlConnection.setDoOutput(true);
		urlConnection.setRequestProperty("Content-Type", "application/atom+xml");
		urlConnection.setRequestProperty("Slug", filePath);
		String atomData="";

		String title = "Test Upload Raw";
		String description = "Test Description";

		if (this.videoLocation == null) {
			String template = "<?xml version=\"1.0\"?> <entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:media=\"http://search.yahoo.com/mrss/\" xmlns:yt=\"http://gdata.youtube.com/schemas/2007\"> <media:group> <media:title type=\"plain\">%s</media:title> <media:description type=\"plain\">%s</media:description> <media:category scheme=\"http://gdata.youtube.com/schemas/2007/categories.cat\">%s</media:category> <media:keywords>%s</media:keywords> </media:group> </entry>";

//			String template = Util.readFile(this, R.raw.gdata).toString();
			atomData = String.format(template, title, description, null, this.tags);
		} else {
//			String template = Util.readFile(this, R.raw.gdata_geo).toString();
			String template = "<?xml version=\"1.0\"?> <entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:media=\"http://search.yahoo.com/mrss/\" xmlns:yt=\"http://gdata.youtube.com/schemas/2007\"> <media:group> <media:title type=\"plain\">%s</media:title> <media:description type=\"plain\">%s</media:description> <media:category scheme=\"http://gdata.youtube.com/schemas/2007/categories.cat\">%s</media:category> <media:keywords>%s</media:keywords> </media:group> <georss:where xmlns:georss=\"http://www.georss.org/georss\" xmlns:gml=\"http://www.opengis.net/gml\"><gml:Point xmlns:gml=\"http://www.opengis.net/gml\"><gml:pos>%f %f</gml:pos></gml:Point></georss:where> </entry>";
			atomData = String.format(template, title, description, null, this.tags,
					videoLocation.getLatitude(), videoLocation.getLongitude());
		}

		OutputStreamWriter outStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
		outStreamWriter.write(atomData);
		outStreamWriter.close();

		int responseCode = urlConnection.getResponseCode();
		if (responseCode < 200 || responseCode >= 300) {
			// The response code is 40X                                                                                                             
			if ((responseCode + "").startsWith("4") && retry) {
				Log.d(LOG_TAG, "retrying to fetch auth token for " + youTubeName);
				this.clientLoginToken = authorizer.getFreshAuthToken(youTubeName, clientLoginToken);
				// Try again with fresh token                                                                                                         
				return uploadMetaData(filePath, false);
			} else {
				throw new IOException(String.format("response code='%s' (code %d)" + " for %s",
						urlConnection.getResponseMessage(), responseCode, urlConnection.getURL()));
			}
		}

		return urlConnection.getHeaderField("Location");
	}

	private boolean shouldResume() {
		this.numberOfRetries++;
		if (this.numberOfRetries>MAX_RETRIES) {
			return false;
		}
		try {
			int sleepSeconds = (int) Math.pow(BACKOFF, this.numberOfRetries);
			Log.d(LOG_TAG,String.format("Zzzzz for : %d sec.", sleepSeconds));
			Thread.currentThread().sleep(sleepSeconds * 1000);
			Log.d(LOG_TAG,String.format("Zzzzz for : %d sec done.", sleepSeconds));
		} catch (InterruptedException se) {
			se.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean isFirstRequest() {
		return totalBytesUploaded==0;
	}

	private String parseVideoId(InputStream atomDataStream) throws ParserConfigurationException,
	SAXException, IOException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(atomDataStream);

		NodeList nodes = doc.getElementsByTagNameNS("*", "*");
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			String nodeName = node.getNodeName();
			if (nodeName != null && nodeName.equals("yt:videoid")) {
				return node.getFirstChild().getNodeValue();
			}
		}
		return null;
	}

	private ResumeInfo resumeFileUpload(String uploadUrl) throws IOException, ParserConfigurationException, SAXException, Internal500ResumeException {
		HttpURLConnection urlConnection = getGDataUrlConnection(uploadUrl);
		urlConnection.setRequestProperty("Content-Range", "bytes */*");
		urlConnection.setRequestMethod("POST");
		urlConnection.setRequestProperty("X-HTTP-Method-Override", "PUT");
		urlConnection.setFixedLengthStreamingMode(0);

		HttpURLConnection.setFollowRedirects(false);

		urlConnection.connect();
		int responseCode = urlConnection.getResponseCode();

		if (responseCode >= 300 && responseCode < 400) {
			int nextByteToUpload;
			String range = urlConnection.getHeaderField("Range");
			if (range == null) {
				Log.d(LOG_TAG, String.format("PUT to %s did not return 'Range' header.", uploadUrl));
				nextByteToUpload = 0;
			} else {
				Log.d(LOG_TAG, String.format("Range header is '%s'.", range));
				String[] parts = range.split("-");
				if (parts.length > 1) {
					nextByteToUpload = Integer.parseInt(parts[1]) + 1;
				} else {
					nextByteToUpload = 0;
				}
			}
			return new ResumeInfo(nextByteToUpload);
		} else if (responseCode >= 200 && responseCode < 300) {
			return new ResumeInfo(parseVideoId(urlConnection.getInputStream()));
		} else if (responseCode == 500) {
			// TODO this is a workaround for current problems with resuming uploads while switching transport (Wifi->EDGE)                          
			throw new Internal500ResumeException(String.format("Unexpected response for PUT to %s: %s " +
					"(code %d)", uploadUrl, urlConnection.getResponseMessage(), responseCode));
		} else {
			throw new IOException(String.format("Unexpected response for PUT to %s: %s " +
					"(code %d)", uploadUrl, urlConnection.getResponseMessage(), responseCode));
		}
	}
	
	private HttpURLConnection getGDataUrlConnection(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Authorization", String.format("GoogleLogin auth=\"%s\"",
				clientLoginToken));
		connection.setRequestProperty("GData-Version", "2");
		connection.setRequestProperty("X-GData-Client", this.getString(R.string.client_id));
		//TODO Add DevKey.
		connection.setRequestProperty("X-GData-Key", String.format("key=%s", this.getString(R.string.dev_key)));
		return connection;
	}

	public static boolean isNullOrEmpty(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}


}
