<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;
$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$show = array();
$test = array();
$items = array();
$itemids = array();
$sort = ['clock'];
$j=0;
$i=0;
$graphname = "";
$text2 = "";
$today = microtime(true)+18000;
$now = date('Y-m-d H:i:s', $today);

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
            'output' => array('itemid', 'delay', 'hostid', 'interfaceid', 'key_', 'name', 'type', 'url', 'value_type', 'lastclock', 'lastvalue'),
            'graphids' => $graphids,
            'selectGraphs' => array('graphid', 'height', 'name', 'width')
        );
    
        $result = $zbx->call('item.get',$params);
    
        foreach($result as $item) {
            foreach($item['graphs'] as $graph){
                $graphname = $graph['name'];
                $items[$j]['id']=$item['itemid'];
                $items[$j]['name']=$item['name'];
            }
            $itemids[$j]=$items[$j]['id'];
            $j++;
            $i++;
        }
    
        
    
    
        $num =  count($itemids);
        $num2 =  count($itemids);
        $num = $num * 10;
    
        $i=0;
        $j=0;
        $k=1;
    
        $params = array(
            'output' => array('clock', 'itemid', 'ns', 'value'),
            'hostids' => $hostids,
            'itemids' => $itemids,
            'sortfield' => $sort,
            'sortorder' => 'ASC',
            'limit' => $num
        );
    
        $result = $zbx->call('history.get',$params);
    
        foreach($result as $history) {
            $seconds = $today - ($history['ns'] /1000000);
            $time2 = date('Y-m-d H:i:s', $seconds);
            foreach($items as $x){
                if($items[$j]['id'] == $history['itemid']){
                    $name = $items[$j]['name'];
                }
                $j++;
            }
            $show[$i]['no']=$k;
            $show[$i]['itemid']=$history['itemid'];
            $show[$i]['name']=$name;
            $show[$i]['value']=$history['value'];
            $show[$i]['time']=$time2;
            $i++;
            $j=0;
            $k++;
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

