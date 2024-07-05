<?php

$serverUrl = 'http://qinlong.com';
$clientId = 'clientId';
$clientSecret = 'clientSecret';

//处理网络请求
$requestMethod = $_SERVER['REQUEST_METHOD'];
if ($requestMethod == 'GET') {
    $data = array(
        'code' => 200,
        'data' => array()
    );
    echo json_encode($data);
} else {
    //传参数据
    $inputJsonStr = file_get_contents('php://input');
    $inputJson = json_decode($inputJsonStr);
    if (count($inputJson) != 1) {
        $data = array(
            'code' => 500,
            'data' => array()
        );
        echo json_encode($data);
        return;
    }
    // echo json_encode($inputJson);
    //获取Token
    $getTokenUrl = "$serverUrl/open/auth/token?client_id=$clientId&client_secret=$clientSecret";
    $getToken = geturl($getTokenUrl);
    $token = $getToken['data']['token'];
    // echo json_encode($token);
    //获取已有的记录
    $getEnvUrl = "$serverUrl/open/envs/";
    $getEnvData = geturl($getEnvUrl, $token);
    // echo json_encode($getEnvData);
    $isUpdate = false;
    foreach ($getEnvData['data'] as $envData) {
        // echo 'remarks1>'.$envData['remarks'];
        // echo 'remarks2>'.$inputJson[0]->remarks;
        if ($envData['remarks'] == $inputJson[0]->remarks) {
            $inputJson[0]->id = $envData['id'];
            $inputJson[0]->name = $envData['name'];
            $isUpdate = true;
        }
    }
    //更新到平台
    if ($isUpdate) $inputJson = $inputJson[0];
    $saveResult = posturl($getEnvUrl, $inputJson, $token, $isUpdate);
    echo json_encode($saveResult);
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
