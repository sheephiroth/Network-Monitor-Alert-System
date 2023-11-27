<?php
$show = array();
if(isset($_POST['mapinfo'])){
    $mapinfo = $_POST['mapinfo'];
    // Load the map data from the JSON file
    // $jsonData = file_get_contents('Zabbix_map_1.json');
    $jsonData = file_get_contents($mapinfo);
    $mapData = json_decode($jsonData, true);

    // Create a blank image with the specified width and height
    $width = (int)$mapData['width'];
    $height = (int)$mapData['height'];
    $image = imagecreatetruecolor($width, $height);

    // Define colors
    $white = imagecolorallocate($image, 255, 255, 255);
    $black = imagecolorallocate($image, 0, 0, 0);
    $green = imagecolorallocate($image, 0, 204, 0); // Green color for links

    // Fill the image with a white background
    imagefilledrectangle($image, 0, 0, $width, $height, $white);

    $shapes = $mapData['shapes'];

    foreach ($shapes as $shape) {
        $x = (int)$shape['x'];
        $y = (int)$shape['y'];
        $width = (int)$shape['width'];
        $height = (int)$shape['height'];
        $text = $shape['text'];

        // Draw the shape
        $borderColor = hexdec($shape['border_color']);
        $backgroundColor = hexdec($shape['background_color']);
        $borderWidth = (int)$shape['border_width'];

        imagerectangle($image, $x, $y, $x + $width, $y + $height, $borderColor);

        // Fill the shape with a background color
        imagefilledrectangle($image, $x + $borderWidth, $y + $borderWidth, $x + $width - $borderWidth, $y + $height - $borderWidth, $backgroundColor);

        // Draw text in the shape
        $textX = $x + ($width - imagefontwidth($shape['font']) * strlen($text)) / 2;
        $textY = $y + ($height - imagefontheight($shape['font'])) / 2;
        imagestring($image, $shape['font'], $textX, $textY, $text, $black);
    }

    // Draw lines on the map
    $lines = $mapData['lines'];

    foreach ($lines as $line) {
        $x1 = (int)$line['x1'];
        $y1 = (int)$line['y1'];
        $x2 = (int)$line['x2'];
        $y2 = (int)$line['y2'];

        // Draw the line
        $lineColor = hexdec($line['line_color']);
        $lineWidth = (int)$line['line_width'];

        imagesetthickness($image, $lineWidth);
        imageline($image, $x1, $y1, $x2, $y2, $lineColor);
        imagesetthickness($image, 1); // Reset line thickness
    }


    // Draw elements on the map
    $elements = $mapData['selements'];
    $minElementID = min(array_column($elements, 'selementid'));

    // Custom icon filenames and labels
    $iconFilenames = [
        '0' => 'window.png',  
        '69' => 'window.png', 
        '125' => 'router.png',
        '185' => 'zabbix_server.png',
    ];

    $iconDimensions = [
        '0' => ['width' => 100, 'height' => 100],  
        '69' => ['width' => 100, 'height' => 100], 
        '125' => ['width' => 100, 'height' => 100],
        '185' => ['width' => 100, 'height' => 100],
    ];

    foreach ($elements as $element) {
        $x = (int)$element['x'];
        $y = (int)$element['y'];
        $iconId = (string)$element['iconid_off'];
        $label = $element['label'];
        $labelWidth = strlen($label) * 8; // Adjust the multiplier as needed
        $labelX = $x + $labelWidth / 2;
        $labelY = $y;

        // Draw the label
        $labelX -= $labelWidth / 2;
        imagestring($image, 5, $labelX, $labelY, $label, $black);

    }

    // Draw links between elements (hosts) using the center position of the label
    $links = $mapData['links'];

    foreach ($links as $link) {
        $element1 = $elements[($link['selementid1'] - $minElementID)];
        $element2 = $elements[($link['selementid2'] - $minElementID)];

        // Calculate the center of each label
        $label1 = $element1['label'];
        $labelWidth1 = strlen($label1) * 8;
        $labelX1 = (int)$element1['x'] + $labelWidth1 / 2;
        $labelY1 = (int)$element1['y'];

        $label2 = $element2['label'];
        $labelWidth2 = strlen($label2) * 8;
        $labelX2 = (int)$element2['x'] + $labelWidth2 / 2;
        $labelY2 = (int)$element2['y'];

        // Calculate new positions closer to the elements to shorten the lines
        $shortenFactor = 0.9; // Adjust this factor to control the line length
        $newX1 = $labelX1 + ($labelX2 - $labelX1) * $shortenFactor;
        $newY1 = $labelY1 + ($labelY2 - $labelY1) * $shortenFactor;
        $newX2 = $labelX2 - ($labelX2 - $labelX1) * $shortenFactor;
        $newY2 = $labelY2 - ($labelY2 - $labelY1) * $shortenFactor;

        // Draw a line between the new positions
        imageline($image, $newX1, $newY1, $newX2, $newY2, $green);
    }

    foreach ($elements as $element) {
        // Calculate the center of the element
        $x = (int)$element['x'];
        $y = (int)$element['y'];
        $iconId = (string)$element['iconid_off'];

        // Calculate the center of the label
        $label = $element['label'];
        $labelWidth = strlen($label) * 8; // Adjust the multiplier as needed
        $labelX = $x + $labelWidth / 2;
        $labelY = $y;

        // Draw the icon on the map with a gap above the label
        if (isset($iconFilenames[$iconId])) {
            $iconWidth = $iconDimensions[$iconId]['width'];
            $iconHeight = $iconDimensions[$iconId]['height'];
            $iconCenterX = $labelX;
            $iconCenterY = $y - $iconHeight / 2 - 30;

            $iconFilename = 'icons/' . $iconFilenames[$iconId];
            $iconImage = imagecreatefrompng($iconFilename);
            

            imagecopyresized($image, $iconImage, $iconCenterX - $iconWidth / 2, $iconCenterY, 0, 0, $iconWidth, $iconHeight, imagesx($iconImage), imagesy($iconImage));
        }
    }

    // // Output the image
    // header('Content-Type: image/png');
    // imagepng($image);

    // Save the image as "map_id.png"
    $mapid = $mapData['sysmapid'];
    // $imageFilename = $mapid . '.png';
    $imageFilename = 'C:/Users/PC/Desktop/PROJECT/app/src/main/assets/' .$mapid . '.png';
    imagepng($image, $imageFilename);

    // Clean up
    imagedestroy($image);

    // echo "Map image saved as $imageFilename";
    $show['mapid']=$mapid;
    $myJSON = json_encode($show);
    echo $myJSON;

} else {
	echo "test";
}
?>
