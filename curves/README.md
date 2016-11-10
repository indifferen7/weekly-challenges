# curves

This is my take on the [ninth weekly challenge by Jamis Buck](http://weblog.jamisbuck.org/2016/9/24/weekly-programming-challenge-9.html), which was about drawing quadratic Bezier curves. I decided to settle with normal mode this time as my choice of technology was challenging enough for me. For some time now I've wanted to try out React, but the thought of writing Javascript wasn't all that compelling for some reason. So the combo I used this time around was [ClojureScript](http://clojurescript.org) + [Reagent](https://reagent-project.github.io) + [Figwheel](https://github.com/bhauman/lein-figwheel) to try out reactive stuff with Clojure. I had super fun and the combo worked splendidly! :)

## Overview

Follow the instructions in the Setup section below to fire up the application. Start clicking around to add a, b and c control points in the Bezier algorithm to start drawing! Maybe you will end up with something like this:

![Something](https://dl.dropboxusercontent.com/u/404130/boo.png)

## Setup

To get an interactive development environment run:

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
