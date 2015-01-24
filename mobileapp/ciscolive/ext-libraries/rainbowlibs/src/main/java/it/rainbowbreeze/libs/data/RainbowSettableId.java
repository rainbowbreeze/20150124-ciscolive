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

package it.rainbowbreeze.libs.data;

import android.content.ContentProvider;

/**
 * Base interface for POJO object that can be stored inside a {@link android.content.ContentProvider}
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public interface RainbowSettableId {

    /**
     * Can set the unique identifier of the entity
     * @param newValue
     */
    public void setId(long newValue);
    
    public long getId();
}
