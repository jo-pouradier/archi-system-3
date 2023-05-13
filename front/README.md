# archi-system-2

## Architecture gloable

![Architecture globale](./docs/archiGlobal.png)

### Routes

 - GET '/' : Renvoie la page d'accueil ou la page de login

 - Pour s'enregistrer:
   - GET '/login' : Renvoie la page de login
   - POST '/login' : Permet d'envoyer les infos de login
   - GET '/register' : Renvoie la page d'enregistrement
   - POST '/register' : Permet de s'enregistrer

 - Pour la visu des cartes:
   - GET '/card' : Renvoie la page d'achat de cartes, affichant toutes les cartes disponibles avec leurs infos
   - GET '/card/:id' : Renvoie la page d'achat de la carte (id = id de la carte)
   
 - Pour la vente/achat des cartes:
   - POST '/buy/:id' : Permet d'acheter une carte (id = id de la carte)
   - POST '/sell/:id' : Permet de mettre en vente une carte (id = id de la carte)


-> 1 on arrive sur '/' (index) c'est une page utilisateur avec ses cartes, si on le cookie de log on reste sion on redirige vers '/login'. 
   - onload on verifie les cookies si oui on reste, si non on redirige vers '/login'.
-> 2 on arrive sur '/login' c'est une page de login, si on a le cookie de log on redirige vers '/' sinon on reste et on se log.  
-> 3 button SELL pour aller sur la page de la carte pour la vendre.
     - GET sur '/card/gerCards' pour avoir les cartes dispo. et les afficher en liste.  
-> 4 Sur '/' on peut vendre ses cartes pour avoir de la tune.
   - affichage de la carte unique avec un GET sur '/card/:id'
   - POST sur '/sell/:id' pour mettre en vente la carte.  
→ 5 Sur '/market' on arrive sur une liste de cartes que l'on peut acheter.  