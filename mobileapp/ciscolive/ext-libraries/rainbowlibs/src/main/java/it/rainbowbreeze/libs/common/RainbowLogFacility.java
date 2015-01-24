/**
 * Copyright (C) 2010 Alfredo Morresi
 *
 * This file is part of RainbowLibs project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.rainbowbreeze.libs.common;


import static it.rainbowbreeze.libs.common.RainbowContractHelper.checkNotNull;
import it.rainbowbreeze.libs.BuildConfig;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import android.text.TextUtils;
import android.util.Log;

/**
 * Logging facility.
 *
 * Use the logcat command in order to collect log application
 *
 * Thank to Xtralogic, Inc. and the android-log-collector project
 * for the inspiration
 *  http://code.google.com/p/android-log-collector/
 *
 *
 * Usage: logcat [options] [filterspecs]
 options include:
 -s              Set default filter to silent.
 Like specifying filterspec '*:s'
 -f <filename>   Log to file. Default to stdout
 -r [<kbytes>]   Rotate log every kbytes. (16 if unspecified). Requires -f
 -n <count>      Sets max number of rotated logs to <count>, default 4
 -v <format>     Sets the log print format, where <format> is one of:

 brief process tag thread raw time threadtime long

 -c              clear (flush) the entire log and exit
 -d              dump the log and then exit (don't block)
 -g              get the size of the log's ring buffer and exit
 -b <buffer>     request alternate ring buffer
 ('main' (default), 'radio', 'events')
 -B              output the log in binary
 filterspecs are a series of
 <tag>[:priority]

 where <tag> is a log component tag (or * for all) and priority is:
 V    Verbose
 D    Debug
 I    Info
 W    Warn
 E    Error
 F    Fatal
 S    Silent (supress all output)

 '*' means '*:d' and <tag> by itself means <tag>:v

 If not specified on the commandline, filterspec is set from ANDROID_LOG_TAGS.
 If no filterspec is found, filter defaults to '*:I'

 If not specified with -v, format is set from ANDROID_PRINTF_LOG
 or defaults to "brief"
 *
 * REMEMBER to set
 *   <uses-permission android:name="android.permission.READ_LOGS" />
 * in the application manifest
 *
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public class RainbowLogFacility implements IRainbowLogFacility {

    //---------- Private fields
    protected final static String LINE_SEPARATOR =
            System.getProperty("line.separator");

    /** Default log tag */
    protected final String mTag;



    //---------- Constructor
    public RainbowLogFacility(String tag) {
        mTag = checkNotNull(tag, "Log Tag");
    }




    //---------- Public properties




    //---------- Public methods
	/* (non-Javadoc)
     * @see it.rainbowbreeze.libs.common.IRainbowLogFacility#e(java.lang.String)
     */
    public void e(String message)
    { log(Log.ERROR, message); }

    /* (non-Javadoc)
     * @see it.rainbowbreeze.libs.common.IRainbowLogFacility#e(java.lang.Exception)
     */
    public void e(Exception e)
    { log(Log.ERROR, getStackTrace(null, e)); }

    /* (non-Javadoc)
     * @see it.rainbowbreeze.libs.common.IRainbowLogFacility#e(java.lang.String, java.lang.Exception)
     */
    public void e(String methodName, Exception e)
    { log(Log.ERROR, getStackTrace(methodName, e)); }

    /* (non-Javadoc)
     * @see it.rainbowbreeze.libs.common.IRainbowLogFacility#e(java.lang.String, java.lang.String)
     */
    public void e(String methodName, String message)
    { log(Log.ERROR, formatSectionName(methodName, message)); }

    /* (non-Javadoc)
     * @see it.rainbowbreeze.libs.common.IRainbowLogFacility#e(java.lang.String, java.lang.String, java.lang.Exception)
     */
    public void e(String methodName, String message, Exception e) {
        e(methodName, message);
        e(methodName, e);
    }

    /* (non-Javadoc)
     * @see it.rainbowbreeze.libs.common.IRainbowLogFacility#i(java.lang.String)
     */
    public void i(String message)
    { log(Log.INFO, message); }

    /* (non-Javadoc)
     * @see it.rainbowbreeze.libs.common.IRainbowLogFacility#i(java.lang.String, java.lang.String)
     */
    public void i(String methodName, String message)
    { log(Log.INFO, formatSectionName(methodName, message)); }

    /* (non-Javadoc)
     * @see it.rainbowbreeze.libs.common.IRainbowLogFacility#v(java.lang.String)
     */
    public void v(String message)
    { log(Log.VERBOSE, message); }

    /* (non-Javadoc)
     * @see it.rainbowbreeze.libs.common.IRainbowLogFacility#v(java.lang.String, java.lang.String)
     */
    public void v(String methodName, String message)
    { log(Log.VERBOSE, formatSectionName(methodName, message)); }


    /* (non-Javadoc)
     * @see it.rainbowbreeze.libs.common.IRainbowLogFacility#logStartOfActivity(java.lang.String, java.lang.Class, java.lang.Object)
     */
    public void logStartOfActivity(String methodName, Class<? extends Object> activityClass, Object bundleData) {
        String logString = "Activity " + activityClass.getName().toString() + " is starting ";
        logString = logString.concat(null == bundleData ? "for first time" : "after a restart");
        if (TextUtils.isEmpty(methodName))
            v(logString);
        else
            v(methodName, logString);
    }
    /* (non-Javadoc)
     * @see it.rainbowbreeze.libs.common.IRainbowLogFacility#logStartOfActivity(java.lang.Class, java.lang.Object)
     */
    public void logStartOfActivity(Class<? extends Object> activityClass, Object bundleData) {
        logStartOfActivity(null, activityClass, bundleData);
    }






    //---------- Private methods
    protected void log(final int level, String msg) {
        String msgToLog = TextUtils.isEmpty(msg) ? "Empty message to log" : msg;
        if (Log.ERROR == level) {
            Log.e(mTag, msgToLog);
            return;
        }
        if (BuildConfig.DEBUG) return;
        if (Log.VERBOSE == level) {
            Log.v(mTag, msgToLog);
        } else if (Log.DEBUG == level) {
            Log.d(mTag, msgToLog);
        } else if (Log.INFO == level) {
            Log.i(mTag, msgToLog);
        }
    }

    protected String formatSectionName(String methodName, String message) {
        if (TextUtils.isEmpty(methodName)) {
            return message;
        } else {
            return "[" + methodName + "] " + message;
        }
    }

    protected String getStackTrace(String methodName, Exception e) {
        StringBuilder sb = new StringBuilder();
        //log the message
        sb.append(formatSectionName(methodName, "--- Errortrace ---\n"));
        if (e != null) {
            sb.append(formatSectionName(methodName, e.getMessage())).append("\n");
            //and the stack trace
            final Writer result = new StringWriter();
            e.printStackTrace(new PrintWriter(result));
            sb.append(formatSectionName(methodName, result.toString()));
//            StackTraceElement[] items = e.getStackTrace();
//            for (StackTraceElement item : items) {
//                sb.append(formatSectionName(methodName, " at "));
//                sb.append(item.toString()).append("\n");
//            }
        }
        sb.append(formatSectionName(methodName, "------------------"));
        return sb.toString();
    }

}
