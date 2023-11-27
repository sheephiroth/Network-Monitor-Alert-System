<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;

$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$show = [];

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
			'selectInterfaces' => array('interfaceid', 'main', 'type', 'useip', 'ip', 'dns', 'port')
		);
        $result = $zbx->call('host.get',$params);
		foreach($result as $host) {
			foreach($host['interfaces'] as $interface) {
				$show['interface']= $interface['ip'];
			}
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