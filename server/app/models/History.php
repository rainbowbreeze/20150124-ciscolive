<?php

use Jenssegers\Mongodb\Model as Moloquent;

/**
 * Description of Client
 *
 * @author christian
 * 
 * @property MongoId $_id        The primary key
 * @property string  $mac        The mac address
 * @property string  $created_at The creation date
 * @property int     $x          The x location
 * @property int     $y          The y location
 */
class History extends Moloquent {

    protected $collection = 'history';

}
