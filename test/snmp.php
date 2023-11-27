<?php
require_once "ZabbixApi.php"; 
use IntelliTrend\Zabbix\ZabbixApi;
$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$show = array();


if(isset($_POST['Surl'])){

    $ip = $_POST['ip'];
    $commu = $_POST['commu'];
    $oid = $_POST['oid'];
    $valtype = $_POST['valtype'];
    $newValue = $_POST['newValue'];

    $Sname = $_POST['Sname'];
    $Surl = $_POST['Surl'];
    $Suser = $_POST['Suser'];
    $Spass = $_POST['Spass'];
        try {
            $zbx->login($Surl, $Suser, $Spass, $options);

            // $newValue="R333";
            // $commu="MyCommu";
            // $oid = '1.3.6.1.2.1.1.5.0';
            // $ip = '192.168.56.2'
            // $valtype  = 's';

            $success = snmp2_set($ip, $commu, $oid, $valtype, $newValue);
            if ($success) {
                $show['text']='Success to execute SNMP command.';
            } else {
                $show['text']='Failed to execute SNMP command.';
            }


            

            
            $myJSON = json_encode($show);
            echo $myJSON;
        } catch (Exception $e) {
            print "==== Exception ===\n";
            print 'Errorcode: '.$e->getCode()."\n";
            print 'ErrorMessage: '.$e->getMessage()."\n";
            exit;
        }
} else {
	echo "test";
}




?>


