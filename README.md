# macOS-Clean-Tool
A tool for clean the time machine snap.

## Why we need remove snaps
Time machine will keep automatically generate snaps, which usually can take a lot of HDD space and almost of them are useless.
This tool can search snaps on Mac and remove them or users able to pick specific snap to remove.

## How to use
This tool has command line ver and also with UI (working on it now).
<br> 
For the command line ver: 
<br>Download this project and run the cleanUtilCLT class file in terminal. Like below:

#### scan
    java cleanUtilCLT
    or
    java cleanUtilCLT -s
    
#### remove all snaps at once
    java cleanUtilCLT -d
    
#### remove specific snap
    java cleanUtilCLT -d [timestamp]
    
    
