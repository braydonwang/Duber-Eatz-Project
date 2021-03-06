# Duber Eatz Project

![Project Image](https://braydonwang.github.io/dubereatz.png)

> This is my Duber Eatz Project created for my ICS3U6 course

---

## Description

A program designed to help a pizza delivery driver find the correct path in a maze sent as a text file. All three of the programs in the repository use recursive BFS, but vary in difficulty from simply finding the shortest path to having to consider microwaves to heat the pizza along the way. In the end, the path the driver took is displayed in a ppm file.

### In-depth Overview

- The first two values of the text file are the number of rows and columns of the grid
- "#" represents walls
- S represents starting point
- Numbers (1-9) represent destinations, the lower the number, the higher its priority

#### Example of Test Case

```
21
21
#####################
#   #    S    #   # #
# # # ####### # ### #
# # #     #       # #
# # # ### ### # ### #
# #     #   # # #   #
# # ####### # ### ###
#1# # #   # # #     #
### # # ##### # #####
# #         # # #   #
# # # ##### ### # ###
#   #1#   #         #
### ##### ### #######
#       # # #     # #
### ##### # # # # # #
#   #   #     # #  1#
# ##### ### ### #####
#       # # #     # #
### # # # ### ##### #
# 1 # #   #         #
#####################
```

#### PPM File Output

![TestCasePPM](https://github.com/braydonwang/Duber-Eatz-Project/blob/main/testcase3.png)

- Green represents the starting point
- Red represents the path
- Blue represents the destinations

#### Language

- Java
