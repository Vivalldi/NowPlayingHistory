package io.cosgrove.nowplaying_history.utils;
/*
  Created by Tyler Cosgrove (vivalldi) on 11/2/17.
  
  Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
   
     http://www.apache.org/licenses/LICENSE-2.0
     
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/


import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateHelper {
    public static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String SHORT_TIME_12_FORMAT = "h:mm a";
    public static final String SHORT_TIME_24_FORMAT = "HH:mm";

    private static String formatDate(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        return sdf.format(date);
    }


    public static String formatDateToISO(Date date) {
        return formatDate(date, ISO_8601_FORMAT);
    }

    public static String formatDateToShortTime12(Date date) {
        return formatDate(date, SHORT_TIME_12_FORMAT);
    }

    public static String formatDateToShortTime24(Date date) {
        return formatDate(date, SHORT_TIME_24_FORMAT);
    }

    private static Date formatStringToDate(String timestamp, String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        try {
            Date date = sdf.parse(timestamp);
            return date;
        } catch (ParseException e) {
            Log.wtf("DateHelper", "Unable to parse date: " + timestamp + " Format: " + dateFormat + " msg:" + e.getMessage());
            return null;
        }
    }

    public static Date formatISOToDate(String timestamp) {
        return formatStringToDate(timestamp, ISO_8601_FORMAT);
    }

    public static Date formatShortTime12ToDate(String timestamp) {
        return formatStringToDate(timestamp, SHORT_TIME_12_FORMAT);
    }

    public static Date formatShortTime24ToDate(String timestamp) {
        return formatStringToDate(timestamp, SHORT_TIME_24_FORMAT);
    }

    private static String changeFormat(String timestamp, String startFormat, String endFormat) {
        String format = null;
        SimpleDateFormat startSDF = new SimpleDateFormat(startFormat, Locale.US);
        SimpleDateFormat endSDF = new SimpleDateFormat(endFormat, Locale.US);
        try {
            Date date = startSDF.parse(timestamp);
            format = endSDF.format(date);
        } catch (ParseException e) {
            Log.wtf("DateHelper", "Unable to parse date: " + e.getMessage());
        } finally {
            startSDF = null;
            endSDF = null;
        }
        return format;
    }

    public static String changeISOToShortTime24(String timestamp) {
        return changeFormat(timestamp, ISO_8601_FORMAT, SHORT_TIME_24_FORMAT);
    }

    public static String changeISOToShortTime12(String timestamp) {
        return changeFormat(timestamp, ISO_8601_FORMAT, SHORT_TIME_12_FORMAT);
    }
}
