admin="at_ios"

#清空文件夹
rm -rf ${admin}

docker run -i --rm -v ${HOME}:/root -v $(pwd):/git alpine/git clone https://github.com/chenweilong1022/at_ios.git

rm -rf /home/web/nginx/html/*
cp -r /home/web/at_ios/admin/src/main/resources/static/* /home/web/nginx/html/

#docker-compose -f docker-compose-data.yml down
#docker-compose -f docker-compose-data.yml up --force-recreate --build -d
