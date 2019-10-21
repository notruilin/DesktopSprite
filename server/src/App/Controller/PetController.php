<?php
/**
 * Created by PhpStorm.
 * User: yanjianyu
 *
 */

namespace App\Controller;

use App\Model\MysqlConnect;
use App\Views\JsonView;

class PetController{

    private $mysqliObj;

    public function __construct() {
        $this->init();
    }

    private function init() {
        $this->mysqliObj = new MysqlConnect();
    }

    public function newpet() {
        $uid = $_POST["uid"];
        $name = $_POST["name"];
        $sql="INSERT INTO user (uid, name) values ('".$uid."','".$name."');";
        $res= $this->mysqliObj->options($sql);
        $count= $this->mysqliObj->getID();
        $res = $this->mysqliObj->select("SELECT id, uid, name From user WHERE uid = '".$uid."';");
        $returnData=array(
            'type'=>1,
            'res'=>$res->fetch_assoc(),
        );
        JsonView::render($returnData);
    }

    public function getstate(){

    }

    public function update($uid, $id, $name) {

        $sql1 = "Update pet Set name='".$name."' WHERE id=".$id."or uid=".$uid;

        $res = $this->mysqliObj->options($sql1);
        $returnData = array(
            'type'=> 1
        );
        JsonView::render($returnData);
    }
}