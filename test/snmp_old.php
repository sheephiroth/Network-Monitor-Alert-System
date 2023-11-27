<?php
require_once "ZabbixApi.php"; 
use IntelliTrend\Zabbix\ZabbixApi;
$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$show = array();
$fail='Failed to execute SNMP command.';


if(isset($_POST['Surl'])){
	$SNMP_NO = $_POST['SNMP_NO'];

    $ip = $_POST['ip'];
    $commu = $_POST['commu'];

    $Sname = $_POST['Sname'];
    $Surl = $_POST['Surl'];
    $Suser = $_POST['Suser'];
    $Spass = $_POST['Spass'];
        try {
            $zbx->login($Surl, $Suser, $Spass, $options);

            //CHANGE HOSTNAME
            if($SNMP_NO == '1'){
            $newName=$_POST['newName'];
            $oid = '1.3.6.1.2.1.1.5.0';
            $valuetype= 's';
            $success = snmp2_set($ip, $commu, $oid, 's', $newName);
                if ($success) {
                    $show['text']='Host name changed.';
                } else {
                    $show['text']=$fail;
                }
            }

            //CHANGE IP FORWARDING (1 forwarding , 2 not-forearding)
            if($SNMP_NO == '2'){
                $newValue=$_POST['newValue'];
                $oid = '1.3.6.1.2.1.4.1.0';
                $success = snmp2_set($ip, $commu, $oid, 'i', $newValue);
                if ($success) {
                    $show['text']='ipForwarding value changed.';
                } else {
                    $show['text']=$fail;
                }
            }


            // $newValue="R333";
            // $commu="MyCommu";
            // $oid = '1.3.6.1.2.1.1.5.0';
            // $ip = '192.168.56.2'
            // $valuetype  = 's';

            // $success = snmp2_set($ip, $commu, $oid, $valuetype, $newValue);
            // if ($success) {
            //     $show['text']='Success to execute SNMP command.';
            // } else {
            //     $show['text']=$fail;
            // }

            
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


