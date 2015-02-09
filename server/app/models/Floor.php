<?php

use Jenssegers\Mongodb\Model as Moloquent;

/**
 * Description of Client
 *
 * @author christian
 * 
 * @property MongoId   $_id        The primary key
 * @property string    $name       The floor name
 * @property MongoId[] $pois       The point of interests references 
 * @property string    $created_at The creation date
 */
class Floor extends Moloquent {

    protected $collection = 'floors';
    
    public function pois () {
        return $this->hasMany('POI');
    }

}
