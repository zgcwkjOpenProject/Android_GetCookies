<?php
require_once '../comm.php';

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
    //echo json_encode($inputJson);
    $token = getToken();
    //echo json_encode($token);
    //启用环境变量
	$envEnableUrl = "$serverUrl/open/envs/enable/";
	$saveResult = posturl($envEnableUrl, $inputJson, $token, true);
    echo json_encode($saveResult);
}
