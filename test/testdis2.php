<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;
$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$show = array();
$i = 0;

try {
    $zbx->login("http://192.168.1.152/zabbix", "Admin", "zabbix", $options);

    $params = array(
        'output' => 'extend',
        'selectInterfaces' => array('interfaceid', 'main', 'type', 'useip', 'ip', 'dns', 'port')
    );
    $hostResult = $zbx->call('host.get', $params);
    $hostMap = array();
    foreach ($hostResult as $host) {
        foreach ($host['interfaces'] as $interface) {
            $hostMap[$interface['ip']] = $host['host'];
        }
    }

    $params = array(
        'output' => 'extend'
    );
    $result = $zbx->call('dservice.get', $params);

    foreach ($result as $dservice) {
        $currentTimestamp = time();
        $lastupTimestamp = $dservice['lastup'];
        $lastdownTimestamp = $dservice['lastdown'];
        $lastupDiff = $currentTimestamp - $lastupTimestamp;
        $lastdownDiff = $currentTimestamp - $lastdownTimestamp;


        if (isset($hostMap[$dservice['ip']])) {
            $show[$i]['hostname'] = $hostMap[$dservice['ip']];
        } else {
            $show[$i]['hostname'] = ' - ';
        }

        $show[$i]['ip'] = $dservice['ip'];
        
        if ($dservice['lastup'] !== "0") {
            $show[$i]['dupdown'] = "UP " . formatTimeDifference($lastupDiff);
        } else if ($dservice['lastdown'] !== "0") {
            $show[$i]['dupdown'] = "DOWN " . formatTimeDifference($lastdownDiff);
        }

        $i++;
    }

    $myJSON = json_encode($show);
    echo $myJSON;
} catch (Exception $e) {
    print "==== Exception ===\n";
    print 'Errorcode: '.$e->getCode()."\n";
    print 'ErrorMessage: '.$e->getMessage()."\n";
    exit;
}

function formatTimeDifference($seconds) {
    $hours = floor($seconds / 3600);
    $minutes = floor(($seconds % 3600) / 60);
    $seconds = $seconds % 60;

    return sprintf('%02dh %02dm %02ds ago', $hours, $minutes, $seconds);
}
?>
