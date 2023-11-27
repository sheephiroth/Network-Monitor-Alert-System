<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;
$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$show = array();
$hostids = array();
$dhostids = array();
$druleids = array();
$druleids ="3";
$i = 0;

// if(isset($_POST['Surl'])){
// 	$Sname = $_POST['Sname'];
//     $Surl = $_POST['Surl'];
//     $Suser = $_POST['Suser'];
//     $Spass = $_POST['Spass'];
// 	$druleids = $_POST['druleids'];

	try {
		// $zbx->login($Surl, $Suser, $Spass, $options);
        $zbx->login("http://192.168.1.152/zabbix", "Admin", "zabbix", $options);

		$params = array(
			'output' => array('hostid', 'host', 'name', 'status', 'maintenance_status'),
			'selectInterfaces' => array('interfaceid', 'main', 'type', 'useip', 'ip', 'dns', 'port')
		);
		$result = $zbx->call('host.get',$params);
		$i = 0;
		foreach($result as $host) {
			$hostids[$i]['hostid']= $host['hostid'];
			$show[$i]['hostid']= $host['hostid'];
			$show[$i]['hostname']= $host['host'];
			foreach($host['interfaces'] as $interface) {
				$show[$i]['interface']= $interface['ip'];
			}
			$i++;
		}

		$i=0;
		
		$params = array(
			'output' => array('druleid', 'iprange', 'name', 'delay', 'proxy_hostid','status','dhostids'),
            'filter' => array('status' => 0),
			'druleids' => $druleids,
			'selectDHosts' => array('dhostid', 'druleid', 'lastdown', 'lastup', 'status')
		);
		$result = $zbx->call('drule.get',$params);
		foreach($result as $drule) {
			foreach($drule['dhosts'] as $dhosts) {
				if (isset($hostids[$i]['hostid'])) {
					$currentTimestamp = time();
					$show[$i]['dhostid'] = $dhosts['dhostid'];
					$lastupTimestamp = $dhosts['lastup'];
					$lastdownTimestamp = $dhosts['lastdown'];
					$lastupDiff = $currentTimestamp - $lastupTimestamp;
					$lastdownDiff = $currentTimestamp - $lastdownTimestamp;
					if ($dhosts['lastup'] !== "0") {
						$show[$i]['dupdown'] = "UP " . formatTimeDifference($lastupDiff);
					}
					if ($dhosts['lastdown'] !== "0") {
						$show[$i]['dupdown'] = "DOWN " . formatTimeDifference($lastdownDiff);
					}
				}
				$i++;
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
	
// } else {
// 	echo "test";
// }


function formatTimeDifference($seconds) {
	$hours = floor($seconds / 3600);
	$minutes = floor(($seconds % 3600) / 60);
	$seconds = $seconds % 60;

	return sprintf('%02dh %02dm %02ds ago', $hours, $minutes, $seconds);
}

?>