<?php
session_start();
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;
$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
//$hostids = ['10432'];
//$hostname = "";
//$hostids = $_SESSION['hostids'];
$problems =array();

// if(isset($_POST['get'])){
// 	$hostids = $_POST['hostids'];
	
// 	try {
// 		$zbx->login('http://192.168.1.102/zabbix', 'Admin', 'zabbix', $options);
// 		$params = array(
// 			'output' => array('hostid', 'host', 'name', 'status', 'maintenance_status','hostids'),
// 			'hostids'=> $hostids,
// 			'selectInterfaces' => array('interfaceid', 'main', 'type', 'useip', 'ip', 'dns', 'port')
// 		);
	
// 		$result = $zbx->call('host.get',$params);
// 		foreach($result as $host) {
// 			$hostname = $host['name'];
// 		}
// 		$params = array(
// 			'output' => array('eventid', 'source', 'objectid', 'clock', 'name' , 'severity' ,'suppressed' ,'ns'),
// 			'hostids' => $hostids,
// 			'selectInterfaces' => array('interfaceid', 'main', 'type', 'useip', 'ip', 'dns', 'port')
// 		);
// 		//echo "$hostname<br/>";
// 		$show['hostname']= $hostname;
// 		$result = $zbx->call('problem.get',$params);
// 		$n =0;
// 		foreach($result as $problem) {
// 			$time = date('m/d/Y', $problem['clock']);
// 			if($problem['severity']== 0){$pb="not classified";}
// 			elseif($problem['severity']== 1){$pb="information";}
// 			elseif($problem['severity']== 2){$pb="warning";}
// 			elseif($problem['severity']== 3){$pb="average";}
// 			elseif($problem['severity']== 4){$pb="high";}
// 			elseif($problem['severity']== 5){$pb="disaster";}
// 			//printf("Name:%s<br/> Time\n:\n$time &nbsp;&nbsp; Severity\n:\n$pb <br/>", $problem['name']);
// 			//print("=================================================<br/>");
// 			$problems[$n]['problem']= $n+1;
// 			$problems[$n]['detail']= $problem['name'];
// 			$problems[$n]['severity']= $pb;
// 			$problems[$n]['time']= $time;
// 			$n++;
// 		}
// 		$myJSON = json_encode($problems);
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
			'output' => array('hostid', 'host', 'name', 'status', 'maintenance_status','hostids'),
			'hostids'=> $hostids,
			'selectInterfaces' => array('interfaceid', 'main', 'type', 'useip', 'ip', 'dns', 'port')
		);
	
		$result = $zbx->call('host.get',$params);
		foreach($result as $host) {
			$hostname = $host['name'];
		}
		$params = array(
			'output' => array('eventid', 'source', 'objectid', 'clock', 'name' , 'severity' ,'suppressed' ,'ns'),
			'hostids' => $hostids,
			'selectInterfaces' => array('interfaceid', 'main', 'type', 'useip', 'ip', 'dns', 'port')
		);
		$show['hostname']= $hostname;
		$result = $zbx->call('problem.get',$params);
		$n =0;
		foreach($result as $problem) {
			$time = date('m/d/Y', $problem['clock']);
			if($problem['severity']== 0){$pb="not classified";}
			elseif($problem['severity']== 1){$pb="information";}
			elseif($problem['severity']== 2){$pb="warning";}
			elseif($problem['severity']== 3){$pb="average";}
			elseif($problem['severity']== 4){$pb="high";}
			elseif($problem['severity']== 5){$pb="disaster";}
			$problems[$n]['problem']= $n+1;
			$problems[$n]['detail']= $problem['name'];
			$problems[$n]['severity']= $pb;
			$problems[$n]['time']= $time;
			$n++;
		}
		$myJSON = json_encode($problems);
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