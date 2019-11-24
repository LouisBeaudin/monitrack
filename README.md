![Image du projet](https://drive.google.com/uc?export=view&id=10cGfM-noIL_E6wjTzp1_dZdvDWGN4OqZ)

# Sommaire
- [Avant de commencer](#avant-de-commencer)
	- [**Important**](#important)
- [Quelques conventions de nommages](#quelques-conventions-de-nommages)
- [Quelques conseils](#quelques-conseils)
- [Organisation du projet Maven](#organisation-du-projet-maven) 
- [Creation de branches pour les evolutions](#creation-de-branches-pour-les-evolutions)
- [Importer le projet git dans Eclipse](#importer-le-projet-git-dans-eclipse)
- [Changer de branche git dans Eclipse](#changer-de-branche-git-dans-eclipse)
- [Mettre a jour son repertoire git local avec le repertoire distant](#mettre-a-jour-son-repertoire-git-local-avec-le-repertoire-distant)
- [Fusionner les modifications faites sur une autre branche avec la branche courante](#fusionner-les-modifications-faites-sur-une-autre-branche-avec-la-branche-courante)
- [Generation de l’artefact JAVA : JAR](#generation-de-l-artefact-java-jar)
	- [Prerequis](#prerequis)
	- [Etapes de generation](#etapes-de-generation)
		- [generation via Eclipse](#generation-via-eclipse)
		- [generation en ligne de commande](#generation-en-ligne-de-commande)
- [Se connecter a l'environnement de developpement](#se-connecter-a-l-environnement-de-developpement)
	- [Connexion a la base de donnnes de developpement](#connexion-a-la-base-de-donnnes-de-developpement)
	- [Connexion au serveur de developpement](#connexion-au-serveur-de-developpement)
- [Se connecter a l'environnement de production](#se-connecter-a-l-environnement-de-production)
	- [Connexion a la base de donnnes de production](#connexion-a-la-base-de-donnnes-de-production)
	- [Connexion au serveur de production](#connexion-au-serveur-de-production)
- [Scenarios de demonstration](#scenarios-de-demonstration)
	- [Release 1 (R1)](#release-1-r1)
	- [Release 2 (R2)](#release-2-r2)
- [Si des erreurs apparaissent](#si-des-erreurs-apparaissent)

# Avant de commencer
1. La branche master ne sera utilisée que pour faire des "merge" dessus. Par conséquent, il faut éviter au maximum de modifier directement les fichiers à partir de la branche master, il faut [créer des branches](#creation-de-branches-pour-les-evolutions).
2. Dans certains dossiers, vous trouverez un fichier oneFileIsRequired.txt. Vous pourrez supprimer ces fichiers lorsque le dossier contiendra au moins un autre fichier (par exemple un fichier .java). Si vous supprimez ce fichier .txt et que le dossier devient vide, alors le dossier ne pourra pas être 'commit' ce qui pourra entraîner des erreurs de compilation.
3. N'oubliez pas de faire des "Fetch" et des "Pull" lorsque vous changez de branche.
4. N'oubliez pas de faire des commits réguliers afin que les autres membres du groupe puissent connaitre votre avancement.

## Important
**Si vous avez cloné le projet _monitrack_ avant le 20 mars 2019, cette section est pour vous, sinon vous pouvez la passer.**
Lorsque vous ferez un push, vous verrez sûrement les dossiers **monitrackCommons**, **monitrackGUI** et **monitrackService** en plus des dossiers **monitrack-commons**, **monitrack-gui** et **monitrack-service** comme sur l'image ci-dessous :
![image](https://drive.google.com/uc?export=view&id=19JF2dXPtEaYFveyLclB02l6P9P7j_bt7) 

Si c'est le cas vous pourrez alors supprimer les dossiers **monitrackCommons**, **monitrackGUI** et **monitrackService** de votre disque dur.
Enfin, importer les projets **monitrack-commons**, **monitrack-gui** et **monitrack-service** (qui sont tous des projets Maven) dans votre _Workspace_.
Faites également un _Maven > Update Project_ si des erreurs apparaissent.

# Quelques conventions de nommages
- Le nom des variables ainsi que le nom des méthodes seront en anglais
- Le nom des interfaces java débutera toujours par un 'I'
- Pour les conventions de nommage des packages rendez-vous sur : 
https://openclassrooms.com/fr/courses/26832-apprenez-a-programmer-en-java/21583-les-packages

# Quelques conseils
- Dans les cas où vous devez concaténer plusieurs plusieurs variables dans une chaîne de caractères, la méthode **String.format(format, args)** peut être utile. Voici un exemple :

```java
int num = 1;
String firstName = "John";
String lastName = "Doe";
String str = String.format("%d) My name is %s %s", num, firstName, lastName);
System.out.println(str);

Sortie : 1) My name is John Doe
```

# Organisation du projet Maven
Pour le moment, le projet Maven se découpe en 3 modules distincts :

**- monitrack-commons :** contient tous les éléments communs aux deux modules ci-dessus comme les entités, des méthodes utiles qui permettent de lire dans un fichier ou encore des classes qui permettent de sérialiser (passage d'un objet JAVA à du JSON) ou de les desérialiser (passer du JSON à un objet JAVA)
	
**- monitrack-gui :** contient tous les éléments qui seront destinés à la réalisation de l'interface graphique pour le client (comme les JPanel, JFrame, ...)
	
**- monitrack-service :** contient tous les éléments qui vont nous permettre d'accéder à la base de données et d'effectuer des requêtes (récupération de données, ajout, suppression,...)

# Creation de branches pour les evolutions
1. Cliquez sur le bouton + (situé à côté du nom de la branche courante)
2. Cliquez sur 'New branch'
3. Vous serez rediriger vers une fenêtre qui vous demandera d'indiquer le nom de votre nouvelle branche. Ce nom doit être le nom de l'évolution de manière succincte et les mots seront séparés par des underscores. Vous devrez également sélectionner la branche  partir de laquelle vous voulez créer la branche : vérifiez que c'est bien la branche master (ou une autre branche si elle est bien à jour)
4. Puis pour travailler sur cette branche dans Eclipse, [mettez à jour le projet](#mettre-a-jour-son-repertoire-git-local-avec-le-repertoire-distant) et [changez de branche](#changer-de-branche-git-dans-eclipse).

# Importer le projet git dans Eclipse
1. Copiez le lien suivant : https://gitlab.com/climg/monitrack.git (Ce lien apparaît lorsque vous cliquez sur le bouton clone en haut à droite de la page puis dans la partie 'Clone with HTTPS')
2. Rendez-vous dans Eclipse
3. Cliquez sur Window > Perspective > Open Perspective > Git
4. Cliquez sur Clone (soit le petit nuage avec une flèche verte ou alors un lien qui s'affiche)
5. Dans l'URI, collez le lien que vous avez copié précédemment.
6. Renseignez votre user (prenom.nom) et votre password.
7. Sélectionnez toutes les branches, puis faites 'Next'.
8. Choisissez l'emplacement du projet, puis faites'Finish'.
9. Une fois que le projet apparaît dans la fenêtre 'Git Repositories', faites une clic-droit sur le projet puis 'Import projects'

# Changer de branche git dans Eclipse
1. Assurez-vous d'avoir [mis à jour votre répertoire avec le répertoire distant] #mettre-a-jour-son-repertoire-git-local-avec-le-repertoire-distant)
2. Dans 'Project Explorer', faites un clic-droit sur 'monitrack', puis Team > Switch To > Other

	- Si vous n'avez pas utilisé la branche dans le projet :
		-> cherchez dans le dossier 'Local'	et la sélectionner
	- Si vous avez déjà utilisé la branche : 
		-> cherchez dans le dossier 'Remote Tracking'
		-> Sélectionnez la branche puis cliquez sur 'Checkout as a new Local Branch'
3. Faites un clic-droit sur monitrack > Maven > Update Project
4. Puis clic-droit sur monitrack > Run As > Maven Clean
5. Puis clic-droit sur monitrack > Run As > Maven Install
6. Vous pouvez ensuite travailler sur cette branche

# Mettre a jour son repertoire git local avec le repertoire distant
1. Dans 'Project Explorer', faites un clic-droit sur 'monitrack', puis Team > Pull

# Fusionner les modifications faites sur une autre branche avec la branche courante
1. Assurez-vous d'avoir [mis à jour votre répertoire avec le répertoire distant](#mettre-a-jour-son-repertoire-git-local-avec-le-repertoire-distant)
2. Puis clic-droit sur 'monitrack', Team > Merge
3. Sélectionnez la branche que vous voulez copier
4. Cliquez sur 'Merge'
5. Si des conflits apparaissent, réglez-les en conservant les parties que vous souhaitez garder
6. Enfin clic-droit sur 'monitrack', Team > Push To Upstream

# Generation de l’artefact JAVA (JAR)
Source : https://www.codejava.net/coding/how-to-create-executable-jar-file-with-resources-and-dependencies-using-maven-in-eclipse
## Prerequis
1.Assurez-vous d'avoir cette partie de code xml dans le fichier pom.xml du module que vous allez exporter au format .jar
```xml
<build>
	<plugins>
		<plugin>
			<artifactId>maven-assembly-plugin</artifactId>
			<configuration>
				<archive>
					<manifest>
						<mainClass>[package de la classe contenant la méthode main].[nom
							de la classe contenant la méthode main]</mainClass>
					</manifest>
				</archive>
				<descriptorRefs>
					<descriptorRef>jar-with-dependencies</descriptorRef>
				</descriptorRefs>
			</configuration>
		</plugin>
	</plugins>
</build>
```

## Etapes de generation
### Generation via Eclipse
1. Faites : clic-droit sur le projet Monitrack > Run As > Run Configurations
![image](https://drive.google.com/uc?export=view&id=1JKQCFFMCMsfCDiVzvwkVdmby03sFkPm9)

2. Cliquer sur **Maven Build (1)** dans la liste des configurations, puis sur l’icône **New launch configuration (2)**
![image](https://drive.google.com/uc?export=view&id=1QFu-CEjMZTYxUphX74TC4voqSggsJYMZ)

3. Une nouvelle fenêtre s’affiche _(voir photo ci-dessous)_
![image](https://drive.google.com/uc?export=view&id=1djItXE_SBkXEtn5b1mg_Jiqyr1KhLH8H)

4. Appuyer sur le bouton « Workspace » où vous serez invité à choisir le module Maven (contenant une méthode « main » principale que vous voudrez exporter au format .jar
Choisir le module correspondant. Dans notre cas, nous aurons à choisir un des 2 modules suivants :
	- le module monitrackGUI pour le client JAVA
	- le module monirtackService pour tester le pool de connexions
_Dans notre cas nous avons choisi le module monitrackGUI_
![image](https://drive.google.com/uc?export=view&id=1mYVyanwlR8x5VxOpUoGOTSphaBOjtra0)

5. Dans la partie goals, y écrire :

```
clean assembly:single
```
![image](https://drive.google.com/uc?export=view&id=13ITCwIcN86TdpqeYqLgScUV-zd9kkOsC)

6. N'hésitez pas à changer le nom de la configuration afin de faciliter les Run ultérieurs
7. Appuyez sur le bouton "Apply" en bas, puis Run.
8. Sur la console devrait s'afficher les différentes étapes
9. Une fois que le "Run" a terminé avec succès, vous pourrez trouver votre fichier .jar généré en suivant le chemin indiqué après "Building jar"
![image](https://drive.google.com/uc?export=view&id=1RPDmLLlS__I3e628636povbH-AkUbaH_)
 


### Generation en ligne de commande
1. Assurez-vous d'avoir installé Maven sur votre ordinateur
2. Rendez- vous dans le dossier du module du projet que vous voulez générer en .jar
3. Ouvrez la console en faisant "Shift + Clic-Droit" dans le dossier courant
4. Lancer la commande suivante :

```
D:\GitHub\PDS_ESIPE_CYCLE_INGENIEUR\monitrack\monitrackGUI> mvn clean compile assembly:single
```
![image](https://drive.google.com/uc?export=view&id=13GuC_9-UF1D1ohc8cM2zj-ORsN1_8QAz)

5. Enfin pour trouver le fichier .jar généré, il vous suffira de suivre le chemin indiqué après "Building jar" ou alors vous pouvez exécuter la commande suivante :

```
D:\GitHub\PDS_ESIPE_CYCLE_INGENIEUR\monitrack\monitrackGUI> java -jar <chemin complet du jar indiqué après "Building jar">
```
![image](https://drive.google.com/uc?export=view&id=1ZCq0y3x-RnhTh-vBt6nWiXexaDG1ODuY)

**Attention : Si vous décidez d'ouvrir l'artefact jar à partir de la l'invite de commande (ou le PowerShell) , faîtes très attention car si vous fermer votre invite de commande alors l'application se fermera aussi.**

# Se connecter a l'environnement de developpement
**Attention : N'oubliez pas de vérifier que vous être en train d'utiliser l'environnement de développement pour les tests et l'environnement de production pour les démonstrations. Lisez la suite pour savoir si vous utilisez le bon environnement ***

## Connexion a la base de donnnes de developpement
1. Lancer les machines virtuelles *MONITRACK_NETWORK* et *MONITRACK_BDD_DEV*
2. Une fois ces machines virtuelles allumées, lancer la commande suivante sur la machine virtuelle MONITRACK_NETWORK :

```
toto@ubuntu:~$ sudo iptables-restore < /etc/iptables_bdd_dev.rules
```
*Cette commande aura pour but de nous permettre d'accéder à la base de données se situant sur une autre machine virtuelle d'un réseau privée*

3. Se rendre sur un navigateur web et y écrire l'url suivante **192.168.20.15:80**. Vous devriez obtenir la page suivante :
![image](https://drive.google.com/uc?export=view&id=1_L_TRi8bQtcGEBWAHazDPLvyP7W0QI_7)

## Connexion au serveur de developpement
1. Lancer les machines virtuelles *MONITRACK_NETWORK*, *MONITRACK_SERVICE_DEV* et *MONITRACK_BDD_DEV*
2. Une fois ces machines virtuelles allumées, lancer la commande suivante sur la machine virtuelle MONITRACK_NETWORK :

```
toto@ubuntu:~$ sudo iptables-restore < /etc/iptables_service_dev.rules
```
3. Télécharger sur le machine virtuelle *MONITRACK_SERVICE_DEV* la dernière version du projet disponible sur le site : http://cheikna98.free.fr/esipe/ing1/pds/dev/ 
_Vous trouverez dans chaque dossier commençant par la lettre 'v' suivi d'un numéro de version, le .jar 'service' (qui fera le lien entre la base de données et l'interface graphique du client) et le .jar 'gui' qui est l'interface graphique pour le client_

# Se connecter a l'environnement de production
## Connexion a la base de donnnes de production
1. Lancer les machines virtuelles *MONITRACK_NETWORK* et *MONITRACK_BDD_PROD*
2. Une fois ces machines virtuelles allumées, lancer la commande suivante sur la machine virtuelle MONITRACK_NETWORK :

```
toto@ubuntu:~$ sudo iptables-restore < /etc/iptables_bdd_prod.rules
```
*Cette commande aura pour but de nous permettre d'accéder à la base de données se situant sur une autre machine virtuelle d'un réseau privée*

3. Se rendre sur un navigateur web et y écrire l'url suivante **192.168.20.15:80**. Vous devriez obtenir la page suivante :
![image](https://drive.google.com/uc?export=view&id=1CP_kG0c1NnjNVd9AC00fHqbvkcldTUHe)

## Connexion au serveur de production
1. Lancer les machines virtuelles *MONITRACK_NETWORK*, *MONITRACK_SERVICE_PROD* et *MONITRACK_BDD_PROD*
2. Une fois ces machines virtuelles allumées, lancer la commande suivante sur la machine virtuelle MONITRACK_NETWORK :

```
toto@ubuntu:~$ sudo iptables-restore < /etc/iptables_service_prod.rules
```
3. Télécharger sur lA machine virtuelle *MONITRACK_SERVICE_PROD* la dernière version du projet disponible sur le site : http://cheikna98.free.fr/esipe/ing1/pds/prod/ 
_Vous trouverez dans chaque dossier commençant par la lettre 'r' suivi d'un numéro de release, le .jar 'service' (qui fera le lien entre la base de données et l'interface graphique du client) et le .jar 'gui' qui est l'interface graphique pour le client_

# Scenarios de demonstration
## Release 1 (R1)
1. [Se connecter à la base de donnees de production](#connexion-a-la-base-de-donnnes-de-production).
2. Ouvrir, l'IHM sur un ordinateur (en local).
3. Remplir le champ nom et appuyer sur le bouton "valider" (ce qui devrait envoyer une requête à la base de données).
4. Refaire l'étape 3 avec plusieurs noms différents.
5. Appuyer sur le bouton "Tout voir".
6. Une liste avec tous les noms (ainsi que leurs dates d'enregistrement) qui ont été saisis précédemment devrait apparaître dans la zone de texte en-dessous du champ nom.


## Release 2 (R2)
### Pour cette release, il y aura 2 connexions dans le pool de connexions
1. [Se connecter au serveur de production](#connexion-au-serveur-de-production).
2. Lancer monitrack-service-vm-2.jar sur la machine virtuelle *MONITRACK_SERVICE_PROD* en utilisant les commandes suivantes :

```
toto@ubuntu:~$ cd Bureau
toto@ubuntu:~/Bureau$ java -jar monitrack-service-vm-2.jar
```
3. Pendant que les connexions se créées, ouvrir le .jar monitrack-gui-vm-2.jar sur la machine locale.
**Le fichier .jar est téléchargeable [ici](http://cheikna98.free.fr/esipe/ing1/pds/prod/r2/monitrack-gui-vm-2.jar)**
4. Une fois les connexions créées sur la machine virtuelle, un message devrait s'afficher : _Waiting for a client's request..._
5. Sur l'application client, appuyer sur le bouton _Accéder à la page d'accueil_.
6. Effectuer les opérations **_d'ajout_, _de mise à jour_, _de suppression_ et _de visualisation_**.
7. Ouvrir l'IHM sur 2 autres machines et effectuer les mêmes opérations qu'à l'étape 6.
8. Afficher tous les éléments et on devrait voir ce que les autres clients ont également fait.
9. Sur deux des trois machines clients (on les nommera **machine 1** et **machine 2**), se mettre en mode administrateur en cliquant sur le bouton en haut à gauche et entrer le mot de passe.
10. Sur la troisième machine (que l'on nommera **machine 3**), choisir l'action de _Visualisation_ dans la première combobox.

**Attention : les opérations suivantes nécessitent d'être rapide**

11. Sur les **machine 1** et **machine 2**, appuyer le deuxième bouton (avec une image de boucle infinie) en haut à gauche. Ainsi, une connexion sera réservée par la machine 1 et une autre par la machine 2.
12. Sur la **machine 3**, appuyer sur le bouton de visualisation nommé _Visualiser_.
13. Au bout de quelques secondes, sur la machine 3, une exception sera levée et un message sera affiché au client car aucune connexion n'est disponible.
14. Présenter les tests effectués.

# Si des erreurs apparaissent

- En cas de problèmes lors de l'exportation du projet Maven en fichier .jar, vous pouvez consulter le site suivant :
https://www.codejava.net/coding/how-to-create-executable-jar-file-with-resources-and-dependencies-using-maven-in-eclipsee
- "Error when trying to fetch or push" (Source : https://stackoverflow.com/questions/22824170/eclipse-egit-error-when-trying-to-fetch-or-push) 
	Clique-droit sur le projet -> Team -> Remote -> Configure push to upstream->URI, Change-> Add authentication details

[Revenir en haut de la page](#sommaire)
	
![bas de page](https://drive.google.com/uc?export=view&id=1lVkDmyl7kpe0XZHS8s5atJ4CMEI7vSSa)

