<?php
/**
 * Created by PhpStorm.
 * User: yanjianyu
 *
 */

namespace App\Controller;

use App\Model\WeatherAPI;
use App\Views\JsonView;

class WeatherController{
    public function __construct() {
        $this->init();
    }

    private function init() {

    }

    public function weatherbyid($city) {
        $obj = new WeatherAPI();
        $res = $obj->byName($city);
        $returnData = array(
            "type" => 1,
            "res" => $res
        );
        JsonView::render($returnData);
    }

    public function weatherbylocation() {

    }
}