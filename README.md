## CTM SpeedrunIGT Integration
category files go in `.minecraft/speedrunigt/ctm-srigt-integration` folder (file names do not matter, it will try to parse anything in the folder)

if it doesn't work at first, make sure the right category is selected
### category specifications
- line 1: category id
- line 2: category display name
- all following lines: criteria for completion
  1. [block identifier](https://minecraft.fandom.com/wiki/Resource_location)
  2. (optional, 1.7-1.12 only) block data value
     - used for wool colors, etc
  3. x, y, z position
- if the spec for a category file is broken, an error will be thrown in the logs with a hopefully helpful description of what went wrong

example 1 (targetted at 1.8.9):
```
spellbound_caves
Spellbound Caves
wool -477 106 608
wool 1 -478 106 608
wool 2 -479 106 608
wool 3 -480 106 608
wool 4 -481 106 608
wool 5 -482 106 608
wool 6 -483 106 608
wool 7 -484 106 608
wool 8 -485 106 608
wool 9 -486 106 608
wool 10 -487 106 608
wool 11 -488 106 608
wool 12 -489 106 608
wool 13 -490 106 608
wool 14 -491 106 608
wool 15 -492 106 608
iron_block -493 106 608
gold_block -494 106 608
diamond_block -495 106 608
```
in this example, the optional `minecraft:` namespace is omitted

example 2 (targetted at 1.19.4):
```
take_the_stairs
Take the Stairs
minecraft:yellow_stained_glass -48 -59 63
```
since this is post-1.12, there are no data values

if you have any issue or questions, you can dm me on discord at `tildejustin#4317`\
have fun running ^-^
