<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;
$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$show = array();
$i = 0;

if(isset($_POST['Surl'])){
	$Sname = $_POST['Sname'];
    $Surl = $_POST['Surl'];
    $Suser = $_POST['Suser'];
    $Spass = $_POST['Spass'];

	try {
        $zbx->login($Surl, $Suser, $Spass, $options);
        // $zbx->login("http://192.168.1.152/zabbix", "Admin", "zabbix", $options);
        $params = array(
            'output' => 'extend',
            'selectSelements' => 'extend'
        );
        $result = $zbx->call('map.get',$params);
    
        foreach ($result as $map) {
            // echo "Map ID: " . $map['sysmapid'] . "<br>";
            $show[$i]['mapid']= $map['sysmapid'];
            $show[$i]['mapname']= $map['name'];
            $i++;
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