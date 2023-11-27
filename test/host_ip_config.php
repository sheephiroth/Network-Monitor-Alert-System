<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;

$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);

if(isset($_POST['Surl'])) {
    $Surl = $_POST['Surl'];
    $Suser = $_POST['Suser'];
    $Spass = $_POST['Spass'];
    $newip = $_POST['Newip']; 
    $hostids = $_POST['hostids'];
    
    try {
        $zbx->login($Surl, $Suser, $Spass, $options);
        
        $hostInfo = $zbx->call('host.get', [
            'output' => ['hostid'],
            'hostids' => $hostids,
            'selectInterfaces' => ['interfaceid']
        ]);

        if (isset($hostInfo[0]['interfaces'][0]['interfaceid'])) {
            $interfaceId = $hostInfo[0]['interfaces'][0]['interfaceid'];

            $result = $zbx->call('hostinterface.update', [
                'interfaceid' => $interfaceId,
                'ip' => $newip
            ]);
            
        }
        
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
