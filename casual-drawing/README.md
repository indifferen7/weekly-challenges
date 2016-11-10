# casual-drawing

This project is my take on the [fourth](https://medium.com/@jamis/weekly-programming-challenge-4-7fe42f28d5d4#.l2tr9acd8) and the [eleventh](http://weblog.jamisbuck.org/2016/10/8/weekly-programming-challenge-11.html) weekly programming challenges by Jamis Buck. The fourth challenge was all about drawing lines using Bresenheims algorithm, while the eleventh one was about drawing circles using the Midpoint circle algorithm. These challenges involved creating an api that can be used to draw shapes in varying colors and to save them in PPM format.

## Project structure

The code is divided into two main parts, a) drawing and b) writing the result to file. These parts have been separated in code so that you can skip the PPM part; for a) you include the *casual-drawing.core* namespace and for b) you include the *casual-drawing.ppm* namespace.

## a) Drawing (casual-drawing.core namespace)

### The image
Internally, an image is represented as a map of pixel coordinates and RGB values. An example of an image containing a single pixel (at position x = 5 and y = 5) colored black looks like this:
```
{[5 5] [0 0 0]}
```

### Draw a pixel
The api used to draw a colored pixel in the image at a particular coordinate looks like this:
```
draw-pixel [image color xy] 
```
The provided image is immutable and not altered, instead a new image is returned from the function call. An example call is:
```
(draw-pixel {[3 3] [100 200 100]} [0 0 0] [5 5])
```
The result of the above call is:
```
{[3 3] [100 200 100], 
 [5 5] [0 0 0]}
```
There is also a similar method for drawing several pixels at one, surprisingly named *draw-pixels*. ;)

### Draw a line
Drawing a line is all about drawing pixels, so the way to draw a line is to feed the function *draw-pixels* with the result of a call to the function:
```
new-line [start end]
```
Start and end are x and y coordinate pairs, e.g. [0 0] and [5 5]. The result from a call to this function is a vector of coordinates that make up the line between start and end, using Bresenhams algorithm. The pixels can be drawn using the *draw-pixels*, like so:
```
(draw-pixels {} [255 0 255] (new-line [0 0] [3 3]))
```
So in the above code we feed an empty image, a color and a line into the *draw-pixels* function. The resulting image then looks like:
```
{[0 0] [255 0 255], 
 [1 1] [255 0 255], 
 [2 2] [255 0 255], 
 [3 3] [255 0 255]}
``` 

### Draw a circle
Drawing a circle is not much different from drawing a line, so we need to feed the *draw-pixels* function with the result of the below function:
```
new-circle [midpoint radius]
```
Midpoint is the centre x and y coordinate pair of the circle we want to draw, and the radius is an number of the radius of the circle. An example call involving *draw-pixels* would then look like:

```
(draw-pixels {} [255 0 255] (new-circle [5 5] 2))
```
And the result will be something like the following:

```
{[7 6] [255 0 255],
 [4 3] [255 0 255],
 [6 7] [255 0 255],
 [7 4] [255 0 255],
 [6 3] [255 0 255],
 [3 4] [255 0 255],
 [5 3] [255 0 255],
 [4 7] [255 0 255],
 [5 7] [255 0 255],
 [3 6] [255 0 255],
 [7 5] [255 0 255],
 [3 5] [255 0 255]}
```

### Draw a styled shape
To draw a dotted or styled shape, e.g. a line or a circle, you can pass the result from the *new-line* function call through either the *dot* or *dash* functions, like so:
```
(draw-pixels {} [255 0 255] (dot (new-line [0 0] [6 6])))
```
The resulting image then is:
```
{[0 0] [255 0 255], 
 [2 2] [255 0 255], 
 [4 4] [255 0 255], 
 [6 6] [255 0 255]}
```
## b) PPM (casual-drawing.ppm namespace)

### Output the image in PPM (P3) format
The following code snippet saves a set of lines into a PPM file (default name if not provided is "image.ppm"). For this example code to work, you will need to require the *casual-drawing.core* namespace, as it contains functions such as *new-line*.
```
(save
  (-> {}
      (draw-pixels [255 0 255] (new-line [400 0] [400 500]))
      (draw-pixels [255 255  0] (new-line [0 0] [600 500]))
      (draw-pixels [0 0 0] (dash (new-line [100 100] [600 100])))
      (draw-pixels [50 50 50] (dot (new-line [300 300] [600 300])))
      (draw-pixels [150 50 149] (dot (new-circle [300 300] 200)))
      (draw-pixels [0 0 0] (new-circle [200 200] 180))
      (draw-pixels [0 0 0] (dash (new-circle [200 200] 150)))))
```

The resulting image will look like this (not really using PPM here as the image would be really large):

![True art!](https://dl.dropboxusercontent.com/u/404130/casualmap/example.png)
