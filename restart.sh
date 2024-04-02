admin="at_ios"

#清空文件夹
rm -rf ${admin}

docker run -i --rm -v ${HOME}:/root -v $(pwd):/git alpine/git clone https://github.com/chenweilong1022/at_ios.git

docker run -i --rm --name my-maven-project -v "$(pwd)":/usr/src/mymaven -v maven-repo:/root/.m2 -w /usr/src/mymaven maven:3.3-jdk-8 mvn clean install -Dmaven.test.skip=true -f ${admin}/admin/pom.xml

docker-compose -f docker-compose.yml down
docker-compose -f docker-compose.yml up --force-recreate --build -d

#docker-compose -f docker-compose-data.yml down
#docker-compose -f docker-compose-data.yml up --force-recreate --build -d
