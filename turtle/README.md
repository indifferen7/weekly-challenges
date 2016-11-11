# turtle graphics

This code is my take on [the fifteenth challenge](http://weblog.jamisbuck.org/2016/11/5/weekly-programming-challenge-15.html) by Jamis Buck. This time around the task at hand was to create code for drawing [turtle graphics](https://en.wikipedia.org/wiki/Turtle_graphics). Once again I decided to give ClojureScript a go. All in all a super fun challenge!

## What can it do?
Check out this screenshot to see a sample image drawn using SVG. 
![Something](https://dl.dropboxusercontent.com/u/404130/turtle/example.png)

There is also [an interactive demo over here](http://casualsemantics.se/turtle). Read on to get some more info on how to use the turtle (and the demo).

## Overview of the code
The code is divided into two main parts:

a) The turtle (located in the file turtle.cljs)

b) The demo code (located in core.cljs)

While the demo is fancy and all it serves only as a showcase for the turtle code, so let's dig a bit more into how to use the turtle.

## The turtle
The turtle has two public functions that can be used, called "new-turtle" and "process". 

### new-turtle [x y]
This functions creates a new turtle located at the provided x and y coordinates. A call to this function in the REPL can look like the following:

```zsh
turtle.turtle=> (new-turtle [5 5])
{:pos [5 5], :degrees 0, :down? true, :color "black", :width 2, :style "solid"}
```
The result contains the current state of the turtle with quite a lot of properties set. Let's talk about them one by one.

#### :pos
The x and y coordinates the turtle is currently located in.

#### :degrees
The current orientation of the turtle. Zero degrees means that the turtle is facing up.

#### :down?
Tells whether or not the turtle is placed on the surface. The turtle must be placed in order two produce any lines while moving.

#### :color
The color of the lines produced by the turtle.

#### :width
The width of the lines produced by the turtle.

#### :style
The style of the lines produced by the turtle. Supported styles are "solid", "dotted" and "dashed".

Now that we know what the turtle looks it's time to see how to make use of it.

### process [turtle actions]
This function takes a turtle and a vector of actions, interprets the actions and returns the new turtle state and all lines produced during the process. The function is pure; the new turtle state is returned by the function. A call to this function in a REPL might look like the following:

```zsh
turtle.turtle=> (process (new-turtle [5 5]) [{:type :move :distance 10}])
{:turtle {:pos [5 -5], :degrees 0, :down? true, :color "black", :width 2, :style "solid"}, :result [{:from [5 5], :to [5 -5], :color "black", :width 2, :style "solid"}]}
```
Alright, a lot is going on here so let's break things down. The first argument to the function is a freshly created turtle. Our second argument is a vector of maps, where each map represents an action that the turtle should perform. In this case, the turtle should move 10 steps in the current direction it's facing. As the starting degrees of a new turtle is 0 - which means it's facing upward - it will walk ten steps in that direction from position [5 5]. Looking at the result, the resulting turtle is now at coordinate [5 -5].

The second map key, :result, contains all the lines produced during the turtle walk. Each line is represented as a map, with its from and to coordinates are found in the :from and :to keys respectively. The other keys in the result have been explained above. As you can see, the current state of the turtle is reflected in the resulting lines. It is then up to the calling code to interpret and render the result in one way or another.

## Available actions
The following actions are supported:

### Lift the turtle up
```zsh
{:type :up}
```
Lifts the turtle up. From now on, no lines will be created when the turtle moves.
### Put the turtle down
```zsh
{:type :down}
```
Puts the turtle down. From now on, lines will be created when the turtle moves.
### Move the turtle
```zsh
{:type :move :distance 10}
```
Moves the turtle a given distance. In the example above, the turtle moves 10 steps.
### Turn the turtle
```zsh
{:type :turn :degrees 90}
```
Turns the turtle by the given degrees. In the example above, the turtle turns 90 degrees.
### Change line color
```zsh
{:type :color :color "red"}
```
Changes the line color drawn by the turtle. In the example above, red lines will be drawn from now on.
### Change line width
```zsh
{:type :width :width 4}
```
Changes the line width drawn by the turtle. In the example above, lines with the width 4 will be drawn.
### Change line style
```zsh
{:type :style :style "dotted"}
```
Changes the line style drawn by the turtle. In the example above, dotted lines will be drawn by the turtle from now on. Supported values are "solid", "dotted" and "dashed".
## Setup the demo environment locally

To get an interactive development environment (and to run the demo locally) run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL.
