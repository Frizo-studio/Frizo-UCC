# UCC 專題

<br>

專題日誌

<br>

* 2020/3/16 完成三方登入與本地註冊功能。

<br>

* 2020/5/6 架設文件伺服器，使用 Nginx，主要文件配置細節:

    編輯 nginx資料夾/conf/nginx.conf 如下 : 
    
    ```editorconfig
    ...
    server {
            listen       9090;
            server_name  localhost;
    
            #charset koi8-r;
    
            #access_log  logs/host.access.log  main;
    
            location / {
                root   D:/frizo/ucc/resource;
                index  index.html index.htm;
            }
      ...
    ```
  
    location 的 root 參數就是文件伺服器的絕對路徑假如我們的文件在這個路徑 : 
    
    D:/frizo/ucc/resource/test.jpg
    
    當我們啟動 Nginx 時，可以透過以下 url 取得圖片 :
    
    http://127.0.0.1:9090/test.jpg