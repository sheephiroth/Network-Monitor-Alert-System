<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;
$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$items = array();
$n = 0;

if(isset($_POST['Surl'])){
	$Sname = $_POST['Sname'];
    $Surl = $_POST['Surl'];
    $Suser = $_POST['Suser'];
    $Spass = $_POST['Spass'];
	$hostids = $_POST['hostids'];

	try {
		$zbx->login($Surl, $Suser, $Spass, $options);
		$params = array(
			'output' => array('hostid', 'host', 'name', 'status', 'maintenance_status'),
			'hostids'=> $hostids,
			'selectItems' => array('itemid', 'delay', 'hostid', 'interfaceid', 'key_', 'name', 'type','url','value_type','lastvalue','lastclock','units')
		);
		$result = $zbx->call('host.get',$params);
		foreach($result as $host) {
			foreach($host['items'] as $item) {
				$time = date('m/d/Y', $item['lastclock']);
				$items[$n]['dataname']= $item['name'];
				$items[$n]['lastvalue']= $item['lastvalue'];
				$items[$n]['unit']= $item['units'];
				$items[$n]['lastcheck']= $time;
				$n++;
			}
		}
		$myJSON = json_encode($items);
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


