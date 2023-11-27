<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;
$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$show = array();
$i = 0;

	try {
		$zbx->login('http://192.168.1.174/zabbix', 'Admin', 'zabbix', $options);
		$params = array(
			'output' => array('hostid', 'host', 'name', 'status', 'maintenance_status'),
			'selectInterfaces' => array('interfaceid', 'main', 'type', 'useip', 'ip', 'dns', 'port'),
			'selectParentTemplates' => array('templateid', 'host', 'description', 'name', 'uuid')
		);
	
		$result = $zbx->call('host.get',$params);
		foreach($result as $host) {
			$show[$i]['hostid']= $host['hostid'];
			$show[$i]['hostname']= $host['host'];
			foreach($host['interfaces'] as $interface) {
				$type = $interface['type'];
				if($type == 1){$type ="AGENT";}
				elseif($type == 2){$type ="SNMP";}
				elseif($type == 3){$type ="IPMI";}
				elseif($type == 4){$type ="JMX";}
				else{$type ="NONE";}
				$show[$i]['interface']= $interface['ip'];
				$show[$i]['type']= $type;
			}
			foreach($host['parentTemplates'] as $parentTemplate) {
				$show[$i]['template']= $parentTemplate['name'];
			}
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
	


?>