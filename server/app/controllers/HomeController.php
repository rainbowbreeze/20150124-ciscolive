<?php

use Peekmo\JsonPath\JsonStore;

class HomeController extends BaseController {

    const FLOOR_WIDTH  = 100;
    const FLOOR_HEIGHT = 50;
    const CLIENTS      = 20;

    public function testUsersCoordinates() {

        $ratioX = Input::get('rx', 1);
        $ratioY = Input::get('ry', 1);

        $client = new CMXClient();
        $data   = $client->getConnectedClients();

        $users = $data['Locations']['entries'];

        $result = [];

        foreach ($users as $user) {
            $result[] = [
                'x'     => $ratioX * $user['MapCoordinate']['x'],
                'y'     => $ratioY * $user['MapCoordinate']['y'],
                'count' => 50
            ];
        }

        return Response::json($result);
    }

    public function getFloorInfo() {
        $data = [
            'floor' => [
                'width'  => self::FLOOR_WIDTH,
                'height' => self::FLOOR_HEIGHT,
                'unit'   => 'feet'
            ],
            'image' => [
                'url'    => 'http://1.bp.blogspot.com/-Tcm4zSikjAw/UPF3Zg4VDqI/AAAAAAAAqOw/S28to9aq1ng/s640/tumblr_mg2hewtqsQ1s16yjvo1_500.jpg',
                'width'  => 500,
                'height' => 375
            ]
        ];

//$client = new CMXClient();
//$data = $client->getFloorInfo();

        return Response::json($data);
    }

    public function getUsersCoordinates() {
        $ratioX = Input::get('rx', 1);
        $ratioY = Input::get('ry', 1);

        $manager = new LocationManager();

        $locations = $manager->getLocations();

        $result = [];

        foreach ($locations as $point) {
            $result[] = [
                'x'     => $ratioX * $point['x'],
                'y'     => $ratioY * $point['y'],
                'mac'   => $point['mac'],
                'count' => 50
            ];
        }

        return Response::json($result);
    }

    public function testFloorInfo() {
        $client = new CMXClient();
        $data   = $client->getFloorInfo();

        return Response::json($data);
    }

    public function _getFloors() {

        $client = new CMXClient();
        $data   = $client->getFloorInfo();

        $store = new JsonStore($data);

        $floors = $store->get('$..Floor.*');

        $result = [];

        foreach ($floors as $floor) {
            $result[] = [
                'name'   => $floor['name'],
                'width'  => $floor['Dimension']['width'],
                'height' => $floor['Dimension']['height'],
                'length' => $floor['Dimension']['length'],
                'image'  => asset('images/avanzi.jpg'),
                'unit'   => $floor['Dimension']['unit'],
                'poi'    => $floor['Zone']
            ];
        }

        return Response::json($result);
    }

    public function getFloors() {
        return [
            [
                "name"   => "AvanziFloor",
                "width"  => 75,
                "height" => 10,
                "length" => 35,
                "image"  => "http:\/\/10.10.30.215\/explore\/server\/public\/images\/avanzi.jpg",
                "unit"   => "FEET",
                "pois"   => [
                    [
                        "name"           => "Biological food",
                        "users"          => 45,
                        "ZoneCoordinate" => ["x" => 35.55, "y" => 0.58]
                    ],
                    [
                        "name"           => "Sustainable agriculture",
                        "users"          => 76,
                        "ZoneCoordinate" => ["x" => 74.55, "y" => 0.12]
                    ],
                    [
                        "name"           => "Gluten free food",
                        "users"          => 32,
                        "ZoneCoordinate" => ["x" => 0.14, "y" => 0.25],
                    ],
                    [
                        "name"           => "Vegan food",
                        "users"          => 90,
                        "ZoneCoordinate" => ["x" => 28.72, "y" => 0.3]
                    ],
                ]
            ]
        ];
    }

}
