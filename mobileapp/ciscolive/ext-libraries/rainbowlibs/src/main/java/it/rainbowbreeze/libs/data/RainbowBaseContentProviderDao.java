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

import it.rainbowbreeze.libs.common.IRainbowLogFacility;

import java.util.List;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Basic Data Access Object for CRUD operation
 * on {@link android.content.ContentProvider} objects.
 * 
 * Unique prerequisite for the object, is to have a numerical (long) id
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public abstract class RainbowBaseContentProviderDao<T extends RainbowSettableId> {
    // ------------------------------------------ Private Fields
    private final String LOG_HASH;
    protected final IRainbowLogFacility mBaseLogFacility;
    protected ContentResolver mMockContentResolver;
    
    // -------------------------------------------- Constructors
    public RainbowBaseContentProviderDao(IRainbowLogFacility logFacility) {
        mBaseLogFacility = logFacility;
        LOG_HASH = getLogHash();
    }

    // --------------------------------------- Public Properties
    /**
     * Default values when an entity is not found inside the content provider
     */
    public static final long NOT_FOUND = -1;
    
    /**
     * Test-only usage, set a new ContentResolver used by other methods instead
     * of the standard one.
     * 
     * @param mockContentResolver new {@link android.content.ContentResolver} to use. Pass <code>null</code> to use
     *  the standard.
     */
    public void setContentResolver(ContentResolver mockContentResolver) {
        mMockContentResolver = mockContentResolver;
    }
    
    /**
     * Returns an item based on its id
     * 
     * @param context a {@link android.content.Context} or an {@link android.app.Activity}, better the latter
     * @param itemId
     * @return
     */
    public T getItem(Context context, long itemId) {
        Uri uri = ContentUris.withAppendedId(getBaseUri(), itemId);
        mBaseLogFacility.v(LOG_HASH, "Query for item " + uri);
       
        Cursor cursor = null; 
        try {
            cursor = performSmartQuery(context, uri, null, null, null, null);            
            if (null == cursor) return null;
            cursor.moveToNext();
            T item = toItem(cursor);
            return item;
        } catch (Exception e) {
            mBaseLogFacility.e(LOG_HASH, e);
            return null;
        } finally {
            if (null != cursor) cursor.close();
        }
    }
    
    /**
     * Returns a cursor for given ids
     * 
     * @param context a {@link android.content.Context} or an {@link android.app.Activity}, better the latter
     * @param ids
     * @return
     */
    public Cursor getCursorForItems(Context context, List<Long> ids) {
        if (null == ids || 0 == ids.size()) return null;
        mBaseLogFacility.v(LOG_HASH, "Query for " + ids.size() + " ids");
        
        StringBuilder selection = new StringBuilder();
        selection.append(BaseColumns._ID).append(" IN (");
        String[] selectionArgs = new String[ids.size()];
        for(int i=0; i<ids.size()-1; i++) {
            selection.append("?,");
            selectionArgs[i] = String.valueOf(ids.get(i));
        }
        selection.append("?)");
        selectionArgs[ids.size()-1] = String.valueOf(ids.get(ids.size()-1));
        
        Cursor cursor = performSmartQuery(
                context,
                getBaseUri(),
                null,
                selection.toString(),
                selectionArgs,
                null);
        return cursor;
    }
    
    /**
     * Inserts a contact in the content provider
     * 
     * @param context
     * @param itemToAdd: the item to add
     * 
     * @return the id of the new item, otherwise {@link AppEnv#NOT_FOUND} is
     *  some errors happened
     */
    public long insertItem(Context context, T itemToAdd) {
        ContentResolver cr = getContentResolver(context);
        ContentValues values = toContentProviderValues(itemToAdd);
        try {
            Uri newItemUri = cr.insert(getBaseUri(), values);
            mBaseLogFacility.v(LOG_HASH, "Insert of item result: " + newItemUri);
            if (null == newItemUri) return NOT_FOUND;
            long newItemId = Long.parseLong(newItemUri.getPathSegments().get(1));
            itemToAdd.setId(newItemId);
            mBaseLogFacility.v(LOG_HASH, "Added item with new id " + newItemId);
            return newItemId;
            
        } catch (Exception e) {
            mBaseLogFacility.e(LOG_HASH, e);
            return NOT_FOUND;
        }
    }
    
    /**
     * Deletes all the item
     * @param context
     * @returns number of deleted items
     */
    public int deleteItem(Context context, long itemId) {
        Uri uri = ContentUris.withAppendedId(getBaseUri(), itemId);
        return deleteForUri(context, uri);
    }
    
    
    /**
     * Deletes all the item
     * @param context
     * @returns number of deleted items
     */
    public int deleteAllItems(Context context) {
        mBaseLogFacility.i(LOG_HASH, "Deleting all items");
        return deleteForUri(context, getBaseUri());
    }
    
    
    /**
     * Updates a given item with new values.
     * @param context
     * @param itemToUpdate
     * @return number of updated items
     */
    public int updateItem(Context context, T itemToUpdate) {
        long itemId = itemToUpdate.getId();
        Uri uri = ContentUris.withAppendedId(getBaseUri(), itemId);
        mBaseLogFacility.v(LOG_HASH, "Update item " + uri);

        ContentResolver cr = getContentResolver(context);
        ContentValues values = toContentProviderValues(itemToUpdate);
        try {
            int affected = cr.update(uri, values, null, null);
            mBaseLogFacility.v(LOG_HASH, "Updated " + affected + " item(s)");
            return affected;
        } catch (Exception e) {
            mBaseLogFacility.e(LOG_HASH, e);
            return 0;
        }
    }
    
    /**
     * Return true if the item already exists in the storage
     * 
     * @param Context
     * @param itemId
     * @return
     */
    public boolean itemExists(Context context, long itemId) {
        Uri uri = ContentUris.withAppendedId(getBaseUri(), itemId);
        mBaseLogFacility.v(LOG_HASH, "Checks for item " + uri);
        Cursor cursor = null;
        
        try {
            cursor = performSmartQuery(
                    context,
                    uri,
                    new String[] { BaseColumns._ID },
                    null,
                    null,
                    null);
            if (null == cursor) return false;
            int count = cursor.getCount();
            return count > 0;
            
        } catch (Exception e) {
            return false;
            
        } finally {
            if (cursor != null)  cursor.close();
        }
    }
    
    /**
     * Returns how many items are present in the storage
     * @param context
     */
    public int getAllItemsCount(Context context) {
        Cursor cursor = null;
        try {
            cursor = performSmartQuery(
                    context,
                    getBaseUri(),
                    new String[] {BaseColumns._ID},
                    null,
                    null,
                    null);
            if (null == cursor) return 0;
            int total = cursor.getCount();
            return total;

        } catch (Exception e) {
            return 0;
            
        } finally {
            if (cursor != null)  cursor.close();
        }
    }
    
    
    /**
     * Transforms properties of an item to ContentProvider values
     * 
     * @param item the item to transform
     * @return the values for the ContentProvider
     */    
    public abstract ContentValues toContentProviderValues(T item);
    
    /**
     * Transform a {@link android.database.Cursor} into an item
     *
     * @param cursor
     * @return an item or null if there was errors during conversion
     */
    public abstract T toItem(Cursor cursor);
    
    
    // ------------------------------------------ Public Methods
    
    /**
     * Returns the log hash to use for logging
     */
    protected abstract String getLogHash();
    
    /**
     * Returns base content provider URI managed by this DAO
     */
    protected abstract Uri getBaseUri();
    
    /**
     * Returns the {@link android.content.ContentResolver} to use, could be the real one or
     * a mock for tests
     * 
     * @param context
     * @return
     */
    protected ContentResolver getContentResolver(Context context) {
        if (null != mMockContentResolver) {
            return mMockContentResolver;
        } else {
            return context.getContentResolver();
        }
    }
    
    /**
     * Performs a query using the {@link android.content.ContentResolver} if a {@link android.content.Context} is used
     *  as parameter, otherwise with {@link android.app.Activity#managedQuery(android.net.Uri, String[], String, String[], String)}
     *  if the parameter is an {@link android.app.Activity}.
     *
     * @param context a {@link android.content.Context} or an {@link android.app.Activity}, better the latter
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    protected Cursor performSmartQuery(
            Context context,
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {
        
        try {
            Cursor cursor;
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                cursor = activity.managedQuery(uri, projection, selection, selectionArgs, sortOrder);
            } else {
                ContentResolver cr = getContentResolver(context);
                cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
            }
            if (null == cursor) {
                mBaseLogFacility.v(LOG_HASH, "Null cursor for uri " + uri);
                return null;
            } else if (cursor.getCount() < 1) {
                mBaseLogFacility.v(LOG_HASH, "No items for uri " + uri);
                cursor.close();
                return null;
            }
            mBaseLogFacility.v(LOG_HASH, "Found " + cursor.getCount() + " result(s)");
            return cursor;
        } catch (Exception e) {
            mBaseLogFacility.e(LOG_HASH, e);
            return null;
        }
    }
    
    
    /**
     * Returns item count
     * 
     * @param context
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @return
     */
    protected int getItemsCount(Context context,
            Uri uri,
            String projection[],
            String selection,
            String[] selectionArgs) {
        
        Cursor cursor = null;
        try {
            cursor = performSmartQuery(
                    context,
                    uri,
                    projection,
                    selection,
                    selectionArgs,
                    null);
    
            int totalCount;
            if (null == cursor) {
                totalCount = 0;
            } else {
                totalCount = cursor.getCount();
            }
            mBaseLogFacility.v(LOG_HASH, "Found " + totalCount + " items from URI " + uri);
            return totalCount;

        } catch (Exception e) {
            mBaseLogFacility.e(LOG_HASH, e);
            return 0;

        } finally {
            if (null != cursor) cursor.close();
        }
    }
    
    /**
     * Deletes all the item
     * @param context
     * @returns number of deleted items
     */
    protected int deleteForUri(Context context, Uri uri) {
        mBaseLogFacility.v(LOG_HASH, "Deleting item(s) for uri " + uri);
        
        ContentResolver cr = getContentResolver(context);
        try {
            int affected = cr.delete(uri, null, null);
            mBaseLogFacility.v(LOG_HASH, "Deleted " + affected + " item(s)");
            return affected;
            
        } catch (Exception e) {
            mBaseLogFacility.e(LOG_HASH, e);
            return 0;
        }
    }
}