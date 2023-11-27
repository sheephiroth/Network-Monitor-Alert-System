<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;

$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$show = array('ava' => 0, 'unava' => 0, 'unknown' => 0, 'total' => 0);


if(isset($_POST['Surl'])){
	$Sname = $_POST['Sname'];
    $Surl = $_POST['Surl'];
    $Suser = $_POST['Suser'];
    $Spass = $_POST['Spass'];

	try {
        $zbx->login($Surl, $Suser, $Spass, $options);
        $params = array(
            'output' => array('hostid', 'host', 'name', 'status', 'maintenance_status'),
            'selectInterfaces' => array('interfaceid', 'main', 'type', 'useip', 'ip', 'dns', 'available')
        );
    
        $result = $zbx->call('host.get', $params);
    
        foreach ($result as $host) {
            $available = $host['interfaces'][0]['available'];
    
            if ($available == 1) {
                $show['ava']++;
            } elseif ($available == 2) {
                $show['unava']++;
            } else {
                $show['unknown']++;
            }
    
            $show['total']++;
        }
    
        $myJSON = json_encode($show);
        echo $myJSON;
    } catch (Exception $e) {
        print "==== Exception ===\n";
        print 'Errorcode: ' . $e->getCode() . "\n";
        print 'ErrorMessage: ' . $e->getMessage() . "\n";
        exit;
    }
	
} else {
	echo "test";
}
?>
