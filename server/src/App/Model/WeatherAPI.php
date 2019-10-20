<?php
/**
 * Created by PhpStorm.
 * User: yanjianyu
 * Date: 2019/10/12
 * Time: 4:12
 */

/**
 * appid: a7b1148cba7fd6b01537c79aaf460c6f
 * api.openweathermap.org/data/2.5/weather?q=Melbourne&appid=a7b1148cba7fd6b01537c79aaf460c6f
 * api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}
 */

namespace App\Model;

class WeatherAPI {
    private $appid = "";

    public function __construct()
    {
        $this->init();
    }

    private function init(){
        $this->appid = include './conf_open_weather.php';
    }

    public function byName($name){
        $googleApiUrl = "api.openweathermap.org/data/2.5/weather?q=".$name."&appid=".$this->appid;

        $ch = curl_init();

        curl_setopt($ch, CURLOPT_HEADER, 0);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_URL, $googleApiUrl);
        curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);
        curl_setopt($ch, CURLOPT_VERBOSE, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        $response = curl_exec($ch);

        curl_close($ch);
        $data = json_decode($response);
        $currentTime = time();
        return $data;
    }

    public function byLocation($lat, $lon){

    }
}