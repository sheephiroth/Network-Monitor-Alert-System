<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;
$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$show = array();
$hosts = array();
$text = "";
$text2 = "";
$i = 0;
$n = 0;
$j = 0;
// try {
// 	$zbx->login('http://192.168.1.102/zabbix', 'Admin', 'zabbix', $options);
//     $params = array(
//         'output' => array('groupid','name'),
// 		'selectHosts'=> array('hostid', 'host', 'name')
//     );
//     $result = $zbx->call('hostgroup.get',$params);
//     foreach($result as $hostgroup) {
// 		foreach($hostgroup['hosts'] as $host){
// 			$hosts[$n]['hostname']=$host['name'];
// 			$n++;
// 		}
// 		$n=0;
// 		foreach($hosts as $x){
// 			$k = count($hosts)-1;
// 			if($n < $k){$text3 = ",";}
// 			else {$text3 = "";}
// 			$text = $hosts[$n]['hostname'];
// 			$text2 = "$text2"."$text $text3 ";
// 			$n++;
// 			$text = "";
// 		}
// 		if($text2 == ""){
// 			$text2 = "-";
// 		}
// 		$show[$i]['hostgroup']= $hostgroup['name'];
// 		$show[$i]['members']= $text2;
// 		$text2 = "";
// 		$i++;
// 		$n=0;
// 		$hosts =array();
//     }
// 	$myJSON = json_encode($show);
// 	echo $myJSON;


// } catch (Exception $e) {
// 	print "==== Exception ===\n";
// 	print 'Errorcode: '.$e->getCode()."\n";
// 	print 'ErrorMessage: '.$e->getMessage()."\n";
// 	exit;
// }

if(isset($_POST['Surl'])){
	$Sname = $_POST['Sname'];
    $Surl = $_POST['Surl'];
    $Suser = $_POST['Suser'];
    $Spass = $_POST['Spass'];

	try {
		$zbx->login($Surl, $Suser, $Spass, $options);
		$params = array(
			'output' => array('groupid','name'),
			'selectHosts'=> array('hostid', 'host', 'name')
		);
		$result = $zbx->call('hostgroup.get',$params);
		foreach($result as $hostgroup) {
			foreach($hostgroup['hosts'] as $host){
				$hosts[$n]['hostname']=$host['name'];
				$n++;
			}
			$n=0;
			foreach($hosts as $x){
				$k = count($hosts)-1;
				if($n < $k){$text3 = ",";}
				else {$text3 = "";}
				$text = $hosts[$n]['hostname'];
				$text2 = "$text2"."$text $text3 ";
				$n++;
				$text = "";
			}
			if($text2 == ""){
				$text2 = "-";
			}
			$show[$i]['hostgroup']= $hostgroup['name'];
			$show[$i]['members']= $text2;
			$text2 = "";
			$i++;
			$n=0;
			$hosts =array();
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