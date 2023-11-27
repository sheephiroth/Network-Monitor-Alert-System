<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;
$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$show = array();
$hostids = array();
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
			'output' => array('druleid', 'iprange', 'name', 'delay', 'proxy_hostid','status'),
            'filter' => array('status' => 0)
		);
		$result = $zbx->call('drule.get',$params);
		foreach($result as $drule) {
			$show[$i]['druleid']= $drule['druleid'];
			$show[$i]['drulename']= $drule['name'];
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