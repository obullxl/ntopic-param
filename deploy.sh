#!/bin/bash

# 本地打包
mvn clean && mvn deploy -Dmaven.test.skip=true

# 上传仓库
cd ./../maven-repository
git add --all
git commit -m 'Deploy ntopic-param JAR: https://github.com/obullxl/ntopic-param'
git push origin master

# 返回项目
cd ../ntopic-param

# Gitee刷新：人工刷新
open -a '/Applications/Microsoft Edge.app' https://gitee.com/obullxl/ntopic-param
