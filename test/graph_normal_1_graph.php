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
        $num = $num * 10;
        
        $i = 0;
        $j = 0;
        $k = 1;
        
        $history_values = [0,1,2,3,4];
        foreach ($history_values as $value_type) {
            $params = array(
                'output' => array('clock', 'itemid', 'value'),
                'hostids' => $hostids,
                'itemids' => $itemids,
                'sortfield' => $sort,
                'sortorder' => 'DESC',
                'time_from' => strtotime('-1 hour'),
                'time_till' => time(),
                'history' => $value_type,
                'limit' => $num
            );
            $result = $zbx->call('history.get', $params);
            foreach ($items as $item) {
                $itemid = $item['id'];
                $name = $item['name'];
                $value_type = $item['value_type'];
                $unit = $item['units'];
                $data = array();
        
                foreach ($result as $history) {
                    if ($history['itemid'] == $itemid) {
                        $value = $history['value'];
                        $time = date('H:i:s', $history['clock']); 
                        $data = array(
                            'itemid' => $itemid,
                            'name' => $name,
                            'last' => $value,
                            'time' => $time,
                            'unit' => $unit == '' ? 'none' : $unit
                        );
                        $data['timestamp'] = strtotime(date('Y-m-d ').$time); 
                        $itemsData[] = $data;
                    }
                }
                
            }
        }
        
        usort($itemsData, function($a, $b) { 
            return $a['timestamp'] - $b['timestamp'];
        });
        
        $show = array();
        foreach ($itemsData as $data) {
            $show[] = array(
                'itemid' => $data['itemid'],
                'name' => $data['name'],
                'last' => $data['last'],
                'time' => $data['time'],
                'unit' => $data['unit']
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

