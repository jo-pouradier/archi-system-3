# Proxy

## Docker

Installer docker et docker-compose sur votre machine. 
Sur windows il faut installer docker desktop. Et dans les settings -> advanced -> choisir System. Cela permet a Docker d'installer les bin a la racines. A voir si ca marche aussi sur wsl2.

tester l'installation avec la commande suivante:

```bash
docker --version
# et 
docker run hello-world
```

Puis pour plus de faciliter ajouter votre user au groupe docker:

```bash
sudo usermod -aG docker $USER
```
Sur mac il faut utiliser la commande suivante:

```bash
sudo dscl . append /Groups/docker GroupMembership $USER
```

## Lancer le proxy

Tout est deja fais grace a docker compose, il suffit de lancer la commande suivante dans le dossier proxy (ce dossier):

```bash
docker-compose up -d
#ou
docker compose up -d
```


## Fin de la session

Pour arreter l'instance docker il suffit d'etre dans le dossier proxy et de lancer la commande suivante:

```bash
docker-compose down
#ou
docker compose down
```


lancer le docekr :
```bash
docker run --name my-custom-asi-nginx-container --network host -p 8089:80 -v C:\Users\pltel\Desktop\CPE\ArchiLogi\archi-system-3\proxy\nginx.conf:/etc/nginx/nginx.conf:ro -d nginx

```