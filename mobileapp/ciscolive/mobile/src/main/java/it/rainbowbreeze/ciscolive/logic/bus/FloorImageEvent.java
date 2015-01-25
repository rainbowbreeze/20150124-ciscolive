package it.rainbowbreeze.ciscolive.logic.bus;

import android.graphics.Bitmap;

/**
 * Created by alfredomorresi on 25/01/15.
 */
public class FloorImageEvent {
    public final Bitmap bitmap;
    public final double width;
    public final double length;

    public FloorImageEvent(Bitmap bitmap, double width, double length) {
        this.bitmap = bitmap;
        this.width = width;
        this.length = length;
    }

}
