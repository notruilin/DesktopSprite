<?php
/**
 * Created by PhpStorm.
 * User: yanjianyu
 *
 */
namespace App\Model;

use Mysqli;

class MysqlConnect {
    private $host;
    private $port;
    private $name;
    private $user;
    private $pswd;
    private $char;
    private $mysqli;

    public function __construct()
    {
        $this->init();
        $this->connect();
    }

    private function error($error) {
        die("php error: ".$error);
    }

    private function init(){
        $conf = include './conf.php';
        $this->host = $conf['HOST'];
        $this->port = $conf['PORT'];
        $this->name = $conf['NAME'];
        $this->user = $conf['USER'];
        $this->pswd = $conf['PSWD'];
        $this->char = $conf['CHAR'];
    }

    private function connect() {
        $this->mysqli = new mysqli($this->host, $this->user, $this->pswd, $this->name, $this->port);
        if($this->mysqli->connect_errno) {
            $this->error($this->mysqli->error);
        }
        $this->mysqli->query('set names '.$this->char);
    }

    public function select($sql){
        $res = $this->mysqli->query($sql);
        return $res;
    }

    public function options($sql){
        $res=$this->mysqli->query($sql);
        if(!$res){
            $this->error($this->mysqli->error);
            return 0;
        } else {
            return $res;
        }
    }

    public function close(){
        $this->mysqli->close();
    }

    public function getID(){
        return $this->mysqli->insert_id;
    }
}