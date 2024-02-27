admin="sweat_x"

#清空文件夹
#rm -rf ${admin}

#docker run -i --rm -v ${HOME}:/root -v $(pwd):/git alpine/git clone -b hp_online https://chenweilong961022:ch1433471850@gitee.com/chenweilong961022/${admin}.git

docker run -i --rm --name my-maven-project -v "$(pwd)":/usr/src/mymaven -v maven-repo:/root/.m2 -w /usr/src/mymaven maven:3.3-jdk-8 mvn clean install -Dmaven.test.skip=true -f ${admin}/admin/pom.xml

docker-compose -f docker-compose.yml down
docker-compose -f docker-compose.yml up --force-recreate --build -d
