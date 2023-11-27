<?php
$t1 = ["temperature status"];
$t2 = ["temperature"];
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


$word = "AC Power Supply: Power supply status";
$word2 = "TEMPERATURE IS BAD";
$word3 = "Temperature status";
$str = [
    "power supply status"
];
$test=[$word,$word2,$word3];
$i = 0;
foreach ($test as $x){
    if(checkstr($test[$i],$t1) == true){echo "it's temperature status <br>";}
    elseif(checkstr($test[$i],$t2) == true){echo "it's temperature only <br>";}
    elseif(checkstr($test[$i],$str) == true){echo "bad <br>";}
    else {echo "good <br>";}
    $i++;
}
?>






