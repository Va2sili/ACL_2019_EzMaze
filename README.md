# EzMaze
![Banniere](https://github.com/Sutcenes/EzMazeWikiSource/blob/master/Images_WIKI/banniere.png)
## Comment réaliser le build
Le dossier qui nous intéresse est le dossier nommé _Ez\_Maze\_Git_. Il contient l'intégralité des fichiers utiles au projet. On pourra lancer le build automatique utilisant Gradle ([cf. le wiki EzMaze](https://github.com/Va2sili/ACL_2019_EzMaze/wiki/GRADLE#libgdx---outils-de-build)).
Pour ce faire :
  1. Se rendre dans la racine du projet avec la commande _cd_. On doit voir le fichier _gradlew_ ou _gradlew.bat_;
  2. Exécuter la commande : _./gradlew desktop:dist_ (Ca va lancer le build du _project_ 'desktop' et on exécute la _task_ 'dist');
  3. Attendre la fin du déploiement et le message "_BUILD SUCCESSFUL_" (cela peut prendre du temps lors de la première exécution de gradle sur le pc);
  4. Se rendre (manuellement ou avec _cd_) dans _/ACL_2019_EzMaze\Ez_Maze_Git\desktop\build\libs_;
  5. On y trouve un fichier .jar dont le nom est de la forme : "_EzMaze-sprint.1-build-200641_29112019_" ([cf. le wiki EzMaze](https://github.com/Va2sili/ACL_2019_EzMaze/wiki/GRADLE#libgdx---outils-de-build))
  6. On peut le lancer (manuellement ou via la commande _java -jar nomdubuild.jar_).
  
## Commandes de jeu
![Banniere](https://github.com/Sutcenes/EzMazeWikiSource/blob/master/Images_WIKI/Commandes.png)
  
## Etat d'avancée du Sprint 2
 
 - [x] Apparition de monstres qui se déplacent et font perdre le joueur
 - [x] Attaquer les monstres
 - [x] Système de résurrection
 - [ ] Case de téléportation
 - [x] Case de boue
 - [x] GUI      

## Informations Supplémentaires
On peut retrouver le dépôt GIT concernant les images utilisées dans le WIKI à l'adresse : https://github.com/Sutcenes/EzMazeWikiSource
