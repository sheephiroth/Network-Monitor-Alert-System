<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;
$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$items = array();
$triggers = array();
$n = 0;
$m = 0;
$hostids = ['10561'];
$unit = "";
$show = array();


function checkstr(string $haystack, array $needles, bool $ignoreCase = false) {
    if (! $ignoreCase) {
        $haystack = mb_strtolower($haystack);
    }
    
    foreach ($needles as $needle) {
        if (str_contains($haystack, $ignoreCase ? mb_strtolower($needle) : $needle)) {
            return true;
        }
    }
    
    return false;
}
$checktrigger=[
	"temperature is above critical threshold",
	"power supply is in critical state",
    "c:: disk is overloaded",
    "high cpu temperature",
    "high gpu temperature"
];




    if(isset($_POST['Surl'])){
        $Sname = $_POST['Sname'];
        $Surl = $_POST['Surl'];
        $Suser = $_POST['Suser'];
        $Spass = $_POST['Spass'];
        $hostids = $_POST['hostids'];
    
        try {
            $zbx->login($Surl, $Suser, $Spass, $options);
            $params = array(
                'output' => array('hostid', 'host', 'name', 'status', 'maintenance_status'),
                'hostids'=> $hostids,
                'selectTriggers' => array('triggerid', 'description', 'expression', 'event_name', 'opdata', 'error', 'state', 'value', 'priority')
            );
        
            $result = $zbx->call('host.get',$params);
            foreach($result as $host) {
                foreach($host['triggers'] as $trigger) {
                    if(isset($trigger['triggerid'])){
                        if($trigger['value'] == 1){
                            $triggers[$m]['description']= $trigger['description'];
                            $triggers[$m]['value']= $trigger['value'];
                            $triggers[$m]['priority']= $trigger['priority'];
                        }
                    }
                }
            }
    
            $i = 0;
            $j = 0;
            if(isset($triggers)){
                foreach ($triggers as $data2){
                    if(checkstr($triggers[$j]['description'],$checktrigger) == true){
                        // $show[$i]['name'] = $triggers[$j]['description'];
                        // $show[$i]['value']= $triggers[$j]['value'];
                        $i++;
                    }
                    if($triggers[$j]['priority'] == 5){
                        $i++;
                    }
                    $j++;
                }
            }
            $trigger_value = strval($i);
            $show = [$trigger_value];
    
            $myJSON = json_encode($show);
            echo $myJSON."<br><br>";
        
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