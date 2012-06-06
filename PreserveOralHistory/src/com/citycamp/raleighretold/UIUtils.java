/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.citycamp.raleighretold;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.LevelListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

public class UIUtils {

    /**
     * Time zone to use when formatting all session times. To always use the
     * phone local time, use {@link TimeZone#getDefault()}.
     */
	private static Time sTime = new Time();
    public static TimeZone CONFERENCE_TIME_ZONE = TimeZone.getTimeZone("America/Los_Angeles");

    public static final long CONFERENCE_START_MILLIS = parseTime(
            "2010-05-19T09:00:00.000-07:00");
    public static final long CONFERENCE_END_MILLIS = parseTime(
            "2010-05-20T17:30:00.000-07:00");

    public static final Uri CONFERENCE_URL = Uri.parse("http://code.google.com/events/io/2010/");

    /** Flags used with {@link DateUtils#formatDateRange}. */
    private static final int TIME_FLAGS = DateUtils.FORMAT_SHOW_TIME
            | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_WEEKDAY;

    private static final int BRIGHTNESS_THRESHOLD = 150;

    /** {@link StringBuilder} used for formatting time block. */
    private static StringBuilder sBuilder = new StringBuilder(50);
    /** {@link Formatter} used for formatting time block. */
    private static Formatter sFormatter = new Formatter(sBuilder, Locale.getDefault());

    private static StyleSpan sBoldSpan = new StyleSpan(Typeface.BOLD);

//    public static void setTitleBarColor(View titleBarView, int color) {
//        final ViewGroup titleBar = (ViewGroup) titleBarView;
//        titleBar.setBackgroundColor(color);
//
//        /*
//         * Calculate the brightness of the titlebar color, based on the commonly known
//         * brightness formula:
//         *
//         * http://en.wikipedia.org/wiki/HSV_color_space%23Lightness
//         */
//        int brColor = (30 * Color.red(color) +
//                       59 * Color.green(color) +
//                       11 * Color.blue(color)) / 100;
//        if (brColor > BRIGHTNESS_THRESHOLD) {
//            ((TextView) titleBar.findViewById(R.id.title_text)).setTextColor(
//                    titleBar.getContext().getResources().getColor(R.color.title_text_alt));
//
//            // Iterate through all children of the titlebar and if they're a LevelListDrawable,
//            // set their level to 1 (alternate).
//            // TODO: find a less hacky way of doing this.
//            titleBar.post(new Runnable() {
//                public void run() {
//                    final int childCount = titleBar.getChildCount();
//                    for (int i = 0; i < childCount; i++) {
//                        final View child = titleBar.getChildAt(i);
//                        if (child instanceof ImageButton) {
//                            final ImageButton childButton = (ImageButton) child;
//                            if (childButton.getDrawable() != null &&
//                                childButton.getDrawable() instanceof LevelListDrawable) {
//                                ((LevelListDrawable) childButton.getDrawable()).setLevel(1);
//                            }
//                        }
//                    }
//                }
//            });
//        }
//    }

    /**
     * Invoke "home" action, returning to {@link HomeActivity}.
     */
    public static void goHome(Context context) {
        final Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * Invoke "search" action, triggering a default search.
     */
    public static void goSearch(Activity activity) {
        activity.startSearch(null, false, Bundle.EMPTY, false);
    }

//    /**
//     * Format and return the given {@link Blocks} and {@link Rooms} values using
//     * {@link #CONFERENCE_TIME_ZONE}.
//     */
//    public static String formatSessionSubtitle(long blockStart, long blockEnd,
//            String roomName, Context context) {
//        TimeZone.setDefault(CONFERENCE_TIME_ZONE);
//
//        // NOTE: There is an efficient version of formatDateRange in Eclair and
//        // beyond that allows you to recycle a StringBuilder.
//        final CharSequence timeString = DateUtils.formatDateRange(context,
//                blockStart, blockEnd, TIME_FLAGS);
//
//        return context.getString(R.string.session_subtitle, timeString, roomName);
//    }

    /**
     * Populate the given {@link TextView} with the requested text, formatting
     * through {@link Html#fromHtml(String)} when applicable. Also sets
     * {@link TextView#setMovementMethod} so inline links are handled.
     */
    public static void setTextMaybeHtml(TextView view, String text) {
        if (text.contains("<") && text.contains(">")) {
            view.setText(Html.fromHtml(text));
            view.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            view.setText(text);
        }
    }

//    public static void setSessionTitleColor(long blockStart, long blockEnd, TextView title,
//            TextView subtitle) {
//        long currentTimeMillis = System.currentTimeMillis();
//        int colorId = android.R.color.primary_text_light;
//        int subColorId = android.R.color.secondary_text_light;
//
//        if (currentTimeMillis > blockEnd &&
//                currentTimeMillis < CONFERENCE_END_MILLIS) {
//            colorId = subColorId = R.color.session_foreground_past;
//        }
//
//        final Resources res = title.getResources();
//        title.setTextColor(res.getColor(colorId));
//        subtitle.setTextColor(res.getColor(subColorId));
//    }

    /**
     * Given a snippet string with matching segments surrounded by curly
     * braces, turn those areas into bold spans, removing the curly braces.
     */
    public static Spannable buildStyledSnippet(String snippet) {
        final SpannableStringBuilder builder = new SpannableStringBuilder(snippet);

        // Walk through string, inserting bold snippet spans
        int startIndex = -1, endIndex = -1, delta = 0;
        while ((startIndex = snippet.indexOf('{', endIndex)) != -1) {
            endIndex = snippet.indexOf('}', startIndex);

            // Remove braces from both sides
            builder.delete(startIndex - delta, startIndex - delta + 1);
            builder.delete(endIndex - delta - 1, endIndex - delta);

            // Insert bold style
            builder.setSpan(sBoldSpan, startIndex - delta, endIndex - delta - 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            delta += 2;
        }

        return builder;
    }
    
    /**
     * Parse the given string as a RFC 3339 timestamp, returning the value as
     * milliseconds since the epoch.
     */
    public static long parseTime(String time) {
        sTime.parse3339(time);
        return sTime.toMillis(false);
    }
    
    public static String checkLocalVersion() {
    	return null;   	
    }
    
    public static String checkJsonVersion() {
		return null;
    	
    }
    
    public static String parseDateTime(String dateTime, String dateOrTime, Context context) {
    	DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;		
		if (dateOrTime.equals("date")){
			// get the date only
	    	try {
				date = iso8601Format.parse(dateTime);
				} catch (ParseException e) {
				Log.e("Parsing ISO8601 datetime failed", "Parsing ISO8601 datetime failed", e);
				}

				long when = date.getTime();
				int flags = 0;				
				flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
				flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
				flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
				flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;			
		
				String finalDateTime = android.text.format.DateUtils.formatDateTime(context,
				when, flags);
				 //+ TimeZone.getDefault().getOffset(when)
				Log.i("finalDateTime", finalDateTime);
				dateTime= finalDateTime.substring(finalDateTime.indexOf(",")+2);
				return dateTime;
				
		} else {
			// get the time only					
	    	try {
			date = iso8601Format.parse(dateTime);
			} catch (ParseException e) {
			Log.e("Parsing ISO8601 datetime failed", "Parsing ISO8601 datetime failed", e);
			}
	
			long when = date.getTime();
			int flags = 0;
			flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
			flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
			flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
			flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;			
	
			String finalDateTime = android.text.format.DateUtils.formatDateTime(context,
			when, flags);
			// + TimeZone.getDefault().getOffset(when)
			Log.i("finalDateTime", finalDateTime);
			//txtdateTime.setText(this.c.getString(this.c.getColumnIndex("date")));	
			dateTime = finalDateTime.substring(0, finalDateTime.indexOf(","));	
			return dateTime;
		}		
		
    }
    
    
    
}
