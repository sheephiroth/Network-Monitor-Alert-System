<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;

$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$show = array();


if(isset($_POST['Surl'])){
	$Sname = $_POST['Sname'];
    $Surl = $_POST['Surl'];
    $Suser = $_POST['Suser'];
    $Spass = $_POST['Spass'];

	try {
        $zbx->login($Surl, $Suser, $Spass, $options);
    
        $params = array(
            'output' => array('druleid', 'iprange', 'name', 'delay', 'proxy_hostid', 'status', 'dhostids'),
            'filter' => array('status' => 0),
            'selectDHosts' => array('dhostid', 'druleid', 'lastdown', 'lastup', 'status')
        );
    
        $result = $zbx->call('drule.get', $params);
    
        foreach ($result as $drule) {
            $druleName = $drule['name'];
            $hup = 0;
            $hdown = 0;
            foreach ($drule['dhosts'] as $dhosts) {
                if($dhosts['status'] == 0){
                    $hup++;
                }
                if($dhosts['status'] == 1){
                    $hdown++;
                }
            }
    
    
            $show[] = array('drulename' => $druleName, 'hup' => $hup, 'hdown' => $hdown);
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
