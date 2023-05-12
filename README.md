# archi-system-3


## Architecture Diagram

Notre architecture sera composé de 6 Services :

![Architecture Diagram](./docs/archi.png)

Chaques services sera construit comme ci-dessous :

![Service Diagram](./docs/microService.png)

## Partage des objet DTO avec une Maven Common Library

Pour partager les objets DTO entre les services, nous avons créé une librairie Maven qui contient les objets DTO et les interfaces des services.

## Proxy

Pour communiquer entre les services, nous avons utilisé un proxy Nginx qui permet de faire des appels REST entre les services.