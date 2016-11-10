# Casual maze solver
This is my take on the [Weekly Programming Challenge #3](https://medium.com/@jamis/weekly-programming-challenge-3-932b16ddd957#.ka3a9ctkb). This turned out to be quite an obstacle for me as I decided to tackle both Clojure and A* (for pathfinding), both of which I am novice in. Clojurists around the world will probably sigh when they see parts of the code, but you are all welcome to contribute with a pull request or two. ;)

Anyway, the whole idea of this challenge was to find the path between the start (O) and end (X) in a maze, such as this one below:
```
###########
#         #
# ##### ###
# #   #   #
# # # ### #
# # #     #
# # ##### #
# # #   # #
# # # # # #
# # # #   #
#O#X#######
```

All maps provided in the challenge can be found in the mazes/ directory in the code base. Through the A* algorithm (well maybe not perfectly A* but close enough) the code will find the shortest path from start to end even if there are several correct paths to choose from. Now let's move on to usage.

## Usage

To solve a map you can run the code using Leinigen as following:
```
lein run <path-to-file>
```
An example would then be:
```
lein run mazes/maze-hard-002.txt 
```
Another option is to run the command:
```
lein uberjar
```
Which creates a stand-alone jar. An example of how to run the program is:
```
java -jar target/maze-0.1.0-SNAPSHOT-standalone.jar mazes/maze-normal-005.txt
```

Super useful stuff - have fun!

## License
MIT. Go crazy! :)
