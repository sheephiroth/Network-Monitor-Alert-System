<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;
$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$time = "";
$show = array();
$items = array();
$itemids = array();
$sort = ['clock'];
$j=0;
$i=0;
date_default_timezone_set('Asia/Bangkok');
$now = date('Y-m-d H:i:s');

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
            'output' => array('itemid', 'delay', 'hostid', 'interfaceid', 'key_', 'name', 'type', 'url', 'value_type', 'lastclock', 'lastvalue', 'units'),
            'graphids' => $graphids,
            'selectGraphs' => array('graphid', 'height', 'name', 'width')
        );
    
        $result = $zbx->call('item.get', $params);
    
        foreach ($result as $item) {
            foreach ($item['graphs'] as $graph) {
                $graphname = $graph['name'];
                $items[$j]['id'] = $item['itemid'];
                $items[$j]['name'] = $item['name'];
                $items[$j]['value_type'] = $item['value_type'];
                $items[$j]['units'] = $item['units'];
            }
            $itemids[$j] = $items[$j]['id'];
            $j++;
            $i++;
        }
    
        $num =  count($itemids);
        $num = $num * 15;
    
        $i = 0;
        $j = 0;
        $k = 1;
    
        $itemsData = array();
    
        foreach ($items as $item) {
            $itemid = $item['id'];
            $name = $item['name'];
            $value_type = $item['value_type'];
            $unit = $item['units'];
    
            $params = array(
                'output' => array('clock', 'itemid', 'value'),
                'hostids' => $hostids,
                'itemids' => $itemid,
                'sortfield' => $sort,
                'sortorder' => 'DESC',
                'time_from' => strtotime('-1 hour'),
                'time_till' => time(),
                'history' => $value_type,
                'limit' => $num
            );
    
            $result = $zbx->call('history.get', $params);
    
            $data = array(
                'itemid' => $itemid,
                'name' => $name,
                'last' => null,
                'min' => null,
                'avg' => null,
                'max' => null,
                'unit' => $unit
            );
            $data['lastTimestamp'] = 0;
            $itemData = array();
    
            foreach ($result as $history) {
                if ($history['itemid'] == $itemid) {
                    $value = $history['value'];
                    $time = date('Y-m-d H:i:s', $history['clock']);
    
                    if ($data['last'] === null || $history['clock'] > $data['lastTimestamp']) {
                        $data['last'] = $value;
                        $data['lastTimestamp'] = $history['clock'];
                    }
    
                    $itemData[] = $value;
                }
            }
    
            if (count($itemData) > 0) {
                $data['min'] = min($itemData);
                $data['avg'] = array_sum($itemData) / count($itemData);
                $data['max'] = max($itemData);
            }
    
            $itemsData[] = $data;
        }
    
        usort($itemsData, function($a, $b) { 
            return $a['lastTimestamp'] - $b['lastTimestamp'];
        });
    
        $show = array();
    
        foreach ($itemsData as $data) {
            $unit = ($data['unit'] === '') ? 'none' : $data['unit'];
            $show[] = array(
                'itemid' => $data['itemid'],
                'name' => $data['name'],
                'last' => $data['last'],
                'min' => $data['min'],
                'avg' => $data['avg'],
                'max' => $data['max'],
                'unit' => $unit
            );
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

