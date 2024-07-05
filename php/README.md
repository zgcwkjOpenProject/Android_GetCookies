# 说明

可以搭建中间者来使用，以避免数据泄露。

# nginx 配置伪静态

```nginx.conf
#Qinglong
location /open {
    rewrite ^/open/(.*)$ /open/$1.php;
}
```
