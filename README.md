# Open ID implementation in Play framework
Service of implementation to [OpenID Conect 1.0 Spec.](http://openid.net/specs/openid-connect-core-1_0.html)

## Prerequisites
### Runtime
* Postgresql
    - All data store in Postgresql database.
* Docker runtime
    - To build, run and publish docker images.
    - In case publishing docker image to DockerHub, you need to grant authorization of publishing to DockerHub first.

### Test in local
```bash
sbt run
```

## Build Docker image
```bash
sbt docker:publishLocal
```

## Publish Docker image
As you signed-in to DockerHub, run command:
```bash
sbt docker:publish
```

### Run with docker
Create a docker network named `local` to communicate with other container. For example: *Postgresql*
```bash
docker run --name openid --rm -e JAVA_OPTS="-Dconfig.file=/tmp/prod.conf" -v ~/Documents/openid_conf:/tmp --net local -p 9000:9000 play-open-id-connect:1.0-SNAPSHOT
```

### Try to generate code, token, id_token
Make sure you had testing data in database first.
```bash
curl http://localhost:9000/oauth2/v1/auth?client_id=client123&response_type=code%20token%20id_token%20id_token&scope=openid&redirect_uri=https://www.github.com/royalan/play-open-id-connect&nonce=12foifcj1j

```