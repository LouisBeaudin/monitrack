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
- [Generation de l�artefact JAVA : JAR](#generation-de-l-artefact-java-jar)
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
1. La branche master ne sera utilis�e que pour faire des "merge" dessus. Par cons�quent, il faut �viter au maximum de modifier directement les fichiers � partir de la branche master, il faut [cr�er des branches](#creation-de-branches-pour-les-evolutions).
2. Dans certains dossiers, vous trouverez un fichier oneFileIsRequired.txt. Vous pourrez supprimer ces fichiers lorsque le dossier contiendra au moins un autre fichier (par exemple un fichier .java). Si vous supprimez ce fichier .txt et que le dossier devient vide, alors le dossier ne pourra pas �tre 'commit' ce qui pourra entra�ner des erreurs de compilation.
3. N'oubliez pas de faire des "Fetch" et des "Pull" lorsque vous changez de branche.
4. N'oubliez pas de faire des commits r�guliers afin que les autres membres du groupe puissent connaitre votre avancement.

## Important
**Si vous avez clon� le projet _monitrack_ avant le 20 mars 2019, cette section est pour vous, sinon vous pouvez la passer.**
Lorsque vous ferez un push, vous verrez s�rement les dossiers **monitrackCommons**, **monitrackGUI** et **monitrackService** en plus des dossiers **monitrack-commons**, **monitrack-gui** et **monitrack-service** comme sur l'image ci-dessous :
![image](https://drive.google.com/uc?export=view&id=19JF2dXPtEaYFveyLclB02l6P9P7j_bt7) 

Si c'est le cas vous pourrez alors supprimer les dossiers **monitrackCommons**, **monitrackGUI** et **monitrackService** de votre disque dur.
Enfin, importer les projets **monitrack-commons**, **monitrack-gui** et **monitrack-service** (qui sont tous des projets Maven) dans votre _Workspace_.
Faites �galement un _Maven > Update Project_ si des erreurs apparaissent.

# Quelques conventions de nommages
- Le nom des variables ainsi que le nom des m�thodes seront en anglais
- Le nom des interfaces java d�butera toujours par un 'I'
- Pour les conventions de nommage des packages rendez-vous sur : 
https://openclassrooms.com/fr/courses/26832-apprenez-a-programmer-en-java/21583-les-packages

# Quelques conseils
- Dans les cas o� vous devez concat�ner plusieurs plusieurs variables dans une cha�ne de caract�res, la m�thode **String.format(format, args)** peut �tre utile. Voici un exemple :

```java
int num = 1;
String firstName = "John";
String lastName = "Doe";
String str = String.format("%d) My name is %s %s", num, firstName, lastName);
System.out.println(str);

Sortie : 1) My name is John Doe
```

# Organisation du projet Maven
Pour le moment, le projet Maven se d�coupe en 3 modules distincts :

**- monitrack-commons :** contient tous les �l�ments communs aux deux modules ci-dessus comme les entit�s, des m�thodes utiles qui permettent de lire dans un fichier ou encore des classes qui permettent de s�rialiser (passage d'un objet JAVA � du JSON) ou de les des�rialiser (passer du JSON � un objet JAVA)
	
**- monitrack-gui :** contient tous les �l�ments qui seront destin�s � la r�alisation de l'interface graphique pour le client (comme les JPanel, JFrame, ...)
	
**- monitrack-service :** contient tous les �l�ments qui vont nous permettre d'acc�der � la base de donn�es et d'effectuer des requ�tes (r�cup�ration de donn�es, ajout, suppression,...)

# Creation de branches pour les evolutions
1. Cliquez sur le bouton + (situ� � c�t� du nom de la branche courante)
2. Cliquez sur 'New branch'
3. Vous serez rediriger vers une fen�tre qui vous demandera d'indiquer le nom de votre nouvelle branche. Ce nom doit �tre le nom de l'�volution de mani�re succincte et les mots seront s�par�s par des underscores. Vous devrez �galement s�lectionner la branche  partir de laquelle vous voulez cr�er la branche : v�rifiez que c'est bien la branche master (ou une autre branche si elle est bien � jour)
4. Puis pour travailler sur cette branche dans Eclipse, [mettez � jour le projet](#mettre-a-jour-son-repertoire-git-local-avec-le-repertoire-distant) et [changez de branche](#changer-de-branche-git-dans-eclipse).

# Importer le projet git dans Eclipse
1. Copiez le lien suivant : https://gitlab.com/climg/monitrack.git (Ce lien appara�t lorsque vous cliquez sur le bouton clone en haut � droite de la page puis dans la partie 'Clone with HTTPS')
2. Rendez-vous dans Eclipse
3. Cliquez sur Window > Perspective > Open Perspective > Git
4. Cliquez sur Clone (soit le petit nuage avec une fl�che verte ou alors un lien qui s'affiche)
5. Dans l'URI, collez le lien que vous avez copi� pr�c�demment.
6. Renseignez votre user (prenom.nom) et votre password.
7. S�lectionnez toutes les branches, puis faites 'Next'.
8. Choisissez l'emplacement du projet, puis faites'Finish'.
9. Une fois que le projet appara�t dans la fen�tre 'Git Repositories', faites une clic-droit sur le projet puis 'Import projects'

# Changer de branche git dans Eclipse
1. Assurez-vous d'avoir [mis � jour votre r�pertoire avec le r�pertoire distant] #mettre-a-jour-son-repertoire-git-local-avec-le-repertoire-distant)
2. Dans 'Project Explorer', faites un clic-droit sur 'monitrack', puis Team > Switch To > Other

	- Si vous n'avez pas utilis� la branche dans le projet :
		-> cherchez dans le dossier 'Local'	et la s�lectionner
	- Si vous avez d�j� utilis� la branche : 
		-> cherchez dans le dossier 'Remote Tracking'
		-> S�lectionnez la branche puis cliquez sur 'Checkout as a new Local Branch'
3. Faites un clic-droit sur monitrack > Maven > Update Project
4. Puis clic-droit sur monitrack > Run As > Maven Clean
5. Puis clic-droit sur monitrack > Run As > Maven Install
6. Vous pouvez ensuite travailler sur cette branche

# Mettre a jour son repertoire git local avec le repertoire distant
1. Dans 'Project Explorer', faites un clic-droit sur 'monitrack', puis Team > Pull

# Fusionner les modifications faites sur une autre branche avec la branche courante
1. Assurez-vous d'avoir [mis � jour votre r�pertoire avec le r�pertoire distant](#mettre-a-jour-son-repertoire-git-local-avec-le-repertoire-distant)
2. Puis clic-droit sur 'monitrack', Team > Merge
3. S�lectionnez la branche que vous voulez copier
4. Cliquez sur 'Merge'
5. Si des conflits apparaissent, r�glez-les en conservant les parties que vous souhaitez garder
6. Enfin clic-droit sur 'monitrack', Team > Push To Upstream

# Generation de l�artefact JAVA (JAR)
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
						<mainClass>[package de la classe contenant la m�thode main].[nom
							de la classe contenant la m�thode main]</mainClass>
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

2. Cliquer sur **Maven Build (1)** dans la liste des configurations, puis sur l�ic�ne **New launch configuration (2)**
![image](https://drive.google.com/uc?export=view&id=1QFu-CEjMZTYxUphX74TC4voqSggsJYMZ)

3. Une nouvelle fen�tre s�affiche _(voir photo ci-dessous)_
![image](https://drive.google.com/uc?export=view&id=1djItXE_SBkXEtn5b1mg_Jiqyr1KhLH8H)

4. Appuyer sur le bouton ��Workspace�� o� vous serez invit� � choisir le module Maven (contenant une m�thode ��main�� principale que vous voudrez exporter au format .jar
Choisir le module correspondant. Dans notre cas, nous aurons � choisir un des 2 modules suivants :
	- le module monitrackGUI pour le client JAVA
	- le module monirtackService pour tester le pool de connexions
_Dans notre cas nous avons choisi le module monitrackGUI_
![image](https://drive.google.com/uc?export=view&id=1mYVyanwlR8x5VxOpUoGOTSphaBOjtra0)

5. Dans la partie goals, y �crire :

```
clean assembly:single
```
![image](https://drive.google.com/uc?export=view&id=13ITCwIcN86TdpqeYqLgScUV-zd9kkOsC)

6. N'h�sitez pas � changer le nom de la configuration afin de faciliter les Run ult�rieurs
7. Appuyez sur le bouton "Apply" en bas, puis Run.
8. Sur la console devrait s'afficher les diff�rentes �tapes
9. Une fois que le "Run" a termin� avec succ�s, vous pourrez trouver votre fichier .jar g�n�r� en suivant le chemin indiqu� apr�s "Building jar"
![image](https://drive.google.com/uc?export=view&id=1RPDmLLlS__I3e628636povbH-AkUbaH_)
 


### Generation en ligne de commande
1. Assurez-vous d'avoir install� Maven sur votre ordinateur
2. Rendez- vous dans le dossier du module du projet que vous voulez g�n�rer en .jar
3. Ouvrez la console en faisant "Shift + Clic-Droit" dans le dossier courant
4. Lancer la commande suivante :

```
D:\GitHub\PDS_ESIPE_CYCLE_INGENIEUR\monitrack\monitrackGUI> mvn clean compile assembly:single
```
![image](https://drive.google.com/uc?export=view&id=13GuC_9-UF1D1ohc8cM2zj-ORsN1_8QAz)

5. Enfin pour trouver le fichier .jar g�n�r�, il vous suffira de suivre le chemin indiqu� apr�s "Building jar" ou alors vous pouvez ex�cuter la commande suivante :

```
D:\GitHub\PDS_ESIPE_CYCLE_INGENIEUR\monitrack\monitrackGUI> java -jar <chemin complet du jar indiqu� apr�s "Building jar">
```
![image](https://drive.google.com/uc?export=view&id=1ZCq0y3x-RnhTh-vBt6nWiXexaDG1ODuY)

**Attention : Si vous d�cidez d'ouvrir l'artefact jar � partir de la l'invite de commande (ou le PowerShell) , fa�tes tr�s attention car si vous fermer votre invite de commande alors l'application se fermera aussi.**

# Se connecter a l'environnement de developpement
**Attention : N'oubliez pas de v�rifier que vous �tre en train d'utiliser l'environnement de d�veloppement pour les tests et l'environnement de production pour les d�monstrations. Lisez la suite pour savoir si vous utilisez le bon environnement ***

## Connexion a la base de donnnes de developpement
1. Lancer les machines virtuelles *MONITRACK_NETWORK* et *MONITRACK_BDD_DEV*
2. Une fois ces machines virtuelles allum�es, lancer la commande suivante sur la machine virtuelle MONITRACK_NETWORK :

```
toto@ubuntu:~$ sudo iptables-restore < /etc/iptables_bdd_dev.rules
```
*Cette commande aura pour but de nous permettre d'acc�der � la base de donn�es se situant sur une autre machine virtuelle d'un r�seau priv�e*

3. Se rendre sur un navigateur web et y �crire l'url suivante **192.168.20.15:80**. Vous devriez obtenir la page suivante :
![image](https://drive.google.com/uc?export=view&id=1_L_TRi8bQtcGEBWAHazDPLvyP7W0QI_7)

## Connexion au serveur de developpement
1. Lancer les machines virtuelles *MONITRACK_NETWORK*, *MONITRACK_SERVICE_DEV* et *MONITRACK_BDD_DEV*
2. Une fois ces machines virtuelles allum�es, lancer la commande suivante sur la machine virtuelle MONITRACK_NETWORK :

```
toto@ubuntu:~$ sudo iptables-restore < /etc/iptables_service_dev.rules
```
3. T�l�charger sur le machine virtuelle *MONITRACK_SERVICE_DEV* la derni�re version du projet disponible sur le site : http://cheikna98.free.fr/esipe/ing1/pds/dev/ 
_Vous trouverez dans chaque dossier commen�ant par la lettre 'v' suivi d'un num�ro de version, le .jar 'service' (qui fera le lien entre la base de donn�es et l'interface graphique du client) et le .jar 'gui' qui est l'interface graphique pour le client_

# Se connecter a l'environnement de production
## Connexion a la base de donnnes de production
1. Lancer les machines virtuelles *MONITRACK_NETWORK* et *MONITRACK_BDD_PROD*
2. Une fois ces machines virtuelles allum�es, lancer la commande suivante sur la machine virtuelle MONITRACK_NETWORK :

```
toto@ubuntu:~$ sudo iptables-restore < /etc/iptables_bdd_prod.rules
```
*Cette commande aura pour but de nous permettre d'acc�der � la base de donn�es se situant sur une autre machine virtuelle d'un r�seau priv�e*

3. Se rendre sur un navigateur web et y �crire l'url suivante **192.168.20.15:80**. Vous devriez obtenir la page suivante :
![image](https://drive.google.com/uc?export=view&id=1CP_kG0c1NnjNVd9AC00fHqbvkcldTUHe)

## Connexion au serveur de production
1. Lancer les machines virtuelles *MONITRACK_NETWORK*, *MONITRACK_SERVICE_PROD* et *MONITRACK_BDD_PROD*
2. Une fois ces machines virtuelles allum�es, lancer la commande suivante sur la machine virtuelle MONITRACK_NETWORK :

```
toto@ubuntu:~$ sudo iptables-restore < /etc/iptables_service_prod.rules
```
3. T�l�charger sur lA machine virtuelle *MONITRACK_SERVICE_PROD* la derni�re version du projet disponible sur le site : http://cheikna98.free.fr/esipe/ing1/pds/prod/ 
_Vous trouverez dans chaque dossier commen�ant par la lettre 'r' suivi d'un num�ro de release, le .jar 'service' (qui fera le lien entre la base de donn�es et l'interface graphique du client) et le .jar 'gui' qui est l'interface graphique pour le client_

# Scenarios de demonstration
## Release 1 (R1)
1. [Se connecter � la base de donnees de production](#connexion-a-la-base-de-donnnes-de-production).
2. Ouvrir, l'IHM sur un ordinateur (en local).
3. Remplir le champ nom et appuyer sur le bouton "valider" (ce qui devrait envoyer une requ�te � la base de donn�es).
4. Refaire l'�tape 3 avec plusieurs noms diff�rents.
5. Appuyer sur le bouton "Tout voir".
6. Une liste avec tous les noms (ainsi que leurs dates d'enregistrement) qui ont �t� saisis pr�c�demment devrait appara�tre dans la zone de texte en-dessous du champ nom.


## Release 2 (R2)
### Pour cette release, il y aura 2 connexions dans le pool de connexions
1. [Se connecter au serveur de production](#connexion-au-serveur-de-production).
2. Lancer monitrack-service-vm-2.jar sur la machine virtuelle *MONITRACK_SERVICE_PROD* en utilisant les commandes suivantes :

```
toto@ubuntu:~$ cd Bureau
toto@ubuntu:~/Bureau$ java -jar monitrack-service-vm-2.jar
```
3. Pendant que les connexions se cr��es, ouvrir le .jar monitrack-gui-vm-2.jar sur la machine locale.
**Le fichier .jar est t�l�chargeable [ici](http://cheikna98.free.fr/esipe/ing1/pds/prod/r2/monitrack-gui-vm-2.jar)**
4. Une fois les connexions cr��es sur la machine virtuelle, un message devrait s'afficher : _Waiting for a client's request..._
5. Sur l'application client, appuyer sur le bouton _Acc�der � la page d'accueil_.
6. Effectuer les op�rations **_d'ajout_, _de mise � jour_, _de suppression_ et _de visualisation_**.
7. Ouvrir l'IHM sur 2 autres machines et effectuer les m�mes op�rations qu'� l'�tape 6.
8. Afficher tous les �l�ments et on devrait voir ce que les autres clients ont �galement fait.
9. Sur deux des trois machines clients (on les nommera **machine 1** et **machine 2**), se mettre en mode administrateur en cliquant sur le bouton en haut � gauche et entrer le mot de passe.
10. Sur la troisi�me machine (que l'on nommera **machine 3**), choisir l'action de _Visualisation_ dans la premi�re combobox.

**Attention : les op�rations suivantes n�cessitent d'�tre rapide**

11. Sur les **machine 1** et **machine 2**, appuyer le deuxi�me bouton (avec une image de boucle infinie) en haut � gauche. Ainsi, une connexion sera r�serv�e par la machine 1 et une autre par la machine 2.
12. Sur la **machine 3**, appuyer sur le bouton de visualisation nomm� _Visualiser_.
13. Au bout de quelques secondes, sur la machine 3, une exception sera lev�e et un message sera affich� au client car aucune connexion n'est disponible.
14. Pr�senter les tests effectu�s.

# Si des erreurs apparaissent

- En cas de probl�mes lors de l'exportation du projet Maven en fichier .jar, vous pouvez consulter le site suivant :
https://www.codejava.net/coding/how-to-create-executable-jar-file-with-resources-and-dependencies-using-maven-in-eclipsee
- "Error when trying to fetch or push" (Source : https://stackoverflow.com/questions/22824170/eclipse-egit-error-when-trying-to-fetch-or-push) 
	Clique-droit sur le projet -> Team -> Remote -> Configure push to upstream->URI, Change-> Add authentication details

[Revenir en haut de la page](#sommaire)
	
![bas de page](https://drive.google.com/uc?export=view&id=1lVkDmyl7kpe0XZHS8s5atJ4CMEI7vSSa)

