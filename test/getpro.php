<?php
require_once "ZabbixApi.php";
use IntelliTrend\Zabbix\ZabbixApi;

$zbx = new ZabbixApi();
$options = array('sslVerifyPeer' => false, 'sslVerifyHost' => false);
$show = array('notclass' => 0, 'info' => 0, 'warn' => 0, 'aver' => 0, 'high' => 0, 'disaster' => 0);


if(isset($_POST['Surl'])){
	$Sname = $_POST['Sname'];
    $Surl = $_POST['Surl'];
    $Suser = $_POST['Suser'];
    $Spass = $_POST['Spass'];

	try {
        $zbx->login($Surl, $Suser, $Spass, $options);
        $params = array(
            'output' => array('eventid', 'source', 'objectid', 'clock', 'name', 'severity', 'suppressed', 'ns'),
        );
        $result = $zbx->call('problem.get', $params);
    
        foreach ($result as $problem) {
            $severity = $problem['severity'];
    
            if ($severity == 0) {
                $show['notclass']++;
            } elseif ($severity == 1) {
                $show['info']++;
            } elseif ($severity == 2) {
                $show['warn']++;
            } elseif ($severity == 3) {
                $show['aver']++;
            } elseif ($severity == 4) {
                $show['high']++;
            } elseif ($severity == 5) {
                $show['disaster']++;
            }
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
