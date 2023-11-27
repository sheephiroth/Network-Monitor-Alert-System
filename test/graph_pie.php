<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;
$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$show = array();
$graphids = "";
$sort = ['clock'];
$i=0;

if(isset($_POST['Surl'])){
	$Sname = $_POST['Sname'];
    $Surl = $_POST['Surl'];
    $Suser = $_POST['Suser'];
    $Spass = $_POST['Spass'];
    $graphids = $_POST['graphid'];
    $hostids = $_POST['hostids'];

	try {
		$zbx->login($Surl, $Suser, $Spass, $options);
		$params = array(
            'output' => array('graphid', 'itemid', 'itemids', 'type', 'width', 'graphtype', 'color', 'drawtype', 'sortorder', 'yaxisside', 'calc_fnc'),
            'graphids' => $graphids
        );
    
        $result = $zbx->call('graphitem.get', $params);
    
        if (!empty($result)) {
            foreach ($result as $graphItem) {
                $itemid = $graphItem['itemid'];
                $itemParams = array(
                    'output' => array('name', 'key_', 'units'),
                    'itemids' => $itemid
                );
                $itemResult = $zbx->call('item.get', $itemParams);
                $unit = '';
                if (!empty($itemResult)) {
                    $unit = $itemResult[0]['units'];
                    $name = $itemResult[0]['name'];
                }
                $show[] = array(
                    'graphid' => $graphItem['graphid'],
                    'itemid' => $graphItem['itemid'],
                    'name' => $name,
                    'color' => $graphItem['color'],
                    'drawtype' => $graphItem['drawtype'],
                    'yaxisside' => $graphItem['yaxisside'],
                    'sortorder' => $graphItem['sortorder'],
                    'calc_fnc' => $graphItem['calc_fnc'],
                    'value' => '',
                    'unit' => $unit
                );
            }
            
            foreach ($show as &$graphItem) {
                $itemids = $graphItem['itemid'];
                $params = array(
                    'output' => array('clock', 'itemid', 'ns', 'value'),
                    'hostids' => $hostids,
                    'itemids' => $itemids,
                    'sortfield' => $sort,
                    'sortorder' => 'DESC',
                    'limit' => 1
                );
            
                $result2 = $zbx->call('history.get',$params);
            
                if (!empty($result2)) {
                    $graphItem['value'] = $result2[0]['value'];
                }
            }
        } else {
            echo "No graphs found for graph with ID {$graphids} <br>";
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