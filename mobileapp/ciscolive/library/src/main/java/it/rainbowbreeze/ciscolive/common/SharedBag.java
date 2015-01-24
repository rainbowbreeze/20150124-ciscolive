package it.rainbowbreeze.ciscolive.common;

/**
 * Created by alfredomorresi on 19/10/14.
 */
public abstract class SharedBag {
    public final static String APP_NAME_LOG = "PicAmazement";


    public final static String WEAR_PATH_AMAZINGPICTURE = "/AmazingPicture";
    public final static String WEAR_PATH_REMOVEPICTURE = "/RemovePicture";
    public final static String WEAR_PATH_UPLOADPICTURE = "/UploadPicture";
    public final static String WEAR_PATH_OPENPICTURE = "/OpenPicture";

    public final static String WEAR_DATAMAPITEM_PICTUREID = "PictureId";
    public static final String WEAR_DATAMAPITEM_TIMESTAMP = "Timestamp"; // Avoids caching

    // Also registered for the service
    public final static String INTENT_ACTION_REMOVEPICTURE = "it.rainbowbreeze.picama.Action.Picture.Remove";
    public static final String INTENT_ACTION_UPLOADPICTURE = "it.rainbowbreeze.picama.Action.Picture.Upload";
    public static final String INTENT_ACTION_OPENONDEVICE = "it.rainbowbreeze.picama.Action.Picture.OpenOnDevice";
    public final static String INTENT_EXTRA_PICTUREID = "Extra.PictureId";
    public final static String INTENT_EXTRA_NOTIFICATIONID = "Extra.NotificationId";

    public static final long ID_NOT_SET = -1;
    public static final long GOOGLE_API_CLIENT_TIMEOUT = 30;  // seconds
}
