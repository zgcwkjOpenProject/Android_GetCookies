<?php

$data = array(
    "code" => 200,
    "data" => array(
        "token" => "ql.zgcwkj.cn",
    )
);
$clientId = isset($_REQUEST["client_id"]) ? $_REQUEST["client_id"] : "";
$clientSecret = isset($_REQUEST["client_secret"]) ? $_REQUEST["client_secret"] : "";
if ($clientId != "zgcwkj" || $clientSecret != "zgcwkj") {
    $data["code"] = 403;
    $data["data"] = null;
}

echo json_encode($data);
