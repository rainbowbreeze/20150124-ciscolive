<?php

use GuzzleHttp\Client;

/**
 * Description of CMXClient
 *
 * @author christian
 */
class CMXClient {

    const VERSION  = 'v1';
    const BASE_URL = 'https://10.10.20.11/api/contextaware/{version}/';
    const USERNAME = 'learning';
    const PASSWORD = 'learning';

    private $client;

    private function get($url) {
        $client = $this->getHTTPClient();
        $result = $client->get($url, [
            'headers' => [
                'Accept' => 'application/json'
            ],
            'config'  => [
                'curl' => [
                    CURLOPT_SSL_VERIFYHOST => false,
                    CURLOPT_SSL_VERIFYPEER => false
                ]
            ]
        ]);

        return $result->json();
    }

    public function getBaseUrl() {
        return $this->getHTTPClient()->getBaseUrl();
    }

    public function getFloorInfo() {
        $client = $this->getHTTPClient();

        $result = $this->get('maps');

        return $result;
    }

    public function getConnectedClients() {
        $result = $this->get('location/clients');

        return $result;
    }

    /**
     * 
     * @return Client
     */
    private function getHTTPClient() {

        if (!$this->client) {
            $this->client = new Client([
                'base_url' => [self::BASE_URL, ['version' => self::VERSION]],
                'defaults' => ['auth' => [self::USERNAME, self::PASSWORD]]
            ]);
        }


        return $this->client;
    }

}
