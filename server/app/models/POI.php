<?php

use Jenssegers\Mongodb\Model as Moloquent;

/**
 * Description of Client
 *
 * @author christian
 * 
 * @property MongoId  $_id           The primary key
 * @property string   $name          The poi name 
 * @property string   $created_at    The creation date
 * @property Point[]  $coordinates   The x location
 */
class POI extends Moloquent {

    protected $collection = 'pois';

}
