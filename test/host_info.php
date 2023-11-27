<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;
$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$show = [];
//$hostids = $_POST['hostids'];
//$hostids = "10432";
//$hostids = $_POST['hostids'];

// if(isset($_POST['get'])){
// 	$hostids = $_POST['hostids'];

// 	try {
// 		$zbx->login('http://192.168.1.102/zabbix', 'Admin', 'zabbix', $options);
// 		$params = array(
// 			'output' => array('hostid', 'host', 'name', 'status', 'maintenance_status'),
// 			'hostids'=> $hostids,
// 			'selectInterfaces' => array('interfaceid', 'main', 'type', 'useip', 'ip', 'dns', 'port')
// 		);
	
// 		$result = $zbx->call('host.get',$params);
// 		foreach($result as $host) {
// 			$show['hostids']= $hostids;
// 			$show['hostname']= $host['host'];
// 			//printf("Name : %s <br/>",$host['host']);
// 			foreach($host['interfaces'] as $interface) {
// 				$type = $interface['type'];
// 				if($type == 1){$type ="AGENT";}
// 				elseif($type == 2){$type ="SNMP";}
// 				elseif($type == 3){$type ="IPMI";}
// 				elseif($type == 4){$type ="JMX";}
// 				else{$type ="NONE";}
// 				$show['interface']= $interface['ip'];
// 				$show['type']= $type;
// 				//printf("IP : %s   <br/>Type : $type <br/>",$interface['ip']);
// 			}
// 			if($host['status']==0){$status="Enabled";}
// 			elseif($host['status']==1){$status="Disabled";}
// 			$show['status']= $status;
// 			//printf("Status : $status");
// 		}
// 		$myJSON = json_encode($show);
// 		echo $myJSON;
	
// 	} catch (Exception $e) {
// 		print "==== Exception ===\n";
// 		print 'Errorcode: '.$e->getCode()."\n";
// 		print 'ErrorMessage: '.$e->getMessage()."\n";
// 		exit;
// 	}
	
// } else {
// 	echo "test";
// }


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
			'selectInterfaces' => array('interfaceid', 'main', 'type', 'useip', 'ip', 'dns', 'port'),
			'selectParentTemplates' => array('templateid', 'host', 'description', 'name', 'uuid')
		);
	
		$result = $zbx->call('host.get',$params);
		foreach($result as $host) {
			$show['hostids']= $hostids;
			$show['hostname']= $host['host'];
			//printf("Name : %s <br/>",$host['host']);
			foreach($host['interfaces'] as $interface) {
				$type = $interface['type'];
				if($type == 1){$type ="AGENT";}
				elseif($type == 2){$type ="SNMP";}
				elseif($type == 3){$type ="IPMI";}
				elseif($type == 4){$type ="JMX";}
				else{$type ="NONE";}
				$show['interface']= $interface['ip'];
				$show['type']= $type;
				//printf("IP : %s   <br/>Type : $type <br/>",$interface['ip']);
			}
			if($host['status']==0){$status="Enabled";}
			elseif($host['status']==1){$status="Disabled";}
			$show['status']= $status;
			foreach($host['parentTemplates'] as $parentTemplate) {
				$show['template']= $parentTemplate['name'];
			}
			//printf("Status : $status");
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