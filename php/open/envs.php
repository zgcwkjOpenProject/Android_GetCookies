<?php
require_once 'comm.php';

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
    $token = getToken();
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
    //准备数据
    $arrayID = array();
    if ($isUpdate) {
        array_push($arrayID, $inputJson[0]->id);
        $inputJson = $inputJson[0];
    }
    //更新到平台
    $saveResult = posturl($getEnvUrl, $inputJson, $token, $isUpdate);
    ////启用环境变量
    //if ($isUpdate) {
    //    $envEnableUrl = "$serverUrl/open/envs/enable/";
    //    $saveResult = posturl($envEnableUrl, $arrayID, $token, true);
    //}
    echo json_encode($saveResult);
}
