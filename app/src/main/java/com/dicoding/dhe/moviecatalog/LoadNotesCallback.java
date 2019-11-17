/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog;

import android.database.Cursor;

public interface LoadNotesCallback {
    void postExecute(Cursor notes);
}

