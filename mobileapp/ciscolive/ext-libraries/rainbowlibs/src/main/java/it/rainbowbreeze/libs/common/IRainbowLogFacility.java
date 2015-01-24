/**
 * Copyright (C) 2012 Alfredo Morresi
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

public interface IRainbowLogFacility {

    //---------- Public methods
    public void e(String message);

    public void e(Exception e);

    public void e(String methodName, Exception e);

    public void e(String methodName, String message);

    public void e(String methodName, String message, Exception e);

    public void i(String message);

    public void i(String methodName, String message);

    public void v(String message);

    public void v(String methodName, String message);

    /**
     * Log the start of the activity
     *
     * @param methodName
     * @param activityClass
     * @param bundleData
     */
    public void logStartOfActivity(String methodName,
                                   Class<? extends Object> activityClass, Object bundleData);

    public void logStartOfActivity(Class<? extends Object> activityClass,
                                   Object bundleData);

}