<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;
$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$show = array();
$graphs = array();
$graphids = array();
$i=0;

if(isset($_POST['Surl'])){
	$Sname = $_POST['Sname'];
    $Surl = $_POST['Surl'];
    $Suser = $_POST['Suser'];
    $Spass = $_POST['Spass'];
	$hostids = $_POST['hostids'];

	try {
		$zbx->login($Surl, $Suser, $Spass, $options);
		$params = array(
			'output' => array('graphid', 'height', 'name', 'width', 'graphtype', 'percent_left', 'percent_right','show_3d','show_legend','show_work_period','show_triggers','yaxismax','yaxismin','ymax_itemid','ymax_type','ymin_itemid','ymin_type'),
			'hostids' => $hostids,
			'selectGraphItems' => array('gitemid','itemid','color','drawtype','yaxisside','sortorder')
		);
	
		$result = $zbx->call('graph.get',$params);
		foreach($result as $graph) {
			if($graph['graphtype'] == "0"){$text = "normal";}
			elseif($graph['graphtype'] == "1"){$text = "stacked";}
			elseif($graph['graphtype'] == "2"){$text = "pie";}
			elseif($graph['graphtype'] == "3"){$text = "exploded";}
			$show[$i] = array(
				'hostids' => $hostids,
				'graphid' => $graph['graphid'],
				'graphname' => $graph['name'],
				'graphtype' => $text,
				'sortwidthorder' => $graph['width'],
				'height' => $graph['height']
			);
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


