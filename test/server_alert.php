<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;
$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
// $Sname=["1","2"];
// $Surl=["http://192.168.1.124/zabbix","http://192.168.1.125/zabbix"];
// $Suser=["Admin","Admin"];
// $Spass=["zabbix","zabbix"];

if(isset($_POST['Surl'])){
	$Sname = $_POST['Sname'];
    $Surl = $_POST['Surl'];
    $Suser = $_POST['Suser'];
    $Spass = $_POST['Spass'];
    $hostids = [];
    $show = array();
    $problems =array();

    try {
        $zbx->login($Surl, $Suser, $Spass, $options);
        $servername=$Sname;
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
                'output' => array('eventid', 'source', 'objectid', 'clock', 'name' , 'severity' ,'suppressed' ,'ns'),
                'hostids' => $hostids[$i]
            );
            $result = $zbx->call('problem.get',$params);
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
            //echo $hostname;
            //$myJSON = json_encode($problems);
            //echo $myJSON;
            //echo 'start i ='.$i . '<br/>';
            $i = $l;
            //echo 'beforeloop i ='.$i . '<br/>';
            if(empty($problems)){
                
            }
            else {
                foreach($problems as $text){
                    $j++;
                    //echo 'i ='.$i . '<br/>';
                    //echo 'j ='.$j . '<br/>';
                    $show[$i]['server']=$servername;
                    $show[$i]['hostname']= $hostname;
                    $show[$i]['problem']= $problems[$j]['problem'];
                    $show[$i]['detail']= $problems[$j]['detail'];
                    $show[$i]['severity']= $problems[$j]['severity'];
                    $show[$i]['time']= $problems[$j]['time'];
                    $i++;
                    $l++;
                }
            }
            $i = $k;
            //echo 'reset i ='.$i . '<br/>';
            //echo 'l ='.$l . '<br/>///<br/>';
            $j=-1;
            $i++;
            $n=0;
            $problems =array();
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
}


