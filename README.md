## Jenkins-Practice

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

#### 참고
---
 + https://bokyung.dev/2021/03/17/jenkins-install/
 + https://www.nowwatersblog.com/backend/cicd/nonstopcicd
