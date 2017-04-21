/*
 * Copyright (C) 2008 The Android Open Source Project
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

/**
 * package-level logging flag
 */

package com.jarry.jayvoice.util;

public class Logger {
	public final static String LOGTAG = "JayVoice";

	public static boolean IFLOG = true;

	public static void v(String logMe) {
		if (IFLOG)
			android.util.Log.v(LOGTAG, logMe);
	}

	public static void i(String logMe) {
		if (IFLOG)
			android.util.Log.i(LOGTAG, logMe);
	}

	public static void e(String logMe) {
		if (IFLOG)
			android.util.Log.e(LOGTAG, logMe);
	}

	public static void e(String logMe, Exception ex) {
		if (IFLOG)
			android.util.Log.e(LOGTAG, logMe, ex);
	}

	public static void v(int logMe) {
		v(logMe + "");
	}

	public static void i(int logMe) {
		i(logMe + "");
	}

	public static void e(int logMe) {
		e(logMe + "");
	}

	public static void e(int logMe, Exception ex) {
		e(logMe + "", ex);
	}

	public static void d(String string) {
		// TODO Auto-generated method stub
		if (IFLOG)
			android.util.Log.d(LOGTAG, string);
	}
}
