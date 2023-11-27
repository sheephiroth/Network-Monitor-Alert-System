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
    $mapid = $_POST['mapid'];

	try {
        $zbx->login($Surl, $Suser, $Spass, $options);
        // $zbx->login("http://192.168.1.152/zabbix", "Admin", "zabbix", $options);
    
        // $mapid = 1;
    
        $params = array(
            'output' => 'extend',
            'selectSelements' => 'extend',
            'selectLinks' => 'extend',  
            'selectShapes' => 'extend',
            'selectLines' => 'extend',
            'sysmapids' => $mapid
        );
    
        $result = $zbx->call('map.get', $params);
    
        if (empty($result)) {
            // echo "Map not found.";
        } else {
            $mapData = reset($result);  
    
    
            $jsonMapData = json_encode($mapData, JSON_PRETTY_PRINT);
    
            // $filename = 'Zabbix_map_' . $mapid . '.json';
            $filename = 'C:/Users/PC/Desktop/PROJECT/app/src/main/assets/Zabbix_map_' . $mapid . '.json';

            file_put_contents($filename, $jsonMapData);

    
            // echo "Map data saved to $filename.";
        }
        $show['mapid']=$mapid;
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
