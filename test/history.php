<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;
$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$hostids = [];
$show = array();
$events =array();
// try {
// 	$zbx->login('http://192.168.1.102/zabbix', 'Admin', 'zabbix', $options);
//     $params = array(
// 		'output' => array('hostid', 'host', 'name', 'status', 'maintenance_status','hostids'),
// 	);

// 	$result = $zbx->call('host.get',$params);
// 	foreach($result as $host) {
//         $hostids[] = $host['hostid'];
// 	}
//     $i = 0;
// 	$n = 0;
//     $j = -1;
//     $k = 0;
//     $l = 0;
// 	foreach ($hostids as $x){
//         $k = $i;
//         $params = array(
//             'output' => array('hostid', 'host', 'name', 'status', 'maintenance_status','hostids'),
//             'hostids'=> $hostids[$i]
//         );
    
//         $result = $zbx->call('host.get',$params);
//         foreach($result as $host) {
//             $hostname = $host['name'];
//         }
//         $params = array(
//             'output' => array('eventid', 'source', 'objectid', 'clock', 'name' , 'severity' ,'suppressed' ,'value'),
//             'hostids' => $hostids[$i]
//         );
//         $result = $zbx->call('event.get',$params);
//         foreach($result as $event) {
//             $time = date('m/d/Y', $event['clock']);
//             if($event['value']== 0){$status="RESOLVED";}
//             elseif($event['value']== 1){$status="PROBLEM";}
//             if($event['severity']== 0){$pb="not classified";}
//             elseif($event['severity']== 1){$pb="information";}
//             elseif($event['severity']== 2){$pb="warning";}
//             elseif($event['severity']== 3){$pb="average";}
//             elseif($event['severity']== 4){$pb="high";}
//             elseif($event['severity']== 5){$pb="disaster";}
// 			$events[$n]['problem']= $n+1;
// 			$events[$n]['detail']= $event['name'];
// 			$events[$n]['severity']= $pb;
//             $events[$n]['status']= $status;
//             $events[$n]['time']= $time;
// 			$n++;
//         }
//         $i = $l;
//         if(empty($events)){
            
//         }
//         else {
//             foreach($events as $text){
//                 $j++;
//                 $show[$i]['hostname']= $hostname;
//                 $show[$i]['problem']= $events[$j]['problem'];
//                 $show[$i]['detail']= $events[$j]['detail'];
//                 $show[$i]['severity']= $events[$j]['severity'];
//                 $show[$i]['status']= $events[$j]['status'];
//                 $show[$i]['time']= $events[$j]['time'];
//                 $i++;
//                 $l++;
//             }
//         }
//         $i = $k;
//         $j=-1;
//         $i++;
// 		$n=0;
//         $events =array();
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
            'output' => array('hostid', 'host', 'name', 'status', 'maintenance_status','hostids'),
        );
    
        $result = $zbx->call('host.get',$params);
        foreach($result as $host) {
            $hostids[] = $host['hostid'];
        }
        $i = 0;
        $n = 0;
        $j = -1;
        $k = 0;
        $l = 0;
        foreach ($hostids as $x){
            $k = $i;
            $params = array(
                'output' => array('hostid', 'host', 'name', 'status', 'maintenance_status','hostids'),
                'hostids'=> $hostids[$i]
            );
        
            $result = $zbx->call('host.get',$params);
            foreach($result as $host) {
                $hostname = $host['name'];
            }
            $params = array(
                'output' => array('eventid', 'source', 'objectid', 'clock', 'name' , 'severity' ,'suppressed' ,'value'),
                'hostids' => $hostids[$i]
            );
            $result = $zbx->call('event.get',$params);
            foreach($result as $event) {
                $time = date('m/d/Y', $event['clock']);
                if($event['value']== 0){$status="RESOLVED";}
                elseif($event['value']== 1){$status="PROBLEM";}
                if($event['severity']== 0){$pb="not classified";}
                elseif($event['severity']== 1){$pb="information";}
                elseif($event['severity']== 2){$pb="warning";}
                elseif($event['severity']== 3){$pb="average";}
                elseif($event['severity']== 4){$pb="high";}
                elseif($event['severity']== 5){$pb="disaster";}
                $events[$n]['problem']= $n+1;
                $events[$n]['detail']= $event['name'];
                $events[$n]['severity']= $pb;
                $events[$n]['status']= $status;
                $events[$n]['time']= $time;
                $n++;
            }
            $i = $l;
            if(empty($events)){
                
            }
            else {
                foreach($events as $text){
                    $j++;
                    $show[$i]['hostname']= $hostname;
                    $show[$i]['problem']= $events[$j]['problem'];
                    $show[$i]['detail']= $events[$j]['detail'];
                    $show[$i]['severity']= $events[$j]['severity'];
                    $show[$i]['status']= $events[$j]['status'];
                    $show[$i]['time']= $events[$j]['time'];
                    $i++;
                    $l++;
                }
            }
            $i = $k;
            $j=-1;
            $i++;
            $n=0;
            $events =array();
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