<?php

require_once "../vendor/autoload.php";
$request = $_SERVER['REQUEST_URI'];
$array = explode("/", $request);
$id = intval(end($array));

$router = [
    '/register' => 'UserController@signup',
    '/login' => 'UserController@login',
    "/user/$uid" => 'UserController@update',
    "/user/$uid/pet" => 'PetController@newpet',
    "/user/$uid/pets" => 'PetController@getstate',
    "/user/$uid/pet/$id/name/$name" => 'PetController@update',
    "/weather/$city" => 'WeatherController@weatherbyid',
    "/weather/$lon/$lat" => 'WeatherController@weatherbylocation',
];

function checkRouting($router, $requestUri)
{
    if (isset($router[$requestUri]))
    {
        return true;
    }

    return false;
}

if (checkRouting($router, $request))
{
    $user = new \App\Controller\UserController();
    $pet = new \App\Controller\PetController();
    $weather = new \App\Model\WeatherAPI();

    if ($request == '/register') {
        $user->signup();
    }

    if ($request == '/login') {
        $home->news();
    }

    if ($request == "/user/$uid") {
        $user->login($uid);
    }

    if ($request == "/user/$uid/pet") {
        $pet->newpet($uid);
    }

    if ($request == "/user/$uid/pets") {
        $pet->getstate();
    }

    if ($request == "/user/$uid/pet/$id/name/$name") {
        $pet->update($uid, $id, $name);
    }

    if ($request == "/weather/$city") {
        $weather->byName($city);
    }

} else {
    $res = array(
        "type"=>404,
        "res"=>"NOT FOUND"
    );
    die(json_encode($res));
}