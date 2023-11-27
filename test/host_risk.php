<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;
$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$items = array();
$triggers = array();
$n = 0;
$m = 0;
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

$checkdata=[
	"ac power supply: power supply status",
	"i/o cont inlet: temperature",
	"i/o cont inlet: temperature status",
	"i/o cont outlet: temperature",
	"i/o cont outlet: temperature status",
	"npe inlet: temperature",
	"npe inlet: temperature status",
	"npe outlet: temperature",
	"npe outlet: temperature status",
	"cpu utilization",
	"(c:): space utilization",
	"memory utilization",
	"/: space utilization",
	"available memory in %",
	"sda: Disk utilization",
	"cpu temperature",
	"gpu temperature"
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
				'selectItems' => array('itemid', 'delay', 'hostid', 'interfaceid', 'key_', 'name', 'type','url','value_type','lastvalue','lastclock','units')
			);
		
			$result = $zbx->call('host.get',$params);
			foreach($result as $host) {
				foreach($host['items'] as $item) {
					$items[$n]['dataname']= $item['name'];
					$items[$n]['lastvalue']= $item['lastvalue'];
					$n++;
				}
			}
	
			$i = 0;
			$j = 0;
			foreach ($items as $data){
				if(checkstr($items[$j]['dataname'],$checkdata) == true){
					$show[$i]['name'] = $items[$j]['dataname'];
					$show[$i]['value']= number_format((float)$items[$j]['lastvalue'], 2, '.', '');
					$i++;
				}
				$j++;
			}
	
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