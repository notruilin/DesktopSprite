<?php
/**
 * Created by PhpStorm.
 * User: yanjianyu
 *
 */

namespace App\Controller;

use App\Model\MysqlConnect;
use App\Views\JsonView;

class UserController
{

    private $mysqliObj = Null;

    public function __construct() {
        $this->init();
    }

    private function init() {
        $this->mysqliObj = new MysqlConnect();
    }

    public function signup() {
        $name = $_POST["name"];
        $phone = $_POST["phone"];
        $email = $_POST["email"];
        $password=$_POST["pswd"];
        $sql="INSERT INTO user (name, phone, email, pswd) values ('".$name."','".$phone."','".$email."','".$password."');";
        $res= $this->mysqliObj->options($sql);
        $count= $this->mysqliObj->getID();
        $res = $this->mysqliObj->select("SELECT id, name, phone, email From user WHERE email = '".$email."';");
        $returnData=array(
            'type'=>1,
            'res'=>$res->fetch_assoc(),
        );
        JsonView::render($returnData);
    }

    public function login() {
        $email = $_POST["email"];
        $pswd = $_POST["pswd"];
        $res = $this->mysqliObj->select("SELECT id, name, phone, email FROM user WHERE email = '".$email."' and pswd = '".$pswd."';");
        if(!($res->num_rows>0)){
            $returnData = array(
                'type'=> 0,
                'res'=>"The email or password are wrong",
            );
        } else {
            $returnData = array(
                'type'=> 1,
                'res'=>$res->fetch_assoc()
            );
        }
        JsonView::render($returnData);
    }

    public function update(){
        $uid = $_POST["uid"];
        $name = $_POST["name"];
        $phone = $_POST["phone"];
        $email = $_POST["email"];
        $pswd = $_POST["pswd"];

        $sql1 = "Update user Set name='".$name.
            "', phone='".$phone.
            "', email='".$email.
            "', pswd='".$pswd."' WHERE id=".$uid;

        $res = $this->mysqliObj->options($sql1);
        $returnData = array(
            'type'=> 1
        );
        JsonView::render($returnData);
    }
}