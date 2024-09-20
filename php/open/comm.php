<?php
//echo "include ok";
$serverUrl = 'http://qinlong.com';
$clientId = 'clientId';
$clientSecret = 'clientSecret';

//获取Token
function getToken() {
    global $serverUrl;
    global $clientId;
    global $clientSecret;
    $getTokenUrl = "$serverUrl/open/auth/token?client_id=$clientId&client_secret=$clientSecret";
    $getToken = geturl($getTokenUrl);
    $token = $getToken['data']['token'];
    // echo json_encode($token);
    return $token;
}

function geturl($url, $token = '') {
    $headerArray = array(
        'Content-Type: application/json',
        'Accept: application/json'
    );
    if (!empty($token))$headerArray[] = "Authorization: Bearer $token";
    //
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
    curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, FALSE);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headerArray);
    $output = curl_exec($ch);
    curl_close($ch);
    $output = json_decode($output, true);
    return $output;
}

function posturl($url, $data, $token = '', $isUpdate = false) {
    $data = json_encode($data);
    $headerArray = array(
        'Content-Type: application/json',
        'Content-Length: ' . strlen($data)
    );
    if (!empty($token))$headerArray[] = "Authorization: Bearer $token";
    //
    $curl = curl_init();
    curl_setopt($curl, CURLOPT_URL, $url);
    if (!$isUpdate) curl_setopt($curl, CURLOPT_POST, true); //POST
    else curl_setopt($curl, CURLOPT_CUSTOMREQUEST, 'PUT'); //PUT
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, FALSE);
    curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, FALSE);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($curl, CURLOPT_HTTPHEADER, $headerArray);
    curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
    $output = curl_exec($curl);
    curl_close($curl);
    $output = json_decode($output, true);
    return $output;
}

