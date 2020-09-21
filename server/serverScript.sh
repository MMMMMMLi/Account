#!/bin/bash
source /etc/profile

# delete jars
echo '----------------------- delete jars begin ----------------------- '
rm -rf /root/wechat/target/*
echo '----------------------- delete jars end ------------------------- '

# pull code
echo '----------------------- pull code begin ------------------------- '
cd /root/wechat/Account
git pull origin master > /dev/null
echo '----------------------- pull code end --------------------------- '

# stop servers
echo '----------------------- stop servers begin ---------------------- '
cd ./server
for port in `cat actuatorPosts`
do
  timeout 3 curl -X POST localhost:$port/imengli/shutdown 2> /dev/null
  echo $port': ok .'
done
for pid in $(ps -ef | grep java | grep -v grep | awk '{print $2}')
do
  kill -9 $pid
  echo $pid': kill -9 .'
done
echo '----------------------- stop servers end ------------------------ '


# build code.
echo '----------------------- build code begin ------------------------ '
for dir in $(ls)
do
  if [ -d $dir ]
	then
		cd $dir
		mvn clean > /dev/null && mvn package -DskipTests > /dev/null
		mv ./target/*.jar /root/wechat/target/
		cd ..
		echo $dir': ok .'
  fi	
done
echo '----------------------- build code end -------------------------- '

# start servers
echo '----------------------- start servers begin --------------------- '
cd /root/wechat/target
`nohup java -Xmx512m -Xms512m -jar ./eureka-server-v1.jar > /dev/null 2> error-eureka-server.log &`
echo 'eureka-server : ok .'
`nohup java -Xmx512m -Xms512m -jar ./gateway-v1.jar > /dev/null 2> error-gateway.log &`
echo 'gateway-server : ok .'
`nohup java -Xmx512m -Xms512m -jar ./redis-server-v1.jar > /dev/null 2> error-redis-server.log &`
echo 'redis-server : ok .'
`nohup java -Xmx512m -Xms512m -jar ./applet-server-v1.jar > /dev/null 2> error-applet-server.log &`
echo 'applet-server : ok .'
`nohup java -Xmx512m -Xms512m -jar ./user-server-v1.jar > /dev/null 2> error-user-server.log &`
echo 'user-server : ok .'
# 由于系统内存较小，所以每个服务的堆内存不能一样，所以舍弃这种循环遍历的方法。
# for jar in $(ls)
# do
#   total=0
#   for excludeJar in `cat /root/wechat/excludeJars`
#   do
# 	if [ $jar != $excludeJar ]
#       then
#         (( total = total +1 ))
#     fi
#   done
#   if [ $total == 3 ]
#     then
#       nohup java -jar ./$jar > error.$jar.log &
# 	  echo $jar': ok .'
#   fi
# done
echo '----------------------- start servers end ----------------------- '

jps -l
