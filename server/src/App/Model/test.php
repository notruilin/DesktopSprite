<?php

//require __DIR__ . '/vendor/autoload.php';
//
//$mail = new \PHPMailer\PHPMailer\PHPMailer();
//$mail->isSMTP();
//$mail->SMTPAuth = true;
//$mail->SMTPSecure = 'ssl';
//$mail->Host = 'smtp.gmail.com';
//$mail->Port = '465';
//$mail->isHTML();
//$mail->Username = 'fireyjy@gmail.com';
//$mail->Password = '178442ae4e';
//$mail->setFrom('fireyjy@gmail.com', 'jianyu');
//$mail->Subject = 'test email';
//$mail->Body = 'a test email from yjy';
//$mail->addAddress('jianyuy2@student.unimelb.edu.au');
//$mail->send();

namespace App\Model;

require_once("WeatherAPI.php");
use App\Model\WeatherAPI as Weather;

$test = new Weather;
print_r($test->byName("Melbourne"));
