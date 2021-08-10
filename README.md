## Jenkins-CI & CD

<br/>

### CI 구축
---

#### Java 8 설치
---
```shell
  sudo yum install -y java-1.8.0-openjdk-devel.x86_64
  
  java -version
```

![2  자바 버전 확인](https://user-images.githubusercontent.com/76584547/128869813-eda056eb-d3cb-442a-bc05-cf5787752009.png)



#### Jenkins 설치
---
```shell
sudo wget -O /etc/yum.repos.d/jenkins.repo \
    https://pkg.jenkins.io/redhat-stable/jenkins.repo
sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key
sudo yum upgrade
sudo yum install jenkins java-1.8.0-openjdk-devel.x86_64
sudo systemctl daemon-reload
sudo systemctl start jenkins
sudo systemctl status jenkins
```

![image](https://user-images.githubusercontent.com/76584547/128870468-c21da6dd-1db6-45a4-b8fc-2a202a103fb7.png)

#### Jenkins 접속
---
```url
  http://15.165.207.139:8080/
```
+ 젠킨스 포트는 8080이기 때문에 인스턴스 방화벽을 열어주고 접속한다.

![image](https://user-images.githubusercontent.com/76584547/128870887-b3475417-94e6-4fb9-aa83-cdef7016c90c.png)


#### 초기 비밀번호 확인
---
```shell
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```
![image](https://user-images.githubusercontent.com/76584547/128871084-8b99e070-1a53-4fa6-be92-433f7fa04777.png)


#### 접속 완료
----
+ 어드민 계정 생성 후 젠킨스 접속 완료
![image](https://user-images.githubusercontent.com/76584547/128871950-4079bbf8-d113-412f-9a9f-72662c3374e2.png)



#### GitHub Webhook 연동
---
+ [Jenkins 관리] - [시스템 설정]
![image](https://user-images.githubusercontent.com/76584547/128872334-e573a146-c7b0-4132-b60c-72fa7c2a161c.png)


+ [GitHub] - [Add GitHub Server] - [Credentials] - [Add] - [Jenkins] 
![image](https://user-images.githubusercontent.com/76584547/128872730-6cb03d2c-0133-4741-92e9-8ade5e7a37e3.png)

+ Kind 를 Secret text 로 설정하고, Secret 에 위에서 복사한 Personal access token을 입력한다. 
+ ID에 자신의 github ID를 입력한 후 Add 버튼을 눌러서 완성한다.
![image](https://user-images.githubusercontent.com/76584547/128873014-c65a5ab3-28a6-48a8-b60c-ceebe6071591.png)

+ Test connection 확인
![image](https://user-images.githubusercontent.com/76584547/128873145-817bb5b7-37c0-466f-9d95-94595ce71510.png)


#### GitHub Repository 연동
---
+ 젠킨스 메인 화면에서 [새로운 Item] - 원하는 이름을 입력하고 Freestyle project 를 눌러 아이템을 생성한다.
![image](https://user-images.githubusercontent.com/76584547/128873439-1117ffab-a317-445a-accb-df86d848ca9d.png)

+ 해당 Item 클릭 후 Git에 Repository를 입력하면 해당 오류가 발생한다.
![image](https://user-images.githubusercontent.com/76584547/128874002-29e55614-a818-4f80-b2e2-740828d654ad.png)

+ 그 이유는, Git을 젠킨스 서버에 설치하지 않았기 때문이다.
```shell
  sudo yum install git
```
![image](https://user-images.githubusercontent.com/76584547/128874347-85c2cbb9-22c4-491b-acbd-0e458a63c772.png)


+ Git을 설치하고 Jenkins를 보면 에러가 없어진 걸 확인할 수 있다.
![image](https://user-images.githubusercontent.com/76584547/128874405-b21614a6-83ca-4ea9-ada3-5538c8414bbe.png)


+ Kind는 Username with password 로 설정하고, Username 은 자신의 깃허브 ID, Password에는 깃허브 계정 비밀번호를 입력한다.
![image](https://user-images.githubusercontent.com/76584547/128874541-70df5719-807d-4f29-9cca-4c7a15e3badf.png)

+ 그리고 빌드 유발 섹션에서 Github hook tigger for GITScm polling 을 선택한다.
![image](https://user-images.githubusercontent.com/76584547/128875782-02ce4f9e-a7a4-45e3-8fd5-43d9092f0871.png)

#### GitHub Webhook 생성
---
+ 깃허브 레포지토리의 [Settings] - [Webhooks]
![image](https://user-images.githubusercontent.com/76584547/128876700-6d1c5707-c96d-4b53-b63a-833d147ffc79.png)

+ URL에 "젠킨스서버:8080/github-webhook/"을 적고 Content type은 application/json으로 설정한다.
![image](https://user-images.githubusercontent.com/76584547/128881114-27b75303-b759-4702-ab46-b30d58266037.png)

+ 결과
![image](https://user-images.githubusercontent.com/76584547/128881293-f95002cc-6eda-4715-830c-cd4dc581f319.png)


#### Project Push
+ 프로젝트에서 "/push" api를 생성해서 "GitHub Webhook <-> Jenkins Push Test" commit message 생성
![image](https://user-images.githubusercontent.com/76584547/128881734-1ef9df95-4702-426b-9a7d-f5bee5ecf487.png)

+ 푸쉬 확인
![image](https://user-images.githubusercontent.com/76584547/128882084-037814b4-0593-4c2c-9ea8-224be1872651.png)

+ 깃허브 프로젝트를 pull한 후, "/var/lib/jenkins/workspace/jenkins-practice" 경로에 저장된 걸 확인할 수 있다.
![image](https://user-images.githubusercontent.com/76584547/128882578-e68e0bcd-1ced-45f2-a32d-9a3d40dae184.png)

+ CI 구축 완료!


### CD 구축
---

#### Gradle로 실행 파일 만들기
---
+ 젠킨스 메인 페이지에서 젠킨스 관리 - Global tool configuration 설정으로 들어간다. 이후 Gradle 섹션을 찾아 Add Gradle버튼을 누르고 프로젝트 Gradle과 버전을 맞춰서 선택
![image](https://user-images.githubusercontent.com/76584547/128883568-8183dd5c-76a6-41cd-8591-02dd26dcd521.png)

+ 다음으로 생성한 젠킨스 아이템으로 들어가 구성 - Build 설정으로 들어간다. 그리고 Add build step - Invoke Gradle Script 를 선택하여 다음 그림처럼 설정해준다.
![image](https://user-images.githubusercontent.com/76584547/128884417-57e4b994-f720-4179-8f7b-f89c19680d88.png)


+ 다음엔 오른쪽 하단에 있는 고급.. 버튼을 누르고 다음과 같이 설정 후 저장한다.
  + 그러면 CI 과정을 거쳐서 자동으로 /var/lib/jenkins/workspace/Jenkins-Practice/build/libs 경로에 실행 파일을 생성한다.
![image](https://user-images.githubusercontent.com/76584547/128884705-8a487160-36b9-4b38-ac6d-25723c4b961e.png)


#### SSH로 배포 및 쉘 스크립트 실행
---
+ 젠킨스 관리 - 플러그인 관리 로 들어가 Publish Over SSH 플러그인을 설치한다.
![image](https://user-images.githubusercontent.com/76584547/128884927-208e6c3d-c396-4312-a16a-dd51458a0cd1.png)


+ 젠킨스 관리 - 시스템 설정 으로 들어가 방금 설치한 Publish Over SSH 플러그인 설정을 다음과 같이 구성한다. (가장 아래에 해당 설정이 있다.)
![image](https://user-images.githubusercontent.com/76584547/128885796-68535ad9-a7cc-4824-8dbb-32a79a86881d.png)

```
Key : 서버가 구동 중인 EC2 인스턴스의 .pem 파일 내용을 복사해서 붙여넣는다.

Hostname : 서버가 구동 중인 EC2 인스턴스의 IP를 적어준다.

Username : 접속하려는 유저를 적어준다.

Remote Directory : 접속했을 때의 기본 디렉터리를 설정한다.
```

+ 이후 아이템의 구성 - 빌드 후 조치 추가 - Send build artifacts over SSH 설정에 들어가서 다음과 같이 설정한다.
![image](https://user-images.githubusercontent.com/76584547/128886425-3fafb77c-3578-4e14-9f33-96bdd573d271.png)


```
ㅇ Source files : 배포하려는 파일 지정
- build/libs/*.jar

ㅇ Remove prefix : 배포하려는 파일이 속해 있는 디렉토리 정보를 제거하기 위해 필요
- build/libs

ㅇ Remote directory : 배포하려는 디렉토리의 위치
- app

ㅇ Exec command : ssh 배포 후 실행하는 명령어 - 여기선 deploy.sh 를 실행한다.
- /home/ec2-user/app/deploy.sh > /dev/null 2>&1
```

+ /dev/null 2>&1 는 표준 출력과 표준 입력을 버리는 것이다. 만약 이 내용을 적어주지 않으면, 젠킨스가 쉘 스크립트를 실행한 후 빠져나오지 못하게 되므로 반드시 붙여줘야 한다!
+ 주의 사항
  + 반드시 Exec command 에 절대 경로를 설정해줘야 한다.
  + 쉘 스크립트 실행 시 스크립트 안에 있는 모든 명령어들은 반드시 절대 경로를 사용해야 한다.





#### 참고
---
 + https://bokyung.dev/2021/03/17/jenkins-install/
 + https://www.nowwatersblog.com/backend/cicd/nonstopcicd
