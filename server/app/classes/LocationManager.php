<?php

use Carbon\Carbon;

/**
 * Description of LocationManager
 *
 * @author christian
 */
class LocationManager {

    public function saveLocations() {
        $client = new CMXClient();
        $data   = $client->getConnectedClients();

        $users = $data['Locations']['entries'];

        Client::query()->delete();
        $counter = 0;

        foreach ($users as $user) {
            $hierarchyString = $user['MapInfo']["mapHierarchyString"];

            if ($hierarchyString === "Avanzi>AvanziBuilding>AvanziFloor") {

                $lastLocatedTime = Carbon::parse($user['Statistics']['lastLocatedTime']);

                $timezoneAdjuntament = 60 * 60 * 7;
                $timeWindowInSeconds = 20;

                $time = Carbon::now()->subSeconds($timezoneAdjuntament + $timeWindowInSeconds);

                if ($lastLocatedTime->gte($time)) {
                    $insertData = [
                        'mac' => $user['macAddress'],
                        'x'   => $user['MapCoordinate']['x'],
                        'y'   => $user['MapCoordinate']['y']
                    ];

                    Client::query()->insert($insertData);
                    History::query()->insert($insertData);

                    $counter ++;
                }
            }
        }

        return $counter;
    }

    public function getLocations() {
        return Client::all();
    }
    

    public function clear() {
        Client::query()->delete();
        History::query()->delete();
    }

}
