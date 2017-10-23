# Takeaway's Game Of 3
Any Whole Number can be divisible by 3 either adding -1 or 0 or 1.
This Game let you see the Magic

## Requirements
- Java 8
- Maven 

## How to build the Game
```
mvn clean package
```
## Run Java standalone executable
Run this command in two separate terminal for 2 player. 
```
java -jar target/game-of-three-0.1.0-SNAPSHOT-jar-with-dependencies.jar 
```

## Game Options After Start
````
  ___   __   _  _  ____     __  ____    ____ 
 / __) / _\ ( \/ )(  __)   /  \(  __)  ( __ \
( (_ \/    \/ \/ \ ) _)   (  O )) _)    (__ (
 \___/\_/\_/\_)(_/(____)   \__/(__)    (____/

#########--MENU--#########
-------------------------

1 - Initiate the Game to play with other player automatically.
2 - Initiate the Game to play as User.
3 - Start the Game and wait for other player to initiate.
4 - Start and initiate the Game, if player is not available wait for other player to initiate.
5 - Quit.

````
Note : - Selecting any option will prompt for port input to start the game as receiver or to connect with other player or both.
## Cobertura Code Coverage
```
mvn cobertura:cobertura
```
Code Coverage result will be in target/site/index.html
